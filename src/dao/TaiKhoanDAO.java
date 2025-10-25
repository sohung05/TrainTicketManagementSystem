package dao;

import connectDB.ConnectDB;
import entity.TaiKhoan;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaiKhoanDAO {

    // Lấy tất cả tài khoản
    public List<TaiKhoan> getALL() {
        List<TaiKhoan> ds = new ArrayList<>();
        String sql = "SELECT * FROM TaiKhoan";

        try (Connection con = ConnectDB.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                TaiKhoan tk = new TaiKhoan(
                        rs.getString("tenTaiKhoan"),
                        rs.getString("matKhau"),
                        rs.getString("maNhanVien")
                );
                ds.add(tk);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    // Thêm tài khoản mới
    public boolean them(TaiKhoan tk) {
        String sql = "INSERT INTO TaiKhoan (tenTaiKhoan, matKhau, maNhanVien) VALUES (?, ?, ?)";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, tk.getTenTaiKhoan());
            ps.setString(2, tk.getMatKhau());
            ps.setString(3, tk.getMaNhanVien());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Sửa thông tin tài khoản
    public boolean sua(TaiKhoan tk) {
        String sql = "UPDATE TaiKhoan SET matKhau=?, maNhanVien=? WHERE tenTaiKhoan=?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, tk.getMatKhau());
            ps.setString(2, tk.getMaNhanVien());
            ps.setString(3, tk.getTenTaiKhoan());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Xóa tài khoản
    public boolean xoa(String tenTaiKhoan) {
        String sql = "DELETE FROM TaiKhoan WHERE tenTaiKhoan=?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, tenTaiKhoan);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
