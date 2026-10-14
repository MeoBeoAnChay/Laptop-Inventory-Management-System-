package com.lims.dao;

import com.lims.model.Supplier;
import com.lims.util.DBContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SupplierDAO {

    private static final Logger LOGGER = Logger.getLogger(SupplierDAO.class.getName());

    public List<Supplier> getAllSuppliers() {
        List<Supplier> list = new ArrayList<>();
        String sql = "SELECT SupplierID, SupplierName, ContactName, Email, Phone, Address, Status FROM SUPPLIERS ORDER BY SupplierName ASC";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching suppliers", e);
        }
        return list;
    }

    public List<Supplier> getActiveSuppliers() {
        List<Supplier> list = new ArrayList<>();
        String sql = "SELECT SupplierID, SupplierName, ContactName, Email, Phone, Address, Status FROM SUPPLIERS WHERE Status = 'ACTIVE' ORDER BY SupplierName ASC";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching active suppliers", e);
        }
        return list;
    }

    public Supplier findById(int id) {
        String sql = "SELECT SupplierID, SupplierName, ContactName, Email, Phone, Address, Status FROM SUPPLIERS WHERE SupplierID = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding supplier: " + id, e);
        }
        return null;
    }

    public boolean createSupplier(Supplier s) {
        String sql = "INSERT INTO SUPPLIERS (SupplierName, ContactName, Email, Phone, Address, Status) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, s.getSupplierName());
            ps.setString(2, s.getContactName());
            ps.setString(3, s.getEmail());
            ps.setString(4, s.getPhone());
            ps.setString(5, s.getAddress());
            ps.setString(6, s.getStatus() != null ? s.getStatus() : "ACTIVE");

            int aff = ps.executeUpdate();
            if (aff > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) s.setSupplierId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating supplier", e);
        }
        return false;
    }

    private Supplier mapResultSet(ResultSet rs) throws SQLException {
        return new Supplier(
                rs.getInt("SupplierID"),
                rs.getString("SupplierName"),
                rs.getString("ContactName"),
                rs.getString("Email"),
                rs.getString("Phone"),
                rs.getString("Address"),
                rs.getString("Status")
        );
    }
}

