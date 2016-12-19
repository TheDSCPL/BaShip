package server.logic.game;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.conn.Client;
import server.database.GameChatDB;
import server.database.GameDB;
import server.database.MoveDB;
import server.database.ShipDB;
import sharedlib.exceptions.ConnectionException;
import sharedlib.exceptions.UserMessageException;
import sharedlib.structs.BoardUIInfo;
import sharedlib.structs.GameUIInfo;
import sharedlib.structs.GameUIInfo.UIType;
import sharedlib.structs.Message;
import sharedlib.structs.Move;

public class GameReplay {

    public final Client client;
    public final Long gameID;

    private String p1Username, p2Username;
    private Board p1Board, p2Board;
    private int currentTurn; // 0 means no turns are being shown; 1.. is the (one-indexed) turn number
    private final int totalTurnCount;
    private final List<Move> moves = new ArrayList<>();

    public GameReplay(Client client, Long gameID) throws SQLException {
        this.client = client;
        this.gameID = gameID;

        String[] usernames = GameDB.getPlayerUsernamesFromGame(gameID);
        p1Username = usernames[0];
        p2Username = usernames[1];

        p1Board = new Board();
        p2Board = new Board();

        p1Board.setShips(ShipDB.getShipPositions(gameID, 1));
        p2Board.setShips(ShipDB.getShipPositions(gameID, 2));

        currentTurn = 0;
        totalTurnCount = MoveDB.getTotalMoveCount(gameID);

        // Show game screen and boards
        refreshClient();

        // Show messages
        try {
            client.clearGameMessages();

            List<Message> messages = GameChatDB.getMessages(gameID);
            System.out.println(messages);
            for (Message msg : messages) {
                client.informAboutGameMessage(msg);
            }
        }
        catch (ConnectionException ex) {
            Logger.getLogger(GameReplay.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // TODO: synchronized methods?
    public void showNextMove() throws UserMessageException {
        if (currentTurn >= totalTurnCount) {
            return;
        }

        currentTurn++;

        Move move;
        if (currentTurn > moves.size()) {
            try {
                move = MoveDB.getMove(gameID, currentTurn - 1); // Moves on DB are zero-indexed
            }
            catch (SQLException ex) {
                Logger.getLogger(GameReplay.class.getName()).log(Level.SEVERE, null, ex);
                throw new UserMessageException("Could not access database to get next move: " + ex.getMessage());
            }
            moves.add(move);
        }

        move = moves.get(currentTurn - 1);
        boardForOpponentOfPlayerN(move.playerN).shootOnSquare(move.coord);
        refreshClient();
    }

    public void showPreviousMove() {
        if (currentTurn <= 0) {
            return;
        }

        currentTurn--;
        
        Move lastMove = moves.get(currentTurn);
        boardForOpponentOfPlayerN(lastMove.playerN).removeShotFromSquare(lastMove.coord);
        refreshClient();
    }

    public void clientClosedGame() {
        GameS.Callbacks.gameReplayFinished(this);
    }

    public void clientDisconnected() {
        GameS.Callbacks.gameReplayFinished(this);
    }

    private Board boardForOpponentOfPlayerN(int playerN) {
        switch (playerN) {
            case 1: return p2Board;
            case 2: return p1Board;
            default: return null;
        }
    }

    private void refreshClient() {
        GameUIInfo info = new GameUIInfo(
                p1Username,
                p2Username,
                true, null, null, null,
                false, false, UIType.Replay,
                currentTurn > 0, currentTurn < totalTurnCount,
                "Turn " + currentTurn + " of " + totalTurnCount
        );

        BoardUIInfo leftBoard = p1Board.getBoardInfoPlaying(true);
        leftBoard.leftBoard = true;

        BoardUIInfo rightBoard = p2Board.getBoardInfoPlaying(true);
        rightBoard.leftBoard = false;

        try {
            client.updateGameScreen(info);
            client.updateGameBoard(leftBoard);
            client.updateGameBoard(rightBoard);
        }
        catch (ConnectionException ex) {
            Logger.getLogger(GameReplay.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
