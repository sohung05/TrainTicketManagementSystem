/*
 * @ (#) ChiTietKhuyenMai.java          1.0        10/26/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package entity;

/**
 * @description: Entity Chi Tiết Khuyến Mãi
 * @author: Truong Tran Hung
 * @date: 10/26/2025
 * @version: 1.0
 */
public class ChiTietKhuyenMai {
    private HoaDon hoaDon;
    private KhuyenMai khuyenMai;
    private String dieuKien;
    private double chietKhau;

    public ChiTietKhuyenMai() {}

    public ChiTietKhuyenMai(HoaDon hoaDon, KhuyenMai khuyenMai, String dieuKien, double chietKhau) {
        this.hoaDon = hoaDon;
        this.khuyenMai = khuyenMai;
        this.dieuKien = dieuKien;
        this.chietKhau = chietKhau;
    }

    public HoaDon getHoaDon() {
        return hoaDon;
    }

    public void setHoaDon(HoaDon hoaDon) {
        this.hoaDon = hoaDon;
    }

    public KhuyenMai getKhuyenMai() {
        return khuyenMai;
    }

    public void setKhuyenMai(KhuyenMai khuyenMai) {
        this.khuyenMai = khuyenMai;
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

    @Override
    public String toString() {
        return "ChiTietKhuyenMai{" +
                "hoaDon=" + hoaDon +
                ", khuyenMai=" + khuyenMai +
                ", dieuKien='" + dieuKien + '\'' +
                ", chietKhau=" + chietKhau +
                '}';
    }
}




