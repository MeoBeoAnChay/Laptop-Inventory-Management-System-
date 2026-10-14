<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<!DOCTYPE html>
<html lang="vi">
<jsp:include page="/WEB-INF/views/common/head.jsp">
    <jsp:param name="title" value="403 - Không có quyền truy cập" />
</jsp:include>
<body class="bg-slate-50 text-slate-800 flex items-center justify-center min-h-screen p-4">
<div class="max-w-md w-full text-center">
    <div class="inline-flex items-center justify-center w-20 h-20 rounded-full bg-rose-100 text-rose-600 text-3xl mb-4">
        <i class="fa-solid fa-ban"></i>
    </div>
    <h1 class="text-3xl font-extrabold text-slate-900 tracking-tight">403 - Quyền truy cập bị từ chối</h1>
    <p class="text-sm text-slate-500 mt-2 mb-6">Bạn không có vai trò phù hợp (RBAC Permission) để thực hiện thao tác hoặc xem tài nguyên này.</p>
    <a href="${pageContext.request.contextPath}/app/dashboard"
       class="inline-flex items-center space-x-2 px-5 py-2.5 bg-blue-600 hover:bg-blue-700 text-white rounded-xl text-sm font-medium shadow-sm transition">
        <i class="fa-solid fa-arrow-left"></i>
        <span>Quay về Trang Tổng quan</span>
    </a>
</div>
</body>
</html>

