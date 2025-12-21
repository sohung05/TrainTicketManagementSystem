/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package gui;
import dao.NhanVien_DAO;
import dao.TaiKhoan_DAO;
import entity.NhanVien;
import gui.menu.form.MainForm;
import utils.JTableExporter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author PC
 */
public class Gui_NhanVien extends javax.swing.JPanel {

    private DefaultTableModel modelNhanVien;
    private NhanVien_DAO nhanVienDAO = new NhanVien_DAO();
    private String maNVChon = null;

    /**
     * Creates new form Gui_NhanVien
     */
    public Gui_NhanVien() {
        initComponents();
        modelNhanVien = (DefaultTableModel) tblNhanVien.getModel();
        loadData();
        addTableSelectionListener();
    }

    private void addTableSelectionListener() {
        tblNhanVien.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = tblNhanVien.getSelectedRow();
                if (row != -1) {
                    int modelRow = tblNhanVien.convertRowIndexToModel(row);
                    maNVChon = modelNhanVien.getValueAt(modelRow, 0).toString(); // mã NV
                    txtCCCD.setText(String.valueOf(modelNhanVien.getValueAt(modelRow, 1)));
                    txtHoTen.setText(String.valueOf(modelNhanVien.getValueAt(modelRow, 2)));
                    // Ngày sinh có thể là null hoặc dạng LocalDate -> hiển thị lên JDateChooser
                    Object ngaySinhObj = modelNhanVien.getValueAt(modelRow, 3);
                    if (ngaySinhObj instanceof LocalDate) {
                        LocalDate ld = (LocalDate) ngaySinhObj;
                        Date d = Date.from(ld.atStartOfDay(ZoneId.systemDefault()).toInstant());
                        dcNgaySinh.setDate(d);
                    } else {
                        dcNgaySinh.setDate(null);
                    }
                    String gioiTinh = String.valueOf(modelNhanVien.getValueAt(modelRow, 4));
                    if ("Nam".equalsIgnoreCase(gioiTinh)) {
                        radNam.setSelected(true);
                        radNu.setSelected(false);
                    } else {
                        radNu.setSelected(true);
                        radNam.setSelected(false);
                    }
                    txtDiaChi.setText(String.valueOf(modelNhanVien.getValueAt(modelRow, 5)));
                    txtEmail.setText(String.valueOf(modelNhanVien.getValueAt(modelRow, 6)));
                    txtSoDienThoai.setText(String.valueOf(modelNhanVien.getValueAt(modelRow, 7)));
                    cmbTrangThai.setSelectedItem(String.valueOf(modelNhanVien.getValueAt(modelRow, 8)));
                    txtMaNV.setText(maNVChon);
                }
            }
        });
    }

    private void loadData() {
        modelNhanVien.setRowCount(0);
        List<NhanVien> ds = nhanVienDAO.getAll();
        for (NhanVien nv : ds) {
            modelNhanVien.addRow(new Object[]{
                    nv.getMaNhanVien(),
                    nv.getCCCD(),
                    nv.getHoTen(),
                    nv.getNgaySinh(),    // LocalDate (DAO đã trả về LocalDate)
                    nv.getGioiTinh(),
                    nv.getDiaChi(),
                    nv.getEmail(),
                    nv.getSDT(),
                    nv.isTrangThai() ? "Đang làm" : "Nghỉ làm"
            });
        }
    }

    private void clearForm() {
        txtMaNV.setText("");
        txtCCCD.setText("");
        txtHoTen.setText("");
        dcNgaySinh.setDate(null);
        radNam.setSelected(false);
        radNu.setSelected(false);
        txtDiaChi.setText("");
        txtEmail.setText("");
        txtSoDienThoai.setText("");
        cmbTrangThai.setSelectedIndex(0);
        tblNhanVien.clearSelection();
        maNVChon = null;
    }

    private boolean kiemTraHopLe() {
        String cccd = txtCCCD.getText().trim();
        String hoTen = txtHoTen.getText().trim();
        String diaChi = txtDiaChi.getText().trim();
        String email = txtEmail.getText().trim();
        String sdt = txtSoDienThoai.getText().trim();

        // 1. Kiểm tra CCCD
        if (cccd.isEmpty() || cccd.length() > 12) { // Ràng buộc: Bé hơn 13 số -> 9 hoặc 12 số
            JOptionPane.showMessageDialog(this, "CCCD không hợp lệ! Vui lòng nhập CMND/CCCD (tối đa 12 số).", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtCCCD.requestFocus();
            return false;
        }
        if (!cccd.matches("\\d+")) { // Chỉ chấp nhận ký tự số
            JOptionPane.showMessageDialog(this, "CCCD chỉ được chứa ký tự số.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtCCCD.requestFocus();
            return false;
        }

        // 2. Kiểm tra Họ tên
        if (hoTen.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Họ tên không được rỗng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtHoTen.requestFocus();
            return false;
        }
        // Ràng buộc: Viết hoa chữ cái đầu tiên (Sẽ xử lý format sau khi kiểm tra rỗng)

        // 3. Kiểm tra Ngày sinh (không bắt buộc, nhưng nên có)
        if (dcNgaySinh.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn Ngày sinh!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // 4. Kiểm tra Giới tính
        if (!radNam.isSelected() && !radNu.isSelected()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn Giới tính!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // 5. Kiểm tra Số điện thoại
        if (sdt.isEmpty() || !sdt.matches("0\\d{9}")) { // Ràng buộc: 10 ký tự số, không rỗng (bắt đầu bằng 0)
            JOptionPane.showMessageDialog(this, "Số điện thoại không hợp lệ! Phải đủ 10 ký tự số và bắt đầu bằng 0.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtSoDienThoai.requestFocus();
            return false;
        }

        // 6. Kiểm tra Email
        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Email không được rỗng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtEmail.requestFocus();
            return false;
        }
        // Ràng buộc: Không quá 30 ký tự, không chứa ký tự đặc biệt (ngoại trừ . và @)
        if (email.length() > 30 || !email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
            JOptionPane.showMessageDialog(this, "Email không hợp lệ! (Không quá 30 ký tự, phải theo định dạng email).", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtEmail.requestFocus();
            return false;
        }

        // 7. Kiểm tra Địa chỉ
        if (diaChi.isEmpty() || diaChi.length() > 50) {
            JOptionPane.showMessageDialog(this, "Địa chỉ không được rỗng và không quá 50 ký tự!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtDiaChi.requestFocus();
            return false;
        }
        if (!diaChi.matches("^[\\p{L}0-9\\s,./-]+$")) {
            JOptionPane.showMessageDialog(this, "Địa chỉ chứa ký tự đặc biệt không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtDiaChi.requestFocus();
            return false;
        }

        // 8. Kiểm tra Trạng thái
        if (cmbTrangThai.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn Trạng thái!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private NhanVien getNhanVienFromForm() {
        // Bỏ qua kiểm tra CCCD, Họ tên, Giới tính ở đây vì đã có kiemTraHopLe()

        // Kiểm tra hợp lệ TỔNG THỂ trước khi xử lý dữ liệu
        if (!kiemTraHopLe()) {
            return null;
        }

        String maNV = txtMaNV.getText().trim();
        String cccd = txtCCCD.getText().trim();
        String hoTen = txtHoTen.getText().trim();
        Date d = dcNgaySinh.getDate();
        LocalDate ngaySinh = null;
        if (d != null) {
            ngaySinh = Instant.ofEpochMilli(d.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
        }
        String gioiTinh = radNam.isSelected() ? "Nam" : "Nữ"; // Đã kiểm tra null ở kiemTraHopLe()
        String diaChi = txtDiaChi.getText().trim();
        String email = txtEmail.getText().trim();
        String sdt = txtSoDienThoai.getText().trim();
        String trangThaiStr = Objects.requireNonNull(cmbTrangThai.getSelectedItem()).toString();
        boolean trangThai = "Đang làm".equalsIgnoreCase(trangThaiStr);


        // Nếu là thao tác Thêm, mã NV sẽ được phát sinh trong btnThemActionPerformed
        if (maNV.isEmpty()) maNV = null;

        NhanVien nv = new NhanVien();
        nv.setMaNhanVien(maNV); // Sẽ được gán lại nếu là Thêm mới
        nv.setCCCD(cccd);
        nv.setHoTen(hoTen);
        nv.setSDT(sdt);
        nv.setEmail(email);
        nv.setDiaChi(diaChi);
        nv.setChucVu(1); // 1 = Nhân viên (mặc định)
        nv.setTrangThai(trangThai);
        nv.setNgaySinh(ngaySinh);
        nv.setNgayVaoLam(null);
        nv.setGioiTinh(gioiTinh);

        return nv;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Vien = new javax.swing.JButton();
        btnTaiKhoan = new javax.swing.JButton();
        pnlThongTin = new javax.swing.JPanel();
        lblMaNV = new javax.swing.JLabel();
        lblCCCD = new javax.swing.JLabel();
        lblHoTen = new javax.swing.JLabel();
        lblNgaySinh = new javax.swing.JLabel();
        lblGioiTinh = new javax.swing.JLabel();
        lblDiaChi = new javax.swing.JLabel();
        lblEmail = new javax.swing.JLabel();
        lblSoDienThoai = new javax.swing.JLabel();
        lblTrangThai = new javax.swing.JLabel();
        txtMaNV = new javax.swing.JTextField();
        dcNgaySinh = new com.toedter.calendar.JDateChooser();
        radNam = new javax.swing.JRadioButton();
        radNu = new javax.swing.JRadioButton();
        txtCCCD = new javax.swing.JTextField();
        txtHoTen = new javax.swing.JTextField();
        cmbTrangThai = new javax.swing.JComboBox<>();
        txtDiaChi = new javax.swing.JTextField();
        txtEmail = new javax.swing.JTextField();
        txtSoDienThoai = new javax.swing.JTextField();
        lblTitle = new javax.swing.JLabel();
        scrollNhanVien = new javax.swing.JScrollPane();
        tblNhanVien = new javax.swing.JTable();
        btnTimKiem = new javax.swing.JButton();
        btnXoaTrang = new javax.swing.JButton();
        btnThem = new javax.swing.JButton();
        btnCapNhat = new javax.swing.JButton();
        btnXuatExcel = new javax.swing.JButton();
        btnTaoTaiKhoan = new javax.swing.JButton();

        setBackground(new java.awt.Color(234, 243, 251));

        Vien.setBackground(new java.awt.Color(102, 204, 255));
        Vien.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/accountant.png"))); // NOI18N
        Vien.setText("Nhân Viên");

        btnTaiKhoan.setBackground(new java.awt.Color(102, 204, 255));
        btnTaiKhoan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/profile.png"))); // NOI18N
        btnTaiKhoan.setText("Tài Khoản");
        btnTaiKhoan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTaiKhoanActionPerformed(evt);
            }
        });

        pnlThongTin.setBackground(new java.awt.Color(234, 243, 251));
        pnlThongTin.setBorder(javax.swing.BorderFactory.createTitledBorder("Thông tin nhân viên"));
        pnlThongTin.setMaximumSize(new java.awt.Dimension(0, 0));

        lblMaNV.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lblMaNV.setText("Mã nhân viên:");

        lblCCCD.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lblCCCD.setText("CCCD:\n");

        lblHoTen.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lblHoTen.setText("Họ tên:");

        lblNgaySinh.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lblNgaySinh.setText("Ngày sinh:");

        lblGioiTinh.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lblGioiTinh.setText("Giới tính:");

        lblDiaChi.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lblDiaChi.setText("Địa chỉ:");

        lblEmail.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lblEmail.setText("Email:\n");

        lblSoDienThoai.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lblSoDienThoai.setText("Số điện thoại:");

        lblTrangThai.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lblTrangThai.setText("Trạng thái:\n");

        radNam.setText("Nam");

        radNu.setText("Nữ");

        cmbTrangThai.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "Đang làm", "Nghỉ làm" }));

        javax.swing.GroupLayout pnlThongTinLayout = new javax.swing.GroupLayout(pnlThongTin);
        pnlThongTin.setLayout(pnlThongTinLayout);
        pnlThongTinLayout.setHorizontalGroup(
                pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlThongTinLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(lblGioiTinh)
                                        .addGroup(pnlThongTinLayout.createSequentialGroup()
                                                .addGroup(pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(lblSoDienThoai)
                                                        .addComponent(lblTrangThai)
                                                        .addComponent(lblEmail)
                                                        .addComponent(lblDiaChi))
                                                .addGroup(pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(pnlThongTinLayout.createSequentialGroup()
                                                                .addGap(59, 59, 59)
                                                                .addGroup(pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(txtDiaChi, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addGap(0, 0, Short.MAX_VALUE))
                                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlThongTinLayout.createSequentialGroup()
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                .addGroup(pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(txtSoDienThoai, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(cmbTrangThai, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                        .addGroup(pnlThongTinLayout.createSequentialGroup()
                                                .addGroup(pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(lblCCCD)
                                                        .addComponent(lblMaNV)
                                                        .addComponent(lblHoTen)
                                                        .addComponent(lblNgaySinh))
                                                .addGap(62, 62, 62)
                                                .addGroup(pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(dcNgaySinh, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(txtMaNV, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(txtHoTen, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(txtCCCD, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGroup(pnlThongTinLayout.createSequentialGroup()
                                                                .addComponent(radNam)
                                                                .addGap(52, 52, 52)
                                                                .addComponent(radNu)))))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlThongTinLayout.setVerticalGroup(
                pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlThongTinLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblMaNV)
                                        .addComponent(txtMaNV, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblCCCD)
                                        .addComponent(txtCCCD, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(16, 16, 16)
                                .addGroup(pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lblHoTen)
                                        .addComponent(txtHoTen, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(pnlThongTinLayout.createSequentialGroup()
                                                .addComponent(lblNgaySinh)
                                                .addGap(22, 22, 22)
                                                .addGroup(pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(lblGioiTinh)
                                                        .addComponent(radNam)
                                                        .addComponent(radNu))
                                                .addGap(18, 18, 18)
                                                .addGroup(pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(lblDiaChi)
                                                        .addComponent(txtDiaChi, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(18, 18, 18)
                                                .addGroup(pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(lblEmail)
                                                        .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addComponent(dcNgaySinh, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lblSoDienThoai)
                                        .addComponent(txtSoDienThoai, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lblTrangThai)
                                        .addComponent(cmbTrangThai, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(19, Short.MAX_VALUE))
        );

        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle.setText("Quản Lý Nhân Viên");

        tblNhanVien.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        tblNhanVien.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {

                },
                new String [] {
                        "Mã nhân viên", "CCCD", "Họ tên", "Ngày sinh", "Giới tính", "Địa chỉ ", "Email", "Số điện thoại", "Trạng thái"
                }
        ));
        scrollNhanVien.setViewportView(tblNhanVien);

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

        btnXuatExcel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/excel.png"))); // NOI18N
        btnXuatExcel.setText("Xuất Excel");
        btnXuatExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXuatExcelActionPerformed(evt);
            }
        });

        btnTaoTaiKhoan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/create.png"))); // NOI18N
        btnTaoTaiKhoan.setText("Tạo tài khoản");
        btnTaoTaiKhoan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTaoTaiKhoanActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGap(17, 17, 17)
                                .addComponent(Vien, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnTaiKhoan)
                                .addGap(0, 0, Short.MAX_VALUE))
                        .addComponent(lblTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addComponent(scrollNhanVien, javax.swing.GroupLayout.DEFAULT_SIZE, 935, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addComponent(btnThem, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(btnTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addComponent(btnXuatExcel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(31, 31, 31)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(btnCapNhat, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(btnXoaTrang, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(btnTaoTaiKhoan))
                                                .addGap(18, 18, 18))
                                        .addComponent(pnlThongTin, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(lblTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnTaiKhoan, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(Vien, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(pnlThongTin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(btnTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(btnXoaTrang, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(14, 14, 14)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(btnCapNhat, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(btnThem, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(18, 18, 18)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(btnTaoTaiKhoan, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(btnXuatExcel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(0, 12, Short.MAX_VALUE))
                                        .addComponent(scrollNhanVien))
                                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnTaiKhoanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTaiKhoanActionPerformed
        // TODO add your handling code here:
        MainForm main = (MainForm) SwingUtilities.getAncestorOfClass(MainForm.class, this);
        if (main != null) {
            // Hiển thị giao diện tài khoản
            main.showForm(new Gui_TaiKhoan());
        }
    }//GEN-LAST:event_btnTaiKhoanActionPerformed

    private void btnTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimKiemActionPerformed
        String maNV = txtMaNV.getText().trim();
        String cccd = txtCCCD.getText().trim();
        String hoTen = txtHoTen.getText().trim();
        String email = txtEmail.getText().trim();
        String sdt = txtSoDienThoai.getText().trim();

        String trangThai = null;
        if (cmbTrangThai.getSelectedIndex() > 0)
            trangThai = cmbTrangThai.getSelectedItem().toString();

        String gioiTinh = null;
        if (radNam.isSelected()) gioiTinh = "Nam";
        else if (radNu.isSelected()) gioiTinh = "Nữ";

        // Lấy ngày sinh (nếu có)
        LocalDate ngaySinh = null;
        if (dcNgaySinh.getDate() != null) {
            ngaySinh = dcNgaySinh.getDate().toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDate();
        }

        // Gọi DAO timKiem
        List<NhanVien> ds = nhanVienDAO.timKiem(maNV, cccd, hoTen, email, sdt, trangThai, gioiTinh, ngaySinh);

        // Cập nhật bảng
        DefaultTableModel model = (DefaultTableModel) tblNhanVien.getModel();
        model.setRowCount(0);
        for (NhanVien nv : ds) {
            model.addRow(new Object[]{
                    nv.getMaNhanVien(),
                    nv.getCCCD(),
                    nv.getHoTen(),
                    nv.getNgaySinh(),
                    nv.getGioiTinh(),
                    nv.getDiaChi(),
                    nv.getEmail(),
                    nv.getSDT(),
                    nv.isTrangThai() ? "Đang làm" : "Nghỉ làm"
            });
        }

        if (ds.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy nhân viên phù hợp!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        NhanVien nv = getNhanVienFromForm();
        if (nv != null) {
            if (nv.getNgayVaoLam() == null) {
                nv.setNgayVaoLam(java.time.LocalDate.now());
            }

            String maMoi = nhanVienDAO.generateMaNhanVien(nv.getNgayVaoLam(), nv.getNgaySinh());
            nv.setMaNhanVien(maMoi);

            boolean ok = nhanVienDAO.them(nv);
            if (ok) {
                JOptionPane.showMessageDialog(this,
                        "✅ Thêm nhân viên thành công!\nMã nhân viên mới: " + nv.getMaNhanVien());
                loadData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this,
                        "❌ Thêm thất bại! Có thể CCCD hoặc email đã tồn tại.");
            }
        }
    }

    private void btnXoaTrangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaTrangActionPerformed
        clearForm();
        loadData();
    }

    private void btnCapNhatActionPerformed(java.awt.event.ActionEvent evt) {
        NhanVien nv = getNhanVienFromForm();
        if (nv != null) {
            // 🚫 Không cho phép cập nhật mã — chỉ cập nhật nhân viên đang được chọn
            if (maNVChon == null || maNVChon.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 nhân viên trong bảng để cập nhật.");
                return;
            }

            // Luôn gán mã của nhân viên đang được chọn
            nv.setMaNhanVien(maNVChon);

            boolean ok = nhanVienDAO.sua(nv);
            if (ok) {
                JOptionPane.showMessageDialog(this, "✅ Cập nhật thông tin nhân viên thành công!");
                loadData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Cập nhật thất bại! Kiểm tra lại dữ liệu hoặc kết nối cơ sở dữ liệu.");
            }
        }

    }

    private void btnXuatExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXuatExcelActionPerformed
        JTableExporter.exportJTableToExcel(tblNhanVien);
    }

    private void btnTaoTaiKhoanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTaoTaiKhoanActionPerformed
        int row = tblNhanVien.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn một nhân viên trong bảng để tạo tài khoản.",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int modelRow = tblNhanVien.convertRowIndexToModel(row);
        String maNVCanTaoTK = modelNhanVien.getValueAt(modelRow, 0).toString();

        TaiKhoan_DAO tkDAO = new TaiKhoan_DAO();
        boolean tonTai = tkDAO.kiemTraTonTaiTheoMaNV(maNVCanTaoTK);

        if (tonTai) {
            JOptionPane.showMessageDialog(this,
                    "Nhân viên này đã có tài khoản!",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        MainForm main = (MainForm) SwingUtilities.getAncestorOfClass(MainForm.class, this);
        if (main != null) {
            Gui_TaiKhoan guiTaiKhoan = new Gui_TaiKhoan(maNVCanTaoTK);
            main.showForm(guiTaiKhoan);
        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Vien;
    private javax.swing.JButton btnCapNhat;
    private javax.swing.JButton btnTaiKhoan;
    private javax.swing.JButton btnTaoTaiKhoan;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnTimKiem;
    private javax.swing.JButton btnXoaTrang;
    private javax.swing.JButton btnXuatExcel;
    private javax.swing.JComboBox<String> cmbTrangThai;
    private com.toedter.calendar.JDateChooser dcNgaySinh;
    private javax.swing.JLabel lblCCCD;
    private javax.swing.JLabel lblDiaChi;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblGioiTinh;
    private javax.swing.JLabel lblHoTen;
    private javax.swing.JLabel lblMaNV;
    private javax.swing.JLabel lblNgaySinh;
    private javax.swing.JLabel lblSoDienThoai;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblTrangThai;
    private javax.swing.JPanel pnlThongTin;
    private javax.swing.JRadioButton radNam;
    private javax.swing.JRadioButton radNu;
    private javax.swing.JScrollPane scrollNhanVien;
    private javax.swing.JTable tblNhanVien;
    private javax.swing.JTextField txtCCCD;
    private javax.swing.JTextField txtDiaChi;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtHoTen;
    private javax.swing.JTextField txtMaNV;
    private javax.swing.JTextField txtSoDienThoai;
    // End of variables declaration//GEN-END:variables
}