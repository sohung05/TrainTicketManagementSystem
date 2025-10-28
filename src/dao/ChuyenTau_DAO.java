/*
 * @ (#) ChuyenTau_DAO.java          1.0        10/26/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package dao;

import entity.ChuyenTau;
import entity.LoaiTau;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: DAO cho bảng ChuyenTau
 * @author: Truong Tran Hung
 * @date: 10/26/2025
 * @version: 1.0
 */
public class ChuyenTau_DAO extends BaseDAO {

    // Lấy tất cả chuyến tàu
    public List<ChuyenTau> findAll() {
        String sql = "SELECT ct.*, lt.maLoaiTau, lt.tenLoaiTau " +
                     "FROM ChuyenTau ct " +
                     "LEFT JOIN LoaiTau lt ON lt.maLoaiTau = ct.maLoaiTau " +
                     "ORDER BY ct.soHieuTau";
        List<ChuyenTau> list = new ArrayList<>();
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ChuyenTau ct = new ChuyenTau();
                ct.setSoHieuTau(rs.getString("soHieuTau"));
                ct.setTocDo(rs.getDouble("tocDo"));
                
                // Load LoaiTau
                LoaiTau loaiTau = new LoaiTau();
                loaiTau.setMaLoaiTau(rs.getString("maLoaiTau"));
                loaiTau.setTenLoaiTau(rs.getString("tenLoaiTau"));
                ct.setLoaiTau(loaiTau);
                
                Integer namSX = rs.getObject("namSanXuat") != null ? rs.getInt("namSanXuat") : null;
                ct.setNamSanXuat(namSX);
                list.add(ct);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Tìm chuyến tàu theo số hiệu
    public ChuyenTau findBySoHieuTau(String soHieuTau) {
        String sql = "SELECT ct.*, lt.maLoaiTau, lt.tenLoaiTau " +
                     "FROM ChuyenTau ct " +
                     "LEFT JOIN LoaiTau lt ON lt.maLoaiTau = ct.maLoaiTau " +
                     "WHERE ct.soHieuTau = ?";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, soHieuTau);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ChuyenTau ct = new ChuyenTau();
                    ct.setSoHieuTau(rs.getString("soHieuTau"));
                    ct.setTocDo(rs.getDouble("tocDo"));
                    
                    LoaiTau loaiTau = new LoaiTau();
                    loaiTau.setMaLoaiTau(rs.getString("maLoaiTau"));
                    loaiTau.setTenLoaiTau(rs.getString("tenLoaiTau"));
                    ct.setLoaiTau(loaiTau);
                    
                    Integer namSX = rs.getObject("namSanXuat") != null ? rs.getInt("namSanXuat") : null;
                    ct.setNamSanXuat(namSX);
                    return ct;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Thêm chuyến tàu mới
    public boolean insert(ChuyenTau ct) {
        String sql = "INSERT INTO ChuyenTau (soHieuTau, tocDo, maLoaiTau, namSanXuat) " +
                     "VALUES (?, ?, ?, ?)";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, ct.getSoHieuTau());
            ps.setDouble(2, ct.getTocDo());
            ps.setString(3, ct.getLoaiTau() != null ? ct.getLoaiTau().getMaLoaiTau() : null);
            if (ct.getNamSanXuat() != null) {
                ps.setInt(4, ct.getNamSanXuat());
            } else {
                ps.setNull(4, java.sql.Types.INTEGER);
            }
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Cập nhật chuyến tàu
    public boolean update(ChuyenTau ct) {
        String sql = "UPDATE ChuyenTau SET tocDo=?, maLoaiTau=?, namSanXuat=? " +
                     "WHERE soHieuTau=?";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, ct.getTocDo());
            ps.setString(2, ct.getLoaiTau() != null ? ct.getLoaiTau().getMaLoaiTau() : null);
            if (ct.getNamSanXuat() != null) {
                ps.setInt(3, ct.getNamSanXuat());
            } else {
                ps.setNull(3, java.sql.Types.INTEGER);
            }
            ps.setString(4, ct.getSoHieuTau());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Xóa chuyến tàu
    public boolean delete(String soHieuTau) {
        String sql = "DELETE FROM ChuyenTau WHERE soHieuTau = ?";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, soHieuTau);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}





