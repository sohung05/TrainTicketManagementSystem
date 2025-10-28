-- ========================================
-- FULL DATA: 5 TUYẾN, 10 NGÀY, MỖI TUYẾN 6 CHUYẾN
-- Convention: LẺ = Chiều đi chính, CHẴN = Chiều về
-- ========================================

USE HTQLVT;
GO

-- Clean all data
DELETE FROM ChiTietHoaDon;
DELETE FROM Ve;
DELETE FROM ChoNgoi;
DELETE FROM Toa;
DELETE FROM LichTrinh;
DELETE FROM ChuyenTau;
DELETE FROM HoaDon;
DELETE FROM KhachHang;
DELETE FROM TaiKhoan;
DELETE FROM NhanVien;
DELETE FROM LoaiTau;
DELETE FROM LoaiToa;
DELETE FROM LoaiVe;
DELETE FROM Ga;
DELETE FROM Tuyen;
GO

PRINT N'✅ Đã xóa dữ liệu cũ';
GO

-- ========================================
-- 1. LoaiTau
-- ========================================
INSERT INTO LoaiTau (maLoaiTau, tenLoaiTau) VALUES 
('SE', N'Tàu siêu tốc'),
('TN', N'Tàu thống nhất'),
('SPT', N'Tàu Sài Gòn - Phan Thiết');
GO

-- ========================================
-- 2. LoaiToa
-- ========================================
INSERT INTO LoaiToa (maLoaiToa, tenLoaiToa) VALUES 
('LTOA001', N'Ngồi mềm điều hòa'),
('LTOA002', N'Giường nằm 4 khoang');
GO

-- ========================================
-- 3. LoaiVe
-- ========================================
INSERT INTO LoaiVe (maLoaiVe, tenLoaiVe, mucGiamGia) VALUES 
('LV001', N'Người lớn', 0.00),
('LV002', N'Sinh viên', 0.10),
('LV003', N'Trẻ em', 0.25),
('LV004', N'Người cao tuổi', 0.15);
GO

-- ========================================
-- 4. Ga (8 stations)
-- ========================================
INSERT INTO Ga (maGa, tenGa, viTri) VALUES 
('HN', N'Hà Nội', N'120 Lê Duẩn, Hà Nội'),
('SG', N'Sài Gòn', N'1 Nguyễn Thông, Quận 3, TP.HCM'),
('DN', N'Đà Nẵng', N'202 Hải Phòng, Đà Nẵng'),
('HUE', N'Huế', N'2 Bùi Thị Xuân, Huế'),
('NT', N'Nha Trang', N'17 Thái Nguyên, Nha Trang'),
('PT', N'Phan Thiết', N'Lê Hồng Phong, Phan Thiết'),
('QN', N'Quảng Ngãi', N'Đường Quang Trung, Quảng Ngãi'),
('VT', N'Vũng Tàu', N'Đường 3 Tháng 2, Vũng Tàu');
GO

-- ========================================
-- 5. Tuyen (5 routes x 2 directions = 10)
-- ========================================
INSERT INTO Tuyen (maTuyen, tenTuyen, doDai) VALUES 
-- Tuyến Bắc - Nam chính
('HN-SG', N'Tuyến Hà Nội - TP.HCM', 1726.0),
('SG-HN', N'Tuyến TP.HCM - Hà Nội', 1726.0),
-- Tuyến Bắc - Trung
('HN-DN', N'Tuyến Hà Nội - Đà Nẵng', 791.0),
('DN-HN', N'Tuyến Đà Nẵng - Hà Nội', 791.0),
-- Tuyến Bắc - Trung (Huế)
('HN-HUE', N'Tuyến Hà Nội - Huế', 658.0),
('HUE-HN', N'Tuyến Huế - Hà Nội', 658.0),
-- Tuyến Nam - Duyên hải
('SG-NT', N'Tuyến TP.HCM - Nha Trang', 411.0),
('NT-SG', N'Tuyến Nha Trang - TP.HCM', 411.0),
-- Tuyến Nam ngắn
('SG-PT', N'Tuyến TP.HCM - Phan Thiết', 231.0),
('PT-SG', N'Tuyến Phan Thiết - TP.HCM', 231.0);
GO

