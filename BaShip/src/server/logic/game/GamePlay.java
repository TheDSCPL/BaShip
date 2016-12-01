package server.logic.game;

import java.sql.SQLException;
import java.util.*;
import java.util.logging.*;
import server.conn.*;
import server.database.GameDB;
import server.database.MoveDB;
import server.database.ShipDB;
import server.logic.*;
import sharedlib.exceptions.*;
import sharedlib.tuples.*;
import sharedlib.utils.*;

public class GamePlay {

    public final Long gameID;

    public final Client player1, player2;
    private PlayerState p1State, p2State;
    private final Board p1Board, p2Board;

    private boolean p1Turn;
    private int moveIndex;

    private final HashSet<Client> spectators = new HashSet<>(); // TODO: make this set thread-safe!

    private enum PlayerState {
        PlacingShips, Waiting, Playing
    }

    public GamePlay(Client p1, Board p1Board, Client p2, Board p2Board) throws SQLException {

        // Set players
        player1 = p1;
        player2 = p2;

        // Set boards
        this.p1Board = p1Board != null ? p1Board : new Board();
        this.p2Board = p2Board != null ? p2Board : new Board();

        // Assign turns
        p1Turn = new Random().nextBoolean();
        moveIndex = 0;

        // Create game in DB
        gameID = GameDB.createGame(UserS.idFromClient(p1), UserS.idFromClient(p2));

        // Refresh interfaces
        refreshClientInfo();
    }

    public void gameClosedByClient(Client client) {
        if (isPlayer(client)) {
            finishGame("Game ended. Player " + UserS.usernameFromClient(client) + " closed game.", opponent(client));
        }
        else {
            // TODO: spectator?
        }
    }

    public void clientDisconnected(Client client) {
        if (isPlayer(client)) {
            finishGame("Game ended. Player " + UserS.usernameFromClient(client) + " disconnected.", opponent(client));
        }
        else {
            // TODO: spectator?
        }
    }

    private void startGame() throws SQLException {
        // Set start date
        GameDB.setStartTimeToNow(gameID);

        // Save ship positions
        ShipDB.saveShipPositions(gameID, 1, p1Board.getShips());
        ShipDB.saveShipPositions(gameID, 2, p2Board.getShips());
    }

