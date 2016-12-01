package server.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MoveDB {

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
