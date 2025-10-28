package entity;

import java.time.LocalDateTime;

public class KhuyenMai {
    private String maKhuyenMai;
    private String tenKhuyenMai;
    private String loaiKhuyenMai;
    private LocalDateTime thoiGianBatDau;
    private LocalDateTime thoiGianKetThuc;
    private boolean trangThai;

    public KhuyenMai() {}

    public KhuyenMai(String maKhuyenMai, String tenKhuyenMai, String loaiKhuyenMai,
                     LocalDateTime thoiGianBatDau, LocalDateTime thoiGianKetThuc, boolean trangThai) {
        this.maKhuyenMai = maKhuyenMai;
        this.tenKhuyenMai = tenKhuyenMai;
        this.loaiKhuyenMai = loaiKhuyenMai;
        this.thoiGianBatDau = thoiGianBatDau;
        this.thoiGianKetThuc = thoiGianKetThuc;
        this.trangThai = trangThai;
    }

    // Getters và Setters
    public String getMaKhuyenMai() { return maKhuyenMai; }
    public void setMaKhuyenMai(String maKhuyenMai) {
        if (maKhuyenMai == null || !maKhuyenMai.matches("^KM\\d{8}\\d{2}$")) {
            throw new IllegalArgumentException(
                    "❌ Mã khuyến mãi không hợp lệ! Phải có dạng KMddMMyyyyXX, ví dụ: KM0609202304");
        }
        this.maKhuyenMai = maKhuyenMai;
    }
    public String getTenKhuyenMai() { return tenKhuyenMai; }
    public void setTenKhuyenMai(String tenKhuyenMai) {
        if (tenKhuyenMai == null || tenKhuyenMai.trim().isEmpty()) {
            throw new IllegalArgumentException("❌ Tên khuyến mãi không được để trống!");
        }
        this.tenKhuyenMai = tenKhuyenMai.trim();
    }
    public String getLoaiKhuyenMai() { return loaiKhuyenMai; }
    public void setLoaiKhuyenMai(String loaiKhuyenMai) {
        if (!"KMKH".equalsIgnoreCase(loaiKhuyenMai) && !"KMHD".equalsIgnoreCase(loaiKhuyenMai)) {
            throw new IllegalArgumentException("❌ Loại khuyến mãi phải là 'KMKH' hoặc 'KMHD'");
        }
        this.loaiKhuyenMai = loaiKhuyenMai.toUpperCase();
    }
    public LocalDateTime getThoiGianBatDau() { return thoiGianBatDau; }
    public void setThoiGianBatDau(LocalDateTime thoiGianBatDau) {
        if (thoiGianBatDau == null)
            throw new IllegalArgumentException("❌ Thời gian bắt đầu không được rỗng!");
        this.thoiGianBatDau = thoiGianBatDau;
    }
    public LocalDateTime getThoiGianKetThuc() { return thoiGianKetThuc; }
    public void setThoiGianKetThuc(LocalDateTime thoiGianKetThuc) {
        if (thoiGianKetThuc == null)
            throw new IllegalArgumentException("❌ Thời gian kết thúc không được rỗng!");
        if (this.thoiGianBatDau != null && thoiGianKetThuc.isBefore(this.thoiGianBatDau)) {
            throw new IllegalArgumentException("❌ Thời gian kết thúc phải sau thời gian bắt đầu!");
        }
        this.thoiGianKetThuc = thoiGianKetThuc;
    }
    public boolean isTrangThai() { return trangThai; }
    public void setTrangThai(boolean trangThai) { this.trangThai = trangThai; }

    private int soVe;

    public int getSoVe() {
        return soVe;
    }

    public void setSoVe(int soVe) {
        this.soVe = soVe;
    }
    private double chietKhau;

    public double getChietKhau() {
        return chietKhau;
    }

    public void setChietKhau(double chietKhau) {
        if (chietKhau <= 0 || chietKhau > 1) {
            throw new IllegalArgumentException("❌ Chiết khấu phải trong khoảng 0 - 1 (dạng 0.25 = 25%)");
        }
        this.chietKhau = chietKhau;
    }
    private String doiTuongApDung;
    public String getDoiTuongApDung() {
        return doiTuongApDung;
    }

    public void setDoiTuongApDung(String doiTuongApDung) {
        this.doiTuongApDung = doiTuongApDung;
    }

    public static String taoMaKhuyenMaiTheoNgay(java.util.Date ngayBatDau, int soThuTu) {
        if (ngayBatDau == null)
            throw new IllegalArgumentException("❌ Ngày bắt đầu không được null khi tạo mã khuyến mãi!");

        String ngay = new java.text.SimpleDateFormat("ddMMyyyy").format(ngayBatDau);
        return String.format("KM%s%02d", ngay, soThuTu);
    }

}
