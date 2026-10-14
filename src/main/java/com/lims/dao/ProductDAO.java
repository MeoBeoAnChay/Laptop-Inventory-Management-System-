package com.lims.dao;

import com.lims.model.Product;
import com.lims.util.DBContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProductDAO {

    private static final Logger LOGGER = Logger.getLogger(ProductDAO.class.getName());

    public List<Product> getProducts(String keyword, Integer brandId, String status, int page, int pageSize) {
        List<Product> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT p.ProductID, p.ProductCode, p.ProductName, p.ProductLineID, p.CPU, p.RAM, p.Storage, p.GPU, p.Screen, p.UnitPrice, p.Status, ")
           .append("pl.LineName, b.BrandName, ")
           .append("(SELECT COUNT(1) FROM INVENTORY inv WHERE inv.ProductID = p.ProductID AND inv.Status = 'IN_STOCK') AS CurrentStock ")
           .append("FROM PRODUCTS p ")
           .append("INNER JOIN PRODUCT_LINES pl ON p.ProductLineID = pl.ProductLineID ")
           .append("INNER JOIN BRANDS b ON pl.BrandID = b.BrandID ")
           .append("WHERE 1=1 ");

        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("AND (p.ProductCode LIKE ? OR p.ProductName LIKE ? OR p.CPU LIKE ?) ");
            String term = "%" + keyword.trim() + "%";
            params.add(term);
            params.add(term);
            params.add(term);
        }

        if (brandId != null && brandId > 0) {
            sql.append("AND b.BrandID = ? ");
            params.add(brandId);
        }

        if (status != null && !status.trim().isEmpty() && !status.equalsIgnoreCase("ALL")) {
            sql.append("AND p.Status = ? ");
            params.add(status.trim());
        }

        sql.append("ORDER BY p.ProductID DESC ")
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
                    list.add(mapResultSetToProduct(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching products", e);
        }
        return list;
    }

    public int countProducts(String keyword, Integer brandId, String status) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(1) FROM PRODUCTS p ")
           .append("INNER JOIN PRODUCT_LINES pl ON p.ProductLineID = pl.ProductLineID ")
           .append("INNER JOIN BRANDS b ON pl.BrandID = b.BrandID ")
           .append("WHERE 1=1 ");

        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("AND (p.ProductCode LIKE ? OR p.ProductName LIKE ? OR p.CPU LIKE ?) ");
            String term = "%" + keyword.trim() + "%";
            params.add(term);
            params.add(term);
            params.add(term);
        }

        if (brandId != null && brandId > 0) {
            sql.append("AND b.BrandID = ? ");
            params.add(brandId);
        }

        if (status != null && !status.trim().isEmpty() && !status.equalsIgnoreCase("ALL")) {
            sql.append("AND p.Status = ? ");
            params.add(status.trim());
        }

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error counting products", e);
        }
        return 0;
    }

    public Product findById(int productId) {
        String sql = "SELECT p.ProductID, p.ProductCode, p.ProductName, p.ProductLineID, p.CPU, p.RAM, p.Storage, p.GPU, p.Screen, p.UnitPrice, p.Status, " +
                     "pl.LineName, b.BrandName, " +
                     "(SELECT COUNT(1) FROM INVENTORY inv WHERE inv.ProductID = p.ProductID AND inv.Status = 'IN_STOCK') AS CurrentStock " +
                     "FROM PRODUCTS p " +
                     "INNER JOIN PRODUCT_LINES pl ON p.ProductLineID = pl.ProductLineID " +
                     "INNER JOIN BRANDS b ON pl.BrandID = b.BrandID " +
                     "WHERE p.ProductID = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToProduct(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding product by ID: " + productId, e);
        }
        return null;
    }

    public boolean createProduct(Product p) {
        String sql = "INSERT INTO PRODUCTS (ProductCode, ProductName, ProductLineID, CPU, RAM, Storage, GPU, Screen, UnitPrice, Status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, p.getProductCode());
            ps.setString(2, p.getProductName());
            ps.setInt(3, p.getProductLineId());
            ps.setString(4, p.getCpu());
            ps.setString(5, p.getRam());
            ps.setString(6, p.getStorage());
            ps.setString(7, p.getGpu());
            ps.setString(8, p.getScreen());
            ps.setBigDecimal(9, p.getUnitPrice());
            ps.setString(10, p.getStatus() != null ? p.getStatus() : "ACTIVE");

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        p.setProductId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating product: " + p.getProductCode(), e);
        }
        return false;
    }

    public boolean updateProduct(Product p) {
        String sql = "UPDATE PRODUCTS SET ProductName = ?, ProductLineID = ?, CPU = ?, RAM = ?, Storage = ?, GPU = ?, Screen = ?, UnitPrice = ?, Status = ? " +
                     "WHERE ProductID = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getProductName());
            ps.setInt(2, p.getProductLineId());
            ps.setString(3, p.getCpu());
            ps.setString(4, p.getRam());
            ps.setString(5, p.getStorage());
            ps.setString(6, p.getGpu());
            ps.setString(7, p.getScreen());
            ps.setBigDecimal(8, p.getUnitPrice());
            ps.setString(9, p.getStatus());
            ps.setInt(10, p.getProductId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating product: " + p.getProductId(), e);
        }
        return false;
    }

    private Product mapResultSetToProduct(ResultSet rs) throws SQLException {
        Product p = new Product();
        p.setProductId(rs.getInt("ProductID"));
        p.setProductCode(rs.getString("ProductCode"));
        p.setProductName(rs.getString("ProductName"));
        p.setProductLineId(rs.getInt("ProductLineID"));
        p.setCpu(rs.getString("CPU"));
        p.setRam(rs.getString("RAM"));
        p.setStorage(rs.getString("Storage"));
        p.setGpu(rs.getString("GPU"));
        p.setScreen(rs.getString("Screen"));
        p.setUnitPrice(rs.getBigDecimal("UnitPrice"));
        p.setStatus(rs.getString("Status"));
        p.setLineName(rs.getString("LineName"));
        p.setBrandName(rs.getString("BrandName"));
        p.setCurrentStock(rs.getInt("CurrentStock"));
        return p;
    }
}

