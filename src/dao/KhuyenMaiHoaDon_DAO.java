package dao;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import connectDB.connectDB;
import entity.KhuyenMai;

public class KhuyenMaiHoaDon_DAO {

//
//    /**
//     * 🔹 Lấy toàn bộ danh sách khuyến mãi áp dụng cho các hóa đơn
//     */
//    public List<KhuyenMai> getTatCaKhuyenMaiHoaDon() {
//        List<KhuyenMai> list = new ArrayList<>();
//        Connection con = connectDB.getConnection();
//
//        if (con == null) {
//            connectDB.getConnection();
//            con = connectDB.getConnection();
//        }
//        String sql = """
//    SELECT
//                                       KM.maKhuyenMai,
//                                       KM.tenKhuyenMai,
//                                       KM.loaiKhuyenMai,
//                                       KM.thoiGianBatDau,
//                                       KM.thoiGianKetThuc,
//                                       KM.trangThai,
//                                       ISNULL(SUM(CTHD.soLuong), 0) AS soVeKhaDung,
//                                       CTKM.chietKhau
//                                   FROM KhuyenMai KM
//                                   LEFT JOIN ChiTietKhuyenMai CTKM\s
//                                       ON CTKM.maKhuyenMai = KM.maKhuyenMai
//                                       AND CTKM.dieuKien LIKE N'Hóa đơn%'  -- 🔹 lọc theo điều kiện trong bảng ChiTietKhuyenMai
//                                   LEFT JOIN HoaDon HD ON HD.maHoaDon = CTKM.maHoaDon
//                                   LEFT JOIN ChiTietHoaDon CTHD ON CTHD.maHoaDon = HD.maHoaDon
//                                   WHERE CTKM.chietKhau > 0
//                                   GROUP BY
//                                       KM.maKhuyenMai,
//                                       KM.tenKhuyenMai,
//                                       KM.loaiKhuyenMai,
//                                       KM.thoiGianBatDau,
//                                       KM.thoiGianKetThuc,
//                                       KM.trangThai,
//                                       CTKM.chietKhau
//                                   ORDER BY KM.thoiGianBatDau DESC;
//
//""";
//
//                try (PreparedStatement ps = con.prepareStatement(sql);
//                     ResultSet rs = ps.executeQuery()) {
//
//                    while (rs.next()) {
//                        String maKM = rs.getString("maKhuyenMai");
//                        String tenKM = rs.getString("tenKhuyenMai");
//                        String loaiKM = rs.getString("loaiKhuyenMai");
//                        LocalDateTime tgBatDau = rs.getTimestamp("thoiGianBatDau").toLocalDateTime();
//                        LocalDateTime tgKetThuc = rs.getTimestamp("thoiGianKetThuc").toLocalDateTime();
//                        boolean trangThai = rs.getBoolean("trangThai");
//                        int soVeKhaDung = rs.getInt("soVeKhaDung");
//                        double chietKhau = rs.getDouble("chietKhau");
//
//                        KhuyenMai km = new KhuyenMai(maKM, tenKM, loaiKM, tgBatDau, tgKetThuc, trangThai);
//                        km.setSoVe(soVeKhaDung);
//                        km.setChietKhau(chietKhau);
//
//                        list.add(km);
//                    }
//
//                    System.out.println("✅ Đã load " + list.size() + " khuyến mãi hóa đơn (KMHD).");
//
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//
//                return list;
//            }


