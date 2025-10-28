package entity;

import java.time.LocalDate;

public class NhanVien {
    private String maNhanVien;
    private String CCCD;
    private String hoTen;
    private String SDT;
    private String email;
    private String diaChi;
    private int chucVu;  // ✅ Đổi từ loaiNV String → chucVu int
    private boolean trangThai;
    private LocalDate ngaySinh;
    private LocalDate ngayVaoLam;
    private String gioiTinh;

    public NhanVien() {
    }

    public NhanVien(String maNhanVien, String CCCD, String hoTen, String SDT,
                    String email, String diaChi, int chucVu, boolean trangThai,
                    LocalDate ngaySinh, LocalDate ngayVaoLam, String gioiTinh) {
        this.maNhanVien = maNhanVien;
        this.CCCD = CCCD;
        this.hoTen = hoTen;
        this.SDT = SDT;
        this.email = email;
        this.diaChi = diaChi;
        this.chucVu = chucVu;  // ✅ Đổi từ loaiNV → chucVu
        this.trangThai = trangThai;
        this.ngaySinh = ngaySinh;
        this.ngayVaoLam = ngayVaoLam;
        this.gioiTinh = gioiTinh;
    }

    public String getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(String maNhanVien) {
        this.maNhanVien = maNhanVien;
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

    public String getSDT() {
        return SDT;
    }

    public void setSDT(String SDT) {
        this.SDT = SDT;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public int getChucVu() {  // ✅ Đổi từ getLoaiNV() → getChucVu()
        return chucVu;
    }

    public void setChucVu(int chucVu) {  // ✅ Đổi từ setLoaiNV() → setChucVu()
        this.chucVu = chucVu;
    }

    public boolean isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }

    public LocalDate getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(LocalDate ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public LocalDate getNgayVaoLam() {
        return ngayVaoLam;
    }

    public void setNgayVaoLam(LocalDate ngayVaoLam) {
        this.ngayVaoLam = ngayVaoLam;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    @Override
    public String toString() {
        return "NhanVien{" +
                "maNhanVien='" + maNhanVien + '\'' +
                ", hoTen='" + hoTen + '\'' +
                ", chucVu=" + chucVu +
                ", trangThai=" + trangThai +
                '}';
    }
}