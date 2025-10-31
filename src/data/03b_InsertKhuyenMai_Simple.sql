-- =============================================
-- Script: Insert Khuyến Mãi (Simplified Version)
-- Mô tả: Thêm các khuyến mãi - Phiên bản đơn giản
-- =============================================

USE HTQLVT;
GO

-- Xóa dữ liệu cũ
DELETE FROM ChiTietKhuyenMai;
DELETE FROM KhuyenMai;
GO

PRINT N'🎉 Bắt đầu thêm dữ liệu Khuyến Mãi...';

-- =============================================
-- PHẦN 1: KHUYẾN MÃI THEO ĐỐI TƯỢNG (KMKH)
-- =============================================

-- Trẻ em 6-10 tuổi: Giảm 25%
INSERT INTO KhuyenMai (maKhuyenMai, tenKhuyenMai, loaiKhuyenMai, thoiGianBatDau, thoiGianKetThuc, trangThai)
VALUES ('KM2910202501', N'Giảm 25% cho Trẻ em (6-10 tuổi)', 'KMKH', '2024-01-01', '2099-12-31', 1);

INSERT INTO ChiTietKhuyenMai (maKhuyenMai, maHoaDon, dieuKien, chietKhau)
VALUES ('KM2910202501', NULL, N'TreEm', 0.25);

PRINT N'✅ Đã thêm: Giảm 25% cho Trẻ em (6-10 tuổi)';

-- Người cao tuổi ≥60 tuổi: Giảm 15%
INSERT INTO KhuyenMai (maKhuyenMai, tenKhuyenMai, loaiKhuyenMai, thoiGianBatDau, thoiGianKetThuc, trangThai)
VALUES ('KM2910202502', N'Giảm 15% cho Người cao tuổi (≥60 tuổi)', 'KMKH', '2024-01-01', '2099-12-31', 1);

INSERT INTO ChiTietKhuyenMai (maKhuyenMai, maHoaDon, dieuKien, chietKhau)
VALUES ('KM2910202502', NULL, N'NguoiCaoTuoi', 0.15);

PRINT N'✅ Đã thêm: Giảm 15% cho Người cao tuổi (≥60 tuổi)';

-- Sinh viên: Giảm 10%
INSERT INTO KhuyenMai (maKhuyenMai, tenKhuyenMai, loaiKhuyenMai, thoiGianBatDau, thoiGianKetThuc, trangThai)
VALUES ('KM2910202503', N'Giảm 10% cho Sinh viên', 'KMKH', '2024-01-01', '2099-12-31', 1);

INSERT INTO ChiTietKhuyenMai (maKhuyenMai, maHoaDon, dieuKien, chietKhau)
VALUES ('KM2910202503', NULL, N'SinhVien', 0.10);

PRINT N'✅ Đã thêm: Giảm 10% cho Sinh viên';

-- =============================================
-- PHẦN 2: KHUYẾN MÃI THEO HÓA ĐƠN (KMHD)
-- =============================================

-- 11-40 vé: Giảm 9%
INSERT INTO KhuyenMai (maKhuyenMai, tenKhuyenMai, loaiKhuyenMai, thoiGianBatDau, thoiGianKetThuc, trangThai)
VALUES ('KM2910202504', N'Giảm 9% khi đặt 11-40 vé', 'KMHD', '2024-01-01', '2099-12-31', 1);

INSERT INTO ChiTietKhuyenMai (maKhuyenMai, maHoaDon, dieuKien, chietKhau)
VALUES ('KM2910202504', NULL, N'11-40 vé', 0.09);

PRINT N'✅ Đã thêm: Giảm 9% khi đặt 11-40 vé';

-- 42-70 vé: Giảm 11%
INSERT INTO KhuyenMai (maKhuyenMai, tenKhuyenMai, loaiKhuyenMai, thoiGianBatDau, thoiGianKetThuc, trangThai)
VALUES ('KM2910202505', N'Giảm 11% khi đặt 42-70 vé', 'KMHD', '2024-01-01', '2099-12-31', 1);

INSERT INTO ChiTietKhuyenMai (maKhuyenMai, maHoaDon, dieuKien, chietKhau)
VALUES ('KM2910202505', NULL, N'42-70 vé', 0.11);

PRINT N'✅ Đã thêm: Giảm 11% khi đặt 42-70 vé';

-- 71-100 vé: Giảm 13%
INSERT INTO KhuyenMai (maKhuyenMai, tenKhuyenMai, loaiKhuyenMai, thoiGianBatDau, thoiGianKetThuc, trangThai)
VALUES ('KM2910202506', N'Giảm 13% khi đặt 71-100 vé', 'KMHD', '2024-01-01', '2099-12-31', 1);

INSERT INTO ChiTietKhuyenMai (maKhuyenMai, maHoaDon, dieuKien, chietKhau)
VALUES ('KM2910202506', NULL, N'71-100 vé', 0.13);

PRINT N'✅ Đã thêm: Giảm 13% khi đặt 71-100 vé';

-- ≥100 vé: Giảm 15%
INSERT INTO KhuyenMai (maKhuyenMai, tenKhuyenMai, loaiKhuyenMai, thoiGianBatDau, thoiGianKetThuc, trangThai)
VALUES ('KM2910202507', N'Giảm 15% khi đặt từ 100 vé trở lên', 'KMHD', '2024-01-01', '2099-12-31', 1);

INSERT INTO ChiTietKhuyenMai (maKhuyenMai, maHoaDon, dieuKien, chietKhau)
VALUES ('KM2910202507', NULL, N'≥100 vé', 0.15);

PRINT N'✅ Đã thêm: Giảm 15% khi đặt từ 100 vé trở lên';

-- =============================================
-- KIỂM TRA KẾT QUẢ
-- =============================================

PRINT N'';
PRINT N'📊 KIỂM TRA DỮ LIỆU:';
PRINT N'=====================================';

SELECT COUNT(*) AS [Tổng số KhuyenMai] FROM KhuyenMai;
SELECT COUNT(*) AS [Tổng số ChiTietKhuyenMai] FROM ChiTietKhuyenMai;

PRINT N'';
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
PRINT N'✅ HOÀN THÀNH! Đã thêm 7 Khuyến Mãi (3 KMKH + 4 KMHD).';
GO






