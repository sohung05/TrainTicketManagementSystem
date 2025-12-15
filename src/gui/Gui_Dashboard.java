package gui;

import dao.Dashboard_DAO;
import connectDB.connectDB;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Map;

// JFreeChart
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.AreaRenderer;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

public class Gui_Dashboard extends JPanel {

    private JLabel lblDoanhThu, lblSoVe, lblKhachHang;
    private Dashboard_DAO dashboardDAO;
    private JPanel panelChart;

    public Gui_Dashboard() {
        setLayout(new BorderLayout());
        connectDB.getConnection();
        dashboardDAO = new Dashboard_DAO();

        // ================= PANEL THỐNG KÊ =================
        JPanel panelStats = new JPanel(new GridLayout(1, 4, 20, 10));
        panelStats.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelStats.setBackground(Color.WHITE);

        lblDoanhThu = createStatCard("Tổng doanh thu", "0",
                new Color(230, 244, 234), new Color(46, 125, 50));
        lblSoVe = createStatCard("Vé đã bán", "0",
                new Color(227, 242, 253), new Color(21, 101, 192));
        lblKhachHang = createStatCard("Khách hàng", "0",
                new Color(255, 248, 225), new Color(245, 124, 0));


        panelStats.add(lblDoanhThu.getParent());
        panelStats.add(lblSoVe.getParent());
        panelStats.add(lblKhachHang.getParent());

        panelChart = new JPanel(new BorderLayout(20, 0));
        panelChart.setPreferredSize(new Dimension(900, 350));
        panelChart.setBackground(Color.WHITE);
        panelChart.setBorder(BorderFactory.createTitledBorder("Thống kê tổng quan"));

        add(panelStats, BorderLayout.NORTH);
        add(panelChart, BorderLayout.CENTER);

        SwingUtilities.invokeLater(this::loadData);
    }

    // ================= LOAD DATA =================
    private void loadData() {
        Map<String, Double> thongKe = dashboardDAO.getThongKeTongQuan();

        lblDoanhThu.setText(String.format("%+.1f %% so với tháng trước", thongKe.getOrDefault("doanhThu", 0.0)));
        lblSoVe.setText(String.format("%+.1f %% so với tháng trước", thongKe.getOrDefault("ptVeBan", 0.0)));
        lblKhachHang.setText(String.format("%+.0f khách hàng mới ", thongKe.getOrDefault("ptKhachHang", 0.0)));

        loadChart();
    }
    /**
     * Cập nhật số lượng khách hàng mới lên label
     * @param thongKe Map chứa các số liệu thống kê
     * @param lblKhachHang JLabel để hiển thị kết quả
     */
    public void capNhatKhachHangMoi(Map<String, Double> thongKe, JLabel lblKhachHang) {
        // Lấy số khách hàng mới, nếu không có dữ liệu thì mặc định = 0
        double soKhachMoi = thongKe.getOrDefault("ptKhachHang", 0.0);

        // Cập nhật text cho label, hiển thị số nguyên
        lblKhachHang.setText(String.format("%+.0f khách hàng mới", soKhachMoi));
    }


