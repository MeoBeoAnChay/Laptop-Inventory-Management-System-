package com.lims.dao;

import com.lims.model.Inventory;
import com.lims.util.DBContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InventoryDAO {

    private static final Logger LOGGER = Logger.getLogger(InventoryDAO.class.getName());

    public List<Inventory> getInventory(String keyword, String status, String location, int page, int pageSize) {
        List<Inventory> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT inv.InventoryID, inv.ProductID, inv.SerialNumber, inv.Location, inv.Status, inv.UpdatedAt, ")
           .append("p.ProductCode, p.ProductName, p.UnitPrice, b.BrandName, pl.LineName ")
           .append("FROM INVENTORY inv ")
           .append("INNER JOIN PRODUCTS p ON inv.ProductID = p.ProductID ")
           .append("INNER JOIN PRODUCT_LINES pl ON p.ProductLineID = pl.ProductLineID ")
           .append("INNER JOIN BRANDS b ON pl.BrandID = b.BrandID ")
           .append("WHERE 1=1 ");

        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("AND (inv.SerialNumber LIKE ? OR p.ProductCode LIKE ? OR p.ProductName LIKE ?) ");
            String term = "%" + keyword.trim() + "%";
            params.add(term);
            params.add(term);
            params.add(term);
        }

        if (status != null && !status.trim().isEmpty() && !status.equalsIgnoreCase("ALL")) {
            sql.append("AND inv.Status = ? ");
            params.add(status.trim());
        }

        if (location != null && !location.trim().isEmpty() && !location.equalsIgnoreCase("ALL")) {
            sql.append("AND inv.Location LIKE ? ");
            params.add("%" + location.trim() + "%");
        }

        sql.append("ORDER BY inv.UpdatedAt DESC ")
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
            LOGGER.log(Level.SEVERE, "Error fetching inventory items", e);
        }
        return list;
    }

    public int countInventory(String keyword, String status, String location) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(1) FROM INVENTORY inv ")
           .append("INNER JOIN PRODUCTS p ON inv.ProductID = p.ProductID ")
           .append("WHERE 1=1 ");

        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("AND (inv.SerialNumber LIKE ? OR p.ProductCode LIKE ? OR p.ProductName LIKE ?) ");
            String term = "%" + keyword.trim() + "%";
            params.add(term);
            params.add(term);
            params.add(term);
        }

        if (status != null && !status.trim().isEmpty() && !status.equalsIgnoreCase("ALL")) {
            sql.append("AND inv.Status = ? ");
            params.add(status.trim());
        }

        if (location != null && !location.trim().isEmpty() && !location.equalsIgnoreCase("ALL")) {
            sql.append("AND inv.Location LIKE ? ");
            params.add("%" + location.trim() + "%");
        }

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error counting inventory items", e);
        }
        return 0;
    }

    public Inventory findBySerialNumber(String serialNumber) {
        String sql = "SELECT inv.InventoryID, inv.ProductID, inv.SerialNumber, inv.Location, inv.Status, inv.UpdatedAt, " +
                     "p.ProductCode, p.ProductName, p.UnitPrice, b.BrandName, pl.LineName " +
                     "FROM INVENTORY inv " +
                     "INNER JOIN PRODUCTS p ON inv.ProductID = p.ProductID " +
                     "INNER JOIN PRODUCT_LINES pl ON p.ProductLineID = pl.ProductLineID " +
                     "INNER JOIN BRANDS b ON pl.BrandID = b.BrandID " +
                     "WHERE inv.SerialNumber = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, serialNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapResultSet(rs);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding inventory by serial number: " + serialNumber, e);
        }
        return null;
    }

    public boolean updateStatusAndLocation(int inventoryId, String newStatus, String newLocation) {
        String sql = "UPDATE INVENTORY SET Status = ?, Location = ?, UpdatedAt = GETDATE() WHERE InventoryID = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newStatus);
            ps.setString(2, newLocation);
            ps.setInt(3, inventoryId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating inventory status/location", e);
            return false;
        }
    }

    public Map<String, Integer> getSummaryStats() {
        Map<String, Integer> stats = new HashMap<>();
        String sql = "SELECT Status, COUNT(1) AS Total FROM INVENTORY GROUP BY Status";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                stats.put(rs.getString("Status"), rs.getInt("Total"));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting inventory stats", e);
        }
        return stats;
    }

    private Inventory mapResultSet(ResultSet rs) throws SQLException {
        Inventory inv = new Inventory();
        inv.setInventoryId(rs.getInt("InventoryID"));
        inv.setProductId(rs.getInt("ProductID"));
        inv.setSerialNumber(rs.getString("SerialNumber"));
        inv.setLocation(rs.getString("Location"));
        inv.setStatus(rs.getString("Status"));
        inv.setUpdatedAt(rs.getTimestamp("UpdatedAt"));
        inv.setProductCode(rs.getString("ProductCode"));
        inv.setProductName(rs.getString("ProductName"));
        inv.setUnitPrice(rs.getBigDecimal("UnitPrice"));
        inv.setBrandName(rs.getString("BrandName"));
        inv.setLineName(rs.getString("LineName"));
        return inv;
    }
}

