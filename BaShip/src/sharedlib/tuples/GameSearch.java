package sharedlib.tuples;

public class GameSearch {

    public final boolean currentlyPlayingOnly;
    public final String usernameFilter;
    public final int orderByColumn;
    public final int rowLimit;

    public GameSearch(boolean currentlyPlayingOnly, String usernameFilter, int orderByColumn, int rowLimit) {
        this.currentlyPlayingOnly = currentlyPlayingOnly;
        this.usernameFilter = usernameFilter;
        this.orderByColumn = orderByColumn;
        this.rowLimit = rowLimit;
    }

}
