package client.logic;

import client.ClientMain;
import java.util.regex.*;
import sharedlib.exceptions.UserMessageException;
import sharedlib.tuples.UserInfo;

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

    public static Long getLoggedInUserID() {
        return loggedInUser.id;
    }

    public static String getLoggedInUsername() {
        return loggedInUser.username;
    }

    public static boolean isLoggedIn() {
        return loggedInUser != null;
    }

}
