package com.lims.dao;

import com.lims.model.Role;
import com.lims.model.User;
import com.lims.util.DBContext;
import com.lims.util.PasswordUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDAO {

    private static final Logger LOGGER = Logger.getLogger(UserDAO.class.getName());

    /**
     * Authenticates a user by username and plain text password.
     * Verifies using BCrypt.
     */
    public User authenticate(String username, String plainPassword) {
        String sql = "SELECT UserID, Username, PasswordHash, FullName, Email, Phone, Status, CreatedAt " +
                     "FROM USERS WHERE Username = ? AND Status = 'ACTIVE'";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("PasswordHash");
                    if (PasswordUtils.checkPassword(plainPassword, storedHash)) {
                        User user = mapResultSetToUser(rs);
                        user.setRoles(getUserRoles(user.getUserId()));
                        return user;
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error authenticating user: " + username, e);
        }
        return null;
    }

    public User findById(int userId) {
        String sql = "SELECT UserID, Username, PasswordHash, FullName, Email, Phone, Status, CreatedAt " +
                     "FROM USERS WHERE UserID = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = mapResultSetToUser(rs);
                    user.setRoles(getUserRoles(user.getUserId()));
                    return user;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding user by ID: " + userId, e);
        }
        return null;
    }

    public User findByUsername(String username) {
        String sql = "SELECT UserID, Username, PasswordHash, FullName, Email, Phone, Status, CreatedAt " +
                     "FROM USERS WHERE Username = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = mapResultSetToUser(rs);
                    user.setRoles(getUserRoles(user.getUserId()));
                    return user;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding user by username: " + username, e);
        }
        return null;
    }

    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT UserID, Username, PasswordHash, FullName, Email, Phone, Status, CreatedAt " +
                     "FROM USERS ORDER BY UserID DESC";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                User user = mapResultSetToUser(rs);
                user.setRoles(getUserRoles(user.getUserId()));
                list.add(user);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching all users", e);
        }
        return list;
    }

    public boolean createUser(User user, List<Integer> roleIds) {
        String userSql = "INSERT INTO USERS (Username, PasswordHash, FullName, Email, Phone, Status, CreatedAt) " +
                         "VALUES (?, ?, ?, ?, ?, ?, GETDATE())";
        String roleSql = "INSERT INTO USER_ROLES (UserID, RoleID) VALUES (?, ?)";

        Connection conn = null;
        try {
            conn = DBContext.getConnection();
            conn.setAutoCommit(false);

            int newUserId = -1;
            try (PreparedStatement ps = conn.prepareStatement(userSql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, user.getUsername());
                ps.setString(2, user.getPasswordHash());
                ps.setString(3, user.getFullName());
                ps.setString(4, user.getEmail());
                ps.setString(5, user.getPhone());
                ps.setString(6, user.getStatus() != null ? user.getStatus() : "ACTIVE");
                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        newUserId = rs.getInt(1);
                        user.setUserId(newUserId);
                    }
                }
            }

            if (newUserId > 0 && roleIds != null && !roleIds.isEmpty()) {
                try (PreparedStatement psRole = conn.prepareStatement(roleSql)) {
                    for (int rId : roleIds) {
                        psRole.setInt(1, newUserId);
                        psRole.setInt(2, rId);
                        psRole.addBatch();
                    }
                    psRole.executeBatch();
                }
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating user", e);
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

    public List<User> searchUsers(String keyword, Integer roleId, String status, int page, int pageSize) {
        List<User> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT DISTINCT u.UserID, u.Username, u.PasswordHash, u.FullName, u.Email, u.Phone, u.Status, u.CreatedAt ")
           .append("FROM USERS u ")
           .append("LEFT JOIN USER_ROLES ur ON u.UserID = ur.UserID ")
           .append("WHERE 1=1 ");

        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("AND (u.Username LIKE ? OR u.FullName LIKE ? OR u.Email LIKE ? OR u.Phone LIKE ?) ");
            String term = "%" + keyword.trim() + "%";
            params.add(term);
            params.add(term);
            params.add(term);
            params.add(term);
        }

        if (roleId != null && roleId > 0) {
            sql.append("AND ur.RoleID = ? ");
            params.add(roleId);
        }

        if (status != null && !status.trim().isEmpty() && !status.equalsIgnoreCase("ALL")) {
            sql.append("AND u.Status = ? ");
            params.add(status.trim());
        }

        sql.append("ORDER BY u.UserID ASC ")
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
                    User user = mapResultSetToUser(rs);
                    user.setRoles(getUserRoles(user.getUserId()));
                    list.add(user);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error searching users", e);
        }
        return list;
    }

    public int countUsers(String keyword, Integer roleId, String status) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(DISTINCT u.UserID) ")
           .append("FROM USERS u ")
           .append("LEFT JOIN USER_ROLES ur ON u.UserID = ur.UserID ")
           .append("WHERE 1=1 ");

        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("AND (u.Username LIKE ? OR u.FullName LIKE ? OR u.Email LIKE ? OR u.Phone LIKE ?) ");
            String term = "%" + keyword.trim() + "%";
            params.add(term);
            params.add(term);
            params.add(term);
            params.add(term);
        }

        if (roleId != null && roleId > 0) {
            sql.append("AND ur.RoleID = ? ");
            params.add(roleId);
        }

        if (status != null && !status.trim().isEmpty() && !status.equalsIgnoreCase("ALL")) {
            sql.append("AND u.Status = ? ");
            params.add(status.trim());
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
            LOGGER.log(Level.SEVERE, "Error counting users", e);
        }
        return 0;
    }

    public boolean updateStatus(int userId, String newStatus) {
        // Prevent disabling the last active Admin
        if (!"ACTIVE".equalsIgnoreCase(newStatus)) {
            User target = findById(userId);
            if (target != null && target.hasRole("Admin")) {
                int activeAdminCount = countUsers(null, 1, "ACTIVE");
                if (activeAdminCount <= 1) {
                    LOGGER.warning("Cannot deactivate the only remaining active Administrator!");
                    return false;
                }
            }
        }

        String sql = "UPDATE USERS SET Status = ? WHERE UserID = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newStatus);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating status for user: " + userId, e);
            return false;
        }
    }

    public boolean resetPassword(int userId, String newPlainPassword) {
        String hashed = PasswordUtils.hashPassword(newPlainPassword);
        return updatePassword(userId, hashed);
    }

    public java.util.Map<String, Integer> getUserStatistics() {
        java.util.Map<String, Integer> stats = new java.util.HashMap<>();
        String sqlStatus = "SELECT Status, COUNT(1) AS Total FROM USERS GROUP BY Status";
        String sqlRole = "SELECT r.RoleName, COUNT(ur.UserID) AS Total FROM ROLES r LEFT JOIN USER_ROLES ur ON r.RoleID = ur.RoleID GROUP BY r.RoleName";

        try (Connection conn = DBContext.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(sqlStatus);
                 ResultSet rs = ps.executeQuery()) {
                int total = 0;
                while (rs.next()) {
                    String st = rs.getString("Status");
                    int count = rs.getInt("Total");
                    stats.put("status_" + st, count);
                    total += count;
                }
                stats.put("total_users", total);
            }

            try (PreparedStatement ps = conn.prepareStatement(sqlRole);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    stats.put("role_" + rs.getString("RoleName"), rs.getInt("Total"));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting user statistics", e);
        }
        return stats;
    }

    public boolean updateUser(User user) {
        String sql = "UPDATE USERS SET FullName = ?, Email = ?, Phone = ?, Status = ? WHERE UserID = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getFullName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPhone());
            ps.setString(4, user.getStatus());
            ps.setInt(5, user.getUserId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating user: " + user.getUserId(), e);
            return false;
        }
    }

    /** Updates a user's profile, status, and role assignments as one transaction. */
    public boolean updateUserAndRoles(User user, List<Integer> roleIds) {
        String updateUserSql = "UPDATE USERS SET FullName = ?, Email = ?, Phone = ?, Status = ? WHERE UserID = ?";
        String removeRolesSql = "DELETE FROM USER_ROLES WHERE UserID = ?";
        String addRoleSql = "INSERT INTO USER_ROLES (UserID, RoleID) VALUES (?, ?)";
        String currentAdminSql = "SELECT COUNT(DISTINCT u.UserID) " +
                "FROM USERS u WITH (UPDLOCK, HOLDLOCK) " +
                "INNER JOIN USER_ROLES ur WITH (UPDLOCK, HOLDLOCK) ON u.UserID = ur.UserID " +
                "INNER JOIN ROLES r ON r.RoleID = ur.RoleID " +
                "WHERE u.Status = 'ACTIVE' AND r.RoleName = 'Admin'";

        try (Connection conn = DBContext.getConnection()) {
            conn.setAutoCommit(false);
            try {
                boolean currentlyActiveAdmin = false;
                try (PreparedStatement ps = conn.prepareStatement(
                        "SELECT CASE WHEN u.Status = 'ACTIVE' AND EXISTS (" +
                        "SELECT 1 FROM USER_ROLES ur INNER JOIN ROLES r ON r.RoleID = ur.RoleID " +
                        "WHERE ur.UserID = u.UserID AND r.RoleName = 'Admin') THEN 1 ELSE 0 END " +
                        "FROM USERS u WHERE u.UserID = ?")) {
                    ps.setInt(1, user.getUserId());
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            conn.rollback();
                            return false;
                        }
                        currentlyActiveAdmin = rs.getBoolean(1);
                    }
                }

                boolean remainsActiveAdmin = "ACTIVE".equalsIgnoreCase(user.getStatus())
                        && roleIds != null && !roleIds.isEmpty()
                        && hasAdminRole(conn, roleIds);
                if (currentlyActiveAdmin && !remainsActiveAdmin) {
                    try (PreparedStatement ps = conn.prepareStatement(currentAdminSql);
                         ResultSet rs = ps.executeQuery()) {
                        if (rs.next() && rs.getInt(1) <= 1) {
                            conn.rollback();
                            return false;
                        }
                    }
                }

                try (PreparedStatement ps = conn.prepareStatement(updateUserSql)) {
                    ps.setString(1, user.getFullName());
                    ps.setString(2, user.getEmail());
                    ps.setString(3, user.getPhone());
                    ps.setString(4, user.getStatus());
                    ps.setInt(5, user.getUserId());
                    if (ps.executeUpdate() == 0) {
                        conn.rollback();
                        return false;
                    }
                }
                try (PreparedStatement ps = conn.prepareStatement(removeRolesSql)) {
                    ps.setInt(1, user.getUserId());
                    ps.executeUpdate();
                }
                if (roleIds != null && !roleIds.isEmpty()) {
                    try (PreparedStatement ps = conn.prepareStatement(addRoleSql)) {
                        for (int roleId : roleIds) {
                            ps.setInt(1, user.getUserId());
                            ps.setInt(2, roleId);
                            ps.addBatch();
                        }
                        ps.executeBatch();
                    }
                }
                conn.commit();
                return true;
            } catch (SQLException | RuntimeException ex) {
                conn.rollback();
                throw ex;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating user and roles: " + user.getUserId(), e);
            return false;
        }
    }

    private boolean hasAdminRole(Connection conn, List<Integer> roleIds) throws SQLException {
        String sql = "SELECT COUNT(*) FROM ROLES WHERE RoleName = 'Admin' AND RoleID = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int roleId : roleIds) {
                ps.setInt(1, roleId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) return true;
                }
            }
        }
        return false;
    }

    public boolean updatePassword(int userId, String newHashedPassword) {
        String sql = "UPDATE USERS SET PasswordHash = ? WHERE UserID = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newHashedPassword);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating password for user: " + userId, e);
            return false;
        }
    }

    public List<Role> getUserRoles(int userId) {
        List<Role> roles = new ArrayList<>();
        String sql = "SELECT r.RoleID, r.RoleName, r.Description " +
                     "FROM ROLES r INNER JOIN USER_ROLES ur ON r.RoleID = ur.RoleID " +
                     "WHERE ur.UserID = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    roles.add(new Role(
                            rs.getInt("RoleID"),
                            rs.getString("RoleName"),
                            rs.getString("Description")
                    ));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting roles for user: " + userId, e);
        }
        return roles;
    }

    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("UserID"));
        user.setUsername(rs.getString("Username"));
        user.setPasswordHash(rs.getString("PasswordHash"));
        user.setFullName(rs.getString("FullName"));
        user.setEmail(rs.getString("Email"));
        user.setPhone(rs.getString("Phone"));
        user.setStatus(rs.getString("Status"));
        user.setCreatedAt(rs.getTimestamp("CreatedAt"));
        return user;
    }
}

