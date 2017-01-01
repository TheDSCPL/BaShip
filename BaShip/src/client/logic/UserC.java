package client.logic;

import client.ClientMain;
import java.util.List;
import java.util.regex.*;
import sharedlib.exceptions.UserMessageException;
import sharedlib.structs.UserInfo;
import sharedlib.structs.UserSearch;

/**
 * Class responsible for managing the state of the user (logged-in or not) and
 * performing other user-related tasks.
 *
 * @author Alex
 */
public class UserC {

    private static UserInfo loggedInUser;

    /**
     * Login using the specified username and password
     *
     * @param username
     * @param password
     * @return True if login was successful
     */
    public static boolean login(String username, char[] password) {
        if (!ClientMain.checkServerConnection()) {
            return false;
        }
        
        try {
            loggedInUser = ClientMain.server.login(username, password);
        }
        catch (UserMessageException ex) {
            ClientMain.showError(ex.getMessage());
        }
        
        return isLoggedIn();
    }

    /**
     * Register using the specified username and password
     *
     * @param username
     * @param password
     * @return True if register was successful
     */
    public static boolean register(String username, char[] password) {
        if (!ClientMain.checkServerConnection()) {
            return false;
        }
        
        try {
            loggedInUser = ClientMain.server.register(username, password);
        }
        catch (UserMessageException ex) {
            ClientMain.showError(ex.getMessage());
        }

        return isLoggedIn();
    }

    /**
     * Logout the current logged-in user for this client.
     *
     * @return True if logout was successful
     */
    public static boolean logout() {
        if (!ClientMain.checkServerConnection()) {
            return false;
        }
        
        try {
            ClientMain.server.logout();
            loggedInUser = null;
            return true;
        }
        catch (UserMessageException ex) {
            ClientMain.showError(ex.getMessage());
            return false;
        }
    }

    /**
     * @return The ID of the user current logged-in on this client, or null if
     * no user is logged-in
     */
    public static Long getLoggedInUserID() {
        return loggedInUser != null ? loggedInUser.id : null;
    }

    /**
     * @return The username of the user current logged-in on this client, or
     * null if no user is logged-in
     */
    public static String getLoggedInUsername() {
        return loggedInUser != null ? loggedInUser.username : null;
    }

    /**
     * @return True if this client is currently logged-in on the server
     */
    public static boolean isLoggedIn() {
        return loggedInUser != null;
    }

    /**
     * Get a filtered list of all users registered on the server. This list may
     * include all players, online and/or offline.
     *
     * @param us The filter parameters.
     * @return A list of <code>UserInfo</code> objects with all its fields
     * non-null.
     */
    public static List<UserInfo> getUserList(UserSearch us) {
        if (!ClientMain.checkServerConnection()) {
            return null;
        }
        
        try {
            return ClientMain.server.getUserList(us);
        }
        catch (UserMessageException ex) {
            ClientMain.showError(ex.getMessage());
            return null;
        }
    }

    /**
     * Informs the server that the client has double clicked another user
     * @param userInfo Info of the user that the client double clicked
     */
    public static void doubleClickUser(UserInfo userInfo) {
        if (!ClientMain.checkServerConnection()) {
            return;
        }
        
        try {
            ClientMain.server.doubleClickUser(userInfo.id);
        }
        catch (UserMessageException ex) {
            ClientMain.showError(ex.getMessage());
        }
    }

}
