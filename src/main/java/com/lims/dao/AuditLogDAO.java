package com.lims.dao;

import com.lims.model.AuditLog;
import com.lims.util.DBContext;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AuditLogDAO {

    private static final Logger LOGGER = Logger.getLogger(AuditLogDAO.class.getName());

    public void log(AuditLog log) {
        String sql = "INSERT INTO AUDIT_LOGS (UserID, Username, Action, TableName, RecordID, OldValues, NewValues, IPAddress, UserAgent, Timestamp) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, GETDATE())";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (log.getUserId() != null) {
                ps.setInt(1, log.getUserId());
            } else {
                ps.setNull(1, java.sql.Types.INTEGER);
            }
            ps.setString(2, log.getUsername());
            ps.setString(3, log.getAction());
            ps.setString(4, log.getTableName());
            ps.setString(5, log.getRecordId());
            ps.setString(6, log.getOldValues());
            ps.setString(7, log.getNewValues());
            ps.setString(8, log.getIpAddress());
            ps.setString(9, log.getUserAgent());

            ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Error inserting audit log", e);
        }
    }

    public List<AuditLog> getRecentLogs(int limit) {
        List<AuditLog> list = new ArrayList<>();
        String sql = "SELECT TOP (?) LogID, UserID, Username, Action, TableName, RecordID, OldValues, NewValues, IPAddress, UserAgent, Timestamp " +
                     "FROM AUDIT_LOGS ORDER BY Timestamp DESC";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, limit > 0 ? limit : 50);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSet(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching recent audit logs", e);
        }
        return list;
    }

    public List<AuditLog> searchLogs(String keyword, String action, String tableName, int page, int pageSize) {
        List<AuditLog> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT LogID, UserID, Username, Action, TableName, RecordID, OldValues, NewValues, IPAddress, UserAgent, Timestamp ")
           .append("FROM AUDIT_LOGS WHERE 1=1 ");

        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("AND (Username LIKE ? OR RecordID LIKE ? OR NewValues LIKE ? OR IPAddress LIKE ?) ");
            String term = "%" + keyword.trim() + "%";
            params.add(term);
            params.add(term);
            params.add(term);
            params.add(term);
        }

        if (action != null && !action.trim().isEmpty() && !action.equalsIgnoreCase("ALL")) {
            sql.append("AND Action = ? ");
            params.add(action.trim());
        }

        if (tableName != null && !tableName.trim().isEmpty() && !tableName.equalsIgnoreCase("ALL")) {
            sql.append("AND TableName = ? ");
            params.add(tableName.trim());
        }

        sql.append("ORDER BY Timestamp DESC ")
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
            LOGGER.log(Level.SEVERE, "Error searching audit logs", e);
        }
        return list;
    }

    public int countLogs(String keyword, String action, String tableName) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(1) FROM AUDIT_LOGS WHERE 1=1 ");

        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("AND (Username LIKE ? OR RecordID LIKE ? OR NewValues LIKE ? OR IPAddress LIKE ?) ");
            String term = "%" + keyword.trim() + "%";
            params.add(term);
            params.add(term);
            params.add(term);
            params.add(term);
        }

        if (action != null && !action.trim().isEmpty() && !action.equalsIgnoreCase("ALL")) {
            sql.append("AND Action = ? ");
            params.add(action.trim());
        }

        if (tableName != null && !tableName.trim().isEmpty() && !tableName.equalsIgnoreCase("ALL")) {
            sql.append("AND TableName = ? ");
            params.add(tableName.trim());
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
            LOGGER.log(Level.SEVERE, "Error counting audit logs", e);
        }
        return 0;
    }

    public List<String> getDistinctActions() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT DISTINCT Action FROM AUDIT_LOGS ORDER BY Action";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(rs.getString("Action"));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching distinct actions", e);
        }
        return list;
    }

    private AuditLog mapResultSet(ResultSet rs) throws SQLException {
        AuditLog log = new AuditLog();
        log.setLogId(rs.getInt("LogID"));
        int uid = rs.getInt("UserID");
        log.setUserId(rs.wasNull() ? null : uid);
        log.setUsername(rs.getString("Username"));
        log.setAction(rs.getString("Action"));
        log.setTableName(rs.getString("TableName"));
        log.setRecordId(rs.getString("RecordID"));
        log.setOldValues(rs.getString("OldValues"));
        log.setNewValues(rs.getString("NewValues"));
        log.setIpAddress(rs.getString("IPAddress"));
        log.setUserAgent(rs.getString("UserAgent"));
        log.setTimestamp(rs.getTimestamp("Timestamp"));
        return log;
    }
}
