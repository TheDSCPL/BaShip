package pt.up.fe.lpro1613.sharedlib.tuples;

/**
 * Tuple containing information about an user. Some fields may be null in some
 * cases. It is up to the function that returns instances of this class to
 * specify which fields contain information.
 *
 * @author Alex
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
        Offline,
        Online,
        Waiting,
        Playing
    }
}
