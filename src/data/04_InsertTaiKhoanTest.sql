-- =====================================================
-- 📝 INSERT TÀI KHOẢN TEST ĐỂ ĐĂNG NHẬP
-- =====================================================
USE HTQLVT;
GO

-- Xóa tài khoản cũ nếu có (để tránh lỗi duplicate)
DELETE FROM TaiKhoan WHERE maNhanVien IN ('NV24030001', 'NV24030002');
GO

-- Insert tài khoản cho 2 nhân viên đầu tiên
-- Tài khoản 1: admin / admin123
INSERT INTO TaiKhoan (userName, passWord, maNhanVien)
VALUES (N'admin', N'admin123', 'NV24030001');

-- Tài khoản 2: nhanvien / 123456
INSERT INTO TaiKhoan (userName, passWord, maNhanVien)
VALUES (N'nhanvien', N'123456', 'NV24030002');

GO

-- Kiểm tra
SELECT 
    tk.userName AS [Tên đăng nhập],
    tk.passWord AS [Mật khẩu],
    tk.maNhanVien AS [Mã NV],
    nv.hoTen AS [Tên nhân viên],
    nv.chucVu AS [Chức vụ]
FROM TaiKhoan tk
JOIN NhanVien nv ON tk.maNhanVien = nv.maNhanVien;

PRINT '✅ Đã tạo 2 tài khoản test:';
PRINT '   1. Username: admin    | Password: admin123 | NV: NV24030001';
PRINT '   2. Username: nhanvien | Password: 123456   | NV: NV24030002';

