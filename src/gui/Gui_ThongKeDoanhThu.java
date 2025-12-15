package gui;

import dao.ThongKeDoanhThu_DAO;
import entity.HoaDon;
import entity.ChiTietHoaDon;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class Gui_ThongKeDoanhThu extends JPanel {

    private JLabel lblDoanhThu, lblSoHoaDon, lblHoaDonTra, lblSoVeTra;

    private JTable table;
    private DefaultTableModel tableModel;

    private JComboBox<String> cbThang, cbNam;

    private DefaultCategoryDataset dataset;
    private ChartPanel chartPanel;

    private final ThongKeDoanhThu_DAO tkDAO = new ThongKeDoanhThu_DAO();

    public Gui_ThongKeDoanhThu() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        add(createFilterPanel(), BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);

        centerPanel.add(createStatCardsPanel(), BorderLayout.NORTH);
        centerPanel.add(createStatsPanel(), BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);
    }

    // ===================  PANEL 4 CARD ===================
    private JPanel createStatCardsPanel() {
        JPanel panelStats = new JPanel(new GridLayout(1, 4, 20, 10));
        panelStats.setBorder(new EmptyBorder(10, 20, 10, 20));
        panelStats.setBackground(Color.WHITE);

        Color textGray = new Color(60, 60, 60);

        lblDoanhThu = createStatCard("Tổng doanh thu trong tháng", "0 ₫", Color.WHITE, textGray);
        lblSoHoaDon = createStatCard("Số hóa đơn trong tháng", "0", Color.WHITE, textGray);
        lblHoaDonTra = createStatCard("Số hóa đơn trả", "0", Color.WHITE, textGray);
        lblSoVeTra = createStatCard("Số vé trả", "0", Color.WHITE, textGray);

        panelStats.add(lblDoanhThu.getParent());
        panelStats.add(lblSoHoaDon.getParent());
        panelStats.add(lblHoaDonTra.getParent());
        panelStats.add(lblSoVeTra.getParent());

        return panelStats;
    }

    private JLabel createStatCard(String title, String value, Color bg, Color textColor) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(bg);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        panel.setPreferredSize(new Dimension(200, 70));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 14));
        lblTitle.setForeground(textColor);

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Arial", Font.BOLD, 24));
        lblValue.setForeground(textColor);

        panel.add(lblTitle, BorderLayout.NORTH);
        panel.add(lblValue, BorderLayout.CENTER);

        return lblValue;
    }

    // =================== PANEL LỌC ===================
    private JPanel createFilterPanel() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p.setBorder(BorderFactory.createTitledBorder("Lọc"));
        p.setBackground(Color.WHITE);

        String[] months = {"01","02","03","04","05","06","07","08","09","10","11","12"};
        cbThang = new JComboBox<>(months);

        String[] years = new String[51];
        for (int i = 0; i <= 50; i++) years[i] = String.valueOf(2020 + i);
        cbNam = new JComboBox<>(years);

        JButton btnLoc = new JButton("Lọc");
        btnLoc.addActionListener(this::thucHienLoc);

        p.add(new JLabel("Tháng:"));
        p.add(cbThang);
        p.add(new JLabel("Năm:"));
        p.add(cbNam);
        p.add(btnLoc);

        return p;
    }

    // =================== BIỂU ĐỒ + BẢNG ===================
    private JPanel createStatsPanel() {
        JPanel statsPanel = new JPanel(new BorderLayout());
        statsPanel.setBackground(Color.WHITE);

        // ===== BIỂU ĐỒ =====
        dataset = new DefaultCategoryDataset();

        for (int i = 1; i <= 31; i++)
            dataset.addValue(0, "Doanh thu", String.valueOf(i));

        JFreeChart barChart = ChartFactory.createBarChart(
                "Doanh thu theo ngày",
                "Ngày",
                "Doanh thu (VNĐ)",
                dataset
        );

        CategoryPlot plot = barChart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.GRAY);

        chartPanel = new ChartPanel(barChart);

        JPanel chartWrapper = new JPanel(new BorderLayout());
        chartWrapper.setPreferredSize(new Dimension(1000, 350));
        chartWrapper.add(chartPanel, BorderLayout.CENTER);

        statsPanel.add(chartWrapper, BorderLayout.NORTH);

        // ===== BẢNG =====
        String[] cols = {"Mã hóa đơn", "Mã nhân viên", "Mã khách hàng", "Ngày tạo", "Giờ tạo", "Tổng tiền"};
        tableModel = new DefaultTableModel(cols, 0);
        table = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Thông tin hóa đơn"));

        statsPanel.add(scrollPane, BorderLayout.CENTER);

        return statsPanel;
    }

    // =================== SỰ KIỆN LỌC ===================
    private void thucHienLoc(ActionEvent e) {
        int thang = Integer.parseInt(cbThang.getSelectedItem().toString());
        int nam = Integer.parseInt(cbNam.getSelectedItem().toString());

        List<HoaDon> dsHD = tkDAO.loadHoaDonTheoThangNam(thang, nam);

        capNhat4Card(dsHD, thang, nam);
        capNhatBang(dsHD);
        capNhatBieuDo(dsHD);
    }

    // =================== CẬP NHẬT 4 CARD ===================
    private void capNhat4Card(List<HoaDon> ds, int thang, int nam) {
        lblSoHoaDon.setText(String.valueOf(ds.size()));

        long hoaDonTra = ds.stream().filter(h -> !h.isTrangThai()).count();
        lblHoaDonTra.setText(String.valueOf(hoaDonTra));

        int soVeTra = 0;
        for (HoaDon hd : ds) {
            for (ChiTietHoaDon ct : hd.getDanhSachChiTiet()) {
                if (ct.getMucGiam() == -1)      // tuỳ bạn định nghĩa vé trả như nào
                    soVeTra++;
            }
        }
        lblSoVeTra.setText(String.valueOf(soVeTra));

        double tongDT = tkDAO.getTongDoanhThu(thang, nam);
        lblDoanhThu.setText(String.format("%,.0f ₫", tongDT));
    }

    // =================== CẬP NHẬT BẢNG ===================
    private void capNhatBang(List<HoaDon> ds) {
        tableModel.setRowCount(0);

        for (HoaDon hd : ds) {
            double tongTien = hd.getDanhSachChiTiet().stream()
                    .mapToDouble(ChiTietHoaDon::tinhThanhTien)
                    .sum();

            tableModel.addRow(new Object[]{
                    hd.getMaHoaDon(),
                    hd.getNhanVien().getMaNhanVien(),
                    hd.getKhachHang().getMaKH(),
                    hd.getNgayTao(),
                    hd.getGioTao(),
                    String.format("%,.0f", tongTien)
            });
        }
    }

    // =================== CẬP NHẬT BIỂU ĐỒ ===================
    private void capNhatBieuDo(List<HoaDon> ds) {
        for (int i = 1; i <= 31; i++)
            dataset.setValue(0, "Doanh thu", String.valueOf(i));

        for (HoaDon hd : ds) {
            if (hd.getNgayTao() == null) continue;

            int day = hd.getNgayTao().getDayOfMonth();

            double sum = hd.getDanhSachChiTiet().stream()
                    .filter(ct -> ct.getMucGiam() >= 0) // loại vé trả
                    .mapToDouble(ChiTietHoaDon::tinhThanhTien)
                    .sum();


            double old = dataset.getValue("Doanh thu", String.valueOf(day)).doubleValue();
            dataset.setValue(old + sum, "Doanh thu", String.valueOf(day));
        }
    }
}
