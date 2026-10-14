package com.lims.controller.admin;

import com.lims.controller.BaseServlet;
import com.lims.dao.AuditLogDAO;
import com.lims.dao.RoleDAO;
import com.lims.dao.UserDAO;
import com.lims.model.AuditLog;
import com.lims.model.Role;
import com.lims.model.User;
import com.lims.util.PasswordUtils;
import com.lims.util.ValidationUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Controller for Account Management (FE-15, UC-62 to UC-66, UC-71)
 */
@WebServlet(name = "AdminUserServlet", urlPatterns = {"/app/admin/users"})
public class AdminUserServlet extends BaseServlet {

    private final UserDAO userDAO = new UserDAO();
    private final RoleDAO roleDAO = new RoleDAO();
    private final AuditLogDAO auditLogDAO = new AuditLogDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String keyword = req.getParameter("keyword");
        int roleId = ValidationUtils.parsePositiveInt(req.getParameter("roleId"), 0);
        String status = req.getParameter("status");
        int page = ValidationUtils.parsePositiveInt(req.getParameter("page"), 1);
        int pageSize = 10;

        List<User> users = userDAO.searchUsers(keyword, roleId > 0 ? roleId : null, status, page, pageSize);
        int totalUsers = userDAO.countUsers(keyword, roleId > 0 ? roleId : null, status);
        int totalPages = (int) Math.ceil((double) totalUsers / pageSize);

        List<Role> allRoles = roleDAO.getAllRoles();
        Map<String, Integer> userStats = userDAO.getUserStatistics();

        req.setAttribute("users", users);
        req.setAttribute("allRoles", allRoles);
        req.setAttribute("userStats", userStats);
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("totalUsers", totalUsers);
        req.setAttribute("keyword", keyword);
        req.setAttribute("selectedRoleId", roleId);
        req.setAttribute("selectedStatus", status);

