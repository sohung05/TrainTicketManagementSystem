/*
 * @ (#) LoaiVe_DAO.java          1.0        10/26/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package dao;

import connectDB.connectDB;
import entity.LoaiVe;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: DAO cho bảng LoaiVe
 * @author: Truong Tran Hung
 * @date: 10/26/2025
 * @version: 1.0
 */
public class LoaiVe_DAO {

    // Lấy tất cả loại vé
    public List<LoaiVe> findAll() {
        String sql = "SELECT * FROM LoaiVe ORDER BY tenLoaiVe";
        List<LoaiVe> list = new ArrayList<>();
        try (Connection con = connectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                LoaiVe lv = new LoaiVe();
                lv.setMaLoaiVe(rs.getString("maLoaiVe"));
                lv.setTenLoaiVe(rs.getString("tenLoaiVe"));
                lv.setMucGiamGia(rs.getFloat("mucGiamGia"));
                list.add(lv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Tìm loại vé theo mã
    public LoaiVe findByMaLoaiVe(String maLoaiVe) {
        String sql = "SELECT * FROM LoaiVe WHERE maLoaiVe = ?";
        try (Connection con = connectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maLoaiVe);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    LoaiVe lv = new LoaiVe();
                    lv.setMaLoaiVe(rs.getString("maLoaiVe"));
                    lv.setTenLoaiVe(rs.getString("tenLoaiVe"));
                    lv.setMucGiamGia(rs.getFloat("mucGiamGia"));
                    return lv;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // Tìm loại vé theo tên
    public LoaiVe findByTenLoaiVe(String tenLoaiVe) {
        String sql = "SELECT * FROM LoaiVe WHERE tenLoaiVe = ?";
        try (Connection con = connectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, tenLoaiVe);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    LoaiVe lv = new LoaiVe();
                    lv.setMaLoaiVe(rs.getString("maLoaiVe"));
                    lv.setTenLoaiVe(rs.getString("tenLoaiVe"));
                    lv.setMucGiamGia(rs.getFloat("mucGiamGia"));
                    return lv;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Thêm loại vé mới
    public boolean insert(LoaiVe lv) {
        String sql = "INSERT INTO LoaiVe (maLoaiVe, tenLoaiVe, mucGiamGia) VALUES (?, ?, ?)";
        try (Connection con = connectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, lv.getMaLoaiVe());
            ps.setString(2, lv.getTenLoaiVe());
            ps.setFloat(3, lv.getMucGiamGia());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Cập nhật loại vé
    public boolean update(LoaiVe lv) {
        String sql = "UPDATE LoaiVe SET tenLoaiVe=?, mucGiamGia=? WHERE maLoaiVe=?";
        try (Connection con = connectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, lv.getTenLoaiVe());
            ps.setFloat(2, lv.getMucGiamGia());
            ps.setString(3, lv.getMaLoaiVe());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Xóa loại vé
    public boolean delete(String maLoaiVe) {
        String sql = "DELETE FROM LoaiVe WHERE maLoaiVe = ?";
        try (Connection con = connectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maLoaiVe);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}





