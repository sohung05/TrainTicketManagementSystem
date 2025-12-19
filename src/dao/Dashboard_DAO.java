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
        // ===== % DOANH THU SO VỚI THÁNG TRƯỚC =====
        LocalDate now = LocalDate.now();
        int thang = now.getMonthValue();
        int nam   = now.getYear();

        // ⚠️ CHỈ TÍNH TOÁN – KHÔNG ĐỤNG CONNECTION con
        double thangNay = getDoanhThuMotThang(thang, nam);

        int thangTruoc = thang == 1 ? 12 : thang - 1;
        int namTruoc   = thang == 1 ? nam - 1 : nam;

        double thangTruocDT = getDoanhThuMotThang(thangTruoc, namTruoc);

        double phanTram = 0;
        if (thangTruocDT != 0) {
            phanTram = ((thangNay - thangTruocDT) / thangTruocDT) * 100;
        }

             double ptVeBan = getPhanTramVeBanSoVoiThangTruoc(thang, nam);
         //    double ptTuyen = getPhanTramTuyenSoVoiThangTruoc(thang, nam);

             data.put("ptVeBan", ptVeBan);
           //  data.put("ptTuyen", ptTuyen);

             data.put("doanhThu", phanTram);
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
            String sqlKH = "SELECT COUNT(DISTINCT maKH) AS khachHang FROM Ve";
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
    public double getDoanhThuMotThang(int thang, int nam) {
        double doanhThu = 0;
        Connection con = connectDB.getConnection(); // dùng connection chung

        String sql = """
        SELECT ISNULL(SUM(cthd.giaVe * cthd.soLuong), 0) AS DoanhThu
        FROM HoaDon hd
        JOIN ChiTietHoaDon cthd ON hd.maHoaDon = cthd.maHoaDon
        WHERE MONTH(hd.ngayTao) = ?
          AND YEAR(hd.ngayTao) = ?
          AND hd.trangThai = 1
    """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, thang);
            ps.setInt(2, nam);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                doanhThu = rs.getDouble("DoanhThu");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return doanhThu;
    }

    public double getPhanTramSoVoiThangTruoc(int thang, int nam) {
        double doanhThuThangNay = getDoanhThuMotThang(thang, nam);

        int thangTruoc = thang == 1 ? 12 : thang - 1;
        int namTruoc   = thang == 1 ? nam - 1 : nam;

        double doanhThuThangTruoc = getDoanhThuMotThang(thangTruoc, namTruoc);

        if (doanhThuThangTruoc == 0) return 0;

        return ((doanhThuThangNay - doanhThuThangTruoc)
                / doanhThuThangTruoc) * 100;
    }

    // === 5️⃣ SỐ KHÁCH HÀNG THEO THÁNG ===
    public int getSoVeTheoTuyenTrongThang(int thang, int nam) {
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
    public Map<Integer, Integer> getSoVeTheoThang(int nam) {
        Map<Integer, Integer> data = new LinkedHashMap<>();

        String sql = """
        SELECT MONTH(thoiGianLenTau) AS Thang,
               COUNT(*) AS SoVe
        FROM Ve
        WHERE thoiGianLenTau IS NOT NULL
          AND YEAR(thoiGianLenTau) = ?
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


    public int getSoVeBanMotThang(int thang, int nam) {
        int soVe = 0;
        Connection con = connectDB.getConnection();

        String sql = """
        SELECT COUNT(maVe)
        FROM Ve
        WHERE trangThai = 1
          AND MONTH(thoiGianLenTau) = ?
          AND YEAR(thoiGianLenTau) = ?
    """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, thang);
            ps.setInt(2, nam);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) soVe = rs.getInt(1);
        } catch (Exception e) {
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

    public Map<String, int[]> getSoVeTheoTuyenHomQuaHomNay(LocalDate ngay) {

        Map<String, int[]> data = new HashMap<>();

        String sql = """
        SELECT t.maTuyen,
               SUM(CASE WHEN CAST(v.thoiGianLenTau AS DATE) = ? THEN 1 ELSE 0 END) AS homNay,
               SUM(CASE WHEN CAST(v.thoiGianLenTau AS DATE) = DATEADD(day, -1, ?) THEN 1 ELSE 0 END) AS homQua
        FROM Ve v
        JOIN LichTrinh lt ON v.maLichTrinh = lt.maLichTrinh
        JOIN Tuyen t ON lt.maTuyen = t.maTuyen
        WHERE v.thoiGianLenTau IS NOT NULL
        GROUP BY t.maTuyen
    """;

        try (Connection con = connectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, java.sql.Date.valueOf(ngay));
            ps.setDate(2, java.sql.Date.valueOf(ngay));

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String maTuyen = rs.getString("maTuyen");
                int homNay = rs.getInt("homNay");
                int homQua = rs.getInt("homQua");

                data.put(maTuyen, new int[]{homQua, homNay});
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return data; // ← LUÔN luôn trả Map (kể cả rỗng)
    }

    public Map<String, Double> getDoanhThuTheoTuyen(LocalDate today) {
        Map<String, Double> data = new LinkedHashMap<>();

        String sql = """
        SELECT 
            t.maTuyen,
            SUM(hd.tongTien) AS doanhThu
        FROM Tuyen t
        JOIN LichTrinh lt ON t.maTuyen = lt.maTuyen
        JOIN Ve v ON lt.maLichTrinh = v.maLichTrinh
        JOIN ChiTietHoaDon cthd ON v.maVe = cthd.maVe
        JOIN HoaDon hd ON cthd.maHoaDon = hd.maHoaDon
        GROUP BY t.maTuyen
        ORDER BY doanhThu DESC
    """;

        try (Connection con = connectDB.getConnection();
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
    public Map<String, Double> getDoanhThuTheoTuyenThang(LocalDate date) {
        Map<String, Double> data = new LinkedHashMap<>();

        // Lấy tháng và năm từ đối tượng LocalDate
        int month = date.getMonthValue();
        int year = date.getYear();

        String sql = """
        SELECT 
            t.maTuyen,
            SUM(hd.tongTien) AS doanhThu
        FROM Tuyen t
        JOIN LichTrinh lt ON t.maTuyen = lt.maTuyen
        JOIN Ve v ON lt.maLichTrinh = v.maLichTrinh
        JOIN ChiTietHoaDon cthd ON v.maVe = cthd.maVe
        JOIN HoaDon hd ON cthd.maHoaDon = hd.maHoaDon
        WHERE MONTH(hd.ngayTao) = ? AND YEAR(hd.ngayTao) = ?
        GROUP BY t.maTuyen
        ORDER BY doanhThu DESC
    """;

        try (Connection con = connectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, month);
            ps.setInt(2, year);

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

    public Map<String, Double> getDoanhThuTheoTuyenThang12(LocalDate today) {
        Map<String, Double> data = new LinkedHashMap<>();

        String sql = """
        SELECT 
            t.maTuyen,
            SUM(hd.tongTien) AS doanhThu
        FROM Tuyen t
        JOIN LichTrinh lt ON t.maTuyen = lt.maTuyen
        JOIN Ve v ON lt.maLichTrinh = v.maLichTrinh
        JOIN ChiTietHoaDon cthd ON v.maVe = cthd.maVe
        JOIN HoaDon hd ON cthd.maHoaDon = hd.maHoaDon
        WHERE MONTH(hd.gioTao) = 12
          AND YEAR(hd.gioTao) = YEAR(GETDATE())
        GROUP BY t.maTuyen
        ORDER BY doanhThu DESC
    """;

        try (Connection con = connectDB.getConnection();
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


    public Map<String, Double> getSoTuyenTheoThang() {
        Map<String, Double> data = new HashMap<>();
        String sqlThangNay = "SELECT COUNT(DISTINCT maLichTrinh) AS tuyen FROM Ve " +
                "WHERE MONTH(ngayBan) = ? AND YEAR(ngayBan) = ?";
        String sqlThangTruoc = "SELECT COUNT(DISTINCT maLichTrinh) AS tuyen FROM Ve " +
                "WHERE MONTH(ngayBan) = ? AND YEAR(ngayBan) = ?";

        LocalDate now = LocalDate.now();
        int thangNay = now.getMonthValue();
        int namNay = now.getYear();
        int thangTruoc = thangNay == 1 ? 12 : thangNay - 1;
        int namTruoc = thangNay == 1 ? namNay - 1 : namNay;


        try (Connection con = connectDB.getConnection();
             PreparedStatement pst1 = con.prepareStatement(sqlThangNay);
             PreparedStatement pst2 = con.prepareStatement(sqlThangTruoc)) {

            // Tháng này
            pst1.setInt(1, thangNay);
            pst1.setInt(2, namNay);
            try (ResultSet rs = pst1.executeQuery()) {
                if (rs.next()) data.put("thangNay", rs.getDouble("tuyen"));
            }

            // Tháng trước
            pst2.setInt(1, thangTruoc);
            pst2.setInt(2, namTruoc);
            try (ResultSet rs = pst2.executeQuery()) {
                if (rs.next()) data.put("thangTruoc", rs.getDouble("tuyen"));
            }

            // Tính % thay đổi
            double thangNayVal = data.getOrDefault("thangNay", 0.0);
            double thangTruocVal = data.getOrDefault("thangTruoc", 0.0);
            double pt = 0.0;
            if (thangTruocVal != 0) {
                pt = ((thangNayVal - thangTruocVal) / thangTruocVal) * 100;
            }
            data.put("pt", pt);

        } catch (SQLException e) {
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

        try (Connection con = connectDB.getConnection();
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

        return data;
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
        return gheTrongMap;
    }

}
