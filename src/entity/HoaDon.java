/*
 * @ (#) HoaDon.java          1.0        10/25/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package entity;

/*
 * @description: Entity Hóa Đơn
 * @author: Truong Tran Hung
 * @date: 10/25/2025
 * @version: 1.0
 */
import java.time.LocalDateTime;

public class HoaDon {
    private String maHoaDon;
    private NhanVien nhanVien;
    private KhachHang khachHang;
    private LocalDateTime gioTao;
    private LocalDateTime ngayTao;
    private boolean trangThai;

    // --- Constructor rỗng ---
    public HoaDon() {}

    // --- Constructor đầy đủ ---
    public HoaDon(String maHoaDon, NhanVien nhanVien, KhachHang khachHang,
                  LocalDateTime gioTao, LocalDateTime ngayTao, boolean trangThai) {
        this.maHoaDon = maHoaDon;
        this.nhanVien = nhanVien;
        this.khachHang = khachHang;
        this.gioTao = gioTao;
        this.ngayTao = ngayTao;
        this.trangThai = trangThai;
    }

    // --- Getters / Setters ---
    public String getMaHoaDon() {
        return maHoaDon;
    }

    public void setMaHoaDon(String maHoaDon) {
        this.maHoaDon = maHoaDon;
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }

    public KhachHang getKhachHang() {
        return khachHang;
    }

    public void setKhachHang(KhachHang khachHang) {
        this.khachHang = khachHang;
    }

    public LocalDateTime getGioTao() {
        return gioTao;
    }

    public void setGioTao(LocalDateTime gioTao) {
        this.gioTao = gioTao;
    }

    public LocalDateTime getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(LocalDateTime ngayTao) {
        this.ngayTao = ngayTao;
    }

    public boolean isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }

    @Override
    public String toString() {
        return "HoaDon{" +
                "maHoaDon='" + maHoaDon + '\'' +
                ", nhanVien=" + nhanVien +
                ", khachHang=" + khachHang +
                ", gioTao=" + gioTao +
                ", ngayTao=" + ngayTao +
                ", trangThai=" + trangThai +
                '}';
    }

    // --- Helper methods cho backward compatibility ---
    public String getMaNhanVien() {
        return nhanVien != null ? nhanVien.getMaNhanVien() : null;
    }

    public String getCccd() {
        return khachHang != null ? khachHang.getCccd() : null;
    }

    public String getTenKhachHang() {
        return khachHang != null ? khachHang.getHoTen() : null;
    }

    public String getSdt() {
        return khachHang != null ? khachHang.getSdt() : null;
    }

    // Temporary fields for loading from DAO (will be removed after refactoring)
    private String khuyenMai;
    private double tongTien;

    public String getKhuyenMai() {
        return khuyenMai;
    }

    public void setKhuyenMai(String khuyenMai) {
        this.khuyenMai = khuyenMai;
    }

    public double getTongTien() {
        return tongTien;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }

    // --- Phương thức tính tổng tiền và tổng giảm giá ---
    public double hinhTongTien() {
        // TODO: Tính từ ChiTietHoaDon
        return tongTien;
    }

    public double hinhTongGiamGia() {
        // TODO: Tính từ ChiTietKhuyenMai
        return 0.0;
    }

    public double hinhThanhTien() {
        return hinhTongTien() - hinhTongGiamGia();
    }
}
