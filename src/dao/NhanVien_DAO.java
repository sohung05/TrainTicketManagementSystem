/*
 * @ (#) NhanVien_DAO.java          1.0        10/26/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package dao;

import entity.NhanVien;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: DAO cho bảng NhanVien
 * @author: Truong Tran Hung
 * @date: 10/26/2025
 * @version: 2.0 (Updated for SQLTaoDULieu.sql - camelCase + chucVu)
 */
public class NhanVien_DAO extends BaseDAO {

    // Tìm nhân viên theo mã
    public NhanVien findByMaNV(String maNV) {
        String sql = "SELECT * FROM NhanVien WHERE maNhanVien = ?";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maNV);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToNhanVien(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Lấy tất cả nhân viên
    public List<NhanVien> findAll() {
        String sql = "SELECT * FROM NhanVien ORDER BY maNhanVien";
        List<NhanVien> list = new ArrayList<>();
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                NhanVien nv = mapResultSetToNhanVien(rs);
                list.add(nv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Helper method
    private NhanVien mapResultSetToNhanVien(ResultSet rs) throws SQLException {
        NhanVien nv = new NhanVien();
        nv.setMaNhanVien(rs.getString("maNhanVien"));
        nv.setCccd(rs.getString("CCCD"));
        nv.setHoTen(rs.getString("hoTen"));
        nv.setSdt(rs.getString("SDT"));
        nv.setEmail(rs.getString("email"));
        nv.setDiaChi(rs.getString("diaChi"));
        nv.setChucVu(rs.getBoolean("chucVu")); // BIT: true = Nhân viên, false = Quản lý
        
        Date ngaySinh = rs.getDate("ngaySinh");
        nv.setNgaySinh(ngaySinh != null ? ngaySinh.toLocalDate() : null);
        
        Date ngayVaoLam = rs.getDate("ngayVaoLam");
        nv.setNgayVaoLam(ngayVaoLam != null ? ngayVaoLam.toLocalDate() : null);
        
        nv.setTrangThai(rs.getBoolean("trangThai"));
        
        return nv;
    }

    // Thêm nhân viên
    public boolean insert(NhanVien nv) {
        String sql = "INSERT INTO NhanVien (maNhanVien, CCCD, hoTen, SDT, email, diaChi, " +
                     "chucVu, ngaySinh, ngayVaoLam, trangThai) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nv.getMaNhanVien());
            ps.setString(2, nv.getCccd());
            ps.setString(3, nv.getHoTen());
            ps.setString(4, nv.getSdt());
            ps.setString(5, nv.getEmail());
            ps.setString(6, nv.getDiaChi());
            ps.setBoolean(7, nv.isChucVu());
            ps.setDate(8, nv.getNgaySinh() != null ? Date.valueOf(nv.getNgaySinh()) : null);
            ps.setDate(9, nv.getNgayVaoLam() != null ? Date.valueOf(nv.getNgayVaoLam()) : null);
            ps.setBoolean(10, nv.isTrangThai());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Cập nhật nhân viên
    public boolean update(NhanVien nv) {
        String sql = "UPDATE NhanVien SET CCCD=?, hoTen=?, SDT=?, email=?, diaChi=?, " +
                     "chucVu=?, ngaySinh=?, ngayVaoLam=?, trangThai=? " +
                     "WHERE maNhanVien=?";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nv.getCccd());
            ps.setString(2, nv.getHoTen());
            ps.setString(3, nv.getSdt());
            ps.setString(4, nv.getEmail());
            ps.setString(5, nv.getDiaChi());
            ps.setBoolean(6, nv.isChucVu());
            ps.setDate(7, nv.getNgaySinh() != null ? Date.valueOf(nv.getNgaySinh()) : null);
            ps.setDate(8, nv.getNgayVaoLam() != null ? Date.valueOf(nv.getNgayVaoLam()) : null);
            ps.setBoolean(9, nv.isTrangThai());
            ps.setString(10, nv.getMaNhanVien());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
