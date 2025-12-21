package dao;

import connectDB.connectDB;
import entity.Ve;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import java.util.Date;

import static connectDB.connectDB.getConnection;

public class Dashboard_DAO {

    public Map<String, Double> getThongKeTongQuan() {
        Map<String, Double> data = new HashMap<>();

        try {
            // ===== % DOANH THU SO VỚI THÁNG TRƯỚC =====
            LocalDate now = LocalDate.now();
            int thang = now.getMonthValue();
            int nam   = now.getYear();

            int thangTruoc = thang == 1 ? 12 : thang - 1;
            int namTruoc   = thang == 1 ? nam - 1 : nam;

            // Các phương thức tính doanh thu tự mở connection riêng
            double thangNay = getDoanhThuMotThang(thang, nam);
            double thangTruocDT = getDoanhThuMotThang(thangTruoc, namTruoc);

            double phanTram = 0;
            if (thangTruocDT != 0) {
                phanTram = ((thangNay - thangTruocDT) / thangTruocDT) * 100;
            }

            double ptVeBan = getPhanTramVeBanSoVoiThangTruoc(thang, nam);

            data.put("ptVeBan", ptVeBan);
            data.put("doanhThu", phanTram);

            // ===== Truy vấn tổng số vé =====
            try (Connection con = getConnection()) {
                if (con != null && !con.isClosed()) {

                    // Tổng số vé
                    String sqlSoVe = "SELECT COUNT(maVe) AS soVe FROM Ve";
                    try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sqlSoVe)) {
                        if (rs.next()) data.put("soVe", rs.getDouble("soVe"));
                    }

                    // Vé đã bán
                    String sqlVeBan = "SELECT COUNT(maVe) AS veBan FROM Ve WHERE trangThai = 1";
                    try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sqlVeBan)) {
                        if (rs.next()) data.put("soVeBan", rs.getDouble("veBan"));
                    }

                    // Vé đã trả/hủy
                    String sqlVeTra = "SELECT COUNT(maVe) AS veTra FROM Ve WHERE trangThai = 0";
                    try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sqlVeTra)) {
                        if (rs.next()) data.put("soVeTra", rs.getDouble("veTra"));
                    }

                    // Tổng số khách hàng
                    String sqlKH = "SELECT COUNT(DISTINCT maKH) AS khachHang FROM Ve";
                    try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sqlKH)) {
                        if (rs.next()) data.put("khachHang", rs.getDouble("khachHang"));
                    }


                    // Tổng số tuyến (đếm lịch trình)
                    String sqlTuyen = "SELECT COUNT(DISTINCT maLichTrinh) AS tuyen FROM Ve";
                    try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sqlTuyen)) {
                        if (rs.next()) data.put("tuyen", rs.getDouble("tuyen"));
                    }
                }
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
        ISNULL(SUM(cthd.giaVe * cthd.soLuong), 0) AS DoanhThu
    FROM HoaDon hd
    JOIN ChiTietHoaDon cthd ON hd.maHoaDon = cthd.maHoaDon
    JOIN Ve v ON v.maVe = cthd.maVe
    WHERE YEAR(hd.ngayTao) = ?
      AND hd.trangThai = 1     -- hóa đơn hợp lệ / đã thanh toán
      AND v.trangThai = 1      -- loại vé đã trả
    GROUP BY MONTH(hd.ngayTao)
    ORDER BY Thang
""";


        try (Connection con = getConnection();
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

public double getDoanhThuMotThang(int thang, int nam) {
    double doanhThu = 0;

    String sql = """
    SELECT ISNULL(SUM(cthd.giaVe * cthd.soLuong), 0) AS DoanhThu
    FROM HoaDon hd
    JOIN ChiTietHoaDon cthd ON hd.maHoaDon = cthd.maHoaDon
    JOIN Ve v ON v.maVe = cthd.maVe
    WHERE hd.trangThai = 1
      AND v.trangThai = 1       -- chỉ tính vé chưa trả
      AND MONTH(hd.ngayTao) = ?
      AND YEAR(hd.ngayTao) = ?
""";

    // Tạo connection mới riêng cho phương thức này
    try (Connection con = getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, thang);
        ps.setInt(2, nam);

        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                doanhThu = rs.getDouble("DoanhThu");
            }
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return doanhThu;
}


    public Map<Integer, Integer> getSoVeTheoThang(int nam) {
        Map<Integer, Integer> data = new LinkedHashMap<>();

        String sql = """
    SELECT MONTH(hd.ngayTao) AS Thang,
           COUNT(DISTINCT v.maVe) AS SoVe
    FROM HoaDon hd
    JOIN ChiTietHoaDon cthd ON hd.maHoaDon = cthd.maHoaDon
    JOIN Ve v ON cthd.maVe = v.maVe
    WHERE YEAR(hd.ngayTao) = ?
      AND hd.trangThai = 1     -- hóa đơn hợp lệ / đã thanh toán
      AND v.trangThai = 1      -- loại vé đã trả
    GROUP BY MONTH(hd.ngayTao)
    ORDER BY Thang
