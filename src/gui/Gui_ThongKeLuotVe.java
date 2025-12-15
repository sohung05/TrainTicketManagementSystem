package gui;

import com.toedter.calendar.JDateChooser;
import dao.ThongKeLuotVe_DAO;
import entity.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Gui_ThongKeLuotVe extends JPanel {

    private JLabel lblKhach, lblSoVe, lblChuyen;
    private JTable table;
    private DefaultTableModel tableModel;

    private JComboBox<String> cbThang, cbNam;
    private JButton btnLoc;

    // DAO
    private ThongKeLuotVe_DAO thongKeDao = new ThongKeLuotVe_DAO();

    // Lưu panel biểu đồ để refresh
    private JPanel pieChartPanel;
    private JPanel barChartPanel;

    public Gui_ThongKeLuotVe() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        add(createFilterPanel(), BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);

        centerPanel.add(createObjectStatsPanel(), BorderLayout.NORTH);
        centerPanel.add(createDailyChartPanel(), BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        // Gắn action
        btnLoc.addActionListener(e -> loadData());

        // Load mặc định
        loadData();
    }

    // =================== PANEL LỌC ===================
    private JPanel createFilterPanel() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p.setBorder(BorderFactory.createTitledBorder("Lọc"));
        p.setBackground(Color.WHITE);

        String[] months = {"01","02","03","04","05","06","07","08","09","10","11","12"};
        cbThang = new JComboBox<>(months);

        String[] years = new String[11];
        for (int i = 0; i <= 10; i++) years[i] = String.valueOf(2020 + i);
        cbNam = new JComboBox<>(years);

        btnLoc = new JButton("Lọc");

        p.add(new JLabel("Tháng:"));
        p.add(cbThang);
        p.add(new JLabel("Năm:"));
        p.add(cbNam);
        p.add(btnLoc);

        return p;
    }

    // =================== STAT CARDS + TABLE + PIE ===================
    private JPanel createObjectStatsPanel() {

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // ========== LEFT COLUMN ==========
        JPanel leftColumn = new JPanel(new BorderLayout());
        leftColumn.setBackground(Color.WHITE);
        leftColumn.setPreferredSize(new Dimension(550, 300));

        // 3 cards
        JPanel cards = createStatCardsPanel();
        leftColumn.add(cards, BorderLayout.NORTH);

        // Table
        String[] cols = {"Chuyến", "Số lượt đi"};
        tableModel = new DefaultTableModel(cols, 0);
        table = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Chuyến đi nhiều nhất trong tháng"));
        leftColumn.add(scrollPane, BorderLayout.CENTER);

        // ========== RIGHT COLUMN - PIE CHART ==========
        pieChartPanel = new JPanel(new BorderLayout());
        pieChartPanel.setPreferredSize(new Dimension(420, 300));
        pieChartPanel.setBackground(Color.WHITE);

        // Gắn rỗng trước, loadData sẽ render biểu đồ thật
        pieChartPanel.add(new JLabel("Đang tải..."), BorderLayout.CENTER);

        mainPanel.add(leftColumn, BorderLayout.WEST);
        mainPanel.add(pieChartPanel, BorderLayout.CENTER);

        return mainPanel;
    }

    private JPanel createStatCardsPanel() {
        JPanel panelStats = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panelStats.setBorder(new EmptyBorder(10, 10, 10, 10));
        panelStats.setBackground(Color.WHITE);

        Color textGray = new Color(60, 60, 60);

        lblKhach = createStatCard("Tổng lượng khách", "0", Color.WHITE, textGray);
        lblSoVe = createStatCard("Tổng số vé đã đặt", "0", Color.WHITE, textGray);
        lblChuyen = createStatCard("Tổng số chuyến", "0", Color.WHITE, textGray);

        panelStats.add(lblKhach.getParent());
        panelStats.add(lblSoVe.getParent());
        panelStats.add(lblChuyen.getParent());

        return panelStats;
    }

    private JLabel createStatCard(String title, String value, Color bg, Color textColor) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(bg);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        panel.setPreferredSize(new Dimension(170, 70));

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

    // =================== DAILY BAR CHART ===================
    private JPanel createDailyChartPanel() {
        barChartPanel = new JPanel(new BorderLayout());
        barChartPanel.setBackground(Color.WHITE);
        barChartPanel.setPreferredSize(new Dimension(1000, 330));

        barChartPanel.add(new JLabel("Đang tải..."), BorderLayout.CENTER);

        return barChartPanel;
    }

    // =================== LOAD DATA CHÍNH ===================
    private void loadData() {

        int thang = Integer.parseInt(cbThang.getSelectedItem().toString());
        int nam = Integer.parseInt(cbNam.getSelectedItem().toString());

        // Cards
        lblKhach.setText(String.valueOf(thongKeDao.getTongLuotKhach(thang, nam)));
        lblSoVe.setText(String.valueOf(thongKeDao.getTongSoVe(thang, nam)));
        lblChuyen.setText(String.valueOf(thongKeDao.getTongSoTuyen(thang, nam)));

        // Table
        tableModel.setRowCount(0);
        for (Object[] row : thongKeDao.getTuyenNhieuNhatTrongThang(thang, nam)) {
            tableModel.addRow(row);
        }

        // Pie
        loadPieChart(thang, nam);

        // Bar
        loadBarChart(thang, nam);
    }

    // =================== PIE CHART ===================
    private void loadPieChart(int thang, int nam) {
        pieChartPanel.removeAll();

        Map<DoiTuong, Integer> map = thongKeDao.getTiLeKhachHangTheoDoiTuong(thang, nam);

        DefaultPieDataset dataset = new DefaultPieDataset();

        // Chỉ add những đối tượng có số lượng > 0
        for (var e : map.entrySet()) {
            if (e.getValue() > 0) {
                dataset.setValue(e.getKey().toString(), e.getValue());
            }
        }

        JFreeChart pieChart = ChartFactory.createPieChart(
                "Tỉ lệ khách hàng theo đối tượng - " + thang + "/" + nam,
                dataset,
                true, true, false
        );

        // =================== LÀM ĐẸP BIỂU ĐỒ ===================
        var plot = (org.jfree.chart.plot.PiePlot) pieChart.getPlot();

        plot.setBackgroundPaint(Color.WHITE);

        // Hiển thị % trên lát
        plot.setLabelGenerator(
                new org.jfree.chart.labels.StandardPieSectionLabelGenerator(
                        "{0}: {2}" // {2} = phần trăm
                )
        );

        plot.setLabelFont(new Font("Arial", Font.BOLD, 12));
        plot.setCircular(true);
        plot.setLabelBackgroundPaint(new Color(255, 255, 255));
        plot.setLabelOutlinePaint(Color.GRAY);
        plot.setLabelShadowPaint(null);

        // Border
        plot.setOutlineVisible(false);

        // =================== MÀU SẮC ĐẸP ===================
        Color[] colors = {
                new Color(72, 149, 239),   // Xanh dương
                new Color(255, 159, 67),   // Cam
                new Color(255, 105, 98),   // Đỏ hồng
                new Color(120, 224, 143),  // Xanh lá
                new Color(162, 155, 254)   // Tím
        };

        int i = 0;
        for (Object key : dataset.getKeys()) {
            plot.setSectionPaint((Comparable) key, colors[i % colors.length]);
            i++;
        }

        // =================== PANEL ===================
        ChartPanel chartPanel = new ChartPanel(pieChart);
        chartPanel.setBackground(Color.WHITE);
        chartPanel.setMouseWheelEnabled(true);

        pieChartPanel.add(chartPanel, BorderLayout.CENTER);
        pieChartPanel.revalidate();
        pieChartPanel.repaint();
    }

    // =================== BAR CHART ===================
    private void loadBarChart(int thang, int nam) {
        barChartPanel.removeAll();

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        Map<Integer, Integer> map = new HashMap<>();

        List<HoaDon> ds = thongKeDao.loadHoaDonTheoThangNam(thang, nam);

        for (HoaDon hd : ds) {
            if (hd.getNgayTao() == null) continue;
            int day = hd.getNgayTao().getDayOfMonth();

            int so = hd.getDanhSachChiTiet().stream()
                    .mapToInt(ChiTietHoaDon::getSoLuong)
                    .sum();

            map.put(day, map.getOrDefault(day, 0) + so);
        }

        for (int i = 1; i <= 31; i++) {
            dataset.addValue(map.getOrDefault(i, 0), "Số vé", String.valueOf(i));
        }

        JFreeChart barChart = ChartFactory.createBarChart(
                "Thống kê lượt vé theo ngày - " + thang + "/" + nam,
                "Ngày",
                "Số lượt vé",
                dataset
        );

        CategoryPlot plot = barChart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.GRAY);

        ChartPanel chartPanel = new ChartPanel(barChart);

        barChartPanel.add(chartPanel, BorderLayout.CENTER);
        barChartPanel.revalidate();
        barChartPanel.repaint();
    }
}
