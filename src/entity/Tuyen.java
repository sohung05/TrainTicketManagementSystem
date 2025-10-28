/*
 * @ (#) Tuyen.java          1.0        10/26/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package entity;

/**
 * @description: Entity Tuyến đường tàu
 * @author: Truong Tran Hung
 * @date: 10/26/2025
 * @version: 1.0
 */
public class Tuyen {
    private String maTuyen;
    private String tenTuyen;
    private double doDai;  // km

    public Tuyen() {}

    public Tuyen(String maTuyen, String tenTuyen, double doDai) {
        this.maTuyen = maTuyen;
        this.tenTuyen = tenTuyen;
        this.doDai = doDai;
    }

    public String getMaTuyen() {
        return maTuyen;
    }

    public void setMaTuyen(String maTuyen) {
        this.maTuyen = maTuyen;
    }

    public String getTenTuyen() {
        return tenTuyen;
    }

    public void setTenTuyen(String tenTuyen) {
        this.tenTuyen = tenTuyen;
    }

    public double getDoDai() {
        return doDai;
    }

    public void setDoDai(double doDai) {
        this.doDai = doDai;
    }

    @Override
    public String toString() {
        return "Tuyen{" +
                "maTuyen='" + maTuyen + '\'' +
                ", tenTuyen='" + tenTuyen + '\'' +
                ", doDai=" + doDai +
                '}';
    }
}


