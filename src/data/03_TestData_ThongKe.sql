-- ========================================
-- HỆ THỐNG QUẢN LÝ VÉ TÀU - DỮ LIỆU TEST THỐNG KÊ
-- File: 03_TestData_ThongKe.sql
-- Mô tả: Thêm dữ liệu hóa đơn và vé để test Dashboard và Thống kê
-- ========================================

USE HTQLVT;
GO

PRINT N'📊 BẮT ĐẦU TẠO DỮ LIỆU TEST THỐNG KÊ...';
GO

-- ========================================
-- TẠO DỮ LIỆU HÓA ĐƠN & VÉ
-- Tạo 100 hóa đơn trong 30 ngày qua
-- Mỗi hóa đơn có 1-10 vé ngẫu nhiên
-- ========================================

DECLARE @counter INT = 1;
DECLARE @ngayTao DATE;
DECLARE @gioTao TIME;
DECLARE @maHoaDon NVARCHAR(20);
DECLARE @maNhanVien NVARCHAR(20);
DECLARE @maKH NVARCHAR(20);
DECLARE @tongTien DECIMAL(18,2);
DECLARE @soVe INT;
DECLARE @veCounter INT;
DECLARE @maVe NVARCHAR(30);
DECLARE @maVach NVARCHAR(50);
DECLARE @maLichTrinh NVARCHAR(20);
DECLARE @maChoNgoi NVARCHAR(30);
DECLARE @maLoaiVe NVARCHAR(20);
DECLARE @giaVe DECIMAL(18,2);
DECLARE @mucGiamGia DECIMAL(5,2);
DECLARE @thoiGianLenTau DATETIME2(0);
DECLARE @hoTenKH NVARCHAR(150);
DECLARE @cccdKH NVARCHAR(20);

-- Lấy danh sách nhân viên, khách hàng, lịch trình có sẵn
DECLARE @dsNhanVien TABLE (ID INT IDENTITY(1,1), maNV NVARCHAR(20));
DECLARE @dsKhachHang TABLE (ID INT IDENTITY(1,1), maKH NVARCHAR(20), hoTen NVARCHAR(150), CCCD NVARCHAR(20), doiTuong NVARCHAR(30));
DECLARE @dsLichTrinh TABLE (ID INT IDENTITY(1,1), maLT NVARCHAR(20), gioKH DATETIME2(0));
DECLARE @dsChoNgoi TABLE (ID INT IDENTITY(1,1), maCN NVARCHAR(30), gia DECIMAL(18,2));

INSERT INTO @dsNhanVien SELECT maNhanVien FROM NhanVien WHERE trangThai = 1;
INSERT INTO @dsKhachHang SELECT maKH, hoTen, CCCD, doiTuong FROM KhachHang;
INSERT INTO @dsLichTrinh SELECT maLichTrinh, gioKhoiHanh FROM LichTrinh WHERE trangThai = 1;
INSERT INTO @dsChoNgoi SELECT maChoNgoi, gia FROM ChoNgoi;

PRINT N'✅ Đã load danh sách nhân viên, khách hàng, lịch trình';

