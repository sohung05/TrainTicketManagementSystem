/*
 * @ (#) Toa.java          1.0        10/26/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package entity;

/**
 * @description: Entity Toa tàu
 * @author: Truong Tran Hung
 * @date: 10/26/2025
 * @version: 1.0
 */
public class Toa {
    private String maToa;
    private ChuyenTau chuyenTau;
    private int soToa;
    private LoaiToa loaiToa;

    public Toa() {}

    public Toa(String maToa, ChuyenTau chuyenTau, int soToa, LoaiToa loaiToa) {
        this.maToa = maToa;
        this.chuyenTau = chuyenTau;
        this.soToa = soToa;
        this.loaiToa = loaiToa;
    }

    public String getMaToa() {
        return maToa;
    }

    public void setMaToa(String maToa) {
        this.maToa = maToa;
    }

    public ChuyenTau getChuyenTau() {
        return chuyenTau;
    }

    public void setChuyenTau(ChuyenTau chuyenTau) {
        this.chuyenTau = chuyenTau;
    }

    public int getSoToa() {
        return soToa;
    }

    public void setSoToa(int soToa) {
        this.soToa = soToa;
    }

    public LoaiToa getLoaiToa() {
        return loaiToa;
    }

    public void setLoaiToa(LoaiToa loaiToa) {
        this.loaiToa = loaiToa;
    }

    @Override
    public String toString() {
        return "Toa{" +
                "maToa='" + maToa + '\'' +
                ", chuyenTau=" + chuyenTau +
                ", soToa=" + soToa +
                ", loaiToa=" + loaiToa +
                '}';
    }
}



