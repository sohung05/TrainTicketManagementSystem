package dao;

import connectDB.connectDB;
import entity.Ve;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class Dashboard_DAO {

    // === 1️⃣ LẤY DANH SÁCH 10 VÉ GẦN ĐÂY ===
    public List<Ve> getDanhSachVeGanDay() {
        List<Ve> list = new ArrayList<>();
        Connection con = connectDB.getConnection();
        if (con == null) {
            System.err.println("❌ Không thể kết nối database trong Dashboard_DAO!");
            return list;
        }

        String sql = """
            SELECT TOP 10 maVe, tenKhachHang, soCCCD, thoiGianLenTau, giaVe
            FROM Ve
            ORDER BY thoiGianLenTau DESC
        """;

        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Ve v = new Ve();
                v.setMaVe(rs.getString("maVe"));
                v.setTenKhachHang(rs.getString("tenKhachHang"));
                v.setSoCCCD(rs.getString("soCCCD"));
                Timestamp t = rs.getTimestamp("thoiGianLenTau");
                if (t != null) v.setThoiGianLenTau(t.toLocalDateTime());
                v.setGiaVe(rs.getDouble("giaVe"));
                list.add(v);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // === 2️⃣ LẤY DANH SÁCH LỊCH TRÌNH GẦN ĐÂY ===
    public List<Object[]> getLichTrinhGanDay() {
        List<Object[]> list = new ArrayList<>();
        Connection con = connectDB.getConnection();
        if (con == null) {
            System.err.println("❌ Không thể kết nối CSDL trong getLichTrinhGanDay!");
            return list;
        }

        String sql = """
            SELECT TOP 10 
                lt.maLichTrinh,
                t.tenTuyen,
                gaDi.tenGa AS gaDi,
                lt.gioKhoiHanh,
                lt.gioDenDuKien AS gioDen
            FROM LichTrinh lt
            JOIN Tuyen t ON lt.maTuyen = t.maTuyen
            LEFT JOIN Ga gaDi ON lt.maGaDi = gaDi.maGa
            ORDER BY lt.gioKhoiHanh DESC
        """;

        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Object[]{
                        rs.getString("maLichTrinh"),
                        rs.getString("tenTuyen"),
                        rs.getString("gaDi"),
                        rs.getTimestamp("gioKhoiHanh"),
                        rs.getTimestamp("gioDen")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // === 3️⃣ THỐNG KÊ TỔNG QUAN ===
    public Map<String, Double> getThongKeTongQuan() {
        Map<String, Double> data = new HashMap<>();
        Connection con = connectDB.getConnection();
        if (con == null) return data;

        try {
            // ✅ Tổng doanh thu = tổng (giá vé * số lượng) trong ChiTietHoaDon
            String sqlDoanhThu = """
                SELECT SUM(cthd.giaVe * cthd.soLuong) AS doanhThu
                FROM ChiTietHoaDon cthd
                JOIN HoaDon hd ON cthd.maHoaDon = hd.maHoaDon
                WHERE hd.trangThai = 1
            """;
            try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sqlDoanhThu)) {
                if (rs.next()) data.put("doanhThu", rs.getDouble("doanhThu"));
            }

            // ✅ Tổng số vé
            String sqlSoVe = "SELECT COUNT(maVe) AS soVe FROM Ve";
            try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sqlSoVe)) {
                if (rs.next()) data.put("soVe", rs.getDouble("soVe"));
            }

            // ✅ Số vé đã bán (trangThai = 1)
            String sqlVeBan = "SELECT COUNT(maVe) AS veBan FROM Ve WHERE trangThai = 1";
            try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sqlVeBan)) {
                if (rs.next()) data.put("soVeBan", rs.getDouble("veBan"));
            }

            // ✅ Số vé đã trả/hủy (trangThai = 0)
            String sqlVeTra = "SELECT COUNT(maVe) AS veTra FROM Ve WHERE trangThai = 0";
            try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sqlVeTra)) {
                if (rs.next()) data.put("soVeTra", rs.getDouble("veTra"));
            }

            // ✅ Tổng số khách hàng
            String sqlKH = "SELECT COUNT(DISTINCT tenKhachHang) AS khachHang FROM Ve";
            try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sqlKH)) {
                if (rs.next()) data.put("khachHang", rs.getDouble("khachHang"));
            }

            // ✅ Tổng số tuyến (đếm lịch trình)
            String sqlTuyen = "SELECT COUNT(DISTINCT maLichTrinh) AS tuyen FROM Ve";
            try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sqlTuyen)) {
                if (rs.next()) data.put("tuyen", rs.getDouble("tuyen"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    // === 4️⃣ DOANH THU THEO THÁNG (cho biểu đồ) ===
    public Map<Integer, Double> getDoanhThuTheoThang(int nam) {
        Map<Integer, Double> data = new LinkedHashMap<>();

        String sql = """
            SELECT 
                MONTH(hd.ngayTao) AS Thang,
                SUM(cthd.giaVe * cthd.soLuong) AS DoanhThu
            FROM HoaDon hd
            JOIN ChiTietHoaDon cthd ON hd.maHoaDon = cthd.maHoaDon
            WHERE YEAR(hd.ngayTao) = ?
            GROUP BY MONTH(hd.ngayTao)
            ORDER BY Thang
        """;

        try (Connection con = connectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, nam);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                data.put(rs.getInt("Thang"), rs.getDouble("DoanhThu"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return data;
    }

    // === 5️⃣ SỐ KHÁCH HÀNG THEO THÁNG ===
    public int getSoKhachHangTheoThang(int thang, int nam) {
        int soKH = 0;
        String sql = """
            SELECT COUNT(DISTINCT maKH) AS SoKH
            FROM HoaDon
            WHERE MONTH(ngayTao) = ? AND YEAR(ngayTao) = ?
        """;
        try (Connection con = connectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, thang);
            ps.setInt(2, nam);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) soKH = rs.getInt("SoKH");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return soKH;
    }
    public Map<Integer, Integer> getSoVeTheoNam(int nam) {
        Map<Integer, Integer> data = new LinkedHashMap<>();
        String sql = """
        SELECT MONTH(thoiGianLenTau) AS Thang, COUNT(maVe) AS SoVe
        FROM Ve
        WHERE YEAR(thoiGianLenTau) = ?
        GROUP BY MONTH(thoiGianLenTau)
        ORDER BY Thang
    """;
        try (Connection con = connectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, nam);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                data.put(rs.getInt("Thang"), rs.getInt("SoVe"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public Map<Integer, Integer> getSoKhachHangTheoNam(int nam) {
        Map<Integer, Integer> data = new LinkedHashMap<>();
        String sql = """
        SELECT MONTH(thoiGianLenTau) AS Thang, COUNT(DISTINCT maKH) AS SoKH
        FROM Ve
        WHERE YEAR(thoiGianLenTau) = ?
        GROUP BY MONTH(thoiGianLenTau)
        ORDER BY Thang
    """;
        try (Connection con = connectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, nam);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                data.put(rs.getInt("Thang"), rs.getInt("SoKH"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
    // === 6️⃣ THỐNG KÊ SỐ CHUYẾN THEO NGÀY (TRONG THÁNG) ===
    public Map<Integer, Integer> getSoChuyenTrongNgay(LocalDate date) {
        Map<Integer, Integer> data = new LinkedHashMap<>();

        String sql = """
        SELECT DAY(gioKhoiHanh) AS Ngay, COUNT(*) AS SoChuyen
        FROM LichTrinh
        WHERE MONTH(gioKhoiHanh) = ? AND YEAR(gioKhoiHanh) = ?
        GROUP BY DAY(gioKhoiHanh)
        ORDER BY Ngay
    """;

        try (Connection con = connectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, date.getMonthValue());
            ps.setInt(2, date.getYear());

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                data.put(rs.getInt("Ngay"), rs.getInt("SoChuyen"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return data;
    }



}
