package server.database;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.ServerMain;

public class Database {
    static {
        try {
            DriverManager.registerDriver(new org.postgresql.Driver());
        }
        catch (SQLException ex) {
            Logger.getLogger(ServerMain.class.getName()).log(Level.SEVERE, "Could not find PostgreSQL driver -> exiting", ex);
            System.exit(-1);
        }
    }

    public final String databaseURL;
    private Connection conn;

    public Database(String databaseURL) {
        this.databaseURL = databaseURL;
    }

    public void connect(String username, String password) {
        try {
            conn = DriverManager.getConnection(databaseURL, username, password);
            System.out.println("Connected to SQL database");
        }
        catch (SQLException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Could not connect to SQL database -> exiting", ex);
            System.exit(-1);
        }
        
    }
    
    /*public void set(String preparedStatement, Object... values) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(preparedStatement);
        ResultSet rs = ps.executeQuery();
        
        rs.
    }

    public void test() throws SQLException {
        Statement stmt = conn.createStatement();
        String sql = "";
        stmt.executeUpdate(sql);
    }*/
    
    public Connection getConn() {
        return conn;
    }
}
