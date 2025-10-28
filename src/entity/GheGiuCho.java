package entity;

import java.time.LocalDateTime;

/**
 * Entity để lưu thông tin ghế đang được giữ chỗ (5 phút)
 */
public class GheGiuCho {
    private String maChoNgoi;
    private String maDonTreo;
    private LocalDateTime thoiGianGiuCho;
    private static final int PHUT_GIU_CHO = 5;
    
    public GheGiuCho(String maChoNgoi, String maDonTreo) {
        this.maChoNgoi = maChoNgoi;
        this.maDonTreo = maDonTreo;
        this.thoiGianGiuCho = LocalDateTime.now();
    }
    
    /**
     * Kiểm tra ghế còn trong thời gian giữ chỗ không
     */
    public boolean conTrongThoiGianGiuCho() {
        LocalDateTime thoiGianHetHan = thoiGianGiuCho.plusMinutes(PHUT_GIU_CHO);
        return LocalDateTime.now().isBefore(thoiGianHetHan);
    }
    
    /**
     * Tính số giây còn lại
     */
    public long getSoGiayConLai() {
        LocalDateTime thoiGianHetHan = thoiGianGiuCho.plusMinutes(PHUT_GIU_CHO);
        return java.time.Duration.between(LocalDateTime.now(), thoiGianHetHan).getSeconds();
    }
    
    /**
     * Gia hạn thời gian giữ chỗ thêm X phút
     */
    public void giaHanThoiGian(int themPhut) {
        this.thoiGianGiuCho = this.thoiGianGiuCho.plusMinutes(themPhut);
    }

    public String getMaChoNgoi() {
        return maChoNgoi;
    }

    public void setMaChoNgoi(String maChoNgoi) {
        this.maChoNgoi = maChoNgoi;
    }

    public String getMaDonTreo() {
        return maDonTreo;
    }

    public void setMaDonTreo(String maDonTreo) {
        this.maDonTreo = maDonTreo;
    }

    public LocalDateTime getThoiGianGiuCho() {
        return thoiGianGiuCho;
    }

    public void setThoiGianGiuCho(LocalDateTime thoiGianGiuCho) {
        this.thoiGianGiuCho = thoiGianGiuCho;
    }
}


