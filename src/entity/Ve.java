/*
 * @ (#) Ve.java          1.0        10/25/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package entity;

/*
 * @description: Entity Vé
 * @author: Truong Tran Hung
 * @date: 10/25/2025
 * @version: 2.0
 */
import java.time.LocalDateTime;

public class Ve {
    // --- Fields chính (theo DB schema) ---
    private String maVe;
    private LoaiVe loaiVe;          // FK -> LoaiVe
    private String maVach;          // Barcode/QR code
    private LocalDateTime thoiGianLenTau;
    private double giaVe;
    private KhachHang khachHang;    // FK -> KhachHang
    private ChoNgoi choNgoi;        // FK -> ChoNgoi
    private LichTrinh lichTrinh;    // FK -> LichTrinh
    private boolean trangThai;      // 1 = Còn hiệu lực, 0 = Đã hủy
    
    // --- Fields tạm để backward compatibility ---
    private String tenKhachHang;    // Tên hành khách trên vé
    private String soCCCD;          // Số CCCD/giấy tờ
    
    // --- Constructor rỗng ---
    public Ve() {}

    // --- Constructor đầy đủ (theo DB) ---
    public Ve(String maVe, LoaiVe loaiVe, String maVach, LocalDateTime thoiGianLenTau,
              double giaVe, KhachHang khachHang, ChoNgoi choNgoi, LichTrinh lichTrinh,
              boolean trangThai, String tenKhachHang, String soCCCD) {
        this.maVe = maVe;
        this.loaiVe = loaiVe;
        this.maVach = maVach;
        this.thoiGianLenTau = thoiGianLenTau;
        this.giaVe = giaVe;
        this.khachHang = khachHang;
        this.choNgoi = choNgoi;
        this.lichTrinh = lichTrinh;
        this.trangThai = trangThai;
        this.tenKhachHang = tenKhachHang;
        this.soCCCD = soCCCD;
    }

    // --- Getters / Setters ---
    public String getMaVe() { return maVe; }
    public void setMaVe(String maVe) { this.maVe = maVe; }

    public LoaiVe getLoaiVe() { return loaiVe; }
    public void setLoaiVe(LoaiVe loaiVe) { this.loaiVe = loaiVe; }

    public String getMaVach() { return maVach; }
    public void setMaVach(String maVach) { this.maVach = maVach; }

    public LocalDateTime getThoiGianLenTau() { return thoiGianLenTau; }
    public void setThoiGianLenTau(LocalDateTime thoiGianLenTau) { this.thoiGianLenTau = thoiGianLenTau; }

    public double getGiaVe() { return giaVe; }
    public void setGiaVe(double giaVe) { this.giaVe = giaVe; }

    public KhachHang getKhachHang() { return khachHang; }
    public void setKhachHang(KhachHang khachHang) { this.khachHang = khachHang; }

    public ChoNgoi getChoNgoi() { return choNgoi; }
    public void setChoNgoi(ChoNgoi choNgoi) { this.choNgoi = choNgoi; }

    public LichTrinh getLichTrinh() { return lichTrinh; }
    public void setLichTrinh(LichTrinh lichTrinh) { this.lichTrinh = lichTrinh; }

    public boolean isTrangThai() { return trangThai; }
    public void setTrangThai(boolean trangThai) { this.trangThai = trangThai; }

    public String getTenKhachHang() { return tenKhachHang; }
    public void setTenKhachHang(String tenKhachHang) { this.tenKhachHang = tenKhachHang; }

    public String getSoCCCD() { return soCCCD; }
    public void setSoCCCD(String soCCCD) { this.soCCCD = soCCCD; }
    
    // --- Helper methods cho backward compatibility ---
    public String getCccd() { 
        return soCCCD != null ? soCCCD : (khachHang != null ? khachHang.getCccd() : null);
    }
    
    public void setCccd(String cccd) { 
        this.soCCCD = cccd; 
    }
    
    public double getGia() { 
        return giaVe; 
    }
    
    public void setGia(double gia) { 
        this.giaVe = gia; 
    }
    
    public String getDoiTuong() {
        return loaiVe != null ? loaiVe.getTenLoaiVe() : null;
    }
    
    public void setDoiTuong(String doiTuong) {
        // Backward compatibility - không cần implement
    }
    
    public String getGaDi() {
        return lichTrinh != null && lichTrinh.getGaDi() != null ? 
               lichTrinh.getGaDi().getTenGa() : null;
    }
    
    public void setGaDi(String gaDi) {
        // Backward compatibility - không cần implement
    }
    
    public String getGaDen() {
        return lichTrinh != null && lichTrinh.getGaDen() != null ? 
               lichTrinh.getGaDen().getTenGa() : null;
    }
    
    public void setGaDen(String gaDen) {
        // Backward compatibility - không cần implement
    }
    
    public String getMaTau() {
        return lichTrinh != null && lichTrinh.getChuyenTau() != null ? 
               lichTrinh.getChuyenTau().getSoHieuTau() : null;
    }
    
    public void setMaTau(String maTau) {
        // Backward compatibility - không cần implement
    }
    
    public int getSoToa() {
        return choNgoi != null && choNgoi.getToa() != null ? 
               choNgoi.getToa().getSoToa() : 0;
    }
    
    public void setSoToa(int soToa) {
        // Backward compatibility - không cần implement
    }
    
    public String getViTriCho() {
        return choNgoi != null ? choNgoi.getMaChoNgoi() : null;
    }
    
    public void setViTriCho(String viTriCho) {
        // Backward compatibility - không cần implement
    }
    
    @Override
    public String toString() {
        return "Ve{" +
                "maVe='" + maVe + '\'' +
                ", loaiVe=" + (loaiVe != null ? loaiVe.getMaLoaiVe() : "null") +
                ", maVach='" + maVach + '\'' +
                ", thoiGianLenTau=" + thoiGianLenTau +
                ", giaVe=" + giaVe +
                ", khachHang=" + (khachHang != null ? khachHang.getMaKH() : "null") +
                ", choNgoi=" + (choNgoi != null ? choNgoi.getMaChoNgoi() : "null") +
                ", lichTrinh=" + (lichTrinh != null ? lichTrinh.getMaLichTrinh() : "null") +
                ", trangThai=" + trangThai +
                ", tenKhachHang='" + tenKhachHang + '\'' +
                ", soCCCD='" + soCCCD + '\'' +
                '}';
    }
}
