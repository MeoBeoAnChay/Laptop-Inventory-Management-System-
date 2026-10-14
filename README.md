# Laptop Inventory Management System (LIMS)

Production-grade Web Application for Managing Enterprise Laptop Inventory, Warehouse Transactions, and Serial-level Hardware Tracking.

---

## 1. Công nghệ & Kiến trúc Hệ thống

- **Ngôn ngữ & Nền tảng**: Java 17+, Jakarta EE / Servlet 5.0, JSP, JSTL
- **Kiến trúc**: Classic MVC (Model - View - Controller) / 3-Tier Web Architecture
  - `com.lims.model`: 21 Entity POJOs map 1-1 với 21 bảng Microsoft SQL Server
  - `com.lims.dao`: Tầng Data Access Object dùng `PreparedStatement` & Connection Pool
  - `com.lims.controller`: Jakarta Servlets xử lý HTTP GET/POST, Session, Redirect, Forward và JSON AJAX
  - `com.lims.filter`: Bộ lọc chuẩn (`EncodingFilter` UTF-8, `AuthFilter` Session, `RBACFilter` 5 quyền)
  - `com.lims.util`: `DBContext` (HikariCP / Fallback JDBC), `PasswordUtils` (BCrypt), `ValidationUtils`, `EmailUtils`
  - `webapp/WEB-INF/views`: JSP với JSTL tags, Tailwind CSS, FontAwesome 6, Vanilla JS / Fetch API
- **Hệ quản trị CSDL**: Microsoft SQL Server (21 Tables, Foreign Keys, Unique Serial Numbers, Indexing)

---

## 2. Cấu trúc Thư mục Dự án

```text
Laptop-Inventory-Management-System-/
├── pom.xml                                     # Cấu hình Maven, Jakarta Servlet 5.0, MSSQL, HikariCP, BCrypt
├── src/
│   ├── main/
│   │   ├── java/com/lims/
│   │   │   ├── controller/                     # Servlets điều hướng
│   │   │   │   ├── auth/                       # LoginServlet, LogoutServlet
│   │   │   │   ├── BaseServlet.java            # Lớp Servlet cơ sở (forward, redirect, json)
│   │   │   │   ├── DashboardServlet.java       # Thống kê tổng quan kho
│   │   │   │   ├── ProductServlet.java         # Quản lý danh mục laptop
│   │   │   │   ├── InventoryServlet.java       # Quản lý tồn kho theo Serial & Vị trí
│   │   │   │   ├── ImportRequestServlet.java   # Quản lý yêu cầu nhập kho
│   │   │   │   └── ExportRequestServlet.java   # Quản lý yêu cầu xuất kho
│   │   │   ├── dao/                            # Data Access Objects (SQL PreparedStatement)
│   │   │   │   ├── UserDAO.java
│   │   │   │   ├── RoleDAO.java
│   │   │   │   ├── AuditLogDAO.java
│   │   │   │   ├── BrandDAO.java
│   │   │   │   ├── ProductLineDAO.java
│   │   │   │   ├── ProductDAO.java
│   │   │   │   ├── SupplierDAO.java
│   │   │   │   ├── CustomerDAO.java
│   │   │   │   ├── InventoryDAO.java
│   │   │   │   ├── ImportRequestDAO.java
│   │   │   │   └── ExportRequestDAO.java
│   │   │   ├── filter/                         # Bộ lọc bảo mật & phân quyền
│   │   │   │   ├── EncodingFilter.java         # Mã hóa UTF-8 toàn hệ thống
│   │   │   │   ├── AuthFilter.java             # Kiểm tra Session đăng nhập trên /app/*
│   │   │   │   └── RBACFilter.java             # Phân quyền 5 vai trò hệ thống
│   │   │   ├── model/                          # 21 Entity Beans POJOs
│   │   │   │   ├── Role.java, User.java, UserRole.java, AuditLog.java
│   │   │   │   ├── Brand.java, ProductLine.java, Product.java
│   │   │   │   ├── Supplier.java, Customer.java
│   │   │   │   ├── ImportRequest.java, ImportRequestDetail.java
│   │   │   │   ├── Handover.java, ImportReceipt.java, ImportReceiptDetail.java
│   │   │   │   ├── Inventory.java
│   │   │   │   ├── ExportRequest.java, ExportRequestDetail.java
│   │   │   │   ├── ExportReceipt.java, ExportReceiptDetail.java
│   │   │   │   └── InventoryCheck.java, InventoryCheckDetail.java
│   │   │   └── util/                           # Các tiện ích dùng chung
│   │   │       ├── DBContext.java              # Quản lý kết nối SQL Server & HikariCP
│   │   │       ├── PasswordUtils.java          # Băm mật khẩu BCrypt an toàn
│   │   │       ├── ValidationUtils.java        # Kiểm tra tính hợp lệ dữ liệu
│   │   │       └── EmailUtils.java             # Gửi email SMTP thông báo
│   │   ├── resources/
│   │   │   ├── db.properties                   # Cấu hình tài khoản & cổng kết nối MSSQL
│   │   │   └── sql/
│   │   │       ├── schema.sql                  # Script khởi tạo 21 bảng CSDL SQL Server
│   │   │       └── seed.sql                    # Dữ liệu mẫu (Tài khoản, quyền, laptop, nhà cung cấp)
│   │   └── webapp/
│   │       ├── WEB-INF/
│   │       │   ├── web.xml                     # Cấu hình Web Application Descriptor
│   │       │   └── views/                      # Các trang JSP bảo mật
│   │       │       ├── common/                 # head.jsp, navbar.jsp, sidebar.jsp, footer.jsp
│   │       │       ├── auth/                   # login.jsp
│   │       │       ├── dashboard/              # index.jsp
│   │       │       ├── products/               # list.jsp
│   │       │       ├── inventory/              # list.jsp
│   │       │       ├── imports/                # list.jsp
│   │       │       ├── exports/                # list.jsp
│   │       │       └── errors/                 # 403.jsp, 404.jsp
│   │       └── index.jsp                       # Điều hướng trang chủ
```

