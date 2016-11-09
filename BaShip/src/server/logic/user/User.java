package server.logic.user;

import java.sql.*;
import server.ServerMain;

public class User {

    /*public static boolean isUsernameAvailable(String username) throws SQLException {
        PreparedStatement stmt = ServerMain.db.getConn().prepareStatement(
                "SELECT COUNT(*) FROM users WHERE username = ?"
        );
        stmt.setString(1, username);

        ResultSet result = stmt.executeQuery(username);
        result.next();
        return result.getInt(1) == 0;
    }*/

    public static User register(String username, String passwordHash) throws SQLException {
        PreparedStatement stmt = ServerMain.db.getConn().prepareStatement(
                "INSERT INTO users VALUES (DEFAULT, ?, ?) RETURNING (uid)"
        );
        stmt.setString(1, username);
        stmt.setString(2, passwordHash);

        ResultSet r = stmt.executeQuery();
        r.next();
        return new User(r.getLong(1), username);
    }

    public static User login(String username, String passwordHash) throws SQLException {
        PreparedStatement stmt = ServerMain.db.getConn().prepareStatement(
                "SELECT uid FROM users WHERE username = ? AND password = ?"
        );
        stmt.setString(1, username);
        stmt.setString(2, passwordHash);

        ResultSet r = stmt.executeQuery();
        if (r.next()) {
            return new User(r.getLong(1), username);
        }
        else {
            throw new SQLException("Inavlid login credentials");
        }
    }

    public final long id;
    public final String username;

    public User(long id, String username) {
        this.id = id;
        this.username = username;
    }
}
