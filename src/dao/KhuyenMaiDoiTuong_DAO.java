package dao;

/*
 * @description: DAO quản lý khuyến mãi theo đối tượng khách hàng
 * @author :
 * @Date : 10/25/2025
 * @version : 1.1
 */

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import entity.KhuyenMai;
import entity.ChiTietKhuyenMai;
import entity.DoiTuong;
import connectDB.connectDB;

public class KhuyenMaiDoiTuong_DAO {
    /**
     * 🔹 Lấy toàn bộ danh sách khuyến mãi - khách hàng (tất cả đối tượng)
     */
    public List<Object[]> getTatCaKhuyenMaiKhachHang() {
        List<Object[]> list = new ArrayList<>();
        Connection con = connectDB.getConnection();

        if (con == null) {
            connectDB.getConnection();
            con = connectDB.getConnection();
        }

        String sql = """
        SELECT 
            KM.maKhuyenMai,
            KM.tenKhuyenMai,
            CTKM.dieuKien AS doiTuong,
            KM.thoiGianBatDau,
            KM.thoiGianKetThuc,
            CTKM.chietKhau,
            KM.trangThai
        FROM KhuyenMai KM
        JOIN ChiTietKhuyenMai CTKM ON KM.maKhuyenMai = CTKM.maKhuyenMai
        WHERE CTKM.chietKhau > 0
        ORDER BY KM.thoiGianBatDau DESC
    """;

        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Object[] row = {
                        rs.getString("maKhuyenMai"),
                        rs.getString("tenKhuyenMai"),
                        rs.getString("doiTuong"),
                        rs.getTimestamp("thoiGianBatDau").toLocalDateTime(),
                        rs.getTimestamp("thoiGianKetThuc").toLocalDateTime(),
                        rs.getDouble("chietKhau"),
                        rs.getBoolean("trangThai")
                };
                list.add(row);
            }

            System.out.println("✅ Đã load toàn bộ " + list.size() + " khuyến mãi - khách hàng hiện có (chỉ >0).");

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * 🔹 Lấy danh sách khuyến mãi theo đối tượng khách hàng
     */
    public List<Object[]> getDanhSachKhuyenMaiDoiTuong() {
        List<Object[]> list = new ArrayList<>();

        // Kết nối database
        Connection con = connectDB.getConnection();
        if (con == null) {
            connectDB.getConnection();
            con = connectDB.getConnection();
        }

        String sql = """
    SELECT km.maKhuyenMai, km.tenKhuyenMai, kh.doiTuong, 
           km.thoiGianBatDau, km.thoiGianKetThuc, ctkm.chietKhau, km.trangThai
    FROM KhuyenMai km
         JOIN ChiTietKhuyenMai ctkm ON km.maKhuyenMai = ctkm.maKhuyenMai
         JOIN HoaDon hd ON hd.maHoaDon = ctkm.maHoaDon
         JOIN KhachHang kh ON kh.maKH = hd.maKH
    GROUP BY km.maKhuyenMai, km.tenKhuyenMai, kh.doiTuong,
             km.thoiGianBatDau, km.thoiGianKetThuc, ctkm.chietKhau, km.trangThai
    ORDER BY km.thoiGianBatDau DESC
""";



        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Object[] row = {
                        rs.getString("maKhuyenMai"),
                        rs.getString("tenKhuyenMai"),
                        rs.getString("doiTuong"),
                        rs.getTimestamp("thoiGianBatDau").toLocalDateTime(),
                        rs.getTimestamp("thoiGianKetThuc").toLocalDateTime(),
                        rs.getDouble("chietKhau"),
                        rs.getBoolean("trangThai")
                };
                list.add(row);
            }

