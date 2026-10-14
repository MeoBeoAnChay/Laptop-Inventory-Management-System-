<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<jsp:include page="/WEB-INF/views/common/head.jsp">
    <jsp:param name="title" value="Vai trò & Phân quyền" />
</jsp:include>
<body class="bg-slate-50 text-slate-800 antialiased min-h-screen flex flex-col">

<jsp:include page="/WEB-INF/views/common/navbar.jsp" />

<div class="flex flex-1">
    <jsp:include page="/WEB-INF/views/common/sidebar.jsp" />

    <main class="flex-1 p-8 overflow-y-auto">
        <!-- Breadcrumb & Header -->
        <div class="mb-6 flex flex-col md:flex-row md:items-center md:justify-between gap-4">
            <div>
                <div class="text-xs font-semibold text-blue-600 uppercase tracking-wider">Hệ thống Quản trị / Phân quyền (FE-16)</div>
                <h1 class="text-2xl font-bold text-slate-900 tracking-tight mt-1">Vai Trò & Ma Trận Quyền Hạn (UC-67 $\rightarrow$ UC-74)</h1>
                <p class="text-sm text-slate-500">Chi tiết 5 vai trò hệ thống và phạm vi trách nhiệm phân công cho nhóm SE1976 Group 5.</p>
            </div>
            <a href="${pageContext.request.contextPath}/app/admin/users"
               class="px-4 py-2.5 bg-blue-600 hover:bg-blue-700 text-white rounded-xl text-sm font-medium shadow-sm transition flex items-center space-x-2">
                <i class="fa-solid fa-users-gear"></i>
                <span>Gán Vai Trò Cho Nhân Viên</span>
            </a>
        </div>

        <!-- 5 Roles Cards Grid -->
        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            <c:forEach var="role" items="${roles}">
                <div class="bg-white rounded-2xl border border-slate-200/80 shadow-sm p-6 flex flex-col justify-between hover:border-blue-300 transition">
                    <div>
                        <div class="flex items-center justify-between mb-3">
                            <span class="inline-flex items-center px-3 py-1 rounded-full text-xs font-bold
                                ${role.roleName == 'Admin' ? 'bg-purple-50 text-purple-700 border border-purple-200' :
                                  (role.roleName == 'Manager' ? 'bg-blue-50 text-blue-700 border border-blue-200' :
                                  (role.roleName == 'Warehouse Staff' ? 'bg-amber-50 text-amber-700 border border-amber-200' :
                                  (role.roleName == 'Sales Staff' ? 'bg-emerald-50 text-emerald-700 border border-emerald-200' :
                                  'bg-indigo-50 text-indigo-700 border border-indigo-200')))}">
                                Mã Role #${role.roleId}
                            </span>
                            <span class="text-xs font-semibold text-slate-500">
                                Nhân sự: <strong class="text-slate-900">${userStats['role_'.concat(role.roleName)] != null ? userStats['role_'.concat(role.roleName)] : 0}</strong>
                            </span>
                        </div>

                        <h3 class="text-lg font-bold text-slate-900 flex items-center space-x-2">
                            <c:choose>
                                <c:when test="${role.roleName == 'Admin'}"><i class="fa-solid fa-crown text-purple-600"></i></c:when>
                                <c:when test="${role.roleName == 'Manager'}"><i class="fa-solid fa-briefcase text-blue-600"></i></c:when>
                                <c:when test="${role.roleName == 'Warehouse Staff'}"><i class="fa-solid fa-warehouse text-amber-600"></i></c:when>
                                <c:when test="${role.roleName == 'Sales Staff'}"><i class="fa-solid fa-chart-line text-emerald-600"></i></c:when>
                                <c:otherwise><i class="fa-solid fa-cart-shopping text-indigo-600"></i></c:otherwise>
                            </c:choose>
                            <span>${role.roleName}</span>
                        </h3>
                        <p class="text-xs text-slate-500 mt-1.5 leading-relaxed">${role.description}</p>

                        <!-- Predefined Permission Capabilities from SRS -->
                        <div class="mt-4 pt-4 border-t border-slate-100">
                            <div class="text-[11px] font-bold uppercase tracking-wider text-slate-400 mb-2">Quyền hạn theo đặc tả SRS (UC):</div>
                            <ul class="space-y-1.5 text-xs text-slate-600">
                                <c:forEach var="perm" items="${rolePermissions[role.roleName]}">
                                    <li class="flex items-start space-x-2">
                                        <i class="fa-solid fa-check text-emerald-500 mt-0.5 text-[11px]"></i>
                                        <span>${perm}</span>
                                    </li>
                                </c:forEach>
                            </ul>
                        </div>
                    </div>

                    <div class="mt-6 pt-4 border-t border-slate-100 flex items-center justify-between text-xs">
                        <span class="text-slate-400">Trạng thái: <strong class="text-emerald-600">Hoạt động</strong></span>
                        <a href="${pageContext.request.contextPath}/app/admin/users?roleId=${role.roleId}"
                           class="text-blue-600 hover:text-blue-800 font-semibold">Xem thành viên &rarr;</a>
                    </div>
                </div>
            </c:forEach>
        </div>
    </main>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
</body>
</html>