-- ========================================
-- VÒng lặp tạo 100 hóa đơn
-- ========================================
WHILE @counter <= 100
BEGIN
    -- Random ngày trong 30 ngày qua
    SET @ngayTao = DATEADD(DAY, -ABS(CHECKSUM(NEWID()) % 30), CAST(GETDATE() AS DATE));
    
    -- Random giờ từ 6:00 đến 20:00
    SET @gioTao = CAST(DATEADD(MINUTE, 360 + ABS(CHECKSUM(NEWID()) % 840), 0) AS TIME);
    
    -- Tạo mã hóa đơn: HDddMMyyyy + 6 chữ số unique từ NEWID()
    SET @maHoaDon = 'HD' + FORMAT(@ngayTao, 'ddMMyyyy') + RIGHT('000000' + CAST(ABS(CHECKSUM(NEWID()) % 1000000) AS NVARCHAR(7)), 6);
    
    -- Random nhân viên và khách hàng
    SET @maNhanVien = (SELECT TOP 1 maNV FROM @dsNhanVien ORDER BY NEWID());
    
    SELECT TOP 1 
        @maKH = maKH,
        @hoTenKH = hoTen,
        @cccdKH = CCCD
    FROM @dsKhachHang 
    ORDER BY NEWID();
    
    -- Random số vé (1-10)
    SET @soVe = 1 + ABS(CHECKSUM(NEWID()) % 10);
    SET @tongTien = 0;
    
    -- Tạo hóa đơn
    INSERT INTO HoaDon (maHoaDon, maNhanVien, maKH, gioTao, ngayTao, tongTien, trangThai)
    VALUES (
        @maHoaDon,
        @maNhanVien,
        @maKH,
        CAST(CONCAT(FORMAT(@ngayTao, 'yyyy-MM-dd'), ' ', CAST(@gioTao AS VARCHAR(8))) AS DATETIME2(0)),
        @ngayTao,
        0,  -- Tạm thời 0, sẽ update sau
        1   -- Đã thanh toán
    );
    
    -- Tạo các vé cho hóa đơn này
    SET @veCounter = 1;
    WHILE @veCounter <= @soVe
    BEGIN
        -- Random lịch trình (trong khoảng 7 ngày sau ngày tạo hóa đơn)
        SELECT TOP 1 
            @maLichTrinh = maLT,
            @thoiGianLenTau = gioKH
        FROM @dsLichTrinh 
        WHERE gioKH >= DATEADD(DAY, 0, @ngayTao) 
          AND gioKH <= DATEADD(DAY, 7, @ngayTao)
        ORDER BY NEWID();
        
        -- Nếu không có lịch trình phù hợp, lấy bất kỳ
        IF @maLichTrinh IS NULL
        BEGIN
            SELECT TOP 1 
                @maLichTrinh = maLT,
                @thoiGianLenTau = gioKH
            FROM @dsLichTrinh 
            ORDER BY NEWID();
        END
        
        -- Random chỗ ngồi
        SELECT TOP 1 
            @maChoNgoi = maCN,
            @giaVe = gia
        FROM @dsChoNgoi 
        ORDER BY NEWID();
        
        -- Random loại vé
        SET @maLoaiVe = (
            SELECT TOP 1 maLoaiVe 
            FROM LoaiVe 
            ORDER BY NEWID()
        );
        
        -- Lấy mức giảm giá của loại vé
        SET @mucGiamGia = (SELECT mucGiamGia FROM LoaiVe WHERE maLoaiVe = @maLoaiVe);
        
        -- Tính giá vé sau giảm
        SET @giaVe = @giaVe * (1 - @mucGiamGia);
        
        -- Tạo mã vé: VEddMMyy + 8 chữ số unique từ NEWID()
        SET @maVe = 'VE' + 
            FORMAT(@ngayTao, 'ddMMyy') + 
            RIGHT('00000000' + CAST(ABS(CHECKSUM(NEWID()) % 100000000) AS NVARCHAR(9)), 8);
        
        -- Tạo mã vạch: VE + 6 chữ số ngẫu nhiên
        SET @maVach = 'VE' + RIGHT('000000' + CAST(ABS(CHECKSUM(NEWID()) % 1000000) AS NVARCHAR(6)), 6);
        
        -- Insert vé
        INSERT INTO Ve (
            maVe, maLoaiVe, maVach, thoiGianLenTau, giaVe, 
            maKH, maChoNgoi, maLichTrinh, maToa, trangThai,
            tenKhachHang, soCCCD
        )
        VALUES (
            @maVe,
            @maLoaiVe,
            @maVach,
            @thoiGianLenTau,
            @giaVe,
            @maKH,
            @maChoNgoi,
            @maLichTrinh,
            (SELECT maToa FROM ChoNgoi WHERE maChoNgoi = @maChoNgoi),
            1,  -- Vé hợp lệ
            @hoTenKH,
            @cccdKH
        );
        
        -- Insert chi tiết hóa đơn
        INSERT INTO ChiTietHoaDon (maHoaDon, maVe, soLuong, giaVe, mucGiam)
        VALUES (
            @maHoaDon,
            @maVe,
            1,
            @giaVe,
            @giaVe * @mucGiamGia
        );
        
        -- Cộng dồn tổng tiền
        SET @tongTien = @tongTien + @giaVe;
        
        SET @veCounter = @veCounter + 1;
    END
    
    -- Áp dụng khuyến mãi hóa đơn (nếu đủ điều kiện)
    DECLARE @chietKhauHD DECIMAL(18,2) = 0;
    DECLARE @maKhuyenMai NVARCHAR(20) = NULL;
    DECLARE @dieuKienKM NVARCHAR(200) = NULL;
    
    IF @soVe BETWEEN 11 AND 40
    BEGIN
        SET @maKhuyenMai = 'KM0101202404';
        SET @dieuKienKM = N'11-40 vé';
        SET @chietKhauHD = 0.09;
    END
    ELSE IF @soVe BETWEEN 42 AND 70  -- Sửa từ 41 thành 42 để tránh trùng
    BEGIN
        SET @maKhuyenMai = 'KM0101202405';
        SET @dieuKienKM = N'42-70 vé';
        SET @chietKhauHD = 0.11;
    END
    ELSE IF @soVe BETWEEN 71 AND 100
    BEGIN
        SET @maKhuyenMai = 'KM0101202406';
        SET @dieuKienKM = N'71-100 vé';
        SET @chietKhauHD = 0.13;
    END
    ELSE IF @soVe >= 100
    BEGIN
        SET @maKhuyenMai = 'KM0101202407';
        SET @dieuKienKM = N'≥100 vé';
        SET @chietKhauHD = 0.15;
    END
    
    -- Áp dụng giảm giá hóa đơn
    IF @maKhuyenMai IS NOT NULL
    BEGIN
        SET @tongTien = @tongTien * (1 - @chietKhauHD);
        
        -- Insert chi tiết khuyến mãi
        INSERT INTO ChiTietKhuyenMai (maKhuyenMai, maHoaDon, dieuKien, chietKhau)
        VALUES (@maKhuyenMai, @maHoaDon, @dieuKienKM, @chietKhauHD);
    END
    
    -- Update tổng tiền hóa đơn
    UPDATE HoaDon 
    SET tongTien = @tongTien 
    WHERE maHoaDon = @maHoaDon;
    
    -- In progress mỗi 10 hóa đơn
    IF @counter % 10 = 0
    BEGIN
        PRINT N'⏳ Đã tạo ' + CAST(@counter AS NVARCHAR(10)) + '/100 hóa đơn...';
    END
    
    SET @counter = @counter + 1;
    
    -- Reset biến
    SET @maLichTrinh = NULL;
