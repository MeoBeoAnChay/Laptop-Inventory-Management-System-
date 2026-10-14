<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<jsp:include page="/WEB-INF/views/common/head.jsp">
    <jsp:param name="title" value="Tồn kho Chi tiết" />
</jsp:include>
<body class="bg-slate-50 text-slate-800 antialiased min-h-screen flex flex-col">

<jsp:include page="/WEB-INF/views/common/navbar.jsp" />

<div class="flex flex-1">
    <jsp:include page="/WEB-INF/views/common/sidebar.jsp" />

    <main class="flex-1 p-8 overflow-y-auto">
        <!-- Title -->
        <div class="mb-6 flex flex-col md:flex-row md:items-center md:justify-between gap-4">
            <div>
                <h1 class="text-2xl font-bold text-slate-900 tracking-tight">Quản lý Tồn kho Chi tiết (Serial & Vị trí)</h1>
                <p class="text-sm text-slate-500 mt-1">Theo dõi từng máy theo Serial Number duy nhất, vị trí kệ kho (Rack/Bin) và tình trạng máy.</p>
            </div>
        </div>

        <!-- Filter Bar -->
        <div class="bg-white p-5 rounded-2xl border border-slate-200/80 shadow-sm mb-6">
            <form action="${pageContext.request.contextPath}/app/inventory" method="GET" class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
                <div>
                    <label class="block text-xs font-semibold text-slate-500 uppercase mb-1">Số Serial / Tên máy</label>
                    <input type="text" name="keyword" value="${keyword}" placeholder="SN-..., Dell, ROG..."
                           class="w-full px-3.5 py-2 bg-slate-50 border border-slate-200 rounded-xl text-sm focus:outline-none focus:ring-2 focus:ring-blue-500" />
                </div>
                <div>
                    <label class="block text-xs font-semibold text-slate-500 uppercase mb-1">Trạng thái máy</label>
                    <select name="status" class="w-full px-3.5 py-2 bg-slate-50 border border-slate-200 rounded-xl text-sm focus:outline-none focus:ring-2 focus:ring-blue-500">
                        <option value="ALL">Tất cả trạng thái</option>
                        <option value="IN_STOCK" ${selectedStatus == 'IN_STOCK' ? 'selected' : ''}>IN_STOCK (Còn hàng)</option>
                        <option value="RESERVED" ${selectedStatus == 'RESERVED' ? 'selected' : ''}>RESERVED (Đã đặt trước)</option>
                        <option value="EXPORTED" ${selectedStatus == 'EXPORTED' ? 'selected' : ''}>EXPORTED (Đã xuất)</option>
                        <option value="DAMAGED" ${selectedStatus == 'DAMAGED' ? 'selected' : ''}>DAMAGED (Hỏng hóc)</option>
                        <option value="RETURNED" ${selectedStatus == 'RETURNED' ? 'selected' : ''}>RETURNED (Đổi trả)</option>
                    </select>
                </div>
                <div>
                    <label class="block text-xs font-semibold text-slate-500 uppercase mb-1">Vị trí kệ kho</label>
                    <input type="text" name="location" value="${selectedLocation}" placeholder="A-01, B-02..."
                           class="w-full px-3.5 py-2 bg-slate-50 border border-slate-200 rounded-xl text-sm focus:outline-none focus:ring-2 focus:ring-blue-500" />
                </div>
                <div class="flex items-end">
                    <button type="submit" class="w-full py-2 px-4 bg-slate-800 hover:bg-slate-900 text-white rounded-xl text-sm font-medium transition flex items-center justify-center space-x-2">
                        <i class="fa-solid fa-filter"></i>
                        <span>Lọc tồn kho</span>
                    </button>
                </div>
            </form>
        </div>

        <!-- Inventory Table -->
        <div class="bg-white rounded-2xl border border-slate-200/80 shadow-sm overflow-hidden">
            <div class="overflow-x-auto">
                <table class="w-full text-left border-collapse text-sm">
                    <thead>
                        <tr class="bg-slate-50 border-b border-slate-100 text-xs font-semibold text-slate-500 uppercase tracking-wider">
                            <th class="py-3.5 px-6">ID</th>
                            <th class="py-3.5 px-6">Số Serial (S/N)</th>
                            <th class="py-3.5 px-6">Sản phẩm Laptop</th>
                            <th class="py-3.5 px-6">Vị trí kho</th>
                            <th class="py-3.5 px-6">Trạng thái máy</th>
                            <th class="py-3.5 px-6">Cập nhật lần cuối</th>
                            <th class="py-3.5 px-6 text-right">Thao tác</th>
                        </tr>
                    </thead>
                    <tbody class="divide-y divide-slate-100">
                        <c:choose>
                            <c:when test="${empty inventoryList}">
                                <tr>
                                    <td colspan="7" class="p-8 text-center text-slate-400">Không có máy nào trong kho thỏa điều kiện.</td>
                                </tr>
                            </c:when>
                            <c:otherwise>
                                <c:forEach var="inv" items="${inventoryList}">
                                    <tr class="hover:bg-slate-50/50">
                                        <td class="py-3.5 px-6 font-mono text-slate-400">#${inv.inventoryId}</td>
                                        <td class="py-3.5 px-6 font-mono font-bold text-slate-900 flex items-center space-x-2">
                                            <i class="fa-solid fa-barcode text-slate-400"></i>
                                            <span>${inv.serialNumber}</span>
                                        </td>
                                        <td class="py-3.5 px-6">
                                            <div class="font-medium text-slate-900">${inv.productName}</div>
                                            <div class="text-xs text-slate-500 font-mono">${inv.productCode}</div>
                                        </td>
                                        <td class="py-3.5 px-6">
                                            <span class="inline-flex items-center px-2.5 py-1 rounded-lg text-xs font-semibold bg-slate-100 text-slate-700 border border-slate-200">
                                                <i class="fa-solid fa-location-dot mr-1.5 text-blue-500"></i> ${inv.location}
                                            </span>
                                        </td>
                                        <td class="py-3.5 px-6">
                                            <c:choose>
                                                <c:when test="${inv.status == 'IN_STOCK'}">
                                                    <span class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-semibold bg-emerald-50 text-emerald-700 border border-emerald-200">IN_STOCK</span>
                                                </c:when>
                                                <c:when test="${inv.status == 'RESERVED'}">
                                                    <span class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-semibold bg-amber-50 text-amber-700 border border-amber-200">RESERVED</span>
                                                </c:when>
                                                <c:when test="${inv.status == 'EXPORTED'}">
                                                    <span class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-semibold bg-blue-50 text-blue-700 border border-blue-200">EXPORTED</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-semibold bg-rose-50 text-rose-700 border border-rose-200">${inv.status}</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td class="py-3.5 px-6 text-xs text-slate-500">${inv.updatedAt}</td>
                                        <td class="py-3.5 px-6 text-right">
                                            <button onclick="openUpdateModal('${inv.inventoryId}', '${inv.serialNumber}', '${inv.status}', '${inv.location}')"
                                                    class="text-blue-600 hover:text-blue-800 text-xs font-semibold bg-blue-50 hover:bg-blue-100 px-3 py-1.5 rounded-lg border border-blue-200 transition">
                                                Đổi vị trí / trạng thái
                                            </button>
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
                    <span>Tổng số: ${totalItems} máy</span>
                    <div class="flex items-center space-x-1">
                        <c:forEach begin="1" end="${totalPages}" var="pageIndex">
                            <a href="${pageContext.request.contextPath}/app/inventory?page=${pageIndex}&keyword=${keyword}&status=${selectedStatus}&location=${selectedLocation}"
                               class="px-3 py-1.5 rounded-lg border font-medium ${currentPage == pageIndex ? 'bg-blue-600 text-white border-blue-600' : 'bg-white hover:bg-slate-50 text-slate-700 border-slate-200'}">
                                ${pageIndex}
                            </a>
                        </c:forEach>
                    </div>
                </div>
            </c:if>
        </div>

        <!-- Update Inventory Modal -->
        <div id="updateInventoryModal" class="hidden fixed inset-0 z-50 flex items-center justify-center bg-slate-900/40 backdrop-blur-sm p-4">
            <div class="bg-white rounded-2xl max-w-md w-full p-6 shadow-2xl border border-slate-200">
                <div class="flex items-center justify-between pb-4 border-b border-slate-100">
                    <h3 class="text-base font-bold text-slate-900">Cập nhật Máy: <span id="modalSerial" class="font-mono text-blue-600"></span></h3>
                    <button onclick="document.getElementById('updateInventoryModal').classList.add('hidden')" class="text-slate-400 hover:text-slate-600">
                        <i class="fa-solid fa-xmark text-lg"></i>
                    </button>
                </div>
                <form action="${pageContext.request.contextPath}/app/inventory" method="POST" class="mt-4 space-y-4">
                    <input type="hidden" name="action" value="update" />
                    <input type="hidden" id="modalInvId" name="inventoryId" />

                    <div>
                        <label class="block text-xs font-semibold text-slate-600 uppercase mb-1">Trạng thái mới</label>
                        <select id="modalStatus" name="newStatus" class="w-full px-3 py-2 border rounded-xl text-sm">
                            <option value="IN_STOCK">IN_STOCK (Còn hàng)</option>
                            <option value="RESERVED">RESERVED (Đã đặt trước)</option>
                            <option value="EXPORTED">EXPORTED (Đã xuất)</option>
                            <option value="DAMAGED">DAMAGED (Lỗi hỏng)</option>
                            <option value="RETURNED">RETURNED (Đổi trả)</option>
                        </select>
                    </div>

                    <div>
                        <label class="block text-xs font-semibold text-slate-600 uppercase mb-1">Vị trí lưu kho (Kệ - Tầng - Ô)</label>
                        <input type="text" id="modalLocation" name="newLocation" required class="w-full px-3 py-2 border rounded-xl text-sm" />
                    </div>

                    <div class="pt-4 border-t border-slate-100 flex items-center justify-end space-x-3">
                        <button type="button" onclick="document.getElementById('updateInventoryModal').classList.add('hidden')"
                                class="px-4 py-2 border border-slate-200 text-slate-700 text-sm font-medium rounded-xl hover:bg-slate-50">
                            Hủy
                        </button>
                        <button type="submit"
                                class="px-5 py-2 bg-blue-600 hover:bg-blue-700 text-white text-sm font-medium rounded-xl shadow-sm">
                            Cập nhật
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </main>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />

<script>
    function openUpdateModal(id, serial, status, loc) {
        document.getElementById('modalInvId').value = id;
        document.getElementById('modalSerial').innerText = serial;
        document.getElementById('modalStatus').value = status;
        document.getElementById('modalLocation').value = loc;
        document.getElementById('updateInventoryModal').classList.remove('hidden');
    }
</script>
</body>
</html>

