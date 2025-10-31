-- =============================================
-- Script: Fix ChiTietKhuyenMai Schema (Dynamic)
-- Mô tả: Tạo lại bảng với kiểu dữ liệu động từ bảng gốc
-- =============================================

USE HTQLVT;
GO

PRINT N'🔧 Đang sửa schema bảng ChiTietKhuyenMai...';
PRINT N'⚠️  Sẽ backup dữ liệu cũ → Xóa bảng → Tạo lại bảng mới';
GO

-- BƯỚC 1: Backup dữ liệu cũ (nếu có)
IF OBJECT_ID('ChiTietKhuyenMai_Backup', 'U') IS NOT NULL
    DROP TABLE ChiTietKhuyenMai_Backup;

IF OBJECT_ID('ChiTietKhuyenMai', 'U') IS NOT NULL
BEGIN
    IF EXISTS (SELECT 1 FROM ChiTietKhuyenMai)
    BEGIN
        SELECT * INTO ChiTietKhuyenMai_Backup FROM ChiTietKhuyenMai;
        PRINT N'✅ Đã backup dữ liệu cũ vào ChiTietKhuyenMai_Backup';
    END
    ELSE
    BEGIN
        PRINT N'ℹ️  Bảng ChiTietKhuyenMai không có dữ liệu, bỏ qua backup';
    END
    
    -- BƯỚC 2: Xóa bảng cũ
    DROP TABLE ChiTietKhuyenMai;
    PRINT N'✅ Đã xóa bảng ChiTietKhuyenMai cũ';
END
ELSE
BEGIN
    PRINT N'ℹ️  Bảng ChiTietKhuyenMai không tồn tại, bỏ qua backup';
END
GO

-- BƯỚC 3: Lấy kiểu dữ liệu từ bảng gốc
DECLARE @dataTypeKM NVARCHAR(50);
DECLARE @dataTypeHD NVARCHAR(50);
DECLARE @sql NVARCHAR(MAX);

-- Lấy kiểu dữ liệu của KhuyenMai.maKhuyenMai
SELECT @dataTypeKM = DATA_TYPE + '(' + CAST(CHARACTER_MAXIMUM_LENGTH AS VARCHAR(10)) + ')'
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_NAME = 'KhuyenMai' AND COLUMN_NAME = 'maKhuyenMai';

-- Lấy kiểu dữ liệu của HoaDon.maHoaDon
SELECT @dataTypeHD = DATA_TYPE + '(' + CAST(CHARACTER_MAXIMUM_LENGTH AS VARCHAR(10)) + ')'
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_NAME = 'HoaDon' AND COLUMN_NAME = 'maHoaDon';

PRINT N'📊 Kiểu dữ liệu phát hiện:';
PRINT N'   - KhuyenMai.maKhuyenMai: ' + @dataTypeKM;
PRINT N'   - HoaDon.maHoaDon: ' + @dataTypeHD;

-- BƯỚC 4: Tạo bảng động với kiểu dữ liệu phù hợp
SET @sql = N'
CREATE TABLE ChiTietKhuyenMai (
    maChiTiet INT IDENTITY(1,1) PRIMARY KEY,
    maKhuyenMai ' + @dataTypeKM + N' NOT NULL,
    maHoaDon ' + @dataTypeHD + N' NULL,
    dieuKien NVARCHAR(100) NOT NULL,
    chietKhau FLOAT NOT NULL,
    
    CONSTRAINT FK_CTKM_KhuyenMai FOREIGN KEY (maKhuyenMai) REFERENCES KhuyenMai(maKhuyenMai),
    CONSTRAINT FK_CTKM_HoaDon FOREIGN KEY (maHoaDon) REFERENCES HoaDon(maHoaDon)
);
';

EXEC sp_executesql @sql;

PRINT N'✅ Đã tạo lại bảng ChiTietKhuyenMai với schema mới';
PRINT N'   - maChiTiet: INT IDENTITY (PK)';
PRINT N'   - maKhuyenMai: ' + @dataTypeKM + N' NOT NULL';
PRINT N'   - maHoaDon: ' + @dataTypeHD + N' NULL ⭐';
PRINT N'   - dieuKien: NVARCHAR(100) NOT NULL';
PRINT N'   - chietKhau: FLOAT NOT NULL';
GO

-- BƯỚC 5: Khôi phục dữ liệu cũ (nếu có)
IF OBJECT_ID('ChiTietKhuyenMai_Backup', 'U') IS NOT NULL
BEGIN
    INSERT INTO ChiTietKhuyenMai (maKhuyenMai, maHoaDon, dieuKien, chietKhau)
    SELECT maKhuyenMai, maHoaDon, dieuKien, chietKhau
    FROM ChiTietKhuyenMai_Backup;
    
    DECLARE @rowCount INT = @@ROWCOUNT;
    PRINT N'✅ Đã khôi phục ' + CAST(@rowCount AS NVARCHAR(10)) + N' dòng dữ liệu cũ';
    
    -- Xóa bảng backup
    DROP TABLE ChiTietKhuyenMai_Backup;
    PRINT N'✅ Đã xóa bảng backup';
END
GO

PRINT N'';
PRINT N'✅ HOÀN THÀNH! Bảng ChiTietKhuyenMai đã được sửa.';
PRINT N'📝 Bây giờ có thể chạy lại script 03_InsertKhuyenMai.sql';
GO