END

GO

PRINT N'';
PRINT N'🎉🎉🎉 HOÀN THÀNH TẠO DỮ LIỆU TEST! 🎉🎉🎉';
PRINT N'';
PRINT N'✅ Đã tạo 100 hóa đơn trong 30 ngày qua';
PRINT N'✅ Mỗi hóa đơn có 1-10 vé ngẫu nhiên';
PRINT N'✅ Đã áp dụng khuyến mãi đối tượng (theo loại vé)';
PRINT N'✅ Đã áp dụng khuyến mãi hóa đơn (nếu đủ điều kiện)';
PRINT N'';
PRINT N'📊 THỐNG KÊ DỮ LIỆU:';

-- Thống kê tổng quan
SELECT 
    'HoaDon' AS [Loại],
    COUNT(*) AS [Số lượng],
    FORMAT(SUM(tongTien), 'N0') + ' VNĐ' AS [Tổng tiền]
FROM HoaDon;

SELECT 
    'Ve' AS [Loại],
    COUNT(*) AS [Số lượng],
    FORMAT(SUM(giaVe), 'N0') + ' VNĐ' AS [Tổng giá trị]
FROM Ve;

-- Thống kê theo loại vé
PRINT N'';
PRINT N'📋 THỐNG KÊ THEO LOẠI VÉ:';
SELECT 
    lv.tenLoaiVe AS [Loại vé],
    COUNT(v.maVe) AS [Số vé bán],
    FORMAT(SUM(v.giaVe), 'N0') + ' VNĐ' AS [Doanh thu]
FROM Ve v
JOIN LoaiVe lv ON v.maLoaiVe = lv.maLoaiVe
GROUP BY lv.tenLoaiVe
ORDER BY COUNT(v.maVe) DESC;

