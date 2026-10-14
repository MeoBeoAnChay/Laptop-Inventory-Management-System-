package com.lims.dao;

import com.lims.model.Brand;
import com.lims.util.DBContext;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BrandDAO {

    private static final Logger LOGGER = Logger.getLogger(BrandDAO.class.getName());

    public List<Brand> getAllBrands() {
        List<Brand> list = new ArrayList<>();
        String sql = "SELECT BrandID, BrandName, Status FROM BRANDS ORDER BY BrandName ASC";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Brand(
                        rs.getInt("BrandID"),
                        rs.getString("BrandName"),
                        rs.getString("Status")
                ));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching brands", e);
        }
        return list;
    }

    public List<Brand> getActiveBrands() {
        List<Brand> list = new ArrayList<>();
        String sql = "SELECT BrandID, BrandName, Status FROM BRANDS WHERE Status = 'ACTIVE' ORDER BY BrandName ASC";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Brand(
                        rs.getInt("BrandID"),
                        rs.getString("BrandName"),
                        rs.getString("Status")
                ));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching active brands", e);
        }
        return list;
    }
}

