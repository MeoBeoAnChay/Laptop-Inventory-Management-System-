package com.lims.dao;

import com.lims.model.ProductLine;
import com.lims.util.DBContext;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProductLineDAO {

    private static final Logger LOGGER = Logger.getLogger(ProductLineDAO.class.getName());

    public List<ProductLine> getLinesByBrand(int brandId) {
        List<ProductLine> list = new ArrayList<>();
        String sql = "SELECT pl.ProductLineID, pl.BrandID, pl.LineName, b.BrandName " +
                     "FROM PRODUCT_LINES pl INNER JOIN BRANDS b ON pl.BrandID = b.BrandID " +
                     "WHERE pl.BrandID = ? ORDER BY pl.LineName ASC";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, brandId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ProductLine pl = new ProductLine(
                            rs.getInt("ProductLineID"),
                            rs.getInt("BrandID"),
                            rs.getString("LineName")
                    );
                    pl.setBrandName(rs.getString("BrandName"));
                    list.add(pl);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching product lines by brand: " + brandId, e);
        }
        return list;
    }

    public List<ProductLine> getAllProductLines() {
        List<ProductLine> list = new ArrayList<>();
        String sql = "SELECT pl.ProductLineID, pl.BrandID, pl.LineName, b.BrandName " +
                     "FROM PRODUCT_LINES pl INNER JOIN BRANDS b ON pl.BrandID = b.BrandID " +
                     "ORDER BY b.BrandName, pl.LineName ASC";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ProductLine pl = new ProductLine(
                        rs.getInt("ProductLineID"),
                        rs.getInt("BrandID"),
                        rs.getString("LineName")
                );
                pl.setBrandName(rs.getString("BrandName"));
                list.add(pl);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching all product lines", e);
        }
        return list;
    }
}