---

## 3. Danh sách 21 Bảng CSDL (Microsoft SQL Server)

1. `ROLES` - Lưu 5 vai trò hệ thống
2. `USERS` - Thông tin tài khoản, mật khẩu băm BCrypt
3. `USER_ROLES` - Bảng trung gian gán vai trò cho người dùng
4. `AUDIT_LOGS` - Nhật ký kiểm toán thao tác hệ thống
5. `BRANDS` - Thương hiệu laptop (Chỉ Dell và Lenovo)
6. `PRODUCT_LINES` - Dòng máy (Dell: Inspiron, Vostro, Latitude, XPS, G-Series, Alienware, Precision; Lenovo: ThinkPad, ThinkBook, IdeaPad, Yoga, Legion, LOQ, Legion Go)
7. `PRODUCTS` - Mẫu mã laptop (CPU, RAM, SSD, GPU, Màn hình, Giá)
8. `SUPPLIERS` - Nhà phân phối / cung cấp laptop
9. `CUSTOMERS` - Khách hàng bán lẻ (CellphoneS, Thế Giới Di Động, Điện Máy Xanh, FPT Shop)
10. `IMPORT_REQUESTS` - Yêu cầu nhập kho
11. `IMPORT_REQUEST_DETAILS` - Chi tiết sản phẩm nhập
12. `HANDOVERS` - Biên bản bàn giao từ đơn vị vận chuyển
13. `IMPORT_RECEIPTS` - Phiếu nhập kho chính thức (UserID người thực hiện)
14. `IMPORT_RECEIPT_DETAILS` - Chi tiết nhập kho theo từng Serial & Tình trạng máy
15. `INVENTORY` - Tồn kho chi tiết theo Serial Number duy nhất & Vị trí kệ kho
16. `EXPORT_REQUESTS` - Yêu cầu xuất kho bán hàng
17. `EXPORT_REQUEST_DETAILS` - Chi tiết số lượng mẫu laptop cần xuất
18. `EXPORT_RECEIPTS` - Phiếu xuất kho chính thức (UserID người thực hiện)
19. `EXPORT_RECEIPT_DETAILS` - Chi tiết máy xuất kho (Serial & Inventory ID)
20. `INVENTORY_CHECKS` - Phiếu kiểm kê kho định kỳ
21. `INVENTORY_CHECK_DETAILS` - Chi tiết đối soát trạng thái hệ thống vs thực tế

---

## 4. Tài khoản Kiểm thử Mặc định

Tất cả các tài khoản mặc định có mật khẩu là: `admin123`

| Username | Họ tên | Vai trò (Role) | Chức năng chính |
| :--- | :--- | :--- | :--- |
| `admin` | Bùi Tuấn Phong | Admin | Toàn quyền quản trị tài khoản, phân quyền, kiểm toán |
| `manager` | Nguyễn Văn Mạnh | Manager | Phê duyệt đơn nhập/xuất kho, xem báo cáo tồn kho |
| `warehouse` | Phạm Huy Thái | Warehouse Staff | Cập nhật vị trí kệ kho, kiểm kê, nhập/xuất kho thực tế |
| `sales` | Bùi Tuấn Hiệp | Sales Staff | Tạo yêu cầu xuất kho bán hàng giao khách hàng |
| `purchasing` | Nguyễn Tuấn Kiệt | Purchasing Staff | Tạo yêu cầu đặt mua laptop từ nhà phân phối |

---

## 5. Hướng dẫn Cài đặt & Chạy Dự án

1. **Khởi tạo CSDL**:
   - Mở SQL Server Management Studio (SSMS).
   - Chạy script [`src/main/resources/sql/schema.sql`](file:///src/main/resources/sql/schema.sql) để tạo cơ sở dữ liệu `LIMS_DB` và 21 bảng.
   - Chạy script [`src/main/resources/sql/seed.sql`](file:///src/main/resources/sql/seed.sql) để chèn 5 vai trò, tài khoản mẫu và các mẫu laptop.
   - Chỉnh sửa thông tin kết nối (user/pass) trong file [`src/main/resources/db.properties`](file:///src/main/resources/db.properties) nếu cần.

2. **Chạy ứng dụng**:
   - Mở dự án trong Apache NetBeans / IntelliJ IDEA / Eclipse hoặc VS Code.
   - Cấu hình máy chủ Apache Tomcat (phiên bản 10.0+ hỗ trợ Jakarta EE).
   - Truy cập: `http://localhost:8080/lims/` (hoặc `http://localhost:8080/`).