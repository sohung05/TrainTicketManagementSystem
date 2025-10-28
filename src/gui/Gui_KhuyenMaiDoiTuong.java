package gui;

import dao.KhuyenMaiDoiTuong_DAO;
import dao.KhuyenMaiHoaDon_DAO;
import entity.KhuyenMai;
import entity.DoiTuong;

import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import com.toedter.calendar.JDateChooser;


/**
 * @description Quản lý khuyến mãi theo đối tượng
 * @author : Anh
 * @date : 25/10/2025
 * @version : 1.0
 */
public class Gui_KhuyenMaiDoiTuong extends javax.swing.JPanel {
    private static int demKhuyenMai = 1;
    private boolean dangCapNhat = false;
    private DefaultTableModel model;
    private void loadTableData() {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0); // xóa dữ liệu cũ
        jTable1.setModel(model);

        KhuyenMaiDoiTuong_DAO dao = new KhuyenMaiDoiTuong_DAO();
        List<Object[]> dsKM = dao.getDanhSachKhuyenMaiDoiTuong();
        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        for (Object[] row : dsKM) {
            row[6] = ((boolean) row[6]) ? "Hoạt động" : "Tạm ngưng";
            model.addRow(row);
        }
    }

    public Gui_KhuyenMaiDoiTuong() {
        initComponents();
        initEvent();
        initTable();
        loadTableData();
    }


    private void initTable() {
        model = (DefaultTableModel) jTable1.getModel();
    }
    /**
     * 🔍 Kiểm tra dữ liệu người dùng nhập trước khi thêm khuyến mãi
     * @return true nếu hợp lệ, false nếu có lỗi
     */
    private boolean validateInput() {
        String ma = jTextField1.getText().trim();
        String ten = jTextField2.getText().trim();
        String doiTuongStr = (String) jComboBox1.getSelectedItem();
        Date start = jDateChooser1.getDate();
        Date end = jDateChooser2.getDate();
        String chietKhauStr = jTextField4.getText().trim();

        // --- Kiểm tra mã khuyến mãi ---
        if (ma.isEmpty() || !ma.matches("^KM\\d{8}\\d{2}$")) {
            JOptionPane.showMessageDialog(this,
                    "❌ Mã khuyến mãi không hợp lệ!\nPhải có dạng: KMddMMyyyyXX");
            jTextField1.requestFocus();
            return false;
        }

        // --- Kiểm tra tên ---
        if (ten.isEmpty()) {
            JOptionPane.showMessageDialog(this, "❌ Tên khuyến mãi không được để trống!");
            jTextField2.requestFocus();
            return false;
        }

        // --- Kiểm tra đối tượng ---
        if (doiTuongStr == null || doiTuongStr.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "❌ Vui lòng chọn đối tượng khuyến mãi!");
            jComboBox1.requestFocus();
            return false;
        }

        // --- Kiểm tra ngày ---
        if (start == null) {
            JOptionPane.showMessageDialog(this, "❌ Thời gian bắt đầu không được để trống!");
            jDateChooser1.requestFocus();
            return false;
        }
        if (end == null) {
            JOptionPane.showMessageDialog(this, "❌ Thời gian kết thúc không được để trống!");
            jDateChooser2.requestFocus();
            return false;
        }
        if (end.before(start)) {
            JOptionPane.showMessageDialog(this, "❌ Thời gian kết thúc phải sau thời gian bắt đầu!");
            return false;
        }
