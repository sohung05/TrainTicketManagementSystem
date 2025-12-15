package dao;

/*
 * @description: DAO quản lý khuyến mãi theo đối tượng khách hàng
 * @author :
 * @Date : 10/25/2025
 * @version : 1.1
 */

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import entity.KhuyenMai;
import entity.ChiTietKhuyenMai;
import entity.DoiTuong;
import connectDB.connectDB;

public class KhuyenMaiDoiTuong_DAO {

    public List<Object[]> getDanhSachKhuyenMaiDoiTuong() {
        List<Object[]> list = new ArrayList<>();

        // Kết nối database
        Connection con = connectDB.getConnection();
        if (con == null) {
            connectDB.getConnection();
            con = connectDB.getConnection();
        }

        String sql = """
    SELECT 
        km.maKhuyenMai, 
        km.tenKhuyenMai, 
        ctkm.dieuKien AS doiTuong, 
        km.thoiGianBatDau, 
        km.thoiGianKetThuc, 
        ctkm.chietKhau, 
        km.trangThai
    FROM KhuyenMai km
    JOIN ChiTietKhuyenMai ctkm ON km.maKhuyenMai = ctkm.maKhuyenMai
    WHERE km.loaiKhuyenMai = 'KMKH' 
      AND ctkm.maHoaDon IS NULL
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

            System.out.println("✅ Đã load " + list.size() + " khuyến mãi đối tượng (KMKH).");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

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

public boolean capNhatKhuyenMaiDoiTuong(String maCu, String ten, Date thoiGianBatDau, Date thoiGianKetThuc, double chietKhau, String dieuKien) {
    Connection con = connectDB.getConnection();
    if (con == null) {
        System.err.println("❌ Không thể cập nhật — Connection = null");
        return false;
    }

    boolean success = false;

    try {
        con.setAutoCommit(false);

        // --- Lấy ngày cũ từ DB ---
        Date ngayBatDauCu = null;
        Date ngayKetThucCu = null;
        String sqlGetDates = "SELECT thoiGianBatDau, thoiGianKetThuc FROM KhuyenMai WHERE maKhuyenMai = ?";
        try (PreparedStatement psGet = con.prepareStatement(sqlGetDates)) {
            psGet.setString(1, maCu);
            ResultSet rs = psGet.executeQuery();
            if (rs.next()) {
                ngayBatDauCu = rs.getDate("thoiGianBatDau");
                ngayKetThucCu = rs.getDate("thoiGianKetThuc");
            } else {
                System.out.println("⚠️ Không tìm thấy mã khuyến mãi: " + maCu);
                return false;
            }
        }

        // --- Kiểm tra xem ngày có thay đổi không ---
        boolean ngayThayDoi = !thoiGianBatDau.equals(ngayBatDauCu) || !thoiGianKetThuc.equals(ngayKetThucCu);
        String maMoi = maCu;

        if (ngayThayDoi) {
            // Sinh mã mới theo ngày, đảm bảo không trùng
            int i = 1;
            while (true) {
                maMoi = KhuyenMai.taoMaKhuyenMaiTheoNgay(thoiGianBatDau, i);
                String sqlCheck = "SELECT COUNT(*) FROM KhuyenMai WHERE maKhuyenMai = ?";
                try (PreparedStatement psCheck = con.prepareStatement(sqlCheck)) {
                    psCheck.setString(1, maMoi);
                    ResultSet rsCheck = psCheck.executeQuery();
                    if (rsCheck.next() && rsCheck.getInt(1) == 0) {
                        break; // mã chưa tồn tại
                    }
                }
                i++;
            }

            System.out.println("🔁 Đổi mã khuyến mãi: " + maCu + " → " + maMoi);

            try (Statement st = con.createStatement()) {
                st.execute("ALTER TABLE ChiTietKhuyenMai NOCHECK CONSTRAINT ALL");

                // Đổi mã trong KhuyenMai
                try (PreparedStatement psUpdateKM = con.prepareStatement(
                        "UPDATE KhuyenMai SET maKhuyenMai = ? WHERE maKhuyenMai = ?")) {
                    psUpdateKM.setString(1, maMoi);
                    psUpdateKM.setString(2, maCu);
                    psUpdateKM.executeUpdate();
                }

                // Đổi mã trong ChiTietKhuyenMai
                try (PreparedStatement psUpdateCT = con.prepareStatement(
                        "UPDATE ChiTietKhuyenMai SET maKhuyenMai = ? WHERE maKhuyenMai = ?")) {
                    psUpdateCT.setString(1, maMoi);
                    psUpdateCT.setString(2, maCu);
                    psUpdateCT.executeUpdate();
                }

                st.execute("ALTER TABLE ChiTietKhuyenMai CHECK CONSTRAINT ALL");
            }

            maCu = maMoi; // cập nhật mã mới cho các bước tiếp theo
        }

        // --- Cập nhật thông tin khuyến mãi trong KhuyenMai ---
        String sqlUpdateKM = """
            UPDATE KhuyenMai
            SET tenKhuyenMai = ?, thoiGianBatDau = ?, thoiGianKetThuc = ?, loaiKhuyenMai = N'KMKH'
            WHERE maKhuyenMai = ?
        """;
        try (PreparedStatement ps = con.prepareStatement(sqlUpdateKM)) {
            ps.setString(1, ten);
            ps.setDate(2, new java.sql.Date(thoiGianBatDau.getTime()));
            ps.setDate(3, new java.sql.Date(thoiGianKetThuc.getTime()));
            ps.setString(4, maCu);
            ps.executeUpdate();
        }

        // --- Cập nhật chi tiết khuyến mãi trong ChiTietKhuyenMai ---
        String sqlUpdateCT = "UPDATE ChiTietKhuyenMai SET chietKhau = ?, dieuKien = ? WHERE maKhuyenMai = ?";
        try (PreparedStatement psCT = con.prepareStatement(sqlUpdateCT)) {
            psCT.setDouble(1, chietKhau);
            psCT.setString(2, dieuKien); // cập nhật dieuKien
            psCT.setString(3, maCu);
            psCT.executeUpdate();
        }

        con.commit();
        success = true;

    } catch (SQLException e) {
        try {
            con.rollback();
            System.err.println("⚠️ Rollback do lỗi khi cập nhật khuyến mãi.");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        e.printStackTrace();
    } finally {
        try {
            con.setAutoCommit(true);
            con.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    return success;
}

//    public List<KhuyenMai> locKhuyenMaiTheoDoiTuong(String doiTuong) {
//        List<KhuyenMai> list = new ArrayList<>();
//        Connection con = connectDB.getConnection();
//
//        if (con == null) {
//            connectDB.getConnection();
//            con = connectDB.getConnection();
//        }
//
//        String sql = """
//        SELECT
//            KM.maKhuyenMai,
//            KM.tenKhuyenMai,
//            CTKM.dieuKien AS doiTuong,
//            KM.thoiGianBatDau,
//            KM.thoiGianKetThuc,
//            CTKM.chietKhau,
//            KM.trangThai
//        FROM KhuyenMai KM
//        JOIN ChiTietKhuyenMai CTKM ON KM.maKhuyenMai = CTKM.maKhuyenMai
//    """;
//
//        // Nếu chọn đối tượng cụ thể, thêm điều kiện WHERE
//        if (doiTuong != null && !doiTuong.equalsIgnoreCase("Tất cả")) {
//            sql += " WHERE LTRIM(RTRIM(CTKM.dieuKien)) COLLATE Latin1_General_CI_AI LIKE ?";
//        }
//
//        sql += " ORDER BY KM.thoiGianBatDau DESC";
//
//        try (PreparedStatement ps = con.prepareStatement(sql)) {
//            if (doiTuong != null && !doiTuong.equalsIgnoreCase("Tất cả")) {
//                ps.setString(1, "%" + doiTuong.trim() + "%");
//            }
//
//            ResultSet rs = ps.executeQuery();
//
//            while (rs.next()) {
//                KhuyenMai km = new KhuyenMai(
//                        rs.getString("maKhuyenMai"),
//                        rs.getString("tenKhuyenMai"),
//                        "KMHD",
//                        rs.getTimestamp("thoiGianBatDau").toLocalDateTime(),
//                        rs.getTimestamp("thoiGianKetThuc").toLocalDateTime(),
//                        rs.getBoolean("trangThai")
//                );
//                km.setChietKhau(rs.getDouble("chietKhau"));
//                km.setDoiTuongApDung(rs.getString("doiTuong"));
//                list.add(km);
//            }
//
//            System.out.println("✅ Đã lọc được " + list.size() + " khuyến mãi cho đối tượng: " + doiTuong);
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return list;
//    }
public List<KhuyenMai> locKhuyenMaiTheoDoiTuong(String doiTuong, LocalDateTime fromDate, LocalDateTime toDate) {
    List<KhuyenMai> list = new ArrayList<>();
    Connection con = connectDB.getConnection();

    if (con == null) {
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

    boolean hasWhere = false;

    // Lọc theo đối tượng
    if (doiTuong != null && !doiTuong.equalsIgnoreCase("Tất cả")) {
        sql += " WHERE LTRIM(RTRIM(CTKM.dieuKien)) COLLATE Latin1_General_CI_AI LIKE ?";
        hasWhere = true;
    }

    // Lọc theo khoảng thời gian
    if (fromDate != null) {
        sql += hasWhere ? " AND KM.thoiGianBatDau >= ?" : " WHERE KM.thoiGianBatDau >= ?";
        hasWhere = true;
    }
    if (toDate != null) {
        sql += hasWhere ? " AND KM.thoiGianKetThuc <= ?" : " WHERE KM.thoiGianKetThuc <= ?";
    }

    sql += " ORDER BY KM.thoiGianBatDau DESC";

    try (PreparedStatement ps = con.prepareStatement(sql)) {
        int index = 1;

        if (doiTuong != null && !doiTuong.equalsIgnoreCase("Tất cả")) {
            ps.setString(index++, "%" + doiTuong.trim() + "%");
        }
        if (fromDate != null) {
            ps.setTimestamp(index++, Timestamp.valueOf(fromDate));
        }
        if (toDate != null) {
            ps.setTimestamp(index++, Timestamp.valueOf(toDate));
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

    public boolean kiemTraMaTonTai(String ma) {
        String sql = "SELECT COUNT(*) FROM KhuyenMai WHERE maKhuyenMai = ?";
        try (Connection con = connectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, ma);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // >0 nghĩa là trùng
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}