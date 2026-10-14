<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<jsp:include page="/WEB-INF/views/common/head.jsp">
    <jsp:param name="title" value="Yêu cầu Nhập kho" />
</jsp:include>
<body class="bg-slate-50 text-slate-800 antialiased min-h-screen flex flex-col">

<jsp:include page="/WEB-INF/views/common/navbar.jsp" />

<div class="flex flex-1">
    <jsp:include page="/WEB-INF/views/common/sidebar.jsp" />

    <main class="flex-1 p-8 overflow-y-auto">
        <div class="mb-6 flex flex-col md:flex-row md:items-center md:justify-between gap-4">
            <div>
                <h1 class="text-2xl font-bold text-slate-900 tracking-tight">Yêu cầu Nhập kho (Import Requests)</h1>
                <p class="text-sm text-slate-500 mt-1">Quản lý và phê duyệt đơn đặt hàng laptop từ nhà cung cấp.</p>
            </div>
            <button onclick="document.getElementById('createImportModal').classList.remove('hidden')"
                    class="px-4 py-2.5 bg-blue-600 hover:bg-blue-700 text-white rounded-xl text-sm font-medium shadow-sm transition flex items-center space-x-2">
                <i class="fa-solid fa-plus"></i>
                <span>Tạo Yêu Cầu Nhập</span>
            </button>
        </div>

        <div class="bg-white rounded-2xl border border-slate-200/80 shadow-sm overflow-hidden">
            <div class="overflow-x-auto">
                <table class="w-full text-left border-collapse text-sm">
                    <thead>
                        <tr class="bg-slate-50 border-b border-slate-100 text-xs font-semibold text-slate-500 uppercase tracking-wider">
                            <th class="py-3.5 px-6">Mã Yêu Cầu</th>
                            <th class="py-3.5 px-6">Nhà Cung Cấp</th>
                            <th class="py-3.5 px-6">Người tạo & Ngày yêu cầu</th>
                            <th class="py-3.5 px-6">Trạng thái</th>
                            <th class="py-3.5 px-6">Người duyệt</th>
                            <th class="py-3.5 px-6 text-right">Hành động</th>
                        </tr>
                    </thead>
                    <tbody class="divide-y divide-slate-100">
                        <c:choose>
                            <c:when test="${empty requests}">
                                <tr>
                                    <td colspan="6" class="p-8 text-center text-slate-400">Chưa có yêu cầu nhập kho nào.</td>
                                </tr>
                            </c:when>
                            <c:otherwise>
                                <c:forEach var="r" items="${requests}">
                                    <tr class="hover:bg-slate-50/50">
                                        <td class="py-3.5 px-6 font-mono font-bold text-blue-600">#${r.importRequestId}</td>
                                        <td class="py-3.5 px-6 font-medium text-slate-900">${r.supplierName}</td>
                                        <td class="py-3.5 px-6">
                                            <div class="text-slate-800 font-medium">${r.creatorName}</div>
                                            <div class="text-xs text-slate-400">${r.requestDate}</div>
                                        </td>
                                        <td class="py-3.5 px-6">
                                            <span class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${r.status == 'APPROVED' ? 'bg-emerald-50 text-emerald-700 border border-emerald-200' : (r.status == 'PENDING' ? 'bg-amber-50 text-amber-700 border border-amber-200' : 'bg-slate-100 text-slate-700')}">
                                                ${r.status}
                                            </span>
                                        </td>
                                        <td class="py-3.5 px-6 text-xs text-slate-500">
                                            ${r.approverName != null ? r.approverName : 'Chưa phê duyệt'}
                                        </td>
                                        <td class="py-3.5 px-6 text-right space-x-2">
                                            <c:if test="${r.status == 'PENDING' and (sessionScope.currentUser.hasRole('Manager') or sessionScope.currentUser.hasRole('Admin'))}">
                                                <form action="${pageContext.request.contextPath}/app/import" method="POST" class="inline">
                                                    <input type="hidden" name="action" value="status" />
                                                    <input type="hidden" name="id" value="${r.importRequestId}" />
                                                    <input type="hidden" name="newStatus" value="APPROVED" />
                                                    <button type="submit" class="px-2.5 py-1 text-xs font-semibold bg-emerald-50 text-emerald-700 border border-emerald-200 rounded-lg hover:bg-emerald-100">
                                                        Phê duyệt
                                                    </button>
                                                </form>
                                                <form action="${pageContext.request.contextPath}/app/import" method="POST" class="inline">
                                                    <input type="hidden" name="action" value="status" />
                                                    <input type="hidden" name="id" value="${r.importRequestId}" />
                                                    <input type="hidden" name="newStatus" value="REJECTED" />
                                                    <button type="submit" class="px-2.5 py-1 text-xs font-semibold bg-rose-50 text-rose-700 border border-rose-200 rounded-lg hover:bg-rose-100">
                                                        Từ chối
                                                    </button>
                                                </form>
                                            </c:if>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                    </tbody>
                </table>
            </div>
        </div>

        <!-- Create Import Modal -->
        <div id="createImportModal" class="hidden fixed inset-0 z-50 flex items-center justify-center bg-slate-900/40 backdrop-blur-sm p-4">
            <div class="bg-white rounded-2xl max-w-xl w-full p-6 shadow-2xl border border-slate-200">
                <div class="flex items-center justify-between pb-4 border-b border-slate-100">
                    <h3 class="text-base font-bold text-slate-900">Tạo Đơn Yêu Cầu Nhập Kho Mới</h3>
                    <button onclick="document.getElementById('createImportModal').classList.add('hidden')" class="text-slate-400 hover:text-slate-600">
                        <i class="fa-solid fa-xmark text-lg"></i>
                    </button>
                </div>
                <form action="${pageContext.request.contextPath}/app/import" method="POST" class="mt-4 space-y-4">
                    <input type="hidden" name="action" value="create" />
                    <div>
                        <label class="block text-xs font-semibold text-slate-600 uppercase mb-1">Nhà cung cấp *</label>
                        <select name="supplierId" required class="w-full px-3 py-2 border rounded-xl text-sm">
                            <c:forEach var="s" items="${suppliers}">
                                <option value="${s.supplierId}">${s.supplierName} (${s.contactName})</option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="p-3 bg-slate-50 rounded-xl border border-slate-200 space-y-3">
                        <div class="text-xs font-bold text-slate-700 uppercase">Sản phẩm laptop cần nhập</div>
                        <div class="grid grid-cols-1 sm:grid-cols-3 gap-2">
                            <div class="sm:col-span-2">
                                <label class="text-[11px] text-slate-500">Chọn mẫu laptop</label>
                                <select name="productId" class="w-full px-2.5 py-1.5 border rounded-lg text-xs">
                                    <c:forEach var="p" items="${products}">
                                        <option value="${p.productId}">${p.productCode} - ${p.productName}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div>
                                <label class="text-[11px] text-slate-500">Số lượng</label>
                                <input type="number" name="quantity" min="1" value="5" class="w-full px-2.5 py-1.5 border rounded-lg text-xs" />
                            </div>
                        </div>
                    </div>

                    <div class="pt-4 border-t border-slate-100 flex items-center justify-end space-x-3">
                        <button type="button" onclick="document.getElementById('createImportModal').classList.add('hidden')"
                                class="px-4 py-2 border border-slate-200 text-slate-700 text-sm font-medium rounded-xl hover:bg-slate-50">
                            Hủy
                        </button>
                        <button type="submit"
                                class="px-5 py-2 bg-blue-600 hover:bg-blue-700 text-white text-sm font-medium rounded-xl shadow-sm">
                            Gửi yêu cầu
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </main>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
</body>
</html>

