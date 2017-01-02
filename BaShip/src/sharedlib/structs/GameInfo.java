package sharedlib.structs;

import java.util.Date;
import sharedlib.enums.GameState;

/**
 * Tuple containing information about a game. Some fields may be null in some
 * cases. It is up to the function that returns instances of this class to
 * specify which fields contain information.
 *
 * @author Alex
 */
public class GameInfo {

    /**
     * ID of the game
     */
    public final Long id;

    /**
     * ID of player1 
     */
    public final Long player1ID;

    /**
     * ID of player2
     */
    public final Long player2ID;

    /**
     * Username of player1
     */
    public final String player1Username;

    /**
     * Username of player2
     */
    public final String player2Username;

    /**
     * State of the game
     */
    public final GameState state;

    /**
     * Start date of the game
     */
    public final Date startDate;

    /**
     * End date of the game
     */
    public final Date endDate;

    /**
     * Number of turns 
     */
    public final Integer turnNumber;

    /**
     * Class constructor
     * @param id ID of Game
     * @param player1ID ID player1
     * @param player2ID ID player2
     * @param player1Username Username player1
     * @param player2Username Username player2
     * @param state State of game
     * @param startDate Start date
     * @param endDate End date
     * @param turnNumber Number of turns
     */
    public GameInfo(Long id, Long player1ID, Long player2ID, String player1Username, String player2Username, GameState state, Date startDate, Date endDate, Integer turnNumber) {
        this.id = id;
        this.player1ID = player1ID;
        this.player2ID = player2ID;
        this.player1Username = player1Username;
        this.player2Username = player2Username;
        this.state = state;
        this.startDate = startDate;
        this.endDate = endDate;
        this.turnNumber = turnNumber;
    }

}
