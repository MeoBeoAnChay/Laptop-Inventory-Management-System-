package com.lims.controller;

import com.lims.dao.AuditLogDAO;
import com.lims.dao.ImportRequestDAO;
import com.lims.dao.ProductDAO;
import com.lims.dao.SupplierDAO;
import com.lims.model.AuditLog;
import com.lims.model.ImportRequest;
import com.lims.model.ImportRequestDetail;
import com.lims.model.User;
import com.lims.util.ValidationUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "ImportRequestServlet", urlPatterns = {"/app/import"})
public class ImportRequestServlet extends BaseServlet {

    private final ImportRequestDAO importRequestDAO = new ImportRequestDAO();
    private final SupplierDAO supplierDAO = new SupplierDAO();
    private final ProductDAO productDAO = new ProductDAO();
    private final AuditLogDAO auditLogDAO = new AuditLogDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        if ("detail".equalsIgnoreCase(action)) {
            int id = ValidationUtils.parsePositiveInt(req.getParameter("id"), 0);
            ImportRequest request = importRequestDAO.findById(id);
            if (request != null) {
                req.setAttribute("request", request);
                forward(req, resp, "imports/detail.jsp");
                return;
            }
        }

        String status = req.getParameter("status");
        int page = ValidationUtils.parsePositiveInt(req.getParameter("page"), 1);
        int pageSize = 10;

        List<ImportRequest> requests = importRequestDAO.getImportRequests(status, page, pageSize);
        req.setAttribute("requests", requests);
        req.setAttribute("suppliers", supplierDAO.getActiveSuppliers());
        req.setAttribute("products", productDAO.getProducts(null, null, "ACTIVE", 1, 100));
        req.setAttribute("selectedStatus", status);

        forward(req, resp, "imports/list.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = getCurrentUser(req);
        String action = req.getParameter("action");

        if ("create".equalsIgnoreCase(action)) {
            int supplierId = ValidationUtils.parsePositiveInt(req.getParameter("supplierId"), 0);
            String[] productIds = req.getParameterValues("productId");
            String[] quantities = req.getParameterValues("quantity");
            String[] unitPrices = req.getParameterValues("unitPrice");

            if (supplierId <= 0 || productIds == null || productIds.length == 0) {
                redirect(req, resp, "/app/import?error=invalid_data");
                return;
            }

            ImportRequest request = new ImportRequest();
            request.setSupplierId(supplierId);
            request.setCreatedBy(user != null ? user.getUserId() : 1);

            List<ImportRequestDetail> details = new ArrayList<>();
            for (int i = 0; i < productIds.length; i++) {
                int pId = ValidationUtils.parsePositiveInt(productIds[i], 0);
                int qty = (quantities != null && i < quantities.length) ? ValidationUtils.parsePositiveInt(quantities[i], 1) : 1;
                BigDecimal price = (unitPrices != null && i < unitPrices.length) ? ValidationUtils.parseBigDecimal(unitPrices[i], BigDecimal.ZERO) : BigDecimal.ZERO;

                if (pId > 0 && qty > 0) {
                    details.add(new ImportRequestDetail(0, 0, pId, qty, price));
                }
            }

            boolean ok = importRequestDAO.createRequest(request, details);
            if (ok) {
                auditLogDAO.log(new AuditLog(
                        user != null ? user.getUserId() : null,
                        user != null ? user.getUsername() : "system",
                        "CREATE", "IMPORT_REQUESTS", String.valueOf(request.getImportRequestId()),
                        null, "Created import request #" + request.getImportRequestId(),
                        getClientIP(req), req.getHeader("User-Agent")
                ));
                redirect(req, resp, "/app/import?success=created");
            } else {
                redirect(req, resp, "/app/import?error=create_failed");
            }
        } else if ("status".equalsIgnoreCase(action)) {
            int id = ValidationUtils.parsePositiveInt(req.getParameter("id"), 0);
            String newStatus = req.getParameter("newStatus");
            String reason = req.getParameter("reason");

            if (id > 0 && newStatus != null) {
                boolean ok = importRequestDAO.updateStatus(id, newStatus, user != null ? user.getUserId() : null, reason);
                if (ok) {
                    auditLogDAO.log(new AuditLog(
                            user != null ? user.getUserId() : null,
                            user != null ? user.getUsername() : "system",
                            "UPDATE_STATUS", "IMPORT_REQUESTS", String.valueOf(id),
                            null, "Updated status to " + newStatus,
                            getClientIP(req), req.getHeader("User-Agent")
                    ));
                    redirect(req, resp, "/app/import?success=status_updated");
                    return;
                }
            }
            redirect(req, resp, "/app/import?error=status_failed");
        }
    }
}

