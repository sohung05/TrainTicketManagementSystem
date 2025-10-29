# 🚨 FIX DATABASE NGAY - CỰC KỲ QUAN TRỌNG!

## ⚠️ **VẤN ĐỀ**

Database hiện tại của bạn **KHÔNG KHỚP** với code. Phải tạo lại database với cấu trúc đúng!

**Các lỗi:**
- ❌ Bảng `LichTrinh`: Có `maGa` → Cần `maGaDi`, `maGaDen`
- ❌ Bảng `NhanVien`: Có `loaiNV` → Cần `chucVu` 
- ❌ Bảng `TaiKhoan`: Có `tenTaiKhoan`, `matKhau` → Cần `userName`, `passWord`

---

## 🔥 **CÁCH FIX (3 BƯỚC)**

### **Bước 1: Tạo lại database** ⭐

Mở **SQL Server Management Studio (SSMS)** và chạy file:

```
src/data/00_CreateDatabase_Fixed.sql
```

**Quan trọng:**
- File này sẽ **XÓA** database HTQLVT cũ
- Tạo lại database HTQLVT với cấu trúc **ĐÃ SỬA**
- Đợi thông báo: `✅ Đã tạo database HTQLVT với cấu trúc đã sửa!`

---

### **Bước 2: Insert dữ liệu cơ bản**

Tiếp tục chạy file:

```
src/data/InsertFullData_MultiRoutes.sql
```

**Lưu ý**: File này đã đúng, **KHÔNG CẦN SỬA** gì!

Đợi thông báo: `🎉🎉🎉 HOÀN THÀNH! 🎉🎉🎉`

---

### **Bước 3: Insert dữ liệu mẫu (hóa đơn, vé)**

Chạy tiếp file:

```
src/data/02_InsertSampleTransactions.sql
```

Đợi thông báo: `🎉 HOÀN THÀNH!`

---

## ✅ **SAU KHI XONG 3 BƯỚC**

1. **Build lại project** trong IntelliJ
2. **Run `demo/Main.java`**
3. **Test các chức năng**

---

## 🎯 **Kết quả sau khi fix:**

✅ Dashboard sẽ hiển thị được dữ liệu

✅ Thống kê sẽ có biểu đồ

✅ Nhân viên, Tài khoản sẽ load được

✅ Tất cả chức năng hoạt động bình thường

---

## ❓ **Nếu gặp lỗi**

### Lỗi: "Database 'HTQLVT' is currently in use"
➡️ Đóng tất cả các cửa sổ query và connection đến HTQLVT, chạy lại

### Lỗi: "Cannot drop database 'HTQLVT'"
➡️ Chạy lệnh này trước:
```sql
USE master;
GO
ALTER DATABASE HTQLVT SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
GO
```

Sau đó chạy lại file `00_CreateDatabase_Fixed.sql`

---

## 📝 **TÓM TẮT**

```
1. Chạy: 00_CreateDatabase_Fixed.sql      → Tạo lại database
2. Chạy: InsertFullData_MultiRoutes.sql    → Insert dữ liệu cơ bản
3. Chạy: 02_InsertSampleTransactions.sql   → Insert hóa đơn mẫu
4. Build project
5. Run demo/Main.java
```

**LÀM ĐÚNG THỨ TỰ NHÉ!** 🎯

---

**Chúc bạn fix thành công! 🚀**