    private void loadChart() {
        int nam = LocalDate.now().getYear();
        LocalDate today = LocalDate.now();

        // ================= DATA =================
        Map<Integer, Double> doanhThuTheoThang = dashboardDAO.getDoanhThuTheoThang(nam);
        Map<Integer, Integer> soVeTheoThang = dashboardDAO.getSoVeTheoThang(nam);
        Map<String, Double> thongKe = dashboardDAO.getThongKeNgay(today); // Ví dụ gọi DAO
        capNhatKhachHangMoi(thongKe, lblKhachHang);


        DefaultCategoryDataset doanhThuDataset = new DefaultCategoryDataset();
        DefaultCategoryDataset soVeDataset = new DefaultCategoryDataset();

        for (int thang = 1; thang <= 12; thang++) {
            doanhThuDataset.addValue(doanhThuTheoThang.getOrDefault(thang, 0.0), "Doanh thu", String.valueOf(thang));
            soVeDataset.addValue(soVeTheoThang.getOrDefault(thang, 0), "Số vé", String.valueOf(thang));
        }

        // ================= COMBO CHART (BAR + LINE) =================
        JFreeChart comboChart = ChartFactory.createBarChart(
                "Xu hướng doanh thu & Số vé theo theo tháng " + nam,
                "Tháng",
                "Doanh thu (VND)",
                doanhThuDataset,
                PlotOrientation.VERTICAL,
                true, true, false
        );

        CategoryPlot plot = comboChart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(Color.GRAY);
        plot.setRangeGridlinePaint(Color.GRAY);

        // Bar Renderer - Doanh thu
        BarRenderer barRenderer = new BarRenderer();
        barRenderer.setSeriesPaint(0, new Color(79, 129, 189));
        barRenderer.setMaximumBarWidth(0.2);
        barRenderer.setDefaultToolTipGenerator(new StandardCategoryToolTipGenerator(
                "{0} tháng {1}: {2} ₫", new DecimalFormat("#,###")
        ));
        plot.setRenderer(0, barRenderer);

        // Trục trái
        NumberAxis doanhThuAxis = (NumberAxis) plot.getRangeAxis();
        doanhThuAxis.setNumberFormatOverride(new DecimalFormat("#,### ₫"));

        // Trục phải - Số vé
        NumberAxis soVeAxis = new NumberAxis("Số vé");
        soVeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        plot.setRangeAxis(1, soVeAxis);
        plot.setDataset(1, soVeDataset);
        plot.mapDatasetToRangeAxis(1, 1);

        // Line Renderer - Số vé
        LineAndShapeRenderer lineRenderer = new LineAndShapeRenderer();
        lineRenderer.setSeriesPaint(0, new Color(192, 80, 77));
        lineRenderer.setSeriesStroke(0, new BasicStroke(3.0f));
        lineRenderer.setSeriesShapesVisible(0, true);
        lineRenderer.setSeriesShape(0, new Ellipse2D.Double(-5, -5, 10, 10));
        lineRenderer.setDefaultToolTipGenerator(new StandardCategoryToolTipGenerator(
                "{0} tháng {1}: {2} vé", new DecimalFormat("#")
        ));
        plot.setRenderer(1, lineRenderer);
        plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);

        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryMargin(0.15);
        domainAxis.setLowerMargin(0.05);
        domainAxis.setUpperMargin(0.05);

        // ChartPanel với tooltip
        ChartPanel comboChartPanel = new ChartPanel(comboChart);
        comboChartPanel.setPreferredSize(new Dimension(900, 500));
        comboChartPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 500));

        comboChartPanel.setDisplayToolTips(true);

// ================= DANH SÁCH TUYẾN ================

                List<String> danhSachTuyen = Arrays.asList(
                        "DN-HN",
                        "HN-DN",
                        "HN-HUE",
                        "HN-SG",
                        "HUE-HN",
                        "NT-SG",
                        "PT-SG",
                        "SG-HN",
                        "SG-NT",
                        "SG-PT"
                );

// ================= LẤY DỮ LIỆU =================
        Map<String, Double> doanhThuTheoTuyen =
                dashboardDAO.getDoanhThuTheoTuyenThang12(today);

// ================= DATASET =================
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (String maTuyen : danhSachTuyen) {
            Double doanhThu = doanhThuTheoTuyen.get(maTuyen);
            if (doanhThu != null && doanhThu > 0) {
                dataset.addValue(doanhThu, "Doanh thu", maTuyen);
            }
        }
// ================= BAR CHART (NẰM NGANG) =================
        JFreeChart chart = ChartFactory.createBarChart(
                "Doanh thu theo tuyến trong tháng",
                "Tuyến",
                "Số tiền (VND)",
                dataset,
                PlotOrientation.HORIZONTAL,
                true,
                true,
                false
        );

// ================= PLOT =================
        CategoryPlot bplot = chart.getCategoryPlot();
        bplot.setBackgroundPaint(Color.WHITE);
        bplot.setRangeGridlinePaint(Color.GRAY);

// ================= TRỤC X (TIỀN) =================
        NumberAxis xAxis = (NumberAxis) bplot.getRangeAxis();
        xAxis.setAutoRangeIncludesZero(true);
        xAxis.setNumberFormatOverride(
                NumberFormat.getCurrencyInstance(new Locale("vi", "VN"))
        );

// ================= TRỤC Y (TUYẾN) =================
        CategoryAxis yAxis = bplot.getDomainAxis();
        yAxis.setLabel(null); // Ẩn chữ "Tuyến" bên trái

// ================= LEGEND + TUYẾN DƯỚI =================
        LegendTitle legend = chart.getLegend();
        chart.removeLegend();                 // Gỡ legend mặc định
        legend.setPosition(RectangleEdge.BOTTOM);
        chart.addSubtitle(legend);            // Thêm legend xuống dưới

        TextTitle tuyenTitle = new TextTitle("Tuyến");
        tuyenTitle.setFont(new Font("SansSerif", Font.BOLD, 12));
        tuyenTitle.setPosition(RectangleEdge.BOTTOM);
        chart.addSubtitle(tuyenTitle);        // "Tuyến" dưới cùng

