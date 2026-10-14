package com.lims.controller.admin;

import com.lims.controller.BaseServlet;
import com.lims.dao.AuditLogDAO;
import com.lims.model.AuditLog;
import com.lims.util.ValidationUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * Controller for Audit Logs & System Monitoring (FE-18, UC-78, UC-79)
 */
@WebServlet(name = "AdminAuditServlet", urlPatterns = {"/app/admin/audit"})
public class AdminAuditServlet extends BaseServlet {

    private final AuditLogDAO auditLogDAO = new AuditLogDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String keyword = req.getParameter("keyword");
        String action = req.getParameter("action");
        String tableName = req.getParameter("tableName");
        int page = ValidationUtils.parsePositiveInt(req.getParameter("page"), 1);
        int pageSize = 15;

        List<AuditLog> logs = auditLogDAO.searchLogs(keyword, action, tableName, page, pageSize);
        int totalLogs = auditLogDAO.countLogs(keyword, action, tableName);
        int totalPages = (int) Math.ceil((double) totalLogs / pageSize);

        List<String> actions = auditLogDAO.getDistinctActions();

        req.setAttribute("logs", logs);
        req.setAttribute("actions", actions);
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("totalLogs", totalLogs);
        req.setAttribute("keyword", keyword);
        req.setAttribute("selectedAction", action);
        req.setAttribute("selectedTable", tableName);

        forward(req, resp, "admin/audit.jsp");
    }
}

