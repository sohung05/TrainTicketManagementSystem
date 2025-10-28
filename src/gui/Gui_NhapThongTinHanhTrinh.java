package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import com.toedter.calendar.JDateChooser;
import java.util.Date;
import java.util.List;
import dao.Ga_DAO;
import entity.Ga;

/**
 * Giao diện nhập thông tin hành trình trước khi vào màn hình bán vé
 */
public class Gui_NhapThongTinHanhTrinh extends JPanel {
    
    private JComboBox<String> cboGaDi;
    private JComboBox<String> cboGaDen;
    private JDateChooser dchNgayDi;
    private JDateChooser dchNgayVe;
    private JRadioButton radMotChieu;
    private JRadioButton radKhuHoi;
    private JButton btnTimKiem;
    private ButtonGroup groupChieu;
    private Ga_DAO gaDAO;
    
    // Callback khi tìm kiếm
    private ThongTinHanhTrinhCallback callback;
    
    public Gui_NhapThongTinHanhTrinh() {
        gaDAO = new Ga_DAO();
        initComponents();
        loadDanhSachGa();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(new Color(234, 243, 251));
        
        // ========== PANEL CHÍNH - CHIA 2 BÊN ==========
        JPanel containerPanel = new JPanel(new BorderLayout(0, 0));
        containerPanel.setBackground(Color.WHITE);
        
        // ========== PANEL BÊN TRÁI - ẢNH ĐOÀN TÀU (60% màn hình) ==========
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setPreferredSize(new Dimension(720, 600)); // 60% của ~1200px width
        leftPanel.setLayout(new GridBagLayout());
        
        // Ảnh đoàn tàu - Full size, không có khung
        try {
            ImageIcon trainIcon = new ImageIcon(getClass().getResource("/icon/doantau.png"));
            // Scale ảnh lớn hơn để chiếm nhiều không gian
            Image scaledImage = trainIcon.getImage().getScaledInstance(650, -1, Image.SCALE_SMOOTH);
            JLabel lblTrainImage = new JLabel(new ImageIcon(scaledImage));
            
            GridBagConstraints gbcImg = new GridBagConstraints();
            gbcImg.gridx = 0;
            gbcImg.gridy = 0;
            gbcImg.insets = new Insets(30, 30, 30, 30);
            leftPanel.add(lblTrainImage, gbcImg);
            
        } catch (Exception e) {
            System.err.println("Không tìm thấy ảnh doantau.png: " + e.getMessage());
            JLabel lblError = new JLabel("🚂 Ảnh đoàn tàu", SwingConstants.CENTER);
            lblError.setFont(new Font("Segoe UI", Font.PLAIN, 20));
            lblError.setForeground(new Color(150, 150, 150));
            leftPanel.add(lblError);
        }
        
        // ========== PANEL BÊN PHẢI - FORM NHẬP THÔNG TIN ==========
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new GridBagLayout());
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Title với icon
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(Color.WHITE);
        
        JLabel lblTitle = new JLabel("Thông tin hành trình");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(new Color(0, 120, 215));
        titlePanel.add(lblTitle);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        rightPanel.add(titlePanel, gbc);
        
        // Separator line
        gbc.gridy = 1;
        gbc.insets = new Insets(5, 10, 15, 10);
        JSeparator separator = new JSeparator();
        separator.setPreferredSize(new Dimension(400, 2));
        separator.setForeground(new Color(0, 120, 215));
        rightPanel.add(separator, gbc);
        
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Ga đi
        gbc.gridwidth = 1;
        gbc.gridy = 2;
        gbc.gridx = 0;
        JLabel lblGaDi = new JLabel("Ga đi");
        lblGaDi.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblGaDi.setForeground(new Color(60, 60, 60));
        rightPanel.add(lblGaDi, gbc);
        
        gbc.gridx = 1;
        cboGaDi = new JComboBox<>();
        cboGaDi.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cboGaDi.setPreferredSize(new Dimension(320, 38));
        cboGaDi.setEditable(true);
        cboGaDi.setBackground(Color.WHITE);
        setupAutoComplete(cboGaDi);
        rightPanel.add(cboGaDi, gbc);
        
