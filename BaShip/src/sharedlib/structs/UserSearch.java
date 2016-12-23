package sharedlib.structs;

/**
 * Tuple containing information about how a search for users on the database
 * should be done.
 *
 * @author Alex
 */
public class UserSearch {

    /**
     * Filter by users that contain this string in their username. To include
     * all users pass in an empty String.
     */
    public final String usernameFilter;
    
    public final int pageIndex;

    public UserSearch(String usernameFilter, int pageIndex) {
        this.usernameFilter = usernameFilter;
        this.pageIndex = pageIndex;
    }

}
