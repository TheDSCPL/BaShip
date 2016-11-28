package server.database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import server.ServerMain;

public class MoveDB {
    
    public static void saveMove(long gameID, int playerN, int moveIndex) throws SQLException {
        PreparedStatement stmt = ServerMain.db.getConn().prepareStatement(
                "INSERT INTO moves VALUES (DEFAULT, ?, ?, ?)"
        );
        stmt.setLong(1, gameID);
        stmt.setInt(2, playerN);
        stmt.setInt(3, moveIndex);
        stmt.executeUpdate();
    }
    
}
