package pt.up.fe.lpro1613.server.logic;

import java.sql.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import pt.up.fe.lpro1613.server.conn.Client;
import pt.up.fe.lpro1613.server.database.GlobalChatDB;
import pt.up.fe.lpro1613.server.database.UserDB;
import pt.up.fe.lpro1613.sharedlib.exceptions.ConnectionException;
import pt.up.fe.lpro1613.sharedlib.exceptions.UserMessageException;
import pt.up.fe.lpro1613.sharedlib.tuples.Message;
import pt.up.fe.lpro1613.sharedlib.tuples.UserInfo;
import pt.up.fe.lpro1613.sharedlib.tuples.UserInfo.Status;

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

    public static void sendGlobalMessage(Client client, String message) throws SQLException {
        if (isClientLoggedIn(client)) {
            Message msg = GlobalChatDB.sendGlobalMessage(loginsClient.get(client), message);
            UserS.distributeGlobalMessage(msg);
        }
        else {
            Logger.getLogger(UserS.class.getName()).log(Level.SEVERE, "Client tried to send global message without being logged in");
        }
    }

    public static void clientDisconnected(Client client) {
        logout(client);
    }

    public static boolean isClientLoggedIn(Client client) {
        return loginsClient.containsKey(client);
    }

    public static boolean isUserLoggedIn(Long userID) {
        return loginsID.containsKey(userID);
    }

    public static Client clientFromID(Long id) {
        return loginsID.get(id);
    }

    public static Long idFromClient(Client c) {
        return loginsClient.get(c);
    }

    public static Status getUserStatus(Long userID) {
        if (isUserLoggedIn(userID)) {
            if (GameS.isClientPlaying(clientFromID(userID))) {
                return Status.Playing;
            }
            else {
                if (GameS.isClientWaiting(clientFromID(userID))) {
                    return Status.Waiting;
                }
            }

            return Status.Online;
        }

        return Status.Offline;
    }

    public static String usernameFromClient(Client c) {
        try {
            return UserDB.getUsernameFromID(idFromClient(c));
        }
        catch (SQLException ex) {
            Logger.getLogger(UserS.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
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