PRINT N'✅ Đã thêm cơ bản (LoaiTau, LoaiToa, LoaiVe, Ga, Tuyen)';
GO

-- ========================================
-- 6. ChuyenTau (30 trains: 5 routes x 6 trains)
-- TUYẾN 1: HN-SG (Lẻ 1,3,5) & SG-HN (Chẵn 2,4,6)
-- TUYẾN 2: HN-DN (Lẻ 7,9,11) & DN-HN (Chẵn 8,10,12)
-- TUYẾN 3: HN-HUE (Lẻ 13,15,17) & HUE-HN (Chẵn 14,16,18)
-- TUYẾN 4: SG-NT (Lẻ 19,21,23) & NT-SG (Chẵn 20,22,24)
-- TUYẾN 5: SG-PT (Lẻ 25,27,29) & PT-SG (Chẵn 26,28,30)
-- ========================================
INSERT INTO ChuyenTau (soHieuTau, tocDo, maLoaiTau, namSanXuat) VALUES 
-- HN-SG & SG-HN
('SE1', 90.0, 'SE', 2020), ('SE2', 90.0, 'SE', 2020),
('SE3', 85.0, 'SE', 2019), ('SE4', 85.0, 'SE', 2019),
('TN1', 70.0, 'TN', 2018), ('TN2', 70.0, 'TN', 2018),
-- HN-DN & DN-HN
('SE7', 90.0, 'SE', 2020), ('SE8', 90.0, 'SE', 2020),
('SE9', 85.0, 'SE', 2019), ('SE10', 85.0, 'SE', 2019),
('TN11', 70.0, 'TN', 2018), ('TN12', 70.0, 'TN', 2018),
-- HN-HUE & HUE-HN
('SE13', 90.0, 'SE', 2020), ('SE14', 90.0, 'SE', 2020),
('SE15', 85.0, 'SE', 2019), ('SE16', 85.0, 'SE', 2019),
('TN17', 70.0, 'TN', 2018), ('TN18', 70.0, 'TN', 2018),
-- SG-NT & NT-SG
('SE19', 90.0, 'SE', 2021), ('SE20', 90.0, 'SE', 2021),
('SE21', 85.0, 'SE', 2020), ('SE22', 85.0, 'SE', 2020),
('TN23', 70.0, 'TN', 2019), ('TN24', 70.0, 'TN', 2019),
-- SG-PT & PT-SG
('SPT25', 80.0, 'SPT', 2021), ('SPT26', 80.0, 'SPT', 2021),
('SPT27', 75.0, 'SPT', 2020), ('SPT28', 75.0, 'SPT', 2020),
('SPT29', 70.0, 'SPT', 2019), ('SPT30', 70.0, 'SPT', 2019);
GO

PRINT N'✅ Đã thêm 30 ChuyenTau (5 tuyến x 6 tàu)';
GO

-- ========================================
-- 7. Toa (30 trains x 10 coaches = 300 toa)
-- ========================================
DECLARE @soHieuTau NVARCHAR(20);
DECLARE @soToa INT;

DECLARE train_cursor CURSOR FOR 
SELECT soHieuTau FROM ChuyenTau ORDER BY soHieuTau;

OPEN train_cursor;
FETCH NEXT FROM train_cursor INTO @soHieuTau;

WHILE @@FETCH_STATUS = 0
BEGIN
    SET @soToa = 1;
    WHILE @soToa <= 10
    BEGIN
        INSERT INTO Toa (maToa, soHieuTau, soToa, maLoaiToa)
        VALUES (
            @soHieuTau + '-T' + RIGHT('00' + CAST(@soToa AS NVARCHAR(2)), 2),
            @soHieuTau,
            @soToa,
            CASE WHEN @soToa <= 5 THEN 'LTOA001' ELSE 'LTOA002' END
        );
        SET @soToa = @soToa + 1;
    END
    FETCH NEXT FROM train_cursor INTO @soHieuTau;
END

CLOSE train_cursor;
DEALLOCATE train_cursor;
GO

PRINT N'✅ Đã thêm 300 Toa (30 tàu x 10 toa)';
GO

