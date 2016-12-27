package server.logic;

import java.sql.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.conn.Client;
import server.database.UserDB;
import server.logic.game.GameS;
import sharedlib.enums.UserStatus;
import sharedlib.exceptions.ConnectionException;
import sharedlib.exceptions.UserMessageException;
import sharedlib.structs.Message;
import sharedlib.structs.UserInfo;

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
    private static final Map<Client, String> usernamesClient = new ConcurrentHashMap<>();

    /**
     * Register the user and, if that is successful (no exception is thrown),
     * login the user automatically.
     *
     * @param client
     * @param username
     * @param passwordHash
     * @return A UserInfo class with the #id and #username fields non-null
     * @throws sharedlib.exceptions.UserMessageException
     */
    public static UserInfo register(Client client, String username, String passwordHash) throws UserMessageException {
        UserInfo user;

        try {
            if (!UserDB.isUsernameAvailable(username)) {
                throw new UserMessageException("Could not register because username is already in use");
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(UserS.class.getName()).log(Level.SEVERE, "Could not verify if username is already in use or not", ex);
            throw new UserMessageException("Could not register due to database error");
        }

        try {
            user = UserDB.register(username, passwordHash);
        }
        catch (SQLException ex) {
            Logger.getLogger(UserS.class.getName()).log(Level.SEVERE, "Could not register due to database error", ex);
            throw new UserMessageException("Could not register due to database error");
        }

        loginsID.put(user.id, client);
        loginsClient.put(client, user.id);
        usernamesClient.put(client, username);
        return user;
    }

    /**
     * Login the user with the given username and password. This user is then
     * associated to this {@code Client} until the client performs a logout.
     * Also, no other client may login with the same username until this client
     * performs a logout.
     *
     * @param client
     * @param username
     * @param passwordHash
     * @return
     * @throws UserMessageException
     */
    public static UserInfo login(Client client, String username, String passwordHash) throws UserMessageException {
        Long id;
        try {
            id = UserDB.verifyLogin(username, passwordHash);
        }
        catch (SQLException ex) {
            Logger.getLogger(UserS.class.getName()).log(Level.SEVERE, "Could not verify login info on database", ex);
            throw new UserMessageException("Could not login due to database error");
        }

        if (id != null) {
            if (isUserLoggedIn(id)) {
                throw new UserMessageException("Could not login because this account is already logged in");
            }
            else {
                loginsID.put(id, client);
                loginsClient.put(client, id);
                usernamesClient.put(client, username);
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
            usernamesClient.remove(client);
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
    public static Client clientWithUserID(Long id) {
        return loginsID.get(id);
    }

    /**
     * @param c
     * @return The id of the user for the given {@code Client} object, if the
     * client is logged-in. If not, returns null.
     */
    public static Long userIDOfClient(Client c) {
        return loginsClient.get(c);
    }

    /**
     * @param c
     * @return The username of the user for the given {@code Client} object, if
     * the client is logged-in. If not, returns null.
     */
    public static String usernameOfClient(Client c) {
        return usernamesClient.get(c);
    }

    /**
     * Each user has a status, as defined in {@code UserInfo.Status}, which can
     * be accessed through this method.
     *
     * @param userID
     * @return The status of the user. Never null
     */
    public static UserStatus statusOfUser(Long userID) {
        if (isUserLoggedIn(userID)) {
            Client c = clientWithUserID(userID);

            if (GameS.PlayerInfo.isPlaying(c) || GameS.PlayerInfo.isSpectating(c) || GameS.PlayerInfo.isWaitingForPlayer(c) || GameS.PlayerInfo.isReplaying(c)) {
                return UserStatus.Playing;
            }
            else if (GameS.PlayerInfo.isWaitingForGame(c)) {
                return UserStatus.Waiting;
            }

            return UserStatus.Online;
        }

        return UserStatus.Offline;
    }

    static void distributeGlobalMessage(Message msg) {
        for (Client client : loginsID.values()) {
            try {
                client.informAboutGlobalMessage(msg);
            }
            catch (ConnectionException ex) {
                Logger.getLogger(UserS.class.getName()).log(Level.SEVERE, "Could not distribute global message to " + client, ex);
            }
        }
    }
}
