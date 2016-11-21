package server.database;

import java.util.Date;
import sharedlib.tuples.Message;

public class GlobalChatDB {

    public static Message sendGlobalMessage(Long userID, String message) {
        return new Message((long)0, userID, new Date(), message); // TODO: do SQL query to insert message
    }
}
