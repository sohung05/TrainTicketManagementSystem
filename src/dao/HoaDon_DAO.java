/*
 * @ (#) HoaDon_DAO.java          1.0        10/25/2025
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
import entity.HoaDon;
import entity.NhanVien;
import entity.KhachHang;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HoaDon_DAO extends BaseDAO {

    public List<HoaDon> findAll() {
        // ✅ Query đơn giản hơn - Đọc tongTien trực tiếp từ database
        String sql =
                "SELECT hd.maHoaDon, hd.maNhanVien, hd.tongTien, hd.ngayTao, hd.gioTao, " +
                        "       kh.CCCD, kh.hoTen, kh.SDT, " +
                        "       STRING_AGG(km.tenKhuyenMai, ', ') WITHIN GROUP (ORDER BY km.tenKhuyenMai) AS tenKM " +
                        "FROM HoaDon hd " +
                        "JOIN KhachHang kh ON kh.maKH = hd.maKH " +
                        "LEFT JOIN ChiTietKhuyenMai ctkm ON ctkm.maHoaDon = hd.maHoaDon " +
                        "LEFT JOIN KhuyenMai km ON km.maKhuyenMai = ctkm.maKhuyenMai " +
                        "GROUP BY hd.maHoaDon, hd.maNhanVien, hd.tongTien, kh.CCCD, kh.hoTen, kh.SDT, hd.ngayTao, hd.gioTao " +
                        "ORDER BY hd.ngayTao DESC, hd.gioTao DESC";

        List<HoaDon> list = new ArrayList<>();
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                HoaDon hd = new HoaDon();
                hd.setMaHoaDon(rs.getString("maHoaDon"));
                
                // Tạo NhanVien object tạm với chỉ có mã
                NhanVien nv = new NhanVien();
                nv.setMaNhanVien(rs.getString("maNhanVien"));
                hd.setNhanVien(nv);
                
                // Tạo KhachHang object tạm với thông tin cơ bản
                KhachHang kh = new KhachHang();
                kh.setCccd(rs.getString("CCCD"));
                kh.setHoTen(rs.getString("hoTen"));
                kh.setSdt(rs.getString("SDT"));
                hd.setKhachHang(kh);
                
                // Set các field tạm thời
                hd.setKhuyenMai(rs.getString("tenKM"));
                hd.setTongTien(rs.getDouble("tongTien"));
                
                Timestamp n = rs.getTimestamp("ngayTao");
                Timestamp g = rs.getTimestamp("gioTao");
                hd.setNgayTao(n != null ? n.toLocalDateTime() : null);
                hd.setGioTao(g != null ? g.toLocalDateTime() : null);
                hd.setTrangThai(true);
                list.add(hd);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    /**
     * Thêm hóa đơn mới với tongTien
     * @param hoaDon Hóa đơn cần thêm (đã có tongTien)
     * @return true nếu thành công
     */
    public boolean insertHoaDon(HoaDon hoaDon) {
        String sql = "INSERT INTO HoaDon (maHoaDon, maNhanVien, maKH, ngayTao, gioTao, tongTien, trangThai) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, hoaDon.getMaHoaDon());
            ps.setString(2, hoaDon.getNhanVien() != null ? hoaDon.getNhanVien().getMaNhanVien() : null);
            ps.setString(3, hoaDon.getKhachHang() != null ? hoaDon.getKhachHang().getMaKH() : null);
            ps.setTimestamp(4, hoaDon.getNgayTao() != null ? 
                              Timestamp.valueOf(hoaDon.getNgayTao()) : null);
            ps.setTimestamp(5, hoaDon.getGioTao() != null ? 
                              Timestamp.valueOf(hoaDon.getGioTao()) : null);
            ps.setDouble(6, hoaDon.getTongTien());
            ps.setBoolean(7, hoaDon.isTrangThai());
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Cập nhật tongTien cho hóa đơn
     * @param maHoaDon Mã hóa đơn cần cập nhật
     * @param tongTien Tổng tiền mới
     * @return true nếu thành công
     */
    public boolean updateTongTien(String maHoaDon, double tongTien) {
        String sql = "UPDATE HoaDon SET tongTien = ? WHERE maHoaDon = ?";
        
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setDouble(1, tongTien);
            ps.setString(2, maHoaDon);
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Tính lại và cập nhật tongTien cho hóa đơn từ chi tiết
     * Dùng khi cần sync lại dữ liệu
     * @param maHoaDon Mã hóa đơn
     * @return true nếu thành công
     */
    public boolean recalculateTongTien(String maHoaDon) {
        String sql = 
            "UPDATE HoaDon " +
            "SET tongTien = ( " +
            "    SELECT ISNULL(SUM(ct.soLuong * ct.giaVe - ct.mucGiam), 0) " +
            "    FROM ChiTietHoaDon ct " +
            "    WHERE ct.maHoaDon = ? " +
            ") " +
            "WHERE maHoaDon = ?";
        
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, maHoaDon);
            ps.setString(2, maHoaDon);
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Tìm hóa đơn theo mã hóa đơn
     * @param maHoaDon Mã hóa đơn cần tìm
     * @return HoaDon hoặc null nếu không tìm thấy
     */
    public HoaDon findByMaHoaDon(String maHoaDon) {
        String sql =
                "SELECT hd.maHoaDon, hd.maNhanVien, hd.tongTien, hd.ngayTao, hd.gioTao, hd.trangThai, " +
                "       kh.maKH, kh.CCCD, kh.hoTen, kh.SDT, kh.email " +
                "FROM HoaDon hd " +
                "JOIN KhachHang kh ON kh.maKH = hd.maKH " +
                "WHERE hd.maHoaDon = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, maHoaDon);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    HoaDon hd = new HoaDon();
                    hd.setMaHoaDon(rs.getString("maHoaDon"));
                    
                    // NhanVien
                    NhanVien nv = new NhanVien();
                    nv.setMaNhanVien(rs.getString("maNhanVien"));
                    hd.setNhanVien(nv);
                    
                    // KhachHang
                    KhachHang kh = new KhachHang();
                    kh.setMaKH(rs.getString("maKH"));
                    kh.setCccd(rs.getString("CCCD"));
                    kh.setHoTen(rs.getString("hoTen"));
                    kh.setSdt(rs.getString("SDT"));
                    kh.setEmail(rs.getString("email"));
                    hd.setKhachHang(kh);
                    
                    // Các field khác
                    hd.setTongTien(rs.getDouble("tongTien"));
                    
                    Timestamp n = rs.getTimestamp("ngayTao");
                    Timestamp g = rs.getTimestamp("gioTao");
                    hd.setNgayTao(n != null ? n.toLocalDateTime() : null);
                    hd.setGioTao(g != null ? g.toLocalDateTime() : null);
                    hd.setTrangThai(rs.getBoolean("trangThai"));
                    
                    return hd;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Tìm hóa đơn theo mã hóa đơn, CCCD hoặc SĐT
     * @param keyword Từ khóa tìm kiếm
     * @return Danh sách hóa đơn tìm thấy
     */
    public List<HoaDon> searchHoaDon(String keyword) {
        String sql =
                "SELECT hd.maHoaDon, hd.maNhanVien, hd.tongTien, hd.ngayTao, hd.gioTao, " +
                "       kh.CCCD, kh.hoTen, kh.SDT, " +
                "       STRING_AGG(km.tenKhuyenMai, ', ') WITHIN GROUP (ORDER BY km.tenKhuyenMai) AS tenKM " +
                "FROM HoaDon hd " +
                "JOIN KhachHang kh ON kh.maKH = hd.maKH " +
                "LEFT JOIN ChiTietKhuyenMai ctkm ON ctkm.maHoaDon = hd.maHoaDon " +
                "LEFT JOIN KhuyenMai km ON km.maKhuyenMai = ctkm.maKhuyenMai " +
                "WHERE hd.maHoaDon LIKE ? OR kh.CCCD LIKE ? OR kh.SDT LIKE ? " +
                "GROUP BY hd.maHoaDon, hd.maNhanVien, hd.tongTien, kh.CCCD, kh.hoTen, kh.SDT, hd.ngayTao, hd.gioTao " +
                "ORDER BY hd.ngayTao DESC, hd.gioTao DESC";

        List<HoaDon> list = new ArrayList<>();
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            String searchPattern = "%" + keyword + "%";
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);
            ps.setString(3, searchPattern);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    HoaDon hd = new HoaDon();
                    hd.setMaHoaDon(rs.getString("maHoaDon"));
                    
                    // Tạo NhanVien object tạm với chỉ có mã
                    NhanVien nv = new NhanVien();
                    nv.setMaNhanVien(rs.getString("maNhanVien"));
                    hd.setNhanVien(nv);
                    
                    // Tạo KhachHang object tạm với thông tin cơ bản
                    KhachHang kh = new KhachHang();
                    kh.setCccd(rs.getString("CCCD"));
                    kh.setHoTen(rs.getString("hoTen"));
                    kh.setSdt(rs.getString("SDT"));
                    hd.setKhachHang(kh);
                    
                    // Set các field tạm thời
                    hd.setKhuyenMai(rs.getString("tenKM"));
                    hd.setTongTien(rs.getDouble("tongTien"));
                    
                    Timestamp n = rs.getTimestamp("ngayTao");
                    Timestamp g = rs.getTimestamp("gioTao");
                    hd.setNgayTao(n != null ? n.toLocalDateTime() : null);
                    hd.setGioTao(g != null ? g.toLocalDateTime() : null);
                    hd.setTrangThai(true);
                    list.add(hd);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    /**
     * Cập nhật lại TongTien của hóa đơn dựa trên các vé còn lại (trangThai = 1)
     * @param maHoaDon Mã hóa đơn cần cập nhật
     * @return true nếu thành công, false nếu thất bại
     */
    public boolean capNhatTongTien(String maHoaDon) {
        String sql = "UPDATE HoaDon " +
                    "SET tongTien = (" +
                    "    SELECT ISNULL(SUM(v.giaVe), 0) " +
                    "    FROM Ve v " +
                    "    JOIN ChiTietHoaDon cthd ON cthd.maVe = v.maVe " +
                    "    WHERE cthd.maHoaDon = ? AND v.trangThai = 1" +
                    ") " +
                    "WHERE maHoaDon = ?";
        
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maHoaDon);
            ps.setString(2, maHoaDon);
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
