package vn.iotstar.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL =
    		"jdbc:sqlserver://localhost:14330;"
    				+ "databaseName=ShoppingService;"
    				+ "encrypt=true;trustServerCertificate=true;"
    				+ "integratedSecurity=true";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Không tìm thấy SQL Server JDBC Driver.", e);
        }

        return DriverManager.getConnection(URL);
    }
}