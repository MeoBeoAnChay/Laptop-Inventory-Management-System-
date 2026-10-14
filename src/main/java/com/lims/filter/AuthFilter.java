package com.lims.filter;

import com.lims.model.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Filter ensuring users are authenticated before accessing protected application resources (/app/*).
 */
@WebFilter(filterName = "AuthFilter", urlPatterns = {"/app/*"})
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        HttpSession session = httpRequest.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("currentUser") : null;

        if (currentUser == null || !"ACTIVE".equalsIgnoreCase(currentUser.getStatus())) {
            // Save requested URL for post-login redirect
            String requestURI = httpRequest.getRequestURI();
            String queryString = httpRequest.getQueryString();
            String redirectUrl = (queryString == null) ? requestURI : requestURI + "?" + queryString;

            httpRequest.getSession(true).setAttribute("redirectAfterLogin", redirectUrl);
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login?error=session_expired");
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {}
}

