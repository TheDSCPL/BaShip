package sharedlib.conn;

/**
 *
 * @author Alex
 */
public enum Query {
    Empty(""),
    UsernameAvailable("UsernameAvailable"),
    Register("Register"),
    Login("Login"),
    Logout("Logout");

    public final String str;

    Query(String q) {
        str = q;
    }

    public static Query fromString(String str) {
        for (Query t : Query.values()) {
            if (t.str.equals(str)) {
                return t;
            }
        }

        return null;
    }
}
