<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">
<jsp:include page="/WEB-INF/views/common/head.jsp">
    <jsp:param name="title" value="Tổng quan Quản trị" />
</jsp:include>
<body class="bg-slate-50 text-slate-800 antialiased min-h-screen flex flex-col">

<jsp:include page="/WEB-INF/views/common/navbar.jsp" />

<div class="flex flex-1">
    <jsp:include page="/WEB-INF/views/common/sidebar.jsp" />

    <main class="flex-1 p-8 overflow-y-auto">
        <!-- Top Admin Header -->
        <div class="mb-8 flex flex-col md:flex-row md:items-center md:justify-between gap-4">
            <div>
                <div class="flex items-center space-x-2">
                    <span class="px-2.5 py-1 text-xs font-bold rounded-lg bg-blue-100 text-blue-700 uppercase tracking-wider">
                        <i class="fa-solid fa-crown mr-1 text-amber-500"></i> Hệ thống Quản trị (Admin)
                    </span>
                    <span class="text-xs text-slate-400">• Người phụ trách: Bùi Tuấn Phong</span>
                </div>
                <h1 class="text-2xl font-bold text-slate-900 tracking-tight mt-1.5">Bảng Điều Khiển Quản Trị Hệ Thống</h1>
                <p class="text-sm text-slate-500">Quản lý phân quyền 5 vai trò, tài khoản nhân viên, nhật ký an ninh và cấu hình hệ thống.</p>
            </div>
            <div class="flex items-center space-x-3">
                <a href="${pageContext.request.contextPath}/app/admin/users"
                   class="px-4 py-2.5 bg-blue-600 hover:bg-blue-700 text-white rounded-xl text-sm font-medium shadow-sm transition flex items-center space-x-2">
                    <i class="fa-solid fa-user-plus"></i>
                    <span>Tạo Tài Khoản Mới</span>
                </a>
                <a href="${pageContext.request.contextPath}/app/admin/audit"
                   class="px-4 py-2.5 bg-slate-800 hover:bg-slate-900 text-white rounded-xl text-sm font-medium shadow-sm transition flex items-center space-x-2">
                    <i class="fa-solid fa-shield-halved"></i>
                    <span>Nhật Ký Kiểm Toán</span>
                </a>
            </div>
        </div>

        <!-- 4 KPI Cards for Admin -->
        <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
            <!-- Total Accounts -->
            <div class="bg-white p-6 rounded-2xl border border-slate-200/80 shadow-sm flex items-center justify-between">
                <div>
                    <div class="text-xs font-semibold uppercase tracking-wider text-slate-400">Tổng số tài khoản</div>
                    <div class="text-3xl font-bold text-slate-900 mt-2">${userStats['total_users'] != null ? userStats['total_users'] : 0}</div>
                    <div class="text-xs text-blue-600 font-medium mt-1"><i class="fa-solid fa-users"></i> Nhân sự hệ thống</div>
                </div>
                <div class="w-12 h-12 rounded-xl bg-blue-50 text-blue-600 flex items-center justify-center text-xl">
                    <i class="fa-solid fa-address-book"></i>
                </div>
            </div>

            <!-- Active Accounts -->
            <div class="bg-white p-6 rounded-2xl border border-slate-200/80 shadow-sm flex items-center justify-between">
                <div>
                    <div class="text-xs font-semibold uppercase tracking-wider text-slate-400">Đang hoạt động</div>
                    <div class="text-3xl font-bold text-emerald-600 mt-2">${userStats['status_ACTIVE'] != null ? userStats['status_ACTIVE'] : 0}</div>
                    <div class="text-xs text-emerald-600 font-medium mt-1"><i class="fa-solid fa-circle-check"></i> Trạng thái ACTIVE</div>
                </div>
                <div class="w-12 h-12 rounded-xl bg-emerald-50 text-emerald-600 flex items-center justify-center text-xl">
                    <i class="fa-solid fa-user-check"></i>
                </div>
            </div>

            <!-- Locked / Inactive Accounts -->
            <div class="bg-white p-6 rounded-2xl border border-slate-200/80 shadow-sm flex items-center justify-between">
                <div>
                    <div class="text-xs font-semibold uppercase tracking-wider text-slate-400">Tài khoản bị khóa</div>
                    <div class="text-3xl font-bold text-rose-600 mt-2">
                        ${(userStats['status_LOCKED'] != null ? userStats['status_LOCKED'] : 0) + (userStats['status_INACTIVE'] != null ? userStats['status_INACTIVE'] : 0)}
                    </div>
                    <div class="text-xs text-rose-600 font-medium mt-1"><i class="fa-solid fa-lock"></i> Vô hiệu hóa / Khóa</div>
                </div>
                <div class="w-12 h-12 rounded-xl bg-rose-50 text-rose-600 flex items-center justify-center text-xl">
                    <i class="fa-solid fa-user-lock"></i>
                </div>
            </div>

            <!-- Total Roles -->
            <div class="bg-white p-6 rounded-2xl border border-slate-200/80 shadow-sm flex items-center justify-between">
                <div>
                    <div class="text-xs font-semibold uppercase tracking-wider text-slate-400">Vai trò hệ thống</div>
                    <div class="text-3xl font-bold text-indigo-600 mt-2">5</div>
                    <div class="text-xs text-indigo-600 font-medium mt-1"><i class="fa-solid fa-shield-halved"></i> Phân quyền RBAC</div>
                </div>
                <div class="w-12 h-12 rounded-xl bg-indigo-50 text-indigo-600 flex items-center justify-center text-xl">
                    <i class="fa-solid fa-id-badge"></i>
                </div>
            </div>
        </div>

        <!-- Role Personnel Distribution Banner -->
        <div class="bg-white rounded-2xl border border-slate-200/80 p-6 shadow-sm mb-8">
            <h2 class="text-sm font-bold text-slate-900 uppercase tracking-wider mb-4 flex items-center justify-between">
                <span>Phân bổ Nhân sự theo 5 Vai trò Hệ thống (Group 5 - SE1976)</span>
                <a href="${pageContext.request.contextPath}/app/admin/roles" class="text-xs font-medium text-blue-600 hover:underline">Chi tiết quyền hạn &rarr;</a>
            </h2>
            <div class="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-5 gap-4">
                <div class="p-4 rounded-xl bg-slate-50 border border-slate-200 text-center">
                    <div class="text-xs text-slate-500 font-medium">Administrator</div>
                    <div class="text-2xl font-bold text-slate-900 mt-1">${userStats['role_Admin'] != null ? userStats['role_Admin'] : 1}</div>
                    <div class="text-[11px] text-blue-600 font-semibold mt-1">Bùi Tuấn Phong</div>
                </div>
                <div class="p-4 rounded-xl bg-slate-50 border border-slate-200 text-center">
                    <div class="text-xs text-slate-500 font-medium">Manager</div>
                    <div class="text-2xl font-bold text-slate-900 mt-1">${userStats['role_Manager'] != null ? userStats['role_Manager'] : 1}</div>
                    <div class="text-[11px] text-blue-600 font-semibold mt-1">Nguyễn Văn Mạnh</div>
                </div>
                <div class="p-4 rounded-xl bg-slate-50 border border-slate-200 text-center">
                    <div class="text-xs text-slate-500 font-medium">Warehouse Staff</div>
                    <div class="text-2xl font-bold text-slate-900 mt-1">${userStats['role_Warehouse Staff'] != null ? userStats['role_Warehouse Staff'] : 1}</div>
                    <div class="text-[11px] text-blue-600 font-semibold mt-1">Phạm Huy Thái</div>
                </div>
                <div class="p-4 rounded-xl bg-slate-50 border border-slate-200 text-center">
                    <div class="text-xs text-slate-500 font-medium">Sales Staff</div>
                    <div class="text-2xl font-bold text-slate-900 mt-1">${userStats['role_Sales Staff'] != null ? userStats['role_Sales Staff'] : 1}</div>
                    <div class="text-[11px] text-blue-600 font-semibold mt-1">Bùi Tuấn Hiệp</div>
                </div>
                <div class="p-4 rounded-xl bg-slate-50 border border-slate-200 text-center">
                    <div class="text-xs text-slate-500 font-medium">Purchasing Staff</div>
                    <div class="text-2xl font-bold text-slate-900 mt-1">${userStats['role_Purchasing Staff'] != null ? userStats['role_Purchasing Staff'] : 1}</div>
                    <div class="text-[11px] text-blue-600 font-semibold mt-1">Nguyễn Tuấn Kiệt</div>
                </div>
            </div>
        </div>

        <!-- 2 Columns: Recent Accounts & Recent Audit Logs -->
        <div class="grid grid-cols-1 lg:grid-cols-2 gap-8 mb-8">
            <!-- Recent Users -->
            <div class="bg-white rounded-2xl border border-slate-200/80 shadow-sm overflow-hidden">
                <div class="px-6 py-4 border-b border-slate-100 flex items-center justify-between">
                    <h2 class="text-base font-bold text-slate-900 flex items-center space-x-2">
                        <i class="fa-solid fa-users text-blue-600"></i>
                        <span>Danh Sách Nhân Viên Hệ Thống</span>
                    </h2>
                    <a href="${pageContext.request.contextPath}/app/admin/users" class="text-xs font-medium text-blue-600 hover:underline">Quản lý tài khoản &rarr;</a>
                </div>
                <div class="divide-y divide-slate-100">
                    <c:forEach var="u" items="${recentUsers}">
                        <div class="p-4 px-6 flex items-center justify-between hover:bg-slate-50 transition">
                            <div class="flex items-center space-x-3">
                                <div class="w-10 h-10 rounded-full bg-blue-100 text-blue-700 font-bold flex items-center justify-center text-sm">
                                    ${u.username.substring(0, 1).toUpperCase()}
                                </div>
                                <div>
                                    <div class="text-sm font-semibold text-slate-800">${u.fullName} <span class="text-xs text-slate-400 font-normal">(@${u.username})</span></div>
                                    <div class="text-xs text-slate-400 mt-0.5">${u.email} • ${u.phone}</div>
                                </div>
                            </div>
                            <div class="text-right">
                                <div>
                                    <c:forEach var="r" items="${u.roles}">
                                        <span class="inline-block px-2 py-0.5 text-[11px] font-semibold bg-blue-50 text-blue-700 border border-blue-200 rounded-md">
                                            ${r.roleName}
                                        </span>
                                    </c:forEach>
                                </div>
                                <div class="mt-1">
                                    <span class="inline-flex items-center px-2 py-0.5 rounded text-[10px] font-bold ${u.status == 'ACTIVE' ? 'bg-emerald-50 text-emerald-700' : 'bg-rose-50 text-rose-700'}">
                                        ${u.status}
                                    </span>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>

            <!-- Recent Audit Logs -->
            <div class="bg-white rounded-2xl border border-slate-200/80 shadow-sm overflow-hidden">
                <div class="px-6 py-4 border-b border-slate-100 flex items-center justify-between">
                    <h2 class="text-base font-bold text-slate-900 flex items-center space-x-2">
                        <i class="fa-solid fa-clock-rotate-left text-slate-600"></i>
                        <span>Nhật Ký Thao Tác An Ninh Gần Đây</span>
                    </h2>
                    <a href="${pageContext.request.contextPath}/app/admin/audit" class="text-xs font-medium text-blue-600 hover:underline">Xem toàn bộ log &rarr;</a>
                </div>
                <div class="divide-y divide-slate-100">
                    <c:forEach var="log" items="${recentLogs}">
                        <div class="p-3.5 px-6 flex items-center justify-between hover:bg-slate-50 transition text-xs">
                            <div>
                                <div class="font-medium text-slate-800 flex items-center space-x-2">
                                    <span class="font-bold text-blue-600">@${log.username}</span>
                                    <span class="px-1.5 py-0.5 rounded bg-slate-100 text-slate-700 text-[10px] font-mono">${log.action}</span>
                                    <span class="text-slate-500 font-mono text-[11px]">${log.tableName}</span>
                                </div>
                                <div class="text-slate-500 mt-1 text-[11px]">${log.newValues}</div>
                            </div>
                            <div class="text-right text-slate-400 text-[11px]">
                                <div>${log.timestamp}</div>
                                <div class="font-mono text-[10px] text-slate-300">${log.ipAddress}</div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>

        <!-- Warehouse High-Level Supervision Overview (Chế độ giám sát kho dành cho Admin) -->
        <div class="bg-slate-900 text-white rounded-2xl p-6 shadow-sm">
            <div class="flex flex-col sm:flex-row items-center justify-between pb-4 border-b border-slate-800 gap-4">
                <div>
                    <h3 class="text-base font-bold flex items-center space-x-2">
                        <i class="fa-solid fa-eye text-blue-400"></i>
                        <span>Giám Sát Vận Hành Kho Hàng & Laptop (Dell & Lenovo)</span>
                    </h3>
                    <p class="text-xs text-slate-400 mt-1">Dữ liệu kiểm soát tồn kho khả dụng và mẫu mã sản phẩm theo thời gian thực.</p>
                </div>
                <div class="flex items-center space-x-3">
                    <a href="${pageContext.request.contextPath}/app/products" class="text-xs px-3 py-1.5 bg-slate-800 hover:bg-slate-700 rounded-lg transition">Xem Laptop</a>
                    <a href="${pageContext.request.contextPath}/app/inventory" class="text-xs px-3 py-1.5 bg-slate-800 hover:bg-slate-700 rounded-lg transition">Xem Serial & Vị trí</a>
                </div>
            </div>
            <div class="grid grid-cols-2 sm:grid-cols-4 gap-4 mt-4 text-center">
                <div class="p-3 bg-slate-800/60 rounded-xl">
                    <div class="text-xs text-slate-400">Tồn kho khả dụng</div>
                    <div class="text-2xl font-bold text-emerald-400 mt-1">${stockStats['IN_STOCK'] != null ? stockStats['IN_STOCK'] : 0} máy</div>
                </div>
                <div class="p-3 bg-slate-800/60 rounded-xl">
                    <div class="text-xs text-slate-400">Đã đặt trước</div>
                    <div class="text-2xl font-bold text-amber-400 mt-1">${stockStats['RESERVED'] != null ? stockStats['RESERVED'] : 0} máy</div>
                </div>
                <div class="p-3 bg-slate-800/60 rounded-xl">
                    <div class="text-xs text-slate-400">Đã xuất bán</div>
                    <div class="text-2xl font-bold text-blue-400 mt-1">${stockStats['EXPORTED'] != null ? stockStats['EXPORTED'] : 0} máy</div>
                </div>
                <div class="p-3 bg-slate-800/60 rounded-xl">
                    <div class="text-xs text-slate-400">Mẫu mã Laptop</div>
                    <div class="text-2xl font-bold text-indigo-400 mt-1">${totalProducts} models</div>
                </div>
            </div>
        </div>
    </main>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
</body>
</html>

