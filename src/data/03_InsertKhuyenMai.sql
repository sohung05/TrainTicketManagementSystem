-- =============================================
-- Script: Insert Khuyến Mãi (Promotion)
-- Mô tả: Thêm các khuyến mãi theo đối tượng và theo hóa đơn
-- Ngày tạo: 2025-10-29
-- =============================================

USE HTQLVT;
GO

-- Xóa dữ liệu cũ (nếu có)
DELETE FROM ChiTietKhuyenMai;
DELETE FROM KhuyenMai;
GO

PRINT N'🎉 Bắt đầu thêm dữ liệu Khuyến Mãi...';
GO

-- =============================================
-- PHẦN 1: KHUYẾN MÃI THEO ĐỐI TƯỢNG (KMKH)
-- =============================================

PRINT N'📌 Thêm Khuyến Mãi Đối Tượng (KMKH)...';
GO

-- 1. Trẻ em dưới 6 tuổi: Miễn vé (100% - trong thực tế không tạo vé riêng)
-- (Không cần insert vì trẻ em dưới 6 tuổi không cần vé riêng)

-- 2. Trẻ em từ 6 đến dưới 10 tuổi: Giảm 25%
INSERT INTO KhuyenMai (maKhuyenMai, tenKhuyenMai, loaiKhuyenMai, thoiGianBatDau, thoiGianKetThuc, trangThai)
VALUES (
    'KM2910202501', 
    N'Giảm 25% cho Trẻ em (6-10 tuổi)', 
    'KMKH', 
    '2024-01-01', 
    '2099-12-31', 
    1
);
-- Thêm chi tiết khuyến mãi vào bảng ChiTietKhuyenMai
INSERT INTO ChiTietKhuyenMai (maKhuyenMai, maHoaDon, dieuKien, chietKhau)
VALUES ('KM2910202501', NULL, N'TreEm', 0.25);
PRINT N'✅ Đã thêm: Giảm 25% cho Trẻ em (6-10 tuổi)';

-- 3. Người cao tuổi từ 60 tuổi trở lên: Giảm 15%
INSERT INTO KhuyenMai (maKhuyenMai, tenKhuyenMai, loaiKhuyenMai, thoiGianBatDau, thoiGianKetThuc, trangThai)
VALUES (
    'KM2910202502', 
    N'Giảm 15% cho Người cao tuổi (≥60 tuổi)', 
    'KMKH', 
    '2024-01-01', 
    '2099-12-31', 
    1
);
-- Thêm chi tiết khuyến mãi vào bảng ChiTietKhuyenMai
INSERT INTO ChiTietKhuyenMai (maKhuyenMai, maHoaDon, dieuKien, chietKhau)
VALUES ('KM2910202502', NULL, N'NguoiCaoTuoi', 0.15);
PRINT N'✅ Đã thêm: Giảm 15% cho Người cao tuổi (≥60 tuổi)';

-- 4. Sinh viên: Giảm 10%
INSERT INTO KhuyenMai (maKhuyenMai, tenKhuyenMai, loaiKhuyenMai, thoiGianBatDau, thoiGianKetThuc, trangThai)
VALUES (
    'KM2910202503', 
    N'Giảm 10% cho Sinh viên', 
    'KMKH', 
    '2024-01-01', 
    '2099-12-31', 
    1
);
-- Thêm chi tiết khuyến mãi vào bảng ChiTietKhuyenMai
INSERT INTO ChiTietKhuyenMai (maKhuyenMai, maHoaDon, dieuKien, chietKhau)
VALUES ('KM2910202503', NULL, N'SinhVien', 0.10);
PRINT N'✅ Đã thêm: Giảm 10% cho Sinh viên';

-- =============================================
-- PHẦN 2: KHUYẾN MÃI THEO HÓA ĐƠN (KMHD)
-- =============================================

PRINT N'📌 Thêm Khuyến Mãi Hóa Đơn (KMHD)...';
GO

-- 1. Đặt từ 11-40 vé: Giảm 9%
INSERT INTO KhuyenMai (maKhuyenMai, tenKhuyenMai, loaiKhuyenMai, thoiGianBatDau, thoiGianKetThuc, trangThai)
VALUES (
    'KM2910202504', 
    N'Giảm 9% khi đặt 11-40 vé', 
    'KMHD', 
    '2024-01-01', 
    '2099-12-31', 
    1
);
-- Thêm chi tiết khuyến mãi vào bảng ChiTietKhuyenMai
INSERT INTO ChiTietKhuyenMai (maKhuyenMai, maHoaDon, dieuKien, chietKhau)
VALUES ('KM2910202504', NULL, N'11-40 vé', 0.09);
PRINT N'✅ Đã thêm: Giảm 9% khi đặt 11-40 vé';

