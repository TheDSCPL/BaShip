package server.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import server.ServerMain;
import sharedlib.UserM;

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

    public static UserM register(String username, String passwordHash) throws SQLException {
        PreparedStatement stmt = ServerMain.db.getConn().prepareStatement(
                "INSERT INTO users VALUES (DEFAULT, ?, ?) RETURNING (uid)"
        );
        stmt.setString(1, username);
        stmt.setString(2, passwordHash);

        ResultSet r = stmt.executeQuery();
        r.next();
        Long id = r.getLong(1);
        
        return new UserM(id, username);
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
    
        /**
     *
     * @param onlineOnly
     * @param usernameFilter
     * @param orderByColumn Starts at column 1
     * @param rowLimit
     * @return
     * @throws SQLException
     */
    public static List<UserM> getUserList(boolean onlineOnly, String usernameFilter, int orderByColumn, int rowLimit) throws SQLException {
        PreparedStatement stmt = ServerMain.db.getConn().prepareStatement(
                "SELECT uid, username, rank, ngames, nwins, nshots "
                + "FROM users JOIN user_ranks USING(uid) JOIN user_stats USING(uid) "
                + "WHERE username LIKE ? "
                + "ORDER BY ? LIMIT ?"
        );
        stmt.setString(1, "%" + usernameFilter + "%");
        stmt.setInt(2, orderByColumn);
        stmt.setInt(3, rowLimit);
        
        ResultSet results = stmt.executeQuery();

        List<UserM> users = new ArrayList<>();

        while (results.next()) {
            long id = results.getLong(1);
            UserM.Status status = UserM.Status.Online; // TODO: get correct status from user id
            users.add(new UserM(id, results.getString(2), results.getInt(3), results.getInt(4), results.getInt(5), results.getInt(6), status));
        }

        return users;
    }

}
