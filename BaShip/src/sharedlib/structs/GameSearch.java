package sharedlib.structs;

/**
 * Tuple containing information about how a search for games on the database
 * should be done.
 *
 * @author Alex
 */
public class GameSearch {

    /**
     * Filter by games that are currently being played. That means, ignore any
     * game played in the past.
     */
    public final boolean currentlyPlayingOnly;

    /**
     * Filter by games played by players whose username contains this string. To
     * ignore this filter pass in an empty String.
     */
    public final String usernameFilter;

    public final int pageIndex;

    public GameSearch(boolean currentlyPlayingOnly, String usernameFilter, int pageIndex) {
        this.currentlyPlayingOnly = currentlyPlayingOnly;
        this.usernameFilter = usernameFilter;
        this.pageIndex = pageIndex;
    }

}