    private void finishGame(String message, Client winner) {
        // Set winner and finish time
        try {
            GameDB.setEndTimeToNow(gameID);

            if (winner != null) {
                GameDB.setWinner(gameID, UserS.idFromClient(winner));
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(GamePlay.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Message players and spectators
        try {
            player1.gameFinished(message);
            player2.gameFinished(message);

            for (Client spectator : spectators) {
                spectator.gameFinished(message);
            }
        }
        catch (ConnectionException ex) {
            Logger.getLogger(GamePlay.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Remove myself from GameS's lists
        GameS.gameFinished(this);
    }

    public void clickReadyButton(Client player) {
        // Verify if player is in this game
        if (!isPlayer(player)) {
            return;
        }

        // Verify player is placing ships
        if (stateForPlayer(player) != PlayerState.PlacingShips) {
            return;
        }

        // Verify if ships are well placed
        if (!boardForPlayer(player).placedShipsAreValid()) {
            return;
        }

        // Change state for player
        setStateForPlayer(player, PlayerState.Waiting);

        // Start game if both players are ready
        if (stateForPlayer(player1) == PlayerState.Waiting && stateForPlayer(player2) == PlayerState.Waiting) {

            // Update player states
            setStateForPlayer(player1, PlayerState.Playing);
            setStateForPlayer(player2, PlayerState.Playing);

            // Start game & save ship positions
            try {
                startGame();
            }
            catch (SQLException ex) {
                Logger.getLogger(GamePlay.class.getName()).log(Level.SEVERE, null, ex);
                finishGame("Could not access database to start game", null);
                return;
            }
        }

        // Refresh interfaces
        refreshClientInfo();
    }

    public void fireShot(Client player, Coord pos) {

        // Verify if player is in this game
        if (!isPlayer(player)) {
            return;
        }

        // Verify player is playing
        if (stateForPlayer(player) != PlayerState.Playing) {
            return;
        }

        // Verify current turn
        if (player != currentPlayerTurn()) {
            return;
        }

        // Verify player can shoot there
        if (!boardForPlayer(opponent(player)).canShootOnSquare(pos)) {
            return;
        }

        // Send that shot to board
        boardForPlayer(opponent(player)).shootOnSquare(pos);

        // Save shot on DB
        try {
            MoveDB.saveMove(gameID, player == player1 ? 1 : 2, moveIndex);
        }
        catch (SQLException ex) {
            Logger.getLogger(GamePlay.class.getName()).log(Level.SEVERE, "Could not fire shot", ex);
            finishGame("Could not access database to fire shot", null);
            return;
        }
        moveIndex++;

        // Check if player won
        if (boardForPlayer(opponent(player)).allShipsAreShot()) { // Player won
            finishGame("Game finished: player " + UserS.usernameFromClient(player) + "won!", player);
            return;
        }
        else { // If not, change turn
            p1Turn = !p1Turn;
        }

        // Refresh interfaces
        refreshClientInfo();
    }

    public void togglePlaceShipOnSquare(Client player, Coord pos) {

        // Verify if player is in this game
        if (!isPlayer(player)) {
            return;
        }

        // Verify player is still placing ships
        if (stateForPlayer(player) != PlayerState.PlacingShips) {
            return;
        }

        // Send info to corresponding board
        boardForPlayer(player).togglePlaceShipOnSquare(pos);

        // Refresh interfaces
        refreshClientInfo();
    }

    private void refreshClientInfo() {
        refreshClientInfoForClient(player1);
        refreshClientInfoForClient(player2);
        spectators.stream().forEach(this::refreshClientInfoForClient);
    }

    private void refreshClientInfoForClient(Client client) {
        refreshGameScreenForClient(client);
        refreshBoardsForClient(client);
    }

    private void refreshGameScreenForClient(Client client) {
        GameUIInfo info;

        if (isPlayer(client)) {
            boolean opponentReady = stateForPlayer(opponent(client)) == PlayerState.Waiting;

            info = new GameUIInfo(
                    UserS.usernameFromClient(client),
                    UserS.usernameFromClient(opponent(client)),
                    gameHasStarted(),
                    !gameHasStarted() ? (opponentReady ? "Opponent is ready" : "Opponent is placing ships") : null,
                    !gameHasStarted() ? "You can place ships" : null,
                    stateForPlayer(client) == PlayerState.PlacingShips && boardForPlayer(client).placedShipsAreValid(),
                    client == player1 ? p1Turn : !p1Turn,
                    client == player1 ? !p1Turn : p1Turn
            );
        }
        else {
            info = new GameUIInfo(
                    UserS.usernameFromClient(player1),
                    UserS.usernameFromClient(player2),
                    true,
                    null, null, null,
                    p1Turn, !p1Turn
            );
        }

        try {
            client.updateGameScreen(info);
        }
        catch (ConnectionException ex) {
            Logger.getLogger(GamePlay.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void refreshBoardsForClient(Client client) {
        BoardUIInfo leftBoard, rightBoard;
        boolean playing = gameHasStarted();

        if (isPlayer(client)) {
            leftBoard = boardForPlayer(client).getBoardInfo(true, playing, true);
            rightBoard = boardForPlayer(opponent(client)).getBoardInfo(false, playing, false);
        }
        else {
            leftBoard = boardForPlayer(player1).getBoardInfo(true, playing, true);
            rightBoard = boardForPlayer(player2).getBoardInfo(false, playing, true);
        }

        try {
            client.updateGameBoard(leftBoard);
            client.updateGameBoard(rightBoard);
        }
        catch (ConnectionException ex) {
            Logger.getLogger(GamePlay.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Client currentPlayerTurn() {
        return p1Turn ? player1 : player2;
    }

    private Client opponent(Client player) {
        if (player == player1) {
            return player2;
        }

        if (player == player2) {
            return player1;
        }

        return null;
    }

    public boolean isPlayer(Client player) {
        return player == player1 || player == player2;
    }

    public boolean isSpectator(Client player) {
        return spectators.contains(player);
    }

    public boolean gameHasStarted() {
        return p1State == PlayerState.Playing && p2State == PlayerState.Playing;
    }

    private PlayerState stateForPlayer(Client player) {
        if (player == player1) {
            return p1State;
        }

        if (player == player2) {
            return p2State;
        }

        return null;
    }

    private void setStateForPlayer(Client player, PlayerState state) {
        if (player == player1) {
            p1State = state;
        }
        else if (player == player2) {
            p2State = state;
        }
    }

    private Board boardForPlayer(Client player) {
        if (player == player1) {
            return p1Board;
        }

        if (player == player2) {
            return p2Board;
        }

        return null;
    }

    public int getCurrentMoveNumber() {
        return moveIndex;
    }
}
