/* ====================== RESET DATABASE ====================== */
USE master;
IF DB_ID('HTQLBVT') IS NOT NULL
BEGIN
    ALTER DATABASE HTQLBVT SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE HTQLBVT;
END
GO

CREATE DATABASE HTQLBVT;
GO
USE HTQLBVT;
GO

/* ====================== LOOKUP TABLES ======================= */
CREATE TABLE LoaiTau (
    MaLoaiTau   NVARCHAR(20)  NOT NULL PRIMARY KEY,
    TenLoaiTau  NVARCHAR(100) NOT NULL
);

CREATE TABLE LoaiToa (
    MaLoaiToa   NVARCHAR(20)  NOT NULL PRIMARY KEY,
    TenLoaiToa  NVARCHAR(100) NOT NULL
);

CREATE TABLE LoaiVe (
    MaLoaiVe    NVARCHAR(20)  NOT NULL PRIMARY KEY,
    TenLoaiVe   NVARCHAR(100) NOT NULL,
    MucGiamGia  DECIMAL(5,2)  NOT NULL DEFAULT (0)   -- % giảm cố định cho loại vé
);

CREATE TABLE Tuyen (
    MaTuyen     NVARCHAR(20)  NOT NULL PRIMARY KEY,
    TenTuyen    NVARCHAR(200) NOT NULL,
    DoDai       FLOAT         NULL
);

CREATE TABLE Ga (
    MaGa        NVARCHAR(20)  NOT NULL PRIMARY KEY,
    TenGa       NVARCHAR(200) NOT NULL,
    ViTri       NVARCHAR(200) NULL
);

/* =================== CHUYEN TAU / TOA / CHO NGOI =================== */
CREATE TABLE ChuyenTau (
    SoHieuTau   NVARCHAR(20)  NOT NULL PRIMARY KEY,
    TocDo       FLOAT         NULL,
    MaLoaiTau   NVARCHAR(20)  NOT NULL,
    NamSanXuat  DATE          NULL,
    CONSTRAINT FK_ChuyenTau_LoaiTau FOREIGN KEY (MaLoaiTau) REFERENCES LoaiTau(MaLoaiTau)
);

CREATE TABLE Toa (
    MaToa       NVARCHAR(20)  NOT NULL PRIMARY KEY,
    SoHieuTau   NVARCHAR(20)  NOT NULL,
    SoToa       INT           NOT NULL,
    MaLoaiToa   NVARCHAR(20)  NOT NULL,
    CONSTRAINT FK_Toa_ChuyenTau FOREIGN KEY (SoHieuTau) REFERENCES ChuyenTau(SoHieuTau),
    CONSTRAINT FK_Toa_LoaiToa   FOREIGN KEY (MaLoaiToa)  REFERENCES LoaiToa(MaLoaiToa)
);

CREATE TABLE ChoNgoi (
    MaChoNgoi   NVARCHAR(30)  NOT NULL PRIMARY KEY,
    LoaiChoNgoi NVARCHAR(50)  NULL,
    MaToa       NVARCHAR(20)  NOT NULL,
    MoTa        NVARCHAR(200) NULL,
    ViTri       INT           NULL,
    Gia         DECIMAL(18,2) NULL,
    CONSTRAINT FK_ChoNgoi_Toa FOREIGN KEY (MaToa) REFERENCES Toa(MaToa)
);

