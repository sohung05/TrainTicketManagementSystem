package utils;

import entity.*;
import javax.print.*;
import java.awt.*;
import java.awt.print.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Class xử lý in hóa đơn cho máy in nhiệt K58 (58mm)
 * Sử dụng Java Print API để in hóa đơn và vé
 */
public class ThermalPrinter implements Printable {

    private HoaDon hoaDon;
    private List<ChiTietHoaDon> chiTietList;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public ThermalPrinter(HoaDon hoaDon, List<ChiTietHoaDon> chiTietList) {
        this.hoaDon = hoaDon;
        this.chiTietList = chiTietList;
    }

    /**
     * In hóa đơn ra máy in nhiệt (có dialog để chọn máy in)
     */
    public boolean printInvoice() {
        return printInvoice(false);
    }

    /**
     * In hóa đơn ra máy in nhiệt
     * @param showDialog true để hiển thị dialog chọn máy in, false để in trực tiếp
     */
    public boolean printInvoice(boolean showDialog) {
        try {
            // Tìm máy in mặc định
            PrintService defaultPrintService = PrintServiceLookup.lookupDefaultPrintService();

            if (defaultPrintService == null) {
                System.err.println("❌ Không tìm thấy máy in!");
                return false;
            }

            System.out.println("🖨️ Đang in trên máy: " + defaultPrintService.getName());

            // Tạo PrinterJob
            PrinterJob printerJob = PrinterJob.getPrinterJob();
            printerJob.setPrintService(defaultPrintService);

            // Set trang in
            PageFormat pageFormat = printerJob.defaultPage();
            Paper paper = pageFormat.getPaper();

            // Cài đặt kích thước giấy 58mm
            double width = 58 * 72 / 25.4; // 58mm = ~165 points

            // Tính chiều cao dựa trên số lượng vé (để không quá lớn)
            int estimatedLines = 40 + (chiTietList.size() * 3); // Mỗi vé ~3 dòng
            double height = Math.min(estimatedLines * 15, 600); // Tối đa 600 points

            paper.setSize(width, height);
            paper.setImageableArea(0, 0, width, height);

            pageFormat.setPaper(paper);
            pageFormat.setOrientation(PageFormat.PORTRAIT);

            printerJob.setPrintable(this, pageFormat);

            // In với hoặc không có dialog
            if (showDialog) {
                // Hiển thị dialog để chọn máy in
                if (printerJob.printDialog()) {
                    printerJob.print();
                } else {
                    System.out.println("⚠️ Người dùng hủy in");
                    return false;
                }
            } else {
                // In trực tiếp không hiển thị dialog
                printerJob.print();
            }

            System.out.println("✅ In hóa đơn thành công!");
            return true;

        } catch (PrinterException e) {
            System.err.println("❌ Lỗi in: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Vẽ nội dung hóa đơn lên trang giấy
     */
    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        if (pageIndex > 0) {
            return NO_SUCH_PAGE;
        }

        try {
            System.out.println("🖨️ Bắt đầu vẽ hóa đơn...");

            Graphics2D g2d = (Graphics2D) graphics;
            g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

            int y = 10; // Vị trí bắt đầu
            int lineHeight = 15;
            int x = 5; // Lề trái

            // ========================
            // 1. HEADER CÔNG TY (giống Dialog_HoaDon)
            // ========================
            Font boldFont = new Font("SansSerif", Font.BOLD, 9);
            Font normalFont = new Font("SansSerif", Font.PLAIN, 8);
            Font headerFont = new Font("SansSerif", Font.BOLD, 10);
            Font titleFont = new Font("SansSerif", Font.BOLD, 12);
            Font smallFont = new Font("SansSerif", Font.PLAIN, 6);

            g2d.setFont(boldFont);

            drawCenteredText(g2d, "CONG TY CO PHAN VAN TAI", y, (int) pageFormat.getImageableWidth());
            y += lineHeight;

            drawCenteredText(g2d, "DUONG SAT SAI GON", y, (int) pageFormat.getImageableWidth());
            y += lineHeight;

            // Thông tin liên hệ
            g2d.setFont(normalFont);
            drawCenteredText(g2d, "12 Nguyễn Văn Bảo, P. Hạnh Thông", y, (int) pageFormat.getImageableWidth());
            y += lineHeight;

            drawCenteredText(g2d, "TP. Hồ Chí minh", y, (int) pageFormat.getImageableWidth());
            y += lineHeight;

            g2d.setFont(boldFont);
            drawCenteredText(g2d, "www.dailybanvetauahktv.com", y, (int) pageFormat.getImageableWidth());
            y += lineHeight;

            drawCenteredText(g2d, "Hostline: 18009898", y, (int) pageFormat.getImageableWidth());
            y += lineHeight + 5;

            // ========================
            // 2. TIÊU ĐỀ HÓA ĐƠN (giống Dialog_HoaDon)
            // ========================
            g2d.setFont(titleFont);
            drawCenteredText(g2d, "HOA DON BAN HANG", y, (int) pageFormat.getImageableWidth());
            y += lineHeight + 3;

            // ========================
            // 3. THÔNG TIN HÓA ĐƠN (giống Dialog_HoaDon)
            // ========================
            g2d.setFont(normalFont);

            System.out.println("  - Mã HĐ: " + hoaDon.getMaHoaDon());
            g2d.drawString("Mã hóa đơn: " + hoaDon.getMaHoaDon(), x, y);
            y += lineHeight;

            String ngayTao = hoaDon.getNgayTao() != null ?
                    hoaDon.getNgayTao().format(DATE_FORMATTER) : "N/A";
            System.out.println("  - Ngày: " + ngayTao);
            g2d.drawString("Ngày: " + ngayTao, x, y);
            y += lineHeight;

            String tenKH = hoaDon.getKhachHang() != null ?
                    hoaDon.getKhachHang().getHoTen() : "(Chua cung cap)";
            g2d.drawString("Khách hàng: " + tenKH, x, y);
            y += lineHeight;

            String sdtKH = hoaDon.getKhachHang() != null && hoaDon.getKhachHang().getSDT() != null ?
                    hoaDon.getKhachHang().getSDT() : "(Chua cung cap)";
            g2d.drawString("Số điện thoại: " + sdtKH, x, y);
            y += lineHeight;

            String emailKH = hoaDon.getKhachHang() != null && hoaDon.getKhachHang().getEmail() != null ?
                    hoaDon.getKhachHang().getEmail() : "(Chua cung cap)";
            g2d.drawString("Email: " + emailKH, x, y);
            y += lineHeight + 5;

            // ========================
            // 4. TIÊU ĐỀ "Thông tin vé" (giống Dialog_HoaDon)
            // ========================
            g2d.setFont(boldFont);
            g2d.drawString("Thong tin ve", x, y);
            y += lineHeight;

            // Dòng phân cách
            drawDashedLine(g2d, x, y, (int) pageFormat.getImageableWidth() - 10);
            y += 8;

            // ========================
            // 5. HEADER BẢNG
            // ========================
            g2d.setFont(new Font("Monospaced", Font.PLAIN, 7));

            String header = String.format(
                    "%s %s %s %s %s",
                    center("L.Vé", 8),
                    center("Gađi", 4),
                    center("Gađến", 6),
                    center("SL", 2),
                    center("T.Tiền", 10)
            );

            g2d.drawString(header, x - 10, y);
            y += 10;

            drawDashedLine(g2d, x, y, (int) pageFormat.getImageableWidth() - 10);
            y += 8;

            // ========================
            // 6. CHI TIẾT VÉ
            // ========================
            g2d.setFont(new Font("Monospaced", Font.PLAIN, 7));

            System.out.println("  - Số lượng chi tiết vé: " + chiTietList.size());

            for (ChiTietHoaDon ct : chiTietList) {
                Ve ve = ct.getVe();
                if (ve == null) {
                    System.out.println("  - Vé NULL, bỏ qua");
                    continue;
                }
                System.out.println("  - In vé: " + ve.getMaVe());

                // Lấy thông tin
                String loaiVe = ve.getLoaiVe() != null ?
                        getLoaiVeAbbreviation(ve.getLoaiVe().getTenLoaiVe()) : "N/A";

                String gaDi = "??";
                String gaDen = "??";

                if (ve.getLichTrinh() != null) {
                    if (ve.getLichTrinh().getGaDi() != null) {
                        gaDi = getGaAbbreviation(ve.getLichTrinh().getGaDi().getTenGa());
                    }
                    if (ve.getLichTrinh().getGaDen() != null) {
                        gaDen = getGaAbbreviation(ve.getLichTrinh().getGaDen().getTenGa());
                    }
                }

                int soLuong = ct.getSoLuong();
                double giaVe = ct.getGiaVe();
                double thanhTien = giaVe * soLuong; // Tính đơn giản: giá vé * số lượng

                // Debug
                System.out.println("    - Giá vé: " + giaVe + ", SL: " + soLuong + ", Thành tiền: " + thanhTien);

                // Format tiền (dạng 200,000)
                String tienStr = formatMoneyFull(Math.abs(thanhTien)); // abs() để tránh số âm

                // Format: Loại Vé (8) | Ga đi (5) | Ga đến (6) | SL (2) | Thành tiền (10)
                String row = String.format("%-7s %-4s %-5s %-1s %10s",
                        loaiVe, gaDi, gaDen, soLuong, tienStr);

                g2d.drawString(row, x, y);
                y += 12;
            }

            // Dòng phân cách
            y += 5;

            // ========================
            // 7. KHUYẾN MÃI VÀ TỔNG TIỀN (giống Dialog_HoaDon)
            // ========================
            g2d.setFont(normalFont);

            // Hiển thị khuyến mãi (tên mã hoặc "Không")
            String khuyenMaiStr = "Khong";
            if (hoaDon.getKhuyenMai() != null && !hoaDon.getKhuyenMai().isEmpty()) {
                double tongGiamGia = hoaDon.tinhTongGiamGia();
                khuyenMaiStr = hoaDon.getKhuyenMai() + " (-" + formatMoneyFull(tongGiamGia) + " VND)";
            }
            g2d.drawString("Khuyen mai: " + khuyenMaiStr, x, y);
            y += lineHeight;

            // LẤY TỔNG TIỀN TỪ DATABASE (đã tính đúng rồi)
            double tongTien = hoaDon.getTongTien();
            System.out.println("  - Tổng tiền từ DB: " + tongTien);

            g2d.setFont(normalFont);
            String tongTienStr = "Tong tien: " + formatMoneyFull(tongTien) + " VND";
            g2d.drawString(tongTienStr, x, y);
            y += lineHeight + 5;

            // ========================
            // 8. THÔNG TIN LIÊN HỆ & LƯU Ý (giống Dialog_HoaDon)
            // ========================
            Font boldSmallFont = new Font("Monospaced", Font.BOLD, smallFont.getSize());

            g2d.setFont(boldSmallFont);
            g2d.drawString("Thông tin liên hệ:", x, y);
            FontMetrics fm = g2d.getFontMetrics(boldSmallFont);
            int xNext = x + fm.stringWidth("Thông tin liên hệ: ");

            g2d.setFont(smallFont);
            g2d.drawString("Công ty Cổ phần Đường ", xNext, y);
            y += 9;
            g2d.drawString("sắt HKTA-Số 12, P. Hạnh Thông, TP. Hồ Chí Minh.", x, y);
            y += 9;
            g2d.drawString("Hotline: 18009898.", x, y);
            y += 11;

            // "Lưu ý:" in đậm
            g2d.setFont(new Font("SansSerif", Font.BOLD, 6));
            g2d.drawString("Luu y:", x, y);
            y += 10;

            // Nội dung lưu ý in thường
            g2d.setFont(new Font("SansSerif", Font.PLAIN, 6));
            g2d.drawString("Khách hàng có thể đổi/trả trước thời gian tàu khởi", x, y);
            y += 10;
            g2d.drawString("hành 1 ngày.", x, y);

            return PAGE_EXISTS;

        } catch (Exception e) {
            System.err.println("❌ Lỗi khi vẽ hóa đơn: " + e.getMessage());
            e.printStackTrace();
            throw new PrinterException("Lỗi khi vẽ hóa đơn: " + e.getMessage());
        }
    }

    /**
     * Căn giữa text trong một độ rộng nhất định bằng cách thêm khoảng trắng
     */
    private String center(String text, int width) {
        if (text.length() >= width) {
            return text.substring(0, width);
        }
        int totalPadding = width - text.length();
        int leftPadding = totalPadding / 2;
        int rightPadding = totalPadding - leftPadding;

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < leftPadding; i++) {
            sb.append(" ");
        }
        sb.append(text);
        for (int i = 0; i < rightPadding; i++) {
            sb.append(" ");
        }
        return sb.toString();
    }

    /**
     * Vẽ text căn giữa
     */
    private void drawCenteredText(Graphics2D g2d, String text, int y, int pageWidth) {
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int x = (pageWidth - textWidth) / 2;
        g2d.drawString(text, x, y);
    }

    /**
     * Vẽ đường kẻ ngang
     */
    private void drawLine(Graphics2D g2d, int x, int y, int width) {
        g2d.drawLine(x, y, x + width, y);
    }

    /**
     * Vẽ đường nét đứt (giống Dialog_HoaDon)
     */
    private void drawDashedLine(Graphics2D g2d, int x, int y, int width) {
        int dashWidth = 3;
        int gapWidth = 2;
        int currentX = x;

        while (currentX < x + width) {
            int endX = Math.min(currentX + dashWidth, x + width);
            g2d.drawLine(currentX, y, endX, y);
            currentX += dashWidth + gapWidth;
        }
    }

    /**
     * Cắt chuỗi nếu quá dài
     */
    private String truncateString(String str, int maxLength) {
        if (str == null) return "N/A";
        if (str.length() <= maxLength) return str;
        return str.substring(0, maxLength - 1) + ".";
    }

    /**
     * Format tiền tệ rút gọn - dùng trong bảng
     * Ví dụ: 200000 → 200.00
     */
    private String formatMoney(double amount) {
        // Chia cho 1000 để rút gọn, giữ 2 chữ số thập phân
        return String.format("%.2f", amount / 1000);
    }

    /**
     * Format tiền tệ đầy đủ với dấu phẩy (giống Dialog_HoaDon)
     */
    private String formatMoneyFull(double amount) {
        return String.format("%,.0f", amount);
    }

    /**
     * Lấy viết tắt 2 chữ đầu của tên ga
     * Ví dụ: Sài Gòn → SG, Hà Nội → HN
     */
    private String getGaAbbreviation(String tenGa) {
        if (tenGa == null || tenGa.isEmpty()) return "??";

        // Tách theo khoảng trắng
        String[] words = tenGa.trim().split("\\s+");

        if (words.length >= 2) {
            // Lấy chữ cái đầu của 2 từ đầu tiên
            return (words[0].substring(0, 1) + words[1].substring(0, 1)).toUpperCase();
        } else if (words.length == 1 && words[0].length() >= 2) {
            // Nếu chỉ có 1 từ, lấy 2 ký tự đầu
            return words[0].substring(0, 2).toUpperCase();
        } else {
            return tenGa.substring(0, Math.min(2, tenGa.length())).toUpperCase();
        }
    }

    /**
     * Lấy viết tắt loại vé
     * Ví dụ: Sinh viên → SV, Người lớn → NL, Trẻ em → TE, Người cao tuổi → NCT
     */
    private String getLoaiVeAbbreviation(String tenLoaiVe) {
        if (tenLoaiVe == null || tenLoaiVe.isEmpty()) return "N/A";

        // Chuẩn hóa tên loại vé (bỏ dấu, chuyển thường)
        String normalized = tenLoaiVe.toLowerCase()
                .replaceAll("à|á|ả|ã|ạ|ă|ằ|ắ|ẳ|ẵ|ặ|â|ầ|ấ|ẩ|ẫ|ậ", "a")
                .replaceAll("è|é|ẻ|ẽ|ẹ|ê|ề|ế|ể|ễ|ệ", "e")
                .replaceAll("ì|í|ỉ|ĩ|ị", "i")
                .replaceAll("ò|ó|ỏ|õ|ọ|ô|ồ|ố|ổ|ỗ|ộ|ơ|ờ|ớ|ở|ỡ|ợ", "o")
                .replaceAll("ù|ú|ủ|ũ|ụ|ư|ừ|ứ|ử|ữ|ự", "u")
                .replaceAll("ỳ|ý|ỷ|ỹ|ỵ", "y")
                .replaceAll("đ", "d");

        // Kiểm tra các trường hợp cụ thể
        if (normalized.contains("sinh vien") || normalized.contains("sinh vi")) {
            return "SV";
        } else if (normalized.contains("nguoi lon") || normalized.contains("nguoi l")) {
            return "NL";
        } else if (normalized.contains("tre em")) {
            return "TE";
        } else if (normalized.contains("nguoi cao tuoi") || normalized.contains("cao tuoi")) {
            return "NCT";
        } else {
            // Lấy chữ cái đầu của mỗi từ (tối đa 3 chữ)
            String[] words = tenLoaiVe.trim().split("\\s+");
            StringBuilder abbr = new StringBuilder();
            for (int i = 0; i < Math.min(words.length, 3); i++) {
                if (!words[i].isEmpty()) {
                    abbr.append(words[i].charAt(0));
                }
            }
            return abbr.toString().toUpperCase();
        }
    }

    /**
     * In vé đơn lẻ
     */
    public static boolean printTicket(Ve ve) {
        try {
            PrintService defaultPrintService = PrintServiceLookup.lookupDefaultPrintService();

            if (defaultPrintService == null) {
                System.err.println("❌ Không tìm thấy máy in!");
                return false;
            }

            PrinterJob printerJob = PrinterJob.getPrinterJob();
            printerJob.setPrintService(defaultPrintService);

            PageFormat pageFormat = printerJob.defaultPage();
            Paper paper = pageFormat.getPaper();

            double width = 58 * 72 / 25.4;
            double height = 400;
            paper.setSize(width, height);
            paper.setImageableArea(0, 0, width, height);

            pageFormat.setPaper(paper);

            printerJob.setPrintable(new Printable() {
                @Override
                public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
                    if (pageIndex > 0) return NO_SUCH_PAGE;

                    Graphics2D g2d = (Graphics2D) graphics;
                    g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

                    int y = 10;
                    int lineHeight = 15;
                    int x = 5;

                    Font boldFont = new Font("SansSerif", Font.BOLD, 9);
                    Font normalFont = new Font("SansSerif", Font.PLAIN, 8);
                    Font titleFont = new Font("SansSerif", Font.BOLD, 10);
                    Font largeFont = new Font("SansSerif", Font.BOLD, 11);
                    Font smallFont = new Font("SansSerif", Font.PLAIN, 6);

                    // ========== HEADER (giống Dialog_Ve) ==========
                    g2d.setFont(boldFont);
                    drawCenteredTextStatic(g2d, "CÔNG TY CỔ PHẦN VẬN TẢI", y, (int) pageFormat.getImageableWidth());
                    y += lineHeight;

                    drawCenteredTextStatic(g2d, "ĐƯỜNG SẮT HKTA", y, (int) pageFormat.getImageableWidth());
                    y += lineHeight;

                    g2d.setFont(normalFont);
                    drawCenteredTextStatic(g2d, "THẺ LÊN TÀU HỎA/BOARDING PASS", y, (int) pageFormat.getImageableWidth());
                    y += lineHeight + 3;

                    // ========== MÃ VÉ ==========
                    g2d.setFont(normalFont);
                    g2d.drawString("Mã vé/TicketID: " + ve.getMaVe(), x, y);
                    y += lineHeight + 2;

                    // ========== GA ĐI - GA ĐẾN (lớn) ==========
                    if (ve.getLichTrinh() != null) {
                        String gaDiText = ve.getLichTrinh().getGaDi() != null ?
                                ve.getLichTrinh().getGaDi().getTenGa() : "";
                        String gaDenText = ve.getLichTrinh().getGaDen() != null ?
                                ve.getLichTrinh().getGaDen().getTenGa() : "";

                        g2d.setFont(smallFont);
                        g2d.drawString("Ga Đi", x + 10, y);
                        g2d.drawString("Ga đến", x + 90, y);
                        y += 10;

                        g2d.setFont(largeFont);
                        g2d.drawString(gaDiText, x + 5, y);
                        g2d.drawString(gaDenText, x + 80, y);
                        y += lineHeight;
                    }

                    // ========== THÔNG TIN TÀU, NGÀY, GIỜ ==========
                    g2d.setFont(normalFont);

                    if (ve.getLichTrinh() != null && ve.getLichTrinh().getChuyenTau() != null) {
                        g2d.drawString("Tàu/Train: " + ve.getLichTrinh().getChuyenTau().getSoHieuTau(), x, y);
                        y += lineHeight;
                    }

                    if (ve.getLichTrinh() != null && ve.getLichTrinh().getGioKhoiHanh() != null) {
                        java.time.format.DateTimeFormatter dateFormatter =
                                java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        java.time.format.DateTimeFormatter timeFormatter =
                                java.time.format.DateTimeFormatter.ofPattern("HH:mm");

                        g2d.drawString("Ngày đi/Date: " +
                                ve.getLichTrinh().getGioKhoiHanh().format(dateFormatter), x, y);
                        y += lineHeight;

                        g2d.drawString("Giờ đi/Time: " +
                                ve.getLichTrinh().getGioKhoiHanh().format(timeFormatter), x, y);
                        y += lineHeight;
                    }

                    // ========== TOA VÀ CHỖ NGỒI ==========
                    if (ve.getChoNgoi() != null) {
                        String toaText = ve.getChoNgoi().getToa() != null ?
                                String.valueOf(ve.getChoNgoi().getToa().getSoToa()) : "";
                        String choText = String.valueOf(ve.getChoNgoi().getViTri());

                        g2d.drawString("Toa/Coach: " + toaText + "       Chỗ/Seat: " + choText, x, y);
                        y += lineHeight;

                        // Loại chỗ
                        if (ve.getChoNgoi().getToa() != null &&
                                ve.getChoNgoi().getToa().getLoaiToa() != null) {
                            g2d.drawString("Loại chỗ/Class: " +
                                    ve.getChoNgoi().getToa().getLoaiToa().getTenLoaiToa(), x, y);
                            y += lineHeight;
                        }
                    }

                    // ========== LOẠI VÉ ==========
                    if (ve.getLoaiVe() != null) {
                        g2d.drawString("Loại vé/Ticket: " + ve.getLoaiVe().getTenLoaiVe(), x, y);
                        y += lineHeight;
                    }

                    // ========== TÊN VÀ GIẤY TỜ ==========
                    g2d.drawString("Họ tên/Name: " +
                            (ve.getTenKhachHang() != null ? ve.getTenKhachHang() : ""), x, y);
                    y += lineHeight;

                    g2d.drawString("Giấy tờ/Passport: " +
                            (ve.getSoCCCD() != null ? ve.getSoCCCD() : ""), x, y);
                    y += lineHeight;

                    // ========== GIÁ VÉ ==========
                    g2d.setFont(boldFont);
                    String giaVeStr = String.format("%,.0f VND", ve.getGiaVe());
                    g2d.drawString("Gía/Price: " + giaVeStr, x, y);
                    y += lineHeight + 5;

                    // ========== ĐƯỜNG PHÂN CÁCH ==========
                    drawDashedLineStatic(g2d, x, y, (int) pageFormat.getImageableWidth() - 10);
                    y += 10;

                    // ========== THÔNG TIN LIÊN HỆ & LƯU Ý ==========
                    Font boldSmallFont = new Font("Monospaced", Font.BOLD, smallFont.getSize());

                    g2d.setFont(boldSmallFont);
                    g2d.drawString("Thông tin liên hệ:", x, y);
                    FontMetrics fm = g2d.getFontMetrics(boldSmallFont);
                    int xNext = x + fm.stringWidth("Thông tin liên hệ: ");

                    g2d.setFont(smallFont);
                    g2d.drawString("Công ty Cổ phần Đường ", xNext, y);
                    y += 9;
                    g2d.drawString("sắt HKTA-Số 12, P. Hạnh Thông, TP. Hồ Chí Minh.", x, y);
                    y += 9;
                    g2d.drawString("Hotline: 18009898.", x, y);
                    y += 11;

                    g2d.setFont(new Font("SansSerif", Font.BOLD, 6));
                    g2d.drawString("Lưu ý:", x, y);
                    y += 9;
                    g2d.setFont(smallFont);
                    g2d.drawString("Để đảm bảo quyền lợi của mình quý khách vui lòng,", x, y);
                    y += 9;
                    g2d.drawString("mang theo vé và giấy tờ tuỳ thân ghi trong vé trong", x, y);
                    y += 9;
                    g2d.drawString("suốt thời gian hành trình và xuất trình cho nhân", x, y);
                    y += 9;
                    g2d.drawString("viên xuất vé khi có yêu cầu.", x, y);
                    y += 9;
                    g2d.drawString("", x, y);

                    return PAGE_EXISTS;
                }
            }, pageFormat);

            printerJob.print();
            System.out.println("✅ In vé thành công!");
            return true;

        } catch (PrinterException e) {
            System.err.println("❌ Lỗi in vé: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private static void drawCenteredTextStatic(Graphics2D g2d, String text, int y, int pageWidth) {
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int x = (pageWidth - textWidth) / 2;
        g2d.drawString(text, x, y);
    }

    /**
     * Vẽ đường nét đứt (static method cho printTicket)
     */
    private static void drawDashedLineStatic(Graphics2D g2d, int x, int y, int width) {
        int dashWidth = 3;
        int gapWidth = 2;
        int currentX = x;

        while (currentX < x + width) {
            int endX = Math.min(currentX + dashWidth, x + width);
            g2d.drawLine(currentX, y, endX, y);
            currentX += dashWidth + gapWidth;
        }
    }
}


