package pt.up.fe.lpro1613.server.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Collection of static methods that access, set and return information present
 * on the table "moves" of the database.
 *
 * @author Alex
 */
public class MoveDB {

    /**
     * XXX
     * @param gameID
     * @param playerN
     * @param moveIndex
     * @throws SQLException 
     */
    public static void saveMove(long gameID, int playerN, int moveIndex) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = Database.getConn();
            stmt = conn.prepareStatement(
                    "INSERT INTO moves VALUES (DEFAULT, ?, ?, ?)"
            );
            stmt.setLong(1, gameID);
            stmt.setInt(2, playerN);
            stmt.setInt(3, moveIndex);
            stmt.executeUpdate();
        }
        finally {
            Database.close(conn, stmt);
        }
    }

}
