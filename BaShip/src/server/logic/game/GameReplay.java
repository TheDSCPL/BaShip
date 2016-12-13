package server.logic.game;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.conn.Client;
import server.database.GameChatDB;
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
    private int currentTurn;
    private final int totalTurnCount;
    private final Queue<Move> moves = new LinkedList<>(); // TODO: optimization possible

    public GameReplay(Client client, Long gameID) throws SQLException {
        this.client = client;
        this.gameID = gameID;

        p1Username = "XXX"; // TODO: finish
        p2Username = "YYY";

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
    // TODO: test this function
    public void showNextMove() throws UserMessageException {
        if (currentTurn >= totalTurnCount) {
            return;
        }

        Move move;
        try {
            move = MoveDB.getMove(gameID, currentTurn);
        }
        catch (SQLException ex) {
            Logger.getLogger(GameReplay.class.getName()).log(Level.SEVERE, null, ex);
            throw new UserMessageException("Could not access database to get next move: " + ex.getMessage());
        }

        currentTurn++;
        moves.add(move);
        boardForOpponentOfPlayerN(move.playerN).shootOnSquare(move.coord);
        refreshClient();
    }

    // TODO: test this function
    public void showPreviousMove() {
        if (currentTurn < 0) {
            return;
        }

        currentTurn--;
        Move lastMove = moves.poll();
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
