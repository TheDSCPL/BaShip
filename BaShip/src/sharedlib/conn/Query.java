package sharedlib.conn;

import com.google.gson.reflect.TypeToken;
import java.util.List;
import sharedlib.tuples.*;

/**
 * Object that defines the information a Packet sent between client and server contains.
 * Queries starting with 'C' are sent by the [C]lient to the server.
 * Queries starting with 'S' are sent by the [S]erver to the client.
 * Queries starting with 'B' are sent in [B]oth directions.
 */
public enum Query {
    BEmpty(),
    SErrorMessageResponse(ErrorMessage.class),
    CUsernameAvailable(String.class),
    CUsernameAvailableResponse(Boolean.class),
    CRegister(UserInfo.class),
    CLogin(UserInfo.class),
    CLogout(),
    CGetUserList(UserSearch.class),
    SGetUserListResponse(new TypeToken<List<UserInfo>>(){}),
    CGetGameList(GameSearch.class),
    SGetGameListResponse(new TypeToken<List<GameInfo>>(){}),
    CSendGameMessage(),
    SReceiveGameMessage,
    CSendGlobalMessage(String.class),
    SReceiveGlobalMessage(Message.class),
    CStartRandomGame(),
    CStartGameWithPlayer(),
    SShowGameScreen(),
    SReceiveGameInvitation(),
    CAnswerGameInvitation();
    
    public final TypeToken infoType;

    private Query(TypeToken infoType) {
        this.infoType = infoType;
    }
    
    private Query(Class c) {
        this.infoType = TypeToken.get(c);
    }
    
    private Query() {
        this.infoType = null;
    }
    
    public static Query fromString(String str) {
        for (Query t : Query.values()) {
            if (t.toString().equals(str)) {
                return t;
            }
        }

        return null;
    }
}
