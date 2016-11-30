package sharedlib.conn;

import com.google.gson.reflect.TypeToken;
import java.util.List;
import sharedlib.tuples.*;
import sharedlib.utils.Coord;

/**
 * Object that defines the information a Packet sent between client and server contains.
 * Queries starting with 'C' are sent by the [C]lient to the server.
 * Queries starting with 'S' are sent by the [S]erver to the client.
 * Queries starting with 'B' are sent in [B]oth directions.
 * Queries starting with 'SR' are sent by the [S]erver to the client and are a [R]esponse to a client request packet.
 */
public enum Query {
    BEmpty(),
    SRErrorMessage(ErrorMessage.class),
    CUsernameAvailable(String.class),
    SRUsernameAvailable(Boolean.class),
    CRegister(UserInfo.class),
    CLogin(UserInfo.class),
    CLogout(),
    CGetUserList(UserSearch.class),
    SRGetUserList(new TypeToken<List<UserInfo>>(){}),
    CGetGameList(GameSearch.class),
    SRGetGameList(new TypeToken<List<GameInfo>>(){}),
    CSendGameMessage(String.class),
    SReceiveGameMessage(Message.class),
    CSendGlobalMessage(String.class),
    SReceiveGlobalMessage(Message.class),
    CStartRandomGame(),
    CStartGameWithPlayer(Long.class),
    SUpdateGameScreen(GameScreenInfo.class),
    SUpdateGameBoard(BoardInfo.class),
    CClickReadyButton(),
    CTogglePlaceOnShipSquare(Coord.class),
    CFireShot(Coord.class),
    CCloseGame(),
    SGameFinished(String.class),
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
