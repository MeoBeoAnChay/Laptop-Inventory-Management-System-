<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<jsp:include page="/WEB-INF/views/common/head.jsp">
    <jsp:param name="title" value="Đăng nhập" />
</jsp:include>
<body class="min-h-screen bg-slate-100 text-slate-800 antialiased">
<main class="grid min-h-screen lg:grid-cols-2">
    <!-- Brand panel -->
    <section class="relative hidden overflow-hidden bg-slate-950 px-12 py-10 text-white lg:flex lg:flex-col lg:justify-between xl:px-20">
        <div class="absolute inset-0 bg-[radial-gradient(circle_at_20%_15%,rgba(59,130,246,.32),transparent_34%),radial-gradient(circle_at_85%_85%,rgba(14,165,233,.18),transparent_30%)]"></div>
        <div class="absolute -right-28 top-1/4 h-96 w-96 rounded-full border border-white/10"></div>
        <div class="absolute -right-12 top-[30%] h-64 w-64 rounded-full border border-white/10"></div>

        <a href="${pageContext.request.contextPath}/login" class="relative inline-flex w-fit items-center gap-3" aria-label="LIMS - Đăng nhập">
            <span class="flex h-12 w-12 items-center justify-center rounded-2xl bg-blue-500 text-xl shadow-lg shadow-blue-950/40">
                <i class="fa-solid fa-laptop"></i>
            </span>
            <span class="text-lg font-bold tracking-wide">LIMS</span>
        </a>

        <div class="relative max-w-xl py-16">
            <span class="mb-5 inline-flex items-center gap-2 rounded-full border border-blue-300/20 bg-blue-400/10 px-3 py-1.5 text-xs font-medium text-blue-200">
                <span class="h-1.5 w-1.5 rounded-full bg-emerald-400"></span>
                HỆ THỐNG QUẢN LÝ KHO LAPTOP
            </span>
            <h1 class="text-4xl font-semibold leading-tight tracking-tight xl:text-5xl">Quản lý kho hàng<br><span class="text-blue-300">gọn gàng, chính xác.</span></h1>
            <p class="mt-5 max-w-md text-base leading-7 text-slate-300">Theo dõi sản phẩm, thiết bị theo số sê-ri và quy trình nhập xuất trên cùng một hệ thống.</p>

            <div class="mt-10 grid max-w-lg grid-cols-3 gap-3">
                <div class="rounded-2xl border border-white/10 bg-white/[0.06] p-4 backdrop-blur-sm">
                    <i class="fa-solid fa-boxes-stacked text-blue-300"></i>
                    <p class="mt-3 text-sm font-medium">Quản lý kho</p>
                </div>
                <div class="rounded-2xl border border-white/10 bg-white/[0.06] p-4 backdrop-blur-sm">
                    <i class="fa-solid fa-arrow-right-arrow-left text-blue-300"></i>
                    <p class="mt-3 text-sm font-medium">Nhập &amp; xuất</p>
                </div>
                <div class="rounded-2xl border border-white/10 bg-white/[0.06] p-4 backdrop-blur-sm">
                    <i class="fa-solid fa-clipboard-check text-blue-300"></i>
                    <p class="mt-3 text-sm font-medium">Kiểm kê</p>
                </div>
            </div>
        </div>

        <p class="relative text-xs text-slate-400">Laptop Inventory Management System <span class="mx-1 text-slate-600">·</span> LIMS</p>
    </section>

    <!-- Login form -->
    <section class="flex items-center justify-center px-5 py-10 sm:px-8 lg:px-12">
        <div class="w-full max-w-md">
            <div class="mb-8 flex items-center gap-3 lg:hidden">
                <span class="flex h-11 w-11 items-center justify-center rounded-xl bg-blue-600 text-lg text-white shadow-lg shadow-blue-600/20"><i class="fa-solid fa-laptop"></i></span>
                <div>
                    <p class="font-bold tracking-wide text-slate-900">LIMS</p>
                    <p class="text-xs text-slate-500">Quản lý kho laptop</p>
                </div>
            </div>

            <div class="rounded-3xl border border-slate-200 bg-white p-6 shadow-xl shadow-slate-200/70 sm:p-9">
                <div class="mb-7">
                    <p class="text-sm font-semibold uppercase tracking-[0.16em] text-blue-600">Chào mừng trở lại</p>
                    <h2 class="mt-2 text-2xl font-bold tracking-tight text-slate-900">Đăng nhập tài khoản</h2>
                    <p class="mt-2 text-sm leading-6 text-slate-500">Nhập thông tin tài khoản của bạn để tiếp tục.</p>
                </div>

                <c:if test="${not empty errorMessage}">
                    <div role="alert" class="mb-5 flex items-start gap-3 rounded-xl border border-rose-200 bg-rose-50 p-3.5 text-sm text-rose-700">
                        <i class="fa-solid fa-circle-exclamation mt-0.5"></i>
                        <span>${errorMessage}</span>
                    </div>
                </c:if>

                <c:if test="${param.status == 'logged_out'}">
                    <div role="status" class="mb-5 flex items-start gap-3 rounded-xl border border-emerald-200 bg-emerald-50 p-3.5 text-sm text-emerald-700">
                        <i class="fa-solid fa-circle-check mt-0.5"></i>
                        <span>Bạn đã đăng xuất an toàn khỏi hệ thống.</span>
                    </div>
                </c:if>

                <form action="${pageContext.request.contextPath}/login" method="POST" class="space-y-5">
                    <div>
                        <label for="username" class="mb-2 block text-sm font-semibold text-slate-700">Tên đăng nhập</label>
                        <div class="relative">
                            <i class="fa-regular fa-user pointer-events-none absolute left-4 top-1/2 -translate-y-1/2 text-slate-400"></i>
                            <input type="text" id="username" name="username" value="${username}" required autocomplete="username" autofocus
                                   placeholder="Nhập tên đăng nhập"
                                   class="w-full rounded-xl border border-slate-300 bg-white py-3 pl-11 pr-4 text-sm text-slate-900 outline-none transition placeholder:text-slate-400 hover:border-slate-400 focus:border-blue-500 focus:ring-4 focus:ring-blue-500/10" />
                        </div>
                    </div>

                    <div>
                        <div class="mb-2 flex items-center justify-between">
                            <label for="password" class="block text-sm font-semibold text-slate-700">Mật khẩu</label>
                        </div>
                        <div class="relative">
                            <i class="fa-solid fa-lock pointer-events-none absolute left-4 top-1/2 -translate-y-1/2 text-slate-400"></i>
                            <input type="password" id="password" name="password" required autocomplete="current-password"
                                   placeholder="Nhập mật khẩu"
                                   class="w-full rounded-xl border border-slate-300 bg-white py-3 pl-11 pr-12 text-sm text-slate-900 outline-none transition placeholder:text-slate-400 hover:border-slate-400 focus:border-blue-500 focus:ring-4 focus:ring-blue-500/10" />
                            <button type="button" id="togglePassword" aria-label="Hiện mật khẩu" aria-pressed="false" class="absolute inset-y-0 right-0 flex items-center px-4 text-slate-400 transition hover:text-slate-700">
                                <i class="fa-regular fa-eye" id="eyeIcon"></i>
                            </button>
                        </div>
                    </div>

                    <button type="submit" class="group flex w-full items-center justify-center gap-2 rounded-xl bg-blue-600 px-4 py-3.5 text-sm font-semibold text-white shadow-lg shadow-blue-600/20 transition hover:bg-blue-700 focus:outline-none focus:ring-4 focus:ring-blue-500/20">
                        Đăng nhập
                        <i class="fa-solid fa-arrow-right text-xs transition-transform group-hover:translate-x-1"></i>
                    </button>
                </form>

                <div class="my-7 flex items-center gap-3">
                    <span class="h-px flex-1 bg-slate-200"></span>
                    <span class="text-[11px] font-semibold uppercase tracking-[0.14em] text-slate-400">Tài khoản dùng thử</span>
                    <span class="h-px flex-1 bg-slate-200"></span>
                </div>
                <p class="mb-3 text-xs text-slate-500">Chọn một vai trò để điền nhanh. Mật khẩu: <span class="font-semibold text-slate-700">123456</span></p>
                <div class="grid grid-cols-2 gap-2">
                    <button type="button" onclick="fillAuth('admin')" class="role-button"><span class="role-icon"><i class="fa-solid fa-shield-halved"></i></span><span><strong>Admin</strong><small>admin</small></span></button>
                    <button type="button" onclick="fillAuth('manager')" class="role-button"><span class="role-icon"><i class="fa-solid fa-user-tie"></i></span><span><strong>Quản lý</strong><small>manager</small></span></button>
                    <button type="button" onclick="fillAuth('warehouse')" class="role-button"><span class="role-icon"><i class="fa-solid fa-warehouse"></i></span><span><strong>Thủ kho</strong><small>warehouse</small></span></button>
                    <button type="button" onclick="fillAuth('sales')" class="role-button"><span class="role-icon"><i class="fa-solid fa-cart-shopping"></i></span><span><strong>Bán hàng</strong><small>sales</small></span></button>
                    <button type="button" onclick="fillAuth('purchasing')" class="role-button col-span-2"><span class="role-icon"><i class="fa-solid fa-truck-fast"></i></span><span><strong>Mua hàng</strong><small>purchasing</small></span></button>
                </div>
            </div>
            <p class="mt-5 text-center text-xs text-slate-400">© <%= java.time.Year.now() %> LIMS · Laptop Inventory Management System</p>
        </div>
    </section>
