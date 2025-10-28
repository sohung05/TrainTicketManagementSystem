/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package gui;

import dao.KhachHang_DAO;
import entity.KhachHang;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.Objects;
import utils.JTableExporter;

/**
 *
 * @author PC
 */
public class Gui_KhachHang extends javax.swing.JPanel {

    private DefaultTableModel modelKhachHang;
    private KhachHang_DAO khachHangDAO = new KhachHang_DAO();
    private String maKHChon = null;

    /**
     * Creates new form Gui_KhachHang
     */
    public Gui_KhachHang() {
        initComponents();
        modelKhachHang = (DefaultTableModel) tblKhachHang.getModel();
        loadData();
        addTableSelectionListener();
    }

    // Sự kiện chọn dòng trong bảng để hiển thị dữ liệu lên form
    private void addTableSelectionListener() {
        tblKhachHang.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = tblKhachHang.getSelectedRow();
                if (row != -1) {
                    int modelRow = tblKhachHang.convertRowIndexToModel(row);
                    maKHChon = modelKhachHang.getValueAt(modelRow, 0).toString();

                    txtCCCD.setText(modelKhachHang.getValueAt(modelRow, 4).toString());

                    txtHoTen.setText(modelKhachHang.getValueAt(modelRow, 1).toString());

                    txtEmail.setText(modelKhachHang.getValueAt(modelRow, 2).toString());

                    txtSoDienThoai.setText(modelKhachHang.getValueAt(modelRow, 3).toString());

                    cmbDoiTuong.setSelectedItem(modelKhachHang.getValueAt(modelRow, 5).toString());
                }
            }
        });
    }
    private void loadData() {
        modelKhachHang.setRowCount(0);
        List<KhachHang> ds = khachHangDAO.getAll();
        for (KhachHang kh : ds) {
            modelKhachHang.addRow(new Object[]{
                    kh.getMaKH(),
                    kh.getHoTen(),
                    kh.getEmail(),
                    kh.getSDT(),
                    kh.getCCCD(),
                    kh.getDoiTuong()
            });
        }
    }


    private void clearForm() {
        txtCCCD.setText("");
        txtHoTen.setText("");
        txtEmail.setText("");
        txtSoDienThoai.setText("");
        cmbDoiTuong.setSelectedIndex(0);
        tblKhachHang.clearSelection();
    }

    private boolean isValidCCCD(String cccd) {
        return cccd.matches("\\d{9,13}");
    }

    private boolean isValidHoTen(String hoTen) {
        return hoTen.matches("([A-Z][a-z]*\\s?)+");
    }

    private boolean isValidEmail(String email) {
        return email.length() <= 30 && email.matches("[A-Za-z0-9]+@gmail\\.com");
    }

    private boolean isValidSDT(String sdt) {
        return sdt.matches("\\d{10}");
    }

    private String generateMaKH(String cccd) {
        // Lấy 4 số cuối CCCD
        String base = "KH" + cccd.substring(Math.max(0, cccd.length() - 4));
        String ma = base;
        int i = 1;
        while (khachHangDAO.exists(ma)) {
            ma = base + "_" + i;
            i++;
        }
        return ma;
    }


    private KhachHang getKhachHangFromForm() {
        String cccd = txtCCCD.getText().trim();
        String hoTen = txtHoTen.getText().trim();
        String email = txtEmail.getText().trim();
        String soDienThoai = txtSoDienThoai.getText().trim();
        String doiTuong = Objects.requireNonNull(cmbDoiTuong.getSelectedItem()).toString();

        // Validate CCCD
        if (!isValidCCCD(cccd)) {
            JOptionPane.showMessageDialog(this, "CCCD phải là 9-13 số và không được trống!");
            return null;
        }

        // Validate Họ tên
        if (!isValidHoTen(hoTen)) {
            JOptionPane.showMessageDialog(this, "Họ tên không được trống và mỗi chữ cái đầu phải viết hoa!");
            return null;
        }

        // Validate Email
        if (!email.isEmpty() && !isValidEmail(email)) {
            JOptionPane.showMessageDialog(this, "Email không quá 30 ký tự và phải kết thúc bằng @gmail.com!");
            return null;
        }

        // Validate SDT
        if (!isValidSDT(soDienThoai)) {
            JOptionPane.showMessageDialog(this, "Số điện thoại phải đủ 10 số và không được trống!");
            return null;
        }

        // Validate Đối tượng
        if (cmbDoiTuong.getSelectedIndex() <= 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn đối tượng khách hàng!");
            return null;
        }

        String maKH;
        if (maKHChon != null) {
            maKH = maKHChon;
        } else {
            maKH = generateMaKH(cccd);
        }

        return new KhachHang(maKH, cccd, hoTen, email, soDienThoai, doiTuong);
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        lblTitle = new javax.swing.JLabel();
        scrollKhachHang = new javax.swing.JPanel();
        lblCCCD = new javax.swing.JLabel();
        lblHoTen = new javax.swing.JLabel();
        lblEmail = new javax.swing.JLabel();
        lblSoDienThoai = new javax.swing.JLabel();
        lblDoiTuong = new javax.swing.JLabel();
        txtCCCD = new javax.swing.JTextField();
        txtHoTen = new javax.swing.JTextField();
        txtEmail = new javax.swing.JTextField();
        txtSoDienThoai = new javax.swing.JTextField();
        cmbDoiTuong = new javax.swing.JComboBox<>();
        pnlThongTin = new javax.swing.JScrollPane();
        tblKhachHang = new javax.swing.JTable();
        btnCapNhat = new javax.swing.JButton();
        btnTimKiem = new javax.swing.JButton();
        btnXoaTrang = new javax.swing.JButton();
        btnXuatExcel = new javax.swing.JButton();

        jLabel2.setText("jLabel2");

        setBackground(new java.awt.Color(234, 243, 251));

        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle.setText("Quản Lý Khách Hàng");

        scrollKhachHang.setBackground(new java.awt.Color(234, 243, 251));
        scrollKhachHang.setBorder(javax.swing.BorderFactory.createTitledBorder("Thông tin khách hàng"));

        lblCCCD.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lblCCCD.setText("CCCD:");

        lblHoTen.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lblHoTen.setText("Họ và tên:");

        lblEmail.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lblEmail.setText("Email:");

        lblSoDienThoai.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lblSoDienThoai.setText("Số điện thoại:");

        lblDoiTuong.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lblDoiTuong.setText("Đối tượng:");

        cmbDoiTuong.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "  ", "Trẻ em", "Người lớn", "Sinh viên", "Người cao tuổi" }));

        javax.swing.GroupLayout scrollKhachHangLayout = new javax.swing.GroupLayout(scrollKhachHang);
        scrollKhachHang.setLayout(scrollKhachHangLayout);
        scrollKhachHangLayout.setHorizontalGroup(
                scrollKhachHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(scrollKhachHangLayout.createSequentialGroup()
                                .addGap(55, 55, 55)
                                .addComponent(lblHoTen, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                        .addGroup(scrollKhachHangLayout.createSequentialGroup()
                                .addGroup(scrollKhachHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(scrollKhachHangLayout.createSequentialGroup()
                                                .addGap(55, 55, 55)
                                                .addGroup(scrollKhachHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(lblEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(lblCCCD)
                                                        .addComponent(lblDoiTuong)))
                                        .addGroup(scrollKhachHangLayout.createSequentialGroup()
                                                .addGap(53, 53, 53)
                                                .addComponent(lblSoDienThoai)))
                                .addGap(56, 56, 56)
                                .addGroup(scrollKhachHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(scrollKhachHangLayout.createSequentialGroup()
                                                .addGroup(scrollKhachHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(txtHoTen, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(txtCCCD, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(txtSoDienThoai, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(33, 39, Short.MAX_VALUE))
                                        .addGroup(scrollKhachHangLayout.createSequentialGroup()
                                                .addComponent(cmbDoiTuong, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(0, 0, Short.MAX_VALUE))))
        );
        scrollKhachHangLayout.setVerticalGroup(
                scrollKhachHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(scrollKhachHangLayout.createSequentialGroup()
                                .addGap(22, 22, 22)
                                .addGroup(scrollKhachHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblCCCD)
                                        .addComponent(txtCCCD, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(scrollKhachHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtHoTen, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lblHoTen))
                                .addGap(22, 22, 22)
                                .addGroup(scrollKhachHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblEmail)
                                        .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(scrollKhachHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblSoDienThoai)
                                        .addComponent(txtSoDienThoai, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(27, 27, 27)
                                .addGroup(scrollKhachHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblDoiTuong)
                                        .addComponent(cmbDoiTuong, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(26, Short.MAX_VALUE))
        );

        tblKhachHang.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {

                },
                new String [] {
                        "Mã khách hàng", "Họ tên", "Email", "Số điện thoại", "CCCD", "Đối tượng"
                }
        ));
        pnlThongTin.setViewportView(tblKhachHang);

        btnCapNhat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/update.png"))); // NOI18N
        btnCapNhat.setText("Cập nhật");
        btnCapNhat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCapNhatActionPerformed(evt);
            }
        });

        btnTimKiem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/TimKiem.png"))); // NOI18N
        btnTimKiem.setText("Tìm kiếm");
        btnTimKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimKiemActionPerformed(evt);
            }
        });

        btnXoaTrang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/clear.png"))); // NOI18N
        btnXoaTrang.setText("Xóa trắng");
        btnXoaTrang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaTrangActionPerformed(evt);
            }
        });

        btnXuatExcel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/excel.png"))); // NOI18N
        btnXuatExcel.setText("Xuất Excel");
        btnXuatExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXuatExcelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(lblTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(pnlThongTin, javax.swing.GroupLayout.DEFAULT_SIZE, 700, Short.MAX_VALUE)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(scrollKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGap(74, 74, 74)
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(btnTimKiem)
                                                                        .addComponent(btnCapNhat))
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                                        .addComponent(btnXoaTrang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                        .addComponent(btnXuatExcel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                                .addGap(48, 48, 48)))))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(lblTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(scrollKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(btnTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(btnXoaTrang, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(26, 26, 26)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(btnCapNhat, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(btnXuatExcel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(0, 138, Short.MAX_VALUE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(pnlThongTin)
                                                .addContainerGap())))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnXoaTrangActionPerformed(java.awt.event.ActionEvent evt) {
        clearForm();
        loadData();
    }

    private void btnTimKiemActionPerformed(java.awt.event.ActionEvent evt) {
        String cccd = txtCCCD.getText().trim();
        String hoTen = txtHoTen.getText().trim();
        String email = txtEmail.getText().trim();
        String sdt = txtSoDienThoai.getText().trim();
        String doiTuong = (String) cmbDoiTuong.getSelectedItem();

        if (doiTuong != null && doiTuong.trim().isEmpty()) {
            doiTuong = null; // bỏ qua nếu người dùng để trống
        }

        // Gọi DAO để tìm kiếm
        List<KhachHang> ds = khachHangDAO.timKiem(cccd, hoTen, email, sdt, doiTuong);

        // Cập nhật lại bảng
        DefaultTableModel model = (DefaultTableModel) tblKhachHang.getModel();
        model.setRowCount(0); // xóa dữ liệu cũ

        for (KhachHang kh : ds) {
            model.addRow(new Object[]{
                    kh.getMaKH(),
                    kh.getHoTen(),
                    kh.getEmail(),
                    kh.getSDT(),
                    kh.getCCCD(),
                    kh.getDoiTuong()
            });
        }

        // Nếu không có kết quả
        if (ds.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy khách hàng phù hợp!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void btnCapNhatActionPerformed(java.awt.event.ActionEvent evt) {
        KhachHang kh = getKhachHangFromForm();
        if (kh != null) {
            if (khachHangDAO.sua(kh)) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy khách hàng để cập nhật!");
            }
        }
    }

    private void btnXuatExcelActionPerformed(java.awt.event.ActionEvent evt) {
        JTableExporter.exportJTableToExcel(tblKhachHang);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCapNhat;
    private javax.swing.JButton btnTimKiem;
    private javax.swing.JButton btnXoaTrang;
    private javax.swing.JButton btnXuatExcel;
    private javax.swing.JComboBox<String> cmbDoiTuong;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel lblCCCD;
    private javax.swing.JLabel lblDoiTuong;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblHoTen;
    private javax.swing.JLabel lblSoDienThoai;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JScrollPane pnlThongTin;
    private javax.swing.JPanel scrollKhachHang;
    private javax.swing.JTable tblKhachHang;
    private javax.swing.JTextField txtCCCD;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtHoTen;
    private javax.swing.JTextField txtSoDienThoai;
    // End of variables declaration//GEN-END:variables
}