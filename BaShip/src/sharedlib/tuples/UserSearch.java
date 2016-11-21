package sharedlib.tuples;

/**
 *
 * @author Alex
 */
public class UserSearch {
    public final boolean onlineOnly;
    public final String usernameFilter;
    public final int orderByColumn;
    public final int rowLimit;

    public UserSearch(boolean onlineOnly, String usernameFilter, int orderByColumn, int rowLimit) {
        this.onlineOnly = onlineOnly;
        this.usernameFilter = usernameFilter;
        this.orderByColumn = orderByColumn;
        this.rowLimit = rowLimit;
    }
    
}
