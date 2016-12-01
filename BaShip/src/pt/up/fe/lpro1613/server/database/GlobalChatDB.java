package pt.up.fe.lpro1613.server.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import pt.up.fe.lpro1613.sharedlib.tuples.Message;

public class GlobalChatDB {

    public static Message sendGlobalMessage(Long userID, String message) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = Database.getConn();
            stmt = conn.prepareStatement(
                    "INSERT INTO globalchat VALUES (DEFAULT, ?, NOW(), ?) RETURNING mssgid, timestamp"
            );

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
