package server.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Collection of static methods that access, set and return information present
 * on the table "moves" of the database.
 *
 * @author Alex
 */
public class MoveDB {

    /**
     * Save a move made by a player on a game on the database.
     *
     * @param gameID The game ID where the player made that move.
     * @param playerN The player that made the move. Can be either 1 or 2. Other
     * values raise an SQLException.
     * @param moveIndex The move number for that game (for both players of the
     * game).
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

    public static int getTotalMoveCount(long gameID) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = Database.getConn();
            stmt = conn.prepareStatement("SELECT COUNT(moveid) FROM moves WHERE gmid = ?");            
            stmt.setLong(1, gameID);

            rs = stmt.executeQuery();
            rs.next();
            return rs.getInt(1);
        }
        finally {
            Database.close(conn, stmt);
        }
    }
}
