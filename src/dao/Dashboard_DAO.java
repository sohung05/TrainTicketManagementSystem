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

    /**
     * Lấy số chỗ trống theo tuyến (không filter)
     */
    public Map<String, Integer> getSoChoNgoiConTrongTheoTuyen() {
        // Test: Kiểm tra có dữ liệu LichTrinh không
        testDataAvailability();
        return getSoChoNgoiConTrongTheoTuyen(null);
    }
    
    /**
     * Test xem có dữ liệu trong database không
     */
    private void testDataAvailability() {
        try (Connection con = connectDB.getConnection();
             Statement st = con.createStatement()) {
            
            // Đếm số lịch trình
            ResultSet rs1 = st.executeQuery("SELECT COUNT(*) AS total FROM LichTrinh WHERE gioKhoiHanh >= GETDATE()");
            if (rs1.next()) {
                System.out.println("📊 Số lịch trình từ hôm nay: " + rs1.getInt("total"));
            }
            
            // Đếm số ghế
            ResultSet rs2 = st.executeQuery("SELECT COUNT(*) AS total FROM ChoNgoi");
            if (rs2.next()) {
                System.out.println("📊 Tổng số ghế: " + rs2.getInt("total"));
            }
            
            // Đếm số vé đã bán
            ResultSet rs3 = st.executeQuery("SELECT COUNT(*) AS total FROM Ve WHERE trangThai = 1");
            if (rs3.next()) {
                System.out.println("📊 Số vé đã bán (trangThai=1): " + rs3.getInt("total"));
            }
            
        } catch (Exception e) {
            System.err.println("❌ Lỗi test data: " + e.getMessage());
        }
    }

    /**
     * Lấy số chỗ trống theo tuyến (có filter theo ngày)
     * @param tuNgay Nếu null thì lấy từ hôm nay trở đi
     */
    public Map<String, Integer> getSoChoNgoiConTrongTheoTuyen(java.time.LocalDate tuNgay) {
        Map<String, Integer> data = new HashMap<>();
        
        // SQL mới: Tính chính xác số ghế trống = Tổng ghế - Ghế đã bán
        // LƯU Ý: JOIN theo soHieuTau (LichTrinh → ChuyenTau → Toa)
        String sql = "SELECT \n" +
                "    g1.tenGa + ' - ' + g2.tenGa AS tuyen,\n" +
                "    SUM(sub.tongGhe) - SUM(sub.gheDaBan) AS soChoTrong\n" +
                "FROM (\n" +
                "    SELECT \n" +
                "        lt.maLichTrinh,\n" +
                "        lt.maGaDi,\n" +
                "        lt.maGaDen,\n" +
                "        COUNT(DISTINCT c.maChoNgoi) AS tongGhe,\n" +
                "        COUNT(DISTINCT CASE WHEN v.trangThai = 1 THEN v.maVe END) AS gheDaBan\n" +
                "    FROM LichTrinh lt\n" +
                "        JOIN ChuyenTau ct ON lt.soHieuTau = ct.soHieuTau\n" +
                "        JOIN Toa t ON ct.soHieuTau = t.soHieuTau\n" +
                "        JOIN ChoNgoi c ON t.maToa = c.maToa\n" +
                "        LEFT JOIN Ve v ON v.maChoNgoi = c.maChoNgoi \n" +
                "                       AND v.maLichTrinh = lt.maLichTrinh\n" +
                "    WHERE lt.gioKhoiHanh >= ?\n" +
                "    GROUP BY lt.maLichTrinh, lt.maGaDi, lt.maGaDen\n" +
                ") AS sub\n" +
                "    JOIN Ga g1 ON sub.maGaDi = g1.maGa\n" +
                "    JOIN Ga g2 ON sub.maGaDen = g2.maGa\n" +
                "GROUP BY g1.tenGa, g2.tenGa\n" +
                "ORDER BY soChoTrong DESC";

        try (Connection con = connectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            // Nếu tuNgay là null, mặc định từ hôm nay
            java.time.LocalDate ngayLoc = (tuNgay != null) ? tuNgay : java.time.LocalDate.now();
            ps.setDate(1, java.sql.Date.valueOf(ngayLoc));

            System.out.println("🔍 SQL Số chỗ trống - Ngày lọc: " + ngayLoc);

            ResultSet rs = ps.executeQuery();
            int count = 0;
            while (rs.next()) {
                String tuyen = rs.getString("tuyen");
                int soChoTrong = rs.getInt("soChoTrong");
                data.put(tuyen, soChoTrong);
                count++;
                System.out.println("   📊 " + tuyen + ": " + soChoTrong + " ghế trống");
            }
            
            if (count == 0) {
                System.out.println("⚠️ KHÔNG CÓ DỮ LIỆU! Thử phương án dự phòng...");
                return getSoChoTrongSimple(ngayLoc);

            }

        } catch (SQLException e) {
            System.err.println("❌ Lỗi SQL getSoChoNgoiConTrongTheoTuyen: " + e.getMessage());
            e.printStackTrace();
            // Fallback: Thử cách đơn giản hơn
            return getSoChoTrongSimple(tuNgay != null ? tuNgay : java.time.LocalDate.now());
        }
        return soLuong;
    }


    /**
     * Phương án dự phòng: Tính số chỗ trống theo cách đơn giản hơn
     */
    private Map<String, Integer> getSoChoTrongSimple(java.time.LocalDate ngayLoc) {
        Map<String, Integer> data = new HashMap<>();
        System.out.println("🔄 Dùng SQL đơn giản để tính số chỗ trống...");
        
        String sql = "SELECT \n" +
                "    g1.tenGa + ' - ' + g2.tenGa AS tuyen,\n" +
                "    COUNT(DISTINCT lt.maLichTrinh) AS soChuyenTau,\n" +
                "    SUM(CASE WHEN v.maVe IS NULL THEN 1 ELSE 0 END) AS soChoTrong\n" +
                "FROM LichTrinh lt\n" +
                "    JOIN Ga g1 ON lt.maGaDi = g1.maGa\n" +
                "    JOIN Ga g2 ON lt.maGaDen = g2.maGa\n" +
                "    JOIN ChuyenTau ct ON lt.soHieuTau = ct.soHieuTau\n" +
                "    JOIN Toa t ON ct.soHieuTau = t.soHieuTau\n" +
                "    JOIN ChoNgoi c ON t.maToa = c.maToa\n" +
                "    LEFT JOIN Ve v ON v.maChoNgoi = c.maChoNgoi \n" +
                "                   AND v.maLichTrinh = lt.maLichTrinh \n" +
                "                   AND v.trangThai = 1\n" +
                "WHERE lt.gioKhoiHanh >= ?\n" +
                "GROUP BY g1.tenGa, g2.tenGa\n" +
                "HAVING COUNT(DISTINCT lt.maLichTrinh) > 0\n" +
                "ORDER BY soChoTrong DESC";
        
        try (Connection con = connectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setDate(1, java.sql.Date.valueOf(ngayLoc));
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                String tuyen = rs.getString("tuyen");
                int soChuyenTau = rs.getInt("soChuyenTau");
                int soChoTrong = rs.getInt("soChoTrong");
                data.put(tuyen, soChoTrong);
                System.out.println("   📊 " + tuyen + " (" + soChuyenTau + " chuyến): " + soChoTrong + " ghế trống");
            }
            
        } catch (Exception e) {
            System.err.println("❌ Lỗi SQL simple: " + e.getMessage());
            e.printStackTrace();
        }
        
        return data;
    }

    public Map<String, Integer> getGheTrongTheoTuyen(LocalDate from, LocalDate to) {
        Map<String, Integer> gheTrongMap = new HashMap<>();
        String sql = "SELECT lt.maTuyen, COUNT(cn.maChoNgoi) - COUNT(v.maVe) AS GheTrong " +
                "FROM LichTrinh lt " +
                "JOIN Toa t ON t.soHieuTau = lt.soHieuTau " +
                "JOIN ChoNgoi cn ON cn.maToa = t.maToa " +
                "LEFT JOIN Ve v ON v.maLichTrinh = lt.maLichTrinh AND v.maChoNgoi = cn.maChoNgoi AND v.trangThai = 1 " +
                "WHERE lt.gioKhoiHanh BETWEEN ? AND ? " +
                "GROUP BY lt.maTuyen " +
                "ORDER BY lt.maTuyen";
        try (Connection con = connectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, java.sql.Date.valueOf(from));
            ps.setDate(2, java.sql.Date.valueOf(to));
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                gheTrongMap.put(rs.getString("maTuyen"), rs.getInt("GheTrong"));
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return data;
}

}



