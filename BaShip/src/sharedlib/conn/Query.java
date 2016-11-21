package sharedlib.conn;

public enum Query {
    Empty,
    UsernameAvailable,
    Register,
    Login,
    Logout,
    GetUserList,
    GetGameList;
    
    public static Query fromString(String str) {
        for (Query t : Query.values()) {
            if (t.toString().equals(str)) {
                return t;
            }
        }

        return null;
    }
}
