/*
 * @ (#) Ga_DAO.java          1.0        10/26/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package dao;

import connectDB.connectDB;
import entity.Ga;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: DAO cho bảng Ga
 * @author: Truong Tran Hung
 * @date: 10/26/2025
 * @version: 1.0
 */
public class Ga_DAO {

    // Lấy tất cả ga
    public List<Ga> findAll() {
        String sql = "SELECT * FROM Ga ORDER BY tenGa";
        List<Ga> list = new ArrayList<>();
        try (Connection con = connectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Ga ga = new Ga();
                ga.setMaGa(rs.getString("maGa"));
                ga.setTenGa(rs.getString("tenGa"));
                ga.setViTri(rs.getString("viTri"));
                list.add(ga);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Tìm ga theo mã
    public Ga findByMaGa(String maGa) {
        String sql = "SELECT * FROM Ga WHERE maGa = ?";
        try (Connection con = connectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maGa);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Ga ga = new Ga();
                    ga.setMaGa(rs.getString("maGa"));
                    ga.setTenGa(rs.getString("tenGa"));
                    ga.setViTri(rs.getString("viTri"));
                    return ga;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Tìm ga theo tên (hỗ trợ tìm kiếm gần đúng)
    public List<Ga> findByTenGa(String tenGa) {
        String sql = "SELECT * FROM Ga WHERE tenGa LIKE ? ORDER BY tenGa";
        List<Ga> list = new ArrayList<>();
        try (Connection con = connectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + tenGa + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Ga ga = new Ga();
                    ga.setMaGa(rs.getString("maGa"));
                    ga.setTenGa(rs.getString("tenGa"));
                    ga.setViTri(rs.getString("viTri"));
                    list.add(ga);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Thêm ga mới
    public boolean insert(Ga ga) {
        String sql = "INSERT INTO Ga (maGa, tenGa, viTri) VALUES (?, ?, ?)";
        try (Connection con = connectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, ga.getMaGa());
            ps.setString(2, ga.getTenGa());
            ps.setString(3, ga.getViTri());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Cập nhật thông tin ga
    public boolean update(Ga ga) {
        String sql = "UPDATE Ga SET tenGa=?, viTri=? WHERE maGa=?";
        try (Connection con = connectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, ga.getTenGa());
            ps.setString(2, ga.getViTri());
            ps.setString(3, ga.getMaGa());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Xóa ga
    public boolean delete(String maGa) {
        String sql = "DELETE FROM Ga WHERE maGa = ?";
        try (Connection con = connectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maGa);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}





