/*
 * @ (#) NhanVien.java          1.0        10/26/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package entity;

import java.time.LocalDate;

/**
 * @description: Entity Nhân Viên
 * @author: Truong Tran Hung
 * @date: 10/26/2025
 * @version: 2.0 (Updated for SQLTaoDULieu.sql - chucVu BIT)
 */
public class NhanVien {
    private String maNhanVien;
    private String cccd;
    private String hoTen;
    private String sdt;
    private String email;
    private String diaChi;
    private boolean chucVu;  // true = Nhân viên, false = Quản lý
    private LocalDate ngaySinh;
    private LocalDate ngayVaoLam;
    private boolean trangThai;

    public NhanVien() {}

    public NhanVien(String maNhanVien, String cccd, String hoTen, String sdt, String email, 
                    String diaChi, boolean chucVu, LocalDate ngaySinh, LocalDate ngayVaoLam, 
                    boolean trangThai) {
        this.maNhanVien = maNhanVien;
        this.cccd = cccd;
        this.hoTen = hoTen;
        this.sdt = sdt;
        this.email = email;
        this.diaChi = diaChi;
        this.chucVu = chucVu;
        this.ngaySinh = ngaySinh;
        this.ngayVaoLam = ngayVaoLam;
        this.trangThai = trangThai;
    }

    public String getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(String maNhanVien) {
        this.maNhanVien = maNhanVien;
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

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
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

    public boolean isChucVu() {
        return chucVu;
    }

    public void setChucVu(boolean chucVu) {
        this.chucVu = chucVu;
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

    public boolean isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }

    @Override
    public String toString() {
        return "NhanVien{" +
                "maNhanVien='" + maNhanVien + '\'' +
                ", cccd='" + cccd + '\'' +
                ", hoTen='" + hoTen + '\'' +
                ", sdt='" + sdt + '\'' +
                ", email='" + email + '\'' +
                ", diaChi='" + diaChi + '\'' +
                ", chucVu=" + (chucVu ? "Nhân viên" : "Quản lý") +
                ", ngaySinh=" + ngaySinh +
                ", ngayVaoLam=" + ngayVaoLam +
                ", trangThai=" + (trangThai ? "Đang làm" : "Đã nghỉ") +
                '}';
    }
}



