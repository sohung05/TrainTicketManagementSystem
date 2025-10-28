/*
 * @ (#) Ga.java          1.0        10/26/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package entity;

/**
 * @description: Entity Ga tàu
 * @author: Truong Tran Hung
 * @date: 10/26/2025
 * @version: 1.0
 */
public class Ga {
    private String maGa;
    private String tenGa;
    private String viTri;
    private String gaDi;    // SQLTaoDULieu có cột này
    private String gaDen;   // SQLTaoDULieu có cột này

    public Ga() {}

    public Ga(String maGa, String tenGa, String viTri) {
        this.maGa = maGa;
        this.tenGa = tenGa;
        this.viTri = viTri;
    }
    
    public Ga(String maGa, String tenGa, String viTri, String gaDi, String gaDen) {
        this.maGa = maGa;
        this.tenGa = tenGa;
        this.viTri = viTri;
        this.gaDi = gaDi;
        this.gaDen = gaDen;
    }

    public String getMaGa() {
        return maGa;
    }

    public void setMaGa(String maGa) {
        this.maGa = maGa;
    }

    public String getTenGa() {
        return tenGa;
    }

    public void setTenGa(String tenGa) {
        this.tenGa = tenGa;
    }

    public String getViTri() {
        return viTri;
    }

    public void setViTri(String viTri) {
        this.viTri = viTri;
    }

    public String getGaDi() {
        return gaDi;
    }

    public void setGaDi(String gaDi) {
        this.gaDi = gaDi;
    }

    public String getGaDen() {
        return gaDen;
    }

    public void setGaDen(String gaDen) {
        this.gaDen = gaDen;
    }

    @Override
    public String toString() {
        return "Ga{" +
                "maGa='" + maGa + '\'' +
                ", tenGa='" + tenGa + '\'' +
                ", viTri='" + viTri + '\'' +
                ", gaDi='" + gaDi + '\'' +
                ", gaDen='" + gaDen + '\'' +
                '}';
    }
}


