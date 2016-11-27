package server.database;

import java.sql.SQLException;
import java.util.Date;
import sharedlib.tuples.Message;

public class GlobalChatDB {

    public static Message sendGlobalMessage(Long userID, String message) throws SQLException {
        // TODO: Register on DB
        return new Message((long)0, userID, UserDB.getUsernameFromID(userID), new Date(), message); // TODO: do SQL query to insert message
    }
}
