package demo;

import connectDB.connectDB;
import gui.Gui_Login;

import javax.swing.*;
import java.awt.*;

/**
 * JFrame để hiển thị màn hình đăng nhập
 */
public class LoginFrame extends JFrame {
    
    public LoginFrame() {
        initComponents();
    }
    
    private void initComponents() {
        setTitle("Hệ Thống Quản Lý Vé Tàu - Đăng Nhập");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Chỉ đóng frame này, không thoát app
        setUndecorated(true); // Bỏ viền window để đẹp hơn
        
        // Thêm Gui_Login vào frame
        Gui_Login loginPanel = new Gui_Login();
        add(loginPanel);
        
        // Pack để tự động điều chỉnh kích thước theo panel
        pack();
        
        // Căn giữa màn hình
        setLocationRelativeTo(null);
        
        // Set resizable
        setResizable(false);
    }
    
    public static void main(String[] args) {
        // Kết nối database trước
        try {
            if (connectDB.getConnection() != null) {
                System.out.println("✅ Kết nối SQL Server thành công!");
                System.out.println("✅ Kết nối database thành công!");
            } else {
                System.err.println("❌ Lỗi kết nối database!");
                JOptionPane.showMessageDialog(null, 
                    "Không thể kết nối đến cơ sở dữ liệu!\nVui lòng kiểm tra SQL Server.", 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                "Lỗi khi khởi tạo kết nối: " + e.getMessage(), 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        
        // Chạy GUI
        SwingUtilities.invokeLater(() -> {
            try {
                // Set Look and Feel
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}

