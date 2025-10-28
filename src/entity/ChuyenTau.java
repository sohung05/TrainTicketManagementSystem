/*
 * @ (#) ChuyenTau.java          1.0        10/26/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package entity;

/**
 * @description: Entity Chuyến Tàu
 * @author: Truong Tran Hung
 * @date: 10/26/2025
 * @version: 2.0 (Updated: namSanXuat INT)
 */
public class ChuyenTau {
    private String soHieuTau;
    private double tocDo;  // km/h
    private LoaiTau loaiTau;
    private Integer namSanXuat;  // Năm sản xuất (2020, 2021...)

    public ChuyenTau() {}

    public ChuyenTau(String soHieuTau, double tocDo, LoaiTau loaiTau, Integer namSanXuat) {
        this.soHieuTau = soHieuTau;
        this.tocDo = tocDo;
        this.loaiTau = loaiTau;
        this.namSanXuat = namSanXuat;
    }

    public String getSoHieuTau() {
        return soHieuTau;
    }

    public void setSoHieuTau(String soHieuTau) {
        this.soHieuTau = soHieuTau;
    }

    public double getTocDo() {
        return tocDo;
    }

    public void setTocDo(double tocDo) {
        this.tocDo = tocDo;
    }

    public LoaiTau getLoaiTau() {
        return loaiTau;
    }

    public void setLoaiTau(LoaiTau loaiTau) {
        this.loaiTau = loaiTau;
    }

    public Integer getNamSanXuat() {
        return namSanXuat;
    }

    public void setNamSanXuat(Integer namSanXuat) {
        this.namSanXuat = namSanXuat;
    }

    @Override
    public String toString() {
        return "ChuyenTau{" +
                "soHieuTau='" + soHieuTau + '\'' +
                ", tocDo=" + tocDo +
                ", loaiTau=" + loaiTau +
                ", namSanXuat=" + namSanXuat +
                '}';
    }
}



