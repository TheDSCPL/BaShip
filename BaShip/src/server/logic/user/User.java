package server.logic.user;

import java.sql.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import server.ServerMain;
import server.conn.Client;
import sharedlib.exceptions.UserMessageException;

public class User {

    public static boolean isUsernameAvailable(String username) throws SQLException {
        PreparedStatement stmt = ServerMain.db.getConn().prepareStatement(
                "SELECT COUNT(*) FROM users WHERE username = ?"
        );
        stmt.setString(1, username);

        ResultSet result = stmt.executeQuery(username);
        result.next();
        return result.getInt(1) == 0;
    }

    public static User register(Client client, String username, String passwordHash) throws SQLException {
        PreparedStatement stmt = ServerMain.db.getConn().prepareStatement(
                "INSERT INTO users VALUES (DEFAULT, ?, ?) RETURNING (uid)"
        );
        stmt.setString(1, username);
        stmt.setString(2, passwordHash);

        ResultSet r = stmt.executeQuery();
        r.next();
        Long id = r.getLong(1);
        
        loggedInUsers.put(id, client);
        return new User(id, username);
    }

    public static User login(Client client, String username, String passwordHash) throws SQLException, UserMessageException {
        PreparedStatement stmt = ServerMain.db.getConn().prepareStatement(
                "SELECT uid FROM users WHERE username = ? AND password = ?"
        );
        stmt.setString(1, username);
        stmt.setString(2, passwordHash);

        ResultSet r = stmt.executeQuery();
        if (r.next()) {
            Long id = r.getLong(1);

            if (loggedInUsers.containsKey(id)) {
                throw new UserMessageException("Already logged in");
            }
            else {
                loggedInUsers.put(id, client);
                return new User(id, username);
            }
        }
        else {
            throw new UserMessageException("Invalid login credentials");
        }
    }
    
    public static void logout(Client client) {
        loggedInUsers.values().remove(client);
    }

    private static final Map<Long, Client> loggedInUsers = new ConcurrentHashMap<>();

    public final long id;
    public final String username;

    public User(long id, String username) {
        this.id = id;
        this.username = username;
    }
}
