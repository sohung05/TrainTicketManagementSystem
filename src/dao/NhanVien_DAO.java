package dao;

import connectDB.connectDB;
import entity.NhanVien;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class NhanVien_DAO {

    public List<NhanVien> getAll() {
        List<NhanVien> ds = new ArrayList<>();
        String sql = "SELECT * FROM NhanVien";

        Connection con = connectDB.getConnection();
        if (con == null) {
            System.err.println("Không thể lấy connection trong getAll()");
            return ds;
        }

        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Date dNgaySinh = rs.getDate("ngaySinh");
                Date dNgayVaoLam = rs.getDate("ngayVaoLam");

                NhanVien nv = new NhanVien(
                        rs.getString("maNhanVien"),
                        rs.getString("CCCD"),
                        rs.getString("hoTen"),
                        rs.getString("SDT"),
                        rs.getString("email"),
                        rs.getString("diaChi"),
                        rs.getInt("chucVu"),  // ✅ Đổi từ loaiNV String → chucVu int
                        rs.getBoolean("trangThai"),
                        (dNgaySinh != null) ? dNgaySinh.toLocalDate() : null,
                        (dNgayVaoLam != null) ? dNgayVaoLam.toLocalDate() : null,
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
        String sql = "INSERT INTO NhanVien (maNhanVien, CCCD, hoTen, SDT, email, diaChi, chucVu, trangThai, ngaySinh, ngayVaoLam, gioiTinh) "  // ✅ Đổi loaiNV → chucVu
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection con = connectDB.getConnection();
        if (con == null) {
            System.err.println("Không thể lấy connection trong them()");
            return false;
        }

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nv.getMaNhanVien());
            ps.setString(2, nv.getCCCD());
            ps.setString(3, nv.getHoTen());
            ps.setString(4, nv.getSDT());
            ps.setString(5, nv.getEmail());
            ps.setString(6, nv.getDiaChi());
            ps.setInt(7, nv.getChucVu());
            ps.setBoolean(8, nv.isTrangThai());
            ps.setDate(9, (nv.getNgaySinh() != null) ? Date.valueOf(nv.getNgaySinh()) : null);

            java.time.LocalDate ngayVaoLam = nv.getNgayVaoLam();
            if (ngayVaoLam == null) {
                ngayVaoLam = java.time.LocalDate.now();
            }
            ps.setDate(10, Date.valueOf(ngayVaoLam));

            ps.setString(11, nv.getGioiTinh());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean sua(NhanVien nv) {
        String sql = "UPDATE NhanVien SET CCCD=?, hoTen=?, SDT=?, email=?, diaChi=?, chucVu=?, trangThai=?, ngaySinh=?, ngayVaoLam=?, gioiTinh=? "  // ✅ Đổi loaiNV → chucVu
                + "WHERE maNhanVien=?";

        Connection con = connectDB.getConnection();
        if (con == null) {
            System.err.println("Không thể lấy connection trong sua()");
            return false;
        }

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nv.getCCCD());
            ps.setString(2, nv.getHoTen());
            ps.setString(3, nv.getSDT());
            ps.setString(4, nv.getEmail());
            ps.setString(5, nv.getDiaChi());
            ps.setInt(6, nv.getChucVu());  // ✅ Đổi từ setString(getLoaiNV) → setInt(getChucVu)
            ps.setBoolean(7, nv.isTrangThai());
            ps.setDate(8, (nv.getNgaySinh() != null) ? Date.valueOf(nv.getNgaySinh()) : null);
            ps.setDate(9, (nv.getNgayVaoLam() != null) ? Date.valueOf(nv.getNgayVaoLam()) : null);
            ps.setString(10, nv.getGioiTinh());
            ps.setString(11, nv.getMaNhanVien());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<NhanVien> timKiem(String maNV, String cccd, String hoTen, String email, String sdt,
                                  String trangThai, String gioiTinh, LocalDate ngaySinh) {
        List<NhanVien> ds = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM NhanVien WHERE 1=1");

        if (maNV != null && !maNV.trim().isEmpty()) sql.append(" AND maNhanVien LIKE ?");
        if (cccd != null && !cccd.trim().isEmpty()) sql.append(" AND CCCD LIKE ?");
        if (hoTen != null && !hoTen.trim().isEmpty()) sql.append(" AND hoTen LIKE ?");
        if (email != null && !email.trim().isEmpty()) sql.append(" AND email LIKE ?");
        if (sdt != null && !sdt.trim().isEmpty()) sql.append(" AND SDT LIKE ?");
        if (trangThai != null && !trangThai.trim().isEmpty()) sql.append(" AND trangThai = ?");
        if (gioiTinh != null && !gioiTinh.trim().isEmpty()) sql.append(" AND gioiTinh = ?");
        if (ngaySinh != null) sql.append(" AND ngaySinh = ?");

        Connection con = connectDB.getConnection();
        if (con == null) {
            System.err.println("Không thể lấy connection trong timKiem()");
            return ds;
        }

        try (PreparedStatement ps = con.prepareStatement(sql.toString())) {
            int idx = 1;
            if (maNV != null && !maNV.trim().isEmpty()) ps.setString(idx++, "%" + maNV + "%");
            if (cccd != null && !cccd.trim().isEmpty()) ps.setString(idx++, "%" + cccd + "%");
            if (hoTen != null && !hoTen.trim().isEmpty()) ps.setString(idx++, "%" + hoTen + "%");
            if (email != null && !email.trim().isEmpty()) ps.setString(idx++, "%" + email + "%");
            if (sdt != null && !sdt.trim().isEmpty()) ps.setString(idx++, "%" + sdt + "%");

            // **XỬ LÝ trangThai**: DB là BIT -> setBoolean; GUI gửi "Đang làm"/"Nghỉ làm"
            if (trangThai != null && !trangThai.trim().isEmpty()) {
                // map các label GUI thành boolean
                boolean tt;
                if (trangThai.equalsIgnoreCase("Đang làm") || trangThai.equalsIgnoreCase("Dang lam") || trangThai.equalsIgnoreCase("1")) {
                    tt = true;
                } else if (trangThai.equalsIgnoreCase("Nghỉ làm") || trangThai.equalsIgnoreCase("Nghi lam") || trangThai.equalsIgnoreCase("0")) {
                    tt = false;
                } else {
                    // nếu GUI dùng các giá trị khác, bạn có thể tùy chỉnh ở đây; mặc định null -> skip
                    tt = "Đang làm".equalsIgnoreCase(trangThai);
                }
                ps.setBoolean(idx++, tt);
            }

            if (gioiTinh != null && !gioiTinh.trim().isEmpty()) ps.setString(idx++, gioiTinh);
            if (ngaySinh != null) ps.setDate(idx++, java.sql.Date.valueOf(ngaySinh));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    NhanVien nv = new NhanVien();
                    nv.setMaNhanVien(rs.getString("maNhanVien"));
                    nv.setCCCD(rs.getString("CCCD"));
                    nv.setHoTen(rs.getString("hoTen"));
                    nv.setEmail(rs.getString("email"));
                    nv.setSDT(rs.getString("SDT"));
                    nv.setDiaChi(rs.getString("diaChi"));
                    nv.setChucVu(rs.getInt("chucVu"));  // ✅ Đổi từ setLoaiNV(getString) → setChucVu(getInt)
                    nv.setTrangThai(rs.getBoolean("trangThai"));
                    Date dNgaySinh = rs.getDate("ngaySinh");
                    Date dNgayVaoLam = rs.getDate("ngayVaoLam");
                    if (dNgaySinh != null) nv.setNgaySinh(dNgaySinh.toLocalDate());
                    if (dNgayVaoLam != null) nv.setNgayVaoLam(dNgayVaoLam.toLocalDate());
                    nv.setGioiTinh(rs.getString("gioiTinh"));
                    ds.add(nv);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }
    public NhanVien getById(String maNhanVien) {
        if (maNhanVien == null || maNhanVien.trim().isEmpty()) return null;
        String sql = "SELECT * FROM NhanVien WHERE maNhanVien = ?";
        Connection con = connectDB.getConnection();
        if (con == null) {
            System.err.println("Không thể lấy connection trong getById()");
            return null;
        }

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maNhanVien.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    NhanVien nv = new NhanVien();
                    nv.setMaNhanVien(rs.getString("maNhanVien"));
                    nv.setCCCD(rs.getString("CCCD"));
                    nv.setHoTen(rs.getString("hoTen"));
                    nv.setSDT(rs.getString("SDT"));
                    nv.setEmail(rs.getString("email"));
                    nv.setDiaChi(rs.getString("diaChi"));
                    nv.setChucVu(rs.getInt("chucVu"));  // ✅ Đổi từ setLoaiNV(getString) → setChucVu(getInt)
                    // trangThai là bit -> getBoolean
                    try { nv.setTrangThai(rs.getBoolean("trangThai")); } catch (SQLException ignored) {}
                    Date dSinh = rs.getDate("ngaySinh");
                    Date dVaoLam = rs.getDate("ngayVaoLam");
                    if (dSinh != null) nv.setNgaySinh(dSinh.toLocalDate());
                    if (dVaoLam != null) nv.setNgayVaoLam(dVaoLam.toLocalDate());
                    nv.setGioiTinh(rs.getString("gioiTinh"));
                    return nv;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String generateMaNhanVien(LocalDate ngayVaoLam, LocalDate ngaySinh) {
        if (ngayVaoLam == null || ngaySinh == null) {
            throw new IllegalArgumentException("Ngày vào làm và ngày sinh không được null");
        }

        String prefix = "NV";
        String namVaoLam = String.valueOf(ngayVaoLam.getYear()).substring(2); // aa
        String namSinh = String.valueOf(ngaySinh.getYear()).substring(2);     // bb
        String base = prefix + namVaoLam + namSinh; // VD: NV2401

        String sql = "SELECT MAX(maNhanVien) AS maxMa FROM NhanVien WHERE maNhanVien LIKE ?";
        Connection con = connectDB.getConnection();
        if (con == null) {
            System.err.println("Không thể lấy connection trong generateMaNhanVien()");
            return null;
        }

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, base + "%");
            try (ResultSet rs = ps.executeQuery()) {
                int nextNumber = 1; // mặc định nếu chưa có mã nào
                if (rs.next()) {
                    String maxMa = rs.getString("maxMa");
                    if (maxMa != null && maxMa.length() >= 10) {
                        // Lấy 4 ký tự cuối (xxxx)
                        String soThuTuStr = maxMa.substring(maxMa.length() - 4);
                        try {
                            int soThuTu = Integer.parseInt(soThuTuStr);
                            nextNumber = soThuTu + 1;
                        } catch (NumberFormatException e) {
                            System.err.println("Lỗi parse số thứ tự từ mã: " + maxMa);
                        }
                    }
                }
                return base + String.format("%04d", nextNumber); // NVaabbxxxx
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

}