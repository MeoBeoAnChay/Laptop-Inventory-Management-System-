<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<jsp:include page="/WEB-INF/views/common/head.jsp">
    <jsp:param name="title" value="Cấu hình Hệ thống" />
</jsp:include>
<body class="bg-slate-50 text-slate-800 antialiased min-h-screen flex flex-col">

<jsp:include page="/WEB-INF/views/common/navbar.jsp" />

<div class="flex flex-1">
    <jsp:include page="/WEB-INF/views/common/sidebar.jsp" />

    <main class="flex-1 p-8 overflow-y-auto">
        <!-- Breadcrumb & Header -->
        <div class="mb-6 flex flex-col md:flex-row md:items-center md:justify-between gap-4">
            <div>
                <div class="text-xs font-semibold text-blue-600 uppercase tracking-wider">Hệ thống Quản trị / Thiết lập (FE-17)</div>
                <h1 class="text-2xl font-bold text-slate-900 tracking-tight mt-1">Cấu Hình Tham Số Hệ Thống (UC-75 $\rightarrow$ UC-77)</h1>
                <p class="text-sm text-slate-500">Thiết lập tham số an ninh, phiên làm việc, kho bãi và dịch vụ email SMTP.</p>
            </div>
        </div>

        <c:if test="${param.success == 'updated'}">
            <div class="mb-5 p-4 rounded-xl bg-emerald-50 border border-emerald-200 text-emerald-700 text-sm flex items-center space-x-2">
                <i class="fa-solid fa-circle-check"></i>
                <span>Cập nhật cấu hình hệ thống thành công và đã ghi nhận vào nhật ký kiểm toán!</span>
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/app/admin/config" method="POST" class="space-y-6 max-w-4xl">
            <!-- Card 1: General Info -->
            <div class="bg-white p-6 rounded-2xl border border-slate-200/80 shadow-sm">
                <h2 class="text-base font-bold text-slate-900 mb-4 flex items-center space-x-2">
                    <i class="fa-solid fa-building text-blue-600"></i>
                    <span>Thông Tin Chung Đơn Vị & Kho Hàng</span>
                </h2>
                <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
                    <div>
                        <label class="block text-xs font-semibold text-slate-600 uppercase mb-1">Tên Ứng Dụng</label>
                        <input type="text" name="system_name" value="${configs['system_name']}" class="w-full px-3 py-2 border rounded-xl text-sm" />
                    </div>
                    <div>
                        <label class="block text-xs font-semibold text-slate-600 uppercase mb-1">Tên Kho Tổng</label>
                        <input type="text" name="warehouse_name" value="${configs['warehouse_name']}" class="w-full px-3 py-2 border rounded-xl text-sm" />
                    </div>
                </div>
            </div>

            <!-- Card 2: Security & Session Policies -->
            <div class="bg-white p-6 rounded-2xl border border-slate-200/80 shadow-sm">
                <h2 class="text-base font-bold text-slate-900 mb-4 flex items-center space-x-2">
                    <i class="fa-solid fa-shield-halved text-purple-600"></i>
                    <span>Chính Sách Bảo Mật & Phiên Làm Việc (Session)</span>
                </h2>
                <div class="grid grid-cols-1 sm:grid-cols-3 gap-4">
                    <div>
                        <label class="block text-xs font-semibold text-slate-600 uppercase mb-1">Thời gian hết hạn phiên (Phút)</label>
                        <input type="number" name="session_timeout" value="${configs['session_timeout']}" min="10" max="1440" class="w-full px-3 py-2 border rounded-xl text-sm" />
                    </div>
                    <div>
                        <label class="block text-xs font-semibold text-slate-600 uppercase mb-1">Số lần đăng nhập sai tối đa</label>
                        <input type="number" name="max_login_attempts" value="${configs['max_login_attempts']}" min="3" max="10" class="w-full px-3 py-2 border rounded-xl text-sm" />
                    </div>
                    <div>
                        <label class="block text-xs font-semibold text-slate-600 uppercase mb-1">Độ dài mật khẩu tối thiểu</label>
                        <input type="number" name="min_password_length" value="${configs['min_password_length']}" min="6" max="32" class="w-full px-3 py-2 border rounded-xl text-sm" />
                    </div>
                </div>
                <div class="mt-4 pt-4 border-t border-slate-100 flex items-center justify-between">
                    <div>
                        <div class="text-sm font-semibold text-slate-800">Chế độ bảo trì hệ thống (Maintenance Mode)</div>
                        <div class="text-xs text-slate-500">Chỉ cho phép tài khoản Admin đăng nhập khi kích hoạt chế độ này.</div>
                    </div>
                    <label class="relative inline-flex items-center cursor-pointer">
                        <input type="checkbox" name="maintenance_mode" ${configs['maintenance_mode'] == 'true' ? 'checked' : ''} class="sr-only peer">
                        <div class="w-11 h-6 bg-slate-200 peer-focus:outline-none rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-slate-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-blue-600"></div>
                    </label>
                </div>
            </div>

            <!-- Card 3: SMTP Mail Server -->
            <div class="bg-white p-6 rounded-2xl border border-slate-200/80 shadow-sm">
                <h2 class="text-base font-bold text-slate-900 mb-4 flex items-center space-x-2">
                    <i class="fa-solid fa-envelope text-emerald-600"></i>
                    <span>Cấu Hình Máy Chủ Email SMTP</span>
                </h2>
                <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
                    <div>
                        <label class="block text-xs font-semibold text-slate-600 uppercase mb-1">SMTP Host</label>
                        <input type="text" name="smtp_host" value="${configs['smtp_host']}" class="w-full px-3 py-2 border rounded-xl text-sm font-mono" />
                    </div>
                    <div>
                        <label class="block text-xs font-semibold text-slate-600 uppercase mb-1">Cổng SMTP</label>
                        <input type="text" name="smtp_port" value="${configs['smtp_port']}" class="w-full px-3 py-2 border rounded-xl text-sm font-mono" />
                    </div>
                </div>
            </div>

            <!-- Submit Button -->
            <div class="flex items-center justify-end">
                <button type="submit"
                        class="px-6 py-2.5 bg-blue-600 hover:bg-blue-700 text-white font-medium rounded-xl shadow-sm transition flex items-center space-x-2">
                    <i class="fa-solid fa-floppy-disk"></i>
                    <span>Lưu Cấu Hình (UC-76)</span>
                </button>
            </div>
        </form>
    </main>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
</body>
</html>

