package entity;

public class ChiTietKhuyenMai {
    private KhuyenMai khuyenMai;
    private HoaDon hoaDon;
    private String dieuKien;
    private double chietKhau;

    public ChiTietKhuyenMai(KhuyenMai khuyenMai, HoaDon hoaDon, String dieuKien, double chietKhau) {
        super();
        this.khuyenMai = khuyenMai;
        this.hoaDon = hoaDon;
        this.dieuKien = dieuKien;
        this.chietKhau = chietKhau;
    }

    public ChiTietKhuyenMai() {
        this(null, null,"", 0);
    }

    public KhuyenMai getKhuyenMai() {
        return khuyenMai;
    }

    public void setKhuyenMai(KhuyenMai khuyenMai) {
        this.khuyenMai = khuyenMai;
    }

    public HoaDon getHoaDon() {
        return hoaDon;
    }

    public void setHoaDon(HoaDon hoaDon) {
        this.hoaDon = hoaDon;
    }

    public String getDieuKien() {
        return dieuKien;
    }

    public void setDieuKien(String dieuKien) {
        this.dieuKien = dieuKien;
    }

    public double getChietKhau() {
        return chietKhau;
    }

    public void setChietKhau(double chietKhau) {
        this.chietKhau = chietKhau;
    }


}

