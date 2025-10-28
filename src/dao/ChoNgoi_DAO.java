/*
 * @ (#) ChoNgoi_DAO.java          1.0        10/26/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package dao;
import connectDB.connectDB;

import entity.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: DAO cho bảng ChoNgoi - Quan trọng cho việc chọn ghế
 * @author: Truong Tran Hung
 * @date: 10/26/2025
 * @version: 1.0
 */
public class ChoNgoi_DAO {

    // Lấy tất cả chỗ ngồi của một toa (cho giao diện sơ đồ ghế)
    public List<ChoNgoi> getChoNgoiByMaToa(String maToa) {
        String sql = "SELECT cn.*, t.maToa, t.soToa, t.soHieuTau " +
                     "FROM ChoNgoi cn " +
                     "JOIN Toa t ON t.maToa = cn.maToa " +
                     "WHERE cn.maToa = ? " +
                     "ORDER BY cn.viTri";

        List<ChoNgoi> list = new ArrayList<>();
        try (Connection con = connectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maToa);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ChoNgoi cn = mapResultSetToChoNgoi(rs);
                    list.add(cn);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Kiểm tra chỗ ngồi đã được đặt chưa trong một lịch trình cụ thể
    public boolean kiemTraChoNgoiDaDat(String maChoNgoi, String maLichTrinh) {
        String sql = "SELECT COUNT(*) FROM Ve " +
                     "WHERE maChoNgoi = ? AND maLichTrinh = ? AND trangThai = 1";
        try (Connection con = connectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maChoNgoi);
            ps.setString(2, maLichTrinh);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // True nếu đã có vé (đã đặt)
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Lấy danh sách chỗ ngồi trống của một toa trong lịch trình
    public List<ChoNgoi> getChoNgoiTrong(String maToa, String maLichTrinh) {
        String sql = "SELECT cn.*, t.maToa, t.soToa, t.soHieuTau " +
                     "FROM ChoNgoi cn " +
                     "JOIN Toa t ON t.maToa = cn.maToa " +
                     "WHERE cn.maToa = ? " +
                     "  AND NOT EXISTS ( " +
                     "      SELECT 1 FROM Ve " +
                     "      WHERE Ve.maChoNgoi = cn.maChoNgoi " +
                     "        AND Ve.maLichTrinh = ? " +
                     "        AND Ve.trangThai = 1 " +
                     "  ) " +
                     "ORDER BY cn.viTri";

        List<ChoNgoi> list = new ArrayList<>();
        try (Connection con = connectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maToa);
            ps.setString(2, maLichTrinh);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ChoNgoi cn = mapResultSetToChoNgoi(rs);
                    list.add(cn);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Lấy tất cả chỗ ngồi
    public List<ChoNgoi> findAll() {
        String sql = "SELECT cn.*, t.maToa, t.soToa, t.soHieuTau " +
                     "FROM ChoNgoi cn " +
                     "JOIN Toa t ON t.maToa = cn.maToa " +
                     "ORDER BY t.soHieuTau, t.soToa, cn.viTri";

        List<ChoNgoi> list = new ArrayList<>();
        try (Connection con = connectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ChoNgoi cn = mapResultSetToChoNgoi(rs);
                list.add(cn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Tìm chỗ ngồi theo mã
    public ChoNgoi findByMaChoNgoi(String maChoNgoi) {
        String sql = "SELECT cn.*, t.maToa, t.soToa, t.soHieuTau " +
                     "FROM ChoNgoi cn " +
                     "JOIN Toa t ON t.maToa = cn.maToa " +
                     "WHERE cn.maChoNgoi = ?";

        try (Connection con = connectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maChoNgoi);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToChoNgoi(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Helper method
    private ChoNgoi mapResultSetToChoNgoi(ResultSet rs) throws SQLException {
        ChoNgoi cn = new ChoNgoi();
        cn.setMaChoNgoi(rs.getString("maChoNgoi"));
        // ❌ XÓA: setLoaiChoNgoi - không có trong SQLTaoDULieu
        cn.setMoTa(rs.getString("moTa"));
        cn.setViTri(rs.getInt("viTri"));
        cn.setGia(rs.getDouble("gia"));
        
        // Map Toa (simplified)
        Toa toa = new Toa();
        toa.setMaToa(rs.getString("maToa"));
        toa.setSoToa(rs.getInt("soToa"));
        
        ChuyenTau ct = new ChuyenTau();
        ct.setSoHieuTau(rs.getString("soHieuTau"));
        toa.setChuyenTau(ct);
        
        cn.setToa(toa);
        
        return cn;
    }

    // Thêm chỗ ngồi mới
    public boolean insert(ChoNgoi cn) {
        String sql = "INSERT INTO ChoNgoi (maChoNgoi, maToa, moTa, viTri, gia) " +
                     "VALUES (?, ?, ?, ?, ?)";
        try (Connection con = connectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cn.getMaChoNgoi());
            ps.setString(2, cn.getToa() != null ? cn.getToa().getMaToa() : null);
            ps.setString(3, cn.getMoTa());
            ps.setInt(4, cn.getViTri());
            ps.setDouble(5, cn.getGia());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Cập nhật chỗ ngồi
    public boolean update(ChoNgoi cn) {
        String sql = "UPDATE ChoNgoi SET maToa=?, moTa=?, viTri=?, gia=? " +
                     "WHERE maChoNgoi=?";
        try (Connection con = connectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cn.getToa() != null ? cn.getToa().getMaToa() : null);
            ps.setString(2, cn.getMoTa());
            ps.setInt(3, cn.getViTri());
            ps.setDouble(4, cn.getGia());
            ps.setString(5, cn.getMaChoNgoi());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Xóa chỗ ngồi
    public boolean delete(String maChoNgoi) {
        String sql = "DELETE FROM ChoNgoi WHERE maChoNgoi = ?";
        try (Connection con = connectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maChoNgoi);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}