    /**
     * 🔹 Lấy toàn bộ danh sách khuyến mãi áp dụng cho các hóa đơn
     */
    public List<KhuyenMai> getTatCaKhuyenMaiHoaDon() {
        List<KhuyenMai> list = new ArrayList<>();

        // ✅ Load khuyến mãi master data: loaiKhuyenMai = 'KMHD' và maHoaDon = NULL
        String sql = """
        SELECT
            KM.maKhuyenMai,
            KM.tenKhuyenMai,
            KM.loaiKhuyenMai,
            KM.thoiGianBatDau,
            KM.thoiGianKetThuc,
            KM.trangThai,
            CTKM.dieuKien AS soLuongVe,
            CTKM.chietKhau
        FROM KhuyenMai KM
        JOIN ChiTietKhuyenMai CTKM ON CTKM.maKhuyenMai = KM.maKhuyenMai
        WHERE KM.loaiKhuyenMai = 'KMHD' 
          AND CTKM.maHoaDon IS NULL
          AND CTKM.chietKhau > 0
        ORDER BY KM.thoiGianBatDau DESC
    """;

        try (Connection con = connectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String maKM = rs.getString("maKhuyenMai");
                String tenKM = rs.getString("tenKhuyenMai");
                String loaiKM = rs.getString("loaiKhuyenMai");

                Timestamp tsBatDau = rs.getTimestamp("thoiGianBatDau");
                Timestamp tsKetThuc = rs.getTimestamp("thoiGianKetThuc");

                LocalDateTime tgBatDau = tsBatDau != null ? tsBatDau.toLocalDateTime() : null;
                LocalDateTime tgKetThuc = tsKetThuc != null ? tsKetThuc.toLocalDateTime() : null;

                boolean trangThai = rs.getBoolean("trangThai");
                String soLuongVe = rs.getString("soLuongVe"); // e.g., "11-40 vé"
                double chietKhau = rs.getDouble("chietKhau");

                KhuyenMai km = new KhuyenMai(maKM, tenKM, loaiKM, tgBatDau, tgKetThuc, trangThai);
                km.setDoiTuongApDung(soLuongVe); // Lưu điều kiện ("11-40 vé") vào doiTuongApDung
                km.setChietKhau(chietKhau);

                list.add(km);
            }

            System.out.println("✅ Đã load " + list.size() + " khuyến mãi hóa đơn (KMHD).");

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("⚠️ Lỗi khi load danh sách khuyến mãi hóa đơn: " + e.getMessage());
        }

