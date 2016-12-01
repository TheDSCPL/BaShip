package pt.up.fe.lpro1613.server.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import pt.up.fe.lpro1613.sharedlib.structs.Ship;

/**
 * Collection of static methods that access, set and return information present
 * on the table "ships" of the database.
 *
 * @author Alex
 */
public class ShipDB {

    /**
     * Save ship layout for a certain game and player to the database.
     * @param gameID
     * @param playerN
     * @param ships
     * @throws SQLException 
     */
    public static void saveShipPositions(long gameID, int playerN, List<Ship> ships) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = Database.getConn();
            stmt = conn.prepareStatement("INSERT INTO ships VALUES (DEFAULT, ?, ?, ?, ?, ?, ?)");

            for (Ship s : ships) {
                stmt.setLong(1, gameID);
                stmt.setInt(2, playerN);
                stmt.setInt(3, s.size);
                stmt.setInt(4, s.posx);
                stmt.setInt(5, s.posy);
                stmt.setBoolean(6, s.vertical);
                stmt.addBatch();
            }

            stmt.executeBatch();
        }
        finally {
            Database.close(conn, stmt);
        }
    }

}
