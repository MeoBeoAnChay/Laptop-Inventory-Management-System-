package com.lims.controller.auth;

import com.lims.controller.BaseServlet;
import com.lims.dao.AuditLogDAO;
import com.lims.dao.UserDAO;
import com.lims.model.AuditLog;
import com.lims.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends BaseServlet {

    private final UserDAO userDAO = new UserDAO();
    private final AuditLogDAO auditLogDAO = new AuditLogDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = getCurrentUser(req);
        if (user != null) {
            redirect(req, resp, "/app/dashboard");
            return;
        }
        forward(req, resp, "auth/login.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            req.setAttribute("errorMessage", "Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu.");
            forward(req, resp, "auth/login.jsp");
            return;
        }

        User user = userDAO.authenticate(username.trim(), password.trim());

        if (user != null) {
            HttpSession session = req.getSession(true);
            session.setAttribute("currentUser", user);

            // Audit Log
            AuditLog log = new AuditLog(
                    user.getUserId(),
                    user.getUsername(),
                    "LOGIN",
                    "USERS",
                    String.valueOf(user.getUserId()),
                    null,
                    "Login successful",
                    getClientIP(req),
                    req.getHeader("User-Agent")
            );
            auditLogDAO.log(log);

            String redirectTarget = (String) session.getAttribute("redirectAfterLogin");
            if (redirectTarget != null && !redirectTarget.isEmpty()) {
                session.removeAttribute("redirectAfterLogin");
                resp.sendRedirect(redirectTarget);
            } else {
                redirect(req, resp, "/app/dashboard");
            }
        } else {
            req.setAttribute("errorMessage", "Tên đăng nhập hoặc mật khẩu không chính xác.");
            req.setAttribute("username", username);
            forward(req, resp, "auth/login.jsp");
        }
    }
}

