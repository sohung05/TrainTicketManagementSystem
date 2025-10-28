import connectDB.connectDB;
import gui.Gui_Login;
import javax.swing.*;

/**
 * Entry point của ứng dụng
 */
public class App {
    public static void main(String[] args) {
        // Kết nối database
        try {
            connectDB.getInstance().connect();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                "Không thể kết nối đến database!\n" + e.getMessage(),
                "Lỗi kết nối", 
                JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        
        // Set Look and Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Hiển thị màn hình Login
        SwingUtilities.invokeLater(() -> {
            JFrame loginFrame = new JFrame("ĐĂNG NHẬP - HỆ THỐNG QUẢN LÝ VÉ TÀU");
            loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            loginFrame.setContentPane(new Gui_Login());
            loginFrame.setSize(800, 500); // Set size cố định thay vì pack()
            loginFrame.setResizable(false); // Không cho resize
            loginFrame.setLocationRelativeTo(null); // Center màn hình
            loginFrame.setVisible(true);
        });
    }
}

