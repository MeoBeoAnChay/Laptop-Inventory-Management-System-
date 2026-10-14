<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<footer class="bg-white border-t border-slate-200 py-4 px-6 text-center text-xs text-slate-500">
    <div class="flex flex-col sm:flex-row items-center justify-between">
        <p>&copy; 2026 Laptop Inventory Management System (LIMS). All rights reserved.</p>
        <p class="mt-2 sm:mt-0 font-medium text-slate-600">Built with Jakarta EE Servlets, JSP & SQL Server</p>
    </div>
</footer>

<script>
    // Responsive sidebar toggle for mobile
    const sidebarToggle = document.getElementById('sidebarToggle');
    const sidebar = document.getElementById('sidebar');
    if (sidebarToggle && sidebar) {
        sidebarToggle.addEventListener('click', () => {
            sidebar.classList.toggle('hidden');
        });
    }
</script>

