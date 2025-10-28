/*
 * @ (#) LichTrinh.java          1.0        10/26/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package entity;

import java.time.LocalDateTime;

/**
 * @description: Entity Lịch Trình
 * @author: Truong Tran Hung
 * @date: 10/26/2025
 * @version: 1.0
 */
public class LichTrinh {
    private String maLichTrinh;
    private ChuyenTau chuyenTau;
    private Tuyen tuyen;
    private Ga gaDi;
    private Ga gaDen;
    private LocalDateTime gioKhoiHanh;
    private LocalDateTime gioDenDuKien;
    private boolean trangThai;

    public LichTrinh() {}

    public LichTrinh(String maLichTrinh, ChuyenTau chuyenTau, Tuyen tuyen, Ga gaDi, Ga gaDen,
                     LocalDateTime gioKhoiHanh, LocalDateTime gioDenDuKien, boolean trangThai) {
        this.maLichTrinh = maLichTrinh;
        this.chuyenTau = chuyenTau;
        this.tuyen = tuyen;
        this.gaDi = gaDi;
        this.gaDen = gaDen;
        this.gioKhoiHanh = gioKhoiHanh;
        this.gioDenDuKien = gioDenDuKien;
        this.trangThai = trangThai;
    }

    public String getMaLichTrinh() {
        return maLichTrinh;
    }

    public void setMaLichTrinh(String maLichTrinh) {
        this.maLichTrinh = maLichTrinh;
    }

    public ChuyenTau getChuyenTau() {
        return chuyenTau;
    }

    public void setChuyenTau(ChuyenTau chuyenTau) {
        this.chuyenTau = chuyenTau;
    }

    public Tuyen getTuyen() {
        return tuyen;
    }

    public void setTuyen(Tuyen tuyen) {
        this.tuyen = tuyen;
    }

    public Ga getGaDi() {
        return gaDi;
    }

    public void setGaDi(Ga gaDi) {
        this.gaDi = gaDi;
    }

    public Ga getGaDen() {
        return gaDen;
    }

    public void setGaDen(Ga gaDen) {
        this.gaDen = gaDen;
    }

    public LocalDateTime getGioKhoiHanh() {
        return gioKhoiHanh;
    }

    public void setGioKhoiHanh(LocalDateTime gioKhoiHanh) {
        this.gioKhoiHanh = gioKhoiHanh;
    }

    public LocalDateTime getGioDenDuKien() {
        return gioDenDuKien;
    }

    public void setGioDenDuKien(LocalDateTime gioDenDuKien) {
        this.gioDenDuKien = gioDenDuKien;
    }

    public boolean isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }

    @Override
    public String toString() {
        return "LichTrinh{" +
                "maLichTrinh='" + maLichTrinh + '\'' +
                ", chuyenTau=" + chuyenTau +
                ", tuyen=" + tuyen +
                ", gaDi=" + gaDi +
                ", gaDen=" + gaDen +
                ", gioKhoiHanh=" + gioKhoiHanh +
                ", gioDenDuKien=" + gioDenDuKien +
                ", trangThai=" + trangThai +
                '}';
    }
}



