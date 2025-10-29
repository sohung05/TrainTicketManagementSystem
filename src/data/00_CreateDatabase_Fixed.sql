-- ========================================
-- FIX: Tạo lại database với cấu trúc đúng
-- ========================================

USE master;
GO
IF DB_ID('HTQLVT') IS NOT NULL
BEGIN
  ALTER DATABASE HTQLVT SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
  DROP DATABASE HTQLVT;
END
GO

CREATE DATABASE HTQLVT;
GO
USE HTQLVT;
GO

/* ============================================
   📚 BẢNG TRA CỨU (LOOKUP)
============================================ */
CREATE TABLE LoaiTau(
  maLoaiTau  NVARCHAR(20) PRIMARY KEY,
  tenLoaiTau NVARCHAR(100) NOT NULL
);

CREATE TABLE LoaiToa(
  maLoaiToa  NVARCHAR(20) PRIMARY KEY,
  tenLoaiToa NVARCHAR(100) NOT NULL
);

CREATE TABLE LoaiVe(
  maLoaiVe   NVARCHAR(20) PRIMARY KEY,
  tenLoaiVe  NVARCHAR(100) NOT NULL,
  mucGiamGia DECIMAL(5,2)  NOT NULL DEFAULT 0
);

CREATE TABLE Tuyen(
  maTuyen   NVARCHAR(20) PRIMARY KEY,
  tenTuyen  NVARCHAR(200) NOT NULL,
  doDai     DECIMAL(18,2) NULL
);

CREATE TABLE Ga(
  maGa   NVARCHAR(20) PRIMARY KEY,
  tenGa  NVARCHAR(200) NOT NULL,
  viTri  NVARCHAR(200) NULL
);

/* ============================================
   🚆 TÀU / TOA / CHỖ NGỒI
============================================ */
CREATE TABLE ChuyenTau(
  soHieuTau  NVARCHAR(20) PRIMARY KEY,
  tocDo      DECIMAL(10,2) NULL,
  maLoaiTau  NVARCHAR(20)  NOT NULL,
  namSanXuat INT NULL,
  FOREIGN KEY (maLoaiTau) REFERENCES LoaiTau(maLoaiTau)
);

CREATE TABLE Toa(
  maToa     NVARCHAR(20) PRIMARY KEY,
  soHieuTau NVARCHAR(20) NOT NULL,
  soToa     INT          NOT NULL,
  maLoaiToa NVARCHAR(20) NOT NULL,
  FOREIGN KEY (soHieuTau) REFERENCES ChuyenTau(soHieuTau),
  FOREIGN KEY (maLoaiToa) REFERENCES LoaiToa(maLoaiToa)
);

CREATE TABLE ChoNgoi(
  maChoNgoi NVARCHAR(30) PRIMARY KEY,
  maToa     NVARCHAR(20) NOT NULL,
  moTa      NVARCHAR(200) NULL,
  viTri     INT NULL,
  gia       DECIMAL(18,2) NULL,
  FOREIGN KEY (maToa) REFERENCES Toa(maToa)
);

/* ============================================
   🕒 LỊCH TRÌNH - FIXED: Thêm maGaDi, maGaDen
============================================ */
CREATE TABLE LichTrinh(
  maLichTrinh  NVARCHAR(20) PRIMARY KEY,
  soHieuTau    NVARCHAR(20) NOT NULL,
  maTuyen      NVARCHAR(20) NOT NULL,
  maGaDi       NVARCHAR(20) NOT NULL,
  maGaDen      NVARCHAR(20) NOT NULL,
  gioKhoiHanh  DATETIME2(0) NOT NULL,
  gioDenDuKien DATETIME2(0) NULL,
  trangThai    BIT NOT NULL DEFAULT 1,
  FOREIGN KEY (soHieuTau) REFERENCES ChuyenTau(soHieuTau),
  FOREIGN KEY (maTuyen)   REFERENCES Tuyen(maTuyen),
  FOREIGN KEY (maGaDi)    REFERENCES Ga(maGa),
  FOREIGN KEY (maGaDen)   REFERENCES Ga(maGa)
);

/* ============================================
   👩‍💼 NHÂN VIÊN / TÀI KHOẢN / KHÁCH HÀNG
   FIXED: Đổi loaiNV -> chucVu
============================================ */
CREATE TABLE NhanVien(
  maNhanVien NVARCHAR(20) PRIMARY KEY,
  CCCD NVARCHAR(20) NULL,
  hoTen NVARCHAR(150) NOT NULL,
  SDT NVARCHAR(20) NULL,
  email NVARCHAR(150) NULL,
  diaChi NVARCHAR(250) NULL,
  chucVu INT NULL,
  trangThai BIT NOT NULL DEFAULT 1,
  ngaySinh DATE NULL,
  ngayVaoLam DATE NULL,
  gioiTinh NVARCHAR(10) NULL
);

/* FIXED: Đổi tenTaiKhoan -> userName, matKhau -> passWord */
CREATE TABLE TaiKhoan(
  userName NVARCHAR(50) PRIMARY KEY,
  passWord NVARCHAR(200) NOT NULL,
  maNhanVien NVARCHAR(20) NOT NULL UNIQUE,
  FOREIGN KEY (maNhanVien) REFERENCES NhanVien(maNhanVien)
);

CREATE TABLE KhachHang(
  maKH NVARCHAR(20) PRIMARY KEY,
  CCCD NVARCHAR(20) NULL,
  hoTen NVARCHAR(150) NOT NULL,
  email NVARCHAR(150) NULL,
  SDT NVARCHAR(20) NULL,
  doiTuong NVARCHAR(30) NULL
);

