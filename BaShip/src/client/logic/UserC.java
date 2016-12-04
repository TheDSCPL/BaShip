package client.logic;

import client.ClientMain;
import java.util.List;
import java.util.regex.*;
import pt.up.fe.lpro1613.sharedlib.exceptions.UserMessageException;
import pt.up.fe.lpro1613.sharedlib.structs.UserInfo;
import pt.up.fe.lpro1613.sharedlib.structs.UserSearch;

/**
 * Class responsible for managing the state of the user (logged-in or not) and
 * performing other user-related tasks.
 *
 * @author Alex
 */
public class UserC {

    private static UserInfo loggedInUser;

    /**
     * Runs the username against a set of rules to determine if it is a valid
     * username
     *
     * @param username Username to verify. Can be null
     * @return True if it obeys all the rules, false otherwise or if username is
     * null
     */
    public static boolean isUsernameValid(String username) {
        if (username == null || username.length() <= 0) {
            return false;
        }
        return username.matches("^[A-Za-z0-9_]+$");
    }

    /**
     * Runs the password against a set of rules to determine if it is a valid
     * password
     *
     * @param password Password to verify
     * @return True if it obeys all the rules and can be used as a password
     */
    public static boolean isPasswordValid(char[] password) {
        if (password == null || password.length <= 0) {
            return false;
        }
        StringBuffer p = new StringBuffer();
        p.append(password);

        Pattern atLeastOneDigit = Pattern.compile(".*\\d+.*");
        Pattern onlyLettersNumbersAndUnderscores = Pattern.compile("^[A-Za-z0-9_]+$");

        boolean ret = p.length() >= 6 && atLeastOneDigit.matcher(p).matches() && onlyLettersNumbersAndUnderscores.matcher(p).matches();
        p.delete(0, p.length() - 1);
        return ret;
    }

    /**
     * Login using the specified username and password
     *
     * @param username
     * @param password
     * @return True if login was successful
     * @throws UserMessageException
     */
    public static boolean login(String username, char[] password) throws UserMessageException {
        loggedInUser = ClientMain.server.login(username, password);
        return isLoggedIn();
    }

    /**
     * Register using the specified username and password
     *
     * @param username
     * @param password
     * @return True if register was successful
     * @throws UserMessageException
     */
    public static boolean register(String username, char[] password) throws UserMessageException {
        loggedInUser = ClientMain.server.register(username, password);
        return isLoggedIn();
    }

    /**
     * Logout the current logged-in user for this client.
     *
     * @return True if logout was successful
     * @throws UserMessageException
     */
    public static boolean logout() throws UserMessageException {
        ClientMain.server.logout();
        loggedInUser = null;
        return true;
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
     * @throws UserMessageException
     */
    public static List<UserInfo> getUserList(UserSearch us) throws UserMessageException {
        return ClientMain.server.getUserList(us);
    }

}
