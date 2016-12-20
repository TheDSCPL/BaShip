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

    public GameReplay(Client client, Long gameID) throws UserMessageException {
        this.client = client;
        this.gameID = gameID;

        String[] usernames;
        try {
            usernames = GameDB.getPlayerUsernamesFromGame(gameID);
        }
        catch (SQLException ex) {
            Logger.getLogger(GameReplay.class.getName()).log(Level.SEVERE, "Could not get usernames of players of game from database", ex);
            throw new UserMessageException("Cannot replay game due to a database error");
        }

        p1Username = usernames[0];
        p2Username = usernames[1];

        p1Board = new Board();
        p2Board = new Board();

        try {
            p1Board.setShips(ShipDB.getShipPositions(gameID, 1));
            p2Board.setShips(ShipDB.getShipPositions(gameID, 2));
        }
        catch (SQLException ex) {
            Logger.getLogger(GameReplay.class.getName()).log(Level.SEVERE, "Could not get ship positions from database", ex);
            throw new UserMessageException("Could not replay game due to a database error");
        }

        currentTurn = 0;

        try {
            totalTurnCount = MoveDB.getTotalMoveCount(gameID);
        }
        catch (SQLException ex) {
            Logger.getLogger(GameReplay.class.getName()).log(Level.SEVERE, "Could not get total move count from database", ex);
            throw new UserMessageException("Could not replay game due to a database error");
        }

        // Show game screen and boards
        refreshClient();

        // Show messages
        try {
            client.clearGameMessages();

            List<Message> messages = GameChatDB.getMessages(gameID);
            for (Message msg : messages) {
                client.informAboutGameMessage(msg);
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(GameReplay.class.getName()).log(Level.SEVERE, "Could not get game messages from database", ex);
        }
        catch (ConnectionException ex) {
            Logger.getLogger(GameReplay.class.getName()).log(Level.SEVERE, "Could not send messages to " + client, ex);
        }
    }

    public synchronized void showNextMove() throws UserMessageException {
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
                Logger.getLogger(GameReplay.class.getName()).log(Level.SEVERE, "Could not access database to get next move", ex);
                throw new UserMessageException("Could not access database to get next move");
            }
            moves.add(move);
        }

        move = moves.get(currentTurn - 1);
        boardForOpponentOfPlayerN(move.playerN).shootOnSquare(move.coord);
        refreshClient();
    }

    public synchronized void showPreviousMove() {
        if (currentTurn <= 0) {
            return;
        }

        currentTurn--;

        Move lastMove = moves.get(currentTurn);
        boardForOpponentOfPlayerN(lastMove.playerN).removeShotFromSquare(lastMove.coord);
        refreshClient();
    }

    public synchronized void clientClosedGame() {
        GameS.Callbacks.gameReplayFinished(this);
    }

    public synchronized void clientDisconnected() {
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
            Logger.getLogger(GameReplay.class.getName()).log(Level.SEVERE, "Could not update game & board info for " + client, ex);
        }
    }

}
