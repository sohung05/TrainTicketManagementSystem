/*
 * @ (#) HoaDon.java          1.0        10/25/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package entity;

/*
 * @description:
 * @author: Truong Tran Hung
 * @date: 10/25/2025
 * @version:    1.0
 */
import java.time.LocalDateTime;
public class HoaDon {
    private String maHoaDon;
    private String maNhanVien;
    private String cccd;          // mã định danh khách hàng
    private String tenKhachHang;
    private String sdt;
    private String khuyenMai;     // chuỗi tên khuyến mãi gộp (nếu có)
    private LocalDateTime ngayTao;
    private LocalDateTime gioTao;
    private double tongTien;

    // --- Constructor rỗng ---
    public HoaDon() {}

    // --- Constructor đầy đủ (nếu cần) ---
    public HoaDon(String maHoaDon, String maNhanVien, String cccd, String tenKhachHang, String sdt,
                  String khuyenMai, LocalDateTime ngayTao, LocalDateTime gioTao, double tongTien) {
        this.maHoaDon = maHoaDon;
        this.maNhanVien = maNhanVien;
        this.cccd = cccd;
        this.tenKhachHang = tenKhachHang;
        this.sdt = sdt;
        this.khuyenMai = khuyenMai;
        this.ngayTao = ngayTao;
        this.gioTao = gioTao;
        this.tongTien = tongTien;
    }

    // --- Getters / Setters ---
    public String getMaHoaDon() { return maHoaDon; }
    public void setMaHoaDon(String maHoaDon) { this.maHoaDon = maHoaDon; }

    public String getMaNhanVien() { return maNhanVien; }
    public void setMaNhanVien(String maNhanVien) { this.maNhanVien = maNhanVien; }

    public String getCccd() { return cccd; }
    public void setCccd(String cccd) { this.cccd = cccd; }

    public String getTenKhachHang() { return tenKhachHang; }
    public void setTenKhachHang(String tenKhachHang) { this.tenKhachHang = tenKhachHang; }

    public String getSdt() { return sdt; }
    public void setSdt(String sdt) { this.sdt = sdt; }

    public String getKhuyenMai() { return khuyenMai; }
    public void setKhuyenMai(String khuyenMai) { this.khuyenMai = khuyenMai; }

    public LocalDateTime getNgayTao() { return ngayTao; }
    public void setNgayTao(LocalDateTime ngayTao) { this.ngayTao = ngayTao; }

    public LocalDateTime getGioTao() { return gioTao; }
    public void setGioTao(LocalDateTime gioTao) { this.gioTao = gioTao; }

    public double getTongTien() { return tongTien; }
    public void setTongTien(double tongTien) { this.tongTien = tongTien; }
}
