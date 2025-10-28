package dao;

import connectDB.connectDB;
import entity.KhachHang;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KhachHang_DAO {

    public List<KhachHang> getAll() {
        List<KhachHang> ds = new ArrayList<>();
        String sql = "SELECT * FROM KhachHang";

        Connection con = connectDB.getConnection();
        if (con == null) {
            System.err.println("Không thể lấy connection trong getAll()");
            return ds;
        }

        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                KhachHang kh = new KhachHang(
                        rs.getString("maKH"),
                        rs.getString("CCCD"),
                        rs.getString("hoTen"),
                        rs.getString("email"),
                        rs.getString("SDT"),
                        rs.getString("doiTuong")
                );
                ds.add(kh);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    public boolean them(KhachHang kh) {
        String sql = "INSERT INTO KhachHang (maKH, CCCD, hoTen, email, SDT, doiTuong) VALUES (?, ?, ?, ?, ?, ?)";
        Connection con = connectDB.getConnection();
        if (con == null) {
            System.err.println("Không thể lấy connection trong them()");
            return false;
        }

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, kh.getMaKH());
            ps.setString(2, kh.getCCCD());
            ps.setString(3, kh.getHoTen());
            ps.setString(4, kh.getEmail());
            ps.setString(5, kh.getSDT());
            ps.setString(6, kh.getDoiTuong());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean sua(KhachHang kh) {
        String sql = "UPDATE KhachHang SET CCCD=?, hoTen=?, email=?, SDT=?, doiTuong=? WHERE maKH=?";
        Connection con = connectDB.getConnection();
        if (con == null) {
            System.err.println("Không thể lấy connection trong sua()");
            return false;
        }

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, kh.getCCCD());
            ps.setString(2, kh.getHoTen());
            ps.setString(3, kh.getEmail());
            ps.setString(4, kh.getSDT());
            ps.setString(5, kh.getDoiTuong());
            ps.setString(6, kh.getMaKH());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean exists(String maKH) {
        String sql = "SELECT 1 FROM KhachHang WHERE maKH = ?";
        Connection con = connectDB.getConnection();
        if (con == null) {
            System.err.println("Không thể lấy connection trong exists()");
            return false;
        }

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maKH);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); // Nếu có kết quả -> đã tồn tại
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public KhachHang findByCCCD(String cccd) {
        String sql = "SELECT * FROM KhachHang WHERE CCCD = ?";
        Connection con = connectDB.getConnection();
        if (con == null) {
            System.err.println("Không thể lấy connection trong findByCCCD()");
            return null;
        }

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cccd);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new KhachHang(
                            rs.getString("maKH"),
                            rs.getString("CCCD"),
                            rs.getString("hoTen"),
                            rs.getString("email"),
                            rs.getString("SDT"),
                            rs.getString("doiTuong")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<KhachHang> timKiem(String cccd, String hoTen, String email, String sdt, String doiTuong) {
        List<KhachHang> ds = new ArrayList<>();

        StringBuilder sql = new StringBuilder("SELECT * FROM KhachHang WHERE 1=1");

        // Tạo câu lệnh SQL động
        if (cccd != null && !cccd.trim().isEmpty()) sql.append(" AND CCCD LIKE ?");
        if (hoTen != null && !hoTen.trim().isEmpty()) sql.append(" AND hoTen LIKE ?");
        if (email != null && !email.trim().isEmpty()) sql.append(" AND email LIKE ?");
        if (sdt != null && !sdt.trim().isEmpty()) sql.append(" AND SDT LIKE ?");
        if (doiTuong != null && !doiTuong.trim().isEmpty()) sql.append(" AND doiTuong LIKE ?");

        Connection con = connectDB.getConnection();
        if (con == null) {
            System.err.println("Không thể lấy connection trong timKiem()");
            return ds;
        }

        try (PreparedStatement ps = con.prepareStatement(sql.toString())) {

            int index = 1;

            // Gán tham số lần lượt
            if (cccd != null && !cccd.trim().isEmpty()) ps.setString(index++, "%" + cccd + "%");
            if (hoTen != null && !hoTen.trim().isEmpty()) ps.setString(index++, "%" + hoTen + "%");
            if (email != null && !email.trim().isEmpty()) ps.setString(index++, "%" + email + "%");
            if (sdt != null && !sdt.trim().isEmpty()) ps.setString(index++, "%" + sdt + "%");
            if (doiTuong != null && !doiTuong.trim().isEmpty()) ps.setString(index++, "%" + doiTuong + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    KhachHang kh = new KhachHang(
                            rs.getString("maKH"),
                            rs.getString("CCCD"),
                            rs.getString("hoTen"),
                            rs.getString("email"),
                            rs.getString("SDT"),
                            rs.getString("doiTuong")
                    );
                    ds.add(kh);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ds;
    }

}