package com.lims.controller;

import com.lims.dao.AuditLogDAO;
import com.lims.dao.UserDAO;
import com.lims.model.AuditLog;
import com.lims.model.User;
import com.lims.util.PasswordUtils;
import com.lims.util.ValidationUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Controller for User Profile & Security (UC-59 to UC-61)
 */
@WebServlet(name = "ProfileServlet", urlPatterns = {"/app/profile"})
public class ProfileServlet extends BaseServlet {

    private final UserDAO userDAO = new UserDAO();
    private final AuditLogDAO auditLogDAO = new AuditLogDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User sessionUser = getCurrentUser(req);
        if (sessionUser == null) {
            redirect(req, resp, "/login");
            return;
        }

        // Refresh user info from database
        User freshUser = userDAO.findById(sessionUser.getUserId());
        req.setAttribute("user", freshUser != null ? freshUser : sessionUser);

        forward(req, resp, "profile/index.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User sessionUser = getCurrentUser(req);
        if (sessionUser == null) {
            redirect(req, resp, "/login");
            return;
        }

        String action = req.getParameter("action");

        if ("update_profile".equalsIgnoreCase(action)) {
            // UC-60: Update Profile
            String fullName = req.getParameter("fullName");
            String email = req.getParameter("email");
            String phone = req.getParameter("phone");

            if (ValidationUtils.isNullOrBlank(fullName) || !ValidationUtils.isValidEmail(email)) {
                redirect(req, resp, "/app/profile?error=invalid_profile_data");
                return;
            }

            User fresh = userDAO.findById(sessionUser.getUserId());
            fresh.setFullName(fullName.trim());
            fresh.setEmail(email.trim());
            fresh.setPhone(phone != null ? phone.trim() : "");

            boolean ok = userDAO.updateUser(fresh);
            if (ok) {
                // Update session
                sessionUser.setFullName(fresh.getFullName());
                sessionUser.setEmail(fresh.getEmail());
                sessionUser.setPhone(fresh.getPhone());
                req.getSession().setAttribute("currentUser", sessionUser);

                auditLogDAO.log(new AuditLog(
                        sessionUser.getUserId(), sessionUser.getUsername(),
                        "UPDATE_PROFILE", "USERS", String.valueOf(sessionUser.getUserId()),
                        null, "Updated personal profile information",
                        getClientIP(req), req.getHeader("User-Agent")
                ));

                redirect(req, resp, "/app/profile?success=profile_updated");
            } else {
                redirect(req, resp, "/app/profile?error=update_failed");
            }

        } else if ("change_password".equalsIgnoreCase(action)) {
            // UC-61: Change Password
            String currentPassword = req.getParameter("currentPassword");
            String newPassword = req.getParameter("newPassword");
            String confirmPassword = req.getParameter("confirmPassword");

            if (ValidationUtils.isNullOrBlank(currentPassword) || ValidationUtils.isNullOrBlank(newPassword)) {
                redirect(req, resp, "/app/profile?error=missing_password");
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                redirect(req, resp, "/app/profile?error=password_mismatch");
                return;
            }

            User fresh = userDAO.findById(sessionUser.getUserId());
            if (!PasswordUtils.checkPassword(currentPassword, fresh.getPasswordHash())) {
                redirect(req, resp, "/app/profile?error=wrong_current_password");
                return;
            }

            String hashed = PasswordUtils.hashPassword(newPassword.trim());
            boolean ok = userDAO.updatePassword(sessionUser.getUserId(), hashed);
            if (ok) {
                auditLogDAO.log(new AuditLog(
                        sessionUser.getUserId(), sessionUser.getUsername(),
                        "CHANGE_PASSWORD", "USERS", String.valueOf(sessionUser.getUserId()),
                        null, "Changed account password",
                        getClientIP(req), req.getHeader("User-Agent")
                ));
                redirect(req, resp, "/app/profile?success=password_changed");
            } else {
                redirect(req, resp, "/app/profile?error=password_change_failed");
            }
        }
    }
}

