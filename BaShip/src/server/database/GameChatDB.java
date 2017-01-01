package server.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import sharedlib.structs.Message;

/**
 * Collection of static methods that access, set and return information present
 * on the table "gamechat" of the database.
 *
 * @author Alex
 */
public class GameChatDB {

    /**
     * Save a message on the database
     * @param gameID ID of the game where the message has been sent
     * @param playerN Player who sent the message
     * @param message Message sent to be saved
     * @return 
     * @throws SQLException
     */
    public static Message saveMessage(long gameID, int playerN, String message) throws SQLException {
        if (Database.testing) {
            return new Message(1L, 2L, "mock_username", new Date(), message);
        }
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = Database.getConn();
            stmt = conn.prepareStatement("INSERT INTO gamechat VALUES (DEFAULT, ?, ?, NOW(), ?) RETURNING mssgid, timestamp");

            stmt.setLong(1, gameID);
            stmt.setInt(2, playerN);
            stmt.setString(3, message);

            rs = stmt.executeQuery();
            rs.next();

            Long mssgID = rs.getLong("mssgid");
            Date timestamp = rs.getTimestamp("timestamp");
            return new Message(mssgID, null, GameDB.getPlayerUsernamesFromGame(gameID)[playerN - 1], timestamp, message);
        }
        finally {
            Database.close(conn, stmt, rs);
        }
    }

    /**
     * Get all the messages that a game had
     * @param gameID ID of the game
     * @return A list with all the messages that had been sent
     * @throws SQLException
     */
    public static List<Message> getMessages(long gameID) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            String query
                    = "SELECT mssgid, timestamp, txt, CASE WHEN player = 1 THEN u1.username ELSE u2.username END AS username "
                      + "FROM gamechat "
                      + "JOIN games USING(gmid) "
                      + "JOIN users AS u1 ON player1 = u1.uid "
                      + "JOIN users AS u2 ON player2 = u2.uid "
                      + "WHERE gmid = ? "
                      + "ORDER BY timestamp ASC";

            conn = Database.getConn();
            stmt = conn.prepareStatement(query);
            stmt.setLong(1, gameID);

            rs = stmt.executeQuery();

            List<Message> messages = new ArrayList<>();
            while (rs.next()) {
                messages.add(
                        new Message(
                                rs.getLong("mssgid"),
                                gameID,
                                rs.getString("username"),
                                rs.getTimestamp("timestamp"),
                                rs.getString("txt")
                        )
                );
            }

            return messages;
        }
        finally {
            Database.close(conn, stmt, rs);
        }
    }

}
