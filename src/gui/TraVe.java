package gui;

import java.awt.*;
import java.io.File;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class TraVe extends JPanel {

    private ArrayList<JLabel> menuLabels = new ArrayList<>();
    private JLabel activeLabel = null;

    public TraVe() {
        setLayout(new BorderLayout(10, 0));

        // =================== BẢNG TRUNG TÂM ===================
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        centerPanel.setPreferredSize(new Dimension(850, 700));

        JLabel titleTop = new JLabel("DANH SÁCH HÓA ĐƠN", SwingConstants.CENTER);
        titleTop.setFont(new Font("Times New Roman", Font.BOLD, 20));
        titleTop.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(titleTop);

        String[] columnsTop = {"Mã hóa đơn","Tên nhân viên", "Mã khách hàng", "Giờ tạo", "Ngày tạo", "Trạng thái"};
        JTable tableTop = new JTable(new DefaultTableModel(null, columnsTop));
        tableTop.setRowHeight(25);
        JScrollPane scrollTop = new JScrollPane(tableTop);
        scrollTop.setPreferredSize(new Dimension(800, 250));
        centerPanel.add(scrollTop);

        centerPanel.add(Box.createVerticalStrut(20));

        JLabel titleBottom = new JLabel("DANH SÁCH VÉ", SwingConstants.CENTER);
        titleBottom.setFont(new Font("Times New Roman", Font.BOLD, 20));
        titleBottom.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(titleBottom);

        String[] columnsBottom = {"Mã vé", "Loại vé", "Mã vạch", "Thời gian đi", "Giá vé",
                "Mã KH", "Toa", "Trạng thái", "Tên KH", "CCCD", "Chỗ ngồi"};
        JTable tableBottom = new JTable(new DefaultTableModel(null, columnsBottom));
        tableBottom.setRowHeight(25);
        JScrollPane scrollBottom = new JScrollPane(tableBottom);
        scrollBottom.setPreferredSize(new Dimension(800, 250));
        centerPanel.add(scrollBottom);

        add(centerPanel, BorderLayout.CENTER);

        // =================== FORM BÊN PHẢI ===================

        // -------- Form Hóa đơn --------
        JPanel formHoaDon = new JPanel();
        formHoaDon.setBackground(new Color(180, 210, 250));
        formHoaDon.setLayout(new BoxLayout(formHoaDon, BoxLayout.Y_AXIS));
        formHoaDon.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createEmptyBorder(10, 10, 10, 10),
                        BorderFactory.createLineBorder(Color.GRAY, 1)
                ),
                "THÔNG TIN HÓA ĐƠN",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("Times New Roman", Font.BOLD, 18),
                new Color(20, 20, 20)
        ));
        formHoaDon.setBorder(BorderFactory.createCompoundBorder(
                formHoaDon.getBorder(),
                new EmptyBorder(10, 0, 0, 0)
        ));

        String[] personalLabels = {"Mã hóa đơn","Tên nhân viên", "Mã khách hàng", "Giờ tạo", "Ngày tạo", "Trạng thái"};
        for (String lbl : personalLabels) formHoaDon.add(createFormRow(lbl));

        JPanel btnPanelHD = new JPanel(new GridLayout(2, 2, 10, 10));
        btnPanelHD.setOpaque(false);
        btnPanelHD.add(new JButton("🔍 Tìm HĐ"));
        btnPanelHD.add(new JButton("Xóa trắng"));
        btnPanelHD.add(new JButton("In hóa đơn"));
        btnPanelHD.add(new JButton("Trả tập vé"));
        formHoaDon.add(Box.createVerticalStrut(10));
        formHoaDon.add(btnPanelHD);

        // -------- Form Vé --------
        JPanel formVe = new JPanel();
        formVe.setBackground(new Color(180, 210, 250));
        formVe.setLayout(new BoxLayout(formVe, BoxLayout.Y_AXIS));
        formVe.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createEmptyBorder(10, 10, 10, 10),
                        BorderFactory.createLineBorder(Color.GRAY, 1)
                ),
                "THÔNG TIN VÉ",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("Times New Roman", Font.BOLD, 18),
                new Color(20, 20, 20)
        ));
        formVe.setBorder(BorderFactory.createCompoundBorder(
                formVe.getBorder(),
                new EmptyBorder(10, 0, 0, 0)
        ));

        String[] workLabels = {"Mã vé", "Loại vé", "Mã vạch", "Thời gian đi", "Giá vé",
                "Toa", "Tên KH","Chỗ ngồi"};
        for (String lbl : workLabels) formVe.add(createFormRow(lbl));

        // ===== HÀNG 1: Tìm vé + In vé =====
        JPanel btnPanelVe = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        btnPanelVe.setOpaque(false);

        JButton btnTimVe = new JButton("🔍 Tìm vé");
        btnTimVe.setPreferredSize(new Dimension(130, 30));

        JButton btnInVe = new JButton("In vé");
        btnInVe.setPreferredSize(new Dimension(130, 30));

        btnPanelVe.add(btnTimVe);
        btnPanelVe.add(btnInVe);
        formVe.add(Box.createVerticalStrut(10));
        formVe.add(btnPanelVe);

// ===== HÀNG 2: Nhập lý do + TextField + Trả vé =====
        JPanel lyDoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        lyDoPanel.setOpaque(false);

        JLabel lblLyDo = new JLabel("Nhập lý do:");
        lblLyDo.setFont(new Font("Times New Roman", Font.BOLD, 14));

        JTextField txtLyDo = new JTextField();
        txtLyDo.setPreferredSize(new Dimension(120, 28));

        JButton btnTraVe = new JButton("Trả vé");
        btnTraVe.setPreferredSize(new Dimension(80, 30));

        lyDoPanel.add(lblLyDo);
        lyDoPanel.add(txtLyDo);
        lyDoPanel.add(btnTraVe);

        formVe.add(Box.createVerticalStrut(10));
        formVe.add(lyDoPanel);


        // ✅ Chia đều chiều cao 2 form
        JSplitPane splitRight = new JSplitPane(JSplitPane.VERTICAL_SPLIT, formHoaDon, formVe);
        splitRight.setResizeWeight(0.5);
        splitRight.setDividerLocation(300);
        splitRight.setDividerSize(6);
        splitRight.setContinuousLayout(true);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setPreferredSize(new Dimension(350, 600));
        rightPanel.add(splitRight, BorderLayout.CENTER);

        add(rightPanel, BorderLayout.EAST);
    }

    private JPanel createFormRow(String lbl) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        row.setOpaque(false);
        row.setPreferredSize(new Dimension(300, 35));

        JLabel lb = new JLabel(lbl + " :");
        lb.setFont(new Font("Times New Roman", Font.BOLD, 15));
        lb.setPreferredSize(new Dimension(120, 30));
        lb.setVerticalAlignment(SwingConstants.CENTER);

        JTextField tf = new JTextField();
        tf.setPreferredSize(new Dimension(170, 28));

        row.add(lb);
        row.add(tf);
        return row;
    }

    private Icon loadScaledIcon(String path, int width, int height) {
        File f = new File(path);
        if (!f.exists()) return null;
        ImageIcon icon = new ImageIcon(f.getAbsolutePath());
        Image scaled = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }


}
