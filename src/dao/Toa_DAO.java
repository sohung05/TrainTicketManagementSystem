/*
 * @ (#) Toa_DAO.java          1.0        10/26/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package dao;

import entity.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: DAO cho bảng Toa
 * @author: Truong Tran Hung
 * @date: 10/26/2025
 * @version: 1.0
 */
public class Toa_DAO extends BaseDAO {

    // Lấy tất cả toa của một chuyến tàu (cho giao diện chọn toa)
    public List<Toa> getToaBySoHieuTau(String soHieuTau) {
        String sql = "SELECT t.*, " +
                     "       ct.soHieuTau, ct.tocDo, " +
                     "       lt.maLoaiToa, lt.tenLoaiToa " +
                     "FROM Toa t " +
                     "JOIN ChuyenTau ct ON ct.soHieuTau = t.soHieuTau " +
                     "JOIN LoaiToa lt ON lt.maLoaiToa = t.maLoaiToa " +
                     "WHERE t.soHieuTau = ? " +
                     "ORDER BY t.soToa";

        List<Toa> list = new ArrayList<>();
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, soHieuTau);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Toa toa = mapResultSetToToa(rs);
                    list.add(toa);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Lấy tất cả toa
    public List<Toa> findAll() {
        String sql = "SELECT t.*, " +
                     "       ct.soHieuTau, ct.tocDo, " +
                     "       lt.maLoaiToa, lt.tenLoaiToa " +
                     "FROM Toa t " +
                     "JOIN ChuyenTau ct ON ct.soHieuTau = t.soHieuTau " +
                     "JOIN LoaiToa lt ON lt.maLoaiToa = t.maLoaiToa " +
                     "ORDER BY t.soHieuTau, t.soToa";

        List<Toa> list = new ArrayList<>();
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Toa toa = mapResultSetToToa(rs);
                list.add(toa);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Tìm toa theo mã
    public Toa findByMaToa(String maToa) {
        String sql = "SELECT t.*, " +
                     "       ct.soHieuTau, ct.tocDo, " +
                     "       lt.maLoaiToa, lt.tenLoaiToa " +
                     "FROM Toa t " +
                     "JOIN ChuyenTau ct ON ct.soHieuTau = t.soHieuTau " +
                     "JOIN LoaiToa lt ON lt.maLoaiToa = t.maLoaiToa " +
                     "WHERE t.maToa = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maToa);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToToa(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Helper method
    private Toa mapResultSetToToa(ResultSet rs) throws SQLException {
        Toa toa = new Toa();
        toa.setMaToa(rs.getString("maToa"));
        toa.setSoToa(rs.getInt("soToa"));
        
        // Map ChuyenTau (simplified)
        ChuyenTau ct = new ChuyenTau();
        ct.setSoHieuTau(rs.getString("soHieuTau"));
        ct.setTocDo(rs.getDouble("tocDo"));
        toa.setChuyenTau(ct);
        
        // Map LoaiToa
        LoaiToa loaiToa = new LoaiToa();
        loaiToa.setMaLoaiToa(rs.getString("maLoaiToa"));
        loaiToa.setTenLoaiToa(rs.getString("tenLoaiToa"));
        toa.setLoaiToa(loaiToa);
        
        return toa;
    }

    // Thêm toa mới
    public boolean insert(Toa toa) {
        String sql = "INSERT INTO Toa (maToa, soHieuTau, soToa, maLoaiToa) VALUES (?, ?, ?, ?)";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, toa.getMaToa());
            ps.setString(2, toa.getChuyenTau() != null ? toa.getChuyenTau().getSoHieuTau() : null);
            ps.setInt(3, toa.getSoToa());
            ps.setString(4, toa.getLoaiToa() != null ? toa.getLoaiToa().getMaLoaiToa() : null);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Cập nhật toa
    public boolean update(Toa toa) {
        String sql = "UPDATE Toa SET soHieuTau=?, soToa=?, maLoaiToa=? WHERE maToa=?";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, toa.getChuyenTau() != null ? toa.getChuyenTau().getSoHieuTau() : null);
            ps.setInt(2, toa.getSoToa());
            ps.setString(3, toa.getLoaiToa() != null ? toa.getLoaiToa().getMaLoaiToa() : null);
            ps.setString(4, toa.getMaToa());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Xóa toa
    public boolean delete(String maToa) {
        String sql = "DELETE FROM Toa WHERE maToa = ?";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maToa);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}





