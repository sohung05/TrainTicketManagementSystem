package dao;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import connectDB.connectDB;
import entity.KhuyenMai;

public class KhuyenMaiHoaDon_DAO {

    public List<KhuyenMai> getTatCaKhuyenMaiHoaDon() {
        List<KhuyenMai> list = new ArrayList<>();

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
                String soLuongVe = rs.getString("soLuongVe"); // điều kiện (ví dụ "11-40 vé")

                double chietKhau = rs.getDouble("chietKhau");
                // 🔹 Chuẩn hóa chietKhau: nếu > 1, chia 100
                if (chietKhau > 1) {
                    chietKhau = chietKhau / 100.0;
                }

                KhuyenMai km = new KhuyenMai(maKM, tenKM, loaiKM, tgBatDau, tgKetThuc, trangThai);
                km.setDoiTuongApDung(soLuongVe); // Lưu điều kiện
                km.setChietKhau(chietKhau);      // ✅ giờ không lỗi nữa

                list.add(km);
            }

            System.out.println("✅ Đã load " + list.size() + " khuyến mãi hóa đơn (KMHD).");

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("⚠️ Lỗi khi load danh sách khuyến mãi hóa đơn: " + e.getMessage());
        }

        return list;
    }

    public boolean themKhuyenMaiHoaDon(KhuyenMai km, String soVeKhongDung, double chietKhau) {
        try (Connection con = connectDB.getConnection()) {
            if (con == null) {
                System.err.println("❌ Không thể kết nối cơ sở dữ liệu!");
                return false;
            }

            String insertKM = """
            INSERT INTO KhuyenMai(maKhuyenMai, tenKhuyenMai, loaiKhuyenMai, 
                                  thoiGianBatDau, thoiGianKetThuc, trangThai)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

            String insertCTKM = """
            INSERT INTO ChiTietKhuyenMai(maHoaDon, maKhuyenMai, dieuKien, chietKhau)
            VALUES (NULL, ?, ?, ?)
        """;

            con.setAutoCommit(false);

            // === 1. THÊM KhuyenMai ===
            try (PreparedStatement ps = con.prepareStatement(insertKM)) {
                ps.setString(1, km.getMaKhuyenMai());
                ps.setString(2, km.getTenKhuyenMai());
                ps.setString(3, km.getLoaiKhuyenMai());
                ps.setTimestamp(4, Timestamp.valueOf(km.getThoiGianBatDau()));
                ps.setTimestamp(5, Timestamp.valueOf(km.getThoiGianKetThuc()));
                ps.setBoolean(6, km.isTrangThai());
                ps.executeUpdate();
            }

            // === 2. THÊM ChiTietKhuyenMai ===
            try (PreparedStatement ps = con.prepareStatement(insertCTKM)) {
                ps.setString(1, km.getMaKhuyenMai());
                ps.setString(2, String.valueOf(soVeKhongDung)); // số vé khả dụng → lưu vào dieuKien
                ps.setDouble(3, chietKhau); // lưu 0.x hoặc 10 tùy bạn
                ps.executeUpdate();
            }

            con.commit();
            return true;

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

    public List<KhuyenMai> locKhuyenMaiHoaDon(String keyword, LocalDate startDate, LocalDate endDate) {
        List<KhuyenMai> ds = new ArrayList<>();
        Connection con = connectDB.getConnection();

        String sql = """
        SELECT maKhuyenMai, tenKhuyenMai, loaiKhuyenMai,
               thoiGianBatDau, thoiGianKetThuc, trangThai
        FROM KhuyenMai
        WHERE tenKhuyenMai LIKE ?
          AND thoiGianBatDau <= ?
          AND thoiGianKetThuc >= ?
        ORDER BY thoiGianBatDau DESC
    """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");

            ps.setTimestamp(2, Timestamp.valueOf(endDate.atStartOfDay()));   // ngày kết thúc lọc
            ps.setTimestamp(3, Timestamp.valueOf(startDate.atStartOfDay())); // ngày bắt đầu lọc

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


    public boolean capNhatKhuyenMaiHoaDon(String maKMCu, String maKMMoi, String ten,
                                      Date thoiGianBatDau, Date thoiGianKetThuc,
                                      double chietKhau, String dieuKien) {
    Connection con = connectDB.getConnection();
    PreparedStatement psDisableFK = null;
    PreparedStatement psUpdateCT = null;
    PreparedStatement psUpdateKM = null;
    PreparedStatement psEnableFK = null;
    boolean success = false;

    try {
        con.setAutoCommit(false);

        // 1️⃣ Tắt khóa ngoại
        psDisableFK = con.prepareStatement("ALTER TABLE ChiTietKhuyenMai NOCHECK CONSTRAINT ALL");
        psDisableFK.execute();

        // 2️⃣ Update ChiTietKhuyenMai
        String sqlUpdateCT = """
            UPDATE ChiTietKhuyenMai
            SET maKhuyenMai = ?, dieuKien = ?, chietKhau = ?
            WHERE maKhuyenMai = ?
        """;
        psUpdateCT = con.prepareStatement(sqlUpdateCT);
        psUpdateCT.setString(1, maKMMoi);
        psUpdateCT.setString(2, dieuKien);  // 🔹 lưu nguyên chuỗi
        psUpdateCT.setDouble(3, chietKhau);
        psUpdateCT.setString(4, maKMCu);
        psUpdateCT.executeUpdate();

        // 3️⃣ Update KhuyenMai
        String sqlUpdateKM = """
            UPDATE KhuyenMai
            SET maKhuyenMai = ?, tenKhuyenMai = ?, thoiGianBatDau = ?, thoiGianKetThuc = ?, loaiKhuyenMai = N'KMHD'
            WHERE maKhuyenMai = ?
        """;
        psUpdateKM = con.prepareStatement(sqlUpdateKM);
        psUpdateKM.setString(1, maKMMoi);
        psUpdateKM.setString(2, ten);
        psUpdateKM.setDate(3, thoiGianBatDau);
        psUpdateKM.setDate(4, thoiGianKetThuc);
        psUpdateKM.setString(5, maKMCu);
        psUpdateKM.executeUpdate();

        // 4️⃣ Bật lại FK
        psEnableFK = con.prepareStatement("ALTER TABLE ChiTietKhuyenMai WITH CHECK CHECK CONSTRAINT ALL");
        psEnableFK.execute();

        con.commit();
        success = true;

    } catch (SQLException e) {
        e.printStackTrace();
        try { con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
    } finally {
        try {
            if(psDisableFK != null) psDisableFK.close();
            if(psUpdateCT != null) psUpdateCT.close();
            if(psUpdateKM != null) psUpdateKM.close();
            if(psEnableFK != null) psEnableFK.close();
            con.setAutoCommit(true);
        } catch (SQLException ex) { ex.printStackTrace(); }
    }
    return success;
}

}