package com.lims.controller.admin;

import com.lims.controller.BaseServlet;
import com.lims.dao.AuditLogDAO;
import com.lims.dao.RoleDAO;
import com.lims.dao.UserDAO;
import com.lims.model.AuditLog;
import com.lims.model.Role;
import com.lims.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller for Role & Permission Management (FE-16, UC-67 to UC-74)
 */
@WebServlet(name = "AdminRoleServlet", urlPatterns = {"/app/admin/roles"})
public class AdminRoleServlet extends BaseServlet {

    private final RoleDAO roleDAO = new RoleDAO();
    private final UserDAO userDAO = new UserDAO();
    private final AuditLogDAO auditLogDAO = new AuditLogDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Role> roles = roleDAO.getAllRoles();
        Map<String, Integer> userStats = userDAO.getUserStatistics();

        // Standard predefined permissions matrix matching SRS document
        Map<String, List<String>> rolePermissions = new HashMap<>();
        rolePermissions.put("Admin", List.of(
                "Toàn quyền Quản trị Tài khoản (UC-62 -> UC-66)",
                "Toàn quyền Quản lý Vai trò & Phân quyền (UC-67 -> UC-74)",
                "Toàn quyền Giám sát & Nhật ký Kiểm toán (UC-78, UC-79)",
                "Cấu hình Tham số Hệ thống (UC-75 -> UC-77)",
                "Xem dữ liệu Kho, Nhập, Xuất ở chế độ Giám sát Cấp cao"
        ));
        rolePermissions.put("Manager", List.of(
                "Xem Báo cáo Tổng quan Kho & Dashboard (UC-07)",
                "Phê duyệt / Từ chối Yêu cầu Nhập kho (UC-28)",
                "Phê duyệt / Từ chối Yêu cầu Xuất kho",
                "Quản lý Danh mục Laptop, Thương hiệu, Dòng máy",
                "Xem Báo cáo Đối soát Kiểm kê Kho"
        ));
        rolePermissions.put("Warehouse Staff", List.of(
                "Tiếp nhận Lô hàng Bàn giao (UC-05)",
                "Kiểm tra Thực tế, Lập Phiếu Nhập kho & Ghi nhận Serial (FE-04)",
                "Cập nhật Vị trí Lưu kho Kệ (Rack-Bin) và Tình trạng máy (FE-09, FE-10)",
                "Xử lý Đơn Xuất kho & Ghi nhận Serial máy xuất (FE-07)",
                "Thực hiện Phiếu Kiểm kê Kho Định kỳ (FE-11)"
        ));
        rolePermissions.put("Sales Staff", List.of(
                "Lập Yêu cầu Xuất kho Bán hàng (UC-54)",
                "Theo dõi Tiến độ Phê duyệt & Xử lý Đơn Xuất (UC-56)",
                "Tra cứu Danh mục Sản phẩm & Tồn kho Khả dụng (FE-01)",
                "Quản lý Thông tin Khách hàng Bán lẻ"
        ));
        rolePermissions.put("Purchasing Staff", List.of(
                "Tạo Yêu cầu Đặt mua Hàng từ Nhà phân phối (UC-01)",
                "Lập Biên bản Bàn giao Lô hàng cho Kho (UC-05)",
                "Theo dõi Tiến độ Đơn đặt hàng Nhập",
                "Quản lý Danh bạ Nhà phân phối / Cung cấp (FE-02)"
        ));

        req.setAttribute("roles", roles);
        req.setAttribute("userStats", userStats);
        req.setAttribute("rolePermissions", rolePermissions);

        forward(req, resp, "admin/roles.jsp");
    }
}