            System.out.println("✅ Đã load " + list.size() + " khuyến mãi đối tượng.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    //    public boolean themKhuyenMaiDoiTuong(KhuyenMai km, DoiTuong doiTuong, double chietKhau) {
//        Connection con = connectDB.getConnection();
//        if (con == null) {
//            connectDB.getConnection();
//            con = connectDB.getConnection();
//        }
//
//        String insertKM = """
//        INSERT INTO KhuyenMai(maKhuyenMai, tenKhuyenMai, loaiKhuyenMai, thoiGianBatDau, thoiGianKetThuc, trangThai)
//        VALUES (?, ?, ?, ?, ?, ?)
//    """;
//
//        // ✅ KHÔNG cần maHoaDon — chỉ thêm thông tin khuyến mãi + đối tượng
//        String insertCTKM = """
//        INSERT INTO ChiTietKhuyenMai(maKhuyenMai, dieuKien, chietKhau, maHoaDon)
//        VALUES (?, ?, ?, NULL)
//    """;
//
//        try {
//            con.setAutoCommit(false);
//
//            // --- 1️⃣ Thêm khuyến mãi
//            try (PreparedStatement ps = con.prepareStatement(insertKM)) {
//                ps.setString(1, km.getMaKhuyenMai());
//                ps.setString(2, km.getTenKhuyenMai());
//                ps.setString(3, km.getLoaiKhuyenMai());
//                ps.setTimestamp(4, Timestamp.valueOf(km.getThoiGianBatDau()));
//                ps.setTimestamp(5, Timestamp.valueOf(km.getThoiGianKetThuc()));
//                ps.setBoolean(6, km.isTrangThai());
//                ps.executeUpdate();
//            }
//
//            // --- 2️⃣ Thêm chi tiết khuyến mãi (đối tượng + chiết khấu)
//            try (PreparedStatement ps = con.prepareStatement(insertCTKM)) {
//                ps.setString(1, km.getMaKhuyenMai());
//                ps.setString(2, doiTuong.name());
//                ps.setDouble(3, chietKhau);
//                ps.executeUpdate();
//            }
//
//            con.commit();
//            con.setAutoCommit(true);
//            return true;
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//            try {
//                con.rollback();
//            } catch (SQLException ex) {
//                ex.printStackTrace();
//            }
//        }
//        return false;
//    }
    public boolean themKhuyenMaiDoiTuong(KhuyenMai km, DoiTuong doiTuong, double chietKhau) {
        Connection con = connectDB.getConnection();
        if (con == null) {
            connectDB.getConnection();
            con = connectDB.getConnection();
        }

        String insertKM = """
        INSERT INTO KhuyenMai(maKhuyenMai, tenKhuyenMai, loaiKhuyenMai, thoiGianBatDau, thoiGianKetThuc, trangThai)
        VALUES (?, ?, ?, ?, ?, ?)
    """;

        // ✅ KHÔNG cần maHoaDon — chỉ thêm thông tin khuyến mãi + đối tượng
        String insertCTKM = """
        INSERT INTO ChiTietKhuyenMai(maKhuyenMai, dieuKien, chietKhau, maHoaDon)
        VALUES (?, ?, ?, NULL)
    """;

        try {
            con.setAutoCommit(false);

            // --- 1️⃣ Thêm khuyến mãi
            try (PreparedStatement ps = con.prepareStatement(insertKM)) {
                ps.setString(1, km.getMaKhuyenMai());
                ps.setString(2, km.getTenKhuyenMai());
                ps.setString(3, km.getLoaiKhuyenMai());
                ps.setTimestamp(4, Timestamp.valueOf(km.getThoiGianBatDau()));
                ps.setTimestamp(5, Timestamp.valueOf(km.getThoiGianKetThuc()));
                ps.setBoolean(6, km.isTrangThai());
                ps.executeUpdate();
            }

            // --- 2️⃣ Thêm chi tiết khuyến mãi (đối tượng + chiết khấu)
            try (PreparedStatement ps = con.prepareStatement(insertCTKM)) {
                ps.setString(1, km.getMaKhuyenMai());
                ps.setString(2, doiTuong.name());
                ps.setDouble(3, chietKhau);
                ps.executeUpdate();
            }

            con.commit();
            con.setAutoCommit(true);
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                con.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return false;
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
    public boolean capNhatKhuyenMai(String maCu, String ten, Date thoiGianBatDau, Date thoiGianKetThuc, double chietKhau) {
        Connection con = connectDB.getConnection();
        if (con == null) {
            System.err.println("❌ Không thể cập nhật — Connection = null (chưa connect SQL)");
            return false;
        }

        PreparedStatement ps = null;
        PreparedStatement psCT = null;
        PreparedStatement psMa = null;
        Statement st = null;
        boolean success = false;

        try {
            con.setAutoCommit(false); // ✅ Bắt đầu transaction
            st = con.createStatement();

            // 🔹 Sinh lại mã mới theo ngày bắt đầu
            String maMoi = KhuyenMai.taoMaKhuyenMaiTheoNgay(thoiGianBatDau, 1);

            // --- Nếu mã mới khác mã cũ → đổi mã trong cả 2 bảng ---
            if (!maCu.equals(maMoi)) {
                System.out.println("🔁 Đổi mã khuyến mãi: " + maCu + " → " + maMoi);

                // ✅ Tạm tắt kiểm tra ràng buộc khóa ngoại
                st.execute("ALTER TABLE ChiTietKhuyenMai NOCHECK CONSTRAINT ALL");

                // ✅ Đổi mã trong bảng KhuyenMai trước (bảng chính)
                String sqlUpdateKM = """
                UPDATE KhuyenMai
                SET maKhuyenMai = ?
                WHERE maKhuyenMai = ?
            """;
                psMa = con.prepareStatement(sqlUpdateKM);
                psMa.setString(1, maMoi);
                psMa.setString(2, maCu);
                int kmRows = psMa.executeUpdate();
                System.out.println("🔹 Đổi mã trong KhuyenMai: " + kmRows + " dòng");

                // ✅ Sau đó đổi mã trong bảng ChiTietKhuyenMai (bảng phụ)
                String sqlUpdateCT = """
                UPDATE ChiTietKhuyenMai
                SET maKhuyenMai = ?
                WHERE maKhuyenMai = ?
            """;
                psMa = con.prepareStatement(sqlUpdateCT);
                psMa.setString(1, maMoi);
                psMa.setString(2, maCu);
                int ctRows = psMa.executeUpdate();
                System.out.println("🔹 Đổi mã trong ChiTietKhuyenMai: " + ctRows + " dòng");

                // ✅ Bật lại kiểm tra FK
                st.execute("ALTER TABLE ChiTietKhuyenMai CHECK CONSTRAINT ALL");

                // Dùng mã mới cho các bước tiếp theo
                maCu = maMoi;
            }

            // --- Cập nhật thông tin khuyến mãi ---
            String sql = """
UPDATE KhuyenMai
SET tenKhuyenMai = ?, 
    thoiGianBatDau = ?, 
    thoiGianKetThuc = ?,
    loaiKhuyenMai = N'Đối Tượng'
WHERE maKhuyenMai = ?
""";


            ps = con.prepareStatement(sql);
            ps.setString(1, ten);
            ps.setDate(2, new java.sql.Date(thoiGianBatDau.getTime()));
            ps.setDate(3, new java.sql.Date(thoiGianKetThuc.getTime()));
            ps.setString(4, maCu);

            int rows = ps.executeUpdate();
            System.out.println("🔹 Cập nhật KhuyenMai: " + rows + " dòng");

            if (rows > 0) {
                // --- Cập nhật chiết khấu ---
                String sqlCT = """
                UPDATE ChiTietKhuyenMai
                SET chietKhau = ?
                WHERE maKhuyenMai = ?
            """;

                psCT = con.prepareStatement(sqlCT);
                psCT.setDouble(1, chietKhau);
                psCT.setString(2, maCu);

                int rowsCT = psCT.executeUpdate();
                System.out.println("🔹 Cập nhật ChiTietKhuyenMai: " + rowsCT + " dòng");

                success = true;
            } else {
                System.out.println("⚠️ Không tìm thấy mã khuyến mãi để cập nhật: " + maCu);
            }

            con.commit(); // ✅ Commit transaction

        } catch (SQLException e) {
            try {
                con.rollback();
                System.err.println("⚠️ Rollback do lỗi khi cập nhật khuyến mãi.");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            System.err.println("❌ Lỗi khi cập nhật khuyến mãi:");
            e.printStackTrace();
        } finally {
            try {
                if (psMa != null) psMa.close();
                if (psCT != null) psCT.close();
                if (ps != null) ps.close();
                if (st != null) st.close();
                con.setAutoCommit(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return success;
    }

    public List<KhuyenMai> locKhuyenMaiTheoDoiTuong(String doiTuong) {
        List<KhuyenMai> list = new ArrayList<>();
        Connection con = connectDB.getConnection();

        if (con == null) {
            connectDB.getConnection();
            con = connectDB.getConnection();
        }

        String sql = """
        SELECT 
            KM.maKhuyenMai,
            KM.tenKhuyenMai,
            CTKM.dieuKien AS doiTuong,
            KM.thoiGianBatDau,
            KM.thoiGianKetThuc,
            CTKM.chietKhau,
            KM.trangThai
        FROM KhuyenMai KM
        JOIN ChiTietKhuyenMai CTKM ON KM.maKhuyenMai = CTKM.maKhuyenMai
    """;

        // Nếu chọn đối tượng cụ thể, thêm điều kiện WHERE
        if (doiTuong != null && !doiTuong.equalsIgnoreCase("Tất cả")) {
            sql += " WHERE LTRIM(RTRIM(CTKM.dieuKien)) COLLATE Latin1_General_CI_AI LIKE ?";
        }

        sql += " ORDER BY KM.thoiGianBatDau DESC";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            if (doiTuong != null && !doiTuong.equalsIgnoreCase("Tất cả")) {
                ps.setString(1, "%" + doiTuong.trim() + "%");
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                KhuyenMai km = new KhuyenMai(
                        rs.getString("maKhuyenMai"),
                        rs.getString("tenKhuyenMai"),
                        "KMHD",
                        rs.getTimestamp("thoiGianBatDau").toLocalDateTime(),
                        rs.getTimestamp("thoiGianKetThuc").toLocalDateTime(),
                        rs.getBoolean("trangThai")
                );
                km.setChietKhau(rs.getDouble("chietKhau"));
                km.setDoiTuongApDung(rs.getString("doiTuong"));
                list.add(km);
            }

            System.out.println("✅ Đã lọc được " + list.size() + " khuyến mãi cho đối tượng: " + doiTuong);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

}