package sharedlib.enums;

/**
 * Possible states of a game
 */
public enum GameState {
    /**
     * Game exists but players are still placing ships.
     */
    Created,
    /**
     * Players are playing. startDate and turnNumber are non-null
     */
    Playing,
    /**
     * Game has ended. startDate and endDate are non-null
     */
    Finished
}
