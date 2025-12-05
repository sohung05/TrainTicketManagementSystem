package gui;

import com.toedter.calendar.JDateChooser;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class Gui_ThongKeDoanhThu extends JPanel {

    private JLabel lblDoanhThu, lblSoHoaDon, lblHoaDonTra, lblSoVeTra;
    private JTable table;
    private DefaultTableModel tableModel;

    public Gui_ThongKeDoanhThu() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // =================== PANEL LỌC ĐƯA LÊN TRÊN CÙNG ===================
        add(createFilterPanel(), BorderLayout.NORTH);

        // =================== PANEL CHỨA 4 CARD + BIỂU ĐỒ + BẢNG ===================
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);

        centerPanel.add(createStatCardsPanel(), BorderLayout.NORTH);
        centerPanel.add(createStatsPanel(), BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);
    }

    // =================== 4 CARD THỐNG KÊ (MÀU TRẮNG - CHỮ XÁM) ===================
    private JPanel createStatCardsPanel() {
        JPanel panelStats = new JPanel(new GridLayout(1, 4, 20, 10));
        panelStats.setBorder(new EmptyBorder(10, 20, 10, 20));
        panelStats.setBackground(Color.WHITE);

        Color textGray = new Color(60, 60, 60); // Xám đậm

        lblDoanhThu = createStatCard("Tổng doanh thu trong tháng", "0 ₫",
                Color.WHITE, textGray);
        lblSoHoaDon = createStatCard("Số hóa đơn trong tháng", "0",
                Color.WHITE, textGray);
        lblHoaDonTra = createStatCard("Số hóa đơn trả", "0",
                Color.WHITE, textGray);
        lblSoVeTra = createStatCard("Số vé trả", "0",
                Color.WHITE, textGray);

        panelStats.add(lblDoanhThu.getParent());
        panelStats.add(lblSoHoaDon.getParent());
        panelStats.add(lblHoaDonTra.getParent());
        panelStats.add(lblSoVeTra.getParent());

        return panelStats;
    }

    private JLabel createStatCard(String title, String value, Color bg, Color textColor) {

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
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

    // =================== PANEL LỌC (TRÊN ĐẦU) ===================
    private JPanel createFilterPanel() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p.setBorder(BorderFactory.createTitledBorder("Lọc"));
        p.setBackground(Color.WHITE);

        String[] months = {"01","02","03","04","05","06","07","08","09","10","11","12"};
        JComboBox<String> cbThang = new JComboBox<>(months);

        String[] years = new String[51];
        for (int i = 0; i <= 50; i++) years[i] = String.valueOf(2020 + i);
        JComboBox<String> cbNam = new JComboBox<>(years);

        JButton btnLoc = new JButton("Lọc");

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
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (int day = 1; day <= 31; day++) {
            dataset.addValue(0, "Doanh thu", String.valueOf(day));
        }

        JFreeChart barChart = ChartFactory.createBarChart(
                "Doanh thu theo ngày - Tháng 12/2025",
                "Ngày",
                "Doanh thu",
                dataset
        );

         // Nền tổng thể chart
        barChart.setBackgroundPaint(Color.WHITE);

         // Lấy plot để chỉnh nền vùng hiển thị cột
        CategoryPlot plot = barChart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);      // nền vùng cột
        plot.setRangeGridlinePaint(Color.GRAY); // màu lưới ngang

        ChartPanel chartPanel = new ChartPanel(barChart);

        JPanel chartWrapper = new JPanel(new BorderLayout());
       // chartWrapper.setBorder(BorderFactory.createTitledBorder("Doanh thu các ngày trong tháng"));
        chartWrapper.setPreferredSize(new Dimension(1000, 350));
        chartWrapper.add(chartPanel, BorderLayout.CENTER);

        statsPanel.add(chartWrapper, BorderLayout.NORTH);


        // ===== BẢNG =====
        String[] cols = {"Mã hóa đơn", "Mã nhân viên", "Mã khách hàng",
                "Ngày tạo", "Giờ tạo", "Tổng tiền"};

        tableModel = new DefaultTableModel(cols, 0);
        table = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(1000, 200));
        scrollPane.setBorder(BorderFactory.createTitledBorder("Thông tin hóa đơn"));

        statsPanel.add(scrollPane, BorderLayout.CENTER);

        return statsPanel;
    }
}
