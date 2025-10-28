package entity;

public class KhachHang {
    private String maKH;
    private String CCCD;
    private String hoTen;
    private String email;
    private String SDT;
    private String doiTuong; // SinhVien, TreEm, NguoiLon, NguoiCaoTuoi

    public KhachHang() {
    }

    public KhachHang(String maKH, String CCCD, String hoTen, String email, String SDT, String doiTuong) {
        this.maKH = maKH;
        this.CCCD = CCCD;
        this.hoTen = hoTen;
        this.email = email;
        this.SDT = SDT;
        this.doiTuong = doiTuong;
    }

    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public String getCCCD() {
        return CCCD;
    }

    public void setCCCD(String CCCD) {
        this.CCCD = CCCD;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSDT() {
        return SDT;
    }

    public void setSDT(String SDT) {
        this.SDT = SDT;
    }

    public String getDoiTuong() {
        return doiTuong;
    }

    public void setDoiTuong(String doiTuong) {
        this.doiTuong = doiTuong;
    }

    @Override
    public String toString() {
        return "KhachHang{" +
                "maKH='" + maKH + '\'' +
                ", hoTen='" + hoTen + '\'' +
                ", SDT='" + SDT + '\'' +
                ", doiTuong='" + doiTuong + '\'' +
                '}';
    }
}