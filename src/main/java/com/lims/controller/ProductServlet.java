package com.lims.controller;

import com.lims.dao.AuditLogDAO;
import com.lims.dao.BrandDAO;
import com.lims.dao.ProductDAO;
import com.lims.dao.ProductLineDAO;
import com.lims.model.AuditLog;
import com.lims.model.Brand;
import com.lims.model.Product;
import com.lims.model.ProductLine;
import com.lims.model.User;
import com.lims.util.ValidationUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet(name = "ProductServlet", urlPatterns = {"/app/products"})
public class ProductServlet extends BaseServlet {

    private final ProductDAO productDAO = new ProductDAO();
    private final BrandDAO brandDAO = new BrandDAO();
    private final ProductLineDAO productLineDAO = new ProductLineDAO();
    private final AuditLogDAO auditLogDAO = new AuditLogDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("lines".equalsIgnoreCase(action)) {
            int brandId = ValidationUtils.parsePositiveInt(req.getParameter("brandId"), 0);
            List<ProductLine> lines = productLineDAO.getLinesByBrand(brandId);
            sendJson(resp, lines);
            return;
        }

        String keyword = req.getParameter("keyword");
        int brandId = ValidationUtils.parsePositiveInt(req.getParameter("brandId"), 0);
        String status = req.getParameter("status");
        int page = ValidationUtils.parsePositiveInt(req.getParameter("page"), 1);
        int pageSize = 10;

        List<Product> products = productDAO.getProducts(keyword, brandId > 0 ? brandId : null, status, page, pageSize);
        int totalProducts = productDAO.countProducts(keyword, brandId > 0 ? brandId : null, status);
        int totalPages = (int) Math.ceil((double) totalProducts / pageSize);

        List<Brand> brands = brandDAO.getActiveBrands();
        List<ProductLine> productLines = productLineDAO.getAllProductLines();

        req.setAttribute("products", products);
        req.setAttribute("brands", brands);
        req.setAttribute("productLines", productLines);
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("totalProducts", totalProducts);
        req.setAttribute("keyword", keyword);
        req.setAttribute("selectedBrandId", brandId);
        req.setAttribute("selectedStatus", status);

        forward(req, resp, "products/list.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = getCurrentUser(req);
        String action = req.getParameter("action");

        if ("create".equalsIgnoreCase(action)) {
            String productCode = req.getParameter("productCode");
            String productName = req.getParameter("productName");
            int productLineId = ValidationUtils.parsePositiveInt(req.getParameter("productLineId"), 0);
            String cpu = req.getParameter("cpu");
            String ram = req.getParameter("ram");
            String storage = req.getParameter("storage");
            String gpu = req.getParameter("gpu");
            String screen = req.getParameter("screen");
            BigDecimal unitPrice = ValidationUtils.parseBigDecimal(req.getParameter("unitPrice"), BigDecimal.ZERO);
            String status = req.getParameter("status");

            if (ValidationUtils.isNullOrBlank(productCode) || ValidationUtils.isNullOrBlank(productName) || productLineId <= 0) {
                redirect(req, resp, "/app/products?error=invalid_data");
                return;
            }

            Product p = new Product(0, productCode.trim(), productName.trim(), productLineId,
                    cpu, ram, storage, gpu, screen, unitPrice, status != null ? status : "ACTIVE");

            boolean success = productDAO.createProduct(p);
            if (success) {
                auditLogDAO.log(new AuditLog(
                        user != null ? user.getUserId() : null,
                        user != null ? user.getUsername() : "system",
                        "CREATE", "PRODUCTS", String.valueOf(p.getProductId()),
                        null, "Created product " + p.getProductCode(),
                        getClientIP(req), req.getHeader("User-Agent")
                ));
                redirect(req, resp, "/app/products?success=created");
            } else {
                redirect(req, resp, "/app/products?error=create_failed");
            }
        }
    }
}

