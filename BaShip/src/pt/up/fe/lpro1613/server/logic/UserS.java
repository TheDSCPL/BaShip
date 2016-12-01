package pt.up.fe.lpro1613.server.logic;

import java.sql.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import pt.up.fe.lpro1613.server.conn.Client;
import pt.up.fe.lpro1613.server.database.UserDB;
import pt.up.fe.lpro1613.server.logic.game.GameS;
import pt.up.fe.lpro1613.sharedlib.exceptions.ConnectionException;
import pt.up.fe.lpro1613.sharedlib.exceptions.UserMessageException;
import pt.up.fe.lpro1613.sharedlib.structs.Message;
import pt.up.fe.lpro1613.sharedlib.structs.UserInfo;
import pt.up.fe.lpro1613.sharedlib.structs.UserInfo.Status;

/**
 * Class responsible for managing the state of the users on the server. Supports
 * basic user operations like login/register/logout and also provides access to
 * various user-related info.
 *
 * @author Alex
 */
public class UserS {

    private static final Map<Long, Client> loginsID = new ConcurrentHashMap<>();
    private static final Map<Client, Long> loginsClient = new ConcurrentHashMap<>();

    /**
     * TODO: JAVADOC
     * @param username
     * @return
     * @throws SQLException 
     */
    public static boolean isUsernameAvailable(String username) throws SQLException {
        return UserDB.isUsernameAvailable(username);
    }

    /**
     * TODO: JAVADOC
     * @param client
     * @param username
     * @param passwordHash
     * @return
     * @throws SQLException 
     */
    public static UserInfo register(Client client, String username, String passwordHash) throws SQLException {
        UserInfo user = UserDB.register(username, passwordHash);
        loginsID.put(user.id, client);
        loginsClient.put(client, user.id);
        return user;
    }

    /**
     * TODO: JAVADOC
     * @param client
     * @param username
     * @param passwordHash
     * @return
     * @throws SQLException
     * @throws UserMessageException 
     */
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

    /**
     * TODO: JAVADOC
     * @param client 
     */
    public static void logout(Client client) {
        if (isClientLoggedIn(client)) {
            loginsID.remove(loginsClient.remove(client));
        }
    }

    /**
     * Called by the {@code Client} class whenever a client is disconnected.
     * @param client The client that disconnected
     */
    public static void clientDisconnected(Client client) {
        logout(client);
    }

    /**
     * TODO: JAVADOC
     * @param client
     * @return 
     */
    public static boolean isClientLoggedIn(Client client) {
        return loginsClient.containsKey(client);
    }

    /**
     * TODO: JAVADOC
     * @param userID
     * @return 
     */
    public static boolean isUserLoggedIn(Long userID) {
        return loginsID.containsKey(userID);
    }

    /**
     * TODO: JAVADOC
     * @param id
     * @return 
     */
    public static Client clientFromID(Long id) {
        return loginsID.get(id);
    }

    /**
     * TODO: JAVADOC
     * @param c
     * @return 
     */
    public static Long idFromClient(Client c) {
        return loginsClient.get(c);
    }

    /**
     * TODO: JAVADOC
     * @param userID
     * @return 
     */
    public static Status getUserStatus(Long userID) {
        if (isUserLoggedIn(userID)) {
            if (GameS.isClientPlaying(clientFromID(userID))) {
                return Status.Playing;
            }
            else if (GameS.isClientWaiting(clientFromID(userID))) {
                return Status.Waiting;
            }

            return Status.Online;
        }

        return Status.Offline;
    }

    /**
     * TODO: JAVADOC
     * @param c
     * @return 
     */
    public static String usernameFromClient(Client c) {
        try {
            return UserDB.getUsernameFromID(idFromClient(c));
        }
        catch (SQLException ex) {
            Logger.getLogger(UserS.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    static void distributeGlobalMessage(Message msg) {
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
