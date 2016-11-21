package sharedlib.tuples;

public class GameSearch {

    public final boolean currentlyPlayingOnly;
    public final String usernameFilter;

    public GameSearch(boolean currentlyPlayingOnly, String usernameFilter) {
        this.currentlyPlayingOnly = currentlyPlayingOnly;
        this.usernameFilter = usernameFilter;
    }

}
