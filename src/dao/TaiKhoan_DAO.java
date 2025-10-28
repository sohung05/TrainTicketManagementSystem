package dao;

import connectDB.connectDB;
import entity.TaiKhoan;
import entity.NhanVien;
import java.sql.*;

/**
 * DAO for TaiKhoan entity
 */
public class TaiKhoan_DAO {
    
    /**
     * Xác thực đăng nhập
     * @param userName Tên đăng nhập
     * @param passWord Mật khẩu
     * @return TaiKhoan object nếu thành công, null nếu thất bại
     */
    public TaiKhoan login(String userName, String passWord) {
        TaiKhoan tk = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            con = connectDB.getCon();
            String sql = "SELECT tk.userName, tk.passWord, " +
                        "nv.maNhanVien, nv.hoTen, nv.chucVu, nv.sdt, nv.email, nv.cccd, nv.ngaySinh " +
                        "FROM TaiKhoan tk " +
                        "JOIN NhanVien nv ON tk.maNhanVien = nv.maNhanVien " +
                        "WHERE tk.userName = ? AND tk.passWord = ?";
            
            ps = con.prepareStatement(sql);
            ps.setString(1, userName);
            ps.setString(2, passWord);
            
            rs = ps.executeQuery();
            
            if (rs.next()) {
                // Tạo NhanVien object
                NhanVien nv = new NhanVien();
                nv.setMaNhanVien(rs.getString("maNhanVien"));
                nv.setHoTen(rs.getString("hoTen"));
                nv.setChucVu(rs.getBoolean("chucVu"));
                nv.setSdt(rs.getString("sdt"));
                nv.setEmail(rs.getString("email"));
                nv.setCccd(rs.getString("cccd"));
                
                // Xử lý ngày sinh (có thể null)
                Date ngaySinh = rs.getDate("ngaySinh");
                if (ngaySinh != null) {
                    nv.setNgaySinh(ngaySinh.toLocalDate());
                }
                
                // Tạo TaiKhoan object
                tk = new TaiKhoan();
                tk.setUserName(rs.getString("userName"));
                tk.setPassWord(rs.getString("passWord"));
                tk.setNhanVien(nv);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return tk;
    }
    
    /**
     * Đổi mật khẩu
     * @param userName Tên đăng nhập
     * @param oldPassword Mật khẩu cũ
     * @param newPassword Mật khẩu mới
     * @return true nếu thành công, false nếu thất bại
     */
    public boolean changePassword(String userName, String oldPassword, String newPassword) {
        Connection con = null;
        PreparedStatement ps = null;
        
        try {
            con = connectDB.getCon();
            
            // Kiểm tra mật khẩu cũ
            String checkSql = "SELECT COUNT(*) FROM TaiKhoan WHERE userName = ? AND passWord = ?";
            ps = con.prepareStatement(checkSql);
            ps.setString(1, userName);
            ps.setString(2, oldPassword);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next() && rs.getInt(1) == 0) {
                return false; // Mật khẩu cũ không đúng
            }
            rs.close();
            ps.close();
            
            // Cập nhật mật khẩu mới
            String updateSql = "UPDATE TaiKhoan SET passWord = ? WHERE userName = ?";
            ps = con.prepareStatement(updateSql);
            ps.setString(1, newPassword);
            ps.setString(2, userName);
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (ps != null) ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

