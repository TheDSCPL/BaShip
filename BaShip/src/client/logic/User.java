package client.logic;

import java.util.*;
import java.util.regex.*;

public class User {

    public final long id;
    public final String username;
    
    public Statistics statistics;
    public Status status;
    
    public class Statistics {
        public final int rank;
        public final int nGames;
        public final int nWins;
        public final int nShots;

        public Statistics(int rank, int nGames, int nWins, int nShots) {
            this.rank = rank;
            this.nGames = nGames;
            this.nWins = nWins;
            this.nShots = nShots;
        }
    }
        
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
    }
    
    public User(Map<String, String> map) {
        id = Long.parseLong(map.get("id"));
        username = map.get("username");
        
        if (map.containsKey("status")) {
            status = Status.fromString(map.get("status"));
        }
        if (map.containsKey("")) {
            // TODO: finish
        }
    }
    
    public static boolean isUsernameValid(String username) {
        if (username == null || username.length() <= 0) {
            return false;
        }
        return username.matches("^[A-Za-z0-9_]+$");
    }

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
