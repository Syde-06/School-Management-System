import java.sql.*;

public class DatabaseConfig {
    private static final String URL = "jdbc:mysql://localhost:3306/chmsu_student_mgmt";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    private static Connection connection;

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                System.out.println("Database connection established successfully!");
            }
            return connection;
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found: " + e.getMessage());
            return null;
        } catch (SQLException e) {
            System.err.println("Failed to connect to database: " + e.getMessage());
            return null;
        }
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
        }
    }

    public static void initializeDatabase() {
        try {
            Connection conn = getConnection();
            if (conn != null) {
                System.out.println("Database initialization successful!");
                
                // Test the connection with a simple query
                try (Statement stmt = conn.createStatement()) {
                    stmt.executeQuery("SELECT 1");
                    System.out.println("Database connection test passed!");
                } catch (SQLException e) {
                    System.err.println("Database connection test failed: " + e.getMessage());
                }
            } else {
                System.err.println("Failed to initialize database connection!");
            }
        } catch (Exception e) {
            System.err.println("Failed to initialize database: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