        // Ga đến
        gbc.gridy = 3;
        gbc.gridx = 0;
        JLabel lblGaDen = new JLabel("Ga đến");
        lblGaDen.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblGaDen.setForeground(new Color(60, 60, 60));
        rightPanel.add(lblGaDen, gbc);
        
        gbc.gridx = 1;
        cboGaDen = new JComboBox<>();
        cboGaDen.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cboGaDen.setPreferredSize(new Dimension(320, 38));
        cboGaDen.setEditable(true);
        cboGaDen.setBackground(Color.WHITE);
        setupAutoComplete(cboGaDen);
        rightPanel.add(cboGaDen, gbc);
        
        // Radio buttons: Một chiều / Khứ hồi
        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 10, 10, 10);
        
        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
        radioPanel.setBackground(Color.WHITE);
        
        radMotChieu = new JRadioButton("Một chiều");
        radMotChieu.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        radMotChieu.setBackground(Color.WHITE);
        radMotChieu.setSelected(true);
        radMotChieu.setFocusPainted(false);
        
        radKhuHoi = new JRadioButton("Khứ hồi");
        radKhuHoi.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        radKhuHoi.setBackground(Color.WHITE);
        radKhuHoi.setFocusPainted(false);
        
        groupChieu = new ButtonGroup();
        groupChieu.add(radMotChieu);
        groupChieu.add(radKhuHoi);
        
        radioPanel.add(radMotChieu);
        radioPanel.add(radKhuHoi);
        rightPanel.add(radioPanel, gbc);
        
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Ngày đi
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        JLabel lblNgayDi = new JLabel("Ngày đi");
        lblNgayDi.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblNgayDi.setForeground(new Color(60, 60, 60));
        rightPanel.add(lblNgayDi, gbc);
        
        gbc.gridx = 1;
        dchNgayDi = new JDateChooser();
        dchNgayDi.setPreferredSize(new Dimension(320, 38));
        dchNgayDi.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dchNgayDi.setDateFormatString("dd/MM/yyyy");
        dchNgayDi.setDate(null); // Để trống
        rightPanel.add(dchNgayDi, gbc);
        
        // Ngày về
        gbc.gridy = 6;
        gbc.gridx = 0;
        JLabel lblNgayVe = new JLabel("Ngày về");
        lblNgayVe.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblNgayVe.setForeground(new Color(60, 60, 60));
        rightPanel.add(lblNgayVe, gbc);
        
        gbc.gridx = 1;
        dchNgayVe = new JDateChooser();
        dchNgayVe.setPreferredSize(new Dimension(320, 38));
        dchNgayVe.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dchNgayVe.setDateFormatString("dd/MM/yyyy");
        dchNgayVe.setDate(null); // Để trống
        dchNgayVe.setEnabled(false);
        rightPanel.add(dchNgayVe, gbc);
        
        // Button Tìm kiếm
        gbc.gridy = 7;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(25, 10, 10, 10);
        
        btnTimKiem = new JButton("TÌM KIẾM CHUYẾN TÀU");
        btnTimKiem.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnTimKiem.setPreferredSize(new Dimension(250, 50));
        btnTimKiem.setBackground(new Color(0, 120, 215));
        btnTimKiem.setForeground(Color.WHITE);
        btnTimKiem.setFocusPainted(false);
        btnTimKiem.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnTimKiem.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        // Hover effect
        btnTimKiem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnTimKiem.setBackground(new Color(0, 100, 190));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                btnTimKiem.setBackground(new Color(0, 120, 215));
            }
        });
        
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.setBackground(Color.WHITE);
        btnPanel.add(btnTimKiem);
        rightPanel.add(btnPanel, gbc);
        
        // ========== THÊM 2 PANEL VÀO CONTAINER ==========
        containerPanel.add(leftPanel, BorderLayout.WEST);
        containerPanel.add(rightPanel, BorderLayout.CENTER);
        
        add(containerPanel, BorderLayout.CENTER);
        
        // Events
        radMotChieu.addActionListener(e -> dchNgayVe.setEnabled(false));
        radKhuHoi.addActionListener(e -> dchNgayVe.setEnabled(true));
        
        btnTimKiem.addActionListener(e -> handleTimKiem());
    }
    
    private void handleTimKiem() {
        String gaDi = getSelectedGa(cboGaDi);
        String gaDen = getSelectedGa(cboGaDen);
        Date ngayDi = dchNgayDi.getDate();
        Date ngayVe = dchNgayVe.getDate();
        boolean motChieu = radMotChieu.isSelected();
        
        // Validate
        if (gaDi == null || gaDi.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng chọn ga đi!", 
                "Thông báo", JOptionPane.WARNING_MESSAGE);
            cboGaDi.requestFocus();
            return;
        }
        
        if (gaDen == null || gaDen.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng chọn ga đến!", 
                "Thông báo", JOptionPane.WARNING_MESSAGE);
            cboGaDen.requestFocus();
            return;
        }
        
        if (ngayDi == null) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng chọn ngày đi!", 
                "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (gaDi.equalsIgnoreCase(gaDen)) {
            JOptionPane.showMessageDialog(this, 
                "Ga đi và ga đến không được trùng nhau!", 
                "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Callback để chuyển sang màn hình bán vé
        if (callback != null) {
            ThongTinHanhTrinh info = new ThongTinHanhTrinh(
                gaDi, gaDen, ngayDi, ngayVe, motChieu
            );
            callback.onTimKiem(info);
        }
    }
    
    /**
     * Load danh sách ga từ database vào ComboBox
     */
    private void loadDanhSachGa() {
        try {
            List<Ga> danhSachGa = gaDAO.findAll();
            
            // Clear trước khi thêm
            cboGaDi.removeAllItems();
            cboGaDen.removeAllItems();
            
            // Thêm item trống ở đầu
            cboGaDi.addItem("");
            cboGaDen.addItem("");
            
            // Thêm danh sách ga
            for (Ga ga : danhSachGa) {
                String tenGa = ga.getTenGa();
                cboGaDi.addItem(tenGa);
                cboGaDen.addItem(tenGa);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Lỗi khi tải danh sách ga: " + e.getMessage(),
                "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Thiết lập autocomplete cho JComboBox
     */
    private void setupAutoComplete(JComboBox<String> comboBox) {
        JTextField editor = (JTextField) comboBox.getEditor().getEditorComponent();
        
        editor.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String typed = editor.getText().toLowerCase();
                
                if (typed.isEmpty()) {
                    comboBox.hidePopup();
                    return;
                }
                
                // Tìm item khớp
                for (int i = 0; i < comboBox.getItemCount(); i++) {
                    String item = comboBox.getItemAt(i);
                    if (item != null && item.toLowerCase().startsWith(typed)) {
                        comboBox.setSelectedIndex(i);
                        comboBox.showPopup();
                        
                        // Highlight phần gợi ý
                        editor.setSelectionStart(typed.length());
                        editor.setSelectionEnd(item.length());
                        return;
                    }
                }
                
                comboBox.hidePopup();
            }
        });
    }
    
    /**
     * Lấy giá trị ga đã chọn từ ComboBox
     */
    private String getSelectedGa(JComboBox<String> comboBox) {
        Object selected = comboBox.getSelectedItem();
        if (selected != null) {
            return selected.toString().trim();
        }
        
        // Nếu user gõ trực tiếp
        JTextField editor = (JTextField) comboBox.getEditor().getEditorComponent();
        return editor.getText().trim();
    }
    
    public void setCallback(ThongTinHanhTrinhCallback callback) {
        this.callback = callback;
    }
    
    // Interface callback
    public interface ThongTinHanhTrinhCallback {
        void onTimKiem(ThongTinHanhTrinh info);
    }
    
    // Class chứa thông tin hành trình
    public static class ThongTinHanhTrinh {
        private String gaDi;
        private String gaDen;
        private Date ngayDi;
        private Date ngayVe;
        private boolean motChieu;
        
        public ThongTinHanhTrinh(String gaDi, String gaDen, Date ngayDi, Date ngayVe, boolean motChieu) {
            this.gaDi = gaDi;
            this.gaDen = gaDen;
            this.ngayDi = ngayDi;
            this.ngayVe = ngayVe;
            this.motChieu = motChieu;
        }
        
        public String getGaDi() { return gaDi; }
        public String getGaDen() { return gaDen; }
        public Date getNgayDi() { return ngayDi; }
        public Date getNgayVe() { return ngayVe; }
        public boolean isMotChieu() { return motChieu; }
    }
}


