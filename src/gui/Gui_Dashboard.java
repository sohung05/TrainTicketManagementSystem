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
import org.jfree.chart.renderer.category.AreaRenderer;
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
