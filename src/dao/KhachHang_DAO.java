/*
 * @ (#) KhachHang_DAO.java          1.0        10/26/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package dao;

import entity.KhachHang;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: DAO cho bảng KhachHang
 * @author: Truong Tran Hung
 * @date: 10/26/2025
 * @version: 1.0
 */
public class KhachHang_DAO extends BaseDAO {

    // Lấy tất cả khách hàng
    public List<KhachHang> findAll() {
        String sql = "SELECT * FROM KhachHang ORDER BY hoTen";
        List<KhachHang> list = new ArrayList<>();
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                KhachHang kh = mapResultSetToKhachHang(rs);
                list.add(kh);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Tìm khách hàng theo mã
    public KhachHang findByMaKH(String maKH) {
        String sql = "SELECT * FROM KhachHang WHERE maKH = ?";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maKH);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToKhachHang(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Tìm khách hàng theo CCCD
    public KhachHang findByCCCD(String cccd) {
        String sql = "SELECT * FROM KhachHang WHERE CCCD = ?";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cccd);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToKhachHang(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Tìm khách hàng theo SĐT
    public KhachHang findBySDT(String sdt) {
        String sql = "SELECT * FROM KhachHang WHERE SDT = ?";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, sdt);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToKhachHang(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Tìm khách hàng theo tên (hỗ trợ tìm kiếm gần đúng)
    public List<KhachHang> findByHoTen(String hoTen) {
        String sql = "SELECT * FROM KhachHang WHERE hoTen LIKE ? ORDER BY hoTen";
        List<KhachHang> list = new ArrayList<>();
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + hoTen + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    KhachHang kh = mapResultSetToKhachHang(rs);
                    list.add(kh);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Helper method
    private KhachHang mapResultSetToKhachHang(ResultSet rs) throws SQLException {
        KhachHang kh = new KhachHang();
        kh.setMaKH(rs.getString("maKH"));
        kh.setCccd(rs.getString("CCCD"));
        kh.setHoTen(rs.getString("hoTen"));
        kh.setEmail(rs.getString("email"));
        kh.setSdt(rs.getString("SDT"));
        kh.setDoiTuong(rs.getString("doiTuong"));
        return kh;
    }

    // Thêm khách hàng mới
    public boolean insert(KhachHang kh) {
        String sql = "INSERT INTO KhachHang (maKH, CCCD, hoTen, email, SDT, doiTuong) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, kh.getMaKH());
            ps.setString(2, kh.getCccd());
            ps.setString(3, kh.getHoTen());
            ps.setString(4, kh.getEmail());
            ps.setString(5, kh.getSdt());
            ps.setString(6, kh.getDoiTuong());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Cập nhật khách hàng
    public boolean update(KhachHang kh) {
        String sql = "UPDATE KhachHang SET CCCD=?, hoTen=?, email=?, SDT=?, doiTuong=? " +
                     "WHERE maKH=?";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, kh.getCccd());
            ps.setString(2, kh.getHoTen());
            ps.setString(3, kh.getEmail());
            ps.setString(4, kh.getSdt());
            ps.setString(5, kh.getDoiTuong());
            ps.setString(6, kh.getMaKH());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Xóa khách hàng
    public boolean delete(String maKH) {
        String sql = "DELETE FROM KhachHang WHERE maKH = ?";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maKH);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Tạo mã khách hàng tự động
    public String generateMaKH() {
        String sql = "SELECT TOP 1 maKH FROM KhachHang ORDER BY maKH DESC";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                String lastMa = rs.getString("maKH");
                // Giả sử mã có dạng KH001, KH002...
                int num = Integer.parseInt(lastMa.substring(2)) + 1;
                return String.format("KH%03d", num);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "KH001"; // Mã đầu tiên
    }
}





