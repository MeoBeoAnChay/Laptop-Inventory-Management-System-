package com.lims.controller.admin;

import com.lims.controller.BaseServlet;
import com.lims.dao.AuditLogDAO;
import com.lims.model.AuditLog;
import com.lims.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller for System Configuration (FE-17, UC-75 to UC-77)
 */
@WebServlet(name = "AdminConfigServlet", urlPatterns = {"/app/admin/config"})
public class AdminConfigServlet extends BaseServlet {

    private final AuditLogDAO auditLogDAO = new AuditLogDAO();

    // In-memory runtime configuration store (or backing db.properties)
    private static final Map<String, String> configs = new HashMap<>();

    static {
        configs.put("system_name", "Laptop Inventory Management System (LIMS)");
        configs.put("organization", "SE1976-JS(AD) Group 5");
        configs.put("warehouse_name", "Kho Tổng Laptop LIMS Hòa Lạc");
        configs.put("session_timeout", "60");
        configs.put("max_login_attempts", "5");
        configs.put("min_password_length", "8");
        configs.put("smtp_host", "smtp.gmail.com");
        configs.put("smtp_port", "587");
        configs.put("smtp_sender", "no-reply@lims.com");
        configs.put("maintenance_mode", "false");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("configs", configs);
        forward(req, resp, "admin/config.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User currentUser = getCurrentUser(req);

        String systemName = req.getParameter("system_name");
        String warehouseName = req.getParameter("warehouse_name");
        String sessionTimeout = req.getParameter("session_timeout");
        String maxLoginAttempts = req.getParameter("max_login_attempts");
        String minPasswordLength = req.getParameter("min_password_length");
        String smtpHost = req.getParameter("smtp_host");
        String smtpPort = req.getParameter("smtp_port");
        String maintenanceMode = req.getParameter("maintenance_mode");

        if (systemName != null) configs.put("system_name", systemName.trim());
        if (warehouseName != null) configs.put("warehouse_name", warehouseName.trim());
        if (sessionTimeout != null) configs.put("session_timeout", sessionTimeout.trim());
        if (maxLoginAttempts != null) configs.put("max_login_attempts", maxLoginAttempts.trim());
        if (minPasswordLength != null) configs.put("min_password_length", minPasswordLength.trim());
        if (smtpHost != null) configs.put("smtp_host", smtpHost.trim());
        if (smtpPort != null) configs.put("smtp_port", smtpPort.trim());
        configs.put("maintenance_mode", "on".equalsIgnoreCase(maintenanceMode) ? "true" : "false");

        auditLogDAO.log(new AuditLog(
                currentUser != null ? currentUser.getUserId() : null,
                currentUser != null ? currentUser.getUsername() : "admin",
                "UPDATE_CONFIG", "SYSTEM_CONFIG", "GLOBAL",
                null, "Updated system configuration settings",
                getClientIP(req), req.getHeader("User-Agent")
        ));

        redirect(req, resp, "/app/admin/config?success=updated");
    }
}

