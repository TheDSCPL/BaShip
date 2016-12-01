package server.database;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.ServerMain;
import static server.ServerMain.prefs;
import server.other.PrefsKey;

/**
 * Creates connections to the SQL database when needed and contains helper methods
 * to deal with SQL resources deallocation.
 * @author Alex
 */
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

    private static Connection createConnection() throws SQLException {
        return DriverManager
                .getConnection(prefs.getS(PrefsKey.DatabaseURL),
                               prefs.getS(PrefsKey.DatabaseUsername),
                               prefs.getS(PrefsKey.DatabasePassword)
                );

    }

    /*private static final ThreadLocal<ConnectionHolder> connections = new ThreadLocal<ConnectionHolder>() {
        @Override
        protected ConnectionHolder initialValue() {
            try {
                return new ConnectionHolder(createConnection());
            }
            catch (SQLException ex) {
                return new ConnectionHolder(ex);
            }
        }
    };*/

    /**
     * Get a connection to the database. Right now, a new connection is created
     * every time this method is called. This means that calling <code>Database::close</code>
     * on the connection handler after using is is <b>essential</b>. This behavior
     * may change in the future if this is re-implemented using connection pools.
     * @return A new connection object to the SQL database.
     * @throws SQLException 
     */
    public static Connection getConn() throws SQLException {
        /*ConnectionHolder ch = connections.get();
        if (ch.conn == null) {
            throw ch.ex;
        }

        return ch.conn;*/
        return createConnection();
    }

    /**
     * Close all SQL database objects given.
     * @param dbObjs An array of objects of types <code>Statement</code>,
     * <code>ResultSet</code> and <code>Connection</code>. Other types are not accepted.
     * The values may be null.
     */
    public static void close(Object... dbObjs) {
        for (Object obj : dbObjs) {
            close(obj);
        }
    }

    private static void close(Object dbObj) {
        if (dbObj == null) {
            return;
        }
        if ((dbObj instanceof Statement)) {
            close((Statement) dbObj);
        }
        else if ((dbObj instanceof ResultSet)) {
            close((ResultSet) dbObj);
        }
        else if ((dbObj instanceof Connection)) {
            close((Connection) dbObj);
        }
        else {
            throw new IllegalArgumentException("Close attempted on unrecognized database object");
        }
    }

    private static void close(Statement stmt) {
        if (stmt == null) {
            return;
        }
        try {
            stmt.close();
        }
        catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void close(ResultSet rs) {
        if (rs == null) {
            return;
        }
        try {
            rs.close();
        }
        catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void close(Connection conn) {
        if (conn == null) {
            return;
        }
        try {
            if (!conn.isClosed()) {
                conn.close();
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

/*final class ConnectionHolder {

    final Connection conn;
    final SQLException ex;

    ConnectionHolder(Connection conn) {
        this.conn = conn;
        ex = null;
    }

    ConnectionHolder(SQLException ex) {
        this.ex = ex;
        conn = null;
    }

    @Override
    @SuppressWarnings("FinalizeDeclaration")
    protected void finalize() throws Throwable {
        //super.finalize();
        Database.close(conn);
        System.out.println("Connection closed");
    }

}*/