// ================= RENDERER =================
        BarRenderer renderer = (BarRenderer) bplot.getRenderer();
        renderer.setSeriesPaint(0, new Color(90, 120, 159));
        renderer.setMaximumBarWidth(0.15);
        renderer.setDefaultItemLabelGenerator(
                new StandardCategoryItemLabelGenerator(
                        "{2}",
                        NumberFormat.getCurrencyInstance(new Locale("vi", "VN"))
                )
        );
        renderer.setDefaultItemLabelsVisible(true);

        // Pie chart
        Map<String, Double> tk = dashboardDAO.getThongKeTongQuan();
        JPanel piePanel = createPieChartPanel(
                tk.getOrDefault("soVeBan", 0.0),
                tk.getOrDefault("soVeTra", 0.0)
        );
        Map<String, Integer> soChoConTrong = dashboardDAO.getSoChoNgoiConTrongTheoTuyen();
        JTable tuyenTable = createTuyenTable(soChoConTrong);

        JScrollPane scrollPane = new JScrollPane(tuyenTable);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        scrollPane.setBorder(BorderFactory.createTitledBorder("Số chỗ trống theo tuyến"));

// Panel bên phải
        JPanel rightPanel = new JPanel(new BorderLayout(0, 10));
        rightPanel.setBackground(Color.WHITE);
        rightPanel.add(piePanel, BorderLayout.NORTH);
        rightPanel.add(scrollPane, BorderLayout.CENTER);

// ================= CHART PANEL =================
        ChartPanel tuyenChartPanel = new ChartPanel(chart);
        tuyenChartPanel.setPreferredSize(new Dimension(900, 450));
        tuyenChartPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 450));
        tuyenChartPanel.setDisplayToolTips(true);

// ================= GHÉP GIAO DIỆN =================
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(Color.WHITE);
        leftPanel.add(comboChartPanel);  // Combo chart doanh thu + số vé
        leftPanel.add(Box.createVerticalStrut(12));
        leftPanel.add(tuyenChartPanel);  // Biểu đồ tuyến ngang

        panelChart.removeAll();
        panelChart.setLayout(new BorderLayout(20, 0));
        panelChart.add(leftPanel, BorderLayout.CENTER);
        panelChart.add(rightPanel, BorderLayout.EAST);

        panelChart.revalidate();
        panelChart.repaint();
    }

    // ================= CARD =================
    private JLabel createStatCard(String title, String value, Color bg, Color fg) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(bg);
        card.setPreferredSize(new Dimension(160, 80));

        JLabel t = new JLabel(title, SwingConstants.CENTER);
        t.setFont(new Font("Segoe UI", Font.BOLD, 13));
        t.setForeground(fg);

        JLabel v = new JLabel(value, SwingConstants.CENTER);
        v.setFont(new Font("Segoe UI", Font.BOLD, 18));
        v.setForeground(fg);

        card.add(t, BorderLayout.NORTH);
        card.add(v, BorderLayout.CENTER);
        return v;
    }
    private JTable createTuyenTable(Map<String, Integer> soChoNgoiTheoTuyen) {
        String[] columns = {"Tuyến", "Số ghế trống"};

        // Lấy tất cả tuyến, sắp xếp giảm dần theo số ghế
        List<Map.Entry<String, Integer>> allTuyen = soChoNgoiTheoTuyen.entrySet().stream()
                .sorted((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue()))
                .toList();

        Object[][] data = new Object[allTuyen.size()][2];
        for (int i = 0; i < allTuyen.size(); i++) {
            data[i][0] = allTuyen.get(i).getKey();
            data[i][1] = allTuyen.get(i).getValue(); // số ghế trống
        }

        JTable table = new JTable(data, columns);
        table.setFillsViewportHeight(true);
        table.setRowHeight(25);
        table.getTableHeader().setReorderingAllowed(false);
        table.setShowGrid(true);
        table.setGridColor(Color.LIGHT_GRAY);

        return table;
    }

    private JPanel createPieChartPanel(double ban, double tra) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("Vé đã bán", ban);
        dataset.setValue("Vé đã trả", tra);

        JFreeChart chart = ChartFactory.createPieChart("Tỷ lệ vé", dataset, true, true, false);
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}: {2}"));
        plot.setBackgroundPaint(Color.WHITE);

        ChartPanel panel = new ChartPanel(chart);
        panel.setPreferredSize(new Dimension(400, 300));

        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setBackground(Color.WHITE);
        wrap.add(panel);
        return wrap;
    }

}
