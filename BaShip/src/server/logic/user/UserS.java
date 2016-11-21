package server.logic.user;

import java.sql.*;
import java.util.*;
import java.util.concurrent.*;
import server.conn.*;
import server.database.UserDB;
import sharedlib.exceptions.*;
import sharedlib.tuples.UserInfo;

// TODO: UserS status on database?
public class UserS {

    private static final Map<Long, Client> loggedInUsers = new ConcurrentHashMap<>();

    public static boolean isUsernameAvailable(String username) throws SQLException {
        return server.database.UserDB.isUsernameAvailable(username);
    }

    public static UserInfo register(Client client, String username, String passwordHash) throws SQLException {
        UserInfo user = UserDB.register(username, passwordHash);
        loggedInUsers.put(user.id, client);
        return user;
    }

    public static UserInfo login(Client client, String username, String passwordHash) throws SQLException, UserMessageException {
        Long id = UserDB.verifyLoginAndReturnUserID(username, passwordHash);

        if (id != null) {
            if (loggedInUsers.containsKey(id)) {
                throw new UserMessageException("Already logged in");
            }
            else {
                loggedInUsers.put(id, client);
                return new UserInfo(id, username);
            }
        }
        else {
            throw new UserMessageException("Invalid login credentials");
        }
    }

    public static void logout(Client client) {
        loggedInUsers.values().remove(client);
    }
    
}
