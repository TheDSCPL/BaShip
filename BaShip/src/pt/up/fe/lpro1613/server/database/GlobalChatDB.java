package pt.up.fe.lpro1613.server.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import pt.up.fe.lpro1613.sharedlib.tuples.Message;

/**
 * Collection of static methods that access, set and return information present
 * on the table "globalchat" of the database.
 *
 * @author Alex
 */
public class GlobalChatDB {

    /**
     * Save a global message on the database. The message's timestamp is
     * automatically set to the current time.
     *
     * @param userID The id of the user who sent the message.
     * @param message The string of the message.
     * @return The message object, equivalent to the row on the database, representing this message
     * @throws SQLException
     */
    public static Message saveGlobalMessage(Long userID, String message) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = Database.getConn();
            stmt = conn.prepareStatement("INSERT INTO globalchat VALUES (DEFAULT, ?, NOW(), ?) RETURNING mssgid, timestamp");

            stmt.setLong(1, userID);
            stmt.setString(2, message);

            rs = stmt.executeQuery();
            rs.next();

            Long mssgID = rs.getLong("mssgid");
            Date timestamp = rs.getTimestamp("timestamp");
            return new Message(mssgID, userID, UserDB.getUsernameFromID(userID), timestamp, message);
        }
        finally {
            Database.close(conn, stmt, rs);
        }
    }

}
