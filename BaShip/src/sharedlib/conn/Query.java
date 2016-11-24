package sharedlib.conn;

/**
 * Object that defines the information a Packet sent between client and server contains.
 * Queries starting with 'C' are sent by the [C]lient to the server.
 * Queries starting with 'S' are sent by the [S]erver to the client.
 * Queries starting with 'B' are sent in [B]oth directions.
 */
public enum Query {
    BEmpty,
    CUsernameAvailable,
    CRegister,
    CLogin,
    CLogout,
    CGetUserList,
    CGetGameList,
    CSendGameMessage,
    SReceiveGameMessage,
    CSendGlobalMessage,
    SReceiveGlobalMessage,
    CStartRandomGame,
    CStartGameWithPlayer,
    SShowGameScreen,
    SReceiveGameInvitation,
    CAnswerGameInvitation;
    
    public static Query fromString(String str) {
        for (Query t : Query.values()) {
            if (t.toString().equals(str)) {
                return t;
            }
        }

        return null;
    }
}
