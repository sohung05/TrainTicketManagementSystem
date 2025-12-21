# 🧪 HƯỚNG DẪN TEST DASHBOARD - BẢN TÀI VIỆT

## 🔧 LỖI ĐÃ FIX:

### ❌ Lỗi: `Invalid column name 'maChuyenTau'`
**Nguyên nhân:** Bảng `LichTrinh` không có cột `maChuyenTau`, mà có cột `soHieuTau`

**Đã sửa:** 
- ✅ JOIN `lt.soHieuTau = ct.soHieuTau` (thay vì `lt.maChuyenTau = ct.maChuyenTau`)
- ✅ Sửa cả 2 query: Query chính + Query fallback

## ✅ ĐÃ SỬA:

### 1. SQL Query mới trong `Dashboard_DAO.java`:
- ✅ Sửa lại logic tính số ghế trống
- ✅ Thêm phương án dự phòng (fallback) nếu SQL phức tạp lỗi
- ✅ Thêm debug logs để kiểm tra dữ liệu

### 2. Filter ở bảng "Số chỗ trống theo tuyến":
- ✅ Chỉ filter cho bảng này (không ảnh hưởng charts khác)
- ✅ Đặt ngay trên đầu bảng
- ✅ 3 tùy chọn: Hôm nay / Tuần này (mặc định) / Tháng này
- ✅ JDateChooser: Chọn ngày cụ thể
- ✅ Nút "Áp dụng": Reload bảng

## 📝 CÁCH TEST:

### Bước 1: Rebuild Project
1. Mở project trong IDE (NetBeans/IntelliJ/Eclipse)
2. **Clean and Build** (hoặc **Rebuild Project**)
3. Chờ build hoàn tất

### Bước 2: Chạy ứng dụng
1. Run main class (có thể là `Main.java` hoặc `App.java`)
2. Đăng nhập vào hệ thống

### Bước 3: Vào Dashboard
1. Click vào menu **Dashboard**
2. **XEM CONSOLE** (terminal/output) để thấy debug logs:

```
📊 Số lịch trình từ hôm nay: 150
📊 Tổng số ghế: 12000
📊 Số vé đã bán (trangThai=1): 3500
🔍 SQL Số chỗ trống - Ngày lọc: 2025-12-19
   📊 Hà Nội - Hồ Chí Minh: 1200 ghế trống
   📊 Hà Nội - Đà Nẵng: 800 ghế trống
   ...
```

### Bước 4: Test Filter
1. Tìm bảng **"Số chỗ trống theo tuyến"** ở bên phải Dashboard
2. Thấy filter ở ngay trên đầu bảng
3. Test các tùy chọn:
   - Chọn **"Hôm nay"** → Bấm **"Áp dụng"** → Xem console log và bảng cập nhật
   - Chọn **"Tuần này"** → Bấm **"Áp dụng"**
   - Chọn **"Tháng này"** → Bấm **"Áp dụng"**
   - Chọn ngày cụ thể trong **JDateChooser** → Bấm **"Áp dụng"**

### Bước 5: Kiểm tra kết quả
- ✅ Bảng hiển thị dữ liệu (tên tuyến + số ghế trống)
- ✅ Số liệu thay đổi khi chọn filter khác nhau
- ✅ Console log hiển thị SQL query và kết quả

## 🔍 NẾU VẪN KHÔNG CÓ DỮ LIỆU:

### Nguyên nhân 1: Không có lịch trình trong tương lai
**Giải pháp:** Chạy file `02_ResetAndInsertData.sql` để insert dữ liệu mẫu

### Nguyên nhân 2: SQL query không đúng với cấu trúc DB
**Console sẽ hiển thị:**
```
⚠️ KHÔNG CÓ DỮ LIỆU! Thử phương án dự phòng...
🔄 Dùng SQL đơn giản để tính số chỗ trống...
```

**Giải pháp:** 
1. Copy console log
2. Gửi cho tôi để debug
3. Hoặc kiểm tra:
   - Bảng `LichTrinh` có dữ liệu không?
   - `gioKhoiHanh >= GETDATE()` có lịch trình nào không?
   - Foreign key giữa `LichTrinh` - `ChuyenTau` - `Toa` - `ChoNgoi` đúng không?

