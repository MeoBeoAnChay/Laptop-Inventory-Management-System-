package com.lims.controller;

import com.lims.dao.AuditLogDAO;
import com.lims.dao.CustomerDAO;
import com.lims.dao.ExportRequestDAO;
import com.lims.dao.ProductDAO;
import com.lims.model.AuditLog;
import com.lims.model.Customer;
import com.lims.model.ExportRequest;
import com.lims.model.ExportRequestDetail;
import com.lims.model.User;
import com.lims.util.ValidationUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "ExportRequestServlet", urlPatterns = {"/app/export"})
public class ExportRequestServlet extends BaseServlet {

    private final ExportRequestDAO exportRequestDAO = new ExportRequestDAO();
    private final CustomerDAO customerDAO = new CustomerDAO();
    private final ProductDAO productDAO = new ProductDAO();
    private final AuditLogDAO auditLogDAO = new AuditLogDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        if ("detail".equalsIgnoreCase(action)) {
            int id = ValidationUtils.parsePositiveInt(req.getParameter("id"), 0);
            ExportRequest request = exportRequestDAO.findById(id);
            if (request != null) {
                req.setAttribute("request", request);
                forward(req, resp, "exports/detail.jsp");
                return;
            }
        }

        String status = req.getParameter("status");
        int page = ValidationUtils.parsePositiveInt(req.getParameter("page"), 1);
        int pageSize = 10;

        List<ExportRequest> requests = exportRequestDAO.getExportRequests(status, page, pageSize);
        req.setAttribute("requests", requests);
        req.setAttribute("customers", customerDAO.getAllCustomers());
        req.setAttribute("products", productDAO.getProducts(null, null, "ACTIVE", 1, 100));
        req.setAttribute("selectedStatus", status);

        forward(req, resp, "exports/list.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = getCurrentUser(req);
        String action = req.getParameter("action");

        if ("create".equalsIgnoreCase(action)) {
            int customerId = ValidationUtils.parsePositiveInt(req.getParameter("customerId"), 0);
            String customerName = req.getParameter("customerName");
            String customerPhone = req.getParameter("customerPhone");

            if (customerId <= 0 && (!ValidationUtils.isNullOrBlank(customerName) && !ValidationUtils.isNullOrBlank(customerPhone))) {
                Customer newCust = new Customer(0, customerName.trim(), customerPhone.trim(), null, null);
                if (customerDAO.createCustomer(newCust)) {
                    customerId = newCust.getCustomerId();
                }
            }

            String[] productIds = req.getParameterValues("productId");
            String[] quantities = req.getParameterValues("quantity");

            if (customerId <= 0 || productIds == null || productIds.length == 0) {
                redirect(req, resp, "/app/export?error=invalid_data");
                return;
            }

            ExportRequest request = new ExportRequest();
            request.setCustomerId(customerId);
            request.setCreatedBy(user != null ? user.getUserId() : 1);

            List<ExportRequestDetail> details = new ArrayList<>();
            for (int i = 0; i < productIds.length; i++) {
                int pId = ValidationUtils.parsePositiveInt(productIds[i], 0);
                int qty = (quantities != null && i < quantities.length) ? ValidationUtils.parsePositiveInt(quantities[i], 1) : 1;

                if (pId > 0 && qty > 0) {
                    details.add(new ExportRequestDetail(0, 0, pId, qty));
                }
            }

            boolean ok = exportRequestDAO.createRequest(request, details);
            if (ok) {
                auditLogDAO.log(new AuditLog(
                        user != null ? user.getUserId() : null,
                        user != null ? user.getUsername() : "system",
                        "CREATE", "EXPORT_REQUESTS", String.valueOf(request.getExportRequestId()),
                        null, "Created export request #" + request.getExportRequestId(),
                        getClientIP(req), req.getHeader("User-Agent")
                ));
                redirect(req, resp, "/app/export?success=created");
            } else {
                redirect(req, resp, "/app/export?error=create_failed");
            }
        } else if ("status".equalsIgnoreCase(action)) {
            int id = ValidationUtils.parsePositiveInt(req.getParameter("id"), 0);
            String newStatus = req.getParameter("newStatus");
            String reason = req.getParameter("reason");

            if (id > 0 && newStatus != null) {
                boolean ok = exportRequestDAO.updateStatus(id, newStatus, user != null ? user.getUserId() : null, reason);
                if (ok) {
                    auditLogDAO.log(new AuditLog(
                            user != null ? user.getUserId() : null,
                            user != null ? user.getUsername() : "system",
                            "UPDATE_STATUS", "EXPORT_REQUESTS", String.valueOf(id),
                            null, "Updated status to " + newStatus,
                            getClientIP(req), req.getHeader("User-Agent")
                    ));
                    redirect(req, resp, "/app/export?success=status_updated");
                    return;
                }
            }
            redirect(req, resp, "/app/export?error=status_failed");
        }
    }
}

