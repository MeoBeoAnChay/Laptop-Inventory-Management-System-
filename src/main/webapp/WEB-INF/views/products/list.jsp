<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<jsp:include page="/WEB-INF/views/common/head.jsp">
    <jsp:param name="title" value="Danh mục Laptop" />
</jsp:include>
<body class="bg-slate-50 text-slate-800 antialiased min-h-screen flex flex-col">

<jsp:include page="/WEB-INF/views/common/navbar.jsp" />

<div class="flex flex-1">
    <jsp:include page="/WEB-INF/views/common/sidebar.jsp" />

    <main class="flex-1 p-8 overflow-y-auto">
        <!-- Breadcrumb & Title -->
        <div class="mb-6 flex flex-col md:flex-row md:items-center md:justify-between gap-4">
            <div>
                <h1 class="text-2xl font-bold text-slate-900 tracking-tight">Danh mục Sản phẩm Laptop</h1>
                <p class="text-sm text-slate-500 mt-1">Quản lý danh sách cấu hình, thông số kỹ thuật và số lượng tồn kho.</p>
            </div>
            <button onclick="document.getElementById('createProductModal').classList.remove('hidden')"
                    class="px-4 py-2.5 bg-blue-600 hover:bg-blue-700 text-white rounded-xl text-sm font-medium shadow-sm transition flex items-center space-x-2">
                <i class="fa-solid fa-plus"></i>
                <span>Thêm Laptop Mới</span>
            </button>
        </div>

        <!-- Filter & Search Bar -->
        <div class="bg-white p-5 rounded-2xl border border-slate-200/80 shadow-sm mb-6">
            <form action="${pageContext.request.contextPath}/app/products" method="GET" class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
                <div>
                    <label class="block text-xs font-semibold text-slate-500 uppercase mb-1">Từ khóa</label>
                    <input type="text" name="keyword" value="${keyword}" placeholder="Mã sp, tên laptop, CPU..."
                           class="w-full px-3.5 py-2 bg-slate-50 border border-slate-200 rounded-xl text-sm focus:outline-none focus:ring-2 focus:ring-blue-500" />
                </div>
                <div>
                    <label class="block text-xs font-semibold text-slate-500 uppercase mb-1">Thương hiệu</label>
                    <select name="brandId" class="w-full px-3.5 py-2 bg-slate-50 border border-slate-200 rounded-xl text-sm focus:outline-none focus:ring-2 focus:ring-blue-500">
                        <option value="">Tất cả thương hiệu</option>
                        <c:forEach var="b" items="${brands}">
                            <option value="${b.brandId}" ${selectedBrandId == b.brandId ? 'selected' : ''}>${b.brandName}</option>
                        </c:forEach>
                    </select>
                </div>
                <div>
                    <label class="block text-xs font-semibold text-slate-500 uppercase mb-1">Trạng thái</label>
                    <select name="status" class="w-full px-3.5 py-2 bg-slate-50 border border-slate-200 rounded-xl text-sm focus:outline-none focus:ring-2 focus:ring-blue-500">
                        <option value="ALL">Tất cả</option>
                        <option value="ACTIVE" ${selectedStatus == 'ACTIVE' ? 'selected' : ''}>ACTIVE (Đang kinh doanh)</option>
                        <option value="INACTIVE" ${selectedStatus == 'INACTIVE' ? 'selected' : ''}>INACTIVE (Tạm ngưng)</option>
                        <option value="DISCONTINUED" ${selectedStatus == 'DISCONTINUED' ? 'selected' : ''}>DISCONTINUED (Ngừng sx)</option>
                    </select>
                </div>
                <div class="flex items-end">
                    <button type="submit" class="w-full py-2 px-4 bg-slate-800 hover:bg-slate-900 text-white rounded-xl text-sm font-medium transition flex items-center justify-center space-x-2">
                        <i class="fa-solid fa-magnifying-glass"></i>
                        <span>Tìm kiếm</span>
                    </button>
                </div>
            </form>
        </div>

        <!-- Products Table -->
        <div class="bg-white rounded-2xl border border-slate-200/80 shadow-sm overflow-hidden">
            <div class="overflow-x-auto">
                <table class="w-full text-left border-collapse text-sm">
                    <thead>
                        <tr class="bg-slate-50 border-b border-slate-100 text-xs font-semibold text-slate-500 uppercase tracking-wider">
                            <th class="py-3.5 px-6">Mã Laptop</th>
                            <th class="py-3.5 px-6">Tên Model & Hãng</th>
                            <th class="py-3.5 px-6">Cấu hình chi tiết</th>
                            <th class="py-3.5 px-6">Đơn giá</th>
                            <th class="py-3.5 px-6">Tồn kho</th>
                            <th class="py-3.5 px-6">Trạng thái</th>
                        </tr>
                    </thead>
                    <tbody class="divide-y divide-slate-100">
                        <c:choose>
                            <c:when test="${empty products}">
                                <tr>
                                    <td colspan="6" class="p-8 text-center text-slate-400">Không tìm thấy sản phẩm nào phù hợp.</td>
                                </tr>
                            </c:when>
                            <c:otherwise>
                                <c:forEach var="p" items="${products}">
                                    <tr class="hover:bg-slate-50/50">
                                        <td class="py-3.5 px-6 font-mono font-semibold text-blue-600">${p.productCode}</td>
                                        <td class="py-3.5 px-6">
                                            <div class="font-medium text-slate-900">${p.productName}</div>
                                            <div class="text-xs text-slate-500">${p.brandName} • ${p.lineName}</div>
                                        </td>
                                        <td class="py-3.5 px-6">
                                            <div class="text-xs text-slate-700">CPU: <span class="font-medium">${p.cpu}</span> | RAM: <span class="font-medium">${p.ram}</span></div>
                                            <div class="text-xs text-slate-500">SSD: ${p.storage} | GPU: ${p.gpu}</div>
                                        </td>
                                        <td class="py-3.5 px-6 font-semibold text-slate-800">
                                            <fmt:formatNumber value="${p.unitPrice}" type="currency" currencySymbol="₫" maxFractionDigits="0" />
                                        </td>
                                        <td class="py-3.5 px-6">
                                            <span class="inline-flex items-center px-2.5 py-1 rounded-full text-xs font-bold ${p.currentStock > 0 ? 'bg-emerald-50 text-emerald-700 border border-emerald-200' : 'bg-rose-50 text-rose-700 border border-rose-200'}">
                                                ${p.currentStock} máy
                                            </span>
                                        </td>
                                        <td class="py-3.5 px-6">
                                            <span class="inline-flex items-center px-2 py-0.5 rounded text-xs font-medium ${p.status == 'ACTIVE' ? 'bg-emerald-100 text-emerald-800' : 'bg-slate-100 text-slate-800'}">
                                                ${p.status}
                                            </span>
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
                    <span>Tổng số: ${totalProducts} sản phẩm</span>
                    <div class="flex items-center space-x-1">
                        <c:forEach begin="1" end="${totalPages}" var="pageIndex">
                            <a href="${pageContext.request.contextPath}/app/products?page=${pageIndex}&keyword=${keyword}&brandId=${selectedBrandId}&status=${selectedStatus}"
                               class="px-3 py-1.5 rounded-lg border font-medium ${currentPage == pageIndex ? 'bg-blue-600 text-white border-blue-600' : 'bg-white hover:bg-slate-50 text-slate-700 border-slate-200'}">
                                ${pageIndex}
                            </a>
                        </c:forEach>
                    </div>
                </div>
            </c:if>
        </div>

        <!-- Create Laptop Modal -->
        <div id="createProductModal" class="hidden fixed inset-0 z-50 flex items-center justify-center bg-slate-900/40 backdrop-blur-sm p-4">
            <div class="bg-white rounded-2xl max-w-2xl w-full p-6 shadow-2xl border border-slate-200 max-h-[90vh] overflow-y-auto">
                <div class="flex items-center justify-between pb-4 border-b border-slate-100">
                    <h3 class="text-lg font-bold text-slate-900">Thêm Laptop Mới Vào Hệ Thống</h3>
                    <button onclick="document.getElementById('createProductModal').classList.add('hidden')" class="text-slate-400 hover:text-slate-600">
                        <i class="fa-solid fa-xmark text-lg"></i>
                    </button>
                </div>
                <form action="${pageContext.request.contextPath}/app/products" method="POST" class="mt-4 space-y-4">
                    <input type="hidden" name="action" value="create" />
                    <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
                        <div>
                            <label class="block text-xs font-semibold text-slate-600 uppercase mb-1">Mã sản phẩm *</label>
                            <input type="text" name="productCode" required placeholder="DELL-XPS-9530" class="w-full px-3 py-2 border rounded-xl text-sm" />
                        </div>
                        <div>
                            <label class="block text-xs font-semibold text-slate-600 uppercase mb-1">Dòng sản phẩm (Line) *</label>
                            <select name="productLineId" required class="w-full px-3 py-2 border rounded-xl text-sm">
                                <c:forEach var="pl" items="${productLines}">
                                    <option value="${pl.productLineId}">${pl.brandName} - ${pl.lineName}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>

                    <div>
                        <label class="block text-xs font-semibold text-slate-600 uppercase mb-1">Tên Laptop đầy đủ *</label>
                        <input type="text" name="productName" required placeholder="Dell XPS 15 9530 OLED Touch" class="w-full px-3 py-2 border rounded-xl text-sm" />
                    </div>

                    <div class="grid grid-cols-1 sm:grid-cols-3 gap-4">
                        <div>
                            <label class="block text-xs font-semibold text-slate-600 uppercase mb-1">CPU *</label>
                            <input type="text" name="cpu" required placeholder="Intel i7-13700H" class="w-full px-3 py-2 border rounded-xl text-sm" />
                        </div>
                        <div>
                            <label class="block text-xs font-semibold text-slate-600 uppercase mb-1">RAM *</label>
                            <input type="text" name="ram" required placeholder="32GB DDR5" class="w-full px-3 py-2 border rounded-xl text-sm" />
                        </div>
                        <div>
                            <label class="block text-xs font-semibold text-slate-600 uppercase mb-1">Ổ cứng (SSD) *</label>
                            <input type="text" name="storage" required placeholder="1TB NVMe SSD" class="w-full px-3 py-2 border rounded-xl text-sm" />
                        </div>
                    </div>

                    <div class="grid grid-cols-1 sm:grid-cols-3 gap-4">
                        <div>
                            <label class="block text-xs font-semibold text-slate-600 uppercase mb-1">GPU (Card màn hình)</label>
                            <input type="text" name="gpu" placeholder="RTX 4060 8GB" class="w-full px-3 py-2 border rounded-xl text-sm" />
                        </div>
                        <div>
                            <label class="block text-xs font-semibold text-slate-600 uppercase mb-1">Màn hình</label>
                            <input type="text" name="screen" placeholder="15.6 3.5K OLED" class="w-full px-3 py-2 border rounded-xl text-sm" />
                        </div>
                        <div>
                            <label class="block text-xs font-semibold text-slate-600 uppercase mb-1">Đơn giá bán (VNĐ) *</label>
                            <input type="number" step="10000" name="unitPrice" required placeholder="45000000" class="w-full px-3 py-2 border rounded-xl text-sm" />
                        </div>
                    </div>

                    <div class="pt-4 border-t border-slate-100 flex items-center justify-end space-x-3">
                        <button type="button" onclick="document.getElementById('createProductModal').classList.add('hidden')"
                                class="px-4 py-2 border border-slate-200 text-slate-700 text-sm font-medium rounded-xl hover:bg-slate-50">
                            Hủy bỏ
                        </button>
                        <button type="submit"
                                class="px-5 py-2 bg-blue-600 hover:bg-blue-700 text-white text-sm font-medium rounded-xl shadow-sm">
                            Lưu Laptop
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

