package dao;

import connectDB.connectDB;
import entity.ChiTietHoaDon;
import entity.HoaDon;
import entity.KhachHang;
import entity.NhanVien;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ThongKeDoanhThu_DAO {

    public List<HoaDon> loadHoaDonTheoThangNam(int thang, int nam) {
        List<HoaDon> ds = new ArrayList<>();
        String sql = "SELECT * FROM HoaDon WHERE MONTH(ngayTao) = ? AND YEAR(ngayTao) = ? ORDER BY ngayTao ASC";

        try (Connection conn = connectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, thang);
            ps.setInt(2, nam);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {

                    String maHD = rs.getString("maHoaDon");
                    NhanVien nv = new NhanVien(rs.getString("maNhanVien"));
                    KhachHang kh = new KhachHang(rs.getString("maKH"));

                    LocalDateTime ngayTao = rs.getTimestamp("ngayTao") != null ?
                            rs.getTimestamp("ngayTao").toLocalDateTime() : null;
                    LocalDateTime gioTao = rs.getTimestamp("gioTao") != null ?
                            rs.getTimestamp("gioTao").toLocalDateTime() : null;

                    boolean trangThai = rs.getBoolean("trangThai");
                    HoaDon hd = new HoaDon(maHD, nv, kh, gioTao, ngayTao, trangThai);

                    // **Truyền Connection đang mở vào load chi tiết**
                    hd.setDanhSachChiTiet(loadChiTietHoaDon(conn, maHD));

                    ds.add(hd);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ds;
    }

    // Sửa load chi tiết để nhận Connection từ ngoài
    private List<ChiTietHoaDon> loadChiTietHoaDon(Connection conn, String maHD) {
        List<ChiTietHoaDon> list = new ArrayList<>();
        String sql = "SELECT * FROM ChiTietHoaDon WHERE maHoaDon = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maHD);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ChiTietHoaDon ct = new ChiTietHoaDon();
                    ct.setMaHoaDon(maHD);
                    ct.setSoLuong(rs.getInt("soLuong"));
                    ct.setGiaVe(rs.getDouble("giaVe"));
                    ct.setMucGiam(rs.getDouble("mucGiam"));
                    list.add(ct);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // ==========================
    // 3. TÍNH TỔNG DOANH THU TRONG THÁNG
    // ==========================
    public double getTongDoanhThu(int thang, int nam) {
        String sql = """
                SELECT SUM(ct.soLuong * ct.giaVe * (1 - ct.mucGiam / 100.0)) AS doanhThu
                FROM HoaDon hd
                JOIN ChiTietHoaDon ct ON hd.maHoaDon = ct.maHoaDon
                WHERE MONTH(hd.ngayTao) = ? AND YEAR(hd.ngayTao) = ? AND hd.trangThai = 1
            """;

        try (Connection conn = connectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, thang);
            ps.setInt(2, nam);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("doanhThu");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    // ==========================
    // 4. TỔNG SỐ VÉ ĐÃ ĐẶT TRONG THÁNG
    // ==========================
    public int getTongSoVe(int thang, int nam) {
        String sql = """
                SELECT SUM(ct.soLuong) AS tongVe
                FROM HoaDon hd
                JOIN ChiTietHoaDon ct ON hd.maHoaDon = ct.maHoaDon
                WHERE MONTH(hd.ngayTao) = ? AND YEAR(hd.ngayTao) = ? AND hd.trangThai = 1
            """;

        try (Connection conn = connectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, thang);
            ps.setInt(2, nam);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("tongVe");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    // ==========================
    // 5. TỔNG LƯỢT KHÁCH TRONG THÁNG
    // ==========================
    public int getTongLuongKhach(int thang, int nam) {
        String sql = """
                SELECT COUNT(DISTINCT hd.maKH) AS tongKhach
                FROM HoaDon hd
                WHERE MONTH(hd.ngayTao) = ? AND YEAR(hd.ngayTao) = ? AND hd.trangThai = 1
            """;

        try (Connection conn = connectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, thang);
            ps.setInt(2, nam);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("tongKhach");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    // ==========================
    // 6. TỔNG SỐ CHUYẾN TRONG THÁNG
    // ==========================
    public int getTongSoChuyen(int thang, int nam) {
        String sql = """
                SELECT COUNT(DISTINCT ct.maLichTrinh) AS tongChuyen
                FROM HoaDon hd
                JOIN ChiTietHoaDon ct ON hd.maHoaDon = ct.maHoaDon
                WHERE MONTH(hd.ngayTao) = ? AND YEAR(hd.ngayTao) = ? AND hd.trangThai = 1
            """;

        try (Connection conn = connectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, thang);
            ps.setInt(2, nam);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("tongChuyen");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
}
