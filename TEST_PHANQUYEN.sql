-- ========================================
-- 🔐 TEST PHÂN QUYỀN
-- ========================================

USE HTQLVT;
GO

-- 1️⃣ KIỂM TRA CÁC TÀI KHOẢN HIỆN CÓ
PRINT N'📋 DANH SÁCH TÀI KHOẢN:';
SELECT 
    tk.userName AS [Tài khoản],
    tk.passWord AS [Mật khẩu],
    nv.hoTen AS [Họ tên],
    nv.chucVu AS [Chức vụ (0=QL, 1=NV)],
    CASE 
        WHEN nv.chucVu = 0 THEN N'Quản lý'
        WHEN nv.chucVu = 1 THEN N'Nhân viên'
        ELSE N'Không xác định'
    END AS [Tên chức vụ]
FROM TaiKhoan tk
    JOIN NhanVien nv ON tk.maNhanVien = nv.maNhanVien
WHERE nv.trangThai = 1  -- Chỉ lấy nhân viên đang làm
ORDER BY nv.chucVu, nv.hoTen;

-- 2️⃣ TẠO TÀI KHOẢN TEST (NẾU CHƯA CÓ)
PRINT N'';
PRINT N'🔧 TẠO TÀI KHOẢN TEST...';

-- Xóa tài khoản test cũ (nếu có)
DELETE FROM TaiKhoan WHERE userName IN ('admin_test', 'nhanvien_test');
DELETE FROM NhanVien WHERE maNhanVien IN ('NV_TEST_QL', 'NV_TEST_NV');

-- Tạo nhân viên QUẢN LÝ
INSERT INTO NhanVien (maNhanVien, hoTen, chucVu, trangThai, CCCD, SDT)
VALUES ('NV_TEST_QL', N'Test Quản Lý', 0, 1, '001111111111', '0911111111');

-- Tạo nhân viên NHÂN VIÊN
INSERT INTO NhanVien (maNhanVien, hoTen, chucVu, trangThai, CCCD, SDT)
VALUES ('NV_TEST_NV', N'Test Nhân Viên', 1, 1, '002222222222', '0922222222');

-- Tạo tài khoản QUẢN LÝ
INSERT INTO TaiKhoan (userName, passWord, maNhanVien)
VALUES ('admin_test', '123', 'NV_TEST_QL');

-- Tạo tài khoản NHÂN VIÊN
INSERT INTO TaiKhoan (userName, passWord, maNhanVien)
VALUES ('nhanvien_test', '123', 'NV_TEST_NV');

PRINT N'✅ Đã tạo 2 tài khoản test:';
PRINT N'   👑 admin_test / 123 (Quản lý - thấy tất cả menu)';
PRINT N'   👤 nhanvien_test / 123 (Nhân viên - không thấy Quản lí Nhân Viên và Khuyến Mãi)';

-- 3️⃣ KIỂM TRA LẠI
PRINT N'';
PRINT N'📋 DANH SÁCH TÀI KHOẢN SAU KHI TẠO:';
SELECT 
    tk.userName AS [Tài khoản],
    tk.passWord AS [Mật khẩu],
    nv.hoTen AS [Họ tên],
    nv.chucVu AS [Chức vụ],
    CASE 
        WHEN nv.chucVu = 0 THEN N'👑 Quản lý (thấy tất cả menu)'
        WHEN nv.chucVu = 1 THEN N'👤 Nhân viên (không thấy 2 menu)'
        ELSE N'Không xác định'
    END AS [Quyền]
FROM TaiKhoan tk
    JOIN NhanVien nv ON tk.maNhanVien = nv.maNhanVien
WHERE nv.trangThai = 1
ORDER BY nv.chucVu, nv.hoTen;

-- 4️⃣ HƯỚNG DẪN TEST
PRINT N'';
PRINT N'========================================';
PRINT N'🧪 CÁCH TEST PHÂN QUYỀN:';
PRINT N'========================================';
PRINT N'';
PRINT N'1️⃣ Chạy lại ứng dụng Java';
PRINT N'';
PRINT N'2️⃣ Đăng nhập với tài khoản QUẢN LÝ:';
PRINT N'   👉 Username: admin_test';
PRINT N'   👉 Password: 123';
PRINT N'   ✅ Kỳ vọng: Thấy TẤT CẢ menu (Dashboard, Vé, Khách hàng, Nhân viên, Khuyến mãi, Thống kê, Trợ giúp)';
PRINT N'';
PRINT N'3️⃣ Đăng xuất, đăng nhập lại với tài khoản NHÂN VIÊN:';
PRINT N'   👉 Username: nhanvien_test';
PRINT N'   👉 Password: 123';
PRINT N'   ❌ Kỳ vọng: KHÔNG thấy 2 menu:';
PRINT N'      - Quản lí Nhân Viên';
PRINT N'      - Khuyến Mãi';
PRINT N'   ✅ Chỉ thấy: Dashboard, Vé, Khách hàng, Thống kê, Trợ giúp';
PRINT N'';
PRINT N'4️⃣ Kiểm tra Console (trong IDE):';
PRINT N'   👉 Sẽ thấy log: "👤 Đăng nhập: ... | Chức vụ: 0 (Quản lý)" hoặc "Chức vụ: 1 (Nhân viên)"';
PRINT N'';
PRINT N'========================================';


