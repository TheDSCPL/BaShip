package pt.up.fe.lpro1613.sharedlib.structs;

/**
 * Tuple containing information about how a search for games on the database
 * should be done.
 *
 * @author Alex
 */
public class GameSearch {

    public final boolean currentlyPlayingOnly;
    public final String usernameFilter;
    public final int rowLimit;

    public GameSearch(boolean currentlyPlayingOnly, String usernameFilter, int rowLimit) {
        this.currentlyPlayingOnly = currentlyPlayingOnly;
        this.usernameFilter = usernameFilter;
        this.rowLimit = rowLimit;
    }

}
