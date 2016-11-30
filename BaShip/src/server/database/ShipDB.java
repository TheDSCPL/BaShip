package server.database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import server.ServerMain;
import server.logic.game.Ship;

public class ShipDB {

    public static void saveShipPositions(long gameID, int playerN, List<Ship> ships) throws SQLException {
        PreparedStatement stmt = ServerMain.db.getConn().prepareStatement(
                "INSERT INTO ships VALUES (DEFAULT, ?, ?, ?, ?, ?, ?)"
        );

        for (Ship ship : ships) {
            stmt.setLong(1, gameID);
            stmt.setInt(2, playerN);
            stmt.setInt(3, ship.size);
            stmt.setInt(4, ship.posx);
            stmt.setInt(5, ship.posy);
            stmt.setBoolean(6, ship.vertical);
            stmt.addBatch();
        }

        stmt.executeBatch();
    }

}
