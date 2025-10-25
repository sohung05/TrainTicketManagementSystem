package dao;

import connectDB.ConnectDB;
import entity.NhanVien;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class NhanVienDAO {

    public List<NhanVien> getALL() {
        List<NhanVien> ds = new ArrayList<>();
        String sql = "SELECT * FROM NhanVien";

        try (Connection con = ConnectDB.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                NhanVien nv = new NhanVien(
                        rs.getString("maNhanVien"),
                        rs.getString("CCCD"),
                        rs.getString("hoTen"),
                        rs.getString("SDT"),
                        rs.getString("email"),
                        rs.getString("diaChi"),
                        rs.getString("loaiNV"),
                        rs.getBoolean("trangThai"),
                        rs.getDate("ngaySinh").toLocalDate(),
                        rs.getDate("ngayVaoLam").toLocalDate(),
                        rs.getString("gioiTinh")
                );
                ds.add(nv);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    public boolean them(NhanVien nv) {
        String sql = "INSERT INTO NhanVien (maNhanVien, CCCD, hoTen, SDT, email, diaChi, loaiNV, trangThai, ngaySinh, ngayVaoLam, gioiTinh) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nv.getMaNhanVien());
            ps.setString(2, nv.getCCCD());
            ps.setString(3, nv.getHoTen());
            ps.setString(4, nv.getSDT());
            ps.setString(5, nv.getEmail());
            ps.setString(6, nv.getDiaChi());
            ps.setString(7, nv.getLoaiNV());
            ps.setBoolean(8, nv.isTrangThai());
            ps.setDate(9, Date.valueOf(nv.getNgaySinh()));
            ps.setDate(10, Date.valueOf(nv.getNgayVaoLam()));
            ps.setString(11, nv.getGioiTinh());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean sua(NhanVien nv) {
        String sql = "UPDATE NhanVien SET CCCD=?, hoTen=?, SDT=?, email=?, diaChi=?, loaiNV=?, trangThai=?, ngaySinh=?, ngayVaoLam=?, gioiTinh=? "
                + "WHERE maNhanVien=?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nv.getCCCD());
            ps.setString(2, nv.getHoTen());
            ps.setString(3, nv.getSDT());
            ps.setString(4, nv.getEmail());
            ps.setString(5, nv.getDiaChi());
            ps.setString(6, nv.getLoaiNV());
            ps.setBoolean(7, nv.isTrangThai());
            ps.setDate(8, Date.valueOf(nv.getNgaySinh()));
            ps.setDate(9, Date.valueOf(nv.getNgayVaoLam()));
            ps.setString(10, nv.getGioiTinh());
            ps.setString(11, nv.getMaNhanVien());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