### Nguyên nhân 3: Thiếu dependencies
**Lỗi:** `JDateChooser cannot be resolved` hoặc `JFreeChart cannot be resolved`

**Giải pháp:** 
1. Kiểm tra thư mục `lib/` có các file `.jar`:
   - `jcalendar-*.jar` (cho JDateChooser)
   - `jfreechart-*.jar` (cho JFreeChart)
   - `jcommon-*.jar` (cho JFreeChart)
2. Nếu thiếu, add libraries vào project:
   - **NetBeans:** Right-click project → Properties → Libraries → Add JAR/Folder
   - **IntelliJ:** File → Project Structure → Libraries → +
   - **Eclipse:** Right-click project → Build Path → Add External Archives

## 📊 SQL QUERY ĐÃ SỬA:

### Query chính (có subquery):
```sql
SELECT 
    g1.tenGa + ' - ' + g2.tenGa AS tuyen,
    SUM(sub.tongGhe) - SUM(sub.gheDaBan) AS soChoTrong
FROM (
    SELECT 
        lt.maLichTrinh,
        lt.maGaDi,
        lt.maGaDen,
        COUNT(DISTINCT c.maChoNgoi) AS tongGhe,
        COUNT(DISTINCT CASE WHEN v.trangThai = 1 THEN v.maVe END) AS gheDaBan
    FROM LichTrinh lt
        JOIN ChuyenTau ct ON lt.soHieuTau = ct.soHieuTau -- ⚠️ Dùng soHieuTau!
        JOIN Toa t ON ct.maTau = t.maTau
        JOIN ChoNgoi c ON t.maToa = c.maToa
        LEFT JOIN Ve v ON v.maChoNgoi = c.maChoNgoi 
                       AND v.maLichTrinh = lt.maLichTrinh
    WHERE lt.gioKhoiHanh >= ?
    GROUP BY lt.maLichTrinh, lt.maGaDi, lt.maGaDen
) AS sub
    JOIN Ga g1 ON sub.maGaDi = g1.maGa
    JOIN Ga g2 ON sub.maGaDen = g2.maGa
GROUP BY g1.tenGa, g2.tenGa
ORDER BY soChoTrong DESC
```

### Query dự phòng (đơn giản hơn):
```sql
SELECT 
    g1.tenGa + ' - ' + g2.tenGa AS tuyen,
    COUNT(DISTINCT lt.maLichTrinh) AS soChuyenTau,
    SUM(CASE WHEN v.maVe IS NULL THEN 1 ELSE 0 END) AS soChoTrong
FROM LichTrinh lt
    JOIN Ga g1 ON lt.maGaDi = g1.maGa
    JOIN Ga g2 ON lt.maGaDen = g2.maGa
    JOIN ChuyenTau ct ON lt.soHieuTau = ct.soHieuTau -- ⚠️ Dùng soHieuTau!
    JOIN Toa t ON ct.maTau = t.maTau
    JOIN ChoNgoi c ON t.maToa = c.maToa
    LEFT JOIN Ve v ON v.maChoNgoi = c.maChoNgoi 
                   AND v.maLichTrinh = lt.maLichTrinh 
                   AND v.trangThai = 1
WHERE lt.gioKhoiHanh >= ?
GROUP BY g1.tenGa, g2.tenGa
HAVING COUNT(DISTINCT lt.maLichTrinh) > 0
ORDER BY soChoTrong DESC
```

## 🆘 GỬI CHO TÔI NẾU LỖI:
1. **Console log** đầy đủ (từ khi vào Dashboard)
2. Kết quả của query sau trong SSMS:
```sql
-- Kiểm tra dữ liệu
SELECT COUNT(*) FROM LichTrinh WHERE gioKhoiHanh >= GETDATE();
SELECT COUNT(*) FROM ChoNgoi;
SELECT COUNT(*) FROM Ve WHERE trangThai = 1;

-- Xem 5 lịch trình gần nhất
SELECT TOP 5 lt.*, g1.tenGa AS gaDi, g2.tenGa AS gaDen 
FROM LichTrinh lt
JOIN Ga g1 ON lt.maGaDi = g1.maGa
JOIN Ga g2 ON lt.maGaDen = g2.maGa
WHERE lt.gioKhoiHanh >= GETDATE()
ORDER BY lt.gioKhoiHanh;
```

