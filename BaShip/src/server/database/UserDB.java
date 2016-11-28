package server.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import server.ServerMain;
import server.logic.UserS;
import sharedlib.tuples.UserInfo;
import sharedlib.tuples.UserSearch;

public class UserDB {

    public static boolean isUsernameAvailable(String username) throws SQLException {
        PreparedStatement stmt = ServerMain.db.getConn().prepareStatement(
                "SELECT COUNT(*) FROM users WHERE username = ?"
        );
        stmt.setString(1, username);

        ResultSet result = stmt.executeQuery(username);
        result.next();
        return result.getInt(1) == 0;
    }
    
    public static UserInfo register(String username, String passwordHash) throws SQLException {
        PreparedStatement stmt = ServerMain.db.getConn().prepareStatement(
                "INSERT INTO users VALUES (DEFAULT, ?, ?) RETURNING (uid)"
        );
        stmt.setString(1, username);
        stmt.setString(2, passwordHash);

        ResultSet r = stmt.executeQuery();
        r.next();
        Long id = r.getLong(1);

        return new UserInfo(id, username);
    }

    public static Long verifyLoginAndReturnUserID(String username, String passwordHash) throws SQLException {
        PreparedStatement stmt = ServerMain.db.getConn().prepareStatement(
                "SELECT uid FROM users WHERE username = ? AND password = ?"
        );
        stmt.setString(1, username);
        stmt.setString(2, passwordHash);

        ResultSet r = stmt.executeQuery();
        if (r.next()) {
            return r.getLong(1);
        }

        return null;
    }

    public static String getUsernameFromID(long id) throws SQLException {
        PreparedStatement stmt = ServerMain.db.getConn().prepareStatement(
                "SELECT username FROM users WHERE uid = ?"
        );
        stmt.setLong(1, id);

        ResultSet r = stmt.executeQuery();

        if (r.next()) {
            return r.getString(1);
        }
        else {
            return null;
        }
    }

    public static List<UserInfo> getUserList(UserSearch s) throws SQLException {
        PreparedStatement stmt = ServerMain.db.getConn().prepareStatement(
                "SELECT uid, username, rank, ngames, nwins, nshots "
                + "FROM users JOIN user_ranks USING(uid) JOIN user_stats USING(uid) "
                + "WHERE username LIKE ? "
                + "ORDER BY ? LIMIT ?"
        );
        stmt.setString(1, "%" + s.usernameFilter + "%");
        stmt.setInt(2, s.orderByColumn);
        stmt.setInt(3, s.rowLimit);

        ResultSet results = stmt.executeQuery();

        List<UserInfo> users = new ArrayList<>();

        while (results.next()) {
            long id = results.getLong(1);
            users.add(
                    new UserInfo(
                            id, results.getString(2), null,
                            results.getInt(3), results.getInt(4), results.getInt(5),
                            results.getInt(6), UserS.getUserStatus(id) // TODO: filter by online only
                    )
            );
        }

        return users;
    }

}
