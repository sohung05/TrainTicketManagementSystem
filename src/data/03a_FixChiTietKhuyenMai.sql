-- =============================================
-- Script: Fix ChiTietKhuyenMai Schema
-- Mô tả: Tạo lại bảng để cho phép maHoaDon NULL
-- =============================================

USE HTQLVT;
GO

PRINT N'🔧 Đang sửa schema bảng ChiTietKhuyenMai...';
PRINT N'⚠️  Sẽ backup dữ liệu cũ → Xóa bảng → Tạo lại bảng mới';
GO

-- BƯỚC 1: Backup dữ liệu cũ (nếu có)
IF OBJECT_ID('ChiTietKhuyenMai_Backup', 'U') IS NOT NULL
    DROP TABLE ChiTietKhuyenMai_Backup;

IF EXISTS (SELECT 1 FROM ChiTietKhuyenMai)
BEGIN
    SELECT * INTO ChiTietKhuyenMai_Backup FROM ChiTietKhuyenMai;
    PRINT N'✅ Đã backup dữ liệu cũ vào ChiTietKhuyenMai_Backup';
END
ELSE
BEGIN
    PRINT N'ℹ️  Bảng ChiTietKhuyenMai không có dữ liệu, bỏ qua backup';
END
GO

-- BƯỚC 2: Xóa bảng cũ
IF OBJECT_ID('ChiTietKhuyenMai', 'U') IS NOT NULL
BEGIN
    DROP TABLE ChiTietKhuyenMai;
    PRINT N'✅ Đã xóa bảng ChiTietKhuyenMai cũ';
END
GO

-- BƯỚC 3: Tạo lại bảng với thiết kế mới
-- Lấy kiểu dữ liệu từ bảng gốc để đảm bảo khớp
CREATE TABLE ChiTietKhuyenMai (
    maChiTiet INT IDENTITY(1,1) PRIMARY KEY,  -- ⭐ Thêm ID tự tăng làm PK
    maKhuyenMai NVARCHAR(50) NOT NULL,        -- Khớp với KhuyenMai.maKhuyenMai
    maHoaDon NVARCHAR(50) NULL,               -- ⭐ Cho phép NULL, khớp với HoaDon.maHoaDon
    dieuKien NVARCHAR(100) NOT NULL,
    chietKhau FLOAT NOT NULL,
    
    -- Foreign Keys
    CONSTRAINT FK_CTKM_KhuyenMai FOREIGN KEY (maKhuyenMai) REFERENCES KhuyenMai(maKhuyenMai),
    CONSTRAINT FK_CTKM_HoaDon FOREIGN KEY (maHoaDon) REFERENCES HoaDon(maHoaDon)
);

PRINT N'✅ Đã tạo lại bảng ChiTietKhuyenMai với schema mới';
PRINT N'   - maChiTiet: INT IDENTITY (PK)';
PRINT N'   - maKhuyenMai: NVARCHAR(50) NOT NULL';
PRINT N'   - maHoaDon: NVARCHAR(50) NULL ⭐';
PRINT N'   - dieuKien: NVARCHAR(100) NOT NULL';
PRINT N'   - chietKhau: FLOAT NOT NULL';
GO

-- BƯỚC 4: Khôi phục dữ liệu cũ (nếu có)
IF OBJECT_ID('ChiTietKhuyenMai_Backup', 'U') IS NOT NULL
BEGIN
    INSERT INTO ChiTietKhuyenMai (maKhuyenMai, maHoaDon, dieuKien, chietKhau)
    SELECT maKhuyenMai, maHoaDon, dieuKien, chietKhau
    FROM ChiTietKhuyenMai_Backup;
    
    PRINT N'✅ Đã khôi phục dữ liệu cũ';
    
    -- Xóa bảng backup
    DROP TABLE ChiTietKhuyenMai_Backup;
    PRINT N'✅ Đã xóa bảng backup';
END
GO

PRINT N'';
PRINT N'✅ HOÀN THÀNH! Bảng ChiTietKhuyenMai đã được sửa.';
PRINT N'📝 Bây giờ có thể chạy lại script 03_InsertKhuyenMai.sql';
GO

