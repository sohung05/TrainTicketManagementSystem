# 🗄️ HƯỚNG DẪN SETUP DATABASE

## ⚠️ **QUAN TRỌNG: ĐỌC KỸ TRƯỚC KHI CHẠY**

Project này đã được **SỬA LỖI** hoàn toàn. Database cần được tạo lại với cấu trúc đã fix.

---

## 📋 **Các bước setup database**

### **Bước 1: Mở SQL Server Management Studio (SSMS)**

Đảm bảo SQL Server đang chạy trên máy bạn.

### **Bước 2: Cấu hình kết nối trong code**

Mở file `src/connectDB/connectDB.java` và chỉnh sửa thông tin kết nối:

```java
private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=BTL;encrypt=false;trustServerCertificate=true;";
private static final String USER = "sa";           // ← Đổi username nếu cần
private static final String PASSWORD = "123456789"; // ← Đổi password nếu cần
```

### **Bước 3: Tạo database (QUAN TRỌNG!)**

**CHÚ Ý**: Phải chạy file mới đã được fix!

#### **Option A: Chạy file đã fix (KHUYẾN NGHỊ)**

1. Mở file: `src/data/00_CreateDatabase_Fixed.sql`
2. Execute toàn bộ file trong SSMS
3. Chờ thông báo: `✅ Đã tạo database BTL với cấu trúc đã sửa!`

#### **Option B: File cũ (KHÔNG KHUYẾN NGHỊ)**

⚠️ File `SQLTaoDULieu.sql` có lỗi cấu trúc, không khớp với code!

### **Bước 4: Insert dữ liệu**

1. Mở file: `src/data/InsertFullData_MultiRoutes.sql`
2. **SỬA dòng 6**: Đổi `USE HTQLVT;` thành `USE BTL;`
3. Execute toàn bộ file
4. Chờ thông báo: `🎉🎉🎉 HOÀN THÀNH! 🎉🎉🎉`

---

## 🔍 **Kiểm tra dữ liệu đã insert**

Chạy các query sau trong SSMS để kiểm tra:

```sql
USE BTL;
GO

-- Kiểm tra số lượng bản ghi
SELECT 'LoaiTau' AS [Table], COUNT(*) AS [Count] FROM LoaiTau
UNION ALL
SELECT 'ChuyenTau', COUNT(*) FROM ChuyenTau
UNION ALL
SELECT 'Ga', COUNT(*) FROM Ga
UNION ALL
SELECT 'Tuyen', COUNT(*) FROM Tuyen
UNION ALL
SELECT 'Toa', COUNT(*) FROM Toa
UNION ALL
SELECT 'ChoNgoi', COUNT(*) FROM ChoNgoi
UNION ALL
SELECT 'LichTrinh', COUNT(*) FROM LichTrinh
UNION ALL
SELECT 'NhanVien', COUNT(*) FROM NhanVien
UNION ALL
SELECT 'TaiKhoan', COUNT(*) FROM TaiKhoan
UNION ALL
SELECT 'KhachHang', COUNT(*) FROM KhachHang;
```

### **Kết quả mong đợi:**

| Table       | Count   |
|-------------|---------|
| LoaiTau     | 3       |
| ChuyenTau   | 30      |
| Ga          | 8       |
| Tuyen       | 10      |
| Toa         | 300     |
| ChoNgoi     | ~15,000 |
| LichTrinh   | 300     |
| NhanVien    | 3       |
| TaiKhoan    | 3       |
| KhachHang   | 3       |

---

## 👤 **Tài khoản đăng nhập**

Sau khi insert dữ liệu, bạn có thể đăng nhập với các tài khoản sau:

| Username   | Password | Vai trò      |
|------------|----------|--------------|
| admin      | 123456   | Quản lý      |
| nhanvien1  | 123456   | Nhân viên    |
| nhanvien2  | 123456   | Nhân viên    |

---

## 🚀 **Chạy ứng dụng**

Sau khi setup xong database:

1. Build project: `Ctrl + F9` (NetBeans) hoặc `Ctrl + F9` (IntelliJ)
2. Run class `App.java`
3. Đăng nhập bằng một trong các tài khoản trên

---

## 🔧 **Các thay đổi đã fix so với file cũ**

### **1. Bảng `LichTrinh`**
- ❌ **Trước**: Chỉ có cột `maGa`
- ✅ **Sau**: Có `maGaDi` và `maGaDen`

### **2. Bảng `HoaDon`**
- ❌ **Trước**: Thiếu cột `tongTien`
- ✅ **Sau**: Đã thêm `tongTien DECIMAL(18,2)`

### **3. Bảng `NhanVien`**
- ❌ **Trước**: Cột `loaiNV NVARCHAR(50)`
- ✅ **Sau**: Cột `chucVu INT`

### **4. Bảng `TaiKhoan`**
- ❌ **Trước**: `tenTaiKhoan`, `matKhau`
- ✅ **Sau**: `userName`, `passWord`

---

## ❓ **Gặp lỗi?**

### **Lỗi: "Cannot open database BTL"**
➡️ Chưa chạy file `00_CreateDatabase_Fixed.sql`

### **Lỗi: "Invalid column name 'maGaDi'"**
➡️ Đang dùng file cũ `SQLTaoDULieu.sql`. Phải dùng `00_CreateDatabase_Fixed.sql`

### **Lỗi: "Invalid column name 'tongTien'"**
➡️ Bảng HoaDon thiếu cột. Chạy lại `00_CreateDatabase_Fixed.sql`

### **Lỗi: "Login failed for user 'sa'"**
➡️ Kiểm tra lại username/password trong `connectDB.java`

### **Lỗi: Không kết nối được SQL Server**
➡️ Kiểm tra:
- SQL Server đã chạy chưa?
- TCP/IP đã enable chưa? (SQL Server Configuration Manager)
- Port 1433 có mở không?

---

## 📝 **Tóm tắt**

✅ Chạy `00_CreateDatabase_Fixed.sql` để tạo database với cấu trúc đã fix

✅ Sửa dòng 6 trong `InsertFullData_MultiRoutes.sql`: `USE HTQLVT;` → `USE BTL;`

✅ Chạy `InsertFullData_MultiRoutes.sql` để insert dữ liệu

✅ Đăng nhập: `admin` / `123456`

---

**Chúc bạn setup thành công! 🎉**






