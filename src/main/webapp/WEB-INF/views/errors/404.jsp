<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<!DOCTYPE html>
<html lang="vi">
<jsp:include page="/WEB-INF/views/common/head.jsp">
    <jsp:param name="title" value="404 - Không tìm thấy trang" />
</jsp:include>
<body class="bg-slate-50 text-slate-800 flex items-center justify-center min-h-screen p-4">
<div class="max-w-md w-full text-center">
    <div class="inline-flex items-center justify-center w-20 h-20 rounded-full bg-slate-100 text-slate-600 text-3xl mb-4">
        <i class="fa-solid fa-magnifying-glass"></i>
    </div>
    <h1 class="text-3xl font-extrabold text-slate-900 tracking-tight">404 - Không tìm thấy trang</h1>
    <p class="text-sm text-slate-500 mt-2 mb-6">Đường dẫn bạn yêu cầu không tồn tại hoặc đã được thay đổi.</p>
    <a href="${pageContext.request.contextPath}/app/dashboard"
       class="inline-flex items-center space-x-2 px-5 py-2.5 bg-blue-600 hover:bg-blue-700 text-white rounded-xl text-sm font-medium shadow-sm transition">
        <i class="fa-solid fa-arrow-left"></i>
        <span>Quay về Trang Tổng quan</span>
    </a>
</div>
</body>
</html>