// --- Kiểm tra chiết khấu ---
        if (chietKhauStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "❌ Chiết khấu không được để trống!");
            jTextField4.requestFocus();
            return false;
        }

        double chietKhau;
        try {
            chietKhau = Double.parseDouble(chietKhauStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "❌ Chiết khấu phải là số hợp lệ!");
            jTextField4.requestFocus();
            return false;
        }

        if (chietKhau <= 0 || chietKhau > 1) {
            JOptionPane.showMessageDialog(this, "❌ Chiết khấu phải lớn hơn 0 và nhỏ hơn hoặc bằng 1 (tức 0–100%)!");
            jTextField4.requestFocus();
            return false;
        }

        return true;
    }

    private void initEvent() {

        // Thêm
        jButton4.addActionListener(e -> {
            if (!validateInput()) {
                return;
            }

            try {
                String ten = jTextField2.getText().trim();
                String doiTuongStr = jComboBox1.getSelectedItem().toString().trim();
                Date start = jDateChooser1.getDate();
                Date end = jDateChooser2.getDate();
                String chietKhauStr = jTextField4.getText().trim();

                // --- Sinh mã tự động theo ngày bắt đầu ---
                SimpleDateFormat sdfDate = new SimpleDateFormat("ddMMyyyy");
                String datePart = sdfDate.format(start);
                String ma = "KM" + datePart + (int) (Math.random() * 90 + 10); // ví dụ: KM2710202593

                double chietKhau = Double.parseDouble(chietKhauStr);
                if (chietKhau < 0 || chietKhau > 1) {
                    JOptionPane.showMessageDialog(this, "⚠️ Chiết khấu phải trong khoảng 0 - 1");
                    return;
                }

                // --- Tạo đối tượng khuyến mãi ---
                KhuyenMai km = new KhuyenMai(
                        ma,
                        ten,
                        "Đối tượng",
                        new java.sql.Timestamp(start.getTime()).toLocalDateTime(),
                        new java.sql.Timestamp(end.getTime()).toLocalDateTime(),
                        true
                );

                // --- Chuyển từ ComboBox sang Enum DoiTuong ---
                DoiTuong doiTuong = switch (doiTuongStr) {
                    case "Sinh viên" -> DoiTuong.SinhVien;
                    case "Trẻ em" -> DoiTuong.TreEm;
                    case "Người lớn" -> DoiTuong.NguoiLon;
                    case "Người cao tuổi" -> DoiTuong.NguoiCaoTuoi;
                    default -> throw new IllegalArgumentException("Đối tượng không hợp lệ: " + doiTuongStr);
                };

                // --- Gọi DAO để lưu vào DB ---
                KhuyenMaiDoiTuong_DAO dao = new KhuyenMaiDoiTuong_DAO();
                boolean success = dao.themKhuyenMaiDoiTuong(km, doiTuong, chietKhau / 100.0); // đổi về dạng 0.x

                if (success) {
                    JOptionPane.showMessageDialog(this, "✅ Thêm khuyến mãi thành công!");

                    // ✅ Load lại toàn bộ dữ liệu từ database để bảng đồng bộ
                    loadTableData();
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Thêm khuyến mãi thất bại!");
                }


            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "❌ Lỗi: " + ex.getMessage());
            }
        });
        // Lọc
        jButton3.addActionListener(e -> {
            String doiTuong = jComboBox1.getSelectedItem().toString().trim();
            Date startDate = jDateChooser1.getDate();
            Date endDate = jDateChooser2.getDate();

            if (doiTuong.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn đối tượng cần lọc!");
                return;
            }
            if (startDate == null || endDate == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn khoảng thời gian lọc!");
                return;
            }
            if (endDate.before(startDate)) {
                JOptionPane.showMessageDialog(this, "Ngày kết thúc phải sau ngày bắt đầu!");
                return;
            }

            DefaultTableModel filteredModel = new DefaultTableModel(
                    new Object[]{"Mã khuyến mãi", "Tên khuyến mãi", "Đối tượng",
                            "Thời gian áp dụng", "Thời gian kết thúc", "Chiết khấu", "Trạng thái"}, 0
            );

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            for (int i = 0; i < model.getRowCount(); i++) {
                String dt = model.getValueAt(i, 2).toString();
                String tgApDungStr = model.getValueAt(i, 3).toString();
                String tgKetThucStr = model.getValueAt(i, 4).toString();

                try {
                    Date tgApDung = sdf.parse(tgApDungStr);
                    Date tgKetThuc = sdf.parse(tgKetThucStr);

                    boolean matchDoiTuong = dt.equalsIgnoreCase(doiTuong);
                    boolean matchThoiGian =
                            (tgApDung.before(endDate) || tgApDung.equals(endDate)) &&
                                    (tgKetThuc.after(startDate) || tgKetThuc.equals(startDate));

                    if (matchDoiTuong && matchThoiGian) {
                        Object[] row = new Object[model.getColumnCount()];
                        for (int j = 0; j < model.getColumnCount(); j++) {
                            row[j] = model.getValueAt(i, j);
                        }
                        filteredModel.addRow(row);
                    }

                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
            }

            jTable1.setModel(filteredModel);

            JOptionPane.showMessageDialog(this,
                    "Đã lọc được " + filteredModel.getRowCount() +
                            " khuyến mãi cho đối tượng: " + doiTuong +
                            " trong khoảng thời gian từ " + sdf.format(startDate) + " đến " + sdf.format(endDate));
        });

        /// --- SỰ KIỆN: Cập nhật khuyến mãi ---
        jButton5.addActionListener(e -> {
            if (!validateInput()) {
                return;
            }

            int row = jTable1.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "⚠️ Vui lòng chọn một khuyến mãi để cập nhật!");
                return;
            }

            // Lấy dữ liệu cũ từ bảng
            String maKMCu = model.getValueAt(row, 0).toString().trim();

            // Lấy dữ liệu mới từ form
            String tenMoi = jTextField2.getText().trim();
            Date ngayBD = jDateChooser1.getDate();
            Date ngayKT = jDateChooser2.getDate();
            String chietKhauStr = jTextField4.getText().trim();

            // Kiểm tra nhập liệu
            if (tenMoi.isEmpty() || ngayBD == null || ngayKT == null || chietKhauStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "⚠️ Vui lòng nhập đầy đủ thông tin!");
                return;
            }

            double chietKhau;
            try {
                chietKhau = Double.parseDouble(chietKhauStr);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "❌ Chiết khấu phải là số hợp lệ!");
                return;
            }

            // Kiểm tra chiết khấu hợp lệ (0 ≤ x ≤ 1)
            if (chietKhau < 0 || chietKhau > 1) {
                JOptionPane.showMessageDialog(this, "⚠️ Chiết khấu phải nằm trong khoảng 0 - 1 (ví dụ: 0.2 = 20%)!");
                return;
            }

            // Sinh mã khuyến mãi mới (theo ngày bắt đầu)
            String maKMMoi = KhuyenMai.taoMaKhuyenMaiTheoNgay(ngayBD, 1);

            // Gọi DAO cập nhật
            KhuyenMaiHoaDon_DAO dao = new KhuyenMaiHoaDon_DAO();
            boolean result = dao.capNhatKhuyenMaiHoaDon(
                    maKMCu,
                    maKMMoi,
                    tenMoi,
                    new java.sql.Date(ngayBD.getTime()),
                    new java.sql.Date(ngayKT.getTime()),
                    chietKhau
            );

            if (result) {
                JOptionPane.showMessageDialog(this, "✅ Cập nhật khuyến mãi thành công!");
                loadTableData();

                // Tự động chọn lại dòng mới
                for (int i = 0; i < jTable1.getRowCount(); i++) {
                    if (jTable1.getValueAt(i, 0).toString().equals(maKMMoi)) {
                        jTable1.setRowSelectionInterval(i, i);
                        break;
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "❌ Cập nhật thất bại!");
            }
        });

        // SỰ KIỆN: Xóa trắng form
        jButton2.addActionListener(e -> clearForm());

        // --- SỰ KIỆN: Tạm ngưng / Kích hoạt khuyến mãi ---
        jButton6.addActionListener(e -> {
            int row = jTable1.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn khuyến mãi cần thay đổi trạng thái!");
                return;
            }

            String maKM = model.getValueAt(row, 0).toString(); // Mã KM
            String currentStatus = model.getValueAt(row, 6).toString().trim(); // Cột trạng thái

            boolean newTrangThai; // true = Hoạt động, false = Tạm ngưng
            String message;

            if (currentStatus.equalsIgnoreCase("Hoạt động")) {
                newTrangThai = false;
                message = "Khuyến mãi đã được tạm ngưng!";
            } else {
                newTrangThai = true;
                message = "Khuyến mãi đã được kích hoạt lại!";
            }

            KhuyenMaiHoaDon_DAO dao = new KhuyenMaiHoaDon_DAO();
            boolean success = dao.tamNgungTrangThai(maKM, newTrangThai);

            if (success) {
                JOptionPane.showMessageDialog(this, message);
                loadTableData(); // ✅ làm mới dữ liệu từ SQL
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật trạng thái thất bại!");
            }
        });


        jTable1.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = jTable1.getSelectedRow();
                if (row != -1) {
                    dangCapNhat = true; // 👉 Đánh dấu là đang cập nhật

                    jTextField1.setText(model.getValueAt(row, 0).toString());
                    jTextField2.setText(model.getValueAt(row, 1).toString());
                    jComboBox1.setSelectedItem(model.getValueAt(row, 2).toString());
                    jTextField4.setText(model.getValueAt(row, 5).toString());

                    try {
                        String startStr = model.getValueAt(row, 3).toString();
                        String endStr = model.getValueAt(row, 4).toString();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        jDateChooser1.setDate(sdf.parse(startStr));
                        jDateChooser2.setDate(sdf.parse(endStr));
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Lỗi định dạng ngày: " + ex.getMessage());
                    }
                }
            }
        });

        // --- SỰ KIỆN: TÌM KIẾM TRONG BẢNG ---
        jButton1.addActionListener(e -> {
            String keyword = jTextField2.getText().trim().toLowerCase();
            if (keyword.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nhập tên khuyến mãi để tìm kiếm!");
                return;
            }

            DefaultTableModel filteredModel = new DefaultTableModel(
                    new Object[]{"Mã khuyến mãi", "Tên khuyến mãi", "Đối tượng",
                            "Thời gian áp dụng", "Thời gian kết thúc", "Chiết khấu", "Trạng thái"}, 0
            );

            for (int i = 0; i < model.getRowCount(); i++) {
                String ten = model.getValueAt(i, 1).toString().toLowerCase();
                if (ten.contains(keyword)) {
                    Object[] row = new Object[model.getColumnCount()];
                    for (int j = 0; j < model.getColumnCount(); j++) {
                        row[j] = model.getValueAt(i, j);
                    }
                    filteredModel.addRow(row);
                }
            }

            if (filteredModel.getRowCount() > 0) {
                jTable1.setModel(filteredModel);
                JOptionPane.showMessageDialog(this,
                        "Tìm thấy " + filteredModel.getRowCount() + " khuyến mãi có tên chứa: " + keyword);
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy khuyến mãi nào phù hợp!");
            }
        });

        // 🔹 Khi người dùng chọn ngày bắt đầu → tự đổi mã theo ngày
        jDateChooser1.addPropertyChangeListener("date", evt -> {
            Date ngayBatDau = jDateChooser1.getDate();
            if (ngayBatDau == null) return;

            try {
                // Nếu người dùng đang sửa (có sẵn mã)
                String maHienTai = jTextField1.getText().trim();

                // Lấy ngày hiện tại để sinh phần giữa mã
                String ngayMoi = new SimpleDateFormat("ddMMyyyy").format(ngayBatDau);

                // Nếu đang sửa và mã có dạng hợp lệ (KM + ngày + số)
                if (!maHienTai.isEmpty() && maHienTai.matches("^KM\\d{8}\\d{2}$")) {
                    // Giữ phần số thứ tự (2 ký tự cuối)
                    String soThuTu = maHienTai.substring(maHienTai.length() - 2);
                    // Ghép lại mã mới với ngày mới
                    String maMoi = "KM" + ngayMoi + soThuTu;
                    jTextField1.setText(maMoi);
                } else {
                    // Nếu đang thêm mới thì sinh mã mới hoàn toàn
                    String maTuDong = KhuyenMai.taoMaKhuyenMaiTheoNgay(ngayBatDau, demKhuyenMai++);
                    jTextField1.setText(maTuDong);
                }

                jTextField1.setEditable(false);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "❌ Lỗi sinh mã: " + ex.getMessage());
            }
        });

    }


    private void clearForm() {
        jTextField2.setText("");
        jComboBox1.setSelectedIndex(0);
        jDateChooser1.setDate(null);
        jDateChooser2.setDate(null);
        jTextField4.setText("");

        jTextField1.setText("");
        jTextField1.setEditable(false);

        loadTableData();
        jTable1.setModel(model);

    }


    @SuppressWarnings("unchecked")
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();

        // ➕ Thêm hai JDateChooser
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();

        setBackground(new java.awt.Color(234, 243, 251));

        jLabel1.setBackground(new java.awt.Color(234, 243, 251));
        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Khuyến Mãi Đối tượng");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {},
                new String [] {
                        "Mã khuyến mãi", "Tên khuyến mãi", "Đối tượng", "Thời gian áp dụng", "Thời gian kết thúc", "Chiết khấu", "Trạng thái"
                }
        ));
        jScrollPane1.setViewportView(jTable1);

        jPanel1.setBackground(new java.awt.Color(234, 243, 251));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Thông tin khuyến mãi"));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 16));
        jLabel2.setText("Mã khuyến mãi:");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 16));
        jLabel3.setText("Tên khuyến mãi:");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 16));
        jLabel4.setText("Đối tượng:");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 16));
        jLabel5.setText("Thời gian áp dụng:");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 16));
        jLabel6.setText("Thời gian kết thúc:");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 16));
        jLabel7.setText("Chiết khấu:");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(
                new String[] { " ", "Trẻ em", "Người lớn", "Người cao tuổi", "Sinh viên" }
        ));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(56, 56, 56)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel2)
                                        .addComponent(jLabel3)
                                        .addComponent(jLabel4)
                                        .addComponent(jLabel5)
                                        .addComponent(jLabel6)
                                        .addComponent(jLabel7))
                                .addGap(51, 51, 51)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                                        .addComponent(jTextField2)
                                        .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jDateChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                                        .addComponent(jDateChooser2, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                                        .addComponent(jTextField4))
                                .addGap(40, 40, 40))
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(26)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel2)
                                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(28)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel3)
                                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(29)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel4)
                                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(34)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel5)
                                        .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(32)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel6)
                                        .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(33)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel7)
                                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(36, Short.MAX_VALUE))
        );

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/TimKiem.png")));
        jButton1.setText("Tìm");

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/clear.png")));
        jButton2.setText("Xóa trắng");

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/filter.png")));
        jButton3.setText("Lọc");

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/add.png")));
        jButton4.setText("Thêm");

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/update.png")));
        jButton5.setText("Cập nhật");

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/stop.png")));
        jButton6.setText("Tạm ngưng");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 566, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addContainerGap())
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(31, 31, 31)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(84, 84, 84))))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(29)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(29)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(18)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(0, 0, Short.MAX_VALUE))
                                        .addComponent(jScrollPane1))
                                .addContainerGap())
        );
    }


    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField4;
    private JDateChooser jDateChooser1;
    private JDateChooser jDateChooser2;

    // End of variables declaration//GEN-END:variables
}