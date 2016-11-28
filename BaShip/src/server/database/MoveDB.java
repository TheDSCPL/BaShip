package server.database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import server.ServerMain;

public class MoveDB {
    
    public static void saveMove(long gameID, int playerN, int moveIndex) throws SQLException {
        PreparedStatement stmt = ServerMain.db.getConn().prepareStatement(
                "INSERT INTO moves VALUES (DEFAULT, ?, ?) RETURNING (uid)"
        );
        stmt.setString(1, username);
        stmt.setString(2, passwordHash);

        ResultSet r = stmt.executeQuery();
        r.next();
        Long id = r.getLong(1);

        return new UserInfo(id, username);
    }
    
}
