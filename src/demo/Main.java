package demo;

import entity.NhanVien;
import gui.*;
import gui.menu.component.Header;
import gui.menu.component.Menu;
import gui.menu.event.EventMenuSelected;
import gui.menu.event.EventShowPopupMenu;
import gui.menu.form.MainForm;
import gui.menu.swing.MenuItem;
import gui.menu.swing.PopupMenu;
import gui.menu.swing.icon.GoogleMaterialDesignIcons;
import gui.menu.swing.icon.IconFontSwing;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.TimingTargetAdapter;
import utils.SessionManager;


public class Main extends javax.swing.JFrame {

    private MigLayout layout;
    private Menu menu;
    private Header header;
    private MainForm main;
    private Animator animator;

    public Main() {
        initComponents();
        init();
        setExtendedState(getExtendedState() | javax.swing.JFrame.MAXIMIZED_BOTH);
        NhanVien nv = SessionManager.getInstance().getNhanVienDangNhap();

        // Truyền vào header
        if (header != null && nv != null) {
            header.setNhanVien(nv);
        }
    }

    private void init() {
        layout = new MigLayout("fill", "0[]0[100%, fill]0", "0[fill, top]0");
        bg.setLayout(layout);
        menu = new Menu();
        header = new Header();
        main = new MainForm();
        
        // Lấy chức vụ từ session
        NhanVien nv = SessionManager.getInstance().getNhanVienDangNhap();
        int chucVu = (nv != null) ? nv.getChucVu() : 0; // Mặc định 0 = Quản lý
        
        // DEBUG: Hiển thị thông tin phân quyền
        if (nv != null) {
            System.out.println("👤 Đăng nhập: " + nv.getHoTen() + " | Chức vụ: " + chucVu + " (" + (chucVu == 0 ? "Quản lý" : "Nhân viên") + ")");
        } else {
            System.out.println("⚠️ Chưa login, dùng quyền mặc định: Quản lý");
        }
        
        // Khởi tạo menu dựa trên chức vụ
        menu.initMenuItemByRole(chucVu);
        
        menu.addEvent(new EventMenuSelected() {
            @Override
            public void menuSelected(int menuIndex, int subMenuIndex) {
                System.out.println("Menu Index : " + menuIndex + " SubMenu Index " + subMenuIndex);
                
                // Nếu là Nhân viên (chucVu = 1), điều chỉnh menu index
                int adjustedMenuIndex = menuIndex;
                if (chucVu == 1 && menuIndex >= 3) {
                    // Nhân viên không thấy menu 3 (Nhân Viên) và 4 (Khuyến Mãi)
                    // Menu 3 (Thống Kê cho nhân viên) → thực tế là menu 5
                    adjustedMenuIndex = menuIndex + 2;
                }
                
                switch (adjustedMenuIndex) {
                    case 0: // Dashboard
                        if (subMenuIndex == 0 || subMenuIndex == -1) {
                            main.showForm(new Gui_Dashboard());
                        }
                        break;
                    case 1: // Vé
                        switch (subMenuIndex) {
                            case 0: // Bán Vé
                                // Tạo màn hình nhập thông tin hành trình
                                Gui_NhapThongTinHanhTrinh guiNhapThongTin = new Gui_NhapThongTinHanhTrinh();
                                
                                // Set callback để chuyển sang màn hình bán vé khi tìm kiếm
                                guiNhapThongTin.setCallback(info -> {
                                    System.out.println("✅ Tìm kiếm: " + info.getGaDi() + " → " + info.getGaDen() 
                                                      + " | Ngày: " + info.getNgayDi());
                                    
                                    // Chuyển sang màn hình bán vé với thông tin đã nhập
                                    Gui_BanVe guiBanVe = new Gui_BanVe(info);
                                    main.showForm(guiBanVe);
                                });
                                
                                main.showForm(guiNhapThongTin);
                                break;
                            case 1: // Trả Vé
                                main.showForm(new Gui_TraVe());
                                break;
                            case 2: // Đổi Vé
                                main.showForm(new Gui_DoiVe());
                                break;
                        }
                        break;
                    case 2: // Khách Hàng
                        if (subMenuIndex == 0 || subMenuIndex == -1) {
                            main.showForm(new Gui_KhachHang());
                        }
                        break;
                    case 3: // Nhân Viên
                        if (subMenuIndex == 0 || subMenuIndex == -1) {
                            main.showForm(new Gui_NhanVien());
                        }
                        break;
                    case 4: // Khuyễn Mãi
                        switch (subMenuIndex) {
                            case 0: // Khuyến mãi theo hóa đơn
                                main.showForm(new Gui_KhuyenMaiHoaDon());
                                break;
                            case 1: // Khuyến mãi theo đối tượng
                                main.showForm(new Gui_KhuyenMaiDoiTuong());
                                break;
                        }
                        break;
                    case 5: // Thống Kê
                        switch (subMenuIndex) {
                            case 0: // Doanh Thu
                                main.showForm(new Gui_ThongKeDoanhThu());
                                break;
                            case 1: // Lượt Vé
                                main.showForm(new Gui_ThongKeLuotVe());
                                break;
                        }
                        break;
                    case 6: // Trợ Giúp
                        if (subMenuIndex == 0) {
                            try {
                                java.awt.Desktop.getDesktop().browse(new java.net.URI("https://example.com/help"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    case 7: // Đăng Xuất
                        if (subMenuIndex == 0) {
                            System.exit(0); // Thoát ứng dụng
                        }
                        break;

                }
            }
        });
        menu.addEventShowPopup(new EventShowPopupMenu() {
            @Override
            public void showPopup(Component com) {
                MenuItem item = (MenuItem) com;
                PopupMenu popup = new PopupMenu(Main.this, item.getIndex(), item.getEventSelected(), item.getMenu().getSubMenu());
                int x = Main.this.getX() + 52;
                int y = Main.this.getY() + com.getY() + 86;
                popup.setLocation(x, y);
                popup.setVisible(true);
            }
        });
        // menu.initMenuItem(); // ❌ BỎ: Đã init menu theo role ở trên rồi, không cần init lại
        bg.add(menu, "w 230!, spany 2");    // Span Y 2cell
        bg.add(header, "h 50!, wrap");
        bg.add(main, "w 100%, h 100%");
        TimingTarget target = new TimingTargetAdapter() {
            @Override
            public void timingEvent(float fraction) {
                double width;
                if (menu.isShowMenu()) {
                    width = 60 + (170 * (1f - fraction));
                } else {
                    width = 60 + (170 * fraction);
                }
                layout.setComponentConstraints(menu, "w " + width + "!, spany2");
                menu.revalidate();
            }

            @Override
            public void end() {
                menu.setShowMenu(!menu.isShowMenu());
                menu.setEnableMenu(true);
            }

        };
        animator = new Animator(500, target);
        animator.setResolution(0);
        animator.setDeceleration(0.5f);
        animator.setAcceleration(0.5f);
        header.addMenuEvent(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (!animator.isRunning()) {
                    animator.start();
                }
                menu.setEnableMenu(false);
                if (menu.isShowMenu()) {
                    menu.hideallMenu();
                }
            }
        });
        //  Init google icon font
        IconFontSwing.register(GoogleMaterialDesignIcons.getIconFont());
        //  Start with this form
        main.showForm(new Gui_Dashboard());
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bg = new javax.swing.JLayeredPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(false);

        bg.setBackground(new java.awt.Color(245, 245, 245));
        bg.setOpaque(true);

        javax.swing.GroupLayout bgLayout = new javax.swing.GroupLayout(bg);
        bg.setLayout(bgLayout);
        bgLayout.setHorizontalGroup(
            bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1366, Short.MAX_VALUE)
        );
        bgLayout.setVerticalGroup(
            bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 783, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(bg)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(bg)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String args[]) {
        // ✅ Kết nối database trước khi chạy
        try {
            if (connectDB.connectDB.getConnection() == null) {
                javax.swing.JOptionPane.showMessageDialog(null, 
                    "Không thể kết nối đến database!\nKiểm tra lại SQL Server và thông tin kết nối.",
                    "Lỗi kết nối Database", 
                    javax.swing.JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
            System.out.println("✅ Kết nối database thành công!");
        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(null, 
                "Lỗi kết nối database: " + e.getMessage(),
                "Lỗi", 
                javax.swing.JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Main().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLayeredPane bg;
    // End of variables declaration//GEN-END:variables
}
