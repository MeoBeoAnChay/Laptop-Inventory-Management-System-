package com.lims.dao;

import com.lims.model.Role;
import com.lims.util.DBContext;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RoleDAO {

    private static final Logger LOGGER = Logger.getLogger(RoleDAO.class.getName());

    public List<Role> getAllRoles() {
        List<Role> list = new ArrayList<>();
        String sql = "SELECT RoleID, RoleName, Description FROM ROLES ORDER BY RoleID ASC";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Role(
                        rs.getInt("RoleID"),
                        rs.getString("RoleName"),
                        rs.getString("Description")
                ));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching all roles", e);
        }
        return list;
    }
}

