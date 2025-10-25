USE HTQLBVT;
SELECT * FROM HoaDon;
SELECT * FROM Ve;
USE HTQLBVT;
GO

/* 1) Lookup cần thiết cho VE */
IF NOT EXISTS (SELECT 1 FROM LoaiVe WHERE MaLoaiVe = 'LV001')
INSERT INTO LoaiVe (MaLoaiVe, TenLoaiVe, MucGiamGia) VALUES
('LV001', N'Vé thường', 0.0);

IF NOT EXISTS (SELECT 1 FROM LoaiVe WHERE MaLoaiVe = 'LV002')
INSERT INTO LoaiVe (MaLoaiVe, TenLoaiVe, MucGiamGia) VALUES
('LV002', N'Vé giảm 10%', 10.0);

/* 2) Nhân viên – để thỏa FK_HoaDon_NhanVien */
IF NOT EXISTS (SELECT 1 FROM NhanVien WHERE MaNhanVien='NV2100001')
INSERT INTO NhanVien (MaNhanVien, HoTen, TrangThai)
VALUES ('NV2100001', N'Nguyễn Văn NV1', 1);

IF NOT EXISTS (SELECT 1 FROM NhanVien WHERE MaNhanVien='NV2100002')
INSERT INTO NhanVien (MaNhanVien, HoTen, TrangThai)
VALUES ('NV2100002', N'Trần Thị NV2', 1);

/* 3) Khách hàng – để thỏa FK_HoaDon_KhachHang (và có thể dùng cho Vé) */
IF NOT EXISTS (SELECT 1 FROM KhachHang WHERE MaKH='KH001')
INSERT INTO KhachHang (MaKH, HoTen, DoiTuong)
VALUES ('KH001', N'Nguyễn Văn A', N'NguoiLon');

IF NOT EXISTS (SELECT 1 FROM KhachHang WHERE MaKH='KH002')
INSERT INTO KhachHang (MaKH, HoTen, DoiTuong)
VALUES ('KH002', N'Trần Thị B', N'NguoiLon');

/* 4) Hóa đơn – giờ mới insert được vì đã có NV & KH */
INSERT INTO HoaDon (MaHoaDon, MaNhanVien, MaKH, GioTao, NgayTao, TrangThai)
VALUES 
('HD001', 'NV2100001', 'KH001', '2025-10-25T14:30:00', '2025-10-25', 1),
('HD002', 'NV2100002', 'KH002', '2025-10-26T09:15:00', '2025-10-26', 1);

/* 5) Vé – tối thiểu cần MaVe, MaLoaiVe, ThoiGianLenTau, GiaVe.
      Các FK khác (MaKH, MaChoNgoi, MaLichTrinh) tạm để NULL cho nhanh. */
INSERT INTO Ve (MaVe, MaLoaiVe, ThoiGianLenTau, GiaVe, MaKH, MaChoNgoi, MaLichTrinh, TrangThai, TenKhachHang, SoCCCD)
VALUES
('VE001', 'LV001', '2025-10-30T08:00:00', 250000, 'KH001', NULL, NULL, 1, N'Nguyễn Văn A',  '012345678900'),
('VE002', 'LV002', '2025-11-01T09:30:00', 300000, 'KH002', NULL, NULL, 1, N'Trần Thị B', '05420409402');


