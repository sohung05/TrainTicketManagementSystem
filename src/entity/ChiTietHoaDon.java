/*
 * @ (#) ChiTietHoaDon.java          1.0        10/26/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package entity;

/**
 * @description: Entity Chi Tiết Hóa Đơn
 * @author: Truong Tran Hung
 * @date: 10/26/2025
 * @version: 1.0
 */
public class ChiTietHoaDon {
    private HoaDon hoaDon;
    private Ve ve;
    private int soLuong;
    private double giaVe;
    private double mucGiam;

    public ChiTietHoaDon() {}

    public ChiTietHoaDon(HoaDon hoaDon, Ve ve, int soLuong, double giaVe, double mucGiam) {
        this.hoaDon = hoaDon;
        this.ve = ve;
        this.soLuong = soLuong;
        this.giaVe = giaVe;
        this.mucGiam = mucGiam;
    }

    // Getters and Setters
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
    
    // ===== HELPER METHODS cho DAO =====
    
    /**
     * Lấy mã hóa đơn dưới dạng String
     */
    public String getMaHoaDon() {
        return hoaDon != null ? hoaDon.getMaHoaDon() : null;
    }
    
    /**
     * Set hóa đơn bằng mã hóa đơn (tạo object HoaDon tạm)
     */
    public void setMaHoaDon(String maHoaDon) {
        if (this.hoaDon == null) {
            this.hoaDon = new HoaDon();
        }
        this.hoaDon.setMaHoaDon(maHoaDon);
    }
    
    /**
     * Lấy mã vé dưới dạng String
     */
    public String getMaVe() {
        return ve != null ? ve.getMaVe() : null;
    }
    
    /**
     * Set vé bằng mã vé (tạo object Ve tạm)
     */
    public void setMaVe(String maVe) {
        if (this.ve == null) {
            this.ve = new Ve();
        }
        this.ve.setMaVe(maVe);
    }

    // Tính tổng tiền (Giá vé * Số lượng - Mức giảm)
    public double tinhTongTien() {
        return (giaVe * soLuong) - mucGiam;
    }

    // Tính tổng tiền giảm giá
    public double hinhTongGiamGia() {
        return mucGiam;
    }

    @Override
    public String toString() {
        return "ChiTietHoaDon{" +
                "hoaDon=" + hoaDon +
                ", ve=" + ve +
                ", soLuong=" + soLuong +
                ", giaVe=" + giaVe +
                ", mucGiam=" + mucGiam +
                '}';
    }
}