-- ========================================
-- 8. ChoNgoi (Seats) - FIXED
-- 300 toa x ~50 ghế = 15,000 ghế
-- ========================================
DECLARE @maToa NVARCHAR(20);
DECLARE @soToa INT;
DECLARE @soGhe INT;
DECLARE @maxGhe INT;
DECLARE @giaGhe DECIMAL(10,2);

DECLARE toa_cursor CURSOR FOR 
SELECT t.maToa, t.soToa 
FROM Toa t
ORDER BY t.maToa;

OPEN toa_cursor;
FETCH NEXT FROM toa_cursor INTO @maToa, @soToa;

WHILE @@FETCH_STATUS = 0
BEGIN
    IF @soToa <= 5
    BEGIN
        SET @maxGhe = 64;
        SET @giaGhe = 200000.0;
    END
    ELSE
    BEGIN
        SET @maxGhe = 36;
        SET @giaGhe = 350000.0;
    END
    
    SET @soGhe = 1;
    WHILE @soGhe <= @maxGhe
    BEGIN
        INSERT INTO ChoNgoi (maChoNgoi, maToa, viTri, gia, moTa)
        VALUES (
            @maToa + '-G' + RIGHT('00' + CAST(@soGhe AS NVARCHAR(2)), 2),
            @maToa,
            @soGhe,
            @giaGhe,
            N'Ghế số ' + CAST(@soGhe AS NVARCHAR(3))
        );
        SET @soGhe = @soGhe + 1;
    END
    
    FETCH NEXT FROM toa_cursor INTO @maToa, @soToa;
END

CLOSE toa_cursor;
DEALLOCATE toa_cursor;
GO

PRINT N'✅ Đã thêm 15,000+ ChoNgoi';
GO

-- ========================================
-- 9. LichTrinh (10 ngày x 5 tuyến x 6 chuyến = 300)
-- ========================================
DECLARE @NgayBatDau DATE = CAST(GETDATE() AS DATE);
DECLARE @NgayKetThuc DATE = DATEADD(DAY, 9, @NgayBatDau);
DECLARE @NgayHienTai DATE = @NgayBatDau;

