/*
 * Entity Đơn Treo - Lưu thông tin đơn hàng tạm
 */
package entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Lưu thông tin đơn đặt vé tạm thời (chưa thanh toán)
 * @author PC
 */
public class DonTreoDat {
    private String maDonTreo;
    private String cccdNguoiDat;
    private String hoTenNguoiDat;
    private String sdtNguoiDat;
    private String emailNguoiDat;
    private LocalDateTime ngayLap;
    private LocalDateTime gioLap;
    private int soLuongVe;
    private double tongTien;
    private String ghiChu;
    
    // Thông tin hành trình
    private String gaDi;
    private String gaDen;
    private String ngayDi;
    private LichTrinh lichTrinh;
    
    // Danh sách vé (thông tin chi tiết)
    private List<ThongTinVeTam> danhSachVe;
    
    public DonTreoDat() {
        this.danhSachVe = new ArrayList<>();
        this.ngayLap = LocalDateTime.now();
        this.gioLap = LocalDateTime.now();
    }
    
    public DonTreoDat(String maDonTreo, String cccdNguoiDat, String hoTenNguoiDat, 
                      String sdtNguoiDat, int soLuongVe, double tongTien) {
        this();
        this.maDonTreo = maDonTreo;
        this.cccdNguoiDat = cccdNguoiDat;
        this.hoTenNguoiDat = hoTenNguoiDat;
        this.sdtNguoiDat = sdtNguoiDat;
        this.soLuongVe = soLuongVe;
        this.tongTien = tongTien;
    }

    // Getters and Setters
    public String getMaDonTreo() { return maDonTreo; }
    public void setMaDonTreo(String maDonTreo) { this.maDonTreo = maDonTreo; }

    public String getCccdNguoiDat() { return cccdNguoiDat; }
    public void setCccdNguoiDat(String cccdNguoiDat) { this.cccdNguoiDat = cccdNguoiDat; }

    public String getHoTenNguoiDat() { return hoTenNguoiDat; }
    public void setHoTenNguoiDat(String hoTenNguoiDat) { this.hoTenNguoiDat = hoTenNguoiDat; }

    public String getSdtNguoiDat() { return sdtNguoiDat; }
    public void setSdtNguoiDat(String sdtNguoiDat) { this.sdtNguoiDat = sdtNguoiDat; }

    public String getEmailNguoiDat() { return emailNguoiDat; }
    public void setEmailNguoiDat(String emailNguoiDat) { this.emailNguoiDat = emailNguoiDat; }

    public LocalDateTime getNgayLap() { return ngayLap; }
    public void setNgayLap(LocalDateTime ngayLap) { this.ngayLap = ngayLap; }

    public LocalDateTime getGioLap() { return gioLap; }
    public void setGioLap(LocalDateTime gioLap) { this.gioLap = gioLap; }

    public int getSoLuongVe() { return soLuongVe; }
    public void setSoLuongVe(int soLuongVe) { this.soLuongVe = soLuongVe; }

    public double getTongTien() { return tongTien; }
    public void setTongTien(double tongTien) { this.tongTien = tongTien; }

    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }

    public String getGaDi() { return gaDi; }
    public void setGaDi(String gaDi) { this.gaDi = gaDi; }

    public String getGaDen() { return gaDen; }
    public void setGaDen(String gaDen) { this.gaDen = gaDen; }

    public String getNgayDi() { return ngayDi; }
    public void setNgayDi(String ngayDi) { this.ngayDi = ngayDi; }

    public LichTrinh getLichTrinh() { return lichTrinh; }
    public void setLichTrinh(LichTrinh lichTrinh) { this.lichTrinh = lichTrinh; }

    public List<ThongTinVeTam> getDanhSachVe() { return danhSachVe; }
    public void setDanhSachVe(List<ThongTinVeTam> danhSachVe) { this.danhSachVe = danhSachVe; }
    
    public void themVe(ThongTinVeTam ve) {
        this.danhSachVe.add(ve);
    }
    
    /**
     * Kiểm tra đơn còn trong thời hạn 15 phút không
     */
    public boolean conTrongThoiHan() {
        if (ngayLap == null) return false;
        LocalDateTime thoiGianHetHan = ngayLap.plusMinutes(15);
        return LocalDateTime.now().isBefore(thoiGianHetHan);
    }
    
    /**
     * Tính số giây còn lại
     */
    public long getSoGiayConLai() {
        if (ngayLap == null) return 0;
        LocalDateTime thoiGianHetHan = ngayLap.plusMinutes(15);
        long giay = java.time.Duration.between(LocalDateTime.now(), thoiGianHetHan).getSeconds();
        return giay > 0 ? giay : 0;
    }
    
    /**
     * Format thời gian còn lại dạng MM:SS
     */
    public String getThoiGianConLaiFormatted() {
        long giay = getSoGiayConLai();
        long phut = giay / 60;
        long giayDu = giay % 60;
        return String.format("%02d:%02d", phut, giayDu);
    }
    
    /**
     * Class lưu thông tin vé tạm (trong đơn treo)
     */
    public static class ThongTinVeTam {
        private String soGiayTo;
        private String hoTen;
        private String doiTuong;
        private String thongTinCho;
        private double giaVe;
        private double giamGia;
        private double thanhTien;
        private ChoNgoi choNgoi;
        private LichTrinh lichTrinh; // ⚡ Thêm lịch trình cho từng vé (quan trọng với khứ hồi)
        
        public ThongTinVeTam() {}
        
        public ThongTinVeTam(String soGiayTo, String hoTen, String doiTuong, 
                            String thongTinCho, double giaVe, double giamGia, double thanhTien) {
            this.soGiayTo = soGiayTo;
            this.hoTen = hoTen;
            this.doiTuong = doiTuong;
            this.thongTinCho = thongTinCho;
            this.giaVe = giaVe;
            this.giamGia = giamGia;
            this.thanhTien = thanhTien;
        }

        // Getters and Setters
        public String getSoGiayTo() { return soGiayTo; }
        public void setSoGiayTo(String soGiayTo) { this.soGiayTo = soGiayTo; }

        public String getHoTen() { return hoTen; }
        public void setHoTen(String hoTen) { this.hoTen = hoTen; }

        public String getDoiTuong() { return doiTuong; }
        public void setDoiTuong(String doiTuong) { this.doiTuong = doiTuong; }

        public String getThongTinCho() { return thongTinCho; }
        public void setThongTinCho(String thongTinCho) { this.thongTinCho = thongTinCho; }

        public double getGiaVe() { return giaVe; }
        public void setGiaVe(double giaVe) { this.giaVe = giaVe; }

        public double getGiamGia() { return giamGia; }
        public void setGiamGia(double giamGia) { this.giamGia = giamGia; }

        public double getThanhTien() { return thanhTien; }
        public void setThanhTien(double thanhTien) { this.thanhTien = thanhTien; }

        public ChoNgoi getChoNgoi() { return choNgoi; }
        public void setChoNgoi(ChoNgoi choNgoi) { this.choNgoi = choNgoi; }

        public LichTrinh getLichTrinh() { return lichTrinh; }
        public void setLichTrinh(LichTrinh lichTrinh) { this.lichTrinh = lichTrinh; }
    }
}

