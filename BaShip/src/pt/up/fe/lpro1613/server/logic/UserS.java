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
     * Register the user and, if that is successful (no exception is thrown),
     * login the user automatically.
     *
     * @param client
     * @param username
     * @param passwordHash
     * @return A UserInfo class with the #id and #username fields non-null
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
     *
     * @param client
     * @param username
     * @param passwordHash
     * @return
     * @throws SQLException
     * @throws UserMessageException
     */
    public static UserInfo login(Client client, String username, String passwordHash) throws SQLException, UserMessageException {
        Long id = UserDB.verifyLogin(username, passwordHash);

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
     * Logout the user. If the client is not logged-in, this method does
     * nothing.
     *
     * @param client
     */
    public static void logout(Client client) {
        if (isClientLoggedIn(client)) {
            loginsID.remove(loginsClient.remove(client));
        }
    }

    /**
     * Inform this class that a client disconnected. If the user is logged-in,
     * logout the user. Called by the {@code Client} class whenever a client is
     * disconnected.
     *
     * @param client The client that disconnected
     */
    public static void clientDisconnected(Client client) {
        logout(client);
    }

    /**
     * @param client
     * @return True if a client is currently logged-in.
     */
    public static boolean isClientLoggedIn(Client client) {
        return loginsClient.containsKey(client);
    }

    /**
     * @param userID
     * @return True if the user with the given user id is currently logged-in.
     */
    public static boolean isUserLoggedIn(Long userID) {
        return loginsID.containsKey(userID);
    }

    /**
     * @param id
     * @return The {@code Client} object that is logged-in with the user with
     * the specified user id. Returns null if the user for that id is not
     * logged-in and therefore is not assigned to a specific {@code Client}.
     */
    public static Client clientFromID(Long id) {
        return loginsID.get(id);
    }

    /**
     * @param c
     * @return The id of the user for the given {@code Client} object, if the
     * client is logged-in. If not, returns null.
     */
    public static Long idFromClient(Client c) {
        return loginsClient.get(c);
    }

    /**
     * Each user has a status, as defined in {@code UserInfo.Status}, which can
     * be accessed through this method.
     *
     * @param userID
     * @return The status of the user. Never null
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
     * @param c
     * @return The username of the user for the given {@code Client} object, if
     * the client is logged-in. If not, returns null.
     */
    public static String usernameFromClient(Client c) {
        try {
            Long id = idFromClient(c);
            if (id != null) {
                return UserDB.getUsernameFromID(id);
            }
            else {
                return null;
            }
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
