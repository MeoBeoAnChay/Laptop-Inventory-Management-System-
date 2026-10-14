<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">
<jsp:include page="/WEB-INF/views/common/head.jsp">
    <jsp:param name="title" value="Nhật ký Kiểm toán" />
</jsp:include>
<body class="bg-slate-50 text-slate-800 antialiased min-h-screen flex flex-col">

<jsp:include page="/WEB-INF/views/common/navbar.jsp" />

<div class="flex flex-1">
    <jsp:include page="/WEB-INF/views/common/sidebar.jsp" />

    <main class="flex-1 p-8 overflow-y-auto">
        <!-- Breadcrumb & Header -->
        <div class="mb-6 flex flex-col md:flex-row md:items-center md:justify-between gap-4">
            <div>
                <div class="text-xs font-semibold text-blue-600 uppercase tracking-wider">Hệ thống Quản trị / Giám sát (FE-18)</div>
                <h1 class="text-2xl font-bold text-slate-900 tracking-tight mt-1">Nhật Ký Kiểm Toán Hệ Thống (UC-78, UC-79)</h1>
                <p class="text-sm text-slate-500">Giám sát mọi thao tác thêm, sửa, xóa, duyệt đơn và đăng nhập của người dùng.</p>
            </div>
            <div class="text-xs text-slate-400 font-mono bg-white px-3 py-2 rounded-xl border border-slate-200">
                <i class="fa-solid fa-database text-emerald-500 mr-1.5"></i> Bảng lưu trữ: AUDIT_LOGS
            </div>
        </div>

        <!-- Filter Bar -->
        <div class="bg-white p-5 rounded-2xl border border-slate-200/80 shadow-sm mb-6">
            <form action="${pageContext.request.contextPath}/app/admin/audit" method="GET" class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
                <div>
                    <label class="block text-xs font-semibold text-slate-500 uppercase mb-1">Từ khóa (Username, IP, Record)</label>
                    <input type="text" name="keyword" value="${keyword}" placeholder="admin, 127.0.0.1..."
                           class="w-full px-3.5 py-2 bg-slate-50 border border-slate-200 rounded-xl text-sm focus:outline-none focus:ring-2 focus:ring-blue-500" />
                </div>
                <div>
                    <label class="block text-xs font-semibold text-slate-500 uppercase mb-1">Hành động (Action)</label>
                    <select name="action" class="w-full px-3.5 py-2 bg-slate-50 border border-slate-200 rounded-xl text-sm focus:outline-none focus:ring-2 focus:ring-blue-500">
                        <option value="ALL">Tất cả hành động</option>
                        <c:forEach var="act" items="${actions}">
                            <option value="${act}" ${selectedAction == act ? 'selected' : ''}>${act}</option>
                        </c:forEach>
                    </select>
                </div>
                <div>
                    <label class="block text-xs font-semibold text-slate-500 uppercase mb-1">Bảng dữ liệu (Table)</label>
                    <select name="tableName" class="w-full px-3.5 py-2 bg-slate-50 border border-slate-200 rounded-xl text-sm focus:outline-none focus:ring-2 focus:ring-blue-500">
                        <option value="ALL">Tất cả bảng</option>
                        <option value="USERS" ${selectedTable == 'USERS' ? 'selected' : ''}>USERS (Tài khoản)</option>
                        <option value="PRODUCTS" ${selectedTable == 'PRODUCTS' ? 'selected' : ''}>PRODUCTS (Sản phẩm)</option>
                        <option value="INVENTORY" ${selectedTable == 'INVENTORY' ? 'selected' : ''}>INVENTORY (Tồn kho)</option>
                        <option value="IMPORT_REQUESTS" ${selectedTable == 'IMPORT_REQUESTS' ? 'selected' : ''}>IMPORT_REQUESTS (Nhập hàng)</option>
                        <option value="EXPORT_REQUESTS" ${selectedTable == 'EXPORT_REQUESTS' ? 'selected' : ''}>EXPORT_REQUESTS (Xuất hàng)</option>
                        <option value="SYSTEM_CONFIG" ${selectedTable == 'SYSTEM_CONFIG' ? 'selected' : ''}>SYSTEM_CONFIG (Cấu hình)</option>
                    </select>
                </div>
                <div class="flex items-end space-x-2">
                    <button type="submit" class="w-full py-2 px-4 bg-slate-800 hover:bg-slate-900 text-white rounded-xl text-sm font-medium transition flex items-center justify-center space-x-2">
                        <i class="fa-solid fa-filter"></i>
                        <span>Lọc nhật ký</span>
                    </button>
                    <a href="${pageContext.request.contextPath}/app/admin/audit" class="py-2 px-3 bg-slate-100 hover:bg-slate-200 text-slate-700 rounded-xl text-sm font-medium transition" title="Xóa bộ lọc">
                        <i class="fa-solid fa-rotate-right"></i>
                    </a>
                </div>
            </form>
        </div>

        <!-- Audit Logs Table -->
        <div class="bg-white rounded-2xl border border-slate-200/80 shadow-sm overflow-hidden">
            <div class="overflow-x-auto">
                <table class="w-full text-left border-collapse text-sm">
                    <thead>
                        <tr class="bg-slate-50 border-b border-slate-100 text-xs font-semibold text-slate-500 uppercase tracking-wider">
                            <th class="py-3.5 px-6">Log ID</th>
                            <th class="py-3.5 px-6">Thời gian</th>
                            <th class="py-3.5 px-6">Người thực hiện</th>
                            <th class="py-3.5 px-6">Hành động</th>
                            <th class="py-3.5 px-6">Bảng dữ liệu</th>
                            <th class="py-3.5 px-6">Record ID</th>
                            <th class="py-3.5 px-6">Chi tiết thay đổi</th>
                            <th class="py-3.5 px-6">Địa chỉ IP</th>
                        </tr>
                    </thead>
                    <tbody class="divide-y divide-slate-100">
                        <c:choose>
                            <c:when test="${empty logs}">
                                <tr>
                                    <td colspan="8" class="p-8 text-center text-slate-400">Không có nhật ký thao tác nào phù hợp.</td>
                                </tr>
                            </c:when>
                            <c:otherwise>
                                <c:forEach var="l" items="${logs}">
                                    <tr class="hover:bg-slate-50/50">
                                        <td class="py-3.5 px-6 font-mono text-slate-400">#${l.logId}</td>
                                        <td class="py-3.5 px-6 text-xs text-slate-500">${l.timestamp}</td>
                                        <td class="py-3.5 px-6">
                                            <span class="font-bold text-slate-900">@${l.username}</span>
                                            <c:if test="${l.userId != null}">
                                                <span class="text-[10px] text-slate-400 font-mono">(ID: ${l.userId})</span>
                                            </c:if>
                                        </td>
                                        <td class="py-3.5 px-6">
                                            <span class="inline-flex items-center px-2 py-0.5 rounded text-xs font-bold
                                                ${l.action == 'CREATE' ? 'bg-emerald-50 text-emerald-700 border border-emerald-200' :
                                                  (l.action == 'UPDATE' || l.action == 'UPDATE_STATUS' ? 'bg-blue-50 text-blue-700 border border-blue-200' :
                                                  (l.action == 'DELETE' ? 'bg-rose-50 text-rose-700 border border-rose-200' :
                                                  (l.action == 'LOGIN' ? 'bg-purple-50 text-purple-700 border border-purple-200' :
                                                  'bg-slate-100 text-slate-700 border border-slate-200')))}">
                                                ${l.action}
                                            </span>
                                        </td>
                                        <td class="py-3.5 px-6 font-mono text-xs text-slate-700">${l.tableName}</td>
                                        <td class="py-3.5 px-6 font-mono text-xs text-blue-600">${l.recordId != null ? l.recordId : '-'}</td>
                                        <td class="py-3.5 px-6 text-xs text-slate-600 max-w-xs truncate" title="${l.newValues}">
                                            ${l.newValues}
                                        </td>
                                        <td class="py-3.5 px-6 font-mono text-xs text-slate-400">${l.ipAddress}</td>
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
                    <span>Tổng số: ${totalLogs} bản ghi nhật ký</span>
                    <div class="flex items-center space-x-1">
                        <c:forEach begin="1" end="${totalPages}" var="pageIndex">
                            <a href="${pageContext.request.contextPath}/app/admin/audit?page=${pageIndex}&keyword=${keyword}&action=${selectedAction}&tableName=${selectedTable}"
                               class="px-3 py-1.5 rounded-lg border font-medium ${currentPage == pageIndex ? 'bg-blue-600 text-white border-blue-600' : 'bg-white hover:bg-slate-50 text-slate-700 border-slate-200'}">
                                ${pageIndex}
                            </a>
                        </c:forEach>
                    </div>
                </div>
            </c:if>
        </div>
    </main>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
</body>
</html>

