package sharedlib.structs;

/**
 * Tuple containing information about how a search for users on the database
 * should be done.
 *
 * @author Alex
 */
public class UserSearch {
    
     /**
      * Filter by users that are online only.
      */
    public final boolean onlineOnly;

    /**
     * Filter by users that contain this string in their username. To include
     * all users pass in an empty String.
     */
    public final String usernameFilter;
    
    public final boolean useStatusFilter;
    
    public final UserInfo.Status statusFilter;

    /**
     * Which user information to order the results by. 1=id , 2=username ,
     * 3=rank , 4=ngames , 5=nwins , 6=nshots.
     */
    public final int orderByColumn;
    
    /**
     * The maximum number of users to retrieve.
     */
    public final int rowLimit;

    public UserSearch(boolean onlineOnly, String usernameFilter, boolean useStatusFilter, UserInfo.Status statusFilter, int orderByColumn, int rowLimit) {
        this.onlineOnly = onlineOnly;
        this.usernameFilter = usernameFilter;
        this.useStatusFilter = useStatusFilter;
        this.statusFilter = statusFilter;
        this.orderByColumn = orderByColumn;
        this.rowLimit = rowLimit;
    }

}
