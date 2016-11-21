package server.logic;

import java.sql.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.conn.*;
import server.database.GlobalChatDB;
import server.database.UserDB;
import sharedlib.exceptions.*;
import sharedlib.tuples.Message;
import sharedlib.tuples.UserInfo;

// TODO: UserS status on database?
public class UserS {

    private static final Map<Long, Client> loginsID = new ConcurrentHashMap<>();
    private static final Map<Client, Long> loginsClient = new ConcurrentHashMap<>();

    public static boolean isUsernameAvailable(String username) throws SQLException {
        return UserDB.isUsernameAvailable(username);
    }

    public static UserInfo register(Client client, String username, String passwordHash) throws SQLException {
        UserInfo user = UserDB.register(username, passwordHash);
        loginsID.put(user.id, client);
        loginsClient.put(client, user.id);
        return user;
    }

    public static UserInfo login(Client client, String username, String passwordHash) throws SQLException, UserMessageException {
        Long id = UserDB.verifyLoginAndReturnUserID(username, passwordHash);

        if (id != null) {
            if (isUserLoggedIn(id)) {
                throw new UserMessageException("Already logged in");
            }
            else {
                loginsID.put(id, client);
                loginsClient.put(client, id);
                return new UserInfo(id, username);
            }
        }
        else {
            throw new UserMessageException("Invalid login credentials");
        }
    }

    public static void logout(Client client) {
        if (isClientLoggedIn(client)) {
            loginsID.remove(loginsClient.remove(client));
        }
    }

    public static void sendGlobalMessage(Client client, String message) {
        if (isClientLoggedIn(client)) {
            Message msg = GlobalChatDB.sendGlobalMessage(loginsClient.get(client), message);
            UserS.distributeGlobalMessage(msg);
        }
        else {
            Logger.getLogger(UserS.class.getName()).log(Level.SEVERE, "Client tried to send global message without being logged in");
        }
    }

    public static boolean isClientLoggedIn(Client client) {
        return loginsClient.containsKey(client);
    }

    public static boolean isUserLoggedIn(Long userID) {
        return loginsID.containsKey(userID);
    }

    private static void distributeGlobalMessage(Message msg) {
        for (Client client : loginsID.values()) {
            try {
                client.informAboutGlobalMessage(msg);
            }
            catch (ConnectionException ex) {
                Logger.getLogger(UserS.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
