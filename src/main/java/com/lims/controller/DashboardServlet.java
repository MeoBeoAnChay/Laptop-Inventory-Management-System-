package com.lims.controller;

import com.lims.dao.AuditLogDAO;
import com.lims.dao.ExportRequestDAO;
import com.lims.dao.ImportRequestDAO;
import com.lims.dao.InventoryDAO;
import com.lims.dao.ProductDAO;
import com.lims.dao.UserDAO;
import com.lims.model.AuditLog;
import com.lims.model.ExportRequest;
import com.lims.model.ImportRequest;
import com.lims.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(name = "DashboardServlet", urlPatterns = {"/app/dashboard"})
public class DashboardServlet extends BaseServlet {

    private final InventoryDAO inventoryDAO = new InventoryDAO();
    private final ProductDAO productDAO = new ProductDAO();
    private final ImportRequestDAO importRequestDAO = new ImportRequestDAO();
    private final ExportRequestDAO exportRequestDAO = new ExportRequestDAO();
    private final AuditLogDAO auditLogDAO = new AuditLogDAO();
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User currentUser = getCurrentUser(req);

        // If the logged in user is Admin, render the specialized Admin Dashboard
        if (currentUser != null && currentUser.hasRole("Admin")) {
            Map<String, Integer> userStats = userDAO.getUserStatistics();
            List<User> recentUsers = userDAO.searchUsers(null, null, null, 1, 5);
            List<AuditLog> recentLogs = auditLogDAO.getRecentLogs(8);
            Map<String, Integer> stockStats = inventoryDAO.getSummaryStats();
            int totalProducts = productDAO.countProducts(null, null, "ACTIVE");

            req.setAttribute("userStats", userStats);
            req.setAttribute("recentUsers", recentUsers);
            req.setAttribute("recentLogs", recentLogs);
            req.setAttribute("stockStats", stockStats);
            req.setAttribute("totalProducts", totalProducts);

            forward(req, resp, "dashboard/admin_dashboard.jsp");
            return;
        }

        // Standard warehouse / operational dashboard for Manager, Warehouse Staff, Sales, Purchasing
        Map<String, Integer> stockStats = inventoryDAO.getSummaryStats();
        int totalProducts = productDAO.countProducts(null, null, "ACTIVE");
        List<ImportRequest> recentImports = importRequestDAO.getImportRequests("ALL", 1, 5);
        List<ExportRequest> recentExports = exportRequestDAO.getExportRequests("ALL", 1, 5);
        List<AuditLog> recentLogs = auditLogDAO.getRecentLogs(6);

        req.setAttribute("stockStats", stockStats);
        req.setAttribute("totalProducts", totalProducts);
        req.setAttribute("recentImports", recentImports);
        req.setAttribute("recentExports", recentExports);
        req.setAttribute("recentLogs", recentLogs);

        forward(req, resp, "dashboard/index.jsp");
    }
}