/* ============================================
   💵 HÓA ĐƠN / VÉ / KHUYẾN MÃI
   FIXED: Thêm cột tongTien vào HoaDon
============================================ */
CREATE TABLE HoaDon(
  maHoaDon NVARCHAR(20) PRIMARY KEY,
  maNhanVien NVARCHAR(20) NOT NULL,
  maKH NVARCHAR(20) NOT NULL,
  gioTao DATETIME2(0) NOT NULL,
  ngayTao DATETIME2(0) NULL,
  tongTien DECIMAL(18,2) NOT NULL DEFAULT 0,
  trangThai BIT NOT NULL DEFAULT 1,
  FOREIGN KEY (maNhanVien) REFERENCES NhanVien(maNhanVien),
  FOREIGN KEY (maKH)       REFERENCES KhachHang(maKH)
);

CREATE TABLE KhuyenMai(
  maKhuyenMai NVARCHAR(20) PRIMARY KEY,
  tenKhuyenMai NVARCHAR(150) NOT NULL,
  loaiKhuyenMai NVARCHAR(50) NULL,
  thoiGianBatDau DATETIME2(0) NOT NULL,
  thoiGianKetThuc DATETIME2(0) NOT NULL,
  trangThai BIT NOT NULL DEFAULT 1
);

CREATE TABLE Ve(
  maVe NVARCHAR(30) PRIMARY KEY,
  maLoaiVe NVARCHAR(20) NOT NULL,
  maVach NVARCHAR(50) NULL,
  thoiGianLenTau DATETIME2(0) NOT NULL,
  giaVe DECIMAL(18,2) NOT NULL,
  maKH NVARCHAR(20) NULL,
  maChoNgoi NVARCHAR(30) NULL,
  maLichTrinh NVARCHAR(20) NULL,
  maToa NVARCHAR(20) NULL,
  trangThai BIT NOT NULL DEFAULT 1,
  tenKhachHang NVARCHAR(150) NULL,
  soCCCD NVARCHAR(20) NULL,
  FOREIGN KEY (maLoaiVe)   REFERENCES LoaiVe(maLoaiVe),
  FOREIGN KEY (maKH)       REFERENCES KhachHang(maKH),
  FOREIGN KEY (maChoNgoi)  REFERENCES ChoNgoi(maChoNgoi),
  FOREIGN KEY (maLichTrinh) REFERENCES LichTrinh(maLichTrinh),
  FOREIGN KEY (maToa)      REFERENCES Toa(maToa)
);

/* ============================================
   📄 CHI TIẾT HÓA ĐƠN & KHUYẾN MÃI
============================================ */
CREATE TABLE ChiTietHoaDon(
  maHoaDon NVARCHAR(20) NOT NULL,
  maVe     NVARCHAR(30) NOT NULL,
  soLuong  INT NOT NULL DEFAULT 1,
  giaVe    DECIMAL(18,2) NOT NULL,
  mucGiam  DECIMAL(18,2) NOT NULL DEFAULT 0,
  CONSTRAINT PK_CTHD PRIMARY KEY (maHoaDon, maVe),
  FOREIGN KEY (maHoaDon) REFERENCES HoaDon(maHoaDon) ON DELETE CASCADE,
  FOREIGN KEY (maVe)     REFERENCES Ve(maVe)
);

CREATE TABLE ChiTietKhuyenMai(
  maHoaDon    NVARCHAR(20) NOT NULL,
  maKhuyenMai NVARCHAR(20) NOT NULL,
  dieuKien    NVARCHAR(200) NULL,
  chietKhau   DECIMAL(18,2) NOT NULL DEFAULT 0,
  CONSTRAINT PK_CTKM PRIMARY KEY (maHoaDon, maKhuyenMai),
  FOREIGN KEY (maHoaDon)    REFERENCES HoaDon(maHoaDon) ON DELETE CASCADE,
  FOREIGN KEY (maKhuyenMai) REFERENCES KhuyenMai(maKhuyenMai)
);

/* ============================================
   ⚙️ INDEX TỐI ƯU TRUY VẤN
============================================ */
CREATE INDEX IX_Toa_Tau        ON Toa(soHieuTau);
CREATE INDEX IX_ChoNgoi_Toa    ON ChoNgoi(maToa);
CREATE INDEX IX_LT_TuyenTau    ON LichTrinh(maTuyen, soHieuTau);
CREATE INDEX IX_LT_GaDi        ON LichTrinh(maGaDi);
CREATE INDEX IX_LT_GaDen       ON LichTrinh(maGaDen);
CREATE INDEX IX_Ve_LichTrinh   ON Ve(maLichTrinh);
CREATE INDEX IX_Ve_KhachHang   ON Ve(maKH);
CREATE INDEX IX_CTHD_Ve        ON ChiTietHoaDon(maVe);
CREATE INDEX IX_CTKM_KM        ON ChiTietKhuyenMai(maKhuyenMai);
GO

PRINT N'✅ Đã tạo database HTQLVT với cấu trúc đã sửa!';
PRINT N'';
PRINT N'🔧 Các thay đổi:';
PRINT N'   1. LichTrinh: Thêm maGaDi, maGaDen (thay vì maGa)';
PRINT N'   2. HoaDon: Thêm cột tongTien';
PRINT N'   3. NhanVien: Đổi loaiNV -> chucVu (INT)';
PRINT N'   4. TaiKhoan: Đổi tenTaiKhoan -> userName, matKhau -> passWord';
PRINT N'';
PRINT N'📌 Tiếp theo: Chạy file InsertFullData_MultiRoutes.sql';
GO

