package gui;

import dao.Dashboard_DAO;
import connectDB.connectDB;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.Map;

// JFreeChart
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
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

        //======================= PANEL 4 THỐNG KÊ =======================
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

        panelChart = new JPanel(new BorderLayout());
        panelChart.setPreferredSize(new Dimension(900, 350));
        panelChart.setBackground(Color.WHITE);
        panelChart.setBorder(BorderFactory.createTitledBorder("Thống kê tổng quan"));

        add(panelStats, BorderLayout.NORTH);
        add(panelChart, BorderLayout.CENTER);

        SwingUtilities.invokeLater(this::loadData);
    }

    private void loadData() {
        Map<String, Double> thongKe = dashboardDAO.getThongKeTongQuan();

        lblDoanhThu.setText(String.format("%,.0f ₫", thongKe.getOrDefault("doanhThu", 0.0)));
        lblSoVe.setText(String.format("%.0f", thongKe.getOrDefault("soVe", 0.0)));
        lblKhachHang.setText(String.format("%.0f", thongKe.getOrDefault("khachHang", 0.0)));
        lblTuyen.setText(String.format("%.0f", thongKe.getOrDefault("tuyen", 0.0)));

        loadChart();
    }

    //=================================================================
    //  LOAD BIỂU ĐỒ ĐÃ SỬA ĐẸP HƠN
    //=================================================================
    private void loadChart() {
        try {
            int nam = LocalDate.now().getYear();
            LocalDate today = LocalDate.now();

            // ========================= BIỂU ĐỒ 1 =========================
            Map<Integer, Double> doanhThuTheoThang = dashboardDAO.getDoanhThuTheoThang(nam);

            DefaultCategoryDataset barDataset = new DefaultCategoryDataset();
            for (int thang = 1; thang <= 12; thang++) {
                barDataset.addValue(doanhThuTheoThang.getOrDefault(thang, 0.0),
                        "Tổng tiền", "" + thang);
            }

            JFreeChart barChart1 = ChartFactory.createBarChart(
                    "Doanh thu theo tháng " + nam,
                    "Tháng",
                    "Doanh thu (VND)",
                    barDataset, PlotOrientation.VERTICAL,
                    false, true, false
            );

            CategoryPlot plot1 = barChart1.getCategoryPlot();
            plot1.setBackgroundPaint(Color.WHITE);
            plot1.setRangeGridlinePaint(Color.GRAY);

            BarRenderer renderer1 = (BarRenderer) plot1.getRenderer();
            renderer1.setSeriesPaint(0, new Color(79, 129, 189));
            renderer1.setBarPainter(new BarRenderer().getBarPainter()); // làm cột thẳng
            renderer1.setShadowVisible(false); // bỏ bóng

            ChartPanel barChartPanel1 = new ChartPanel(barChart1);
            barChartPanel1.setPreferredSize(new Dimension(800, 250));

            JPanel legendThang = createLegend(new Color(79, 129, 189), "Tổng tiền");

            // ========================= BIỂU ĐỒ 2 =========================
            Map<Integer, Integer> soChuyenTheoNgay = dashboardDAO.getSoChuyenTrongNgay(today);

            DefaultCategoryDataset barDataset2 = new DefaultCategoryDataset();
            for (int day = 1; day <= 31; day++) {
                barDataset2.addValue(
                        soChuyenTheoNgay.getOrDefault(day, 0),
                        "Số chuyến",
                        String.valueOf(day)
                );
            }

            JFreeChart barChart2 = ChartFactory.createBarChart(
                    "Số chuyến theo ngày trong tháng " + today.getMonthValue(),
                    "Ngày", "Số chuyến",
                    barDataset2,
                    PlotOrientation.VERTICAL,
                    false, true, false
            );

            CategoryPlot plot2 = barChart2.getCategoryPlot();
            plot2.setBackgroundPaint(Color.WHITE);
            plot2.setRangeGridlinePaint(Color.GRAY);

            BarRenderer renderer2 = (BarRenderer) plot2.getRenderer();
            renderer2.setSeriesPaint(0, new Color(255, 140, 0));
            renderer2.setBarPainter(new BarRenderer().getBarPainter());
            renderer2.setShadowVisible(false);

            ChartPanel barChartPanel2 = new ChartPanel(barChart2);
            barChartPanel2.setPreferredSize(new Dimension(800, 250));

            JPanel legendNgay = createLegend(new Color(255, 140, 0), "Chuyến");

            JPanel leftChartsPanel = new JPanel();
            leftChartsPanel.setLayout(new BoxLayout(leftChartsPanel, BoxLayout.Y_AXIS));
            leftChartsPanel.setBackground(Color.WHITE);

            leftChartsPanel.add(barChartPanel1);
            leftChartsPanel.add(legendThang);
            leftChartsPanel.add(Box.createVerticalStrut(10));
            leftChartsPanel.add(barChartPanel2);
            leftChartsPanel.add(legendNgay);

            // ====================== BIỂU ĐỒ TRÒN ======================
            Map<String, Double> thongKe = dashboardDAO.getThongKeTongQuan();
            double soVeBan = thongKe.getOrDefault("soVeBan", 0.0);
            double soVeTra = thongKe.getOrDefault("soVeTra", 0.0);

            JPanel piePanel = createPieChartPanel(soVeBan, soVeTra);

            panelChart.removeAll();
            panelChart.setLayout(new BorderLayout(20, 0));
            panelChart.add(leftChartsPanel, BorderLayout.WEST);
            panelChart.add(piePanel, BorderLayout.CENTER);

            panelChart.revalidate();
            panelChart.repaint();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //=================================================================
    private JPanel createLegend(Color color, String text) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        panel.setOpaque(false);

        JLabel colorBox = new JLabel();
        colorBox.setPreferredSize(new Dimension(14, 14));
        colorBox.setOpaque(true);
        colorBox.setBackground(color);
        colorBox.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        panel.add(colorBox);
        panel.add(label);

        return panel;
    }

    //=================================================================
    //  BIỂU ĐỒ TRÒN HIỆN PHẦN TRĂM
    //=================================================================
    private JPanel createPieChartPanel(double ban, double tra) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        ChartPanel chartPanel = new ChartPanel(
                createPieChart(ban, tra, "Tỷ lệ vé bán - vé trả", "Vé đã bán", "Vé đã trả")
        );
        chartPanel.setPreferredSize(new Dimension(300, 500));

        panel.add(chartPanel, BorderLayout.CENTER);
        return panel;
    }

    private JFreeChart createPieChart(double value1, double value2,
                                      String title, String label1, String label2) {

        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue(label1, value1);
        dataset.setValue(label2, value2);

        JFreeChart pieChart = ChartFactory.createPieChart(title, dataset, true, true, false);

        PiePlot plot = (PiePlot) pieChart.getPlot();
        plot.setSectionPaint(label1, new Color(79, 129, 189));
        plot.setSectionPaint(label2, new Color(192, 80, 77));

        plot.setLabelFont(new Font("Segoe UI", Font.BOLD, 14));
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator(
                "{0}: {2}"   // {2} = phần trăm %
        ));

        plot.setBackgroundPaint(Color.WHITE);
        plot.setOutlineVisible(false);
        pieChart.setBackgroundPaint(Color.WHITE);

        return pieChart;
    }

    //=================================================================
    private JLabel createStatCard(String title, String value,
                                  Color bgColor, Color textColor) {

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(bgColor);
        card.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        card.setPreferredSize(new Dimension(160, 80));

        JLabel lblTitle = new JLabel(title, SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTitle.setForeground(textColor);

        JLabel lblValue = new JLabel(value, SwingConstants.CENTER);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblValue.setForeground(textColor);

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(lblValue, BorderLayout.CENTER);

        return lblValue;
    }
}
