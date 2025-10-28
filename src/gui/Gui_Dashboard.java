package gui;

import dao.Dashboard_DAO;
import entity.Ve;
import connectDB.connectDB;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

// JFreeChart import
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

public class Gui_Dashboard extends JPanel {
    private JLabel lblDoanhThu, lblSoVe, lblKhachHang, lblTuyen;
    private JTable tblVeGanDay, tblLichTrinhGanDay;
    private Dashboard_DAO dashboardDAO;
    private DefaultTableModel modelLT;
    private JPanel panelChart;

    public Gui_Dashboard() {
        setLayout(new BorderLayout());
        connectDB.getConnection();
        dashboardDAO = new Dashboard_DAO();

        // ===== PANEL THỐNG KÊ =====
        JPanel panelStats = new JPanel(new GridLayout(1, 4, 20, 10));
        panelStats.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelStats.setBackground(Color.WHITE);

        lblDoanhThu = createStatCard("Tổng doanh thu", "0 ₫", new Color(230, 244, 234), new Color(46, 125, 50));
        lblSoVe = createStatCard("Vé đã bán", "0", new Color(227, 242, 253), new Color(21, 101, 192));
        lblKhachHang = createStatCard("Khách hàng", "0", new Color(255, 248, 225), new Color(245, 124, 0));
        lblTuyen = createStatCard("Tuyến hoạt động", "0", new Color(243, 229, 245), new Color(106, 27, 154));

        panelStats.add(lblDoanhThu.getParent());
        panelStats.add(lblSoVe.getParent());
        panelStats.add(lblKhachHang.getParent());
        panelStats.add(lblTuyen.getParent());

        // ===== PANEL BIỂU ĐỒ =====
        panelChart = new JPanel(new BorderLayout());
        panelChart.setPreferredSize(new Dimension(900, 350));
        panelChart.setBackground(Color.WHITE);
        panelChart.setBorder(BorderFactory.createTitledBorder("Doanh thu - Vé - Khách hàng theo tháng"));

        // ===== BẢNG VÉ GẦN ĐÂY =====
        String[] columnsVe = {"Mã vé", "Tên khách hàng", "Số CCCD", "Thời gian lên tàu", "Giá vé"};
        DefaultTableModel modelVe = new DefaultTableModel(columnsVe, 0);
        tblVeGanDay = new JTable(modelVe);
        JScrollPane scrollVe = new JScrollPane(tblVeGanDay);
        scrollVe.setBorder(BorderFactory.createTitledBorder("Danh sách vé gần đây"));
        scrollVe.setPreferredSize(new Dimension(800, 180));

        // ===== BẢNG LỊCH TRÌNH GẦN ĐÂY =====
        String[] columnsLichTrinh = {"Mã lịch trình", "Tuyến", "Ga", "Giờ khởi hành", "Giờ đến dự kiến"};
        modelLT = new DefaultTableModel(columnsLichTrinh, 0);
        tblLichTrinhGanDay = new JTable(modelLT);
        JScrollPane scrollLichTrinh = new JScrollPane(tblLichTrinhGanDay);
        scrollLichTrinh.setBorder(BorderFactory.createTitledBorder("Danh sách lịch trình gần đây"));
        scrollLichTrinh.setPreferredSize(new Dimension(800, 180));

        // ===== PANEL CHỨA 2 BẢNG =====
        JPanel panelTables = new JPanel(new GridLayout(1, 2, 10, 0));
        panelTables.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelTables.add(scrollVe);
        panelTables.add(scrollLichTrinh);

        // ===== THÊM VÀO GIAO DIỆN CHÍNH =====
        add(panelStats, BorderLayout.NORTH);
        add(panelChart, BorderLayout.CENTER);
        add(panelTables, BorderLayout.SOUTH);

        // ===== LOAD DỮ LIỆU SAU KHI GUI SẴN SÀNG =====
        SwingUtilities.invokeLater(this::loadData);
    }

    // === HÀM LOAD DỮ LIỆU ===
    private void loadData() {
        // ===== VÉ GẦN ĐÂY =====
        DefaultTableModel model = (DefaultTableModel) tblVeGanDay.getModel();
        model.setRowCount(0);

        List<Ve> list = dashboardDAO.getDanhSachVeGanDay();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (Ve v : list) {
            String thoiGian = (v.getThoiGianLenTau() != null)
                    ? v.getThoiGianLenTau().format(fmt)
                    : "Chưa xác định";

            model.addRow(new Object[]{
                    v.getMaVe(),
                    v.getTenKhachHang() != null ? v.getTenKhachHang() : "Không rõ",
                    v.getSoCCCD() != null ? v.getSoCCCD() : "Không có",
                    thoiGian,
                    String.format("%,.0f ₫", v.getGiaVe())
            });
        }

        // ===== LỊCH TRÌNH GẦN ĐÂY =====
        modelLT.setRowCount(0);
        List<Object[]> dsLichTrinh = dashboardDAO.getLichTrinhGanDay();
        DateTimeFormatter fmt2 = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (Object[] lt : dsLichTrinh) {
            String maLichTrinh = (String) lt[0];
            String tenTuyen = (String) lt[1];
            String tenGa = (String) lt[2];
            Timestamp gioKhoiHanh = (Timestamp) lt[3];
            Timestamp gioDenDuKien = (Timestamp) lt[4];

            String gioKhoiHanhStr = gioKhoiHanh != null ? gioKhoiHanh.toLocalDateTime().format(fmt2) : "—";
            String gioDenStr = gioDenDuKien != null ? gioDenDuKien.toLocalDateTime().format(fmt2) : "—";

            modelLT.addRow(new Object[]{maLichTrinh, tenTuyen, tenGa, gioKhoiHanhStr, gioDenStr});
        }

        // ===== CẬP NHẬT CHỈ SỐ TỔNG QUAN =====
        Map<String, Double> thongKe = dashboardDAO.getThongKeTongQuan();

        double doanhThu = thongKe.getOrDefault("doanhThu", 0.0);
        double soVe = thongKe.getOrDefault("soVe", 0.0);
        double khachHang = thongKe.getOrDefault("khachHang", 0.0);
        double tuyen = thongKe.getOrDefault("tuyen", 0.0);

        lblDoanhThu.setText(String.format("%,.0f ₫", doanhThu));
        lblSoVe.setText(String.format("%.0f vé", soVe));
        lblKhachHang.setText(String.format("%.0f KH", khachHang));
        lblTuyen.setText(String.format("%.0f tuyến", tuyen));

        // ===== TẠO BIỂU ĐỒ =====
        loadChart();
    }