        return list;
    }

    /**
     * 🔹 Thêm khuyến mãi hóa đơn
     */
    public boolean themKhuyenMaiHoaDon(KhuyenMai km, double chietKhau) {
        Connection con = connectDB.getConnection();
        if (con == null) {
            connectDB.getConnection();
            con = connectDB.getConnection();
        }

        String insertKM = """
            INSERT INTO KhuyenMai(maKhuyenMai, tenKhuyenMai, loaiKhuyenMai, thoiGianBatDau, thoiGianKetThuc, trangThai)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        String insertCTKM = """
            INSERT INTO ChiTietKhuyenMai(maHoaDon, maKhuyenMai, dieuKien, chietKhau)
            SELECT maHoaDon, ?, N'Hóa đơn áp dụng', ?
            FROM HoaDon
        """;

        try {
            con.setAutoCommit(false);

            // Thêm khuyến mãi
            try (PreparedStatement ps = con.prepareStatement(insertKM)) {
                ps.setString(1, km.getMaKhuyenMai());
                ps.setString(2, km.getTenKhuyenMai());
                ps.setString(3, km.getLoaiKhuyenMai());
                ps.setTimestamp(4, Timestamp.valueOf(km.getThoiGianBatDau()));
                ps.setTimestamp(5, Timestamp.valueOf(km.getThoiGianKetThuc()));
                ps.setBoolean(6, km.isTrangThai());
                ps.executeUpdate();
            }

            // Thêm chi tiết khuyến mãi
            try (PreparedStatement ps = con.prepareStatement(insertCTKM)) {
                ps.setString(1, km.getMaKhuyenMai());
                ps.setDouble(2, chietKhau);
                ps.executeUpdate();
            }

            con.commit();
            con.setAutoCommit(true);
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            try { con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
        }

        return false;
    }

    /**
     * 🔹 Cập nhật chiết khấu
     */
    public boolean capNhatChietKhau(String maKhuyenMai, double chietKhau) {
        Connection con = connectDB.getConnection();
        if (con == null) {
            connectDB.getConnection();
            con = connectDB.getConnection();
        }

        String sql = """
            UPDATE ChiTietKhuyenMai
            SET chietKhau = ?
            WHERE maKhuyenMai = ?
        """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, chietKhau);
            ps.setString(2, maKhuyenMai);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    public boolean capNhatKhuyenMai(KhuyenMai km) {
        String sql = """
        UPDATE KhuyenMai
        SET tenKhuyenMai = ?, thoiGianBatDau = ?, thoiGianKetThuc = ?, trangThai = ?
        WHERE maKhuyenMai = ?
    """;

        try (Connection con = connectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            if (con == null) {
                connectDB.getConnection();
                // Lấy lại Connection nhưng **không dùng try-with-resources với con** nữa
                Connection newCon = connectDB.getConnection();
                return false; // hoặc throw Exception tùy thiết kế
            }

            ps.setString(1, km.getTenKhuyenMai());
            ps.setTimestamp(2, Timestamp.valueOf(km.getThoiGianBatDau()));
            ps.setTimestamp(3, Timestamp.valueOf(km.getThoiGianKetThuc()));
            ps.setBoolean(4, km.isTrangThai());
            ps.setString(5, km.getMaKhuyenMai());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean tamNgungTrangThai(String maKhuyenMai, boolean trangThai) {
        String sql = "UPDATE KhuyenMai SET trangThai = ? WHERE maKhuyenMai = ?";
        try (Connection con = connectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setBoolean(1, trangThai);
            ps.setString(2, maKhuyenMai);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<KhuyenMai> locKhuyenMaiHoaDon(String keyword) {
        List<KhuyenMai> ds = new ArrayList<>();
        Connection con = connectDB.getConnection();
        if (con == null) {
            connectDB.getConnection();
            con = connectDB.getConnection();
        }

        String sql = """
        SELECT maKhuyenMai, tenKhuyenMai, loaiKhuyenMai, 
               thoiGianBatDau, thoiGianKetThuc, trangThai
        FROM KhuyenMai
        WHERE tenKhuyenMai LIKE ?
        ORDER BY thoiGianBatDau DESC
    """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%"); // LIKE không phân biệt chữ hoa/thường

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    KhuyenMai km = new KhuyenMai();
                    km.setMaKhuyenMai(rs.getString("maKhuyenMai"));
                    km.setTenKhuyenMai(rs.getString("tenKhuyenMai"));
                    km.setLoaiKhuyenMai(rs.getString("loaiKhuyenMai"));
                    km.setThoiGianBatDau(rs.getTimestamp("thoiGianBatDau").toLocalDateTime());
                    km.setThoiGianKetThuc(rs.getTimestamp("thoiGianKetThuc").toLocalDateTime());
                    km.setTrangThai(rs.getBoolean("trangThai"));

                    ds.add(km);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ds;
    }


    public boolean themKhuyenMai(KhuyenMai km) {
        Connection con = connectDB.getConnection();
        if (con == null) {
            connectDB.getConnection();
            con = connectDB.getConnection();
        }

        String sqlKM = """
        INSERT INTO KhuyenMai (maKhuyenMai, tenKhuyenMai, loaiKhuyenMai, 
                               thoiGianBatDau, thoiGianKetThuc, trangThai)
        VALUES (?, ?, 'KMHD', ?, ?, ?)
    """;

        try (PreparedStatement psKM = con.prepareStatement(sqlKM)) {
            con.setAutoCommit(false);

            psKM.setString(1, km.getMaKhuyenMai());
            psKM.setString(2, km.getTenKhuyenMai());
            psKM.setTimestamp(3, Timestamp.valueOf(km.getThoiGianBatDau()));
            psKM.setTimestamp(4, Timestamp.valueOf(km.getThoiGianKetThuc()));
            psKM.setBoolean(5, km.isTrangThai());

            psKM.executeUpdate();
            con.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            try { con.rollback(); } catch (SQLException ignored) {}
        }
        return false;
    }
//    public boolean capNhatKhuyenMaiHoaDon(String maKMCu, String maKMMoi, String ten, Date thoiGianBatDau, Date thoiGianKetThuc, double chietKhau) {
//        Connection con = connectDB.getConnection();
//        PreparedStatement psDisableFK = null;
//        PreparedStatement psUpdateCT = null;
//        PreparedStatement psUpdateKM = null;
//        PreparedStatement psEnableFK = null;
//        boolean success = false;
//
//        try {
//            con.setAutoCommit(false);
//
//            // 1️⃣ Tạm tắt kiểm tra khóa ngoại FK
//            psDisableFK = con.prepareStatement("ALTER TABLE ChiTietKhuyenMai NOCHECK CONSTRAINT ALL");
//            psDisableFK.execute();
//
//            // 2️⃣ Cập nhật ChiTietKhuyenMai (chuyển mã cũ -> mới)
//            String sqlUpdateCT = """
//            UPDATE ChiTietKhuyenMai
//            SET maKhuyenMai = ?, chietKhau = ?
//            WHERE maKhuyenMai = ?
//        """;
//            psUpdateCT = con.prepareStatement(sqlUpdateCT);
//            psUpdateCT.setString(1, maKMMoi);
//            psUpdateCT.setDouble(2, chietKhau);
//            psUpdateCT.setString(3, maKMCu);
//            psUpdateCT.executeUpdate();
//
//            // 3️⃣ Cập nhật KhuyenMai (đổi mã)
//            String sqlUpdateKM = """
//            UPDATE KhuyenMai
//            SET maKhuyenMai = ?, tenKhuyenMai = ?, thoiGianBatDau = ?, thoiGianKetThuc = ?
//            WHERE maKhuyenMai = ?
//        """;
//            psUpdateKM = con.prepareStatement(sqlUpdateKM);
//            psUpdateKM.setString(1, maKMMoi);
//            psUpdateKM.setString(2, ten);
//            psUpdateKM.setDate(3, thoiGianBatDau);
//            psUpdateKM.setDate(4, thoiGianKetThuc);
//            psUpdateKM.setString(5, maKMCu);
//            psUpdateKM.executeUpdate();
//
//            // 4️⃣ Bật lại kiểm tra khóa ngoại
//            psEnableFK = con.prepareStatement("ALTER TABLE ChiTietKhuyenMai WITH CHECK CHECK CONSTRAINT ALL");
//            psEnableFK.execute();
//
//            con.commit();
//            success = true;
//            System.out.println("✅ Cập nhật và đổi mã thành công!");
//
//        } catch (SQLException e) {
//            try {
//                con.rollback();
//                System.err.println("⚠️ Rollback do lỗi: " + e.getMessage());
//            } catch (SQLException ex) {
//                ex.printStackTrace();
//            }
//            e.printStackTrace();
//        } finally {
//            try {
//                if (psDisableFK != null) psDisableFK.close();
//                if (psUpdateCT != null) psUpdateCT.close();
//                if (psUpdateKM != null) psUpdateKM.close();
//                if (psEnableFK != null) psEnableFK.close();
//                con.setAutoCommit(true);
//            } catch (SQLException ex) {
//                ex.printStackTrace();
//            }
//        }
//        return success;
//    }

    public boolean capNhatKhuyenMaiHoaDon(String maKMCu, String maKMMoi, String ten, Date thoiGianBatDau, Date thoiGianKetThuc, double chietKhau) {
        Connection con = connectDB.getConnection();
        PreparedStatement psDisableFK = null;
        PreparedStatement psUpdateCT = null;
        PreparedStatement psUpdateKM = null;
        PreparedStatement psEnableFK = null;
        boolean success = false;

        try {
            con.setAutoCommit(false);

            // 1️⃣ Tạm tắt kiểm tra khóa ngoại
            psDisableFK = con.prepareStatement("ALTER TABLE ChiTietKhuyenMai NOCHECK CONSTRAINT ALL");
            psDisableFK.execute();

            // 2️⃣ Cập nhật bảng ChiTietKhuyenMai: đổi mã cũ -> mã mới, cập nhật chiết khấu
            String sqlUpdateCT = """
            UPDATE ChiTietKhuyenMai
            SET maKhuyenMai = ?, chietKhau = ?
            WHERE maKhuyenMai = ?
        """;
            psUpdateCT = con.prepareStatement(sqlUpdateCT);
            psUpdateCT.setString(1, maKMMoi);
            psUpdateCT.setDouble(2, chietKhau);
            psUpdateCT.setString(3, maKMCu);
            psUpdateCT.executeUpdate();

            // 3️⃣ Cập nhật bảng KhuyenMai: đổi mã, cập nhật tên, ngày bắt đầu/kết thúc, loại khuyến mãi
            String sqlUpdateKM = """
            UPDATE KhuyenMai
            SET maKhuyenMai = ?, 
                tenKhuyenMai = ?, 
                thoiGianBatDau = ?, 
                thoiGianKetThuc = ?, 
                loaiKhuyenMai = N'Hóa Đơn'
            WHERE maKhuyenMai = ?
        """;
            psUpdateKM = con.prepareStatement(sqlUpdateKM);
            psUpdateKM.setString(1, maKMMoi);
            psUpdateKM.setString(2, ten);
            psUpdateKM.setDate(3, thoiGianBatDau);
            psUpdateKM.setDate(4, thoiGianKetThuc);
            psUpdateKM.setString(5, maKMCu);
            psUpdateKM.executeUpdate();

            // 4️⃣ Bật lại kiểm tra khóa ngoại
            psEnableFK = con.prepareStatement("ALTER TABLE ChiTietKhuyenMai WITH CHECK CHECK CONSTRAINT ALL");
            psEnableFK.execute();

            con.commit();
            success = true;
            System.out.println("✅ Cập nhật khuyến mãi hóa đơn thành công (mã mới: " + maKMMoi + ")");

        } catch (SQLException e) {
            try {
                con.rollback();
                System.err.println("⚠️ Rollback do lỗi SQL: " + e.getMessage());
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                if (psDisableFK != null) psDisableFK.close();
                if (psUpdateCT != null) psUpdateCT.close();
                if (psUpdateKM != null) psUpdateKM.close();
                if (psEnableFK != null) psEnableFK.close();
                con.setAutoCommit(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return success;
    }

}