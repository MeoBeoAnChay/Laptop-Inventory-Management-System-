package com.lims.filter;

import com.lims.model.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Role-Based Access Control (RBAC) Filter for the 5 system roles:
 * - Admin
 * - Manager
 * - Warehouse Staff
 * - Sales Staff
 * - Purchasing Staff
 */
@WebFilter(filterName = "RBACFilter", urlPatterns = {"/app/*"})
public class RBACFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("currentUser") : null;

        if (user == null) {
            chain.doFilter(request, response);
            return;
        }

        // Admin has universal superuser access
        if (user.hasRole("Admin")) {
            chain.doFilter(request, response);
            return;
        }

        String path = req.getServletPath();

        // 1. Admin only paths
        if (path.startsWith("/app/admin") || path.startsWith("/app/users")) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied: Requires Administrator privilege.");
            return;
        }

        // 2. Import module paths
        if (path.startsWith("/app/import")) {
            if (!user.hasRole("Purchasing Staff") && !user.hasRole("Warehouse Staff") && !user.hasRole("Manager")) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied: Unauthorized for import operations.");
                return;
            }
        }

        // 3. Export module paths
        if (path.startsWith("/app/export")) {
            if (!user.hasRole("Sales Staff") && !user.hasRole("Warehouse Staff") && !user.hasRole("Manager")) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied: Unauthorized for export operations.");
                return;
            }
        }

        // 4. Warehouse & Stocktaking paths
        if (path.startsWith("/app/inventory-check") || path.startsWith("/app/stocktake")) {
            if (!user.hasRole("Warehouse Staff") && !user.hasRole("Manager")) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied: Unauthorized for stocktaking.");
                return;
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {}
}