""";

        try (Connection con = getConnection();
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

public int getSoVeBanMotThang(int thang, int nam) {
    int soVe = 0;
            String sql = """
    SELECT COUNT(DISTINCT v.maVe)
    FROM Ve v
    JOIN ChiTietHoaDon ct ON ct.maVe = v.maVe
    JOIN HoaDon hd ON hd.maHoaDon = ct.maHoaDon
    WHERE hd.trangThai = 1        -- hóa đơn hợp lệ / đã thanh toán
      AND v.trangThai = 1         -- chỉ tính vé chưa trả
      AND MONTH(hd.ngayTao) = ?
      AND YEAR(hd.ngayTao) = ?
""";

    try (Connection con = getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, thang);
        ps.setInt(2, nam);

        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            soVe = rs.getInt(1);
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return soVe;
}

    public double getPhanTramVeBanSoVoiThangTruoc(int thang, int nam) {
        int thangNay = getSoVeBanMotThang(thang, nam);

        int thangTruoc = thang == 1 ? 12 : thang - 1;
        int namTruoc   = thang == 1 ? nam - 1 : nam;

        int thangTruocVe = getSoVeBanMotThang(thangTruoc, namTruoc);

        if (thangTruocVe == 0) return 0;

        return ((double)(thangNay - thangTruocVe) / thangTruocVe) * 100;
    }

    public Map<String, Double> getDoanhThuTheoTuyenThang12(LocalDate today) {
        Map<String, Double> data = new LinkedHashMap<>();

        String sql = """
    SELECT 
        t.maTuyen,
        SUM(cthd.giaVe * cthd.soLuong) AS doanhThu
    FROM Tuyen t
    JOIN LichTrinh lt ON t.maTuyen = lt.maTuyen
    JOIN Ve v ON lt.maLichTrinh = v.maLichTrinh
    JOIN ChiTietHoaDon cthd ON v.maVe = cthd.maVe
    JOIN HoaDon hd ON cthd.maHoaDon = hd.maHoaDon
    WHERE hd.trangThai = 1        -- hóa đơn hợp lệ
      AND v.trangThai = 1         -- loại vé đã trả
      AND MONTH(hd.gioTao) = 12
      AND YEAR(hd.gioTao) = YEAR(GETDATE())
    GROUP BY t.maTuyen
    ORDER BY doanhThu DESC
""";


        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                data.put(
                        rs.getString("maTuyen"),
                        rs.getDouble("doanhThu")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }



    public Map<String, Double> getThongKeNgay(LocalDate today) {
        Map<String, Double> thongKe = new LinkedHashMap<>();

        String sql = """
        SELECT 
            COUNT(DISTINCT hd.maKH) AS soKhachMoi,
            SUM(hd.tongTien) AS doanhThu,
            COUNT(cthd.maVe) AS soVeBan
        FROM HoaDon hd
        LEFT JOIN ChiTietHoaDon cthd ON hd.maHoaDon = cthd.maHoaDon
        WHERE CAST(hd.ngayTao AS DATE) = ?
    """;

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            // Truyền ngày vào PreparedStatement
            ps.setDate(1, java.sql.Date.valueOf(today));

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                thongKe.put("ptKhachHang", rs.getDouble("soKhachMoi"));  // Khách hàng mới
                thongKe.put("doanhThu", rs.getDouble("doanhThu"));        // Doanh thu
                thongKe.put("soVeBan", rs.getDouble("soVeBan"));          // Số vé bán
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return thongKe;
    }

    public double getSoKhuyenMaiSapHetHan(int soNgay) {
        double soLuong = 0;

        String sql = """
        SELECT COUNT(*) 
        FROM KhuyenMai
        WHERE thoiGianKetThuc >= CAST(GETDATE() AS DATE)
          AND thoiGianKetThuc <= DATEADD(DAY, ?, CAST(GETDATE() AS DATE))
    """;

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, soNgay);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                soLuong = rs.getDouble(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return soLuong;
    }


public Map<String, Integer> getSoChoNgoiConTrongTheoTuyen(int day, int month) {
    Map<String, Integer> data = new HashMap<>();

    String sql =
            "SELECT t.maTuyen, " +
                    "       COUNT(DISTINCT c.maChoNgoi) AS soChoConTrong " +
                    "FROM LichTrinh l " +
                    "JOIN Tuyen t ON l.maTuyen = t.maTuyen " +
                    "JOIN Toa toa ON l.soHieuTau = toa.soHieuTau " +
                    "JOIN ChoNgoi c ON toa.maToa = c.maToa " +
                    "WHERE l.trangThai = 1 " +
                    "  AND DAY(l.gioKhoiHanh) = ? " +
                    "  AND MONTH(l.gioKhoiHanh) = ? " +
                    "  AND NOT EXISTS ( " +
                    "      SELECT 1 FROM Ve v " +
                    "      JOIN ChiTietHoaDon ct ON ct.maVe = v.maVe " +
                    "      WHERE v.maChoNgoi = c.maChoNgoi " +
                    "        AND v.maLichTrinh = l.maLichTrinh " +
                    "  ) " +
                    "GROUP BY t.maTuyen";

    try (Connection con = getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, day);
        ps.setInt(2, month);

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            data.put(
                    rs.getString("maTuyen"),
                    rs.getInt("soChoConTrong")
            );
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return data;
}

}



