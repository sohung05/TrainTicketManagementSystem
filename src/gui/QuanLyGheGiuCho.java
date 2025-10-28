package gui;

import entity.GheGiuCho;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

/**
 * Quản lý danh sách ghế đang được giữ chỗ (5 phút)
 */
public class QuanLyGheGiuCho {
    private static List<GheGiuCho> danhSachGheGiuCho = new ArrayList<>();
    private static Timer timer = new Timer(true); // Daemon thread
    
    /**
     * Thêm ghế vào danh sách giữ chỗ
     */
    public static void themGheGiuCho(String maChoNgoi, String maDonTreo) {
        GheGiuCho gheGiuCho = new GheGiuCho(maChoNgoi, maDonTreo);
        danhSachGheGiuCho.add(gheGiuCho);
        
        // Tạo task tự động xóa sau 5 phút
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                xoaGheGiuCho(maChoNgoi);
                System.out.println("Đã hết hạn giữ chỗ: " + maChoNgoi);
            }
        }, 5 * 60 * 1000); // 5 phút = 5 * 60 * 1000 milliseconds
    }
    
    /**
     * Kiểm tra ghế có đang được giữ chỗ không
     */
    public static boolean kiemTraGheDangGiuCho(String maChoNgoi) {
        // Xóa các ghế đã hết hạn trước
        xoaGheHetHan();
        
        return danhSachGheGiuCho.stream()
            .anyMatch(ghe -> ghe.getMaChoNgoi().equals(maChoNgoi) && ghe.conTrongThoiGianGiuCho());
    }
    
    /**
     * Xóa ghế khỏi danh sách giữ chỗ
     */
    public static void xoaGheGiuCho(String maChoNgoi) {
        danhSachGheGiuCho.removeIf(ghe -> ghe.getMaChoNgoi().equals(maChoNgoi));
    }
    
    /**
     * Xóa tất cả ghế của một đơn treo
     */
    public static void xoaTatCaGheCuaDonTreo(String maDonTreo) {
        danhSachGheGiuCho.removeIf(ghe -> ghe.getMaDonTreo() != null && ghe.getMaDonTreo().equals(maDonTreo));
    }
    
    /**
     * Gia hạn thời gian giữ chỗ cho các ghế của đơn treo (thêm 5 phút nữa)
     */
    public static void giaHanGheCuaDonTreo(String maDonTreo) {
        for (GheGiuCho ghe : danhSachGheGiuCho) {
            if (ghe.getMaDonTreo() != null && ghe.getMaDonTreo().equals(maDonTreo)) {
                ghe.giaHanThoiGian(5); // Gia hạn thêm 5 phút
            }
        }
        System.out.println("✅ Đã gia hạn ghế giữ chỗ cho đơn: " + maDonTreo);
    }
    
    /**
     * Xóa các ghế đã hết hạn giữ chỗ
     */
    public static void xoaGheHetHan() {
        danhSachGheGiuCho.removeIf(ghe -> !ghe.conTrongThoiGianGiuCho());
    }
    
    /**
     * Lấy danh sách ghế đang giữ chỗ
     */
    public static List<GheGiuCho> layDanhSachGheGiuCho() {
        xoaGheHetHan();
        return new ArrayList<>(danhSachGheGiuCho);
    }
    
    /**
     * Đếm số ghế đang giữ chỗ
     */
    public static int demSoGheGiuCho() {
        xoaGheHetHan();
        return danhSachGheGiuCho.size();
    }
}

