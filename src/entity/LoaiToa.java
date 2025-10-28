/*
 * @ (#) LoaiToa.java          1.0        10/26/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package entity;

/**
 * @description: Entity Loại Toa
 * @author: Truong Tran Hung
 * @date: 10/26/2025
 * @version: 1.0
 */
public class LoaiToa {
    private String maLoaiToa;
    private String tenLoaiToa;

    public LoaiToa() {}

    public LoaiToa(String maLoaiToa, String tenLoaiToa) {
        this.maLoaiToa = maLoaiToa;
        this.tenLoaiToa = tenLoaiToa;
    }

    public String getMaLoaiToa() {
        return maLoaiToa;
    }

    public void setMaLoaiToa(String maLoaiToa) {
        this.maLoaiToa = maLoaiToa;
    }

    public String getTenLoaiToa() {
        return tenLoaiToa;
    }

    public void setTenLoaiToa(String tenLoaiToa) {
        this.tenLoaiToa = tenLoaiToa;
    }

    @Override
    public String toString() {
        return "LoaiToa{" +
                "maLoaiToa='" + maLoaiToa + '\'' +
                ", tenLoaiToa='" + tenLoaiToa + '\'' +
                '}';
    }
}


