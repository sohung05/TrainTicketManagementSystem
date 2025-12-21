/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package gui;
import dao.TaiKhoan_DAO;
import entity.NhanVien;
import gui.menu.form.MainForm;

import javax.swing.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 *
 * @author PC
 */
public class Gui_TaiKhoan extends javax.swing.JPanel {

    private TaiKhoan_DAO taiKhoanDAO = new TaiKhoan_DAO();
    private dao.NhanVien_DAO nhanVienDAO = new dao.NhanVien_DAO();
    private javax.swing.table.DefaultTableModel modelTaiKhoan;
    private String maNVchon = null;
    private boolean hienMatKhau = false;
    private String matKhauGoc = "";


    /**
     * Creates new form Gui_TaiKhoan
     */
    public Gui_TaiKhoan() {
        initComponents();
        modelTaiKhoan = (javax.swing.table.DefaultTableModel) tblTaiKhoan.getModel();
        loadData();
        addTableSelectionListener();
        txtMaNV.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                loadNhanVienInfo(txtMaNV.getText().trim());
            }
        });
        loadNhanVienInfo(txtMaNV.getText().trim());
        chkHienThiMatKhau.setSelected(true);
    }

    public Gui_TaiKhoan(String maNV) {
        this();

        txtMaNV.setText(maNV);

        loadNhanVienInfo(maNV);

        timKiemVaChonDong(maNV);
    }

    private void loadData() {
        modelTaiKhoan.setRowCount(0);
        List<Object[]> ds = taiKhoanDAO.getAll();
        for (Object[] row : ds) {
            modelTaiKhoan.addRow(row);
        }
    }

    private void clearForm() {
        txtMaNV.setText("");
        txtTenNV.setText("");
        txtTenTaiKhoan.setText("");
        txtMatKhau.setText("");
        chkHienThiMatKhau.setSelected(false);
        tblTaiKhoan.clearSelection();
        maNVchon = null;

        loadNhanVienInfo(txtMaNV.getText().trim());
    }

    private entity.TaiKhoan getTaiKhoanFromForm() {
        String maNV = txtMaNV.getText().trim();
        String tenNV = txtTenNV.getText().trim();
        String tenTK = txtTenTaiKhoan.getText().trim();
        String matKhau = txtMatKhau.getText();

        if (tenTK.isEmpty() || matKhau.isEmpty() || maNV.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập Mã NV, Tên tài khoản và Mật khẩu.");
            return null;
        }

        entity.TaiKhoan tk = new entity.TaiKhoan();
        tk.setMaNhanVien(maNV);
        tk.setTenTaiKhoan(tenTK);
        tk.setMatKhau(matKhau);
        // nếu entity có ngayTao: tk.setNgayTao(LocalDate.now());
        return tk;
    }

    private void addTableSelectionListener() {
        tblTaiKhoan.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = tblTaiKhoan.getSelectedRow();
                if (row != -1) {
                    int modelRow = tblTaiKhoan.convertRowIndexToModel(row);
                    maNVchon = String.valueOf(modelTaiKhoan.getValueAt(modelRow, 0));
                    txtMaNV.setText(String.valueOf(modelTaiKhoan.getValueAt(modelRow, 0)));
                    txtTenNV.setText(String.valueOf(modelTaiKhoan.getValueAt(modelRow, 1)));
                    txtTenTaiKhoan.setText(String.valueOf(modelTaiKhoan.getValueAt(modelRow, 2)));
                    txtMatKhau.setText(String.valueOf(modelTaiKhoan.getValueAt(modelRow, 3)));

                    matKhauGoc = txtMatKhau.getText();

                    loadNhanVienInfo(txtMaNV.getText().trim());
                }
            }
        });
    }


    private void loadNhanVienInfo(String maNV) {
        txtThongTinNhanVien.setText(""); // reset
        txtTenNV.setText("");
        if (maNV == null || maNV.trim().isEmpty()) {
            txtThongTinNhanVien.setText("Vui lòng chọn nhân viên");
            return;
        }
        NhanVien nv = nhanVienDAO.getById(maNV.trim());
        if (nv == null) {
            txtThongTinNhanVien.setText("Không có nhân viên");
        } else {
            // set tạm tên lên ô tên
            txtTenNV.setText(nv.getHoTen() != null ? nv.getHoTen() : "");

            // format ngày (nếu có)
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String ngaySinh = nv.getNgaySinh() != null ? nv.getNgaySinh().format(fmt) : "N/A";
            String ngayVaoLam = nv.getNgayVaoLam() != null ? nv.getNgayVaoLam().format(fmt) : "N/A";
            String trangThai = nv.isTrangThai() ? "Đang làm" : "Nghỉ làm";

            StringBuilder sb = new StringBuilder();
            sb.append("Mã NV: ").append(nv.getMaNhanVien()).append("\n");
            sb.append("Họ và tên: ").append(nv.getHoTen() != null ? nv.getHoTen() : "").append("\n");
            sb.append("CCCD: ").append(nv.getCCCD() != null ? nv.getCCCD() : "").append("\n");
            sb.append("Email: ").append(nv.getEmail() != null ? nv.getEmail() : "").append("\n");
            sb.append("SĐT: ").append(nv.getSDT() != null ? nv.getSDT() : "").append("\n");
            sb.append("Địa chỉ: ").append(nv.getDiaChi() != null ? nv.getDiaChi() : "").append("\n");
            sb.append("Giới tính: ").append(nv.getGioiTinh() != null ? nv.getGioiTinh() : "").append("\n");
            sb.append("Ngày sinh: ").append(ngaySinh).append("\n");
            sb.append("Ngày vào làm: ").append(ngayVaoLam).append("\n");
            sb.append("Trạng thái: ").append(trangThai).append("\n");

            txtThongTinNhanVien.setText(sb.toString());
        }
    }

    private void timKiemVaChonDong(String maNV) {
        List<Object[]> ds = taiKhoanDAO.timKiem(maNV, null, null);

        if (!ds.isEmpty()) {
            Object[] taiKhoanDaCo = ds.get(0);
            String tenTaiKhoanDaCo = String.valueOf(taiKhoanDaCo[2]);

            txtMaNV.setText(String.valueOf(taiKhoanDaCo[0]));
            txtTenNV.setText(String.valueOf(taiKhoanDaCo[1]));
            txtTenTaiKhoan.setText(tenTaiKhoanDaCo);
            txtMatKhau.setText(String.valueOf(taiKhoanDaCo[3]));

            matKhauGoc = txtMatKhau.getText();
            loadNhanVienInfo(maNV);

            for (int i = 0; i < modelTaiKhoan.getRowCount(); i++) {
                if (tenTaiKhoanDaCo.equals(modelTaiKhoan.getValueAt(i, 2))) { // So sánh tên tài khoản
                    tblTaiKhoan.setRowSelectionInterval(i, i);
                    tblTaiKhoan.scrollRectToVisible(tblTaiKhoan.getCellRect(i, 0, true)); // Cuộn đến dòng đó
                    break;
                }
            }

            JOptionPane.showMessageDialog(this, "Nhân viên này đã có tài khoản. Dữ liệu đã được tải lên form.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);

        } else {

            loadNhanVienInfo(maNV);

            txtTenTaiKhoan.setText("");
            txtMatKhau.setText("");

            tblTaiKhoan.clearSelection();
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

        tblNhanVien = new javax.swing.JButton();
        btnTaiKhoan = new javax.swing.JButton();
        pnlThongTinTaiKhoan = new javax.swing.JPanel();
        lblMaNV = new javax.swing.JLabel();
        lblTenNV = new javax.swing.JLabel();
        lblTenTaiKhoan = new javax.swing.JLabel();
        lblMatKhau = new javax.swing.JLabel();
        txtMaNV = new javax.swing.JTextField();
        txtTenNV = new javax.swing.JTextField();
        txtTenTaiKhoan = new javax.swing.JTextField();
        txtMatKhau = new javax.swing.JTextField();
        chkHienThiMatKhau = new javax.swing.JCheckBox();
        lblTitle = new javax.swing.JLabel();
        scrollTaiKhoan = new javax.swing.JScrollPane();
        tblTaiKhoan = new javax.swing.JTable();
        btnTim = new javax.swing.JButton();
        btnXoaTrang = new javax.swing.JButton();
        btnThem = new javax.swing.JButton();
        btnCapNhat = new javax.swing.JButton();
        pnlThongTinNhanVien = new javax.swing.JPanel();
        scrollThongTinNhanVien = new javax.swing.JScrollPane();
        txtThongTinNhanVien = new javax.swing.JTextArea();

        setBackground(new java.awt.Color(234, 243, 251));

        tblNhanVien.setBackground(new java.awt.Color(102, 204, 255));
        tblNhanVien.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/accountant.png"))); // NOI18N
        tblNhanVien.setText("Nhân Viên");
        tblNhanVien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tblNhanVienActionPerformed(evt);
            }
        });

        btnTaiKhoan.setBackground(new java.awt.Color(102, 204, 255));
        btnTaiKhoan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/profile.png"))); // NOI18N
        btnTaiKhoan.setText("Tài Khoản");

        pnlThongTinTaiKhoan.setBackground(new java.awt.Color(234, 243, 251));
        pnlThongTinTaiKhoan.setBorder(javax.swing.BorderFactory.createTitledBorder("Thông tin tài khoản"));

        lblMaNV.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lblMaNV.setText("Mã nhân viên:");

        lblTenNV.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lblTenNV.setText("Tên nhân viên:");

        lblTenTaiKhoan.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lblTenTaiKhoan.setText("Tên tài khoản:");

        lblMatKhau.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lblMatKhau.setText("Mật khẩu:");

        chkHienThiMatKhau.setText("Hiển thị mật khẩu");
        chkHienThiMatKhau.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkHienThiMatKhauActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlThongTinTaiKhoanLayout = new javax.swing.GroupLayout(pnlThongTinTaiKhoan);
        pnlThongTinTaiKhoan.setLayout(pnlThongTinTaiKhoanLayout);
        pnlThongTinTaiKhoanLayout.setHorizontalGroup(
                pnlThongTinTaiKhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlThongTinTaiKhoanLayout.createSequentialGroup()
                                .addGroup(pnlThongTinTaiKhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(pnlThongTinTaiKhoanLayout.createSequentialGroup()
                                                .addGap(0, 0, Short.MAX_VALUE)
                                                .addComponent(chkHienThiMatKhau))
                                        .addGroup(pnlThongTinTaiKhoanLayout.createSequentialGroup()
                                                .addGap(20, 20, 20)
                                                .addGroup(pnlThongTinTaiKhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(lblTenNV)
                                                        .addComponent(lblMaNV, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(lblTenTaiKhoan, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(lblMatKhau, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                                                .addGroup(pnlThongTinTaiKhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(txtMatKhau, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(txtTenTaiKhoan, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(txtTenNV, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(txtMaNV, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(30, 30, 30))
        );
        pnlThongTinTaiKhoanLayout.setVerticalGroup(
                pnlThongTinTaiKhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlThongTinTaiKhoanLayout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(pnlThongTinTaiKhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblMaNV)
                                        .addComponent(txtMaNV, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(pnlThongTinTaiKhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblTenNV)
                                        .addComponent(txtTenNV, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(pnlThongTinTaiKhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblTenTaiKhoan)
                                        .addComponent(txtTenTaiKhoan, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(pnlThongTinTaiKhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtMatKhau, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lblMatKhau))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(chkHienThiMatKhau))
        );

        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle.setText("Quản Lý Tài Khoản");

        tblTaiKhoan.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {

                },
                new String [] {
                        "Mã nhân viên", "Tên nhân viên", "Tên tài khoản", "Mật khẩu"
                }
        ));
        scrollTaiKhoan.setViewportView(tblTaiKhoan);

        btnTim.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/TimKiem.png"))); // NOI18N
        btnTim.setText("Tìm");
        btnTim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimActionPerformed(evt);
            }
        });

        btnXoaTrang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/clear.png"))); // NOI18N
        btnXoaTrang.setText("Xóa trắng");
        btnXoaTrang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaTrangActionPerformed(evt);
            }
        });

        btnThem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/add.png"))); // NOI18N
        btnThem.setText("Thêm");
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });

        btnCapNhat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/update.png"))); // NOI18N
        btnCapNhat.setText("Cập nhật");
        btnCapNhat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCapNhatActionPerformed(evt);
            }
        });

        pnlThongTinNhanVien.setBackground(new java.awt.Color(234, 243, 251));
        pnlThongTinNhanVien.setBorder(javax.swing.BorderFactory.createTitledBorder("Thông tin nhân viên"));

        txtThongTinNhanVien.setColumns(20);
        txtThongTinNhanVien.setRows(5);
        scrollThongTinNhanVien.setViewportView(txtThongTinNhanVien);

        javax.swing.GroupLayout pnlThongTinNhanVienLayout = new javax.swing.GroupLayout(pnlThongTinNhanVien);
        pnlThongTinNhanVien.setLayout(pnlThongTinNhanVienLayout);
        pnlThongTinNhanVienLayout.setHorizontalGroup(
                pnlThongTinNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlThongTinNhanVienLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(scrollThongTinNhanVien)
                                .addContainerGap())
        );
        pnlThongTinNhanVienLayout.setVerticalGroup(
                pnlThongTinNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(scrollThongTinNhanVien)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(14, 14, 14)
                                .addComponent(tblNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnTaiKhoan)
                                .addGap(0, 0, Short.MAX_VALUE))
                        .addComponent(lblTitle, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(scrollTaiKhoan, javax.swing.GroupLayout.DEFAULT_SIZE, 726, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addComponent(btnThem, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(btnTim, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(40, 40, 40)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addComponent(btnCapNhat, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(btnXoaTrang))
                                                .addGap(51, 51, 51))
                                        .addComponent(pnlThongTinTaiKhoan, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(pnlThongTinNhanVien, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addContainerGap())))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(lblTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(tblNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnTaiKhoan, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(pnlThongTinTaiKhoan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(btnTim, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(btnXoaTrang, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(18, 18, 18)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(btnThem, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(btnCapNhat, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(pnlThongTinNhanVien, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addComponent(scrollTaiKhoan, javax.swing.GroupLayout.PREFERRED_SIZE, 629, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(22, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tblNhanVienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tblNhanVienActionPerformed
        // TODO add your handling code here
        MainForm main = (MainForm) SwingUtilities.getAncestorOfClass(MainForm.class, this);
        if (main != null) {
            main.showForm(new Gui_NhanVien());
        }
    }

    private void btnTimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimActionPerformed
        String maNV = txtMaNV.getText().trim();
        String tenTK = txtTenTaiKhoan.getText().trim();
        String tenNV = txtTenNV.getText().trim();

        List<Object[]> ds = taiKhoanDAO.timKiem(maNV, tenTK, tenNV);
        modelTaiKhoan.setRowCount(0);
        for (Object[] r : ds) {
            modelTaiKhoan.addRow(new Object[] {
                    r[0],                       // maNhanVien
                    r[1] != null ? r[1] : "",   // hoTen
                    r[2],                       // tenTaiKhoan
                    r[3]                        // matKhau
            });
        }

    }

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        entity.TaiKhoan tk = getTaiKhoanFromForm();
        if (tk == null) return;

        // Kiểm tra trống
        if (tk.getMaNhanVien().isEmpty() || tk.getTenTaiKhoan().isEmpty() || tk.getMatKhau().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        // KIỂM TRA NHÂN VIÊN ĐÃ CÓ TÀI KHOẢN CHƯA
        if (taiKhoanDAO.kiemTraTonTaiTheoMaNV(tk.getMaNhanVien())) {
            JOptionPane.showMessageDialog(this, "Nhân viên này đã có tài khoản!");
            return;
        }

        boolean ok = taiKhoanDAO.them(tk);
        if (ok) {
            JOptionPane.showMessageDialog(this, "Thêm tài khoản thành công!");
            loadData();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm thất bại! Kiểm tra tên tài khoản hoặc mã nhân viên đã tồn tại.");
        }
    }

    private void btnXoaTrangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaTrangActionPerformed
        clearForm();
        loadData();
    }

    private void btnCapNhatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCapNhatActionPerformed
        entity.TaiKhoan tk = getTaiKhoanFromForm();
        if (tk == null) return;

        // Nếu bạn muốn ưu tiên mã NV được chọn từ bảng:
        if (maNVchon != null && !maNVchon.isEmpty()) {
            tk.setMaNhanVien(maNVchon);
        }

        boolean ok = taiKhoanDAO.sua(tk);
        if (ok) {
            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            loadData();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
        }
    }

    private void chkHienThiMatKhauActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkHienThiMatKhauActionPerformed
        hienMatKhau = chkHienThiMatKhau.isSelected();

        if (hienMatKhau) {
            // Khi chọn hiển thị, dùng mật khẩu gốc đã lưu từ lúc load/chọn dòng
            txtMatKhau.setText(matKhauGoc);
        } else {
            if (matKhauGoc == null || matKhauGoc.isEmpty()) {
                matKhauGoc = txtMatKhau.getText();
            }

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < matKhauGoc.length(); i++) {
                sb.append("*");
            }
            txtMatKhau.setText(sb.toString());
        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCapNhat;
    private javax.swing.JButton btnTaiKhoan;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnTim;
    private javax.swing.JButton btnXoaTrang;
    private javax.swing.JCheckBox chkHienThiMatKhau;
    private javax.swing.JLabel lblMaNV;
    private javax.swing.JLabel lblMatKhau;
    private javax.swing.JLabel lblTenNV;
    private javax.swing.JLabel lblTenTaiKhoan;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JPanel pnlThongTinNhanVien;
    private javax.swing.JPanel pnlThongTinTaiKhoan;
    private javax.swing.JScrollPane scrollTaiKhoan;
    private javax.swing.JScrollPane scrollThongTinNhanVien;
    private javax.swing.JButton tblNhanVien;
    private javax.swing.JTable tblTaiKhoan;
    private javax.swing.JTextField txtMaNV;
    private javax.swing.JTextField txtMatKhau;
    private javax.swing.JTextField txtTenNV;
    private javax.swing.JTextField txtTenTaiKhoan;
    private javax.swing.JTextArea txtThongTinNhanVien;
    // End of variables declaration//GEN-END:variables
}