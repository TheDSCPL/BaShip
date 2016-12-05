package server.database;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.ServerMain;
import static server.ServerMain.prefs;
import server.other.PrefsKey;

/**
 * Creates connections to the SQL database when needed and contains helper
 * methods to deal with SQL resources deallocation.
 *
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

    private static BoneCP connectionPool;

    /**
     * Connect to the SQL database and set up the thread pool. Needs to be
     * called before any attempt to run SQl queries.
     */
    public static void initialize() {
        System.out.print("Connecting to the database... ");

        BoneCPConfig config = new BoneCPConfig();
        config.setJdbcUrl(prefs.getS(PrefsKey.DatabaseURL));
        config.setUsername(prefs.getS(PrefsKey.DatabaseUsername));
        config.setPassword(prefs.getS(PrefsKey.DatabasePassword));
        config.setMinConnectionsPerPartition(5);
        config.setPartitionCount(1);

        try {
            connectionPool = new BoneCP(config);
            System.out.println("done.");
        }
        catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static Connection createConnection() throws SQLException {
        return connectionPool.getConnection();
        /*return DriverManager
                .getConnection(prefs.getS(PrefsKey.DatabaseURL),
                               prefs.getS(PrefsKey.DatabaseUsername),
                               prefs.getS(PrefsKey.DatabasePassword)
                );*/
    }

    /**
     * Get a connection to the database. Right now, a new connection is created
     * every time this method is called. This means that calling
     * <code>Database::close</code> on the connection handler after using is is
     * <b>essential</b>. This behavior may change in the future if this is
     * re-implemented using connection pools.
     *
     * @return A new connection object to the SQL database.
     * @throws SQLException
     */
    public static Connection getConn() throws SQLException {
        return createConnection();
    }

    /**
     * Close all SQL database objects given.
     *
     * @param dbObjs An array of objects of types <code>Statement</code>,
     * <code>ResultSet</code> and <code>Connection</code>. Other types are not
     * accepted. The values may be null.
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
