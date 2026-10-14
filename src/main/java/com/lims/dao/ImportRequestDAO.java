package com.lims.dao;

import com.lims.model.ImportRequest;
import com.lims.model.ImportRequestDetail;
import com.lims.util.DBContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ImportRequestDAO {

    private static final Logger LOGGER = Logger.getLogger(ImportRequestDAO.class.getName());

    public List<ImportRequest> getImportRequests(String status, int page, int pageSize) {
        List<ImportRequest> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ir.ImportRequestID, ir.SupplierID, ir.CreatedBy, ir.RequestDate, ir.ApprovedBy, ir.ApprovedDate, ir.Status, ir.CancelReason, ")
           .append("s.SupplierName, u1.FullName AS CreatorName, u2.FullName AS ApproverName ")
           .append("FROM IMPORT_REQUESTS ir ")
           .append("INNER JOIN SUPPLIERS s ON ir.SupplierID = s.SupplierID ")
           .append("INNER JOIN USERS u1 ON ir.CreatedBy = u1.UserID ")
           .append("LEFT JOIN USERS u2 ON ir.ApprovedBy = u2.UserID ")
           .append("WHERE 1=1 ");

        List<Object> params = new ArrayList<>();
        if (status != null && !status.trim().isEmpty() && !status.equalsIgnoreCase("ALL")) {
            sql.append("AND ir.Status = ? ");
            params.add(status.trim());
        }

        sql.append("ORDER BY ir.ImportRequestID DESC ")
           .append("OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");

        int offset = Math.max(0, (page - 1) * pageSize);
        params.add(offset);
        params.add(pageSize);

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSet(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching import requests", e);
        }
        return list;
    }

    public ImportRequest findById(int id) {
        String sql = "SELECT ir.ImportRequestID, ir.SupplierID, ir.CreatedBy, ir.RequestDate, ir.ApprovedBy, ir.ApprovedDate, ir.Status, ir.CancelReason, " +
                     "s.SupplierName, u1.FullName AS CreatorName, u2.FullName AS ApproverName " +
                     "FROM IMPORT_REQUESTS ir " +
                     "INNER JOIN SUPPLIERS s ON ir.SupplierID = s.SupplierID " +
                     "INNER JOIN USERS u1 ON ir.CreatedBy = u1.UserID " +
                     "LEFT JOIN USERS u2 ON ir.ApprovedBy = u2.UserID " +
                     "WHERE ir.ImportRequestID = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ImportRequest req = mapResultSet(rs);
                    req.setDetails(getDetails(id));
                    return req;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding import request: " + id, e);
        }
        return null;
    }

    public List<ImportRequestDetail> getDetails(int importRequestId) {
        List<ImportRequestDetail> list = new ArrayList<>();
        String sql = "SELECT ird.ImportRequestDetailID, ird.ImportRequestID, ird.ProductID, ird.Quantity, ird.UnitPrice, " +
                     "p.ProductCode, p.ProductName " +
                     "FROM IMPORT_REQUEST_DETAILS ird " +
                     "INNER JOIN PRODUCTS p ON ird.ProductID = p.ProductID " +
                     "WHERE ird.ImportRequestID = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, importRequestId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ImportRequestDetail d = new ImportRequestDetail(
                            rs.getInt("ImportRequestDetailID"),
                            rs.getInt("ImportRequestID"),
                            rs.getInt("ProductID"),
                            rs.getInt("Quantity"),
                            rs.getBigDecimal("UnitPrice")
                    );
                    d.setProductCode(rs.getString("ProductCode"));
                    d.setProductName(rs.getString("ProductName"));
                    list.add(d);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching import request details for: " + importRequestId, e);
        }
        return list;
    }

    public boolean createRequest(ImportRequest req, List<ImportRequestDetail> details) {
        String reqSql = "INSERT INTO IMPORT_REQUESTS (SupplierID, CreatedBy, RequestDate, Status) VALUES (?, ?, GETDATE(), 'PENDING')";
        String detailSql = "INSERT INTO IMPORT_REQUEST_DETAILS (ImportRequestID, ProductID, Quantity, UnitPrice) VALUES (?, ?, ?, ?)";

        Connection conn = null;
        try {
            conn = DBContext.getConnection();
            conn.setAutoCommit(false);

            int newId = -1;
            try (PreparedStatement ps = conn.prepareStatement(reqSql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, req.getSupplierId());
                ps.setInt(2, req.getCreatedBy());
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) newId = rs.getInt(1);
                }
            }

            if (newId > 0 && details != null) {
                try (PreparedStatement psDetail = conn.prepareStatement(detailSql)) {
                    for (ImportRequestDetail d : details) {
                        psDetail.setInt(1, newId);
                        psDetail.setInt(2, d.getProductId());
                        psDetail.setInt(3, d.getQuantity());
                        psDetail.setBigDecimal(4, d.getUnitPrice());
                        psDetail.addBatch();
                    }
                    psDetail.executeBatch();
                }
            }

            conn.commit();
            req.setImportRequestId(newId);
            return true;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating import request", e);
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ignored) {}
            }
            return false;
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException ignored) {}
            }
        }
    }

    public boolean updateStatus(int requestId, String newStatus, Integer approverId, String cancelReason) {
        String sql = "UPDATE IMPORT_REQUESTS SET Status = ?, ApprovedBy = ?, ApprovedDate = CASE WHEN ? IN ('APPROVED','REJECTED') THEN GETDATE() ELSE ApprovedDate END, CancelReason = ? WHERE ImportRequestID = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newStatus);
            if (approverId != null) ps.setInt(2, approverId); else ps.setNull(2, Types.INTEGER);
            ps.setString(3, newStatus);
            ps.setString(4, cancelReason);
            ps.setInt(5, requestId);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating import request status: " + requestId, e);
            return false;
        }
    }

    private ImportRequest mapResultSet(ResultSet rs) throws SQLException {
        ImportRequest req = new ImportRequest();
        req.setImportRequestId(rs.getInt("ImportRequestID"));
        req.setSupplierId(rs.getInt("SupplierID"));
        req.setCreatedBy(rs.getInt("CreatedBy"));
        req.setRequestDate(rs.getTimestamp("RequestDate"));
        int approver = rs.getInt("ApprovedBy");
        req.setApprovedBy(rs.wasNull() ? null : approver);
        req.setApprovedDate(rs.getTimestamp("ApprovedDate"));
        req.setStatus(rs.getString("Status"));
        req.setCancelReason(rs.getString("CancelReason"));
        req.setSupplierName(rs.getString("SupplierName"));
        req.setCreatorName(rs.getString("CreatorName"));
        req.setApproverName(rs.getString("ApproverName"));
        return req;
    }
}

