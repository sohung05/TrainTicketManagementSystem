/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package gui;
/*
 * @description
 * @author : Anh Van Duong
 * @Date : 10/25/2025
 * @version :  1.0
 */

import com.toedter.calendar.JDateChooser;
import dao.KhuyenMaiDoiTuong_DAO;
import dao.KhuyenMaiHoaDon_DAO;
import entity.KhuyenMai;

import java.awt.*;
import java.awt.event.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class Gui_KhuyenMaiHoaDon extends JPanel {
    private static int demKhuyenMai = 1;
    private boolean dangCapNhat = false;
    private DefaultTableModel model;
    private void loadTableData() {
        // Lấy model từ jTable1 và gán cho biến instance
        model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0); // Xóa dữ liệu cũ

        KhuyenMaiHoaDon_DAO dao = new KhuyenMaiHoaDon_DAO();
        List<KhuyenMai> dsKM = dao.getTatCaKhuyenMaiHoaDon();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        for (KhuyenMai km : dsKM) {
            // ✅ Kiểm tra hết hạn
            String trangThai;
            if (km.getThoiGianKetThuc() != null && km.getThoiGianKetThuc().isBefore(now)) {
                trangThai = "Hết hạn";
            } else {
                trangThai = km.isTrangThai() ? "Hoạt động" : "Tạm ngưng";
            }
            
            Object[] row = {
                    km.getMaKhuyenMai(),
                    km.getTenKhuyenMai(),
                    km.getDoiTuongApDung(), // Hiển thị điều kiện ("11-40 vé") thay vì số vé
                    km.getThoiGianBatDau() != null ? km.getThoiGianBatDau().format(formatter) : "",
                    km.getThoiGianKetThuc() != null ? km.getThoiGianKetThuc().format(formatter) : "",
                    km.getChietKhau(),
                    trangThai
            };
            model.addRow(row);
        }

        System.out.println("✅ Đã load " + dsKM.size() + " khuyến mãi hóa đơn vào bảng.");
    }
    private boolean validateInput() {
        String ma = jTextField1.getText().trim();
        String ten = jTextField2.getText().trim();
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
        double chietKhau;
        try {
            chietKhau = Double.parseDouble(chietKhauStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "❌ Chiết khấu phải là số!");
            jTextField4.requestFocus();
            return false;
        }
        if (chietKhau <= 0 || chietKhau > 100) {
            JOptionPane.showMessageDialog(this, "❌ Chiết khấu phải trong khoảng 0 - 100 (%)");
            jTextField4.requestFocus();
            return false;
        }

        return true;
    }
    public Gui_KhuyenMaiHoaDon() {
        initComponents();
        initEvent();
        initTable();
        loadTableData();
    }

    private void initTable() {
        model = (DefaultTableModel) jTable1.getModel();
    }

    private void initEvent() {
        // SỰ KIỆN: Thêm khuyến mãi hóa đơn
        jButton4.addActionListener(e -> {
            if (!validateInput()) {
                return;
            }
            try {
                // === LẤY DỮ LIỆU TỪ FORM ===
                String tenKM = jTextField2.getText().trim();
                String soVeStr = jTextField3.getText().trim();
                String chietKhauStr = jTextField4.getText().trim();
                Date ngayBatDau = jDateChooser1.getDate();
                Date ngayKetThuc = jDateChooser2.getDate();

                int soVe;
                double chietKhau;

                try {
                    soVe = Integer.parseInt(soVeStr);
                    chietKhau = Double.parseDouble(chietKhauStr);

                    if (soVe <= 0) {
                        JOptionPane.showMessageDialog(this, "⚠️ Số vé phải lớn hơn 0!");
                        return;
                    }

                    if (chietKhau <= 0 || chietKhau > 100) {
                        JOptionPane.showMessageDialog(this, "⚠️ Chiết khấu phải trong khoảng 0 - 100 (%)!");
                        return;
                    }

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "⚠️ Số vé hoặc chiết khấu phải là số hợp lệ!");
                    return;
                }

                // === SINH MÃ TỰ ĐỘNG ===
                SimpleDateFormat sdfDate = new SimpleDateFormat("ddMMyyyy");
                String datePart = sdfDate.format(ngayBatDau);
                String maKM = "KMHD" + datePart + (int) (Math.random() * 90 + 10);

                // === TẠO ĐỐI TƯỢNG KHuyến Mãi ===
                KhuyenMai km = new KhuyenMai(
                        maKM,
                        tenKM,
                        "KMHD",
                        new Timestamp(ngayBatDau.getTime()).toLocalDateTime(),
                        new Timestamp(ngayKetThuc.getTime()).toLocalDateTime(),
                        true
                );
                km.setSoVe(soVe);
                km.setChietKhau(chietKhau / 100.0); // lưu dạng 0.x, đã validate > 0

                // === GỌI DAO LƯU VÀO CSDL ===
                KhuyenMaiHoaDon_DAO dao = new KhuyenMaiHoaDon_DAO();
                boolean success = dao.themKhuyenMai(km); // gọi phương thức mới không gán hóa đơn

                if (success) {
                    // === HIỂN THỊ LÊN BẢNG ===
                    SimpleDateFormat sdfISO = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    model.addRow(new Object[]{
                            maKM,
                            tenKM,
                            soVe,
                            sdfISO.format(ngayBatDau),
                            sdfISO.format(ngayKetThuc),
                            chietKhau, // hiển thị %
                            "Hoạt động"
                    });

                    JOptionPane.showMessageDialog(this, "✅ Thêm khuyến mãi thành công!");
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Thêm khuyến mãi thất bại!");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "❌ Lỗi khi thêm khuyến mãi: " + ex.getMessage());
            }
        });

        jButton3.addActionListener(e -> {
            String keyword = jTextField2.getText().trim();
            Date startDate = jDateChooser1.getDate();
            Date endDate = jDateChooser2.getDate();

            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
            jTable1.setRowSorter(sorter);

            List<RowFilter<Object, Object>> filters = new ArrayList<>();

            // --- 1️⃣ Lọc theo tên khuyến mãi ---
            if (!keyword.isEmpty()) {
                filters.add(RowFilter.regexFilter("(?i)" + Pattern.quote(keyword), 1)); // cột 1 = tên khuyến mãi
            }

            // --- 2️⃣ Lọc theo khoảng thời gian ---
            if (startDate != null && endDate != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                filters.add(new RowFilter<>() {
                    @Override
                    public boolean include(Entry<? extends Object, ? extends Object> entry) {
                        try {
                            String ngayBDStr = entry.getStringValue(3); // cột 3 = Thời gian áp dụng
                            String ngayKTStr = entry.getStringValue(4); // cột 4 = Thời gian kết thúc

                            Date ngayBD = sdf.parse(ngayBDStr);
                            Date ngayKT = sdf.parse(ngayKTStr);

                            // Giữ các khuyến mãi có khoảng thời gian giao với khoảng lọc
                            return !(ngayKT.before(startDate) || ngayBD.after(endDate));
                        } catch (Exception ex) {
                            return false;
                        }
                    }
                });
            }

            // --- 3️⃣ Áp dụng bộ lọc ---
            if (filters.isEmpty()) {
                sorter.setRowFilter(null);
                JOptionPane.showMessageDialog(this, "Hiển thị tất cả khuyến mãi!");
            } else {
                sorter.setRowFilter(RowFilter.andFilter(filters));
                String msg = "Đã lọc theo ";
                if (!keyword.isEmpty()) msg += "tên \"" + keyword + "\" ";
                if (startDate != null && endDate != null) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    msg += "và khoảng thời gian từ " + sdf.format(startDate) + " đến " + sdf.format(endDate);
                }
                JOptionPane.showMessageDialog(this, msg.trim() + "!");
            }
        });


        jButton5.addActionListener(e -> {
            int row = jTable1.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn khuyến mãi cần cập nhật!");
                return;
            }

            try {
                String maKMCu = (String) model.getValueAt(row, 0); // mã khuyến mãi cũ
                String maKMMoi = jTextField1.getText().trim();     // mã mới
                String ten = jTextField2.getText().trim();         // tên khuyến mãi
                int soVe = Integer.parseInt(jTextField3.getText().trim()); // ✅ đúng: parse số vé
                double chietKhau = Double.parseDouble(jTextField4.getText().trim());

                java.sql.Date sqlStart = new java.sql.Date(jDateChooser1.getDate().getTime());
                java.sql.Date sqlEnd = new java.sql.Date(jDateChooser2.getDate().getTime());

                KhuyenMaiHoaDon_DAO dao = new KhuyenMaiHoaDon_DAO();
                boolean ok = dao.capNhatKhuyenMaiHoaDon(maKMCu, maKMMoi, ten, sqlStart, sqlEnd, chietKhau);

                if (ok) {
                    JOptionPane.showMessageDialog(this, "✅ Cập nhật khuyến mãi thành công!");
                    model.setValueAt(maKMMoi, row, 0);
                    model.setValueAt(ten, row, 1);
                    model.setValueAt(soVe, row, 2);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    model.setValueAt(sdf.format(sqlStart), row, 3);
                    model.setValueAt(sdf.format(sqlEnd), row, 4);
                    model.setValueAt(chietKhau, row, 5);
                } else {
                    JOptionPane.showMessageDialog(this, "⚠️ Không có dòng nào được cập nhật!");
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "⚠️ Số vé hoặc chiết khấu không hợp lệ!");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "❌ Lỗi khi cập nhật: " + ex.getMessage());
            }
        });


        // SỰ KIỆN: Xóa trắng form
        jButton2.addActionListener(e -> clearForm());

        // SỰ KIỆN: Tạm ngưng khuyến mãi (chuyển trạng thái)
        jButton6.addActionListener(e -> {
            int row = jTable1.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "⚠️ Vui lòng chọn khuyến mãi cần thay đổi trạng thái!");
                return;
            }

            String maKM = model.getValueAt(row, 0).toString(); // cột mã KM
            String currentStatus = model.getValueAt(row, 6).toString().trim();

            // ✅ Không cho phép thay đổi trạng thái nếu đã hết hạn
            if (currentStatus.equalsIgnoreCase("Hết hạn")) {
                JOptionPane.showMessageDialog(this, 
                    "❌ Không thể thay đổi trạng thái!\nKhuyến mãi này đã hết hạn.", 
                    "Thông báo", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            boolean newTrangThai; // true = Hoạt động, false = Tạm ngưng
            String message;

            if (currentStatus.equalsIgnoreCase("Hoạt động")) {
                newTrangThai = false;
                message = "✅ Khuyến mãi đã được tạm ngưng!";
            } else {
                newTrangThai = true;
                message = "✅ Khuyến mãi đã được kích hoạt lại!";
            }

            KhuyenMaiHoaDon_DAO dao = new KhuyenMaiHoaDon_DAO();
            boolean success = dao.tamNgungTrangThai(maKM, newTrangThai);

            if (success) {
                JOptionPane.showMessageDialog(this, message);
                loadTableData(); // ✅ làm mới dữ liệu từ SQL
            } else {
                JOptionPane.showMessageDialog(this, "❌ Cập nhật trạng thái thất bại!");
            }
        });


        // SỰ KIỆN: Click vào bảng → load dữ liệu lên form
        jTable1.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = jTable1.getSelectedRow();
                if (row != -1) {
                    jTextField1.setText(model.getValueAt(row, 0).toString());
                    jTextField2.setText(model.getValueAt(row, 1).toString());
                    jTextField3.setText(model.getValueAt(row, 2).toString());
                    
                    // ✅ Chiết khấu: nhân 100 để hiển thị (DB lưu 0.09, hiển thị 9)
                    double chietKhau = Double.parseDouble(model.getValueAt(row, 5).toString());
                    jTextField4.setText(String.format("%.0f", chietKhau * 100));

                    try {
                        String startStr = model.getValueAt(row, 3).toString();
                        String endStr = model.getValueAt(row, 4).toString();

                        // Format từ database: "yyyy-MM-dd HH:mm:ss"
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        jDateChooser1.setDate(sdf.parse(startStr));
                        jDateChooser2.setDate(sdf.parse(endStr));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Lỗi định dạng ngày: " + ex.getMessage());
                    }
                }
            }
        });


        // --- SỰ KIỆN: TÌM KIẾM DANH SÁCH PHÙ HỢP ---
        jButton1.addActionListener(e -> {
            String keyword = jTextField2.getText().trim().toLowerCase();
            if (keyword.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập tên khuyến mãi để tìm kiếm!");
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
                        "Đã tìm thấy " + filteredModel.getRowCount() +
                                " khuyến mãi có tên chứa: " + keyword);
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy khuyến mãi nào phù hợp!");
            }
        });


        // 🔹 Khi người dùng chọn ngày bắt đầu → tự sinh mã
        jDateChooser1.addPropertyChangeListener("date", evt -> {
            Date ngayBatDau = jDateChooser1.getDate();
            if (ngayBatDau != null) {
                try {
                    String maTuDong = KhuyenMai.taoMaKhuyenMaiTheoNgay(ngayBatDau, demKhuyenMai++);
                    jTextField1.setText(maTuDong);
                    jTextField1.setEditable(false);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "❌ Lỗi sinh mã: " + ex.getMessage());
                }
            }
        });
    }
    private void resetTable() {
        try {
            model.setRowCount(0); // xóa dữ liệu cũ
            KhuyenMaiHoaDon_DAO dao = new KhuyenMaiHoaDon_DAO();
            List<KhuyenMai> dsKM = dao.getTatCaKhuyenMaiHoaDon();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            
            for (KhuyenMai km : dsKM) {
                // ✅ Kiểm tra hết hạn
                String trangThai;
                if (km.getThoiGianKetThuc() != null && km.getThoiGianKetThuc().isBefore(now)) {
                    trangThai = "Hết hạn";
                } else {
                    trangThai = km.isTrangThai() ? "Hoạt động" : "Tạm ngưng";
                }
                
                Object[] row = {
                        km.getMaKhuyenMai(),
                        km.getTenKhuyenMai(),
                        km.getDoiTuongApDung(), // Hiển thị điều kiện ("11-40 vé") thay vì số vé
                        km.getThoiGianBatDau().format(formatter),
                        km.getThoiGianKetThuc().format(formatter),
                        km.getChietKhau(),
                        trangThai
                };
                model.addRow(row);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi reset bảng!");
        }
    }

    // Hàm xóa trắng form
    private void clearForm() {
        jTextField1.setText("");
        jTextField2.setText("");
        jTextField3.setText("");
        jTextField4.setText("");
        jDateChooser1.setDate(null);
        jDateChooser2.setDate(null);
        jTable1.clearSelection();

        loadTableData();  // ✅ Load lại data từ DB vào model (instance variable)
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new JLabel();
        jPanel1 = new JPanel();
        jLabel2 = new JLabel();
        jLabel3 = new JLabel();
        jLabel4 = new JLabel();
        jLabel5 = new JLabel();
        jLabel6 = new JLabel();
        jLabel7 = new JLabel();
        jTextField1 = new JTextField();
        jTextField2 = new JTextField();
        jTextField3 = new JTextField();
        jTextField4 = new JTextField();
        jDateChooser1 = new JDateChooser();
        jDateChooser2 = new JDateChooser();
        jButton1 = new JButton();
        jButton2 = new JButton();
        jButton3 = new JButton();
        jButton4 = new JButton();
        jButton5 = new JButton();
        jButton6 = new JButton();
        jScrollPane1 = new JScrollPane();
        jTable1 = new JTable();

        setBackground(new Color(234, 243, 251));
        setRequestFocusEnabled(false);
        setVerifyInputWhenFocusTarget(false);

        jLabel1.setFont(new Font("Segoe UI", 1, 20)); // NOI18N
        jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel1.setText("Khuyến Mãi Hóa Đơn");

        jPanel1.setBackground(new Color(234, 243, 251));
        jPanel1.setBorder(BorderFactory.createTitledBorder("Thông tin khuyến mãi"));

        jLabel2.setFont(new Font("Segoe UI", 0, 16)); // NOI18N
        jLabel2.setText("Mã khuyến mãi:");

        jLabel3.setFont(new Font("Segoe UI", 0, 16)); // NOI18N
        jLabel3.setText("Tên khuyến mãi:");

        jLabel4.setFont(new Font("Segoe UI", 0, 16)); // NOI18N
        jLabel4.setText("Số lượng vé:");

        jLabel5.setFont(new Font("Segoe UI", 0, 16)); // NOI18N
        jLabel5.setText("Thời gian áp dụng:");

        jLabel6.setFont(new Font("Segoe UI", 0, 16)); // NOI18N
        jLabel6.setText("Thời gian kết thúc:");

        jLabel7.setFont(new Font("Segoe UI", 0, 16)); // NOI18N
        jLabel7.setText("Chiết khấu:");

        GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(56, 56, 56)
                                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel2, GroupLayout.PREFERRED_SIZE, 112, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel6)
                                        .addComponent(jLabel7, GroupLayout.PREFERRED_SIZE, 98, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel4, GroupLayout.PREFERRED_SIZE, 99, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel3, GroupLayout.PREFERRED_SIZE, 118, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel5))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 51, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addComponent(jTextField1, GroupLayout.PREFERRED_SIZE, 170, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jTextField3, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 170, GroupLayout.PREFERRED_SIZE))
                                                .addGroup(GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(jDateChooser1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jDateChooser2, GroupLayout.PREFERRED_SIZE, 170, GroupLayout.PREFERRED_SIZE))
                                                .addComponent(jTextField4, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 170, GroupLayout.PREFERRED_SIZE))
                                        .addComponent(jTextField2, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 170, GroupLayout.PREFERRED_SIZE))
                                .addGap(40, 40, 40))
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel2)
                                        .addComponent(jTextField1, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
                                .addGap(28, 28, 28)
                                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel3)
                                        .addComponent(jTextField2, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
                                .addGap(27, 27, 27)
                                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel4)
                                        .addComponent(jTextField3, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
                                .addGap(20, 20, 20)
                                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel5)
                                        .addComponent(jDateChooser1, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
                                .addGap(32, 32, 32)
                                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel6)
                                        .addComponent(jDateChooser2, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
                                .addGap(25, 25, 25)
                                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel7)
                                        .addComponent(jTextField4, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(36, Short.MAX_VALUE))
        );

        jButton1.setIcon(new ImageIcon(getClass().getResource("/icon/TimKiem.png"))); // NOI18N
        jButton1.setText("Tìm");
        jButton1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setIcon(new ImageIcon(getClass().getResource("/icon/clear.png"))); // NOI18N
        jButton2.setText("Xóa trắng");

        jButton3.setIcon(new ImageIcon(getClass().getResource("/icon/filter.png"))); // NOI18N
        jButton3.setText("Lọc");

        jButton4.setIcon(new ImageIcon(getClass().getResource("/icon/add.png"))); // NOI18N
        jButton4.setText("Thêm");

        jButton5.setIcon(new ImageIcon(getClass().getResource("/icon/update.png"))); // NOI18N
        jButton5.setText("Cập nhật");

        jButton6.setIcon(new ImageIcon(getClass().getResource("/icon/stop.png"))); // NOI18N
        jButton6.setText("Tạm ngưng");

        jTable1.setModel(new DefaultTableModel(
                new Object [][] {

                },
                new String [] {
                        "Mã khuyến mãi", "Tên khuyến mãi", "Số vé khả dụng", "Thời gian áp dụng", "Thời gian kết thúc ", "Chiết khấu", "Trạng thái"
                }
        ));
        jScrollPane1.setViewportView(jTable1);

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 603, Short.MAX_VALUE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addContainerGap())
                                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(jButton1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jButton3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jButton5, GroupLayout.PREFERRED_SIZE, 126, GroupLayout.PREFERRED_SIZE))
                                                .addGap(31, 31, 31)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(jButton2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jButton6, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jButton4, GroupLayout.PREFERRED_SIZE, 127, GroupLayout.PREFERRED_SIZE))
                                                .addGap(84, 84, 84))))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel1, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addGap(29, 29, 29)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jButton1, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jButton2, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE))
                                                .addGap(29, 29, 29)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jButton3, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jButton4, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE))
                                                .addGap(18, 18, 18)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jButton5, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jButton6, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE))
                                                .addGap(0, 91, Short.MAX_VALUE))
                                        .addComponent(jScrollPane1))
                                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JButton jButton1;
    private JButton jButton2;
    private JButton jButton3;
    private JButton jButton4;
    private JButton jButton5;
    private JButton jButton6;
    private JDateChooser jDateChooser1;
    private JDateChooser jDateChooser2;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JLabel jLabel5;
    private JLabel jLabel6;
    private JLabel jLabel7;
    private JPanel jPanel1;
    private JScrollPane jScrollPane1;
    private JTable jTable1;
    private JTextField jTextField1;
    private JTextField jTextField2;
    private JTextField jTextField3;
    private JTextField jTextField4;
    // End of variables declaration//GEN-END:variables
}