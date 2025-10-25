/*
 * @ (#) Ve.java          1.0        10/25/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package entity;

/*
 * @description:
 * @author: Truong Tran Hung
 * @date: 10/25/2025
 * @version:    1.0
 */
import java.time.LocalDateTime;

public class Ve {
    private String maVe;
    private String cccd;
    private String tenKhachHang;
    private String doiTuong;
    private String gaDi;
    private String gaDen;
    private String maTau;
    private int soToa;
    private String viTriCho;
    private LocalDateTime thoiGianLenTau;
    private double gia;

    public Ve() {}

    public Ve(String maVe, String cccd, String tenKhachHang, String doiTuong,
              String gaDi, String gaDen, String maTau, int soToa, String viTriCho,
              LocalDateTime thoiGianLenTau, double gia) {
        this.maVe = maVe;
        this.cccd = cccd;
        this.tenKhachHang = tenKhachHang;
        this.doiTuong = doiTuong;
        this.gaDi = gaDi;
        this.gaDen = gaDen;
        this.maTau = maTau;
        this.soToa = soToa;
        this.viTriCho = viTriCho;
        this.thoiGianLenTau = thoiGianLenTau;
        this.gia = gia;
    }

    // --- Getters / Setters ---
    public String getMaVe() { return maVe; }
    public void setMaVe(String maVe) { this.maVe = maVe; }

    public String getCccd() { return cccd; }
    public void setCccd(String cccd) { this.cccd = cccd; }

    public String getTenKhachHang() { return tenKhachHang; }
    public void setTenKhachHang(String tenKhachHang) { this.tenKhachHang = tenKhachHang; }

    public String getDoiTuong() { return doiTuong; }
    public void setDoiTuong(String doiTuong) { this.doiTuong = doiTuong; }

    public String getGaDi() { return gaDi; }
    public void setGaDi(String gaDi) { this.gaDi = gaDi; }

    public String getGaDen() { return gaDen; }
    public void setGaDen(String gaDen) { this.gaDen = gaDen; }

    public String getMaTau() { return maTau; }
    public void setMaTau(String maTau) { this.maTau = maTau; }

    public int getSoToa() { return soToa; }
    public void setSoToa(int soToa) { this.soToa = soToa; }

    public String getViTriCho() { return viTriCho; }
    public void setViTriCho(String viTriCho) { this.viTriCho = viTriCho; }

    public LocalDateTime getThoiGianLenTau() { return thoiGianLenTau; }
    public void setThoiGianLenTau(LocalDateTime thoiGianLenTau) { this.thoiGianLenTau = thoiGianLenTau; }

    public double getGia() { return gia; }
    public void setGia(double gia) { this.gia = gia; }
}
