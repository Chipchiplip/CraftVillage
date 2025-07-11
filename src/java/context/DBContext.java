package context;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author PC
 */
public class DBContext {

    public static String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    public static String dbURL = "jdbc:sqlserver://localhost:1433;databaseName=CraftDB;encrypt=false;trustServerCertificate=true;loginTimeout=5;socketTimeout=10000;";
    public static String userDB = "sa";
    public static String passDB = "1234";
    
    public static Connection getConnection() throws SQLException {
        Connection con = null;
        try {
            Class.forName(driverName);
            con = DriverManager.getConnection(dbURL, userDB, passDB);
            return con;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBContext.class.getName()).log(Level.SEVERE, "SQL Server JDBC Driver not found.", ex);
            throw new SQLException("Database driver not found.", ex);
        }
    }
    
    public static void main(String[] args) {
        try (Connection con = getConnection()) {
            if (con != null) {
    
            }            
        } catch (SQLException ex) {
            Logger.getLogger(DBContext.class.getName()).log(Level.SEVERE, "Failed to connect during main method test.", ex);
        }
    }
}