/*
 * @ (#) LichTrinh_DAO.java          1.0        10/26/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package dao;

import entity.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: DAO cho bảng LichTrinh - Quan trọng cho chức năng tìm kiếm vé
 * @author: Truong Tran Hung
 * @date: 10/26/2025
 * @version: 1.0
 */
public class LichTrinh_DAO extends BaseDAO {

    // Tìm lịch trình theo ga đi, ga đến và ngày (cho chức năng tìm vé)
    public List<LichTrinh> timLichTrinh(String tenGaDi, String tenGaDen, LocalDate ngayDi) {
        String sql = "SELECT lt.*, " +
                     "       ct.soHieuTau, ct.tocDo, ct.namSanXuat, " +
                     "       loaiTau.maLoaiTau, loaiTau.tenLoaiTau, " +
                     "       t.maTuyen, t.tenTuyen, t.doDai, " +
                     "       gaDi.maGa AS maGaDi, gaDi.tenGa AS tenGaDi, gaDi.viTri AS viTriGaDi, " +
                     "       gaDen.maGa AS maGaDen, gaDen.tenGa AS tenGaDen, gaDen.viTri AS viTriGaDen " +
                     "FROM LichTrinh lt " +
                     "JOIN ChuyenTau ct ON ct.soHieuTau = lt.soHieuTau " +
                     "JOIN LoaiTau loaiTau ON loaiTau.maLoaiTau = ct.maLoaiTau " +
                     "JOIN Tuyen t ON t.maTuyen = lt.maTuyen " +
                     "JOIN Ga gaDi ON gaDi.maGa = lt.maGaDi " +
                     "JOIN Ga gaDen ON gaDen.maGa = lt.maGaDen " +
                     "WHERE gaDi.tenGa LIKE ? " +
                     "  AND gaDen.tenGa LIKE ? " +
                     "  AND CAST(lt.gioKhoiHanh AS DATE) = ? " +
                     "  AND lt.trangThai = 1 " +
                     "ORDER BY lt.gioKhoiHanh";

        List<LichTrinh> list = new ArrayList<>();
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + tenGaDi + "%");
            ps.setString(2, "%" + tenGaDen + "%");
            ps.setDate(3, Date.valueOf(ngayDi));
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    LichTrinh lt = mapResultSetToLichTrinh(rs);
                    list.add(lt);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Lấy tất cả lịch trình
    public List<LichTrinh> findAll() {
        String sql = "SELECT lt.*, " +
                     "       ct.soHieuTau, ct.tocDo, ct.namSanXuat, " +
                     "       loaiTau.maLoaiTau, loaiTau.tenLoaiTau, " +
                     "       t.maTuyen, t.tenTuyen, t.doDai, " +
                     "       gaDi.maGa AS maGaDi, gaDi.tenGa AS tenGaDi, gaDi.viTri AS viTriGaDi, " +
                     "       gaDen.maGa AS maGaDen, gaDen.tenGa AS tenGaDen, gaDen.viTri AS viTriGaDen " +
                     "FROM LichTrinh lt " +
                     "JOIN ChuyenTau ct ON ct.soHieuTau = lt.soHieuTau " +
                     "JOIN LoaiTau loaiTau ON loaiTau.maLoaiTau = ct.maLoaiTau " +
                     "JOIN Tuyen t ON t.maTuyen = lt.maTuyen " +
                     "JOIN Ga gaDi ON gaDi.maGa = lt.maGaDi " +
                     "JOIN Ga gaDen ON gaDen.maGa = lt.maGaDen " +
                     "ORDER BY lt.gioKhoiHanh DESC";

        List<LichTrinh> list = new ArrayList<>();
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                LichTrinh lt = mapResultSetToLichTrinh(rs);
                list.add(lt);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Tìm lịch trình theo mã
    public LichTrinh findByMaLichTrinh(String maLichTrinh) {
        String sql = "SELECT lt.*, " +
                     "       ct.soHieuTau, ct.tocDo, ct.namSanXuat, " +
                     "       loaiTau.maLoaiTau, loaiTau.tenLoaiTau, " +
                     "       t.maTuyen, t.tenTuyen, t.doDai, " +
                     "       gaDi.maGa AS maGaDi, gaDi.tenGa AS tenGaDi, gaDi.viTri AS viTriGaDi, " +
                     "       gaDen.maGa AS maGaDen, gaDen.tenGa AS tenGaDen, gaDen.viTri AS viTriGaDen " +
                     "FROM LichTrinh lt " +
                     "JOIN ChuyenTau ct ON ct.soHieuTau = lt.soHieuTau " +
                     "JOIN LoaiTau loaiTau ON loaiTau.maLoaiTau = ct.maLoaiTau " +
                     "JOIN Tuyen t ON t.maTuyen = lt.maTuyen " +
                     "JOIN Ga gaDi ON gaDi.maGa = lt.maGaDi " +
                     "JOIN Ga gaDen ON gaDen.maGa = lt.maGaDen " +
                     "WHERE lt.maLichTrinh = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maLichTrinh);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToLichTrinh(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Helper method để map ResultSet sang LichTrinh object
    private LichTrinh mapResultSetToLichTrinh(ResultSet rs) throws SQLException {
        LichTrinh lt = new LichTrinh();
        lt.setMaLichTrinh(rs.getString("maLichTrinh"));
        
        // Map ChuyenTau
        ChuyenTau ct = new ChuyenTau();
        ct.setSoHieuTau(rs.getString("soHieuTau"));
        ct.setTocDo(rs.getDouble("tocDo"));
        Integer namSX = rs.getObject("namSanXuat") != null ? rs.getInt("namSanXuat") : null;
        ct.setNamSanXuat(namSX);
        
        LoaiTau loaiTau = new LoaiTau();
        loaiTau.setMaLoaiTau(rs.getString("maLoaiTau"));
        loaiTau.setTenLoaiTau(rs.getString("tenLoaiTau"));
        ct.setLoaiTau(loaiTau);
        lt.setChuyenTau(ct);
        
        // Map Tuyen
        Tuyen tuyen = new Tuyen();
        tuyen.setMaTuyen(rs.getString("maTuyen"));
        tuyen.setTenTuyen(rs.getString("tenTuyen"));
        tuyen.setDoDai(rs.getDouble("doDai"));
        lt.setTuyen(tuyen);
        
        // Map Ga đi
        Ga gaDi = new Ga();
        gaDi.setMaGa(rs.getString("maGaDi"));
        gaDi.setTenGa(rs.getString("tenGaDi"));
        gaDi.setViTri(rs.getString("viTriGaDi"));
        lt.setGaDi(gaDi);
        
        // Map Ga đến
        Ga gaDen = new Ga();
        gaDen.setMaGa(rs.getString("maGaDen"));
        gaDen.setTenGa(rs.getString("tenGaDen"));
        gaDen.setViTri(rs.getString("viTriGaDen"));
        lt.setGaDen(gaDen);
        
        // Map datetime
        Timestamp gioKH = rs.getTimestamp("gioKhoiHanh");
        lt.setGioKhoiHanh(gioKH != null ? gioKH.toLocalDateTime() : null);
        
        Timestamp gioDen = rs.getTimestamp("gioDenDuKien");
        lt.setGioDenDuKien(gioDen != null ? gioDen.toLocalDateTime() : null);
        
        lt.setTrangThai(rs.getBoolean("trangThai"));
        
        return lt;
    }

    // Thêm lịch trình mới
    public boolean insert(LichTrinh lt) {
        String sql = "INSERT INTO LichTrinh (maLichTrinh, soHieuTau, maTuyen, maGaDi, maGaDen, " +
                     "gioKhoiHanh, gioDenDuKien, trangThai) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, lt.getMaLichTrinh());
            ps.setString(2, lt.getChuyenTau() != null ? lt.getChuyenTau().getSoHieuTau() : null);
            ps.setString(3, lt.getTuyen() != null ? lt.getTuyen().getMaTuyen() : null);
            ps.setString(4, lt.getGaDi() != null ? lt.getGaDi().getMaGa() : null);
            ps.setString(5, lt.getGaDen() != null ? lt.getGaDen().getMaGa() : null);
            ps.setTimestamp(6, lt.getGioKhoiHanh() != null ? Timestamp.valueOf(lt.getGioKhoiHanh()) : null);
            ps.setTimestamp(7, lt.getGioDenDuKien() != null ? Timestamp.valueOf(lt.getGioDenDuKien()) : null);
            ps.setBoolean(8, lt.isTrangThai());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Cập nhật lịch trình
    public boolean update(LichTrinh lt) {
        String sql = "UPDATE LichTrinh SET soHieuTau=?, maTuyen=?, maGaDi=?, maGaDen=?, " +
                     "gioKhoiHanh=?, gioDenDuKien=?, trangThai=? WHERE maLichTrinh=?";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, lt.getChuyenTau() != null ? lt.getChuyenTau().getSoHieuTau() : null);
            ps.setString(2, lt.getTuyen() != null ? lt.getTuyen().getMaTuyen() : null);
            ps.setString(3, lt.getGaDi() != null ? lt.getGaDi().getMaGa() : null);
            ps.setString(4, lt.getGaDen() != null ? lt.getGaDen().getMaGa() : null);
            ps.setTimestamp(5, lt.getGioKhoiHanh() != null ? Timestamp.valueOf(lt.getGioKhoiHanh()) : null);
            ps.setTimestamp(6, lt.getGioDenDuKien() != null ? Timestamp.valueOf(lt.getGioDenDuKien()) : null);
            ps.setBoolean(7, lt.isTrangThai());
            ps.setString(8, lt.getMaLichTrinh());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Xóa lịch trình (soft delete)
    public boolean delete(String maLichTrinh) {
        String sql = "UPDATE LichTrinh SET trangThai = 0 WHERE maLichTrinh = ?";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maLichTrinh);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}





