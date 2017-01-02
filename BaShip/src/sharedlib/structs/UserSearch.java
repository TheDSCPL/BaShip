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
    
    /**
     * Filter by the Index of list of user page.
     */
    public final int pageIndex;

    /**
     * UserSearch constructor
     * @param usernameFilter Username to filter. It can be an empty string
     * @param pageIndex Index of page to search
     */
    public UserSearch(String usernameFilter, int pageIndex) {
        this.usernameFilter = usernameFilter;
        this.pageIndex = pageIndex;
    }

}
