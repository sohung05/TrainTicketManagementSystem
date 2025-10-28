/*
 * @ (#) KhachHang.java          1.0        10/26/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package entity;

/**
 * @description: Entity Khách Hàng
 * @author: Truong Tran Hung
 * @date: 10/26/2025
 * @version: 1.0
 */
public class KhachHang {
    private String maKH;
    private String cccd;
    private String hoTen;
    private String email;
    private String sdt;
    private String doiTuong;  // SinhVien, TreEm, NguoiLon, NguoiCaoTuoi

    public KhachHang() {}

    public KhachHang(String maKH, String cccd, String hoTen, String email, String sdt, String doiTuong) {
        this.maKH = maKH;
        this.cccd = cccd;
        this.hoTen = hoTen;
        this.email = email;
        this.sdt = sdt;
        this.doiTuong = doiTuong;
    }

    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public String getCccd() {
        return cccd;
    }

    public void setCccd(String cccd) {
        this.cccd = cccd;
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

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
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
                ", cccd='" + cccd + '\'' +
                ", hoTen='" + hoTen + '\'' +
                ", email='" + email + '\'' +
                ", sdt='" + sdt + '\'' +
                ", doiTuong='" + doiTuong + '\'' +
                '}';
    }
}



