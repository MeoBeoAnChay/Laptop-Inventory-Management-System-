<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<header class="bg-white border-b border-slate-200 sticky top-0 z-30">
    <div class="px-6 py-3 flex items-center justify-between">
        <div class="flex items-center space-x-3">
            <button id="sidebarToggle" class="text-slate-500 hover:text-slate-700 md:hidden p-2 rounded-lg hover:bg-slate-100">
                <i class="fa-solid fa-bars text-lg"></i>
            </button>
            <div class="flex items-center space-x-2 text-slate-800 font-semibold text-lg">
                <i class="fa-solid fa-laptop-code text-blue-600 text-xl"></i>
                <span class="tracking-tight">LIMS <span class="text-xs text-blue-600 bg-blue-50 px-2 py-0.5 rounded-full border border-blue-200 font-medium ml-1">v1.0</span></span>
            </div>
        </div>

        <div class="flex items-center space-x-4">
            <!-- User Status & Role Badge -->
            <a href="${pageContext.request.contextPath}/app/profile" class="text-right hidden sm:block group">
                <div class="text-sm font-semibold text-slate-800 group-hover:text-blue-600 transition">${sessionScope.currentUser.fullName}</div>
                <div class="text-xs text-slate-500 flex items-center justify-end space-x-1">
                    <span class="inline-block w-2 h-2 rounded-full bg-emerald-500"></span>
                    <span>
                        <c:forEach var="role" items="${sessionScope.currentUser.roles}" varStatus="status">
                            ${role.roleName}${!status.last ? ', ' : ''}
                        </c:forEach>
                    </span>
                </div>
            </a>

            <!-- Avatar linking to profile -->
            <a href="${pageContext.request.contextPath}/app/profile"
               class="w-9 h-9 rounded-full bg-blue-600 hover:bg-blue-700 text-white font-bold flex items-center justify-center text-sm shadow-sm ring-2 ring-blue-100 transition"
               title="Xem hồ sơ cá nhân">
                ${sessionScope.currentUser.username.substring(0, 1).toUpperCase()}
            </a>

            <!-- Logout Link -->
            <a href="${pageContext.request.contextPath}/logout"
               class="text-slate-500 hover:text-rose-600 p-2 rounded-lg hover:bg-rose-50 transition-colors"
               title="Đăng xuất">
                <i class="fa-solid fa-arrow-right-from-bracket text-lg"></i>
            </a>
        </div>
    </div>
</header>
