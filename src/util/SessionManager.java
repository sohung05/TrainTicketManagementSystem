package util;

import entity.NhanVien;

/**
 * Quản lý thông tin phiên đăng nhập
 */
public class SessionManager {
    private static SessionManager instance;
    private NhanVien nhanVienDangNhap;
    
    private SessionManager() {}
    
    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }
    
    public void setNhanVienDangNhap(NhanVien nv) {
        this.nhanVienDangNhap = nv;
    }
    
    public NhanVien getNhanVienDangNhap() {
        return nhanVienDangNhap;
    }
    
    public String getMaNhanVienDangNhap() {
        // Nếu chưa login, trả về admin mặc định (để test không cần login)
        if (nhanVienDangNhap == null) {
            return "NV24030001"; // Admin mặc định
        }
        return nhanVienDangNhap.getMaNhanVien();
    }
    
    public boolean isLoggedIn() {
        return nhanVienDangNhap != null;
    }
    
    public void logout() {
        nhanVienDangNhap = null;
    }
}

