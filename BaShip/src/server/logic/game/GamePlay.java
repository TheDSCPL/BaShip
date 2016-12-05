package server.logic.game;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.conn.Client;
import server.database.GameChatDB;
import server.database.GameDB;
import server.database.MoveDB;
import server.database.ShipDB;
import server.logic.UserS;
import sharedlib.exceptions.ConnectionException;
import sharedlib.structs.BoardUIInfo;
import sharedlib.structs.GameUIInfo;
import sharedlib.structs.Message;
import sharedlib.utils.Coord;

class GamePlay {

    public final Long gameID;

    public final Client player1, player2;
    private PlayerState p1State, p2State;
    private final Board p1Board, p2Board;

    private boolean finished;
    private boolean p1Turn;
    private int moveIndex;

    private final HashSet<Client> spectators = new HashSet<>();

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

        // Set states
        p1State = PlayerState.PlacingShips;
        p2State = PlayerState.PlacingShips;

        // Assign turns
        finished = false;
        p1Turn = new Random().nextBoolean();
        moveIndex = 0;

        // Create game in DB
        gameID = GameDB.createGame(UserS.idFromClient(p1), UserS.idFromClient(p2));

        // Refresh interfaces
        refreshClientInfo();
    }

    public synchronized void addSpectator(Client client) {
        spectators.add(client);
        
        // Update UI
        refreshClientInfoForClient(client);
        
        // Send messages
        // TODO: XXX
    }

    public synchronized void removeSpectator(Client client) {
        spectators.remove(client);
    }

    public synchronized void clientClosedGame(Client client) {
        if (isPlayer(client)) {
            finishGame("Game ended. Player " + UserS.usernameFromClient(client) + " closed game.", opponent(client), client);
        }
        else {
            // TODO: spectator?
        }
    }

    public synchronized void clientDisconnected(Client client) {
        if (isPlayer(client)) {
            finishGame("Game ended. Player " + UserS.usernameFromClient(client) + " disconnected.", opponent(client), null);
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

    // TODO: refactor use of this function
    private void finishGame(String message, Client winner, Client dontSendMessageTo) {
        finished = true;

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
            if (dontSendMessageTo != player1) {
                player1.gameFinished(message);
            }
            if (dontSendMessageTo != player2) {
                player2.gameFinished(message);
            }

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

    public synchronized void clickReadyButton(Client player) {
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
                finishGame("Could not access database to start game", null, null);
                return;
            }
        }

        // Refresh interfaces
        refreshClientInfo();
    }

    public synchronized void playerClickedRightBoard(Client player, Coord pos) {
        // Verify if game has finished
        if (finished) {
            return;
        }

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
            finishGame("Could not access database to fire shot", null, null);
            return;
        }
        moveIndex++;

        // Check if player won
        if (boardForPlayer(opponent(player)).allShipsAreShot()) { // Player won
            refreshClientInfo();
            finishGame("Game finished: player " + UserS.usernameFromClient(player) + " won!", player, null);
        }
        else { // If not, change turn
            p1Turn = !p1Turn;
            refreshClientInfo();
        }
    }

    public synchronized void playerClickedLeftBoard(Client player, Coord pos) {
        // Verify if game has finished
        if (finished) {
            return;
        }

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

    public synchronized void playerSentMessage(Client player, String text) throws SQLException, ConnectionException {
        Message message = GameChatDB.saveMessage(gameID, player == player1 ? 1 : 2, text);
        
        player1.informAboutGameMessage(message);
        player2.informAboutGameMessage(message);
        
        for (Client c : spectators) {
            c.informAboutGameMessage(message);
        }
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
        boolean gameStarted = gameHasStarted();

        if (isPlayer(client)) {
            boolean opponentReady = stateForPlayer(opponent(client)) == PlayerState.Waiting;
            boolean placingShips = stateForPlayer(client) == PlayerState.PlacingShips;

            info = new GameUIInfo(
                    UserS.usernameFromClient(client),
                    UserS.usernameFromClient(opponent(client)),
                    gameHasStarted(),
                    opponentReady ? "Opponent is ready" : "Opponent is placing ships",
                    placingShips ? "You can place ships" : "Please wait for opponent",
                    placingShips && boardForPlayer(client).placedShipsAreValid(),
                    gameStarted ? (client == player1 ? p1Turn : !p1Turn) : false,
                    gameStarted ? (client == player1 ? !p1Turn : p1Turn) : false
            );
        }
        else {
            info = new GameUIInfo(
                    UserS.usernameFromClient(player1),
                    UserS.usernameFromClient(player2),
                    true,
                    null, null, null,
                    gameStarted ? p1Turn : false,
                    gameStarted ? !p1Turn : false
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
            leftBoard = boardForPlayer(client).getBoardInfo(playing, true, true);
            rightBoard = boardForPlayer(opponent(client)).getBoardInfo(playing, false, false);
        }
        else {
            leftBoard = boardForPlayer(player1).getBoardInfo(playing, true, true);
            rightBoard = boardForPlayer(player2).getBoardInfo(playing, true, false);
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

    public synchronized boolean isPlayer(Client client) {
        return client == player1 || client == player2;
    }

    public synchronized boolean isSpectator(Client client) {
        return spectators.contains(client);
    }

    public synchronized boolean gameHasStarted() {
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

    public synchronized int getCurrentMoveNumber() {
        return moveIndex;
    }
}
