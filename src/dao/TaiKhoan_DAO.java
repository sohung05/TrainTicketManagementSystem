package dao;

import connectDB.connectDB;
import entity.TaiKhoan;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaiKhoan_DAO {

    // Lấy tất cả tài khoản
    public List<Object[]> getAll() {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT tk.maNhanVien, nv.hoTen, tk.tenTaiKhoan, tk.matKhau " +
                "FROM TaiKhoan tk " +
                "JOIN NhanVien nv ON tk.maNhanVien = nv.maNhanVien";

        Connection con = connectDB.getConnection();
        if (con == null) {
            System.err.println("Không thể kết nối DB trong getAllWithTenNhanVien()");
            return list;
        }

        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                String maNV = rs.getString("maNhanVien");
                String hoTen = rs.getString("hoTen");
                String tenTK = rs.getString("tenTaiKhoan");
                String matKhau = rs.getString("matKhau");

                // Thay vì return entity, ta trả về Object[] để tiện add vào table
                list.add(new Object[]{maNV, hoTen, tenTK, matKhau});
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }


    // Thêm tài khoản mới
    public boolean them(TaiKhoan tk) {
        String sql = "INSERT INTO TaiKhoan (tenTaiKhoan, matKhau, maNhanVien) VALUES (?, ?, ?)";

        Connection con = connectDB.getConnection();
        if (con == null) {
            System.err.println("Không thể lấy connection trong them()");
            return false;
        }

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, tk.getTenTaiKhoan());
            ps.setString(2, tk.getMatKhau());
            ps.setString(3, tk.getMaNhanVien());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean sua(TaiKhoan tk) {
        String sql = "UPDATE TaiKhoan SET tenTaiKhoan = ?, matKhau = ? WHERE maNhanVien = ?";

        Connection con = connectDB.getConnection();
        if (con == null) {
            System.err.println("Không thể lấy connection trong sua()");
            return false;
        }

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, tk.getTenTaiKhoan());
            ps.setString(2, tk.getMatKhau());
            ps.setString(3, tk.getMaNhanVien());

            int rows = ps.executeUpdate();
            System.out.println("Số dòng cập nhật: " + rows);
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Object[]> timKiem(String maNV, String tenTaiKhoan, String tenNhanVien) {
        List<Object[]> list = new ArrayList<>();
        // LEFT JOIN để vẫn lấy được tài khoản ngay cả khi không có bản ghi tương ứng trong NhanVien
        StringBuilder sql = new StringBuilder(
                "SELECT tk.maNhanVien, nv.hoTen, tk.tenTaiKhoan, tk.matKhau "
                        + "FROM TaiKhoan tk "
                        + "LEFT JOIN NhanVien nv ON tk.maNhanVien = nv.maNhanVien "
                        + "WHERE 1=1"
        );

        if (maNV != null && !maNV.trim().isEmpty()) sql.append(" AND tk.maNhanVien LIKE ?");
        if (tenTaiKhoan != null && !tenTaiKhoan.trim().isEmpty()) sql.append(" AND tk.tenTaiKhoan LIKE ?");
        if (tenNhanVien != null && !tenNhanVien.trim().isEmpty()) sql.append(" AND nv.hoTen LIKE ?");

        Connection con = connectDB.getConnection();
        if (con == null) {
            System.err.println("Không thể lấy connection trong timKiem()");
            return list;
        }

        try (PreparedStatement ps = con.prepareStatement(sql.toString())) {
            int idx = 1;
            if (maNV != null && !maNV.trim().isEmpty()) ps.setString(idx++, "%" + maNV + "%");
            if (tenTaiKhoan != null && !tenTaiKhoan.trim().isEmpty()) ps.setString(idx++, "%" + tenTaiKhoan + "%");
            if (tenNhanVien != null && !tenNhanVien.trim().isEmpty()) ps.setString(idx++, "%" + tenNhanVien + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String m = rs.getString("maNhanVien");
                    String hoTen = rs.getString("hoTen");
                    String tk = rs.getString("tenTaiKhoan");
                    String mk = rs.getString("matKhau");
                    // nếu không có cột ngayTao thì rs.getDate sẽ trả SQLException -> bắt/ignore
                    java.time.LocalDate ngayTao = null;
                    try {
                        java.sql.Date d = rs.getDate("ngayTao");
                        if (d != null) ngayTao = d.toLocalDate();
                    } catch (SQLException ignored) { /* cột ngayTao không tồn tại */ }

                    // Trả về mảng Object; GUI sẽ biết cách hiển thị các cột tương ứng
                    list.add(new Object[]{ m, hoTen, tk, mk, ngayTao });
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public boolean kiemTraTonTaiTheoMaNV(String maNV) {
        String sql = "SELECT COUNT(*) FROM TaiKhoan WHERE maNhanVien = ?";
        try (Connection con = connectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maNV);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // nếu count > 0 nghĩa là nhân viên đã có tài khoản
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public TaiKhoan dangNhap(String tenTaiKhoan, String matKhau) {
        String sql = "SELECT maNhanVien, tenTaiKhoan, matKhau FROM TaiKhoan WHERE tenTaiKhoan = ? AND matKhau = ?";
        Connection con = connectDB.getConnection();
        if (con == null) {
            System.err.println("Không thể lấy connection trong dangNhap()");
            return null;
        }

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, tenTaiKhoan);
            ps.setString(2, matKhau); // nếu dùng hashed thì truyền hashed ở đây
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    TaiKhoan tk = new TaiKhoan();
                    tk.setMaNhanVien(rs.getString("maNhanVien"));
                    tk.setTenTaiKhoan(rs.getString("tenTaiKhoan"));
                    tk.setMatKhau(rs.getString("matKhau"));
                    return tk;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean xacThuc(String tenTaiKhoan, String matKhau) {
        return dangNhap(tenTaiKhoan, matKhau) != null;
    }
}