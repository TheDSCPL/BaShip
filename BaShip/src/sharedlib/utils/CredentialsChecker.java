/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sharedlib.utils;

import java.util.regex.Pattern;

public class CredentialsChecker {
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
}
