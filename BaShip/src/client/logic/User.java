package client.logic;

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
        return username.matches("^[A-Za-z0-9_]+$");
    }

    public static boolean isPasswordValid(char[] password) {
        String p = new String(password);
        return p.length() >= 6 && p.matches(".*\\d+.*") && p.matches("^[A-Za-z0-9_]+$");
    }
}
