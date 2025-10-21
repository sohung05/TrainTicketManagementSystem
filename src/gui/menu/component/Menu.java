package gui.menu.component;

import gui.menu.event.EventMenu;
import gui.menu.event.EventMenuSelected;
import gui.menu.event.EventShowPopupMenu;
import gui.menu.model.ModelMenu;
import gui.menu.swing.MenuAnimation;
import gui.menu.swing.MenuItem;
import gui.menu.swing.scrollbar.ScrollBarCustom;

import java.awt.Component;
import javax.swing.*;

import net.miginfocom.swing.MigLayout;

public class Menu extends javax.swing.JPanel {

    public boolean isShowMenu() {
        return showMenu;
    }

    public void addEvent(EventMenuSelected event) {
        this.event = event;
    }

    public void setEnableMenu(boolean enableMenu) {
        this.enableMenu = enableMenu;
    }

    public void setShowMenu(boolean showMenu) {
        this.showMenu = showMenu;
    }

    public void addEventShowPopup(EventShowPopupMenu eventShowPopup) {
        this.eventShowPopup = eventShowPopup;
    }

    private final MigLayout layout;
    private EventMenuSelected event;
    private EventShowPopupMenu eventShowPopup;
    private boolean enableMenu = true;
    private boolean showMenu = true;

    public Menu() {
        initComponents();
        setOpaque(false);
        sp.getViewport().setOpaque(false);
        sp.setVerticalScrollBar(new ScrollBarCustom());
        layout = new MigLayout("wrap, fillx, insets 0", "[fill]", "[]50[]");
        panel.setLayout(layout);
    }
    private ImageIcon resizeIcon(String path, int width, int height) {
        ImageIcon icon = new ImageIcon(getClass().getResource(path));
        java.awt.Image img = icon.getImage();
        java.awt.Image scaled = img.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    public void initMenuItem() {
        addMenu(new ModelMenu(new ImageIcon(getClass().getResource("/gui/menu/icon/dashboard.png")), "Dashboard"));
        addMenu(new ModelMenu(new ImageIcon(getClass().getResource("/gui/menu/icon/Ve.png")), "Vé", "Bán Vé", "Trả Vé", "Đổi Vé"));
        addMenu(new ModelMenu(new ImageIcon(getClass().getResource("/gui/menu/icon/KhachHang.png")), "Khách Hàng"));
        addMenu(new ModelMenu(new ImageIcon(getClass().getResource("/gui/menu/icon/NhanVien.png")), "Nhân viên"));
        addMenu(new ModelMenu(new ImageIcon(getClass().getResource("/gui/menu/icon/KhuyenMai.png")), "Khuyến Mãi", "Khuyễn mãi theo hóa đơn", "Khuyến mãi theo đối tượng"));
        addMenu(new ModelMenu(new ImageIcon(getClass().getResource("/gui/menu/icon/ThongKe.png")), "Thống Kê", "Doanh Thu", "Lượt Vé"));
        addMenu(new ModelMenu(new ImageIcon(getClass().getResource("/gui/menu/icon/TroGiup.png")), "Trợ Giúp"));
        addMenu(new ModelMenu(new ImageIcon(getClass().getResource("/gui/menu/icon/DangXuat.png")), "Đăng Xuất"));



//        addMenu(new ModelMenu(resizeIcon("/gui/menu/icon/1.png", 32, 32), "Dashboard"));
//        addMenu(new ModelMenu(resizeIcon("/gui/menu/icon/2.png", 32, 32), "Vé", "Bán Vé", "Trả Vé", "Đổi Vé"));
//        addMenu(new ModelMenu(resizeIcon("/gui/menu/icon/3.png", 32, 32), "Khách Hàng"));
//        addMenu(new ModelMenu(resizeIcon("/gui/menu/icon/NhanVien.png", 32, 32), "Nhân Viên"));
//        addMenu(new ModelMenu(resizeIcon("/gui/menu/icon/KhachHang.png", 32, 32), "Khuyễn Mãi", "Khuyễn mãi theo hóa đơn", "Khuyến mãi theo đối tượng"));
//        addMenu(new ModelMenu(resizeIcon("/gui/menu/icon/6.png", 32, 32), "Thống Kê", "Doanh Thu", "Lượt Vé"));
//        addMenu(new ModelMenu(resizeIcon("/gui/menu/icon/7.png", 32, 32), "Trợ Giúp"));
//        addMenu(new ModelMenu(resizeIcon("/gui/menu/icon/8.png", 32, 32), "Đăng Xuất"));
    }



    private void addMenu(ModelMenu menu) {
        panel.add(new MenuItem(menu, getEventMenu(), event, panel.getComponentCount()), "h 40!");
    }

    private EventMenu getEventMenu() {
        return new EventMenu() {
            @Override
            public boolean menuPressed(Component com, boolean open) {
                if (enableMenu) {
                    if (isShowMenu()) {
                        if (open) {
                            new MenuAnimation(layout, com).openMenu();
                        } else {
                            new MenuAnimation(layout, com).closeMenu();
                        }
                        return true;
                    } else {
                        eventShowPopup.showPopup(com);
                    }
                }
                return false;
            }
        };
    }

    public void hideallMenu() {
        for (Component com : panel.getComponents()) {
            MenuItem item = (MenuItem) com;
            if (item.isOpen()) {
                new MenuAnimation(layout, com, 500).closeMenu();
                item.setOpen(false);
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        sp = new javax.swing.JScrollPane();
        panel = new javax.swing.JPanel();
        profile1 = new gui.menu.component.Profile();

        sp.setBorder(null);
        sp.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        sp.setViewportBorder(null);

        panel.setOpaque(false);

        javax.swing.GroupLayout panelLayout = new javax.swing.GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 312, Short.MAX_VALUE)
        );
        panelLayout.setVerticalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 523, Short.MAX_VALUE)
        );

        sp.setViewportView(panel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(sp, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
            .addComponent(profile1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(profile1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sp, javax.swing.GroupLayout.DEFAULT_SIZE, 523, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    @Override
    protected void paintComponent(java.awt.Graphics g) {
        java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
        g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,
                java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

        // Gradient xanh pastel nhẹ
        java.awt.GradientPaint gp = new java.awt.GradientPaint(
                0, 0, new java.awt.Color(200, 230, 255),   // xanh trắng nhạt ở trên
                0, getHeight(), new java.awt.Color(140, 190, 255) // xanh sáng ở dưới
        );

        g2.setPaint(gp);
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.dispose();
    }



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel panel;
    private gui.menu.component.Profile profile1;
    private javax.swing.JScrollPane sp;
    // End of variables declaration//GEN-END:variables
}

