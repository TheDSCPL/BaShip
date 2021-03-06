package sharedlib.conn;

import com.google.gson.reflect.TypeToken;
import java.util.List;
import sharedlib.structs.BoardUIInfo;
import sharedlib.structs.ErrorMessage;
import sharedlib.structs.GameInfo;
import sharedlib.structs.GameSearch;
import sharedlib.structs.GameUIInfo;
import sharedlib.structs.Message;
import sharedlib.structs.UserInfo;
import sharedlib.structs.UserSearch;
import sharedlib.utils.Coord;

/**
 * Object that defines: (1) the information a {@code Packet} sent between client and server contains, (2) the type of event that the {@code Packet} signals.
 * Queries starting with 'C' are sent by the [C]lient to the server.
 * Queries starting with 'S' are sent by the [S]erver to the client.
 * Queries starting with 'B' are sent in [B]oth directions.
 * Queries starting with 'SR' are sent by the [S]erver to the client and are a [R]esponse to a client request packet.
 */
public enum Query {
    B_Empty(),
    SR_ErrorMessage(ErrorMessage.class),
    SR_UsernameAvailable(Boolean.class),
    B_Register(UserInfo.class),
    B_Login(UserInfo.class),
    C_Logout(),
    C_GetUserList(UserSearch.class),
    SR_GetUserList(new TypeToken<List<UserInfo>>(){}),
    C_GetGameList(GameSearch.class),
    SR_GetGameList(new TypeToken<List<GameInfo>>(){}),
    C_SendGameMessage(String.class),
    S_ReceiveGameMessage(Message.class),
    C_SendGlobalMessage(String.class),
    S_ReceiveGlobalMessage(Message.class),
    S_ClearGameMessages(),
    C_DoubleClickGame(Long.class),
    C_DoubleClickUser(Long.class),
    C_StartRandomGame(),
    S_UpdateGameScreen(GameUIInfo.class),
    S_UpdateGameBoard(BoardUIInfo.class),
    C_ClickReadyButton(),
    C_ClickLeftBoard(Coord.class),
    C_ClickRightBoard(Coord.class),
    C_CloseGame(),
    S_ShowMessageAndCloseGame(String.class),
    S_ReceiveGameInvitation(String.class),
    C_AnswerGameInvitation(Boolean.class),
    S_CloseGameInvitation(),
    C_ShowNextMove(),
    C_ShowPreviousMove();
    
    final TypeToken infoType;

    private Query(TypeToken infoType) {
        this.infoType = infoType;
    }
    
    private Query(Class c) {
        this.infoType = TypeToken.get(c);
    }
    
    private Query() {
        this.infoType = null;
    }
    
}
