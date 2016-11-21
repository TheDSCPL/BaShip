package sharedlib.conn;

public enum Query {
    Empty(""),
    /**
     * Indicates packet is not a query, but a response to a query
     */
    Response("Response"),
    UsernameAvailable("UsernameAvailable"),
    Register("Register"),
    Login("Login"),
    Logout("Logout"),
    GetUserList("GetUserList");

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