</main>

<style>
    .role-button { display:flex; align-items:center; gap:.65rem; border:1px solid #e2e8f0; border-radius:.8rem; background:#fff; padding:.65rem .75rem; text-align:left; transition:all .15s ease; }
    .role-button:hover { border-color:#93c5fd; background:#eff6ff; }
    .role-button:focus-visible { outline:3px solid rgb(59 130 246 / .25); outline-offset:2px; }
    .role-icon { display:flex; width:2rem; height:2rem; flex:none; align-items:center; justify-content:center; border-radius:.65rem; background:#eff6ff; color:#2563eb; }
    .role-button strong,.role-button small { display:block; }
    .role-button strong { color:#334155; font-size:.75rem; font-weight:600; }
    .role-button small { margin-top:.1rem; color:#94a3b8; font-size:.65rem; }
</style>
<script>
    function fillAuth(username) {
        document.getElementById('username').value = username;
        document.getElementById('password').value = '123456';
        document.getElementById('password').focus();
    }

    const toggleBtn = document.getElementById('togglePassword');
    const pwdInput = document.getElementById('password');
    const eyeIcon = document.getElementById('eyeIcon');
    toggleBtn.addEventListener('click', () => {
        const showPassword = pwdInput.type === 'password';
        pwdInput.type = showPassword ? 'text' : 'password';
        eyeIcon.classList.toggle('fa-eye', !showPassword);
        eyeIcon.classList.toggle('fa-eye-slash', showPassword);
        toggleBtn.setAttribute('aria-pressed', String(showPassword));
        toggleBtn.setAttribute('aria-label', showPassword ? 'Ẩn mật khẩu' : 'Hiện mật khẩu');
    });
</script>
</body>
</html>
