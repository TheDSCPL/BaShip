package server.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import sharedlib.structs.Ship;

/**
 * Collection of static methods that access, set and return information present
 * on the table "ships" of the database.
 *
 * @author Alex
 */
public class ShipDB {

    /**
     * Save ship layout for a certain game and player to the database.
     *
     * @param gameID
     * @param playerN
     * @param ships
     * @throws SQLException
     */
    public static void saveShipPositions(long gameID, int playerN, List<Ship> ships) throws SQLException {
        if (Database.testing) {
            return;
        }
        
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

    /**
     * Get the ship positions that a player chooses in a certain game
     * @param gameID ID of the game
     * @param playerN Player to know the ship positions
     * @return A list with the positions of every ship
     * @throws SQLException
     */
    public static List<Ship> getShipPositions(long gameID, int playerN) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            String query
                    = "SELECT sid, posx, posy, size, vertical "
                      + "FROM ships "
                      + "WHERE gmid = ? AND player = ?";

            conn = Database.getConn();
            stmt = conn.prepareStatement(query);
            stmt.setLong(1, gameID);
            stmt.setInt(2, playerN);

            rs = stmt.executeQuery();

            List<Ship> ships = new ArrayList<>();
            while (rs.next()) {

                ships.add(
                        new Ship(
                                rs.getInt("posx"),
                                rs.getInt("posy"),
                                rs.getInt("size"),
                                rs.getBoolean("vertical")
                        )
                );
            }

            return ships;
        }
        finally {
            Database.close(conn, stmt, rs);
        }
    }

}
