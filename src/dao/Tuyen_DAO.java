/*
 * @ (#) Tuyen_DAO.java          1.0        10/26/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package dao;

import connectDB.connectDB;
import entity.Tuyen;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: DAO cho bảng Tuyen
 * @author: Truong Tran Hung
 * @date: 10/26/2025
 * @version: 1.0
 */
public class Tuyen_DAO {

    // Lấy tất cả tuyến
    public List<Tuyen> findAll() {
        String sql = "SELECT * FROM Tuyen ORDER BY tenTuyen";
        List<Tuyen> list = new ArrayList<>();
        try (Connection con = connectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Tuyen tuyen = new Tuyen();
                tuyen.setMaTuyen(rs.getString("maTuyen"));
                tuyen.setTenTuyen(rs.getString("tenTuyen"));
                tuyen.setDoDai(rs.getDouble("doDai"));
                list.add(tuyen);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Tìm tuyến theo mã
    public Tuyen findByMaTuyen(String maTuyen) {
        String sql = "SELECT * FROM Tuyen WHERE maTuyen = ?";
        try (Connection con = connectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maTuyen);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Tuyen tuyen = new Tuyen();
                    tuyen.setMaTuyen(rs.getString("maTuyen"));
                    tuyen.setTenTuyen(rs.getString("tenTuyen"));
                    tuyen.setDoDai(rs.getDouble("doDai"));
                    return tuyen;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Thêm tuyến mới
    public boolean insert(Tuyen tuyen) {
        String sql = "INSERT INTO Tuyen (maTuyen, tenTuyen, doDai) VALUES (?, ?, ?)";
        try (Connection con = connectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, tuyen.getMaTuyen());
            ps.setString(2, tuyen.getTenTuyen());
            ps.setDouble(3, tuyen.getDoDai());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Cập nhật tuyến
    public boolean update(Tuyen tuyen) {
        String sql = "UPDATE Tuyen SET tenTuyen=?, doDai=? WHERE maTuyen=?";
        try (Connection con = connectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, tuyen.getTenTuyen());
            ps.setDouble(2, tuyen.getDoDai());
            ps.setString(3, tuyen.getMaTuyen());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Xóa tuyến
    public boolean delete(String maTuyen) {
        String sql = "DELETE FROM Tuyen WHERE maTuyen = ?";
        try (Connection con = connectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maTuyen);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}





