package gui;

import dao.Dashboard_DAO;
import connectDB.connectDB;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.Map;

// JFreeChart import
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

public class Gui_Dashboard extends JPanel {
    private JLabel lblDoanhThu, lblSoVe, lblKhachHang, lblTuyen;
    private Dashboard_DAO dashboardDAO;
    private JPanel panelChart;

    public Gui_Dashboard() {
        setLayout(new BorderLayout());
        connectDB.getConnection();
        dashboardDAO = new Dashboard_DAO();

        // ===== PANEL THỐNG KÊ =====
        JPanel panelStats = new JPanel(new GridLayout(1, 4, 20, 10));
        panelStats.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelStats.setBackground(Color.WHITE);

        lblDoanhThu = createStatCard("Tổng doanh thu", "0 ₫",
                new Color(230, 244, 234), new Color(46, 125, 50));
        lblSoVe = createStatCard("Vé đã bán", "0",
                new Color(227, 242, 253), new Color(21, 101, 192));
        lblKhachHang = createStatCard("Khách hàng", "0",
                new Color(255, 248, 225), new Color(245, 124, 0));
        lblTuyen = createStatCard("Tuyến hoạt động", "0",
                new Color(243, 229, 245), new Color(106, 27, 154));

        panelStats.add(lblDoanhThu.getParent());
        panelStats.add(lblSoVe.getParent());
        panelStats.add(lblKhachHang.getParent());
        panelStats.add(lblTuyen.getParent());

        // ===== PANEL BIỂU ĐỒ =====
        panelChart = new JPanel(new BorderLayout());
        panelChart.setPreferredSize(new Dimension(900, 350));
        panelChart.setBackground(Color.WHITE);
        panelChart.setBorder(BorderFactory.createTitledBorder("Doanh thu - Vé - Khách hàng theo tháng"));

        add(panelStats, BorderLayout.NORTH);
        add(panelChart, BorderLayout.CENTER);

        SwingUtilities.invokeLater(this::loadData);
    }

    private void loadData() {
        // ===== CẬP NHẬT CHỈ SỐ TỔNG QUAN =====
        Map<String, Double> thongKe = dashboardDAO.getThongKeTongQuan();

        double doanhThu = thongKe.getOrDefault("doanhThu", 0.0);
        double soVe = thongKe.getOrDefault("soVe", 0.0);
        double khachHang = thongKe.getOrDefault("khachHang", 0.0);
        double tuyen = thongKe.getOrDefault("tuyen", 0.0);

        lblDoanhThu.setText(String.format("%,.0f %% so với tháng trước", doanhThu));
        lblSoVe.setText(String.format("%.0f %% so với tháng trước", soVe));
        lblKhachHang.setText(String.format("%.0f %% so với tháng trước", khachHang));
        lblTuyen.setText(String.format("%.0f %% so với tháng trước", tuyen));

        // ===== TẢI BIỂU ĐỒ =====
        loadChart();
    }

private void loadChart() {
    try {
        int nam = LocalDate.now().getYear();

        // ===== Biểu đồ cột =====
        Map<Integer, Double> doanhThuTheoThang = dashboardDAO.getDoanhThuTheoThang(nam);

        DefaultCategoryDataset barDataset = new DefaultCategoryDataset();
        for (int thang = 1; thang <= 12; thang++) {
            barDataset.addValue(doanhThuTheoThang.getOrDefault(thang, 0.0),
                    "Tổng tiền", "" + thang);
        }

        JFreeChart barChart = ChartFactory.createBarChart(
                "Doanh thu theo tháng năm " + nam,
                "Tháng",
                "Doanh thu (VND)",
                barDataset,
                PlotOrientation.VERTICAL,
                true, true, false
        );

        CategoryPlot plot = barChart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.GRAY);
        ((BarRenderer) plot.getRenderer()).setSeriesPaint(0, new Color(79, 129, 189));

        ChartPanel barChartPanel = new ChartPanel(barChart);
        barChartPanel.setPreferredSize(new Dimension(600, 500));

        // ===== 2 Pie Chart =====
        Map<String, Double> thongKe = dashboardDAO.getThongKeTongQuan();

        double soVeBan = thongKe.getOrDefault("soVeBan", 0.0);
        double soVeTra = thongKe.getOrDefault("soVeTra", 0.0);

        double luuLuongTuyen = thongKe.getOrDefault("luuLuongTuyen", 0.0);


        JPanel twoPieChartsPanel = createTwoPieCharts(soVeBan, soVeTra, luuLuongTuyen);
        twoPieChartsPanel.setPreferredSize(new Dimension(300, 500));

        // ===== Panel chính =====
        panelChart.removeAll();
        panelChart.setLayout(new BorderLayout(10, 0));
        panelChart.add(barChartPanel, BorderLayout.WEST); // Bar chart bên trái
        panelChart.add(twoPieChartsPanel, BorderLayout.CENTER); // 2 Pie chart bên phải

        panelChart.revalidate();
        panelChart.repaint();

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this,
                "Lỗi khi tải biểu đồ: " + e.getMessage(),
                "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
}

    private JFreeChart createPieChart(double value1, double value2, String title, String label1, String label2) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue(label1, value1);
        dataset.setValue(label2, value2);

        JFreeChart pieChart = ChartFactory.createPieChart(
                title,
                dataset,
                true, true, false
        );

        // Chỉnh màu
        PiePlot plot = (PiePlot) pieChart.getPlot();
        plot.setSectionPaint(label1, new Color(79, 129, 189)); // xanh dương
        plot.setSectionPaint(label2, new Color(192, 80, 77));  // đỏ
        plot.setBackgroundPaint(Color.WHITE);
        pieChart.setBackgroundPaint(Color.WHITE);

        return pieChart;
    }

    private JPanel createTwoPieCharts(double soVeBan, double soVeTra, double luuLuongGa) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        ChartPanel chartPanel1 = new ChartPanel(
                createPieChart(soVeBan, soVeTra, "Tỷ lệ vé bán - vé trả", "Vé đã bán", "Vé đã trả")
        );
        chartPanel1.setPreferredSize(new Dimension(300, 250));
        panel.add(chartPanel1);

        return panel;
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

        return lblValue; // không add trực tiếp vào 'this', panelStats sẽ add
    }

}
