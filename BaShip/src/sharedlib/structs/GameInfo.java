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

    public final Long id;
    public final Long player1ID;
    public final Long player2ID;
    public final String player1Username;
    public final String player2Username;
    public final GameState state;
    public final Date startDate;
    public final Date endDate;
    public final Integer turnNumber;

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