/* ========================= LICH TRINH ======================= */
CREATE TABLE LichTrinh (
    MaLichTrinh  NVARCHAR(20)  NOT NULL PRIMARY KEY,
    SoHieuTau    NVARCHAR(20)  NOT NULL,
    MaTuyen      NVARCHAR(20)  NOT NULL,
    MaGaDi       NVARCHAR(20)  NOT NULL,
    MaGaDen      NVARCHAR(20)  NOT NULL,
    GioKhoiHanh  DATETIME2(0)  NOT NULL,
    GioDenDuKien DATETIME2(0)  NULL,
    TrangThai    BIT           NOT NULL DEFAULT (1),
    CONSTRAINT FK_LichTrinh_ChuyenTau FOREIGN KEY (SoHieuTau) REFERENCES ChuyenTau(SoHieuTau),
    CONSTRAINT FK_LichTrinh_Tuyen     FOREIGN KEY (MaTuyen)   REFERENCES Tuyen(MaTuyen),
    CONSTRAINT FK_LichTrinh_GaDi      FOREIGN KEY (MaGaDi)    REFERENCES Ga(MaGa),
    CONSTRAINT FK_LichTrinh_GaDen     FOREIGN KEY (MaGaDen)   REFERENCES Ga(MaGa)
);

/* ================= NHAN SU / TAI KHOAN / KH ================= */
CREATE TABLE NhanVien (
    MaNhanVien NVARCHAR(20)   NOT NULL PRIMARY KEY,
    CCCD       NVARCHAR(20)   NULL,
    HoTen      NVARCHAR(150)  NOT NULL,
    SDT        NVARCHAR(20)   NULL,
    Email      NVARCHAR(150)  NULL,
    DiaChi     NVARCHAR(250)  NULL,
    LoaiNV     NVARCHAR(50)   NULL,
    NgaySinh   DATE           NULL,
    NgayVaoLam DATE           NULL,
    GioiTinh   NVARCHAR(10)   NULL,
    TrangThai  BIT            NOT NULL DEFAULT (1)
);

CREATE TABLE TaiKhoan (
    UserName    NVARCHAR(50)  NOT NULL PRIMARY KEY,
    PassWord    NVARCHAR(200) NOT NULL,
    MaNhanVien  NVARCHAR(20)  NOT NULL UNIQUE,
    CONSTRAINT FK_TaiKhoan_NhanVien FOREIGN KEY (MaNhanVien) REFERENCES NhanVien(MaNhanVien)
);

CREATE TABLE KhachHang (
    MaKH      NVARCHAR(20)   NOT NULL PRIMARY KEY,
    CCCD      NVARCHAR(20)   NULL,
    HoTen     NVARCHAR(150)  NOT NULL,
    Email     NVARCHAR(150)  NULL,
    SDT       NVARCHAR(20)   NULL,
    DoiTuong  NVARCHAR(30)   NULL,
    CONSTRAINT CK_KhachHang_DoiTuong CHECK (DoiTuong IS NULL OR DoiTuong IN (N'SinhVien', N'TreEm', N'NguoiLon', N'NguoiCaoTuoi'))
);

/* ===================== HOA DON / VE ========================= */
CREATE TABLE HoaDon (
    MaHoaDon    NVARCHAR(20)  NOT NULL PRIMARY KEY,
    MaNhanVien  NVARCHAR(20)  NOT NULL,
    MaKH        NVARCHAR(20)  NOT NULL,
    GioTao      DATETIME2(0)  NOT NULL,
    NgayTao     DATETIME2(0)  NULL,
    TrangThai   BIT           NOT NULL DEFAULT (1),
    CONSTRAINT FK_HoaDon_NhanVien FOREIGN KEY (MaNhanVien) REFERENCES NhanVien(MaNhanVien),
    CONSTRAINT FK_HoaDon_KhachHang FOREIGN KEY (MaKH) REFERENCES KhachHang(MaKH)
);

