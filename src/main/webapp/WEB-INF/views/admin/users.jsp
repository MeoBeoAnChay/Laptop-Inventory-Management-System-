<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">
<jsp:include page="/WEB-INF/views/common/head.jsp">
    <jsp:param name="title" value="Quản lý Tài khoản & Nhân sự" />
</jsp:include>
<body class="bg-slate-50 text-slate-800 antialiased min-h-screen flex flex-col">

<jsp:include page="/WEB-INF/views/common/navbar.jsp" />

<div class="flex flex-1">
    <jsp:include page="/WEB-INF/views/common/sidebar.jsp" />

    <main class="flex-1 p-8 overflow-y-auto">
        <!-- Breadcrumb & Header -->
        <div class="mb-6 flex flex-col md:flex-row md:items-center md:justify-between gap-4">
            <div>
                <div class="text-xs font-semibold text-blue-600 uppercase tracking-wider">Hệ thống Quản trị / Phân hệ Nhân sự (FE-15)</div>
                <h1 class="text-2xl font-bold text-slate-900 tracking-tight mt-1">Quản Lý Tài Khoản Nhân Viên (UC-62 $\rightarrow$ UC-66)</h1>
                <p class="text-sm text-slate-500">Xem danh sách, thêm tài khoản mới, cập nhật thông tin, phân quyền vai trò và đặt lại mật khẩu.</p>
            </div>
            <button onclick="openCreateModal()"
                    class="px-4 py-2.5 bg-blue-600 hover:bg-blue-700 text-white rounded-xl text-sm font-medium shadow-sm transition flex items-center space-x-2">
                <i class="fa-solid fa-user-plus"></i>
                <span>Tạo Tài Khoản Mới (UC-63)</span>
            </button>
        </div>

        <!-- Notification Alerts -->
        <c:if test="${param.success == 'created'}">
            <div class="mb-5 p-4 rounded-xl bg-emerald-50 border border-emerald-200 text-emerald-700 text-sm flex items-center space-x-2">
                <i class="fa-solid fa-circle-check"></i>
                <span>Tạo tài khoản nhân viên mới thành công!</span>
            </div>
        </c:if>
        <c:if test="${param.success == 'updated'}">
            <div class="mb-5 p-4 rounded-xl bg-blue-50 border border-blue-200 text-blue-700 text-sm flex items-center space-x-2">
                <i class="fa-solid fa-circle-check"></i>
                <span>Cập nhật thông tin tài khoản và vai trò thành công!</span>
            </div>
        </c:if>
        <c:if test="${param.success == 'status_changed'}">
            <div class="mb-5 p-4 rounded-xl bg-blue-50 border border-blue-200 text-blue-700 text-sm flex items-center space-x-2">
                <i class="fa-solid fa-circle-check"></i>
                <span>Đã thay đổi trạng thái tài khoản thành công!</span>
            </div>
        </c:if>
        <c:if test="${param.success == 'password_reset'}">
            <div class="mb-5 p-4 rounded-xl bg-emerald-50 border border-emerald-200 text-emerald-700 text-sm flex items-center space-x-2">
                <i class="fa-solid fa-key"></i>
                <span>Đã đặt lại mật khẩu cho tài khoản thành công!</span>
            </div>
        </c:if>
        <c:if test="${param.error == 'username_exists'}">
            <div class="mb-5 p-4 rounded-xl bg-rose-50 border border-rose-200 text-rose-700 text-sm flex items-center space-x-2">
                <i class="fa-solid fa-circle-exclamation"></i>
                <span>Tên đăng nhập (Username) này đã tồn tại trên hệ thống.</span>
            </div>
        </c:if>

        <!-- KPI Mini Stats Row -->
        <div class="grid grid-cols-2 sm:grid-cols-4 gap-4 mb-6">
            <div class="p-4 bg-white rounded-xl border border-slate-200 shadow-sm">
                <div class="text-xs text-slate-400 font-medium">Tổng số tài khoản</div>
                <div class="text-xl font-bold text-slate-900 mt-1">${userStats['total_users'] != null ? userStats['total_users'] : 0}</div>
            </div>
            <div class="p-4 bg-white rounded-xl border border-slate-200 shadow-sm">
                <div class="text-xs text-slate-400 font-medium">Đang hoạt động (Active)</div>
                <div class="text-xl font-bold text-emerald-600 mt-1">${userStats['status_ACTIVE'] != null ? userStats['status_ACTIVE'] : 0}</div>
            </div>
            <div class="p-4 bg-white rounded-xl border border-slate-200 shadow-sm">
                <div class="text-xs text-slate-400 font-medium">Đang bị khóa (Locked)</div>
                <div class="text-xl font-bold text-rose-600 mt-1">${userStats['status_LOCKED'] != null ? userStats['status_LOCKED'] : 0}</div>
            </div>
            <div class="p-4 bg-white rounded-xl border border-slate-200 shadow-sm">
                <div class="text-xs text-slate-400 font-medium">Tạm ngừng (Inactive)</div>
                <div class="text-xl font-bold text-amber-600 mt-1">${userStats['status_INACTIVE'] != null ? userStats['status_INACTIVE'] : 0}</div>
            </div>
        </div>

        <!-- Filter & Search Bar (UC-62) -->
        <div class="bg-white p-5 rounded-2xl border border-slate-200/80 shadow-sm mb-6">
            <form action="${pageContext.request.contextPath}/app/admin/users" method="GET" class="grid grid-cols-1 sm:grid-cols-3 lg:grid-cols-4 gap-4">
                <div class="lg:col-span-2">
                    <label class="block text-xs font-semibold text-slate-500 uppercase mb-1">Tìm kiếm nhân sự</label>
                    <input type="text" name="keyword" value="${keyword}" placeholder="Tìm theo username, họ tên, email, SĐT..."
                           class="w-full px-3.5 py-2 bg-slate-50 border border-slate-200 rounded-xl text-sm focus:outline-none focus:ring-2 focus:ring-blue-500" />
                </div>
                <div>
                    <label class="block text-xs font-semibold text-slate-500 uppercase mb-1">Vai trò (Role)</label>
                    <select name="roleId" class="w-full px-3.5 py-2 bg-slate-50 border border-slate-200 rounded-xl text-sm focus:outline-none focus:ring-2 focus:ring-blue-500">
                        <option value="">Tất cả vai trò</option>
                        <c:forEach var="r" items="${allRoles}">
                            <option value="${r.roleId}" ${selectedRoleId == r.roleId ? 'selected' : ''}>${r.roleName}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="flex items-end space-x-2">
                    <button type="submit" class="w-full py-2 px-4 bg-slate-800 hover:bg-slate-900 text-white rounded-xl text-sm font-medium transition flex items-center justify-center space-x-2">
                        <i class="fa-solid fa-magnifying-glass"></i>
                        <span>Lọc danh sách</span>
                    </button>
                    <a href="${pageContext.request.contextPath}/app/admin/users" class="py-2 px-3 bg-slate-100 hover:bg-slate-200 text-slate-700 rounded-xl text-sm font-medium transition" title="Xóa bộ lọc">
                        <i class="fa-solid fa-rotate-right"></i>
                    </a>
                </div>
            </form>
        </div>

        <!-- Users Table (UC-62) -->
        <div class="bg-white rounded-2xl border border-slate-200/80 shadow-sm overflow-hidden">
            <div class="overflow-x-auto">
                <table class="w-full text-left border-collapse text-sm">
                    <thead>
                        <tr class="bg-slate-50 border-b border-slate-100 text-xs font-semibold text-slate-500 uppercase tracking-wider">
                            <th class="py-3.5 px-6">ID</th>
                            <th class="py-3.5 px-6">Nhân viên / Username</th>
                            <th class="py-3.5 px-6">Email & Số điện thoại</th>
                            <th class="py-3.5 px-6">Vai trò (RBAC Roles)</th>
                            <th class="py-3.5 px-6">Trạng thái</th>
                            <th class="py-3.5 px-6">Ngày tạo</th>
                            <th class="py-3.5 px-6 text-right">Thao tác</th>
                        </tr>
                    </thead>
                    <tbody class="divide-y divide-slate-100">
                        <c:choose>
                            <c:when test="${empty users}">
                                <tr>
                                    <td colspan="7" class="p-8 text-center text-slate-400">Không tìm thấy tài khoản nhân viên nào phù hợp.</td>
                                </tr>
                            </c:when>
                            <c:otherwise>
                                <c:forEach var="u" items="${users}">
                                    <tr class="hover:bg-slate-50/50">
                                        <td class="py-3.5 px-6 font-mono text-slate-400">#${u.userId}</td>
                                        <td class="py-3.5 px-6">
                                            <div class="flex items-center space-x-3">
                                                <div class="w-9 h-9 rounded-full bg-blue-100 text-blue-700 font-bold flex items-center justify-center text-xs ring-2 ring-blue-50">
                                                    ${u.username.substring(0, 1).toUpperCase()}
                                                </div>
                                                <div>
                                                    <div class="font-bold text-slate-900">${u.fullName}</div>
                                                    <div class="text-xs font-mono text-slate-400">@${u.username}</div>
                                                </div>
                                            </div>
                                        </td>
                                        <td class="py-3.5 px-6">
                                            <div class="text-slate-700">${u.email}</div>
                                            <div class="text-xs text-slate-400 font-mono">${u.phone != null && !u.phone.isEmpty() ? u.phone : 'Chưa cập nhật'}</div>
                                        </td>
                                        <td class="py-3.5 px-6">
                                            <div class="flex flex-wrap gap-1">
                                                <c:forEach var="r" items="${u.roles}">
                                                    <span class="inline-block px-2 py-0.5 text-xs font-semibold rounded-md border
                                                        ${r.roleName == 'Admin' ? 'bg-purple-50 text-purple-700 border-purple-200' :
                                                          (r.roleName == 'Manager' ? 'bg-blue-50 text-blue-700 border-blue-200' :
                                                          (r.roleName == 'Warehouse Staff' ? 'bg-amber-50 text-amber-700 border-amber-200' :
                                                          (r.roleName == 'Sales Staff' ? 'bg-emerald-50 text-emerald-700 border-emerald-200' :
                                                          'bg-indigo-50 text-indigo-700 border-indigo-200')))}">
                                                        ${r.roleName}
                                                    </span>
                                                </c:forEach>
                                            </div>
                                        </td>
                                        <td class="py-3.5 px-6">
                                            <c:choose>
                                                <c:when test="${u.status == 'ACTIVE'}">
                                                    <span class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-semibold bg-emerald-50 text-emerald-700 border border-emerald-200">
                                                        <span class="w-1.5 h-1.5 bg-emerald-500 rounded-full mr-1.5"></span> ACTIVE
                                                    </span>
                                                </c:when>
                                                <c:when test="${u.status == 'LOCKED'}">
                                                    <span class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-semibold bg-rose-50 text-rose-700 border border-rose-200">
                                                        <span class="w-1.5 h-1.5 bg-rose-500 rounded-full mr-1.5"></span> LOCKED
                                                    </span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-semibold bg-slate-100 text-slate-700 border border-slate-200">
                                                        ${u.status}
                                                    </span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td class="py-3.5 px-6 text-xs text-slate-400 font-mono">${u.createdAt}</td>
                                        <td class="py-3.5 px-6 text-right">
                                            <div class="flex items-center justify-end space-x-1.5">
                                                <!-- Edit Button (UC-64) -->
                                                <button onclick="openEditModal(${u.userId}, '${u.username}', '${u.fullName}', '${u.email}', '${u.phone}', '${u.status}', [${u.roles.stream().map(r -> r.roleId).toList().toString().replace('[','').replace(']','')}])"
                                                        class="p-1.5 text-blue-600 hover:text-blue-800 hover:bg-blue-50 rounded-lg transition" title="Sửa thông tin & vai trò (UC-64, UC-71)">
                                                    <i class="fa-solid fa-pen-to-square"></i>
                                                </button>

                                                <!-- Reset Password Button (UC-66) -->
                                                <button onclick="openResetModal(${u.userId}, '${u.username}')"
                                                        class="p-1.5 text-amber-600 hover:text-amber-800 hover:bg-amber-50 rounded-lg transition" title="Đặt lại mật khẩu (UC-66)">
                                                    <i class="fa-solid fa-key"></i>
                                                </button>

                                                <!-- Lock / Unlock Toggle Button (UC-65) -->
                                                <form action="${pageContext.request.contextPath}/app/admin/users" method="POST" class="inline" onsubmit="return confirm('Bạn có chắc chắn muốn thay đổi trạng thái tài khoản này?');">
                                                    <input type="hidden" name="action" value="status" />
                                                    <input type="hidden" name="userId" value="${u.userId}" />
                                                    <c:choose>
                                                        <c:when test="${u.status == 'ACTIVE'}">
                                                            <input type="hidden" name="newStatus" value="LOCKED" />
                                                            <button type="submit" class="p-1.5 text-rose-500 hover:text-rose-700 hover:bg-rose-50 rounded-lg transition" title="Khóa tài khoản (UC-65)">
                                                                <i class="fa-solid fa-lock"></i>
                                                            </button>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <input type="hidden" name="newStatus" value="ACTIVE" />
                                                            <button type="submit" class="p-1.5 text-emerald-600 hover:text-emerald-800 hover:bg-emerald-50 rounded-lg transition" title="Mở khóa tài khoản">
                                                                <i class="fa-solid fa-lock-open"></i>
                                                            </button>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </form>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                    </tbody>
                </table>
            </div>

            <!-- Pagination -->
            <c:if test="${totalPages > 1}">
                <div class="px-6 py-4 border-t border-slate-100 flex items-center justify-between text-xs text-slate-500">
                    <span>Hiển thị ${users.size()} / ${totalUsers} tài khoản</span>
                    <div class="flex items-center space-x-1">
                        <c:forEach begin="1" end="${totalPages}" var="pageIndex">
                            <a href="${pageContext.request.contextPath}/app/admin/users?page=${pageIndex}&keyword=${keyword}&roleId=${selectedRoleId}&status=${selectedStatus}"
                               class="px-3 py-1.5 rounded-lg border font-medium ${currentPage == pageIndex ? 'bg-blue-600 text-white border-blue-600' : 'bg-white hover:bg-slate-50 text-slate-700 border-slate-200'}">
                                ${pageIndex}
                            </a>
                        </c:forEach>
                    </div>
                </div>
            </c:if>
        </div>

        <!-- ================= MODAL 1: CREATE USER (UC-63) ================= -->
        <div id="createUserModal" class="hidden fixed inset-0 z-50 flex items-center justify-center bg-slate-900/40 backdrop-blur-sm p-4">
            <div class="bg-white rounded-2xl max-w-lg w-full p-6 shadow-2xl border border-slate-200 max-h-[90vh] overflow-y-auto">
                <div class="flex items-center justify-between pb-4 border-b border-slate-100">
                    <h3 class="text-base font-bold text-slate-900 flex items-center space-x-2">
                        <i class="fa-solid fa-user-plus text-blue-600"></i>
                        <span>Tạo Tài Khoản Nhân Viên Mới (UC-63)</span>
                    </h3>
                    <button onclick="closeModal('createUserModal')" class="text-slate-400 hover:text-slate-600">
                        <i class="fa-solid fa-xmark text-lg"></i>
                    </button>
                </div>
                <form action="${pageContext.request.contextPath}/app/admin/users" method="POST" class="mt-4 space-y-4">
                    <input type="hidden" name="action" value="create" />
                    <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
                        <div>
                            <label class="block text-xs font-semibold text-slate-600 uppercase mb-1">Tên đăng nhập (Username) *</label>
                            <input type="text" name="username" required placeholder="thaiph" class="w-full px-3 py-2 border rounded-xl text-sm" />
                        </div>
                        <div>
                            <label class="block text-xs font-semibold text-slate-600 uppercase mb-1">Mật khẩu khởi tạo *</label>
                            <input type="password" name="password" required value="admin123" placeholder="••••••••" class="w-full px-3 py-2 border rounded-xl text-sm" />
                        </div>
                    </div>

                    <div>
                        <label class="block text-xs font-semibold text-slate-600 uppercase mb-1">Họ và tên nhân viên *</label>
                        <input type="text" name="fullName" required placeholder="Phạm Huy Thái" class="w-full px-3 py-2 border rounded-xl text-sm" />
                    </div>

                    <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
                        <div>
                            <label class="block text-xs font-semibold text-slate-600 uppercase mb-1">Email *</label>
                            <input type="email" name="email" required placeholder="thaiph@lims.com" class="w-full px-3 py-2 border rounded-xl text-sm" />
                        </div>
                        <div>
                            <label class="block text-xs font-semibold text-slate-600 uppercase mb-1">Số điện thoại</label>
                            <input type="text" name="phone" placeholder="0903456789" class="w-full px-3 py-2 border rounded-xl text-sm" />
                        </div>
                    </div>

                    <div>
                        <label class="block text-xs font-semibold text-slate-600 uppercase mb-2">Phân quyền vai trò (RBAC Roles - UC-71) *</label>
                        <div class="grid grid-cols-2 gap-2 p-3 bg-slate-50 border border-slate-200 rounded-xl">
                            <c:forEach var="r" items="${allRoles}">
                                <label class="flex items-center space-x-2 text-xs font-medium text-slate-700 cursor-pointer">
                                    <input type="checkbox" name="roleIds" value="${r.roleId}" class="rounded text-blue-600 focus:ring-blue-500" />
                                    <span>${r.roleName}</span>
                                </label>
                            </c:forEach>
                        </div>
                    </div>

                    <div>
                        <label class="block text-xs font-semibold text-slate-600 uppercase mb-1">Trạng thái</label>
                        <select name="status" class="w-full px-3 py-2 border rounded-xl text-sm">
                            <option value="ACTIVE" selected>ACTIVE (Kích hoạt ngay)</option>
                            <option value="INACTIVE">INACTIVE (Chưa kích hoạt)</option>
                            <option value="LOCKED">LOCKED (Khóa tạm thời)</option>
                        </select>
                    </div>

                    <div class="pt-4 border-t border-slate-100 flex items-center justify-end space-x-3">
                        <button type="button" onclick="closeModal('createUserModal')"
                                class="px-4 py-2 border border-slate-200 text-slate-700 text-sm font-medium rounded-xl hover:bg-slate-50">Hủy</button>
                        <button type="submit"
                                class="px-5 py-2 bg-blue-600 hover:bg-blue-700 text-white text-sm font-medium rounded-xl shadow-sm">Lưu Tài Khoản</button>
                    </div>
                </form>
            </div>
        </div>

        <!-- ================= MODAL 2: EDIT USER (UC-64, UC-71) ================= -->
        <div id="editUserModal" class="hidden fixed inset-0 z-50 flex items-center justify-center bg-slate-900/40 backdrop-blur-sm p-4">
            <div class="bg-white rounded-2xl max-w-lg w-full p-6 shadow-2xl border border-slate-200 max-h-[90vh] overflow-y-auto">
                <div class="flex items-center justify-between pb-4 border-b border-slate-100">
                    <h3 class="text-base font-bold text-slate-900 flex items-center space-x-2">
                        <i class="fa-solid fa-user-pen text-blue-600"></i>
                        <span>Cập Nhật Thông Tin Tài Khoản: <span id="editModalUsername" class="font-mono text-blue-600"></span></span>
                    </h3>
                    <button onclick="closeModal('editUserModal')" class="text-slate-400 hover:text-slate-600">
                        <i class="fa-solid fa-xmark text-lg"></i>
                    </button>
                </div>
                <form action="${pageContext.request.contextPath}/app/admin/users" method="POST" class="mt-4 space-y-4">
                    <input type="hidden" name="action" value="update" />
                    <input type="hidden" id="editUserId" name="userId" />

                    <div>
                        <label class="block text-xs font-semibold text-slate-600 uppercase mb-1">Họ và tên nhân viên *</label>
                        <input type="text" id="editFullName" name="fullName" required class="w-full px-3 py-2 border rounded-xl text-sm" />
                    </div>

                    <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
                        <div>
                            <label class="block text-xs font-semibold text-slate-600 uppercase mb-1">Email *</label>
                            <input type="email" id="editEmail" name="email" required class="w-full px-3 py-2 border rounded-xl text-sm" />
                        </div>
                        <div>
                            <label class="block text-xs font-semibold text-slate-600 uppercase mb-1">Số điện thoại</label>
                            <input type="text" id="editPhone" name="phone" class="w-full px-3 py-2 border rounded-xl text-sm" />
                        </div>
                    </div>

                    <div>
                        <label class="block text-xs font-semibold text-slate-600 uppercase mb-2">Vai trò hệ thống (Gán lại quyền - UC-71) *</label>
                        <div class="grid grid-cols-2 gap-2 p-3 bg-slate-50 border border-slate-200 rounded-xl" id="editRolesContainer">
                            <c:forEach var="r" items="${allRoles}">
                                <label class="flex items-center space-x-2 text-xs font-medium text-slate-700 cursor-pointer">
                                    <input type="checkbox" name="roleIds" value="${r.roleId}" id="roleCheckbox_${r.roleId}" class="rounded text-blue-600 focus:ring-blue-500" />
                                    <span>${r.roleName}</span>
                                </label>
                            </c:forEach>
                        </div>
                    </div>

                    <div>
                        <label class="block text-xs font-semibold text-slate-600 uppercase mb-1">Trạng thái tài khoản</label>
                        <select id="editStatus" name="status" class="w-full px-3 py-2 border rounded-xl text-sm">
                            <option value="ACTIVE">ACTIVE (Hoạt động)</option>
                            <option value="INACTIVE">INACTIVE (Tạm ngưng)</option>
                            <option value="LOCKED">LOCKED (Khóa tài khoản)</option>
                        </select>
                    </div>

                    <div class="pt-4 border-t border-slate-100 flex items-center justify-end space-x-3">
                        <button type="button" onclick="closeModal('editUserModal')"
                                class="px-4 py-2 border border-slate-200 text-slate-700 text-sm font-medium rounded-xl hover:bg-slate-50">Hủy</button>
                        <button type="submit"
                                class="px-5 py-2 bg-blue-600 hover:bg-blue-700 text-white text-sm font-medium rounded-xl shadow-sm">Lưu Thay Đổi</button>
                    </div>
                </form>
            </div>
        </div>

        <!-- ================= MODAL 3: RESET PASSWORD (UC-66) ================= -->
        <div id="resetPasswordModal" class="hidden fixed inset-0 z-50 flex items-center justify-center bg-slate-900/40 backdrop-blur-sm p-4">
            <div class="bg-white rounded-2xl max-w-sm w-full p-6 shadow-2xl border border-slate-200">
                <div class="flex items-center justify-between pb-4 border-b border-slate-100">
                    <h3 class="text-base font-bold text-slate-900 flex items-center space-x-2">
                        <i class="fa-solid fa-key text-amber-500"></i>
                        <span>Đặt Lại Mật Khẩu (UC-66)</span>
                    </h3>
                    <button onclick="closeModal('resetPasswordModal')" class="text-slate-400 hover:text-slate-600">
                        <i class="fa-solid fa-xmark text-lg"></i>
                    </button>
                </div>
                <form action="${pageContext.request.contextPath}/app/admin/users" method="POST" class="mt-4 space-y-4">
                    <input type="hidden" name="action" value="reset_password" />
                    <input type="hidden" id="resetUserId" name="userId" />

                    <div class="text-xs text-slate-500">
                        Đang đặt lại mật khẩu cho tài khoản: <strong id="resetUsername" class="font-mono text-slate-800"></strong>
                    </div>

                    <div>
                        <label class="block text-xs font-semibold text-slate-600 uppercase mb-1">Mật khẩu mới *</label>
                        <input type="password" id="resetNewPassword" name="newPassword" required value="admin123" class="w-full px-3 py-2 border rounded-xl text-sm" />
                    </div>

                    <div class="pt-2">
                        <button type="button" onclick="document.getElementById('resetNewPassword').value = 'lims@' + Math.floor(1000 + Math.random() * 9000);"
                                class="text-xs text-blue-600 hover:underline">
                            <i class="fa-solid fa-shuffle mr-1"></i> Sinh mật khẩu ngẫu nhiên
                        </button>
                    </div>

                    <div class="pt-4 border-t border-slate-100 flex items-center justify-end space-x-3">
                        <button type="button" onclick="closeModal('resetPasswordModal')"
                                class="px-4 py-2 border border-slate-200 text-slate-700 text-sm font-medium rounded-xl hover:bg-slate-50">Hủy</button>
                        <button type="submit"
                                class="px-5 py-2 bg-amber-600 hover:bg-amber-700 text-white text-sm font-medium rounded-xl shadow-sm">Xác Nhận Reset</button>
                    </div>
                </form>
            </div>
        </div>
    </main>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />

<script>
    function closeModal(id) {
        document.getElementById(id).classList.add('hidden');
    }

    function openCreateModal() {
        document.getElementById('createUserModal').classList.remove('hidden');
    }

    function openEditModal(userId, username, fullName, email, phone, status, roleIds) {
        document.getElementById('editUserId').value = userId;
        document.getElementById('editModalUsername').innerText = '@' + username;
        document.getElementById('editFullName').value = fullName;
        document.getElementById('editEmail').value = email;
        document.getElementById('editPhone').value = phone || '';
        document.getElementById('editStatus').value = status;

        // Uncheck all role checkboxes first
        const checkboxes = document.querySelectorAll('#editRolesContainer input[type="checkbox"]');
        checkboxes.forEach(cb => cb.checked = false);

        // Check assigned roles
        if (roleIds && Array.isArray(roleIds)) {
            roleIds.forEach(rId => {
                const cb = document.getElementById('roleCheckbox_' + rId);
                if (cb) cb.checked = true;
            });
        }

        document.getElementById('editUserModal').classList.remove('hidden');
    }

    function openResetModal(userId, username) {
        document.getElementById('resetUserId').value = userId;
        document.getElementById('resetUsername').innerText = '@' + username;
        document.getElementById('resetPasswordModal').classList.remove('hidden');
    }
</script>
</body>
</html>

