package sharedlib;

import java.util.*;

/**
 * User info Model
 */
public class UserM {
    
    public final long id;
    public final String username;
    public final Integer rank;
    public final Integer nGames;
    public final Integer nWins;
    public final Integer nShots;
    public final Status status;

    public UserM(long id, String username) {
        this(0, username, null, null, null, null, null);
    }

    public UserM(long id, String username, Integer rank, Integer nGames, Integer nWins, Integer nShots, Status status) {
        this.id = id;
        this.username = username;
        this.rank = rank;
        this.nGames = nGames;
        this.nWins = nWins;
        this.nShots = nShots;
        this.status = status;
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
    
    public static UserM fromMap(Map<String, String> map) {
        return null;//new UserM();
    }
    
    public Map<String, String> getMap() {
        return new HashMap<>();
    }

}
