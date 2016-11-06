/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.sql.*;

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
            Main.exit(ex);
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