-- Thống kê theo ngày
PRINT N'';
PRINT N'📅 THỐNG KÊ THEO NGÀY (7 NGÀY GẦN NHẤT):';
SELECT 
    FORMAT(h.ngayTao, 'dd/MM/yyyy') AS [Ngày],
    COUNT(DISTINCT h.maHoaDon) AS [Số hóa đơn],
    COUNT(ct.maVe) AS [Số vé],
    FORMAT(SUM(h.tongTien), 'N0') + ' VNĐ' AS [Doanh thu]
FROM HoaDon h
LEFT JOIN ChiTietHoaDon ct ON h.maHoaDon = ct.maHoaDon
WHERE h.ngayTao >= DATEADD(DAY, -7, CAST(GETDATE() AS DATE))
GROUP BY h.ngayTao
ORDER BY h.ngayTao DESC;

-- Thống kê khuyến mãi đã áp dụng
PRINT N'';
PRINT N'🎁 THỐNG KÊ KHUYẾN MÃI ĐÃ ÁP DỤNG:';
SELECT 
    km.tenKhuyenMai AS [Tên khuyến mãi],
    COUNT(DISTINCT ctkm.maHoaDon) AS [Số lần áp dụng],
    FORMAT(AVG(ctkm.chietKhau) * 100, 'N0') + '%' AS [% Giảm TB]
FROM ChiTietKhuyenMai ctkm
JOIN KhuyenMai km ON ctkm.maKhuyenMai = km.maKhuyenMai
WHERE ctkm.maHoaDon IS NOT NULL
GROUP BY km.tenKhuyenMai, km.maKhuyenMai
ORDER BY COUNT(DISTINCT ctkm.maHoaDon) DESC;

-- Top nhân viên bán nhiều nhất
PRINT N'';
PRINT N'👑 TOP 5 NHÂN VIÊN BÁN NHIỀU NHẤT:';
SELECT TOP 5
    nv.hoTen AS [Nhân viên],
    COUNT(DISTINCT hd.maHoaDon) AS [Số hóa đơn],
    COUNT(ct.maVe) AS [Số vé bán],
    FORMAT(SUM(hd.tongTien), 'N0') + ' VNĐ' AS [Doanh thu]
FROM HoaDon hd
JOIN NhanVien nv ON hd.maNhanVien = nv.maNhanVien
LEFT JOIN ChiTietHoaDon ct ON hd.maHoaDon = ct.maHoaDon
GROUP BY nv.hoTen, nv.maNhanVien
ORDER BY SUM(hd.tongTien) DESC;

-- Top khách hàng mua nhiều nhất
PRINT N'';
PRINT N'👑 TOP 5 KHÁCH HÀNG MUA NHIỀU NHẤT:';
SELECT TOP 5
    kh.hoTen AS [Khách hàng],
    kh.doiTuong AS [Đối tượng],
    COUNT(DISTINCT hd.maHoaDon) AS [Số hóa đơn],
    COUNT(ct.maVe) AS [Số vé mua],
    FORMAT(SUM(hd.tongTien), 'N0') + ' VNĐ' AS [Tổng chi tiêu]
FROM HoaDon hd
JOIN KhachHang kh ON hd.maKH = kh.maKH
LEFT JOIN ChiTietHoaDon ct ON hd.maHoaDon = ct.maHoaDon
GROUP BY kh.hoTen, kh.maKH, kh.doiTuong
ORDER BY SUM(hd.tongTien) DESC;

PRINT N'';
PRINT N'✅ DỮ LIỆU ĐÃ SẴN SÀNG CHO TEST DASHBOARD & THỐNG KÊ!';
PRINT N'';
PRINT N'📌 LƯU Ý:';
PRINT N'  - Hóa đơn có format: HDddMMyyyy + 6 số unique';
PRINT N'  - Vé có format: VEddMMyy + 8 số unique';
PRINT N'  - Mã vạch có format: VE + 6 số ngẫu nhiên';
PRINT N'  - Dữ liệu trải đều trong 30 ngày qua';
PRINT N'  - Tất cả hóa đơn đã thanh toán (trangThai = 1)';
GO

