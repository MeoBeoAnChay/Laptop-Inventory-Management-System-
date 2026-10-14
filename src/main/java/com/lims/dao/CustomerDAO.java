package com.lims.dao;

import com.lims.model.Customer;
import com.lims.util.DBContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CustomerDAO {

    private static final Logger LOGGER = Logger.getLogger(CustomerDAO.class.getName());

    public List<Customer> getAllCustomers() {
        List<Customer> list = new ArrayList<>();
        String sql = "SELECT CustomerID, CustomerName, Phone, Email, Address FROM CUSTOMERS ORDER BY CustomerName ASC";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching customers", e);
        }
        return list;
    }

    public Customer findById(int id) {
        String sql = "SELECT CustomerID, CustomerName, Phone, Email, Address FROM CUSTOMERS WHERE CustomerID = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding customer by ID: " + id, e);
        }
        return null;
    }

    public Customer findByPhone(String phone) {
        String sql = "SELECT CustomerID, CustomerName, Phone, Email, Address FROM CUSTOMERS WHERE Phone = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, phone);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding customer by phone: " + phone, e);
        }
        return null;
    }

    public boolean createCustomer(Customer c) {
        String sql = "INSERT INTO CUSTOMERS (CustomerName, Phone, Email, Address) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, c.getCustomerName());
            ps.setString(2, c.getPhone());
            ps.setString(3, c.getEmail());
            ps.setString(4, c.getAddress());

            int aff = ps.executeUpdate();
            if (aff > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) c.setCustomerId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating customer", e);
        }
        return false;
    }

    private Customer mapResultSet(ResultSet rs) throws SQLException {
        return new Customer(
                rs.getInt("CustomerID"),
                rs.getString("CustomerName"),
                rs.getString("Phone"),
                rs.getString("Email"),
                rs.getString("Address")
        );
    }
}

