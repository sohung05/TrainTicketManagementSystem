import connectDB.connectDB;
import demo.LoginFrame;
import javax.swing.*;

/**
 * Entry point của ứng dụng
 */
public class App {
    public static void main(String[] args) {
        // Kết nối database
        try {
            if (connectDB.getConnection() != null) {
                System.out.println("✅ Kết nối SQL Server thành công!");
                System.out.println("✅ Kết nối database thành công!");
            } else {
                throw new Exception("Không thể kết nối đến database!");
            }
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
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}