        forward(req, resp, "admin/users.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User currentUser = getCurrentUser(req);
        String action = req.getParameter("action");

        if ("create".equalsIgnoreCase(action)) {
            // UC-63: Create User Account
            String username = req.getParameter("username");
            String password = req.getParameter("password");
            String fullName = req.getParameter("fullName");
            String email = req.getParameter("email");
            String phone = req.getParameter("phone");
            String status = req.getParameter("status");
            String[] roleIdsParam = req.getParameterValues("roleIds");

            if (ValidationUtils.isNullOrBlank(username) || ValidationUtils.isNullOrBlank(fullName) ||
                !ValidationUtils.isValidEmail(email) || ValidationUtils.isNullOrBlank(password)) {
                redirect(req, resp, "/app/admin/users?error=invalid_data");
                return;
            }

            // Check if username or email already exists
            if (userDAO.findByUsername(username.trim()) != null) {
                redirect(req, resp, "/app/admin/users?error=username_exists");
                return;
            }

            User newUser = new User();
            newUser.setUsername(username.trim());
            newUser.setPasswordHash(PasswordUtils.hashPassword(password.trim()));
            newUser.setFullName(fullName.trim());
            newUser.setEmail(email.trim());
            newUser.setPhone(phone != null ? phone.trim() : "");
            newUser.setStatus(status != null ? status : "ACTIVE");

            List<Integer> roleIds = new ArrayList<>();
            if (roleIdsParam != null) {
                for (String rId : roleIdsParam) {
                    int r = ValidationUtils.parsePositiveInt(rId, 0);
                    if (r > 0) roleIds.add(r);
                }
            }

            boolean ok = userDAO.createUser(newUser, roleIds);
            if (ok) {
                auditLogDAO.log(new AuditLog(
                        currentUser != null ? currentUser.getUserId() : null,
                        currentUser != null ? currentUser.getUsername() : "admin",
                        "CREATE", "USERS", String.valueOf(newUser.getUserId()),
                        null, "Created user account: " + newUser.getUsername() + " (" + newUser.getFullName() + ")",
                        getClientIP(req), req.getHeader("User-Agent")
                ));
                redirect(req, resp, "/app/admin/users?success=created");
            } else {
                redirect(req, resp, "/app/admin/users?error=create_failed");
            }

        } else if ("update".equalsIgnoreCase(action)) {
            // UC-64: Update User Account & UC-71: Assign Roles
            int userId = ValidationUtils.parsePositiveInt(req.getParameter("userId"), 0);
            String fullName = req.getParameter("fullName");
            String email = req.getParameter("email");
            String phone = req.getParameter("phone");
            String status = req.getParameter("status");
            String[] roleIdsParam = req.getParameterValues("roleIds");

            if (userId <= 0 || ValidationUtils.isNullOrBlank(fullName) || !ValidationUtils.isValidEmail(email)) {
                redirect(req, resp, "/app/admin/users?error=invalid_data");
                return;
            }

            User user = userDAO.findById(userId);
            if (user == null) {
                redirect(req, resp, "/app/admin/users?error=not_found");
                return;
            }

            user.setFullName(fullName.trim());
            user.setEmail(email.trim());
            user.setPhone(phone != null ? phone.trim() : "");
            user.setStatus(status != null ? status : "ACTIVE");

            List<Integer> roleIds = new ArrayList<>();
            if (roleIdsParam != null) {
                for (String rId : roleIdsParam) {
                    int r = ValidationUtils.parsePositiveInt(rId, 0);
                    if (r > 0) roleIds.add(r);
                }
            }

            boolean ok = userDAO.updateUserAndRoles(user, roleIds);
            if (ok) {
                auditLogDAO.log(new AuditLog(
                        currentUser != null ? currentUser.getUserId() : null,
                        currentUser != null ? currentUser.getUsername() : "admin",
                        "UPDATE", "USERS", String.valueOf(userId),
                        null, "Updated account info and roles for: " + user.getUsername(),
                        getClientIP(req), req.getHeader("User-Agent")
                ));
                redirect(req, resp, "/app/admin/users?success=updated");
            } else {
                redirect(req, resp, "/app/admin/users?error=update_failed");
            }

        } else if ("status".equalsIgnoreCase(action)) {
            // UC-65: Lock / Unlock / Inactive account
            int userId = ValidationUtils.parsePositiveInt(req.getParameter("userId"), 0);
            String newStatus = req.getParameter("newStatus");

            if (userId > 0 && newStatus != null) {
                boolean ok = userDAO.updateStatus(userId, newStatus);
                if (ok) {
                    auditLogDAO.log(new AuditLog(
                            currentUser != null ? currentUser.getUserId() : null,
                            currentUser != null ? currentUser.getUsername() : "admin",
                            "UPDATE_STATUS", "USERS", String.valueOf(userId),
                            null, "Changed user #" + userId + " status to " + newStatus,
                            getClientIP(req), req.getHeader("User-Agent")
                    ));
                    redirect(req, resp, "/app/admin/users?success=status_changed");
                    return;
                }
            }
            redirect(req, resp, "/app/admin/users?error=status_failed");

        } else if ("reset_password".equalsIgnoreCase(action)) {
            // UC-66: Reset User Password
            int userId = ValidationUtils.parsePositiveInt(req.getParameter("userId"), 0);
            String newPassword = req.getParameter("newPassword");

            if (userId > 0 && !ValidationUtils.isNullOrBlank(newPassword)) {
                boolean ok = userDAO.resetPassword(userId, newPassword.trim());
                if (ok) {
                    auditLogDAO.log(new AuditLog(
                            currentUser != null ? currentUser.getUserId() : null,
                            currentUser != null ? currentUser.getUsername() : "admin",
                            "RESET_PASSWORD", "USERS", String.valueOf(userId),
                            null, "Reset password for user #" + userId,
                            getClientIP(req), req.getHeader("User-Agent")
                    ));
                    redirect(req, resp, "/app/admin/users?success=password_reset");
                    return;
                }
            }
            redirect(req, resp, "/app/admin/users?error=reset_failed");
        }
    }
}

