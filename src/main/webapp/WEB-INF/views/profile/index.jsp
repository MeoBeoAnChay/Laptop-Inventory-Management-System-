<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<jsp:include page="/WEB-INF/views/common/head.jsp">
    <jsp:param name="title" value="Hồ sơ cá nhân" />
</jsp:include>
<body class="bg-slate-50 text-slate-800 antialiased min-h-screen flex flex-col">

<jsp:include page="/WEB-INF/views/common/navbar.jsp" />

<div class="flex flex-1">
    <jsp:include page="/WEB-INF/views/common/sidebar.jsp" />

    <main class="flex-1 p-8 overflow-y-auto">
        <!-- Breadcrumb & Header -->
        <div class="mb-6 flex flex-col md:flex-row md:items-center md:justify-between gap-4">
            <div>
                <div class="text-xs font-semibold text-blue-600 uppercase tracking-wider">Tài khoản & An ninh</div>
                <h1 class="text-2xl font-bold text-slate-900 tracking-tight mt-1">Hồ Sơ Cá Nhân & Đổi Mật Khẩu (UC-59 $\rightarrow$ UC-61)</h1>
                <p class="text-sm text-slate-500">Xem thông tin cá nhân, cập nhật liên hệ và thay đổi mật khẩu tài khoản.</p>
            </div>
        </div>

        <!-- Notification Alerts -->
        <c:if test="${param.success == 'profile_updated'}">
            <div class="mb-5 p-4 rounded-xl bg-emerald-50 border border-emerald-200 text-emerald-700 text-sm flex items-center space-x-2">
                <i class="fa-solid fa-circle-check"></i>
                <span>Cập nhật thông tin hồ sơ cá nhân thành công!</span>
            </div>
        </c:if>
        <c:if test="${param.success == 'password_changed'}">
            <div class="mb-5 p-4 rounded-xl bg-emerald-50 border border-emerald-200 text-emerald-700 text-sm flex items-center space-x-2">
                <i class="fa-solid fa-key"></i>
                <span>Mật khẩu của bạn đã được thay đổi an toàn!</span>
            </div>
        </c:if>
        <c:if test="${param.error == 'wrong_current_password'}">
            <div class="mb-5 p-4 rounded-xl bg-rose-50 border border-rose-200 text-rose-700 text-sm flex items-center space-x-2">
                <i class="fa-solid fa-circle-exclamation"></i>
                <span>Mật khẩu hiện tại không chính xác. Vui lòng kiểm tra lại.</span>
            </div>
        </c:if>
        <c:if test="${param.error == 'password_mismatch'}">
            <div class="mb-5 p-4 rounded-xl bg-rose-50 border border-rose-200 text-rose-700 text-sm flex items-center space-x-2">
                <i class="fa-solid fa-circle-exclamation"></i>
                <span>Mật khẩu mới và mật khẩu xác nhận không trùng khớp nhau.</span>
            </div>
        </c:if>

        <div class="grid grid-cols-1 lg:grid-cols-3 gap-8 max-w-5xl">
            <!-- Left Col: Profile Badge -->
            <div class="bg-white rounded-2xl border border-slate-200/80 p-6 shadow-sm text-center">
                <div class="w-24 h-24 mx-auto rounded-full bg-blue-600 text-white font-bold text-3xl flex items-center justify-center shadow-lg shadow-blue-500/20 ring-4 ring-blue-100">
                    ${user.username.substring(0, 1).toUpperCase()}
                </div>
                <h2 class="text-xl font-bold text-slate-900 mt-4">${user.fullName}</h2>
                <p class="text-xs font-mono text-slate-400 mt-0.5">@${user.username}</p>

                <div class="mt-4 flex flex-wrap justify-center gap-1.5">
                    <c:forEach var="r" items="${user.roles}">
                        <span class="px-2.5 py-1 text-xs font-semibold rounded-lg bg-blue-50 text-blue-700 border border-blue-200">
                            ${r.roleName}
                        </span>
                    </c:forEach>
                </div>

                <div class="mt-6 pt-6 border-t border-slate-100 text-left space-y-3 text-xs">
                    <div class="flex items-center justify-between text-slate-500">
                        <span>Trạng thái tài khoản:</span>
                        <span class="font-bold text-emerald-600">${user.status}</span>
                    </div>
                    <div class="flex items-center justify-between text-slate-500">
                        <span>ID người dùng:</span>
                        <span class="font-mono font-medium text-slate-700">#${user.userId}</span>
                    </div>
                    <div class="flex items-center justify-between text-slate-500">
                        <span>Ngày khởi tạo:</span>
                        <span class="font-mono text-slate-600">${user.createdAt}</span>
                    </div>
                </div>
            </div>

            <!-- Right Col: Edit Profile & Change Password -->
            <div class="lg:col-span-2 space-y-6">
                <!-- Form 1: Update Profile (UC-60) -->
                <div class="bg-white rounded-2xl border border-slate-200/80 p-6 shadow-sm">
                    <h3 class="text-base font-bold text-slate-900 mb-4 flex items-center space-x-2">
                        <i class="fa-solid fa-user-pen text-blue-600"></i>
                        <span>Cập Nhật Thông Tin Cá Nhân (UC-60)</span>
                    </h3>
                    <form action="${pageContext.request.contextPath}/app/profile" method="POST" class="space-y-4">
                        <input type="hidden" name="action" value="update_profile" />
                        <div>
                            <label class="block text-xs font-semibold text-slate-600 uppercase mb-1">Họ và tên *</label>
                            <input type="text" name="fullName" value="${user.fullName}" required class="w-full px-3 py-2 border rounded-xl text-sm" />
                        </div>
                        <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
                            <div>
                                <label class="block text-xs font-semibold text-slate-600 uppercase mb-1">Email *</label>
                                <input type="email" name="email" value="${user.email}" required class="w-full px-3 py-2 border rounded-xl text-sm" />
                            </div>
                            <div>
                                <label class="block text-xs font-semibold text-slate-600 uppercase mb-1">Số điện thoại</label>
                                <input type="text" name="phone" value="${user.phone}" class="w-full px-3 py-2 border rounded-xl text-sm" />
                            </div>
                        </div>
                        <div class="pt-2 flex justify-end">
                            <button type="submit" class="px-5 py-2 bg-blue-600 hover:bg-blue-700 text-white text-sm font-medium rounded-xl shadow-sm">
                                Lưu Thông Tin
                            </button>
                        </div>
                    </form>
                </div>

                <!-- Form 2: Change Password (UC-61) -->
                <div class="bg-white rounded-2xl border border-slate-200/80 p-6 shadow-sm">
                    <h3 class="text-base font-bold text-slate-900 mb-4 flex items-center space-x-2">
                        <i class="fa-solid fa-lock text-purple-600"></i>
                        <span>Đổi Mật Khẩu Đăng Nhập (UC-61)</span>
                    </h3>
                    <form action="${pageContext.request.contextPath}/app/profile" method="POST" class="space-y-4">
                        <input type="hidden" name="action" value="change_password" />
                        <div>
                            <label class="block text-xs font-semibold text-slate-600 uppercase mb-1">Mật khẩu hiện tại *</label>
                            <input type="password" name="currentPassword" required placeholder="••••••••" class="w-full px-3 py-2 border rounded-xl text-sm" />
                        </div>
                        <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
                            <div>
                                <label class="block text-xs font-semibold text-slate-600 uppercase mb-1">Mật khẩu mới *</label>
                                <input type="password" name="newPassword" required placeholder="••••••••" class="w-full px-3 py-2 border rounded-xl text-sm" />
                            </div>
                            <div>
                                <label class="block text-xs font-semibold text-slate-600 uppercase mb-1">Xác nhận mật khẩu mới *</label>
                                <input type="password" name="confirmPassword" required placeholder="••••••••" class="w-full px-3 py-2 border rounded-xl text-sm" />
                            </div>
                        </div>
                        <div class="pt-2 flex justify-end">
                            <button type="submit" class="px-5 py-2 bg-purple-600 hover:bg-purple-700 text-white text-sm font-medium rounded-xl shadow-sm">
                                Đổi Mật Khẩu
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </main>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
</body>
</html>

