/*
 * @ (#) LoaiVe.java          1.0        10/26/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package entity;

/**
 * @description: Entity Loại Vé
 * @author: Truong Tran Hung
 * @date: 10/26/2025
 * @version: 1.0
 */
public class LoaiVe {
    private String maLoaiVe;
    private String tenLoaiVe;
    private float mucGiamGia;  // % giảm giá

    public LoaiVe() {}

    public LoaiVe(String maLoaiVe, String tenLoaiVe, float mucGiamGia) {
        this.maLoaiVe = maLoaiVe;
        this.tenLoaiVe = tenLoaiVe;
        this.mucGiamGia = mucGiamGia;
    }

    public String getMaLoaiVe() {
        return maLoaiVe;
    }

    public void setMaLoaiVe(String maLoaiVe) {
        this.maLoaiVe = maLoaiVe;
    }

    public String getTenLoaiVe() {
        return tenLoaiVe;
    }

    public void setTenLoaiVe(String tenLoaiVe) {
        this.tenLoaiVe = tenLoaiVe;
    }

    public float getMucGiamGia() {
        return mucGiamGia;
    }

    public void setMucGiamGia(float mucGiamGia) {
        this.mucGiamGia = mucGiamGia;
    }

    @Override
    public String toString() {
        return "LoaiVe{" +
                "maLoaiVe='" + maLoaiVe + '\'' +
                ", tenLoaiVe='" + tenLoaiVe + '\'' +
                ", mucGiamGia=" + mucGiamGia +
                '}';
    }
}


