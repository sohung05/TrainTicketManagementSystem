/*
 * @ (#) ChoNgoi.java          1.0        10/26/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package entity;

/**
 * @description: Entity Chỗ Ngồi
 * @author: Truong Tran Hung
 * @date: 10/26/2025
 * @version: 1.0
 */
public class ChoNgoi {
    private String maChoNgoi;
    // ❌ XÓA loaiChoNgoi - không có trong SQLTaoDULieu
    private Toa toa;
    private String moTa;
    private int viTri;
    private double gia;

    public ChoNgoi() {}

    public ChoNgoi(String maChoNgoi, Toa toa, String moTa, int viTri, double gia) {
        this.maChoNgoi = maChoNgoi;
        this.toa = toa;
        this.moTa = moTa;
        this.viTri = viTri;
        this.gia = gia;
    }

    public String getMaChoNgoi() {
        return maChoNgoi;
    }

    public void setMaChoNgoi(String maChoNgoi) {
        this.maChoNgoi = maChoNgoi;
    }

    public Toa getToa() {
        return toa;
    }

    public void setToa(Toa toa) {
        this.toa = toa;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public int getViTri() {
        return viTri;
    }

    public void setViTri(int viTri) {
        this.viTri = viTri;
    }

    public double getGia() {
        return gia;
    }

    public void setGia(double gia) {
        this.gia = gia;
    }

    @Override
    public String toString() {
        return "ChoNgoi{" +
                "maChoNgoi='" + maChoNgoi + '\'' +
                ", toa=" + toa +
                ", moTa='" + moTa + '\'' +
                ", viTri=" + viTri +
                ", gia=" + gia +
                '}';
    }
}


