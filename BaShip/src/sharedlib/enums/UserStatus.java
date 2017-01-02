package sharedlib.enums;

/**
 * Possible status of a user
 */
public enum UserStatus {

    /**
     * The user isn't connected to the game
     */
    Offline,

    /**
     * The user is logged in. He is in the lobby, and can interact with other
     * players
     */
    Online,

    /**
     * The user is waiting for other player to join a game.
     */
    Waiting,

    /**
     * The user is in a game playing against another user
     */
    Playing;
    
    /**
     *
     * @param s The user status to compare
     * @return The user status that is equal to the string passed by argument.
     * Null otherwise
     */
    public UserStatus getFromString(String s)
    {
        for(UserStatus us : UserStatus.values())
            if(us.name().equals(s))
                return us;
        return null;
    }
}
