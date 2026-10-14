package com.lims.controller;

import com.lims.dao.AuditLogDAO;
import com.lims.dao.InventoryDAO;
import com.lims.model.AuditLog;
import com.lims.model.Inventory;
import com.lims.model.User;
import com.lims.util.ValidationUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "InventoryServlet", urlPatterns = {"/app/inventory"})
public class InventoryServlet extends BaseServlet {

    private final InventoryDAO inventoryDAO = new InventoryDAO();
    private final AuditLogDAO auditLogDAO = new AuditLogDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String keyword = req.getParameter("keyword");
        String status = req.getParameter("status");
        String location = req.getParameter("location");
        int page = ValidationUtils.parsePositiveInt(req.getParameter("page"), 1);
        int pageSize = 12;

        List<Inventory> inventoryList = inventoryDAO.getInventory(keyword, status, location, page, pageSize);
        int totalItems = inventoryDAO.countInventory(keyword, status, location);
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);

        req.setAttribute("inventoryList", inventoryList);
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("totalItems", totalItems);
        req.setAttribute("keyword", keyword);
        req.setAttribute("selectedStatus", status);
        req.setAttribute("selectedLocation", location);

        forward(req, resp, "inventory/list.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = getCurrentUser(req);
        String action = req.getParameter("action");

        if ("update".equalsIgnoreCase(action)) {
            int inventoryId = ValidationUtils.parsePositiveInt(req.getParameter("inventoryId"), 0);
            String newStatus = req.getParameter("newStatus");
            String newLocation = req.getParameter("newLocation");

            if (inventoryId > 0 && !ValidationUtils.isNullOrBlank(newStatus) && !ValidationUtils.isNullOrBlank(newLocation)) {
                boolean success = inventoryDAO.updateStatusAndLocation(inventoryId, newStatus, newLocation);
                if (success) {
                    auditLogDAO.log(new AuditLog(
                            user != null ? user.getUserId() : null,
                            user != null ? user.getUsername() : "system",
                            "UPDATE", "INVENTORY", String.valueOf(inventoryId),
                            null, "Updated to status: " + newStatus + ", location: " + newLocation,
                            getClientIP(req), req.getHeader("User-Agent")
                    ));
                    redirect(req, resp, "/app/inventory?success=updated");
                    return;
                }
            }
            redirect(req, resp, "/app/inventory?error=update_failed");
        }
    }
}

