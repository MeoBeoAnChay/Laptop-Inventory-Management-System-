<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<aside id="sidebar" class="w-64 bg-slate-900 text-slate-300 flex-shrink-0 min-h-screen flex flex-col justify-between transition-all">
    <div>
        <!-- App Title in Sidebar -->
        <div class="px-6 py-5 border-b border-slate-800 flex items-center space-x-3">
            <div class="w-8 h-8 rounded-lg bg-blue-600 flex items-center justify-center text-white">
                <i class="fa-solid fa-boxes-stacked"></i>
            </div>
            <div>
                <div class="text-sm font-bold text-white tracking-wide">LIMS ENTERPRISE</div>
                <div class="text-[11px] text-slate-400">Inventory Management</div>
            </div>
        </div>

        <!-- Navigation Menu -->
        <nav class="px-3 py-4 space-y-1">
            <!-- 1. ADMIN MENU (Ưu tiên hiển thị đầy đủ nghiệp vụ Admin theo DOC) -->
            <c:if test="${sessionScope.currentUser.hasRole('Admin')}">
                <div class="pb-1 px-3 text-[11px] font-semibold uppercase tracking-wider text-blue-400 flex items-center justify-between">
                    <span>Nghiệp vụ Quản trị</span>
                    <i class="fa-solid fa-crown text-amber-400 text-xs"></i>
                </div>

                <a href="${pageContext.request.contextPath}/app/dashboard"
                   class="flex items-center space-x-3 px-3 py-2.5 rounded-lg text-sm font-medium transition-colors ${pageContext.request.servletPath == '/app/dashboard' ? 'bg-blue-600 text-white shadow-sm' : 'hover:bg-slate-800 hover:text-white'}">
                    <i class="fa-solid fa-chart-line w-5 text-center"></i>
                    <span>Tổng quan Quản trị</span>
                </a>

                <a href="${pageContext.request.contextPath}/app/admin/users"
                   class="flex items-center space-x-3 px-3 py-2.5 rounded-lg text-sm font-medium transition-colors ${pageContext.request.servletPath.startsWith('/app/admin/users') ? 'bg-blue-600 text-white shadow-sm' : 'hover:bg-slate-800 hover:text-white'}">
                    <i class="fa-solid fa-users-gear w-5 text-center"></i>
                    <span>Quản lý Tài khoản (UC-62)</span>
                </a>

                <a href="${pageContext.request.contextPath}/app/admin/roles"
                   class="flex items-center space-x-3 px-3 py-2.5 rounded-lg text-sm font-medium transition-colors ${pageContext.request.servletPath.startsWith('/app/admin/roles') ? 'bg-blue-600 text-white shadow-sm' : 'hover:bg-slate-800 hover:text-white'}">
                    <i class="fa-solid fa-shield-halved w-5 text-center"></i>
                    <span>Vai trò & Quyền (UC-67)</span>
                </a>

                <a href="${pageContext.request.contextPath}/app/admin/audit"
                   class="flex items-center space-x-3 px-3 py-2.5 rounded-lg text-sm font-medium transition-colors ${pageContext.request.servletPath.startsWith('/app/admin/audit') ? 'bg-blue-600 text-white shadow-sm' : 'hover:bg-slate-800 hover:text-white'}">
                    <i class="fa-solid fa-clock-rotate-left w-5 text-center"></i>
                    <span>Nhật ký Kiểm toán (UC-78)</span>
                </a>

                <a href="${pageContext.request.contextPath}/app/admin/config"
                   class="flex items-center space-x-3 px-3 py-2.5 rounded-lg text-sm font-medium transition-colors ${pageContext.request.servletPath.startsWith('/app/admin/config') ? 'bg-blue-600 text-white shadow-sm' : 'hover:bg-slate-800 hover:text-white'}">
                    <i class="fa-solid fa-sliders w-5 text-center"></i>
                    <span>Cấu hình Hệ thống (UC-75)</span>
                </a>

                <div class="pt-4 pb-1 px-3 text-[11px] font-semibold uppercase tracking-wider text-slate-500">
                    Giám sát Kho & Kinh doanh
                </div>
            </c:if>

            <!-- 2. DASHBOARD CHO CÁC ROLE KHÁC -->
            <c:if test="${!sessionScope.currentUser.hasRole('Admin')}">
                <a href="${pageContext.request.contextPath}/app/dashboard"
                   class="flex items-center space-x-3 px-3 py-2.5 rounded-lg text-sm font-medium transition-colors ${pageContext.request.servletPath == '/app/dashboard' ? 'bg-blue-600 text-white shadow-sm' : 'hover:bg-slate-800 hover:text-white'}">
                    <i class="fa-solid fa-chart-pie w-5 text-center"></i>
                    <span>Tổng quan Kho</span>
                </a>
            </c:if>

            <!-- 3. QUẢN LÝ SẢN PHẨM & TỒN KHO -->
            <a href="${pageContext.request.contextPath}/app/products"
               class="flex items-center space-x-3 px-3 py-2.5 rounded-lg text-sm font-medium transition-colors ${pageContext.request.servletPath == '/app/products' ? 'bg-blue-600 text-white shadow-sm' : 'hover:bg-slate-800 hover:text-white'}">
                <i class="fa-solid fa-laptop w-5 text-center"></i>
                <span>Danh mục Laptop</span>
            </a>

            <a href="${pageContext.request.contextPath}/app/inventory"
               class="flex items-center space-x-3 px-3 py-2.5 rounded-lg text-sm font-medium transition-colors ${pageContext.request.servletPath == '/app/inventory' ? 'bg-blue-600 text-white shadow-sm' : 'hover:bg-slate-800 hover:text-white'}">
                <i class="fa-solid fa-warehouse w-5 text-center"></i>
                <span>Tồn kho (Serial & Vị trí)</span>
            </a>

            <!-- 4. GIAO DỊCH KHO (NHẬP / XUẤT) -->
            <a href="${pageContext.request.contextPath}/app/import"
               class="flex items-center space-x-3 px-3 py-2.5 rounded-lg text-sm font-medium transition-colors ${pageContext.request.servletPath == '/app/import' ? 'bg-blue-600 text-white shadow-sm' : 'hover:bg-slate-800 hover:text-white'}">
                <i class="fa-solid fa-truck-ramp-box w-5 text-center"></i>
                <span>Yêu cầu Nhập kho</span>
            </a>

            <a href="${pageContext.request.contextPath}/app/export"
               class="flex items-center space-x-3 px-3 py-2.5 rounded-lg text-sm font-medium transition-colors ${pageContext.request.servletPath == '/app/export' ? 'bg-blue-600 text-white shadow-sm' : 'hover:bg-slate-800 hover:text-white'}">
                <i class="fa-solid fa-dolly w-5 text-center"></i>
                <span>Yêu cầu Xuất kho</span>
            </a>

            <!-- 5. TÀI KHOẢN CÁ NHÂN -->
            <div class="pt-4 pb-1 px-3 text-[11px] font-semibold uppercase tracking-wider text-slate-500">
                Tài khoản cá nhân
            </div>

            <a href="${pageContext.request.contextPath}/app/profile"
               class="flex items-center space-x-3 px-3 py-2.5 rounded-lg text-sm font-medium transition-colors ${pageContext.request.servletPath == '/app/profile' ? 'bg-blue-600 text-white shadow-sm' : 'hover:bg-slate-800 hover:text-white'}">
                <i class="fa-solid fa-user-gear w-5 text-center"></i>
                <span>Hồ sơ & Đổi mật khẩu</span>
            </a>
        </nav>
    </div>

    <!-- Bottom Account Info -->
    <div class="p-4 border-t border-slate-800">
        <div class="flex items-center justify-between text-xs text-slate-400">
            <span>Microsoft SQL Server</span>
            <span class="inline-flex items-center text-emerald-400 font-medium">
                <span class="w-1.5 h-1.5 bg-emerald-400 rounded-full mr-1.5"></span> Connected
            </span>
        </div>
    </div>
</aside>
