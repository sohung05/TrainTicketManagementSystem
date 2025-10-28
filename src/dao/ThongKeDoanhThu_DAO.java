package dao;

import connectDB.connectDB;
import entity.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ThongKeDoanhThu_DAO {

    // ✅ Lấy toàn bộ hóa đơn
    public List<HoaDon> loadAllHoaDon() {
        List<HoaDon> dsHoaDon = new ArrayList<>();

        try {
            Connection con = connectDB.getConnection();
            if (con == null) {
                connectDB.getConnection();
                con = connectDB.getConnection();
            }
            System.out.println(">>> Đang dùng kết nối: " + con);

            String sql = "SELECT * FROM HoaDon";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                String maHoaDon = rs.getString("maHoaDon");

                NhanVien nv = new NhanVien();
                nv.setMaNhanVien(rs.getString("maNhanVien"));

                KhachHang kh = new KhachHang();
                kh.setMaKH(rs.getString("maKH"));

                LocalDateTime gioTao = null;
                LocalDateTime ngayTao = null;

                try {
                    Timestamp ts1 = rs.getTimestamp("gioTao");
                    if (ts1 != null) gioTao = ts1.toLocalDateTime();
                } catch (Exception ex) {
                    System.out.println("Lỗi chuyển gioTao: " + ex.getMessage());
                }

                try {
                    Timestamp ts2 = rs.getTimestamp("ngayTao");
                    if (ts2 != null) ngayTao = ts2.toLocalDateTime();
                } catch (Exception ex) {
                    System.out.println("Lỗi chuyển ngayTao: " + ex.getMessage());
                }

                boolean trangThai = rs.getBoolean("trangThai");
                HoaDon hd = new HoaDon(maHoaDon, nv, kh, gioTao, ngayTao, trangThai);
                dsHoaDon.add(hd);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("Tổng số hóa đơn đọc được: " + dsHoaDon.size());
        return dsHoaDon;
    }


    // ✅ Lọc doanh thu theo tháng/năm
    public List<HoaDon> loadHoaDonTheoThangNam(int thang, int nam) {
        connectDB.getConnection(); // đảm bảo có kết nối
        List<HoaDon> dsHoaDon = new ArrayList<>();

        String sqlHoaDon = "SELECT * FROM HoaDon WHERE MONTH(ngayTao) = ? AND YEAR(ngayTao) = ?";

        try (Connection con = connectDB.getConnection();
             PreparedStatement psHD = con.prepareStatement(sqlHoaDon)) {

            psHD.setInt(1, thang);
            psHD.setInt(2, nam);
            ResultSet rsHD = psHD.executeQuery();

            while (rsHD.next()) {
                String maHoaDon = rsHD.getString("maHoaDon");

                // Tạo nhân viên
                NhanVien nv = new NhanVien();
                nv.setMaNhanVien(rsHD.getString("maNhanVien"));

                // Tạo khách hàng
                KhachHang kh = new KhachHang();
                kh.setMaKH(rsHD.getString("maKH"));

                // Chuyển Timestamp sang LocalDateTime
                Timestamp gioTaoTS = rsHD.getTimestamp("gioTao");
                Timestamp ngayTaoTS = rsHD.getTimestamp("ngayTao");
                LocalDateTime gioTao = gioTaoTS != null ? gioTaoTS.toLocalDateTime() : null;
                LocalDateTime ngayTao = ngayTaoTS != null ? ngayTaoTS.toLocalDateTime() : null;
                boolean trangThai = rsHD.getBoolean("trangThai");

                HoaDon hd = new HoaDon(maHoaDon, nv, kh, gioTao, ngayTao, trangThai);

                // Load chi tiết hóa đơn cho HoaDon này
                String sqlCTHD = "SELECT * FROM ChiTietHoaDon WHERE maHoaDon = ?";
                try (PreparedStatement psCT = con.prepareStatement(sqlCTHD)) {
                    psCT.setString(1, maHoaDon);
                    ResultSet rsCT = psCT.executeQuery();

                    while (rsCT.next()) {
                        String maHD = rsCT.getString("maHoaDon");
                        String maVe = rsCT.getString("maVe");
                        int soLuong = rsCT.getInt("soLuong");
                        double giaVe = rsCT.getDouble("giaVe");
                        double mucGiam = rsCT.getDouble("mucGiam");

                        // Tạo đối tượng ChiTietHoaDon
                        ChiTietHoaDon cthd = new ChiTietHoaDon(maHD, hd, new Ve(maVe), soLuong, giaVe, mucGiam);
                        hd.themChiTiet(cthd);
                    }
                }

                dsHoaDon.add(hd);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dsHoaDon;
    }
    // ✅ Tổng lượt khách = tổng số vé đã bán trong tháng/năm
    public int getTongLuongKhach(int thang, int nam) {
        int tongKhach = 0;
        String sql = """
        SELECT COUNT(*) AS tong
        FROM Ve v
        JOIN HoaDon hd ON v.maKH = hd.maKH
        WHERE MONTH(hd.ngayTao)=? AND YEAR(hd.ngayTao)=?
    """;

        try (Connection con = connectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, thang);
            ps.setInt(2, nam);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) tongKhach = rs.getInt("tong");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tongKhach;
    }

    // ✅ Tổng số vé đã đặt (tương tự lượt khách, nhưng dùng số vé)
    public int getTongSoVeDat(int thang, int nam) {
        int tongVe = 0;
        String sql = """
        SELECT COUNT(maVe) AS tong
        FROM Ve v
        JOIN HoaDon hd ON v.maKH = hd.maKH
        WHERE MONTH(hd.ngayTao)=? AND YEAR(hd.ngayTao)=?
    """;

        try (Connection con = connectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, thang);
            ps.setInt(2, nam);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) tongVe = rs.getInt("tong");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tongVe;
    }

    // ✅ Tổng số chuyến = tổng số lịch trình trong tháng
    public int getTongSoChuyen(int thang, int nam) {
        int tongChuyen = 0;
        String sql = """
        SELECT COUNT(DISTINCT v.maLichTrinh) AS tong
        FROM Ve v
        JOIN HoaDon hd ON v.maKH = hd.maKH
        WHERE MONTH(hd.ngayTao)=? AND YEAR(hd.ngayTao)=?
    """;

        try (Connection con = connectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, thang);
            ps.setInt(2, nam);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) tongChuyen = rs.getInt("tong");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tongChuyen;
    }
    public double getTongDoanhThu(int thang, int nam) {
        double tongDoanhThu = 0;
        String sql = """
        SELECT SUM(ct.thanhTien) AS tong
        FROM ChiTietHoaDon ct
        JOIN HoaDon hd ON ct.maHoaDon = hd.maHoaDon
        WHERE MONTH(hd.ngayTao) = ? AND YEAR(hd.ngayTao) = ? AND hd.trangThai = 1
    """;

        try (Connection con = connectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, thang);
            ps.setInt(2, nam);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) tongDoanhThu = rs.getDouble("tong");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tongDoanhThu;
    }




}