CREATE TABLE Ve (
    MaVe           NVARCHAR(30)  NOT NULL PRIMARY KEY,
    MaLoaiVe       NVARCHAR(20)  NOT NULL,
    MaVach         NVARCHAR(50)  NULL,
    ThoiGianLenTau DATETIME2(0)  NOT NULL,
    GiaVe          DECIMAL(18,2) NOT NULL,
    MaKH           NVARCHAR(20)  NULL,
    MaChoNgoi      NVARCHAR(30)  NULL,
    MaLichTrinh    NVARCHAR(20)  NULL,
    TrangThai      BIT           NOT NULL DEFAULT (1),
    TenKhachHang   NVARCHAR(150) NULL,
    SoCCCD         NVARCHAR(20)  NULL,
    CONSTRAINT FK_Ve_LoaiVe FOREIGN KEY (MaLoaiVe) REFERENCES LoaiVe(MaLoaiVe),
    CONSTRAINT FK_Ve_KhachHang FOREIGN KEY (MaKH) REFERENCES KhachHang(MaKH),
    CONSTRAINT FK_Ve_ChoNgoi FOREIGN KEY (MaChoNgoi) REFERENCES ChoNgoi(MaChoNgoi),
    CONSTRAINT FK_Ve_LichTrinh FOREIGN KEY (MaLichTrinh) REFERENCES LichTrinh(MaLichTrinh)
);

CREATE TABLE ChiTietHoaDon (
    MaHoaDon NVARCHAR(20)  NOT NULL,
    MaVe     NVARCHAR(30)  NOT NULL,
    SoLuong  INT           NOT NULL DEFAULT (1),
    GiaVe    DECIMAL(18,2) NOT NULL,
    MucGiam  DECIMAL(18,2) NOT NULL DEFAULT (0),
    CONSTRAINT PK_ChiTietHoaDon PRIMARY KEY (MaHoaDon, MaVe),
    CONSTRAINT FK_CTHD_HoaDon FOREIGN KEY (MaHoaDon) REFERENCES HoaDon(MaHoaDon),
    CONSTRAINT FK_CTHD_Ve FOREIGN KEY (MaVe) REFERENCES Ve(MaVe)
);

/* ===================== KHUYEN MAI =========================== */
CREATE TABLE KhuyenMai (
    MaKhuyenMai    NVARCHAR(20)  NOT NULL PRIMARY KEY,
    TenKhuyenMai   NVARCHAR(150) NOT NULL,
    LoaiKhuyenMai  NVARCHAR(50)  NULL,
    ThoiGianBatDau DATETIME2(0)  NOT NULL,
    ThoiGianKetThuc DATETIME2(0) NOT NULL,
    TrangThai      BIT           NOT NULL DEFAULT (1)
);

CREATE TABLE ChiTietKhuyenMai (
    MaHoaDon    NVARCHAR(20)  NOT NULL,
    MaKhuyenMai NVARCHAR(20)  NOT NULL,
    DieuKien    NVARCHAR(200) NULL,
    ChietKhau   DECIMAL(18,2) NOT NULL DEFAULT (0),
    CONSTRAINT PK_ChiTietKhuyenMai PRIMARY KEY (MaHoaDon, MaKhuyenMai),
    CONSTRAINT FK_CTKM_HoaDon FOREIGN KEY (MaHoaDon) REFERENCES HoaDon(MaHoaDon),
    CONSTRAINT FK_CTKM_KhuyenMai FOREIGN KEY (MaKhuyenMai) REFERENCES KhuyenMai(MaKhuyenMai)
);

/* ====================== INDEX GOI Y ========================= */
CREATE INDEX IX_Toa_SoHieuTau       ON Toa(SoHieuTau);
CREATE INDEX IX_ChoNgoi_MaToa       ON ChoNgoi(MaToa);
CREATE INDEX IX_LichTrinh_TuyenTau  ON LichTrinh(MaTuyen, SoHieuTau);
CREATE INDEX IX_LichTrinh_GaDiDen   ON LichTrinh(MaGaDi, MaGaDen);
CREATE INDEX IX_Ve_LichTrinh        ON Ve(MaLichTrinh);
CREATE INDEX IX_Ve_KhachHang        ON Ve(MaKH);
CREATE INDEX IX_CTHD_MaVe           ON ChiTietHoaDon(MaVe);
CREATE INDEX IX_CTKM_MaKM           ON ChiTietKhuyenMai(MaKhuyenMai);
GO
