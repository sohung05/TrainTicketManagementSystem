/*
 * @ (#) Ve_DAO.java          1.0        10/25/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package dao;

/*
 * @description:
 * @author: Truong Tran Hung
 * @date: 10/25/2025
 * @version:    1.0
 */
import connectDB.connectDB;
import entity.Ve;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Ve_DAO {

    // Lấy danh sách vé (đầy đủ thông tin)
    public List<Ve> findAll() {
        String sql =
                "SELECT v.maVe, v.maLoaiVe, v.maVach, v.thoiGianLenTau, v.giaVe, " +
                        "       v.maKH, v.maChoNgoi, v.maLichTrinh, v.trangThai, " +
                        "       v.tenKhachHang, v.soCCCD " +
                        "FROM Ve v " +
                        "ORDER BY v.thoiGianLenTau DESC";

        List<Ve> list = new ArrayList<>();
        try (Connection con = connectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Ve ve = new Ve();
                ve.setMaVe(rs.getString("maVe"));
                // TODO: Load LoaiVe, KhachHang, ChoNgoi, LichTrinh từ DAO khác nếu cần
                ve.setMaVach(rs.getString("maVach"));
                Timestamp tg = rs.getTimestamp("thoiGianLenTau");
                ve.setThoiGianLenTau(tg != null ? tg.toLocalDateTime() : null);
                ve.setGiaVe(rs.getDouble("giaVe"));
                ve.setTrangThai(rs.getBoolean("trangThai"));
                ve.setTenKhachHang(rs.getString("tenKhachHang"));
                ve.setSoCCCD(rs.getString("soCCCD"));
                list.add(ve);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Tìm vé theo mã (với JOIN để lấy đầy đủ thông tin)
    public Ve findByMaVe(String maVe) {
        String sql = 
            "SELECT v.*, " +
            "       lv.maLoaiVe, lv.tenLoaiVe, lv.mucGiamGia, " +
            "       lt.maLichTrinh, lt.soHieuTau, lt.gioKhoiHanh, " +
            "       gaDi.maGa as maGaDi, gaDi.tenGa as tenGaDi, " +
            "       gaDen.maGa as maGaDen, gaDen.tenGa as tenGaDen, " +
            "       cn.maChoNgoi, cn.viTri, " +
            "       toa.maToa, toa.soToa " +
            "FROM Ve v " +
            "LEFT JOIN LoaiVe lv ON v.maLoaiVe = lv.maLoaiVe " +
            "LEFT JOIN LichTrinh lt ON v.maLichTrinh = lt.maLichTrinh " +
            "LEFT JOIN Ga gaDi ON lt.maGaDi = gaDi.maGa " +
            "LEFT JOIN Ga gaDen ON lt.maGaDen = gaDen.maGa " +
            "LEFT JOIN ChoNgoi cn ON v.maChoNgoi = cn.maChoNgoi " +
            "LEFT JOIN Toa toa ON cn.maToa = toa.maToa " +
            "WHERE v.maVe = ? AND v.trangThai = 1";
        
        try (Connection con = connectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maVe);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Ve ve = new Ve();
                    ve.setMaVe(rs.getString("maVe"));
                    ve.setMaVach(rs.getString("maVach"));
                    Timestamp tg = rs.getTimestamp("thoiGianLenTau");
                    ve.setThoiGianLenTau(tg != null ? tg.toLocalDateTime() : null);
                    ve.setGiaVe(rs.getDouble("giaVe"));
                    ve.setTrangThai(rs.getBoolean("trangThai"));
                    ve.setTenKhachHang(rs.getString("tenKhachHang"));
                    ve.setSoCCCD(rs.getString("soCCCD"));
                    
                    // Load LoaiVe
                    if (rs.getString("maLoaiVe") != null) {
                        entity.LoaiVe loaiVe = new entity.LoaiVe();
                        loaiVe.setMaLoaiVe(rs.getString("maLoaiVe"));
                        loaiVe.setTenLoaiVe(rs.getString("tenLoaiVe"));
                        loaiVe.setMucGiamGia(rs.getFloat("mucGiamGia"));
                        ve.setLoaiVe(loaiVe);
                    }
                    
                    // Load LichTrinh (với Ga đi, Ga đến, ChuyenTau)
                    if (rs.getString("maLichTrinh") != null) {
                        entity.LichTrinh lichTrinh = new entity.LichTrinh();
                        lichTrinh.setMaLichTrinh(rs.getString("maLichTrinh"));
                        
                        Timestamp gioKH = rs.getTimestamp("gioKhoiHanh");
                        lichTrinh.setGioKhoiHanh(gioKH != null ? gioKH.toLocalDateTime() : null);
                        
                        // Ga đi
                        if (rs.getString("maGaDi") != null) {
                            entity.Ga gaDi = new entity.Ga();
                            gaDi.setMaGa(rs.getString("maGaDi"));
                            gaDi.setTenGa(rs.getString("tenGaDi"));
                            lichTrinh.setGaDi(gaDi);
                        }
                        
                        // Ga đến
                        if (rs.getString("maGaDen") != null) {
                            entity.Ga gaDen = new entity.Ga();
                            gaDen.setMaGa(rs.getString("maGaDen"));
                            gaDen.setTenGa(rs.getString("tenGaDen"));
                            lichTrinh.setGaDen(gaDen);
                        }
                        
                        // ChuyenTau
                        if (rs.getString("soHieuTau") != null) {
                            entity.ChuyenTau chuyenTau = new entity.ChuyenTau();
                            chuyenTau.setSoHieuTau(rs.getString("soHieuTau"));
                            lichTrinh.setChuyenTau(chuyenTau);
                        }
                        
                        ve.setLichTrinh(lichTrinh);
                    }
                    
                    // Load ChoNgoi (với Toa)
                    if (rs.getString("maChoNgoi") != null) {
                        entity.ChoNgoi choNgoi = new entity.ChoNgoi();
                        choNgoi.setMaChoNgoi(rs.getString("maChoNgoi"));
                        choNgoi.setViTri(rs.getInt("viTri"));
                        
                        // Toa
                        if (rs.getString("maToa") != null) {
                            entity.Toa toa = new entity.Toa();
                            toa.setMaToa(rs.getString("maToa"));
                            toa.setSoToa(rs.getInt("soToa"));
                            choNgoi.setToa(toa);
                        }
                        
                        ve.setChoNgoi(choNgoi);
                    }
                    
                    return ve;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Thêm vé mới
    public boolean insert(Ve ve) {
        String sql = "INSERT INTO Ve (maVe, maLoaiVe, maVach, thoiGianLenTau, giaVe, " +
                "maKH, maChoNgoi, maLichTrinh, trangThai, tenKhachHang, soCCCD) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = connectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, ve.getMaVe());
            ps.setString(2, ve.getLoaiVe() != null ? ve.getLoaiVe().getMaLoaiVe() : null);
            ps.setString(3, ve.getMaVach());
            ps.setTimestamp(4, ve.getThoiGianLenTau() != null ?
                    Timestamp.valueOf(ve.getThoiGianLenTau()) : null);
            ps.setDouble(5, ve.getGiaVe());
            ps.setString(6, ve.getKhachHang() != null ? ve.getKhachHang().getMaKH() : null);
            ps.setString(7, ve.getChoNgoi() != null ? ve.getChoNgoi().getMaChoNgoi() : null);
            ps.setString(8, ve.getLichTrinh() != null ? ve.getLichTrinh().getMaLichTrinh() : null);
            ps.setBoolean(9, ve.isTrangThai());
            ps.setString(10, ve.getTenKhachHang());
            ps.setString(11, ve.getSoCCCD());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Cập nhật vé
    public boolean update(Ve ve) {
        String sql = "UPDATE Ve SET maLoaiVe=?, maVach=?, thoiGianLenTau=?, giaVe=?, " +
                "maKH=?, maChoNgoi=?, maLichTrinh=?, trangThai=?, tenKhachHang=?, soCCCD=? " +
                "WHERE maVe=?";
        try (Connection con = connectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, ve.getLoaiVe() != null ? ve.getLoaiVe().getMaLoaiVe() : null);
            ps.setString(2, ve.getMaVach());
            ps.setTimestamp(3, ve.getThoiGianLenTau() != null ?
                    Timestamp.valueOf(ve.getThoiGianLenTau()) : null);
            ps.setDouble(4, ve.getGiaVe());
            ps.setString(5, ve.getKhachHang() != null ? ve.getKhachHang().getMaKH() : null);
            ps.setString(6, ve.getChoNgoi() != null ? ve.getChoNgoi().getMaChoNgoi() : null);
            ps.setString(7, ve.getLichTrinh() != null ? ve.getLichTrinh().getMaLichTrinh() : null);
            ps.setBoolean(8, ve.isTrangThai());
            ps.setString(9, ve.getTenKhachHang());
            ps.setString(10, ve.getSoCCCD());
            ps.setString(11, ve.getMaVe());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Xóa vé (soft delete - đổi trạng thái)
    public boolean delete(String maVe) {
        String sql = "UPDATE Ve SET trangThai = 0 WHERE maVe = ?";
        try (Connection con = connectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maVe);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Kiểm tra xem một ghế đã được đặt cho lịch trình cụ thể hay chưa
     * @param maChoNgoi Mã chỗ ngồi
     * @param maLichTrinh Mã lịch trình
     * @return true nếu ghế đã được đặt, false nếu còn trống
     */
    public boolean kiemTraGheDaDat(String maChoNgoi, String maLichTrinh) {
        String sql = "SELECT COUNT(*) FROM Ve WHERE maChoNgoi = ? AND maLichTrinh = ? AND trangThai = 1";
        try (Connection con = connectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maChoNgoi);
            ps.setString(2, maLichTrinh);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // Nếu có ít nhất 1 vé => đã đặt
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Mặc định là chưa đặt nếu có lỗi
    }
    
    /**
     * Lấy danh sách MÃ CHỖ NGỒI đã được đặt trong 1 lịch trình (TỐI ƯU - chỉ query 1 lần)
     * @param maLichTrinh Mã lịch trình
     * @return Set chứa các MaChoNgoi đã đặt
     */
    public java.util.Set<String> layDanhSachGheDaDat(String maLichTrinh) {
        String sql = "SELECT maChoNgoi FROM Ve WHERE maLichTrinh = ? AND trangThai = 1";
        java.util.Set<String> gheDaDat = new java.util.HashSet<>();
        try (Connection con = connectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maLichTrinh);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String maChoNgoi = rs.getString("maChoNgoi");
                    if (maChoNgoi != null) {
                        gheDaDat.add(maChoNgoi);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return gheDaDat;
    }
    
    /**
     * Tìm vé theo mã vé hoặc CCCD
     * @param keyword Từ khóa tìm kiếm (Mã vé hoặc CCCD)
     * @return Danh sách vé tìm thấy
     */
    public java.util.List<Ve> searchVe(String keyword) {
        String sql = 
            "SELECT v.*, " +
            "       lv.maLoaiVe, lv.tenLoaiVe, lv.mucGiamGia, " +
            "       lt.maLichTrinh, lt.soHieuTau, lt.gioKhoiHanh, " +
            "       gaDi.maGa as maGaDi, gaDi.tenGa as tenGaDi, " +
            "       gaDen.maGa as maGaDen, gaDen.tenGa as tenGaDen, " +
            "       cn.maChoNgoi, cn.viTri, " +
            "       toa.maToa, toa.soToa " +
            "FROM Ve v " +
            "LEFT JOIN LoaiVe lv ON v.maLoaiVe = lv.maLoaiVe " +
            "LEFT JOIN LichTrinh lt ON v.maLichTrinh = lt.maLichTrinh " +
            "LEFT JOIN Ga gaDi ON lt.maGaDi = gaDi.maGa " +
            "LEFT JOIN Ga gaDen ON lt.maGaDen = gaDen.maGa " +
            "LEFT JOIN ChoNgoi cn ON v.maChoNgoi = cn.maChoNgoi " +
            "LEFT JOIN Toa toa ON cn.maToa = toa.maToa " +
            "WHERE (v.maVe LIKE ? OR v.soCCCD LIKE ?) AND v.trangThai = 1 " +
            "ORDER BY v.thoiGianLenTau DESC";
        
        java.util.List<Ve> list = new java.util.ArrayList<>();
        try (Connection con = connectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            String searchPattern = "%" + keyword + "%";
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Ve ve = new Ve();
                    ve.setMaVe(rs.getString("maVe"));
                    ve.setMaVach(rs.getString("maVach"));
                    Timestamp tg = rs.getTimestamp("thoiGianLenTau");
                    ve.setThoiGianLenTau(tg != null ? tg.toLocalDateTime() : null);
                    ve.setGiaVe(rs.getDouble("giaVe"));
                    ve.setTrangThai(rs.getBoolean("trangThai"));
                    ve.setTenKhachHang(rs.getString("tenKhachHang"));
                    ve.setSoCCCD(rs.getString("soCCCD"));
                    
                    // Load LoaiVe
                    if (rs.getString("maLoaiVe") != null) {
                        entity.LoaiVe loaiVe = new entity.LoaiVe();
                        loaiVe.setMaLoaiVe(rs.getString("maLoaiVe"));
                        loaiVe.setTenLoaiVe(rs.getString("tenLoaiVe"));
                        loaiVe.setMucGiamGia(rs.getFloat("mucGiamGia"));
                        ve.setLoaiVe(loaiVe);
                    }
                    
                    // Load LichTrinh
                    if (rs.getString("maLichTrinh") != null) {
                        entity.LichTrinh lichTrinh = new entity.LichTrinh();
                        lichTrinh.setMaLichTrinh(rs.getString("maLichTrinh"));
                        
                        Timestamp gioKH = rs.getTimestamp("gioKhoiHanh");
                        lichTrinh.setGioKhoiHanh(gioKH != null ? gioKH.toLocalDateTime() : null);
                        
                        // Ga đi
                        if (rs.getString("maGaDi") != null) {
                            entity.Ga gaDi = new entity.Ga();
                            gaDi.setMaGa(rs.getString("maGaDi"));
                            gaDi.setTenGa(rs.getString("tenGaDi"));
                            lichTrinh.setGaDi(gaDi);
                        }
                        
                        // Ga đến
                        if (rs.getString("maGaDen") != null) {
                            entity.Ga gaDen = new entity.Ga();
                            gaDen.setMaGa(rs.getString("maGaDen"));
                            gaDen.setTenGa(rs.getString("tenGaDen"));
                            lichTrinh.setGaDen(gaDen);
                        }
                        
                        // ChuyenTau
                        if (rs.getString("soHieuTau") != null) {
                            entity.ChuyenTau chuyenTau = new entity.ChuyenTau();
                            chuyenTau.setSoHieuTau(rs.getString("soHieuTau"));
                            lichTrinh.setChuyenTau(chuyenTau);
                        }
                        
                        ve.setLichTrinh(lichTrinh);
                    }
                    
                    // Load ChoNgoi
                    if (rs.getString("maChoNgoi") != null) {
                        entity.ChoNgoi choNgoi = new entity.ChoNgoi();
                        choNgoi.setMaChoNgoi(rs.getString("maChoNgoi"));
                        choNgoi.setViTri(rs.getInt("viTri"));
                        
                        // Toa
                        if (rs.getString("maToa") != null) {
                            entity.Toa toa = new entity.Toa();
                            toa.setMaToa(rs.getString("maToa"));
                            toa.setSoToa(rs.getInt("soToa"));
                            choNgoi.setToa(toa);
                        }
                        
                        ve.setChoNgoi(choNgoi);
                    }
                    
                    list.add(ve);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
