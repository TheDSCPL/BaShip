package server.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import server.ServerMain;
import sharedlib.tuples.Message;

public class GlobalChatDB {

    public static Message sendGlobalMessage(Long userID, String message) throws SQLException {
        PreparedStatement stmt = ServerMain.db.getConn().prepareStatement(
                "INSERT INTO globalchat VALUES (DEFAULT, ?, NOW(), ?) RETURNING mssgid, timestamp"
        );
        stmt.setLong(1, userID);
        stmt.setString(2, message);

        ResultSet r = stmt.executeQuery();
        r.next();
        
        Long mssgID = r.getLong("mssgid");
        Date timestamp = r.getTimestamp("timestamp");
        return new Message(mssgID, userID, UserDB.getUsernameFromID(userID), timestamp, message);
    }
}
