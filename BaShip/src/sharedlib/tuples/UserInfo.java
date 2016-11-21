package sharedlib.tuples;

import java.util.*;

/**
 * User info Model
 */
public class UserInfo {
    
    public final Long id;
    public final String username;
    public final String passwordHash;
    public final Integer rank;
    public final Integer nGames;
    public final Integer nWins;
    public final Integer nShots;
    public final Status status;

    public UserInfo(Long id, String username) {
        this(id, username, null, null, null, null, null, null);
    }
    
    public UserInfo(String username, String passwordHash) {
        this(null, username, passwordHash, null, null, null, null, null);
    }
    
    public UserInfo(Long id, String username, String passwordHash, Integer rank, Integer nGames, Integer nWins, Integer nShots, Status status) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
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
    
    public static UserInfo fromMap(Map<String, String> map) {
        return null;//new UserInfo();
    }
    
    public Map<String, String> getMap() {
        return new HashMap<>();
    }

}
