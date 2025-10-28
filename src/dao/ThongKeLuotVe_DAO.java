package dao;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import connectDB.connectDB;
import entity.*;

public class ThongKeLuotVe_DAO {

    // Hàm tiện ích đảm bảo kết nối luôn mở
    private Connection ensureConnection() throws SQLException {
        Connection con = connectDB.getConnection();
        if (con == null || con.isClosed()) {
            connectDB.getConnection();
            con = connectDB.getConnection();
        }
        return con;
    }

    public int getTongLuotKhach(int thang, int nam) {
        int tong = 0;
        String sql = """
            SELECT COUNT(ct.maVe) AS tongLuot
            FROM ChiTietHoaDon ct
            JOIN HoaDon hd ON ct.maHoaDon = hd.maHoaDon
            WHERE MONTH(hd.ngayTao) = ? AND YEAR(hd.ngayTao) = ? AND hd.trangThai = 1
        """;

        try (Connection con = ensureConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, thang);
            ps.setInt(2, nam);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) tong = rs.getInt("tongLuot");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tong;
    }

    public int getTongSoVe(int thang, int nam) {
        int tong = 0;
        String sql = """
            SELECT COUNT(DISTINCT ct.maVe) AS tongVe
            FROM ChiTietHoaDon ct
            JOIN HoaDon hd ON ct.maHoaDon = hd.maHoaDon
            WHERE MONTH(hd.ngayTao) = ? AND YEAR(hd.ngayTao) = ? AND hd.trangThai = 1
        """;

        try (Connection con = ensureConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, thang);
            ps.setInt(2, nam);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) tong = rs.getInt("tongVe");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tong;
    }

    public int getTongSoTuyen(int thang, int nam) {
        int tong = 0;
        String sql = """
            SELECT COUNT(DISTINCT v.maLichTrinh) AS tongTuyen
            FROM ChiTietHoaDon ct
            JOIN HoaDon hd ON ct.maHoaDon = hd.maHoaDon
            JOIN Ve v ON ct.maVe = v.maVe
            WHERE MONTH(hd.ngayTao) = ? AND YEAR(hd.ngayTao) = ? AND hd.trangThai = 1
        """;

        try (Connection con = ensureConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, thang);
            ps.setInt(2, nam);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) tong = rs.getInt("tongTuyen");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tong;
    }

    public List<Object[]> getTuyenNhieuNhatTrongThang(int thang, int nam) {
        List<Object[]> list = new ArrayList<>();
        String sql = """
            SELECT v.maLichTrinh, COUNT(ct.maVe) AS soLuot
            FROM ChiTietHoaDon ct
            JOIN HoaDon hd ON ct.maHoaDon = hd.maHoaDon
            JOIN Ve v ON ct.maVe = v.maVe
            WHERE MONTH(hd.ngayTao) = ? AND YEAR(hd.ngayTao) = ? AND hd.trangThai = 1
            GROUP BY v.maLichTrinh
            ORDER BY soLuot DESC
        """;

        try (Connection con = ensureConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, thang);
            ps.setInt(2, nam);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String maTuyen = rs.getString("maLichTrinh");
                    int soLuot = rs.getInt("soLuot");
                    list.add(new Object[]{maTuyen, soLuot});
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public Map<DoiTuong, Integer> getTiLeKhachHangTheoDoiTuong(int thang, int nam) {
        Map<DoiTuong, Integer> map = new HashMap<>();
        String sql = """
        SELECT kh.doiTuong, SUM(ct.soLuong) AS tong
        FROM ChiTietHoaDon ct
        JOIN HoaDon hd ON ct.maHoaDon = hd.maHoaDon
        JOIN Ve v ON ct.maVe = v.maVe
        JOIN KhachHang kh ON v.maKH = kh.maKH
        WHERE MONTH(hd.ngayTao) = ? AND YEAR(hd.ngayTao) = ? AND hd.trangThai = 1
        GROUP BY kh.doiTuong
    """;

        try (Connection con = ensureConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, thang);
            ps.setInt(2, nam);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String doiTuongStr = rs.getString("doiTuong");
                    int tong = rs.getInt("tong");
                    DoiTuong doiTuong = DoiTuong.fromString(doiTuongStr);
                    map.put(doiTuong, tong);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }
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

}