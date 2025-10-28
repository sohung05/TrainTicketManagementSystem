package connectDB;

import java.sql.*;

public class connectDB {
    private static Connection con = null;
    public static connectDB instance = new connectDB();

    public static Connection getCon() {
        try {
            // Nếu kết nối chưa mở hoặc đã bị đóng → tự mở lại
            if (con == null || con.isClosed()) {
                instance.connect();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return con;
    }

    public static connectDB getInstance() {
        return instance;
    }

    public void connect() throws SQLException {
        try {
            // Load driver (bắt buộc trên IntelliJ)
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            // Thêm encrypt=true & trustServerCertificate=true để tránh lỗi SSL
            String url = "jdbc:sqlserver://localhost:1433;databaseName=HTQLVT;encrypt=true;trustServerCertificate=true";
            String user = "sa";
            String password = "sapassword";

            // Nếu kết nối đang đóng → mở lại
            if (con == null || con.isClosed()) {
                con = DriverManager.getConnection(url, user, password);
                System.out.println("Connected to the database.");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Driver SQL Server chưa được load!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Không thể kết nối đến SQL Server!");
            e.printStackTrace();
            throw e;
        }
    }

    public void disconnect() {
        if (con != null) {
            try {
                con.close();
                System.out.println("Disconnected from the database.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
