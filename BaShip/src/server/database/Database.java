package server.database;

import java.sql.*;

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
        catch (SQLException ex) {
            ex.printStackTrace();
            System.exit(-1);
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