-- 2. Đặt từ 42-70 vé: Giảm 11%
INSERT INTO KhuyenMai (maKhuyenMai, tenKhuyenMai, loaiKhuyenMai, thoiGianBatDau, thoiGianKetThuc, trangThai)
VALUES (
    'KM2910202505', 
    N'Giảm 11% khi đặt 42-70 vé', 
    'KMHD', 
    '2024-01-01', 
    '2099-12-31', 
    1
);
-- Thêm chi tiết khuyến mãi vào bảng ChiTietKhuyenMai
INSERT INTO ChiTietKhuyenMai (maKhuyenMai, maHoaDon, dieuKien, chietKhau)
VALUES ('KM2910202505', NULL, N'42-70 vé', 0.11);
PRINT N'✅ Đã thêm: Giảm 11% khi đặt 42-70 vé';

-- 3. Đặt từ 71-100 vé: Giảm 13%
INSERT INTO KhuyenMai (maKhuyenMai, tenKhuyenMai, loaiKhuyenMai, thoiGianBatDau, thoiGianKetThuc, trangThai)
VALUES (
    'KM2910202506', 
    N'Giảm 13% khi đặt 71-100 vé', 
    'KMHD', 
    '2024-01-01', 
    '2099-12-31', 
    1
);
-- Thêm chi tiết khuyến mãi vào bảng ChiTietKhuyenMai
INSERT INTO ChiTietKhuyenMai (maKhuyenMai, maHoaDon, dieuKien, chietKhau)
VALUES ('KM2910202506', NULL, N'71-100 vé', 0.13);
PRINT N'✅ Đã thêm: Giảm 13% khi đặt 71-100 vé';

-- 4. Đặt từ 100 vé trở lên: Giảm 15%
INSERT INTO KhuyenMai (maKhuyenMai, tenKhuyenMai, loaiKhuyenMai, thoiGianBatDau, thoiGianKetThuc, trangThai)
VALUES (
    'KM2910202507', 
    N'Giảm 15% khi đặt từ 100 vé trở lên', 
    'KMHD', 
    '2024-01-01', 
    '2099-12-31', 
    1
);
-- Thêm chi tiết khuyến mãi vào bảng ChiTietKhuyenMai
INSERT INTO ChiTietKhuyenMai (maKhuyenMai, maHoaDon, dieuKien, chietKhau)
VALUES ('KM2910202507', NULL, N'≥100 vé', 0.15);
PRINT N'✅ Đã thêm: Giảm 15% khi đặt từ 100 vé trở lên';

GO

-- =============================================
-- KIỂM TRA KẾT QUẢ
-- =============================================

PRINT N'';
PRINT N'📊 KIỂM TRA DỮ LIỆU:';
PRINT N'=====================================';

PRINT N'🎯 Khuyến mãi Đối tượng (KMKH):';
SELECT 
    km.maKhuyenMai, 
    km.tenKhuyenMai, 
    ctkm.dieuKien AS doiTuong,
    ctkm.chietKhau, 
    km.trangThai
FROM KhuyenMai km
JOIN ChiTietKhuyenMai ctkm ON km.maKhuyenMai = ctkm.maKhuyenMai
WHERE km.loaiKhuyenMai = 'KMKH'
ORDER BY ctkm.chietKhau DESC;

PRINT N'';
PRINT N'🎯 Khuyến mãi Hóa đơn (KMHD):';
SELECT 
    km.maKhuyenMai, 
    km.tenKhuyenMai, 
    ctkm.dieuKien AS soLuongVe,
    ctkm.chietKhau, 
    km.trangThai
FROM KhuyenMai km
JOIN ChiTietKhuyenMai ctkm ON km.maKhuyenMai = ctkm.maKhuyenMai
WHERE km.loaiKhuyenMai = 'KMHD'
ORDER BY ctkm.chietKhau ASC;

PRINT N'';
PRINT N'✅ HOÀN THÀNH! Đã thêm tất cả Khuyến Mãi.';
PRINT N'📝 Lưu ý: Để hiển thị đúng trong GUI, cần JOIN với bảng ChiTietKhuyenMai';
GO

