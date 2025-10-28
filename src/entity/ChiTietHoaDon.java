package entity;

public class ChiTietHoaDon {
    private String maCTHD;
    private HoaDon hoaDon;
    private Ve ve;
    private int soLuong;
    private double giaVe;
    private double mucGiam;

    // ✅ Constructor đầy đủ
    public ChiTietHoaDon(String maCTHD, HoaDon hoaDon, Ve ve, int soLuong, double giaVe, double mucGiam) {
        this.maCTHD = maCTHD;
        this.hoaDon = hoaDon;
        this.ve = ve;
        this.soLuong = soLuong;
        this.giaVe = giaVe;
        this.mucGiam = mucGiam;
    }

    // ✅ Constructor phụ (dùng khi chỉ có thông tin cơ bản)
    public ChiTietHoaDon(String maVe, double giaVe, int soLuong, double mucGiam) {
        this("", null, null, soLuong, giaVe, mucGiam);
    }

    // ✅ Constructor mặc định (THÊM ĐỂ KHỎI LỖI DAO)
    public ChiTietHoaDon() {
        this("", null, null, 0, 0.0, 0.0);
    }

    // Getter / Setter
    public String getMaCTHD() {
        return maCTHD;
    }

    public void setMaCTHD(String maCTHD) {
        this.maCTHD = maCTHD;
    }

    public HoaDon getHoaDon() {
        return hoaDon;
    }

    public void setHoaDon(HoaDon hoaDon) {
        this.hoaDon = hoaDon;
    }

    public Ve getVe() {
        return ve;
    }

    public void setVe(Ve ve) {
        this.ve = ve;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public double getGiaVe() {
        return giaVe;
    }

    public void setGiaVe(double giaVe) {
        this.giaVe = giaVe;
    }

    public double getMucGiam() {
        return mucGiam;
    }

    public void setMucGiam(double mucGiam) {
        this.mucGiam = mucGiam;
    }

    // ✅ Tính giá vé sau khi giảm
    public double tinhTienVe() {
        return giaVe - (giaVe * mucGiam / 100);
    }

    // ✅ Tính tổng thành tiền cho vé
    public double tinhThanhTien() {
        return tinhTienVe() * soLuong;
    }

    @Override
    public String toString() {
        return "ChiTietHoaDon [maCTHD=" + maCTHD
                + ", ve=" + (ve != null ? ve.getMaVe() : "null")
                + ", soLuong=" + soLuong
                + ", giaVe=" + giaVe
                + ", mucGiam=" + mucGiam
                + ", thanhTien=" + tinhThanhTien() + "]";
    }
}
