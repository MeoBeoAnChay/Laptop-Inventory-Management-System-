package com.lims.dao;

import com.lims.model.ExportRequest;
import com.lims.model.ExportRequestDetail;
import com.lims.util.DBContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExportRequestDAO {

    private static final Logger LOGGER = Logger.getLogger(ExportRequestDAO.class.getName());

    public List<ExportRequest> getExportRequests(String status, int page, int pageSize) {
        List<ExportRequest> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT er.ExportRequestID, er.CustomerID, er.CreatedBy, er.RequestDate, er.ApprovedBy, er.ApprovedDate, er.Status, er.CancelReason, ")
           .append("c.CustomerName, c.Phone AS CustomerPhone, u1.FullName AS CreatorName, u2.FullName AS ApproverName ")
           .append("FROM EXPORT_REQUESTS er ")
           .append("INNER JOIN CUSTOMERS c ON er.CustomerID = c.CustomerID ")
           .append("INNER JOIN USERS u1 ON er.CreatedBy = u1.UserID ")
           .append("LEFT JOIN USERS u2 ON er.ApprovedBy = u2.UserID ")
           .append("WHERE 1=1 ");

        List<Object> params = new ArrayList<>();
        if (status != null && !status.trim().isEmpty() && !status.equalsIgnoreCase("ALL")) {
            sql.append("AND er.Status = ? ");
            params.add(status.trim());
        }

        sql.append("ORDER BY er.ExportRequestID DESC ")
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
            LOGGER.log(Level.SEVERE, "Error fetching export requests", e);
        }
        return list;
    }

    public ExportRequest findById(int id) {
        String sql = "SELECT er.ExportRequestID, er.CustomerID, er.CreatedBy, er.RequestDate, er.ApprovedBy, er.ApprovedDate, er.Status, er.CancelReason, " +
                     "c.CustomerName, c.Phone AS CustomerPhone, u1.FullName AS CreatorName, u2.FullName AS ApproverName " +
                     "FROM EXPORT_REQUESTS er " +
                     "INNER JOIN CUSTOMERS c ON er.CustomerID = c.CustomerID " +
                     "INNER JOIN USERS u1 ON er.CreatedBy = u1.UserID " +
                     "LEFT JOIN USERS u2 ON er.ApprovedBy = u2.UserID " +
                     "WHERE er.ExportRequestID = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ExportRequest req = mapResultSet(rs);
                    req.setDetails(getDetails(id));
                    return req;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding export request: " + id, e);
        }
        return null;
    }

    public List<ExportRequestDetail> getDetails(int exportRequestId) {
        List<ExportRequestDetail> list = new ArrayList<>();
        String sql = "SELECT erd.ExportRequestDetailID, erd.ExportRequestID, erd.ProductID, erd.Quantity, " +
                     "p.ProductCode, p.ProductName " +
                     "FROM EXPORT_REQUEST_DETAILS erd " +
                     "INNER JOIN PRODUCTS p ON erd.ProductID = p.ProductID " +
                     "WHERE erd.ExportRequestID = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, exportRequestId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ExportRequestDetail d = new ExportRequestDetail(
                            rs.getInt("ExportRequestDetailID"),
                            rs.getInt("ExportRequestID"),
                            rs.getInt("ProductID"),
                            rs.getInt("Quantity")
                    );
                    d.setProductCode(rs.getString("ProductCode"));
                    d.setProductName(rs.getString("ProductName"));
                    list.add(d);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching export request details for: " + exportRequestId, e);
        }
        return list;
    }

    public boolean createRequest(ExportRequest req, List<ExportRequestDetail> details) {
        String reqSql = "INSERT INTO EXPORT_REQUESTS (CustomerID, CreatedBy, RequestDate, Status) VALUES (?, ?, GETDATE(), 'PENDING')";
        String detailSql = "INSERT INTO EXPORT_REQUEST_DETAILS (ExportRequestID, ProductID, Quantity) VALUES (?, ?, ?)";

        Connection conn = null;
        try {
            conn = DBContext.getConnection();
            conn.setAutoCommit(false);

            int newId = -1;
            try (PreparedStatement ps = conn.prepareStatement(reqSql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, req.getCustomerId());
                ps.setInt(2, req.getCreatedBy());
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) newId = rs.getInt(1);
                }
            }

            if (newId > 0 && details != null) {
                try (PreparedStatement psDetail = conn.prepareStatement(detailSql)) {
                    for (ExportRequestDetail d : details) {
                        psDetail.setInt(1, newId);
                        psDetail.setInt(2, d.getProductId());
                        psDetail.setInt(3, d.getQuantity());
                        psDetail.addBatch();
                    }
                    psDetail.executeBatch();
                }
            }

            conn.commit();
            req.setExportRequestId(newId);
            return true;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating export request", e);
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
        String sql = "UPDATE EXPORT_REQUESTS SET Status = ?, ApprovedBy = ?, ApprovedDate = CASE WHEN ? IN ('APPROVED','REJECTED') THEN GETDATE() ELSE ApprovedDate END, CancelReason = ? WHERE ExportRequestID = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newStatus);
            if (approverId != null) ps.setInt(2, approverId); else ps.setNull(2, Types.INTEGER);
            ps.setString(3, newStatus);
            ps.setString(4, cancelReason);
            ps.setInt(5, requestId);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating export request status: " + requestId, e);
            return false;
        }
    }

    private ExportRequest mapResultSet(ResultSet rs) throws SQLException {
        ExportRequest req = new ExportRequest();
        req.setExportRequestId(rs.getInt("ExportRequestID"));
        req.setCustomerId(rs.getInt("CustomerID"));
        req.setCreatedBy(rs.getInt("CreatedBy"));
        req.setRequestDate(rs.getTimestamp("RequestDate"));
        int approver = rs.getInt("ApprovedBy");
        req.setApprovedBy(rs.wasNull() ? null : approver);
        req.setApprovedDate(rs.getTimestamp("ApprovedDate"));
        req.setStatus(rs.getString("Status"));
        req.setCancelReason(rs.getString("CancelReason"));
        req.setCustomerName(rs.getString("CustomerName"));
        req.setCustomerPhone(rs.getString("CustomerPhone"));
        req.setCreatorName(rs.getString("CreatorName"));
        req.setApproverName(rs.getString("ApproverName"));
        return req;
    }
}

