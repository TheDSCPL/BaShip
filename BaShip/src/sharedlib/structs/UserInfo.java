package sharedlib.structs;

import sharedlib.enums.UserStatus;

/**
 * Tuple containing information about an user. Some fields may be null in some
 * cases. It is up to the function that returns instances of this class to
 * specify which fields contain information.
 *
 * @author Alex
 */
public class UserInfo {
    
    /**
     * ID of the user
     */
    public final Long id;

    /**
     * Username of the user
     */
    public final String username;

    /**
     * Password Hash of the user
     */
    public final String passwordHash;

    /**
     * Rank of the user
     */
    public final Integer rank;

    /**
     * Number of games played
     */
    public final Integer nGames;

    /**
     * Number of wins
     */
    public final Integer nWins;

    /**
     * Number of shots made
     */
    public final Integer nShots;

    /**
     * User status
     */
    public final UserStatus status;

    /**
     * Constructor of the class containing only the id and username
     * The other parameteres are set to null
     * 
     * @param id ID of user
     * @param username Username 
     */
    public UserInfo(Long id, String username) {
        this(id, username, null, null, null, null, null, null);
    }
    
    /**
     * Constructor of the class containing only the username and password hash.
     * The other parameteres are set to null
     * 
     * @param username Username
     * @param passwordHash PasswordHash
     */
    public UserInfo(String username, String passwordHash) {
        this(null, username, passwordHash, null, null, null, null, null);
    }
    
    /**
     * Constructor of the class
     * @param id ID 
     * @param username Username
     * @param passwordHash Password Hash
     * @param rank Rank of user
     * @param nGames Number of games played
     * @param nWins Number of wins
     * @param nShots Number of shots made
     * @param status Current status
     */
    public UserInfo(Long id, String username, String passwordHash, Integer rank, Integer nGames, Integer nWins, Integer nShots, UserStatus status) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.rank = rank;
        this.nGames = nGames;
        this.nWins = nWins;
        this.nShots = nShots;
        this.status = status;
    }
    
}