WHILE @NgayHienTai <= @NgayKetThuc
BEGIN
    DECLARE @ngayStr NVARCHAR(8) = FORMAT(@NgayHienTai, 'yyyyMMdd');
    
    -- ===== TUYẾN 1: HN ↔ SG =====
    INSERT INTO LichTrinh (maLichTrinh, soHieuTau, maGaDi, maGaDen, gioKhoiHanh, gioDenDuKien, maTuyen, trangThai) VALUES 
    ('LT-HN-SG-' + @ngayStr + '-01', 'SE1', 'HN', 'SG', 
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 06:00:00') AS DATETIME2(0)),
     CAST(CONCAT(FORMAT(DATEADD(DAY, 1, @NgayHienTai), 'yyyy-MM-dd'), ' 12:00:00') AS DATETIME2(0)), 'HN-SG', 1),
    ('LT-HN-SG-' + @ngayStr + '-02', 'SE3', 'HN', 'SG',
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 12:00:00') AS DATETIME2(0)),
     CAST(CONCAT(FORMAT(DATEADD(DAY, 1, @NgayHienTai), 'yyyy-MM-dd'), ' 18:00:00') AS DATETIME2(0)), 'HN-SG', 1),
    ('LT-HN-SG-' + @ngayStr + '-03', 'TN1', 'HN', 'SG',
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 19:00:00') AS DATETIME2(0)),
     CAST(CONCAT(FORMAT(DATEADD(DAY, 2, @NgayHienTai), 'yyyy-MM-dd'), ' 04:00:00') AS DATETIME2(0)), 'HN-SG', 1),
    ('LT-SG-HN-' + @ngayStr + '-01', 'SE2', 'SG', 'HN',
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 07:00:00') AS DATETIME2(0)),
     CAST(CONCAT(FORMAT(DATEADD(DAY, 1, @NgayHienTai), 'yyyy-MM-dd'), ' 13:00:00') AS DATETIME2(0)), 'SG-HN', 1),
    ('LT-SG-HN-' + @ngayStr + '-02', 'SE4', 'SG', 'HN',
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 13:00:00') AS DATETIME2(0)),
     CAST(CONCAT(FORMAT(DATEADD(DAY, 1, @NgayHienTai), 'yyyy-MM-dd'), ' 19:00:00') AS DATETIME2(0)), 'SG-HN', 1),
    ('LT-SG-HN-' + @ngayStr + '-03', 'TN2', 'SG', 'HN',
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 20:00:00') AS DATETIME2(0)),
     CAST(CONCAT(FORMAT(DATEADD(DAY, 2, @NgayHienTai), 'yyyy-MM-dd'), ' 05:00:00') AS DATETIME2(0)), 'SG-HN', 1);
    
    -- ===== TUYẾN 2: HN ↔ DN =====
    INSERT INTO LichTrinh (maLichTrinh, soHieuTau, maGaDi, maGaDen, gioKhoiHanh, gioDenDuKien, maTuyen, trangThai) VALUES 
    ('LT-HN-DN-' + @ngayStr + '-01', 'SE7', 'HN', 'DN',
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 07:00:00') AS DATETIME2(0)),
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 20:00:00') AS DATETIME2(0)), 'HN-DN', 1),
    ('LT-HN-DN-' + @ngayStr + '-02', 'SE9', 'HN', 'DN',
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 14:00:00') AS DATETIME2(0)),
     CAST(CONCAT(FORMAT(DATEADD(DAY, 1, @NgayHienTai), 'yyyy-MM-dd'), ' 03:00:00') AS DATETIME2(0)), 'HN-DN', 1),
    ('LT-HN-DN-' + @ngayStr + '-03', 'TN11', 'HN', 'DN',
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 21:00:00') AS DATETIME2(0)),
     CAST(CONCAT(FORMAT(DATEADD(DAY, 1, @NgayHienTai), 'yyyy-MM-dd'), ' 11:00:00') AS DATETIME2(0)), 'HN-DN', 1),
    ('LT-DN-HN-' + @ngayStr + '-01', 'SE8', 'DN', 'HN',
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 08:00:00') AS DATETIME2(0)),
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 21:00:00') AS DATETIME2(0)), 'DN-HN', 1),
    ('LT-DN-HN-' + @ngayStr + '-02', 'SE10', 'DN', 'HN',
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 15:00:00') AS DATETIME2(0)),
     CAST(CONCAT(FORMAT(DATEADD(DAY, 1, @NgayHienTai), 'yyyy-MM-dd'), ' 04:00:00') AS DATETIME2(0)), 'DN-HN', 1),
    ('LT-DN-HN-' + @ngayStr + '-03', 'TN12', 'DN', 'HN',
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 22:00:00') AS DATETIME2(0)),
     CAST(CONCAT(FORMAT(DATEADD(DAY, 1, @NgayHienTai), 'yyyy-MM-dd'), ' 12:00:00') AS DATETIME2(0)), 'DN-HN', 1);
    
    -- ===== TUYẾN 3: HN ↔ HUE =====
    INSERT INTO LichTrinh (maLichTrinh, soHieuTau, maGaDi, maGaDen, gioKhoiHanh, gioDenDuKien, maTuyen, trangThai) VALUES 
    ('LT-HN-HUE-' + @ngayStr + '-01', 'SE13', 'HN', 'HUE',
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 08:00:00') AS DATETIME2(0)),
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 20:00:00') AS DATETIME2(0)), 'HN-HUE', 1),
    ('LT-HN-HUE-' + @ngayStr + '-02', 'SE15', 'HN', 'HUE',
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 15:00:00') AS DATETIME2(0)),
     CAST(CONCAT(FORMAT(DATEADD(DAY, 1, @NgayHienTai), 'yyyy-MM-dd'), ' 03:00:00') AS DATETIME2(0)), 'HN-HUE', 1),
    ('LT-HN-HUE-' + @ngayStr + '-03', 'TN17', 'HN', 'HUE',
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 22:00:00') AS DATETIME2(0)),
     CAST(CONCAT(FORMAT(DATEADD(DAY, 1, @NgayHienTai), 'yyyy-MM-dd'), ' 10:00:00') AS DATETIME2(0)), 'HN-HUE', 1),
    ('LT-HUE-HN-' + @ngayStr + '-01', 'SE14', 'HUE', 'HN',
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 09:00:00') AS DATETIME2(0)),
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 21:00:00') AS DATETIME2(0)), 'HUE-HN', 1),
    ('LT-HUE-HN-' + @ngayStr + '-02', 'SE16', 'HUE', 'HN',
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 16:00:00') AS DATETIME2(0)),
     CAST(CONCAT(FORMAT(DATEADD(DAY, 1, @NgayHienTai), 'yyyy-MM-dd'), ' 04:00:00') AS DATETIME2(0)), 'HUE-HN', 1),
    ('LT-HUE-HN-' + @ngayStr + '-03', 'TN18', 'HUE', 'HN',
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 23:00:00') AS DATETIME2(0)),
     CAST(CONCAT(FORMAT(DATEADD(DAY, 1, @NgayHienTai), 'yyyy-MM-dd'), ' 11:00:00') AS DATETIME2(0)), 'HUE-HN', 1);
    
    -- ===== TUYẾN 4: SG ↔ NT =====
    INSERT INTO LichTrinh (maLichTrinh, soHieuTau, maGaDi, maGaDen, gioKhoiHanh, gioDenDuKien, maTuyen, trangThai) VALUES 
    ('LT-SG-NT-' + @ngayStr + '-01', 'SE19', 'SG', 'NT',
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 06:30:00') AS DATETIME2(0)),
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 15:00:00') AS DATETIME2(0)), 'SG-NT', 1),
    ('LT-SG-NT-' + @ngayStr + '-02', 'SE21', 'SG', 'NT',
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 13:00:00') AS DATETIME2(0)),
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 21:30:00') AS DATETIME2(0)), 'SG-NT', 1),
    ('LT-SG-NT-' + @ngayStr + '-03', 'TN23', 'SG', 'NT',
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 20:00:00') AS DATETIME2(0)),
     CAST(CONCAT(FORMAT(DATEADD(DAY, 1, @NgayHienTai), 'yyyy-MM-dd'), ' 06:00:00') AS DATETIME2(0)), 'SG-NT', 1),
    ('LT-NT-SG-' + @ngayStr + '-01', 'SE20', 'NT', 'SG',
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 07:00:00') AS DATETIME2(0)),
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 15:30:00') AS DATETIME2(0)), 'NT-SG', 1),
    ('LT-NT-SG-' + @ngayStr + '-02', 'SE22', 'NT', 'SG',
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 14:00:00') AS DATETIME2(0)),
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 22:30:00') AS DATETIME2(0)), 'NT-SG', 1),
    ('LT-NT-SG-' + @ngayStr + '-03', 'TN24', 'NT', 'SG',
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 21:00:00') AS DATETIME2(0)),
     CAST(CONCAT(FORMAT(DATEADD(DAY, 1, @NgayHienTai), 'yyyy-MM-dd'), ' 07:00:00') AS DATETIME2(0)), 'NT-SG', 1);
    
    -- ===== TUYẾN 5: SG ↔ PT =====
    INSERT INTO LichTrinh (maLichTrinh, soHieuTau, maGaDi, maGaDen, gioKhoiHanh, gioDenDuKien, maTuyen, trangThai) VALUES 
    ('LT-SG-PT-' + @ngayStr + '-01', 'SPT25', 'SG', 'PT',
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 07:00:00') AS DATETIME2(0)),
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 11:00:00') AS DATETIME2(0)), 'SG-PT', 1),
    ('LT-SG-PT-' + @ngayStr + '-02', 'SPT27', 'SG', 'PT',
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 13:00:00') AS DATETIME2(0)),
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 17:00:00') AS DATETIME2(0)), 'SG-PT', 1),
    ('LT-SG-PT-' + @ngayStr + '-03', 'SPT29', 'SG', 'PT',
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 18:00:00') AS DATETIME2(0)),
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 22:00:00') AS DATETIME2(0)), 'SG-PT', 1),
    ('LT-PT-SG-' + @ngayStr + '-01', 'SPT26', 'PT', 'SG',
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 08:00:00') AS DATETIME2(0)),
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 12:00:00') AS DATETIME2(0)), 'PT-SG', 1),
    ('LT-PT-SG-' + @ngayStr + '-02', 'SPT28', 'PT', 'SG',
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 14:00:00') AS DATETIME2(0)),
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 18:00:00') AS DATETIME2(0)), 'PT-SG', 1),
    ('LT-PT-SG-' + @ngayStr + '-03', 'SPT30', 'PT', 'SG',
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 19:00:00') AS DATETIME2(0)),
     CAST(CONCAT(FORMAT(@NgayHienTai, 'yyyy-MM-dd'), ' 23:00:00') AS DATETIME2(0)), 'PT-SG', 1);
    
    SET @NgayHienTai = DATEADD(DAY, 1, @NgayHienTai);
END
GO

PRINT N'✅ Đã thêm 300 LichTrinh (10 ngày x 5 tuyến x 6 chuyến)';
GO

-- ========================================
-- 10. NhanVien, TaiKhoan, KhachHang
-- ========================================
INSERT INTO NhanVien (maNhanVien, CCCD, hoTen, SDT, email, diaChi, chucVu, ngaySinh, ngayVaoLam, trangThai)
VALUES 
('NV24030001', '001234567890', N'Nguyễn Văn An', '0901234567', 'nva@railway.vn', N'Hà Nội', 0, '1990-01-01', '2024-03-01', 1),
('NV24030002', '001234567891', N'Trần Thị Bình', '0901234568', 'ttb@railway.vn', N'TP.HCM', 1, '1995-05-15', '2024-03-01', 1),
('NV24030003', '001234567892', N'Lê Văn Cường', '0901234569', 'lvc@railway.vn', N'Đà Nẵng', 1, '1992-08-20', '2024-03-01', 1);

INSERT INTO TaiKhoan (userName, passWord, maNhanVien)
VALUES 
('admin', '123456', 'NV24030001'),
('nhanvien1', '123456', 'NV24030002'),
('nhanvien2', '123456', 'NV24030003');

INSERT INTO KhachHang (maKH, CCCD, hoTen, email, SDT, doiTuong)
VALUES 
('KH001', '079123456789', N'Lê Minh Tuấn', 'lmt@gmail.com', '0987654321', N'Người lớn'),
('KH002', '079123456788', N'Phạm Thu Hà', 'pth@gmail.com', '0987654322', N'Sinh viên'),
('KH003', '079123456787', N'Ngô Thị Mai', 'ntm@gmail.com', '0987654323', N'Người cao tuổi');
GO

PRINT N'✅ Đã thêm NhanVien, TaiKhoan, KhachHang';
GO

-- ========================================
-- SUCCESS
-- ========================================
PRINT N'';
PRINT N'🎉🎉🎉 HOÀN THÀNH! 🎉🎉🎉';
PRINT N'';
PRINT N'✅ Database: HTQLVT';
PRINT N'✅ Trains: 30 tàu (5 tuyến x 6 tàu)';
PRINT N'✅ Coaches: 300 toa (30 tàu x 10 toa)';
PRINT N'✅ Seats: 15,000 chỗ';
PRINT N'✅ Routes: 5 tuyến (HN-SG, HN-DN, HN-HUE, SG-NT, SG-PT)';
PRINT N'✅ Schedules: 300 lịch trình (10 ngày x 30 chuyến)';
PRINT N'✅ Data: 10 ngày TỪ HÔM NAY (' + CONVERT(NVARCHAR(10), GETDATE(), 103) + ')';
PRINT N'';
PRINT N'📌 Login: admin / 123456';
PRINT N'📌 Mỗi tuyến: 3 chuyến ĐI (lẻ) + 3 chuyến VỀ (chẵn)';
PRINT N'📌 Mỗi tàu: 500 chỗ (320 ngồi + 180 nằm)';
PRINT N'';
PRINT N'📋 TEST:';
PRINT N'  - HN → SG: SE1, SE3, TN1 (3 chuyến)';
PRINT N'  - HN → DN: SE7, SE9, TN11 (3 chuyến)';
PRINT N'  - SG → NT: SE19, SE21, TN23 (3 chuyến)';
GO

