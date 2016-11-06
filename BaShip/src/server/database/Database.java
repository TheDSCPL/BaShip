package server.database;

import java.sql.*;
import server.ServerMain;

/**
 *
 * @author luisp
 */
public class Database {
    // Create
    // Connect
    // Begin transaction
    // Commit transaction
    // Execute query
    // Execute query with results
    // Parse reslts, perhaps?

    static {
        try {
            DriverManager.registerDriver(new org.postgresql.Driver());
        }
        catch (Exception ex) {
            ServerMain.exit(ex);
        }
    }

    public final String databaseURL;
    private Connection conn;

    public Database(String databaseURL) {
        this.databaseURL = databaseURL;
    }

    public void connect(String username, String password) throws SQLException {
        conn = DriverManager.getConnection(databaseURL, username, password);
    }
    
    

    public void test() throws SQLException {
        Statement stmt = conn.createStatement();
        String sql = "CREATE TABLE TEST "
                     + "(ID INT PRIMARY KEY     NOT NULL,"
                     + " NAME           TEXT    NOT NULL, "
                     + " AGE            INT     NOT NULL, "
                     + " ADDRESS        CHAR(50), "
                     + " SALARY         REAL)";
        stmt.executeUpdate(sql);
    }
}
