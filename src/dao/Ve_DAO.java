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
import entity.Ve;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Ve_DAO extends BaseDAO {

    // Lấy danh sách vé cho bảng "Bảng Vé"
    public List<Ve> findAll() {
        String sql =
                "SELECT v.MaVe, v.SoCCCD, v.TenKhachHang, kh.DoiTuong, " +
                        "       g1.TenGa AS GaDi, g2.TenGa AS GaDen, " +
                        "       ct.SoHieuTau AS MaTau, toa.SoToa, CAST(cn.ViTri AS NVARCHAR(20)) AS ViTriCho, " +
                        "       v.ThoiGianLenTau, v.GiaVe " +
                        "FROM Ve v " +
                        "LEFT JOIN KhachHang kh ON kh.MaKH = v.MaKH " +
                        "LEFT JOIN LichTrinh lt ON lt.MaLichTrinh = v.MaLichTrinh " +
                        "LEFT JOIN Ga g1 ON g1.MaGa = lt.MaGaDi " +
                        "LEFT JOIN Ga g2 ON g2.MaGa = lt.MaGaDen " +
                        "LEFT JOIN ChoNgoi cn ON cn.MaChoNgoi = v.MaChoNgoi " +
                        "LEFT JOIN Toa toa ON toa.MaToa = cn.MaToa " +
                        "LEFT JOIN ChuyenTau ct ON ct.SoHieuTau = toa.SoHieuTau " +
                        "ORDER BY v.ThoiGianLenTau DESC";

        List<Ve> list = new ArrayList<>();
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Ve ve = new Ve();
                ve.setMaVe(rs.getString("MaVe"));
                ve.setCccd(rs.getString("SoCCCD"));
                ve.setTenKhachHang(rs.getString("TenKhachHang"));
                ve.setDoiTuong(rs.getString("DoiTuong"));
                ve.setGaDi(rs.getString("GaDi"));
                ve.setGaDen(rs.getString("GaDen"));
                ve.setMaTau(rs.getString("MaTau"));
                ve.setSoToa(rs.getInt("SoToa"));
                ve.setViTriCho(rs.getString("ViTriCho"));
                Timestamp tg = rs.getTimestamp("ThoiGianLenTau");
                ve.setThoiGianLenTau(tg != null ? tg.toLocalDateTime() : null);
                ve.setGia(rs.getDouble("GiaVe"));
                list.add(ve);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
