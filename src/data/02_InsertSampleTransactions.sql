-- ========================================
-- INSERT DỮ LIỆU MẪU: HÓA ĐƠN, VÉ ĐÃ BÁN
-- Để hiển thị Dashboard và Thống kê
-- ========================================

USE HTQLVT;
GO

PRINT N'🎫 Bắt đầu thêm dữ liệu mẫu...';
GO

-- ========================================
-- 1. Thêm một số vé đã bán (30 ngày qua)
-- ========================================
DECLARE @NgayHienTai DATE = CAST(GETDATE() AS DATE);
DECLARE @i INT = 0;
DECLARE @maHoaDon NVARCHAR(20);
DECLARE @maVe NVARCHAR(30);
DECLARE @ngayTao DATETIME2(0);
DECLARE @tongTien DECIMAL(18,2);
DECLARE @giaVe DECIMAL(18,2);
DECLARE @maKH NVARCHAR(20);
DECLARE @maLichTrinh NVARCHAR(20);
DECLARE @maChoNgoi NVARCHAR(30);
DECLARE @maToa NVARCHAR(20);
DECLARE @gioKhoiHanh DATETIME2(0);
DECLARE @soHieuTau NVARCHAR(20);
DECLARE @tenKH NVARCHAR(150);
DECLARE @cccdKH NVARCHAR(20);

-- Lấy danh sách lịch trình có sẵn
DECLARE @dsLichTrinh TABLE (
    maLichTrinh NVARCHAR(20),
    soHieuTau NVARCHAR(20),
    gioKhoiHanh DATETIME2(0)
);

INSERT INTO @dsLichTrinh
SELECT TOP 30 maLichTrinh, soHieuTau, gioKhoiHanh 
FROM LichTrinh 
WHERE trangThai = 1
ORDER BY gioKhoiHanh;

-- Tạo 50 hóa đơn mẫu trong 30 ngày qua
WHILE @i < 50
BEGIN
    -- Ngày tạo ngẫu nhiên trong 30 ngày qua
    SET @ngayTao = DATEADD(DAY, -ABS(CHECKSUM(NEWID()) % 30), GETDATE());
    
    -- Mã hóa đơn
    SET @maHoaDon = 'HD' + FORMAT(@ngayTao, 'yyyyMMdd') + RIGHT('000' + CAST(@i AS NVARCHAR(3)), 3);
    
    -- Chọn khách hàng ngẫu nhiên
    SELECT TOP 1 @maKH = maKH FROM KhachHang ORDER BY NEWID();
    
    -- Chọn lịch trình ngẫu nhiên
    SELECT TOP 1 
        @maLichTrinh = maLichTrinh,
        @soHieuTau = soHieuTau,
        @gioKhoiHanh = gioKhoiHanh
    FROM @dsLichTrinh 
    ORDER BY NEWID();
    
    -- Chọn chỗ ngồi ngẫu nhiên
    SELECT TOP 1 
        @maChoNgoi = cn.maChoNgoi,
        @giaVe = cn.gia,
        @maToa = cn.maToa
    FROM ChoNgoi cn
    INNER JOIN Toa t ON t.maToa = cn.maToa
    INNER JOIN LichTrinh lt ON lt.soHieuTau = t.soHieuTau
    WHERE lt.maLichTrinh = @maLichTrinh
      AND NOT EXISTS (
          SELECT 1 FROM Ve v 
          WHERE v.maChoNgoi = cn.maChoNgoi 
            AND v.maLichTrinh = @maLichTrinh
      )
    ORDER BY NEWID();
    
    -- Nếu không tìm được chỗ ngồi, bỏ qua
    IF @maChoNgoi IS NULL
    BEGIN
        SET @i = @i + 1;
        CONTINUE;
    END
    
    -- Tổng tiền (có thể có giảm giá 0-20%)
    SET @tongTien = @giaVe * (1 - (ABS(CHECKSUM(NEWID()) % 21) / 100.0));
    
    -- Tạo hóa đơn
    INSERT INTO HoaDon (maHoaDon, maNhanVien, maKH, gioTao, ngayTao, tongTien, trangThai)
    VALUES (
        @maHoaDon,
        'NV24030001', -- Admin
        @maKH,
        @ngayTao,
        @ngayTao,
        @tongTien,
        1
    );
    
    -- Mã vé
    SET @maVe = 'VE' + FORMAT(@ngayTao, 'yyyyMMdd') + RIGHT('00000' + CAST(@i AS NVARCHAR(5)), 5);
    
    -- Lấy thông tin khách hàng
    SELECT @tenKH = hoTen, @cccdKH = CCCD FROM KhachHang WHERE maKH = @maKH;
    
    -- Tạo vé
    INSERT INTO Ve (maVe, maLoaiVe, maVach, thoiGianLenTau, giaVe, maKH, maChoNgoi, maLichTrinh, trangThai, tenKhachHang, soCCCD)
    VALUES (
        @maVe,
        'LV001', -- Người lớn
        'BARCODE' + @maVe,
        @gioKhoiHanh,
        @giaVe,
        @maKH,
        @maChoNgoi,
        @maLichTrinh,
        1,
        @tenKH,
        @cccdKH
    );
    
    -- Tạo chi tiết hóa đơn
    INSERT INTO ChiTietHoaDon (maHoaDon, maVe, soLuong, giaVe, mucGiam)
    VALUES (
        @maHoaDon,
        @maVe,
        1,
        @giaVe,
        @giaVe - @tongTien -- Mức giảm
    );
    
    -- Reset biến
    SET @maChoNgoi = NULL;
    SET @gioKhoiHanh = NULL;
    SET @i = @i + 1;
    
    -- In tiến độ
    IF @i % 10 = 0
        PRINT N'  ✅ Đã tạo ' + CAST(@i AS NVARCHAR(10)) + ' hóa đơn...';
END
GO

PRINT N'';
PRINT N'🎉 HOÀN THÀNH!';
PRINT N'';
PRINT N'✅ Đã thêm ~50 hóa đơn và vé trong 30 ngày qua';
PRINT N'✅ Bây giờ bạn có thể xem Dashboard và Thống kê';
PRINT N'';
PRINT N'📊 Kiểm tra dữ liệu:';
GO

-- Hiển thị thống kê
SELECT 
    'Tổng hóa đơn' AS [Loại],
    COUNT(*) AS [Số lượng],
    FORMAT(SUM(tongTien), 'N0') + ' VNĐ' AS [Tổng tiền]
FROM HoaDon
UNION ALL
SELECT 
    'Tổng vé đã bán',
    COUNT(*),
    FORMAT(SUM(giaVe), 'N0') + ' VNĐ'
FROM Ve
WHERE trangThai = 1;
GO

-- Doanh thu 7 ngày gần nhất
SELECT 
    CAST(ngayTao AS DATE) AS [Ngày],
    COUNT(*) AS [Số hóa đơn],
    FORMAT(SUM(tongTien), 'N0') + ' VNĐ' AS [Doanh thu]
FROM HoaDon
WHERE ngayTao >= DATEADD(DAY, -7, GETDATE())
GROUP BY CAST(ngayTao AS DATE)
ORDER BY CAST(ngayTao AS DATE) DESC;
GO