    private void loadChart() {
        try {
            int nam = LocalDate.now().getYear();

            // ===== Biểu đồ cột =====
            Map<Integer, Double> doanhThuTheoThang = dashboardDAO.getDoanhThuTheoThang(nam);

            DefaultCategoryDataset barDataset = new DefaultCategoryDataset();
            for (int thang = 1; thang <= 12; thang++) {
                double doanhThu = doanhThuTheoThang.getOrDefault(thang, 0.0);
                barDataset.addValue(doanhThu, "Tổng tiền", "" + thang);
            }

            JFreeChart barChart = ChartFactory.createBarChart(
                    "Doanh thu theo tháng " + nam,
                    "Tháng",
                    "Doanh thu (VNĐ)",
                    barDataset,
                    PlotOrientation.VERTICAL,
                    true, true, false
            );

            CategoryPlot plot = barChart.getCategoryPlot();
            plot.setBackgroundPaint(Color.WHITE);
            plot.setRangeGridlinePaint(Color.GRAY);
            BarRenderer renderer = (BarRenderer) plot.getRenderer();
            renderer.setSeriesPaint(0, new Color(79, 129, 189));

            CategoryAxis domainAxis = plot.getDomainAxis();
            domainAxis.setTickLabelFont(new Font("Segoe UI", Font.PLAIN, 12));
            domainAxis.setLabelFont(new Font("Segoe UI", Font.BOLD, 14));

            NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
            rangeAxis.setLabelFont(new Font("Segoe UI", Font.BOLD, 14));
            rangeAxis.setTickLabelFont(new Font("Segoe UI", Font.PLAIN, 12));

            ChartPanel barChartPanel = new ChartPanel(barChart);

            // ===== Biểu đồ tròn =====
            Map<String, Double> thongKe = dashboardDAO.getThongKeTongQuan();
            double soVeBan = thongKe.getOrDefault("soVeBan", 0.0);  // Vé đã bán (trangThai = 1)
            double soVeTra = thongKe.getOrDefault("soVeTra", 0.0);  // Vé đã trả (trangThai = 0)

            JFreeChart pieChart = createPieChart(soVeBan, soVeTra);
            ChartPanel pieChartPanel = new ChartPanel(pieChart);

            // ===== Panel chứa cả 2 biểu đồ =====
            panelChart.removeAll();
            panelChart.setLayout(new GridLayout(1, 2, 10, 0)); // 2 cột
            panelChart.add(barChartPanel);
            panelChart.add(pieChartPanel);

            panelChart.revalidate();
            panelChart.repaint();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải biểu đồ: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JFreeChart createPieChart(double soVeBan, double soVeTra) {
        DefaultPieDataset pieDataset = new DefaultPieDataset();
        pieDataset.setValue("Vé đã bán", soVeBan);
        pieDataset.setValue("Vé đã trả", soVeTra);

        JFreeChart pieChart = ChartFactory.createPieChart(
                "Tỷ lệ giữa Vé đã bán và Vé đã trả",
                pieDataset,
                true,   // legend
                true,   // tooltips
                false   // URLs
        );

        pieChart.setBackgroundPaint(Color.WHITE);
        pieChart.getTitle().setFont(new Font("Segoe UI", Font.BOLD, 14));
        pieChart.getLegend().setItemFont(new Font("Segoe UI", Font.PLAIN, 12));

        org.jfree.chart.plot.PiePlot plot = (org.jfree.chart.plot.PiePlot) pieChart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);

        // ===== Nhãn bên ngoài =====
        plot.setSimpleLabels(false); // quan trọng: false để nhãn ra ngoài
        plot.setLabelGap(0.05);      // khoảng cách nhãn ra ngoài
        plot.setLabelOutlinePaint(null);
        plot.setLabelBackgroundPaint(null);
        plot.setLabelShadowPaint(null);
        plot.setLabelFont(new Font("Segoe UI", Font.BOLD, 12));
        plot.setLabelGenerator(new org.jfree.chart.labels.StandardPieSectionLabelGenerator(
                "{0}: {2}" // tên + phần trăm
        ));

        plot.setCircular(true);
        plot.setInteriorGap(0.04); // khoảng trống giữa trung tâm và múi

        return pieChart;
    }






    private JLabel createStatCard(String title, String value, Color bgColor, Color textColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(bgColor);
        card.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        card.setPreferredSize(new Dimension(150, 80));

        JLabel lblTitle = new JLabel(title, SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTitle.setForeground(textColor);

        JLabel lblValue = new JLabel(value, SwingConstants.CENTER);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblValue.setForeground(textColor);

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(lblValue, BorderLayout.CENTER);

        add(card);
        return lblValue;
    }
}
