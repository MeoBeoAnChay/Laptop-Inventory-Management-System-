<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<jsp:include page="/WEB-INF/views/common/head.jsp">
    <jsp:param name="title" value="Bảng điều khiển" />
</jsp:include>
<body class="bg-slate-50 text-slate-800 antialiased min-h-screen flex flex-col">

<jsp:include page="/WEB-INF/views/common/navbar.jsp" />

<div class="flex flex-1">
    <jsp:include page="/WEB-INF/views/common/sidebar.jsp" />

    <main class="flex-1 p-8 overflow-y-auto">
        <!-- Welcome Banner -->
        <div class="mb-8 flex flex-col md:flex-row md:items-center md:justify-between gap-4">
            <div>
                <h1 class="text-2xl font-bold text-slate-900 tracking-tight">Xin chào, ${sessionScope.currentUser.fullName}! 👋</h1>
                <p class="text-sm text-slate-500 mt-1">Chào mừng bạn quay lại hệ thống quản lý tồn kho và giao dịch kho laptop LIMS.</p>
            </div>
            <div class="flex items-center space-x-3">
                <a href="${pageContext.request.contextPath}/app/products"
                   class="px-4 py-2.5 bg-blue-600 hover:bg-blue-700 text-white rounded-xl text-sm font-medium shadow-sm transition flex items-center space-x-2">
                    <i class="fa-solid fa-plus"></i>
                    <span>Thêm Laptop Mới</span>
                </a>
            </div>
        </div>

        <!-- 4 Key Metric Cards -->
        <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
            <!-- In Stock -->
            <div class="bg-white p-6 rounded-2xl border border-slate-200/80 shadow-sm flex items-center justify-between">
                <div>
                    <div class="text-xs font-semibold uppercase tracking-wider text-slate-400">Tồn kho khả dụng</div>
                    <div class="text-3xl font-bold text-slate-900 mt-2">${stockStats['IN_STOCK'] != null ? stockStats['IN_STOCK'] : 0}</div>
                    <div class="text-xs text-emerald-600 font-medium mt-1"><i class="fa-solid fa-circle-check"></i> Sẵn sàng xuất kho</div>
                </div>
                <div class="w-12 h-12 rounded-xl bg-emerald-50 text-emerald-600 flex items-center justify-center text-xl">
                    <i class="fa-solid fa-boxes-stacked"></i>
                </div>
            </div>

            <!-- Reserved -->
            <div class="bg-white p-6 rounded-2xl border border-slate-200/80 shadow-sm flex items-center justify-between">
                <div>
                    <div class="text-xs font-semibold uppercase tracking-wider text-slate-400">Đã đặt trước</div>
                    <div class="text-3xl font-bold text-slate-900 mt-2">${stockStats['RESERVED'] != null ? stockStats['RESERVED'] : 0}</div>
                    <div class="text-xs text-amber-600 font-medium mt-1"><i class="fa-solid fa-clock"></i> Chờ xuất kho</div>
                </div>
                <div class="w-12 h-12 rounded-xl bg-amber-50 text-amber-600 flex items-center justify-center text-xl">
                    <i class="fa-solid fa-dolly"></i>
                </div>
            </div>

            <!-- Exported -->
            <div class="bg-white p-6 rounded-2xl border border-slate-200/80 shadow-sm flex items-center justify-between">
                <div>
                    <div class="text-xs font-semibold uppercase tracking-wider text-slate-400">Đã xuất bán</div>
                    <div class="text-3xl font-bold text-slate-900 mt-2">${stockStats['EXPORTED'] != null ? stockStats['EXPORTED'] : 0}</div>
                    <div class="text-xs text-blue-600 font-medium mt-1"><i class="fa-solid fa-truck-fast"></i> Giao khách hàng</div>
                </div>
                <div class="w-12 h-12 rounded-xl bg-blue-50 text-blue-600 flex items-center justify-center text-xl">
                    <i class="fa-solid fa-truck-ramp-box"></i>
                </div>
            </div>

            <!-- Total Models -->
            <div class="bg-white p-6 rounded-2xl border border-slate-200/80 shadow-sm flex items-center justify-between">
                <div>
                    <div class="text-xs font-semibold uppercase tracking-wider text-slate-400">Mẫu Laptop Đang Bán</div>
                    <div class="text-3xl font-bold text-slate-900 mt-2">${totalProducts}</div>
                    <div class="text-xs text-indigo-600 font-medium mt-1"><i class="fa-solid fa-tag"></i> Danh mục sản phẩm</div>
                </div>
                <div class="w-12 h-12 rounded-xl bg-indigo-50 text-indigo-600 flex items-center justify-center text-xl">
                    <i class="fa-solid fa-laptop"></i>
                </div>
            </div>
        </div>

        <!-- 2 Column Section: Recent Transactions & Audit Log -->
        <div class="grid grid-cols-1 lg:grid-cols-2 gap-8 mb-8">
            <!-- Recent Import Requests -->
            <div class="bg-white rounded-2xl border border-slate-200/80 shadow-sm overflow-hidden">
                <div class="px-6 py-4 border-b border-slate-100 flex items-center justify-between">
                    <h2 class="text-base font-bold text-slate-900 flex items-center space-x-2">
                        <i class="fa-solid fa-arrow-down-long text-blue-600"></i>
                        <span>Yêu cầu Nhập kho Gần đây</span>
                    </h2>
                    <a href="${pageContext.request.contextPath}/app/import" class="text-xs font-medium text-blue-600 hover:underline">Xem tất cả &rarr;</a>
                </div>
                <div class="divide-y divide-slate-100">
                    <c:choose>
                        <c:when test="${empty recentImports}">
                            <div class="p-8 text-center text-sm text-slate-400">Chưa có yêu cầu nhập kho nào.</div>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="item" items="${recentImports}">
                                <div class="p-4 px-6 flex items-center justify-between hover:bg-slate-50 transition">
                                    <div>
                                        <div class="text-sm font-semibold text-slate-800">Mã đơn #${item.importRequestId} - ${item.supplierName}</div>
                                        <div class="text-xs text-slate-400 mt-0.5">Tạo bởi: ${item.creatorName} • ${item.requestDate}</div>
                                    </div>
                                    <div>
                                        <span class="inline-flex items-center px-2.5 py-1 rounded-full text-xs font-medium ${item.status == 'APPROVED' ? 'bg-emerald-50 text-emerald-700 border border-emerald-200' : (item.status == 'PENDING' ? 'bg-amber-50 text-amber-700 border border-amber-200' : 'bg-slate-100 text-slate-700')}">
                                            ${item.status}
                                        </span>
                                    </div>
                                </div>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>

            <!-- Recent Export Requests -->
            <div class="bg-white rounded-2xl border border-slate-200/80 shadow-sm overflow-hidden">
                <div class="px-6 py-4 border-b border-slate-100 flex items-center justify-between">
                    <h2 class="text-base font-bold text-slate-900 flex items-center space-x-2">
                        <i class="fa-solid fa-arrow-up-long text-emerald-600"></i>
                        <span>Yêu cầu Xuất kho Gần đây</span>
                    </h2>
                    <a href="${pageContext.request.contextPath}/app/export" class="text-xs font-medium text-blue-600 hover:underline">Xem tất cả &rarr;</a>
                </div>
                <div class="divide-y divide-slate-100">
                    <c:choose>
                        <c:when test="${empty recentExports}">
                            <div class="p-8 text-center text-sm text-slate-400">Chưa có yêu cầu xuất kho nào.</div>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="item" items="${recentExports}">
                                <div class="p-4 px-6 flex items-center justify-between hover:bg-slate-50 transition">
                                    <div>
                                        <div class="text-sm font-semibold text-slate-800">Mã xuất #${item.exportRequestId} - ${item.customerName}</div>
                                        <div class="text-xs text-slate-400 mt-0.5">Tạo bởi: ${item.creatorName} • ${item.requestDate}</div>
                                    </div>
                                    <div>
                                        <span class="inline-flex items-center px-2.5 py-1 rounded-full text-xs font-medium ${item.status == 'APPROVED' ? 'bg-emerald-50 text-emerald-700 border border-emerald-200' : (item.status == 'PENDING' ? 'bg-amber-50 text-amber-700 border border-amber-200' : 'bg-slate-100 text-slate-700')}">
                                            ${item.status}
                                        </span>
                                    </div>
                                </div>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>

        <!-- Recent Audit Log Table -->
        <div class="bg-white rounded-2xl border border-slate-200/80 shadow-sm overflow-hidden">
            <div class="px-6 py-4 border-b border-slate-100">
                <h2 class="text-base font-bold text-slate-900 flex items-center space-x-2">
                    <i class="fa-solid fa-clock-rotate-left text-slate-600"></i>
                    <span>Nhật ký Thao tác Hệ thống (Audit Logs)</span>
                </h2>
            </div>
            <div class="overflow-x-auto">
                <table class="w-full text-left border-collapse text-sm">
                    <thead>
                        <tr class="bg-slate-50 border-b border-slate-100 text-xs font-semibold text-slate-500 uppercase tracking-wider">
                            <th class="py-3 px-6">Thời gian</th>
                            <th class="py-3 px-6">Người thực hiện</th>
                            <th class="py-3 px-6">Hành động</th>
                            <th class="py-3 px-6">Bảng dữ liệu</th>
                            <th class="py-3 px-6">Chi tiết</th>
                            <th class="py-3 px-6">IP</th>
                        </tr>
                    </thead>
                    <tbody class="divide-y divide-slate-100">
                        <c:forEach var="log" items="${recentLogs}">
                            <tr class="hover:bg-slate-50/50">
                                <td class="py-3 px-6 text-slate-500">${log.timestamp}</td>
                                <td class="py-3 px-6 font-medium text-slate-800">${log.username}</td>
                                <td class="py-3 px-6">
                                    <span class="inline-flex items-center px-2 py-0.5 rounded text-xs font-medium bg-slate-100 text-slate-800">
                                        ${log.action}
                                    </span>
                                </td>
                                <td class="py-3 px-6 text-slate-600 font-mono text-xs">${log.tableName}</td>
                                <td class="py-3 px-6 text-slate-600">${log.newValues}</td>
                                <td class="py-3 px-6 text-slate-400 font-mono text-xs">${log.ipAddress}</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </main>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
</body>
</html>

