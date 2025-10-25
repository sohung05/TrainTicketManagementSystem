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
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HoaDon_DAO extends BaseDAO {

    public List<HoaDon> findAll() {
        String sql =
                "SELECT hd.MaHoaDon, hd.MaNhanVien, kh.CCCD, kh.HoTen, kh.SDT, " +
                        "       STRING_AGG(km.TenKhuyenMai, ', ') WITHIN GROUP (ORDER BY km.TenKhuyenMai) AS TenKM, " +
                        "       hd.NgayTao, hd.GioTao, " +
                        "       ISNULL(SUM(ct.SoLuong * ct.GiaVe - ct.MucGiam), 0) AS TongTien " +
                        "FROM HoaDon hd " +
                        "JOIN KhachHang kh ON kh.MaKH = hd.MaKH " +
                        "LEFT JOIN ChiTietHoaDon ct ON ct.MaHoaDon = hd.MaHoaDon " +
                        "LEFT JOIN ChiTietKhuyenMai ctkm ON ctkm.MaHoaDon = hd.MaHoaDon " +
                        "LEFT JOIN KhuyenMai km ON km.MaKhuyenMai = ctkm.MaKhuyenMai " +
                        "GROUP BY hd.MaHoaDon, hd.MaNhanVien, kh.CCCD, kh.HoTen, kh.SDT, hd.NgayTao, hd.GioTao " +
                        "ORDER BY hd.NgayTao DESC, hd.GioTao DESC";

        List<HoaDon> list = new ArrayList<>();
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                HoaDon hd = new HoaDon();
                hd.setMaHoaDon(rs.getString("MaHoaDon"));
                hd.setMaNhanVien(rs.getString("MaNhanVien"));
                hd.setCccd(rs.getString("CCCD"));
                hd.setTenKhachHang(rs.getString("HoTen"));
                hd.setSdt(rs.getString("SDT"));
                hd.setKhuyenMai(rs.getString("TenKM"));
                Timestamp n = rs.getTimestamp("NgayTao");
                Timestamp g = rs.getTimestamp("GioTao");
                hd.setNgayTao(n != null ? n.toLocalDateTime() : null);
                hd.setGioTao(g != null ? g.toLocalDateTime() : null);
                hd.setTongTien(rs.getDouble("TongTien"));
                list.add(hd);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
