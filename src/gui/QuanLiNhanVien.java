package gui;

import java.awt.*;
import java.io.File;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.text.SimpleDateFormat;
import java.util.*;

public class QuanLiNhanVien extends JPanel {

    private CardLayout cardLayout;
    private JPanel centerContentPanel;

    public QuanLiNhanVien() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // CardLayout chứa 2 trang: Nhân viên & Tài khoản
        cardLayout = new CardLayout();
        centerContentPanel = new JPanel(cardLayout);

        centerContentPanel.add(createNhanVienPanel(), "NhanVien");
        centerContentPanel.add(createTaiKhoanPanel(), "TaiKhoan");

        add(centerContentPanel, BorderLayout.CENTER);
    }

    // Trang quản lý nhân viên
    private JPanel createNhanVienPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(20, 10, 20, 10));

        JLabel title = new JLabel("QUẢN LÍ NHÂN VIÊN", SwingConstants.CENTER);
        title.setFont(new Font("Times New Roman", Font.BOLD, 30));
        title.setBorder(new EmptyBorder(10, 0, 10, 0));

        // Thanh chuyển trang
        JPanel pLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        JButton btnNhanVien = new JButton("Nhân viên");
        JButton btnTaiKhoan = new JButton("Tài khoản");
        styleSwitchButton(btnNhanVien);
        styleSwitchButton(btnTaiKhoan);
        pLeft.add(btnNhanVien);
        pLeft.add(btnTaiKhoan);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.add(pLeft);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        topPanel.add(title);

        // Sự kiện chuyển trang
        btnNhanVien.addActionListener(e -> cardLayout.show(centerContentPanel, "NhanVien"));
        btnTaiKhoan.addActionListener(e -> cardLayout.show(centerContentPanel, "TaiKhoan"));

        panel.add(topPanel, BorderLayout.NORTH);

        // TABLE danh sách nhân viên
        String[] columns = {"Mã nhân viên", "CCCD", "Họ tên", "Giới tính", "SĐT", "Email",
                "Địa chỉ", "Ngày sinh", "Ngày vào làm", "Chức vụ", "Trạng thái"};
        DefaultTableModel model = new DefaultTableModel(null, columns);
        JTable table = new JTable(model);
        table.getTableHeader().setFont(new Font("Times New Roman", Font.BOLD, 14));
        table.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        // PANEL PHẢI
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setPreferredSize(new Dimension(350, 0));
        rightPanel.setBackground(new Color(140, 196, 246));
        rightPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // FORM THÔNG TIN NHÂN VIÊN
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createTitledBorder("Thông tin nhân viên"));
        formPanel.setOpaque(false);

        String[] labels = {"Mã nhân viên", "CCCD", "Họ tên", "Giới tính", "SĐT", "Email",
                "Địa chỉ", "Ngày sinh", "Ngày vào làm", "Chức vụ", "Trạng thái"};

        Map<String, JComponent> fieldMap = new HashMap<>();

        for (String lbl : labels) {
            JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
            row.setOpaque(false);

            JLabel lb = new JLabel(lbl + " :");
            lb.setFont(new Font("Times New Roman", Font.BOLD, 15));
            lb.setPreferredSize(new Dimension(100, 25)); // cột nhãn

            if (lbl.equals("Giới tính")) {
                JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
                genderPanel.setOpaque(false);
                JRadioButton rbNam = new JRadioButton("Nam");
                JRadioButton rbNu = new JRadioButton("Nữ");
                rbNam.setOpaque(false);
                rbNu.setOpaque(false);
                ButtonGroup group = new ButtonGroup();
                group.add(rbNam);
                group.add(rbNu);
                genderPanel.add(rbNam);
                genderPanel.add(rbNu);
                genderPanel.setPreferredSize(new Dimension(160, 25));
                row.add(lb);
                row.add(genderPanel);
            }
            else if (lbl.equals("Ngày vào làm")) {
                JPanel datePanel = new JPanel(new BorderLayout());
                datePanel.setOpaque(false);
                datePanel.setPreferredSize(new Dimension(160, 25));

                JTextField tfNgayVaoLam = new JTextField();
                tfNgayVaoLam.setEditable(false);
                datePanel.add(tfNgayVaoLam, BorderLayout.CENTER);

                JButton btnCalendar = new JButton("📅");
                btnCalendar.setMargin(new Insets(0, 5, 0, 5));
                datePanel.add(btnCalendar, BorderLayout.EAST);

                // Popup chọn ngày
                JPopupMenu popup = new JPopupMenu();
                SpinnerDateModel dateModel = new SpinnerDateModel();
                JSpinner spinner = new JSpinner(dateModel);
                JSpinner.DateEditor editor = new JSpinner.DateEditor(spinner, "dd/MM/yyyy");
                spinner.setEditor(editor);
                popup.add(spinner);

                btnCalendar.addActionListener(e -> popup.show(btnCalendar, 0, btnCalendar.getHeight()));
                spinner.addChangeListener(e -> {
                    Date selectedDate = (Date) spinner.getValue();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    tfNgayVaoLam.setText(sdf.format(selectedDate));
                });

                row.add(lb);
                row.add(datePanel);
                fieldMap.put("Ngày vào làm", tfNgayVaoLam);
            }
            else {
                JTextField tf = new JTextField();
                tf.setPreferredSize(new Dimension(160, 25));
                row.add(lb);
                row.add(tf);
                fieldMap.put(lbl, tf);
            }

            formPanel.add(row);
        }

        rightPanel.add(formPanel, BorderLayout.CENTER);

        // CÁC NÚT CHỨC NĂNG
        JPanel btnPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        btnPanel.setOpaque(false);

        JButton btnTimKiem = new JButton("Tìm Kiếm");
        JButton btncapNhat = new JButton("Cập nhật");
        JButton btnThem = new JButton("Thêm");
        JButton btnLamMoi = new JButton("Làm Mới");
        JButton btnXuat = new JButton("Xuất Excel");

        Dimension btnSize = new Dimension(100, 30);
        for (JButton b : new JButton[]{btnTimKiem, btncapNhat, btnThem, btnLamMoi, btnXuat}) {
            b.setPreferredSize(btnSize);
            btnPanel.add(b);
        }
        btnPanel.add(new JLabel()); // ô trống cuối

        rightPanel.add(btnPanel, BorderLayout.SOUTH);

        panel.add(rightPanel, BorderLayout.EAST);

        return panel;
    }

    // Trang quản lý tài khoản
    private JPanel createTaiKhoanPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(20, 10, 20, 10));

        JLabel title = new JLabel("QUẢN LÍ TÀI KHOẢN", SwingConstants.CENTER);
        title.setFont(new Font("Times New Roman", Font.BOLD, 30));
        title.setBorder(new EmptyBorder(5, 0, 5, 0));

        // Thanh nút chuyển trang
        JPanel pLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        JButton btnNhanVien = new JButton("Nhân viên");
        JButton btnTaiKhoan = new JButton("Tài khoản");
        styleSwitchButton(btnNhanVien);
        styleSwitchButton(btnTaiKhoan);
        pLeft.add(btnNhanVien);
        pLeft.add(btnTaiKhoan);

        // Gom nút và tiêu đề vào 1 panel
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.add(pLeft);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        topPanel.add(title);

        // Sự kiện chuyển giữa 2 trang
        btnNhanVien.addActionListener(e -> cardLayout.show(centerContentPanel, "NhanVien"));
        btnTaiKhoan.addActionListener(e -> cardLayout.show(centerContentPanel, "TaiKhoan"));

        panel.add(topPanel, BorderLayout.NORTH);

        // TABLE
        String[] columns = {"Mã nhân viên", "Tên nhân viên", "Tên tài khoản", "Mật khẩu"};
        DefaultTableModel model = new DefaultTableModel(null, columns);
        JTable table = new JTable(model);
        table.getTableHeader().setFont(new Font("Times New Roman", Font.BOLD, 14));
        table.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        // PANEL PHẢI TỔNG
        JPanel rightPanel = new JPanel();
        rightPanel.setPreferredSize(new Dimension(350, 0)); // <-- Sửa chỗ này
        rightPanel.setBackground(new Color(140, 196, 246));
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // FORM TÀI KHOẢN
        JPanel formTaiKhoan = new JPanel();
        formTaiKhoan.setOpaque(false);
        formTaiKhoan.setBorder(BorderFactory.createTitledBorder("Thông tin tài khoản"));
        formTaiKhoan.setLayout(new BoxLayout(formTaiKhoan, BoxLayout.Y_AXIS));

        String[] labels = {"Mã nhân viên", "Tên nhân viên", "Tên tài khoản", "Mật khẩu"};
        for (String lbl : labels) {
            if (!lbl.equals("Mật khẩu")) {
                JPanel row = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 10));
                row.setOpaque(false);

                JLabel lb = new JLabel(lbl + " :");
                lb.setFont(new Font("Times New Roman", Font.BOLD, 15));
                lb.setPreferredSize(new Dimension(130, 25));

                JTextField tf = new JTextField();
                tf.setPreferredSize(new Dimension(150, 25));

                row.add(lb);
                row.add(tf);
                formTaiKhoan.add(row);
            } else {
                JPanel row = new JPanel();
                row.setLayout(new BoxLayout(row, BoxLayout.Y_AXIS));
                row.setOpaque(false);

                JPanel upperRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 10));
                upperRow.setOpaque(false);

                JLabel lb = new JLabel(lbl + " :");
                lb.setFont(new Font("Times New Roman", Font.BOLD, 15));
                lb.setPreferredSize(new Dimension(130, 25));

                JPasswordField passwordField = new JPasswordField();
                passwordField.setPreferredSize(new Dimension(150, 25));
                char defaultEcho = passwordField.getEchoChar();

                upperRow.add(lb);
                upperRow.add(passwordField);

                JPanel lowerRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
                lowerRow.setOpaque(false);

                JCheckBox showPass = new JCheckBox("Hiện mật khẩu");
                showPass.setFont(new Font("Times New Roman", Font.PLAIN, 13));
                showPass.setOpaque(false);
                showPass.addActionListener(e -> {
                    if (showPass.isSelected()) {
                        passwordField.setEchoChar((char) 0);
                    } else {
                        passwordField.setEchoChar(defaultEcho);
                    }
                });

                lowerRow.add(showPass);
                row.add(upperRow);
                row.add(lowerRow);
                formTaiKhoan.add(row);
            }
        }

        // Nút chức năng
        JPanel btnPanelTaiKhoan = new JPanel(new GridLayout(2, 2, 10, 10));
        btnPanelTaiKhoan.setOpaque(false);

        JButton btnthemNV = new JButton("Thêm nhân viên", new ImageIcon(new ImageIcon("src/images/CapQuyen.png").getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        JButton btnlamMoi = new JButton("Làm mới", new ImageIcon(new ImageIcon("src/images/LamMoi.png").getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        JButton btnTim = new JButton("Tìm", new ImageIcon(new ImageIcon("src/images/TimKiem.png").getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        JButton btncapNhat = new JButton("Cập nhật", new ImageIcon(new ImageIcon("src/images/CapNhat.png").getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));

        Dimension btnSize = new Dimension(120, 30);
        btnthemNV.setPreferredSize(btnSize);
        btnlamMoi.setPreferredSize(btnSize);
        btnTim.setPreferredSize(btnSize);
        btncapNhat.setPreferredSize(btnSize);

        btnPanelTaiKhoan.add(btnthemNV);
        btnPanelTaiKhoan.add(btnlamMoi);
        btnPanelTaiKhoan.add(btncapNhat);
        btnPanelTaiKhoan.add(btnTim);

        formTaiKhoan.add(btnPanelTaiKhoan);
        rightPanel.add(formTaiKhoan);

        // FORM NHÂN VIÊN
        JPanel formNhanVien = new JPanel();
        formNhanVien.setOpaque(false);
        formNhanVien.setBorder(BorderFactory.createTitledBorder("Thông tin nhân viên"));
        formNhanVien.setLayout(new BoxLayout(formNhanVien, BoxLayout.Y_AXIS));

        String[] labelsNV = {"Họ tên", "Ngày sinh", "Giới tính","Email", "Số điện thoại"};
        for (String lbl : labelsNV) {
            JPanel row = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 10));
            row.setOpaque(false);

            JLabel lb = new JLabel(lbl + " :");
            lb.setFont(new Font("Times New Roman", Font.BOLD, 15));
            lb.setPreferredSize(new Dimension(130, 25));

            JTextField tf = new JTextField();
            tf.setPreferredSize(new Dimension(150, 25));

            row.add(lb);
            row.add(tf);
            formNhanVien.add(row);
        }

        rightPanel.add(Box.createVerticalStrut(5)); // khoảng cách giữa 2 form
        rightPanel.add(formNhanVien);

        panel.add(rightPanel, BorderLayout.EAST);

        return panel;
    }


    // Styling cho các nút chuyển trang
    private void styleSwitchButton(JButton btn) {
        btn.setBackground(new Color(136, 204, 240));
        btn.setFont(new Font("Times New Roman", Font.BOLD, 18));
        btn.setFocusPainted(false);
    }

}