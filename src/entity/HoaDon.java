package entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HoaDon {

    private String maHoaDon;
    private NhanVien nhanVien;
    private KhachHang khachHang;
    private LocalDateTime gioTao;
    private LocalDateTime ngayTao;
    private double tongTien;
    private boolean trangThai; // true = đã trả, false = chưa trả
    private List<ChiTietHoaDon> danhSachChiTiet;
    private String khuyenMai; // Tạm thời để hiển thị

    // Constructor đầy đủ
    public HoaDon(String maHoaDon, NhanVien nhanVien, KhachHang khachHang,
                  LocalDateTime gioTao, LocalDateTime ngayTao, boolean trangThai) {
        this.maHoaDon = maHoaDon;
        this.nhanVien = nhanVien;
        this.khachHang = khachHang;
        this.gioTao = gioTao;
        this.ngayTao = ngayTao;
        this.trangThai = trangThai;
        this.danhSachChiTiet = new ArrayList<>();
    }

    // Constructor mặc định
    public HoaDon() {
        this("", null, null, LocalDateTime.now(), LocalDateTime.now(), true);
    }

    // Getter/Setter
    public String getMaHoaDon() { return maHoaDon; }
    public void setMaHoaDon(String maHoaDon) { this.maHoaDon = maHoaDon; }

    public NhanVien getNhanVien() { return nhanVien; }
    public void setNhanVien(NhanVien nhanVien) { this.nhanVien = nhanVien; }

    public KhachHang getKhachHang() { return khachHang; }
    public void setKhachHang(KhachHang khachHang) { this.khachHang = khachHang; }

    public LocalDateTime getGioTao() { return gioTao; }
    public void setGioTao(LocalDateTime gioTao) { this.gioTao = gioTao; }

    public LocalDateTime getNgayTao() { return ngayTao; }
    public void setNgayTao(LocalDateTime ngayTao) { this.ngayTao = ngayTao; }

    public boolean isTrangThai() { return trangThai; }
    public void setTrangThai(boolean trangThai) { this.trangThai = trangThai; }

    public double getTongTien() { return tongTien; }
    public void setTongTien(double tongTien) { this.tongTien = tongTien; }

    public String getKhuyenMai() { return khuyenMai; }
    public void setKhuyenMai(String khuyenMai) { this.khuyenMai = khuyenMai; }

    public List<ChiTietHoaDon> getDanhSachChiTiet() { return danhSachChiTiet; }
    public void setDanhSachChiTiet(List<ChiTietHoaDon> danhSachChiTiet) { this.danhSachChiTiet = danhSachChiTiet; }

    public void themChiTiet(ChiTietHoaDon cthd) { danhSachChiTiet.add(cthd); }

    // ✅ Tính tổng tiền = tổng (giá vé * số lượng)
    public double tinhTongTien() {
        double tong = 0;
        for (ChiTietHoaDon cthd : danhSachChiTiet) {
            tong += cthd.getGiaVe() * cthd.getSoLuong();
        }
        return tong;
    }

    // ✅ Tính tổng giảm giá = tổng ((giá vé * % giảm) * số lượng)
    public double tinhTongGiamGia() {
        double tongGiam = 0;
        for (ChiTietHoaDon cthd : danhSachChiTiet) {
            tongGiam += (cthd.getGiaVe() * cthd.getMucGiam() / 100) * cthd.getSoLuong();
        }
        return tongGiam;
    }

    // ✅ Tính thành tiền = tổng tiền - tổng giảm giá
    public double tinhThanhTien() {
        double tong = 0;
        for (ChiTietHoaDon cthd : danhSachChiTiet) {
            tong += cthd.tinhThanhTien();
        }
        return tong;
    }

    // ✅ Kiểm tra hóa đơn đã trả chưa
    public boolean isDaTra() {
        return trangThai;
    }

    // ✅ Tính tổng số vé trong hóa đơn
    public int getTongSoVe() {
        int tongVe = 0;
        for (ChiTietHoaDon cthd : danhSachChiTiet) {
            tongVe += cthd.getSoLuong();
        }
        return tongVe;
    }

    @Override
    public String toString() {
        return "HoaDon [maHoaDon=" + maHoaDon +
                ", nhanVien=" + (nhanVien != null ? nhanVien.getMaNhanVien() : "null") +
                ", khachHang=" + (khachHang != null ? khachHang.getMaKH() : "null") +
                ", ngayTao=" + ngayTao +
                ", tongTien=" + tinhTongTien() +
                ", tongGiam=" + tinhTongGiamGia() +
                ", thanhTien=" + tinhThanhTien() + "]";
    }
}
