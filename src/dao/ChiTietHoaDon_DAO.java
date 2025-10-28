/*
 * @ (#) ChiTietHoaDon_DAO.java          1.0        10/28/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package dao;
import connectDB.connectDB;

import entity.ChiTietHoaDon;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO cho bảng ChiTietHoaDon
 * @author AI Assistant
 * @date 10/28/2025
 * @version 1.0
 */
public class ChiTietHoaDon_DAO {

    /**
     * Lấy danh sách chi tiết hóa đơn theo mã hóa đơn
     */
    public List<ChiTietHoaDon> findByMaHoaDon(String maHoaDon) {
        String sql = "SELECT * FROM ChiTietHoaDon WHERE maHoaDon = ?";
        List<ChiTietHoaDon> list = new ArrayList<>();
        
        try (Connection con = connectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, maHoaDon);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ChiTietHoaDon cthd = new ChiTietHoaDon();
                    cthd.setMaHoaDon(rs.getString("maHoaDon"));
                    cthd.setMaVe(rs.getString("maVe"));
                    cthd.setSoLuong(rs.getInt("soLuong"));
                    cthd.setGiaVe(rs.getDouble("giaVe"));
                    cthd.setMucGiam(rs.getDouble("mucGiam"));
                    list.add(cthd);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Thêm chi tiết hóa đơn mới
     */
    public boolean insert(ChiTietHoaDon cthd) {
        String sql = "INSERT INTO ChiTietHoaDon (maHoaDon, maVe, soLuong, giaVe, mucGiam) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection con = connectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, cthd.getMaHoaDon());
            ps.setString(2, cthd.getMaVe());
            ps.setInt(3, cthd.getSoLuong());
            ps.setDouble(4, cthd.getGiaVe());
            ps.setDouble(5, cthd.getMucGiam());
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Xóa chi tiết hóa đơn
     */
    public boolean delete(String maHoaDon, String maVe) {
        String sql = "DELETE FROM ChiTietHoaDon WHERE maHoaDon = ? AND maVe = ?";
        
        try (Connection con = connectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, maHoaDon);
            ps.setString(2, maVe);
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Xóa tất cả chi tiết hóa đơn của một hóa đơn
     */
    public boolean deleteByMaHoaDon(String maHoaDon) {
        String sql = "DELETE FROM ChiTietHoaDon WHERE maHoaDon = ?";
        
        try (Connection con = connectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, maHoaDon);
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}


