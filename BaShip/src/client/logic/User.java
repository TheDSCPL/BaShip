package client.logic;

import java.util.regex.Pattern;

public class User {

    public final long id;
    public final String username;
    /*public final int rank;
    public final int nGames;
    public final int nWins;
    public final int nShots;
    public final Status status;

    public enum Status {
        Offline("Offline"),
        Online("Online"),
        Waiting("Waiting"),
        Playing("Playing");

        public final String str;

        Status(String s) {
            str = s;
        }

        public static Status fromString(String str) {
            for (Status t : Status.values()) {
                if (t.str.equals(str)) {
                    return t;
                }
            }

            return null;
        }
    }*/

    public User(long id, String username) {
        this.id = id;
        this.username = username;
    }

    public static boolean isUsernameValid(String username) {
        if(username == null || username.length() <= 0)
            return false;
        return username.matches("^[A-Za-z0-9_]+$");
    }

    public static boolean isPasswordValid(char[] password) {
        if(password == null || password.length <= 0)
            return false;
        StringBuffer p = new StringBuffer();
        p.append(password);
        
        Pattern atLeastOneDigit = Pattern.compile(".*\\d+.*");
        Pattern onlyLettersNumbersAndUnderscores = Pattern.compile("^[A-Za-z0-9_]+$");
        
        boolean ret = p.length() >= 6 && atLeastOneDigit.matcher(p).matches() && onlyLettersNumbersAndUnderscores.matcher(p).matches();
        p.delete(0, p.length()-1);
        return ret;
    }
}
