/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package gui;

import dao.HoaDon_DAO;
import dao.Ve_DAO;
import entity.HoaDon;
import entity.Ve;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author PC
 */
public class Gui_TraVe extends javax.swing.JPanel {

    private HoaDon_DAO hoaDonDAO;
    private Ve_DAO veDAO;
    private DefaultTableModel modelHoaDon;
    private DefaultTableModel modelVe;
    private NumberFormat currencyFormat;
    private DateTimeFormatter dateFormatter;
    private DateTimeFormatter timeFormatter;

    /**
     * Creates new form Gui_TraVe
     */
    public Gui_TraVe() {
        initComponents();
        initDAO();
        initCustomComponents();
        loadAllHoaDon();
    }
    
    private void initDAO() {
        hoaDonDAO = new HoaDon_DAO();
        veDAO = new Ve_DAO();
    }
    
    private void initCustomComponents() {
        // Lấy model của các table
        modelHoaDon = (DefaultTableModel) jTable1.getModel();
        modelVe = (DefaultTableModel) jTable2.getModel();
        
        // Format tiền tệ và ngày giờ
        currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        
        // Thêm listener cho table hóa đơn
        jTable1.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = jTable1.getSelectedRow();
                if (selectedRow >= 0) {
                    onHoaDonSelected(selectedRow);
                }
            }
        });
        
        // Thêm listener cho table vé
        jTable2.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = jTable2.getSelectedRow();
                if (selectedRow >= 0) {
                    onVeSelected(selectedRow);
                }
            }
        });
    }
    
    /**
     * Load 15 hóa đơn gần nhất
     */
    private void loadAllHoaDon() {
        System.out.println("📋 Loading 15 hóa đơn gần nhất...");
        modelHoaDon.setRowCount(0);
        
        List<HoaDon> danhSach = hoaDonDAO.findAll();
        System.out.println("✅ Tìm thấy " + danhSach.size() + " hóa đơn");
        
        // Sắp xếp theo ngày giờ giảm dần (mới nhất trước)
        danhSach.sort((hd1, hd2) -> {
            if (hd1.getNgayTao() == null || hd2.getNgayTao() == null) return 0;
            int dateCompare = hd2.getNgayTao().compareTo(hd1.getNgayTao());
            if (dateCompare != 0) return dateCompare;
            if (hd1.getGioTao() == null || hd2.getGioTao() == null) return 0;
            return hd2.getGioTao().compareTo(hd1.getGioTao());
        });
        
        // Chỉ lấy 15 hóa đơn đầu tiên
        int count = 0;
        for (HoaDon hd : danhSach) {
            if (count >= 15) break;
            
            modelHoaDon.addRow(new Object[]{
                hd.getMaHoaDon(),
                hd.getNhanVien() != null ? hd.getNhanVien().getMaNhanVien() : "",
                hd.getKhachHang() != null ? hd.getKhachHang().getCCCD() : "",
                hd.getKhachHang() != null ? hd.getKhachHang().getHoTen() : "",
                hd.getKhachHang() != null ? hd.getKhachHang().getSDT() : "",
                hd.getKhuyenMai() != null ? hd.getKhuyenMai() : "Không",
                hd.getNgayTao() != null ? hd.getNgayTao().toLocalDate().format(dateFormatter) : "",
                hd.getGioTao() != null ? hd.getGioTao().toLocalTime().format(timeFormatter) : "",
                currencyFormat.format(hd.getTongTien())
            });
            count++;
        }
        
        System.out.println("✅ Đã load " + count + " hóa đơn gần nhất");
    }
    
    /**
     * Khi chọn hóa đơn → Load danh sách vé
     */
    private void onHoaDonSelected(int row) {
        String maHoaDon = modelHoaDon.getValueAt(row, 0).toString();
        loadVeByHoaDon(maHoaDon);
        
        // Hiển thị thông tin hóa đơn vào các text field
        jTextField1.setText(modelHoaDon.getValueAt(row, 0).toString()); // Mã hóa đơn
        jTextField2.setText(modelHoaDon.getValueAt(row, 1).toString()); // Mã NV
        jTextField3.setText(modelHoaDon.getValueAt(row, 2).toString()); // CCCD
        jTextField4.setText(modelHoaDon.getValueAt(row, 3).toString()); // Tên KH
        jTextField5.setText(modelHoaDon.getValueAt(row, 4).toString()); // SĐT
    }
    
    /**
     * Khi chọn vé → Hiển thị thông tin vé
     */
    private void onVeSelected(int row) {
        try {
            // Lấy thông tin từ bảng vé
            String maVe = modelVe.getValueAt(row, 0).toString();
            String cccd = modelVe.getValueAt(row, 1).toString();
            String tenKhach = modelVe.getValueAt(row, 2).toString();
            String giaVe = modelVe.getValueAt(row, 10).toString();
            
            // Hiển thị thông tin vé vào các text field
            // jTextField6: Mã vé
            // jTextField7: CCCD
            // jTextField8: Họ tên
            // jTextField9: Giá
            jTextField6.setText(maVe);
            jTextField7.setText(cccd);
            jTextField8.setText(tenKhach);
            jTextField9.setText(giaVe);
            
            System.out.println("✅ Đã hiển thị thông tin vé: " + maVe);
        } catch (Exception e) {
            System.out.println("❌ Lỗi hiển thị thông tin vé: " + e.getMessage());
        }
    }
    
    /**
     * Parse string thành double (loại bỏ ký tự đặc biệt như ₫, dấu phân cách)
     */
    private double parseDouble(String str) {
        if (str == null || str.trim().isEmpty()) return 0;
        
        // Xóa tất cả ký tự không phải số (bao gồm cả dấu phân cách, ₫, khoảng trắng, v.v.)
        str = str.replaceAll("[^0-9.]", "");
        
        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    
    /**
     * Tính tiền hoàn trả cho VÉ CÁ NHÂN theo quy định:
     * - Dưới 4 giờ: không trả vé (0%)
     * - Từ 4 đến dưới 24 giờ: thu 20% giá vé (hoàn 80%)
     * - Trên 24 giờ: thu 10% giá vé (hoàn 90%)
     * - Mức trả vé tối thiểu: 10.000đ/vé
     * 
     * @param giaVe Giá vé
     * @param gioKhoiHanh Thời gian khởi hành
     * @return Số tiền hoàn trả
     */
    private double tinhTienHoanTraCaNhan(double giaVe, java.time.LocalDateTime gioKhoiHanh) {
        if (gioKhoiHanh == null) return 0;
        
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        long hoursUntilDeparture = java.time.Duration.between(now, gioKhoiHanh).toHours();
        
        System.out.println("⏱️ Thời gian còn lại đến khởi hành: " + hoursUntilDeparture + " giờ");
        
        // Phải trả trước ít nhất 24 giờ
        if (hoursUntilDeparture < 24) {
            System.out.println("❌ Không được trả vé: dưới 24 giờ trước khởi hành");
            return -1; // Không được trả vé
        }
        
        double tienHoan = 0;
        
        if (hoursUntilDeparture < 4) {
            // Dưới 4 giờ: không trả vé (nhưng đã check 24h ở trên rồi)
            tienHoan = 0;
        } else if (hoursUntilDeparture < 24) {
            // Từ 4 đến dưới 24 giờ: hoàn 80%
            tienHoan = giaVe * 0.8;
        } else {
            // Trên 24 giờ: hoàn 90%
            tienHoan = giaVe * 0.9;
        }
        
        // Mức trả vé tối thiểu: 10.000đ/vé
        if (tienHoan > 0 && tienHoan < 10000) {
            tienHoan = 10000;
        }
        
        System.out.println("💰 Tiền hoàn trả: " + currencyFormat.format(tienHoan));
        return tienHoan;
    }
    
    /**
     * Tính tiền hoàn trả cho VÉ TẬP THỂ theo quy định:
     * - Dưới 24 giờ: không trả vé (0%)
     * - Từ 24 đến dưới 72 giờ: thu 20% giá vé (hoàn 80%)
     * - Trên 72 giờ: thu 10% giá vé (hoàn 90%)
     * 
     * @param giaVe Giá vé
     * @param gioKhoiHanh Thời gian khởi hành
     * @return Số tiền hoàn trả
     */
    private double tinhTienHoanTraTapThe(double giaVe, java.time.LocalDateTime gioKhoiHanh) {
        if (gioKhoiHanh == null) return 0;
        
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        long hoursUntilDeparture = java.time.Duration.between(now, gioKhoiHanh).toHours();
        
        System.out.println("⏱️ [TẬP THỂ] Thời gian còn lại đến khởi hành: " + hoursUntilDeparture + " giờ");
        
        double tienHoan = 0;
        
        if (hoursUntilDeparture < 24) {
            // Dưới 24 giờ: không trả vé
            System.out.println("❌ Không được trả vé tập thể: dưới 24 giờ trước khởi hành");
            return -1;
        } else if (hoursUntilDeparture < 72) {
            // Từ 24 đến dưới 72 giờ: hoàn 80%
            tienHoan = giaVe * 0.8;
        } else {
            // Trên 72 giờ: hoàn 90%
            tienHoan = giaVe * 0.9;
        }
        
        System.out.println("💰 [TẬP THỂ] Tiền hoàn trả: " + currencyFormat.format(tienHoan));
        return tienHoan;
    }
    
    /**
     * Load danh sách vé theo mã hóa đơn
     */
    private void loadVeByHoaDon(String maHoaDon) {
        System.out.println("🎫 Loading vé cho hóa đơn: " + maHoaDon);
        modelVe.setRowCount(0);
        
        // Xóa thông tin vé cũ
        jTextField6.setText("");
        jTextField7.setText("");
        jTextField8.setText("");
        jTextField9.setText("");
        
        List<Ve> danhSachVe = veDAO.findByMaHoaDon(maHoaDon);
        System.out.println("✅ Tìm thấy " + danhSachVe.size() + " vé (chưa trả)");
        
        for (Ve ve : danhSachVe) {
            modelVe.addRow(new Object[]{
                ve.getMaVe(),
                ve.getSoCCCD() != null ? ve.getSoCCCD() : "",
                ve.getTenKhachHang() != null ? ve.getTenKhachHang() : "",
                ve.getLoaiVe() != null ? ve.getLoaiVe().getTenLoaiVe() : "",
                ve.getLichTrinh() != null && ve.getLichTrinh().getGaDi() != null ? 
                    ve.getLichTrinh().getGaDi().getTenGa() : "",
                ve.getLichTrinh() != null && ve.getLichTrinh().getGaDen() != null ? 
                    ve.getLichTrinh().getGaDen().getTenGa() : "",
                ve.getLichTrinh() != null && ve.getLichTrinh().getChuyenTau() != null ? 
                    ve.getLichTrinh().getChuyenTau().getSoHieuTau() : "",
                ve.getChoNgoi() != null && ve.getChoNgoi().getToa() != null ? 
                    String.valueOf(ve.getChoNgoi().getToa().getSoToa()) : "",
                ve.getChoNgoi() != null ? String.valueOf(ve.getChoNgoi().getViTri()) : "",
                ve.getThoiGianLenTau() != null ? 
                    ve.getThoiGianLenTau().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "",
                currencyFormat.format(ve.getGiaVe())
            });
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jTextField7 = new javax.swing.JTextField();
        jTextField8 = new javax.swing.JTextField();
        jTextField9 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        btnTimHoaDon = new javax.swing.JButton();
        btnInHoaDon = new javax.swing.JButton();
        btnXoaTrang = new javax.swing.JButton();
        btnTraTapVe = new javax.swing.JButton();
        btnTimVe = new javax.swing.JButton();
        btnInVe = new javax.swing.JButton();
        btnTraVe = new javax.swing.JButton();

        setBackground(new java.awt.Color(234, 243, 251));

        jPanel1.setBackground(new java.awt.Color(234, 243, 251));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Thông tin hóa đơn"));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("Mã hóa đơn:");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setText("Mã nhân viên:");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setText("CCCD:");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel6.setText("Tên khách hàng:");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setText("Số điện thoại:");

        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7)
                    .addComponent(jLabel5)
                    .addComponent(jLabel4)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(42, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel2.setBackground(new java.awt.Color(234, 243, 251));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Thông tin vé"));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel8.setText("Mã vé:");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel9.setText("CCCD:");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel10.setText("Họ tên:");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel11.setText("Giá:");

        jTextField6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(jLabel8)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11))
                .addGap(90, 90, 90)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(36, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Hóa Đơn");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Bảng Vé");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã hóa đơn", "Mã nhân viên", "CCCD", "Tên khách hàng", "Số điện thoại", "Khuyến mãi", "Ngày lập", "Giờ lập ", "Tổng tiền"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã vé ", "CCCD", "Tên khách hàng", "Đối tượng", "Ga đi ", "Ga đến ", "Mã tàu", "Số toa", "Vị trí chỗ ", "Giờ lên tàu", "Giá"
            }
        ));
        jScrollPane2.setViewportView(jTable2);

        btnTimHoaDon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/TimKiem.png"))); // NOI18N
        btnTimHoaDon.setText("Tìm hóa đơn");
        btnTimHoaDon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimHoaDonActionPerformed(evt);
            }
        });

        btnInHoaDon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/print.png"))); // NOI18N
        btnInHoaDon.setText("In Hóa Đơn");
        btnInHoaDon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInHoaDonActionPerformed(evt);
            }
        });

        btnXoaTrang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/clear.png"))); // NOI18N
        btnXoaTrang.setText("Xóa trắng");
        btnXoaTrang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaTrangActionPerformed(evt);
            }
        });

        btnTraTapVe.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/documents.png"))); // NOI18N
        btnTraTapVe.setText("Trả tập vé");
        btnTraTapVe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTraTapVeActionPerformed(evt);
            }
        });

        btnTimVe.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/TimKiem.png"))); // NOI18N
        btnTimVe.setText("Tìm vé");
        btnTimVe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimVeActionPerformed(evt);
            }
        });

        btnInVe.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/print.png"))); // NOI18N
        btnInVe.setText("In vé");
        btnInVe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInVeActionPerformed(evt);
            }
        });

        btnTraVe.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/exchange.png"))); // NOI18N
        btnTraVe.setText("Trả vé");
        btnTraVe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTraVeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(47, 47, 47)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(btnTimHoaDon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnInHoaDon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(45, 45, 45)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(btnTraTapVe, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnXoaTrang, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)))
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 736, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(27, 27, 27)
                                .addComponent(btnTimVe)
                                .addGap(27, 27, 27)
                                .addComponent(btnInVe)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnTraVe)
                                .addGap(18, 18, 18)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnTimHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnXoaTrang, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnInHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnTraTapVe, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 332, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnTimVe, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnInVe, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnTraVe, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 268, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jTextField6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField6ActionPerformed

    private void btnTimHoaDonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimHoaDonActionPerformed
        // ⚡ TÌM HÓA ĐƠN theo bất kỳ thông tin nào
        String maHD = jTextField1.getText().trim();
        String maNV = jTextField2.getText().trim();
        String cccd = jTextField3.getText().trim();
        String tenKH = jTextField4.getText().trim();
        String sdt = jTextField5.getText().trim();
        
        // Nếu tất cả đều trống → Load tất cả
        if (maHD.isEmpty() && maNV.isEmpty() && cccd.isEmpty() && tenKH.isEmpty() && sdt.isEmpty()) {
            loadAllHoaDon();
            return;
        }
        
        System.out.println("🔍 Tìm kiếm hóa đơn với: mã=" + maHD + ", maNV=" + maNV + ", cccd=" + cccd + ", tên=" + tenKH + ", sdt=" + sdt);
        
        // Lọc hóa đơn
        List<HoaDon> allHoaDon = hoaDonDAO.findAll();
        modelHoaDon.setRowCount(0);
        int count = 0;
        
        for (HoaDon hd : allHoaDon) {
            boolean match = true;
            
            // Kiểm tra từng điều kiện (nếu có nhập)
            if (!maHD.isEmpty() && !hd.getMaHoaDon().toLowerCase().contains(maHD.toLowerCase())) {
                match = false;
            }
            if (!maNV.isEmpty() && hd.getNhanVien() != null && 
                !hd.getNhanVien().getMaNhanVien().toLowerCase().contains(maNV.toLowerCase())) {
                match = false;
            }
            if (!cccd.isEmpty() && hd.getKhachHang() != null && 
                !hd.getKhachHang().getCCCD().toLowerCase().contains(cccd.toLowerCase())) {
                match = false;
            }
            if (!tenKH.isEmpty() && hd.getKhachHang() != null && 
                !hd.getKhachHang().getHoTen().toLowerCase().contains(tenKH.toLowerCase())) {
                match = false;
            }
            if (!sdt.isEmpty() && hd.getKhachHang() != null && 
                !hd.getKhachHang().getSDT().toLowerCase().contains(sdt.toLowerCase())) {
                match = false;
            }
            
            if (match) {
                modelHoaDon.addRow(new Object[]{
                    hd.getMaHoaDon(),
                    hd.getNhanVien() != null ? hd.getNhanVien().getMaNhanVien() : "",
                    hd.getKhachHang() != null ? hd.getKhachHang().getCCCD() : "",
                    hd.getKhachHang() != null ? hd.getKhachHang().getHoTen() : "",
                    hd.getKhachHang() != null ? hd.getKhachHang().getSDT() : "",
                    hd.getKhuyenMai() != null ? hd.getKhuyenMai() : "Không",
                    hd.getNgayTao() != null ? hd.getNgayTao().toLocalDate().format(dateFormatter) : "",
                    hd.getGioTao() != null ? hd.getGioTao().toLocalTime().format(timeFormatter) : "",
                    currencyFormat.format(hd.getTongTien())
                });
                count++;
            }
        }
        
        System.out.println("✅ Tìm thấy " + count + " hóa đơn");
        
        if (count == 0) {
            JOptionPane.showMessageDialog(this,
                "Không tìm thấy hóa đơn nào!",
                "Kết quả tìm kiếm",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnTimHoaDonActionPerformed

    private void btnTraTapVeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTraTapVeActionPerformed
        // ⚡ TRẢ TẬP VÉ (VÉ TẬP THỂ) - Tính tiền hoàn trả theo quy định
        
        // Kiểm tra xem đã chọn hóa đơn chưa
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng chọn hóa đơn cần trả!",
                "Thông báo",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Lấy thông tin hóa đơn từ database
        String maHoaDon = modelHoaDon.getValueAt(selectedRow, 0).toString();
        entity.HoaDon hoaDonCanTra = hoaDonDAO.findByMaHoaDon(maHoaDon);
        if (hoaDonCanTra == null) {
            JOptionPane.showMessageDialog(this,
                "Không tìm thấy thông tin hóa đơn!",
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        double tongTienHoaDon = hoaDonCanTra.getTongTien();
        
        System.out.println("📋 Trả tập vé cho hóa đơn: " + maHoaDon + " | Tổng tiền: " + String.format("%,.0f đ", tongTienHoaDon));
        
        // Kiểm tra số lượng vé trong bảng vé
        int soLuongVe = modelVe.getRowCount();
        if (soLuongVe == 0) {
            JOptionPane.showMessageDialog(this,
                "Không có vé nào trong hóa đơn này!",
                "Thông báo",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        System.out.println("🎫 Số lượng vé: " + soLuongVe);
        
        // Tính tổng tiền hoàn trả cho tất cả vé (theo quy định VÉ TẬP THỂ)
        double tongTienHoanLai = 0;
        boolean coVeKhongDuDieuKien = false;
        StringBuilder chiTietVe = new StringBuilder();
        
        for (int i = 0; i < soLuongVe; i++) {
            String maVe = modelVe.getValueAt(i, 0).toString();
            String thoiGianKhoiHanhStr = modelVe.getValueAt(i, 9).toString();
            
            // Lấy giá vé từ database để tránh lỗi parse từ GUI
            entity.Ve veHienTai = veDAO.findByMaVe(maVe);
            if (veHienTai == null) {
                System.out.println("❌ Không tìm thấy vé: " + maVe);
                continue;
            }
            double giaVe = veHienTai.getGiaVe();
            
            // Parse thời gian khởi hành
            java.time.LocalDateTime gioKhoiHanh = null;
            try {
                java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                gioKhoiHanh = java.time.LocalDateTime.parse(thoiGianKhoiHanhStr, formatter);
            } catch (Exception e) {
                System.out.println("❌ Lỗi parse thời gian khởi hành cho vé: " + maVe);
                continue;
            }
            
            // Tính tiền hoàn trả cho vé này theo quy định VÉ TẬP THỂ
            double tienHoanVe = tinhTienHoanTraTapThe(giaVe, gioKhoiHanh);
            
            if (tienHoanVe < 0) {
                coVeKhongDuDieuKien = true;
                chiTietVe.append(String.format("• %s: Không đủ điều kiện trả\n", maVe));
            } else {
                tongTienHoanLai += tienHoanVe;
                chiTietVe.append(String.format("• %s: %s\n", maVe, currencyFormat.format(tienHoanVe)));
            }
        }
        
        // Nếu có vé không đủ điều kiện → Hỏi xem có muốn tiếp tục không
        if (coVeKhongDuDieuKien) {
            String warning = "Một số vé không đủ điều kiện trả!\n" +
                           "Vé tập thể phải trả trước khi tàu khởi hành ít nhất 24 giờ.\n\n" +
                           "Chi tiết:\n" + chiTietVe.toString() +
                           "\nBạn có muốn trả các vé còn lại không?";
            
            int confirm = JOptionPane.showConfirmDialog(
                this,
                warning,
                "Cảnh báo",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );
            
            if (confirm != JOptionPane.YES_OPTION) {
                System.out.println("❌ Đã hủy trả tập vé");
                return;
            }
        }
        
        // Nếu không có vé nào được hoàn trả
        if (tongTienHoanLai <= 0) {
            JOptionPane.showMessageDialog(this,
                "Không có vé nào đủ điều kiện trả trong hóa đơn này!",
                "Không thể trả vé",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Tính chiết khấu (số tiền bị trừ)
        double chietKhau = tongTienHoaDon - tongTienHoanLai;
        
        // Hiện dialog xác nhận
        String message = String.format(
            "Xác nhận trả tập vé cho hóa đơn %s?\n\n" +
            "Số lượng vé: %d\n" +
            "Tổng tiền hóa đơn: %,.0f đ\n" +
            "Chiết khấu: %,.0f đ\n" +
            "Số tiền hoàn lại: %,.0f đ",
            maHoaDon,
            soLuongVe,
            tongTienHoaDon,
            chietKhau,
            tongTienHoanLai
        );
        
        int choice = JOptionPane.showConfirmDialog(
            this,
            message,
            "Xác nhận",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (choice == JOptionPane.YES_OPTION) {
            // ⚡ Xử lý logic trả tập vé trong database
            System.out.println("✅ Đang xử lý trả tập vé cho hóa đơn: " + maHoaDon);
            System.out.println("💰 Tổng tiền hoàn lại: " + currencyFormat.format(tongTienHoanLai));
            
            int soVeDaTra = 0;
            
            // 1. Cập nhật trạng thái tất cả vé trong hóa đơn thành 0 (đã trả)
            for (int i = 0; i < soLuongVe; i++) {
                String maVe = modelVe.getValueAt(i, 0).toString();
                boolean success = veDAO.delete(maVe); // Set trangThai = 0
                if (success) {
                    soVeDaTra++;
                    System.out.println("   ✅ Đã trả vé: " + maVe);
                } else {
                    System.err.println("   ❌ Lỗi khi trả vé: " + maVe);
                }
            }
            
            // 2. Cập nhật tổng tiền hóa đơn (trừ số tiền hoàn lại)
            dao.HoaDon_DAO hdDAO = new dao.HoaDon_DAO();
            double tongTienMoi = tongTienHoaDon - tongTienHoanLai; // Trừ số tiền hoàn lại
            if (tongTienMoi < 0) tongTienMoi = 0; // Đảm bảo không âm
            
            hdDAO.updateTongTien(maHoaDon, tongTienMoi);
            System.out.println("✅ Đã cập nhật tổng tiền hóa đơn: " + maHoaDon + 
                " | Cũ: " + currencyFormat.format(tongTienHoaDon) + 
                " | Mới: " + currencyFormat.format(tongTienMoi) +
                " | Đã trừ: " + currencyFormat.format(tongTienHoanLai));
            
            // 3. Hiển thị thông báo thành công
            JOptionPane.showMessageDialog(this,
                String.format("Đã trả tập vé thành công!\nSố vé đã trả: %d/%d\nSố tiền hoàn lại: %,.0f đ",
                    soVeDaTra, soLuongVe, tongTienHoanLai),
                "Thành công",
                JOptionPane.INFORMATION_MESSAGE);
            
            // 4. Reload lại danh sách hóa đơn và xóa bảng vé
            loadAllHoaDon();
            modelVe.setRowCount(0); // Clear bảng vé vì đã trả hết
            
            System.out.println("✅ Hoàn tất trả tập vé");
        } else {
            System.out.println("❌ Đã hủy trả tập vé");
        }
    }//GEN-LAST:event_btnTraTapVeActionPerformed

    private void btnXoaTrangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaTrangActionPerformed
        // ⚡ XÓA TRẮNG - Clear tất cả text field
        jTextField1.setText("");
        jTextField2.setText("");
        jTextField3.setText("");
        jTextField4.setText("");
        jTextField5.setText("");
        jTextField6.setText("");
        jTextField7.setText("");
        jTextField8.setText("");
        jTextField9.setText("");
        
        // Clear bảng vé
        modelVe.setRowCount(0);
        
        // Reload tất cả hóa đơn
        loadAllHoaDon();
        
        System.out.println("🧹 Đã xóa trắng tất cả dữ liệu");
    }//GEN-LAST:event_btnXoaTrangActionPerformed

    private void btnTraVeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTraVeActionPerformed
        // ⚡ TRẢ VÉ ĐƠN LẺ (VÉ CÁ NHÂN)
        int selectedRow = jTable2.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng chọn vé cần trả!",
                "Thông báo",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String maVe = modelVe.getValueAt(selectedRow, 0).toString();
        String thoiGianKhoiHanhStr = modelVe.getValueAt(selectedRow, 9).toString();
        
        System.out.println("🎫 Trả vé: " + maVe + " | Khởi hành: " + thoiGianKhoiHanhStr);
        
        // Lấy giá vé từ database để tránh lỗi parse từ GUI
        entity.Ve veCanTra = veDAO.findByMaVe(maVe);
        if (veCanTra == null) {
            JOptionPane.showMessageDialog(this,
                "Không tìm thấy thông tin vé!",
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        double giaVe = veCanTra.getGiaVe();
        
        // Parse thời gian khởi hành (format: "dd/MM/yyyy HH:mm")
        java.time.LocalDateTime gioKhoiHanh = null;
        try {
            java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            gioKhoiHanh = java.time.LocalDateTime.parse(thoiGianKhoiHanhStr, formatter);
        } catch (Exception e) {
            System.out.println("❌ Lỗi parse thời gian khởi hành: " + e.getMessage());
            JOptionPane.showMessageDialog(this,
                "Không thể xác định thời gian khởi hành của vé!",
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Tính tiền hoàn trả theo quy định VÉ CÁ NHÂN
        double tienHoanLai = tinhTienHoanTraCaNhan(giaVe, gioKhoiHanh);
        
        if (tienHoanLai < 0) {
            JOptionPane.showMessageDialog(this,
                "Không thể trả vé!\n" +
                "Vé cá nhân phải trả trước khi tàu khởi hành ít nhất 24 giờ.",
                "Không đủ điều kiện trả vé",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Tính chiết khấu (số tiền bị trừ)
        double chietKhau = giaVe - tienHoanLai;
        
        // Hiển thị dialog xác nhận
        String message = String.format(
            "Xác nhận trả vé %s?\n\n" +
            "Giá vé: %,.0f đ\n" +
            "Chiết khấu: %,.0f đ\n" +
            "Số tiền hoàn lại: %,.0f đ",
            maVe,
            giaVe,
            chietKhau,
            tienHoanLai
        );
        
        int choice = JOptionPane.showConfirmDialog(
            this,
            message,
            "Xác nhận trả vé",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (choice == JOptionPane.YES_OPTION) {
            // ⚡ Xử lý logic trả vé trong database
            System.out.println("✅ Đang xử lý trả vé: " + maVe);
            System.out.println("💰 Tiền hoàn lại: " + currencyFormat.format(tienHoanLai));
            
            // 1. Cập nhật trạng thái vé thành 0 (đã trả)
            boolean updateSuccess = veDAO.delete(maVe); // delete() = soft delete (set trangThai = 0)
            
            if (!updateSuccess) {
                JOptionPane.showMessageDialog(this,
                    "Lỗi khi cập nhật trạng thái vé!",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // 2. Lấy mã hóa đơn và cập nhật tổng tiền (trừ số tiền hoàn lại)
            int hoaDonRow = jTable1.getSelectedRow();
            String maHoaDon = hoaDonRow >= 0 ? modelHoaDon.getValueAt(hoaDonRow, 0).toString() : null;
            
            if (maHoaDon != null) {
                // Lấy tổng tiền hiện tại của hóa đơn
                dao.HoaDon_DAO hdDAO = new dao.HoaDon_DAO();
                entity.HoaDon hoaDon = hdDAO.findByMaHoaDon(maHoaDon);
                if (hoaDon != null) {
                    double tongTienCu = hoaDon.getTongTien();
                    double tongTienMoi = tongTienCu - tienHoanLai; // Trừ số tiền hoàn lại
                    if (tongTienMoi < 0) tongTienMoi = 0; // Đảm bảo không âm
                    
                    hdDAO.updateTongTien(maHoaDon, tongTienMoi);
                    System.out.println("✅ Đã cập nhật tổng tiền hóa đơn: " + maHoaDon + 
                        " | Cũ: " + currencyFormat.format(tongTienCu) + 
                        " | Mới: " + currencyFormat.format(tongTienMoi) +
                        " | Đã trừ: " + currencyFormat.format(tienHoanLai));
                }
            }
            
            // 3. Hiển thị thông báo thành công
            JOptionPane.showMessageDialog(this,
                String.format("Đã trả vé thành công!\nSố tiền hoàn lại: %,.0f đ", tienHoanLai),
                "Thành công",
                JOptionPane.INFORMATION_MESSAGE);
            
            // 4. Reload lại bảng vé và hóa đơn
            if (maHoaDon != null) {
                loadVeByHoaDon(maHoaDon); // Reload bảng vé (vé đã trả sẽ biến mất)
                loadAllHoaDon(); // Reload bảng hóa đơn (tổng tiền đã thay đổi)
            }
            
            System.out.println("✅ Hoàn tất trả vé");
        } else {
            System.out.println("❌ Đã hủy trả vé");
        }
    }//GEN-LAST:event_btnTraVeActionPerformed

    private void btnInVeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInVeActionPerformed
        // ⚡ IN VÉ - Mở Dialog_Ve với dữ liệu vé đã chọn
        int selectedRow = jTable2.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng chọn vé cần in!",
                "Thông báo",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String maVe = modelVe.getValueAt(selectedRow, 0).toString();
        System.out.println("🖨️ In vé: " + maVe);
        
        // Lấy hóa đơn hiện tại
        int hoaDonRow = jTable1.getSelectedRow();
        if (hoaDonRow < 0) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng chọn hóa đơn trước!",
                "Thông báo",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String maHoaDon = modelHoaDon.getValueAt(hoaDonRow, 0).toString();
        
        // Lấy danh sách vé từ database
        List<Ve> danhSachVe = veDAO.findByMaHoaDon(maHoaDon);
        
        // Tìm vé có mã trùng khớp
        Ve veCanIn = null;
        for (Ve ve : danhSachVe) {
            if (ve.getMaVe().equals(maVe)) {
                veCanIn = ve;
                break;
            }
        }
        
        if (veCanIn == null) {
            JOptionPane.showMessageDialog(this,
                "Không tìm thấy thông tin vé!",
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Mở Dialog_Ve
        java.awt.Frame parentFrame = (java.awt.Frame) javax.swing.SwingUtilities.getWindowAncestor(this);
        Dialog_Ve dialogVe = new Dialog_Ve(parentFrame, false, veCanIn);
        dialogVe.setVisible(true);
        
        System.out.println("✅ Đã mở Dialog_Ve cho vé: " + maVe);
    }//GEN-LAST:event_btnInVeActionPerformed
    
    private void btnInHoaDonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInHoaDonActionPerformed
        // ⚡ IN HÓA ĐƠN - Mở Dialog_HoaDon và load dữ liệu từ database
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng chọn hóa đơn cần in!",
                "Thông báo",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Lấy mã hóa đơn
        String maHoaDon = modelHoaDon.getValueAt(selectedRow, 0).toString();
        
        System.out.println("🖨️ Mở Dialog_HoaDon cho mã: " + maHoaDon);
        
        // Mở Dialog_HoaDon với constructor load từ database
        // Constructor này sẽ tự động load toàn bộ thông tin hóa đơn và vé từ DB
        java.awt.Frame parentFrame = (java.awt.Frame) javax.swing.SwingUtilities.getWindowAncestor(this);
        Dialog_HoaDon dialogHoaDon = new Dialog_HoaDon(parentFrame, false, maHoaDon);
        dialogHoaDon.setVisible(true);
        
        System.out.println("✅ Đã mở Dialog_HoaDon với đầy đủ thông tin vé");
    }//GEN-LAST:event_btnInHoaDonActionPerformed
    
    private void btnTimVeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimVeActionPerformed
        // ⚡ TÌM VÉ theo thông tin
        String maVe = jTextField6.getText().trim();
        String cccd = jTextField7.getText().trim();
        String hoTen = jTextField8.getText().trim();
        
        // Nếu tất cả đều trống → Yêu cầu chọn hóa đơn
        if (maVe.isEmpty() && cccd.isEmpty() && hoTen.isEmpty()) {
            int hoaDonRow = jTable1.getSelectedRow();
            if (hoaDonRow >= 0) {
                String maHoaDon = modelHoaDon.getValueAt(hoaDonRow, 0).toString();
                loadVeByHoaDon(maHoaDon);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn hóa đơn hoặc nhập thông tin vé để tìm kiếm!",
                    "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            }
            return;
        }
        
        System.out.println("🔍 Tìm kiếm vé với: mã=" + maVe + ", cccd=" + cccd + ", tên=" + hoTen);
        
        // Lấy danh sách vé hiện tại và lọc
        int hoaDonRow = jTable1.getSelectedRow();
        if (hoaDonRow < 0) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng chọn hóa đơn trước!",
                "Thông báo",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String maHoaDon = modelHoaDon.getValueAt(hoaDonRow, 0).toString();
        List<Ve> allVe = veDAO.findByMaHoaDon(maHoaDon);
        
        modelVe.setRowCount(0);
        int count = 0;
        
        for (Ve ve : allVe) {
            boolean match = true;
            
            if (!maVe.isEmpty() && !ve.getMaVe().toLowerCase().contains(maVe.toLowerCase())) {
                match = false;
            }
            if (!cccd.isEmpty() && (ve.getSoCCCD() == null || !ve.getSoCCCD().toLowerCase().contains(cccd.toLowerCase()))) {
                match = false;
            }
            if (!hoTen.isEmpty() && (ve.getTenKhachHang() == null || !ve.getTenKhachHang().toLowerCase().contains(hoTen.toLowerCase()))) {
                match = false;
            }
            
            if (match) {
                modelVe.addRow(new Object[]{
                    ve.getMaVe(),
                    ve.getSoCCCD() != null ? ve.getSoCCCD() : "",
                    ve.getTenKhachHang() != null ? ve.getTenKhachHang() : "",
                    ve.getLoaiVe() != null ? ve.getLoaiVe().getTenLoaiVe() : "",
                    ve.getLichTrinh() != null && ve.getLichTrinh().getGaDi() != null ? 
                        ve.getLichTrinh().getGaDi().getTenGa() : "",
                    ve.getLichTrinh() != null && ve.getLichTrinh().getGaDen() != null ? 
                        ve.getLichTrinh().getGaDen().getTenGa() : "",
                    ve.getLichTrinh() != null && ve.getLichTrinh().getChuyenTau() != null ? 
                        ve.getLichTrinh().getChuyenTau().getSoHieuTau() : "",
                    ve.getChoNgoi() != null && ve.getChoNgoi().getToa() != null ? 
                        String.valueOf(ve.getChoNgoi().getToa().getSoToa()) : "",
                    ve.getChoNgoi() != null ? String.valueOf(ve.getChoNgoi().getViTri()) : "",
                    ve.getThoiGianLenTau() != null ? 
                        ve.getThoiGianLenTau().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "",
                    currencyFormat.format(ve.getGiaVe())
                });
                count++;
            }
        }
        
        System.out.println("✅ Tìm thấy " + count + " vé");
        
        if (count == 0) {
            JOptionPane.showMessageDialog(this,
                "Không tìm thấy vé nào!",
                "Kết quả tìm kiếm",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnTimVeActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnInHoaDon;
    private javax.swing.JButton btnInVe;
    private javax.swing.JButton btnTimHoaDon;
    private javax.swing.JButton btnTimVe;
    private javax.swing.JButton btnTraTapVe;
    private javax.swing.JButton btnTraVe;
    private javax.swing.JButton btnXoaTrang;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    // End of variables declaration//GEN-END:variables
}
