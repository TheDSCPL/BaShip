package server.logic.user;

import java.sql.*;
import java.util.*;
import java.util.concurrent.*;
import server.conn.*;
import server.database.UserDB;
import sharedlib.UserM;
import sharedlib.exceptions.*;

// TODO: UserS status on database?
public class UserS {
    
    public static boolean isUsernameAvailable(String username) throws SQLException {
        return server.database.UserDB.isUsernameAvailable(username);
    }

    public static UserM register(Client client, String username, String passwordHash) throws SQLException {
        UserM user = UserDB.register(username, passwordHash);
        loggedInUsers.put(user.id, client);
        return user;
    }

    public static UserM login(Client client, String username, String passwordHash) throws SQLException, UserMessageException {
        Long id = UserDB.verifyLoginAndReturnUserID(username, passwordHash);
        
        if (id != null) {
            if (loggedInUsers.containsKey(id)) {
                throw new UserMessageException("Already logged in");
            }
            else {
                loggedInUsers.put(id, client);
                return new UserM(id, username);
            }
        }
        else {
            throw new UserMessageException("Invalid login credentials");
        }
    }
    
    public static List<UserM> getUserList(boolean onlineOnly, String usernameFilter, int orderByColumn, int rowLimit) throws SQLException {
        return UserDB.getUserList(onlineOnly, usernameFilter, orderByColumn, rowLimit);
    }

    public static void logout(Client client) {
        loggedInUsers.values().remove(client);
    }    
    
    private static final Map<Long, Client> loggedInUsers = new ConcurrentHashMap<>();
}
