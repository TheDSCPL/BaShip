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

    public static Message saveMessage(long gameID, int playerN, String message) throws SQLException {
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
            return new Message(mssgID, null, null, timestamp, message);
        }
        finally {
            Database.close(conn, stmt);
        }
    }

    public static List<Message> getMessages(long gameID) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            String query
                    = "SELECT mssgid, player, timestamp, txt "
                      + "FROM gamechat "
                      + "WHERE gmid = ?";

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
                                "XXXX", // TODO: get correct username
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
