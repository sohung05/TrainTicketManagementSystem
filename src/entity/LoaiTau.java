/*
 * @ (#) LoaiTau.java          1.0        10/26/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package entity;

/**
 * @description: Entity Loại Tàu
 * @author: Truong Tran Hung
 * @date: 10/26/2025
 * @version: 1.0
 */
public class LoaiTau {
    private String maLoaiTau;
    private String tenLoaiTau;

    public LoaiTau() {}

    public LoaiTau(String maLoaiTau, String tenLoaiTau) {
        this.maLoaiTau = maLoaiTau;
        this.tenLoaiTau = tenLoaiTau;
    }

    public String getMaLoaiTau() {
        return maLoaiTau;
    }

    public void setMaLoaiTau(String maLoaiTau) {
        this.maLoaiTau = maLoaiTau;
    }

    public String getTenLoaiTau() {
        return tenLoaiTau;
    }

    public void setTenLoaiTau(String tenLoaiTau) {
        this.tenLoaiTau = tenLoaiTau;
    }

    @Override
    public String toString() {
        return "LoaiTau{" +
                "maLoaiTau='" + maLoaiTau + '\'' +
                ", tenLoaiTau='" + tenLoaiTau + '\'' +
                '}';
    }
}


