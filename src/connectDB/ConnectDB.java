package connectDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDB {
    private static Connection con = null;
    private static ConnectDB instance = new ConnectDB();

    private final String url = "jdbc:sqlserver://localhost:1433;databaseName=QuanLyNhaHang;encrypt=false;trustServerCertificate=true";
    private final String user = "sa";
    private final String pass = "11111";

    // Singleton instance
    public static ConnectDB getInstance() {
        return instance;
    }

    // Establish a new connection
    public void connect() throws SQLException {
        if (con == null || con.isClosed()) {
            con = DriverManager.getConnection(url, user, pass);
//            System.out.println("Connection established");
        }
    }

    // Get the existing connection or establish a new one if it's closed
    public static Connection getConnection() {
        if (con == null || !instance.isConnected()) {
            try {
                instance.connect();
//                System.out.println("Kết nối cơ sở dữ liệu thành công!");
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Kết nối cơ sở dữ liệu thất bại!");
            }
        }
        return con;
    }

    // Check if the connection is still active
    private boolean isConnected() {
        try {
            return con != null && !con.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    // Close the connection
    public static void closeConnection() {
        if (con != null) {
            try {
                con.close();
                con = null;
                System.out.println("Đã đóng kết nối cơ sở dữ liệu.");
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Đóng kết nối cơ sở dữ liệu thất bại!");
            }
        }
    }
}