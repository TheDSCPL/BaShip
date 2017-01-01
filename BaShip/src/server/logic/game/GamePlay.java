package server.logic.game;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
import sharedlib.exceptions.UserMessageException;
import sharedlib.structs.BoardUIInfo;
import sharedlib.structs.GameUIInfo;
import sharedlib.structs.GameUIInfo.UIType;
import sharedlib.structs.Message;
import sharedlib.utils.Coord;

class GamePlay {

    public final Long gameID;

    private final Client player1, player2;
    private PlayerState p1State, p2State;
    private final Board p1Board, p2Board;

    private boolean finished;
    private boolean p1Turn;
    private int moveIndex;

    private static final List<Message> messages = new ArrayList<>();

    private final HashSet<Client> spectators = new HashSet<>();

    private enum PlayerState {
        PlacingShips, Waiting, Playing
    }

    public GamePlay(Client p1, Board p1Board, Client p2, Board p2Board) throws UserMessageException {

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
        try {
            gameID = GameDB.createGame(UserS.userIDOfClient(p1), UserS.userIDOfClient(p2));
        }
        catch (SQLException ex) {
            Logger.getLogger(GamePlay.class.getName()).log(Level.SEVERE, "Could not create game on the database", ex);
            throw new UserMessageException("Could not start a game due to a database error");
        }

        // Clear game messages
        try {
            player1.clearGameMessages();
        }
        catch (ConnectionException ex) {
            Logger.getLogger(GamePlay.class.getName()).log(Level.SEVERE, "Could not clear game messages of " + player1, ex);
        }

        try {
            player2.clearGameMessages();
        }
        catch (ConnectionException ex) {
            Logger.getLogger(GamePlay.class.getName()).log(Level.SEVERE, "Could not clear game messages of " + player2, ex);
        }

        // Refresh interfaces
        refreshClientInfo();
    }

    /**
     * Add a spectator to the gameplay
     * @param client The client who wants to spectate
     */
    public synchronized void addSpectator(Client client) {
        if (isPlayer(client)) {
            return;
        }
        // Add spectator
        spectators.add(client);

        // Update UI
        refreshClientInfoForClient(client);

        // Send messages
        try {
            client.clearGameMessages();
            for (Message m : messages) {
                client.informAboutGameMessage(m);
            }
        }
        catch (ConnectionException ex) {
            Logger.getLogger(GamePlay.class.getName()).log(Level.SEVERE, "Could not send game messages to spectator " + client, ex);
        }
    }

    public synchronized void removeSpectator(Client client) {
        spectators.remove(client);
    }

    /**
     * Informs the class that the client closed the game. If he's a player of that game,
     * the game ends and the victory goes to the opponent.
     * @param client Client who closed the game
     */
    public synchronized void clientClosedGame(Client client) {
        if (isPlayer(client)) {
            finishGame("Game ended. Player " + UserS.usernameOfClient(client) + " closed game.", opponent(client), client);
        }
        else if (isSpectator(client)) {
            spectators.remove(client);
        }
    }

    /**
     * Informs the class that the client disconnected. If he's a player of the
     * game, the game ends and his opponent wins.
     * @param client Client who disconnected
     */
    public synchronized void clientDisconnected(Client client) {
        if (isPlayer(client)) {
            finishGame("Game ended. Player " + UserS.usernameOfClient(client) + " disconnected.", opponent(client), null);
        }
        else if (isSpectator(client)) {
            spectators.remove(client);
        }
    }

    /**
     * Informs this class that the client is ready to start the game. If both
     * players are ready, the game starts.
     * @param player The client who clicked the ready button
     */
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
                Logger.getLogger(GamePlay.class.getName()).log(Level.SEVERE, "Could not save start game info on database", ex);
                finishGame("Game closed due to a database error", null, null);
                return;
            }
        }

        // Refresh interfaces
        refreshClientInfo();
    }

    /**
     * Informs this class that the player clicked on the right board at pos. If 
     * he shoots the last ship, the game ends
     * @param player The player of clicked on the right board
     * @param pos Coordinate where player clicked
     */
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
        if (player != getCurrentPlayer()) {
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
            MoveDB.saveMove(gameID, player == player1 ? 1 : 2, moveIndex, pos);
        }
        catch (SQLException ex) {
            Logger.getLogger(GamePlay.class.getName()).log(Level.SEVERE, "Could not save move info on database", ex);
            finishGame("Game closed due to a database error", null, null);
            return;
        }
        moveIndex++;

        // Check if player won
        if (boardForPlayer(opponent(player)).allShipsAreShot()) { // Player won
            refreshClientInfo();
            finishGame("Game finished: player " + UserS.usernameOfClient(player) + " won!", player, null);
        }
        else { // If not, change turn
            p1Turn = !p1Turn;
            refreshClientInfo();
        }
    }

    /**
     * Informs this class that the player clicked on the left board at pos.
     * @param player The player who clicked
     * @param pos Coordinate where the player clicked
     */
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

    /**
     * Informs this class that the client send a text message to the game chat
     * @param player The client who sent the message
     * @param text The text that the player sent
     * @throws UserMessageException 
     */
    public synchronized void playerSentMessage(Client player, String text) throws UserMessageException {
        Message message;
        try {
            message = GameChatDB.saveMessage(gameID, player == player1 ? 1 : 2, text);
        }
        catch (SQLException ex) {
            Logger.getLogger(GamePlay.class.getName()).log(Level.SEVERE, "Could not save game message on database", ex);
            throw new UserMessageException("Could not send game message due to database error");
        }

        messages.add(message);

        try {
            player1.informAboutGameMessage(message);
            player2.informAboutGameMessage(message);

            for (Client c : spectators) {
                c.informAboutGameMessage(message);
            }
        }
        catch (ConnectionException ex) {
            Logger.getLogger(GamePlay.class.getName()).log(Level.SEVERE, "Could not inform players and/or spectators about game message", ex);
        }
    }

    public synchronized boolean isPlayer(Client client) {
        return client == player1 || client == player2;
    }

    public synchronized boolean isSpectator(Client client) {
        return spectators.contains(client);
    }

    public synchronized boolean hasStarted() {
        return p1State == PlayerState.Playing && p2State == PlayerState.Playing;
    }

    public synchronized int getCurrentMoveNumber() {
        return moveIndex;
    }

    public synchronized boolean hasFinished() {
        return finished;
    }

    public synchronized Client getCurrentPlayer() {
        return p1Turn ? player1 : player2;
    }

    private void startGame() throws SQLException {
        // Set start date
        GameDB.setStartTimeToNow(gameID);

        // Save ship positions
        ShipDB.saveShipPositions(gameID, 1, p1Board.getShips());
        ShipDB.saveShipPositions(gameID, 2, p2Board.getShips());
    }

    /**
     * Informs this class that the game finished. The winner is set and all the
     * spectator are removed
     * @param message Message sent to the winner
     * @param winner The player that wins the game
     * @param dontSendMessageTo The player that loses the game
     */
    private void finishGame(String message, Client winner, Client dontSendMessageTo) {
        finished = true;

        // Set winner and finish time
        try {
            GameDB.setEndTimeToNow(gameID);

            if (winner != null) {
                GameDB.setWinner(gameID, UserS.userIDOfClient(winner));
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(GamePlay.class.getName()).log(Level.SEVERE, "Could not save game finished information on database. Game with id=" + gameID + " is possibly corrupt on database", ex);
        }

        // Message players and spectators that game has finished
        if (dontSendMessageTo != player1) {
            try {
                player1.showMessageAndCloseGame(message);
            }
            catch (ConnectionException ex) {
                Logger.getLogger(GamePlay.class.getName()).log(Level.SEVERE, "Could not close game of player " + player1, ex);
            }
        }

        if (dontSendMessageTo != player2) {
            try {
                player2.showMessageAndCloseGame(message);
            }
            catch (ConnectionException ex) {
                Logger.getLogger(GamePlay.class.getName()).log(Level.SEVERE, "Could not close game of player " + player2, ex);
            }
        }

        for (Client spectator : spectators) {
            try {
                spectator.showMessageAndCloseGame(message);
            }
            catch (ConnectionException ex) {
                Logger.getLogger(GamePlay.class.getName()).log(Level.SEVERE, "Could not close game of spectator " + spectator, ex);
            }
        }

        // Remove myself from GameS's lists
        GameS.Callbacks.gamePlayFinished(this);
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
        boolean gameStarted = hasStarted();

        if (isPlayer(client)) {
            boolean opponentReady = stateForPlayer(opponent(client)) == PlayerState.Waiting;
            boolean placingShips = stateForPlayer(client) == PlayerState.PlacingShips;

            info = new GameUIInfo(
                    UserS.usernameOfClient(client),
                    UserS.usernameOfClient(opponent(client)),
                    hasStarted(),
                    opponentReady ? "Opponent is ready" : "Opponent is placing ships",
                    placingShips ? "You can place ships" : "Please wait for opponent",
                    placingShips && boardForPlayer(client).placedShipsAreValid(),
                    gameStarted ? (client == player1 ? p1Turn : !p1Turn) : false,
                    gameStarted ? (client == player1 ? !p1Turn : p1Turn) : false,
                    UIType.Play, null, null, null
            );
        }
        else {
            info = new GameUIInfo(
                    UserS.usernameOfClient(player1),
                    UserS.usernameOfClient(player2),
                    true,
                    null, null, null,
                    gameStarted ? p1Turn : false,
                    gameStarted ? !p1Turn : false,
                    UIType.Spectate, null, null, null
            );
        }

        try {
            client.updateGameScreen(info);
        }
        catch (ConnectionException ex) {
            Logger.getLogger(GamePlay.class.getName()).log(Level.SEVERE, "Could not update game screen info of " + client, ex);
        }
    }

    private void refreshBoardsForClient(Client client) {
        BoardUIInfo leftBoard, rightBoard;
        boolean playing = hasStarted();

        if (isPlayer(client)) {
            leftBoard = boardForPlayer(client).getBoardInfo(playing, true);
            rightBoard = boardForPlayer(opponent(client)).getBoardInfo(playing, false);
        }
        else {
            leftBoard = boardForPlayer(player1).getBoardInfo(playing, true);
            rightBoard = boardForPlayer(player2).getBoardInfo(playing, true);
        }

        leftBoard.leftBoard = true;
        rightBoard.leftBoard = false;

        try {
            client.updateGameBoard(leftBoard);
            client.updateGameBoard(rightBoard);
        }
        catch (ConnectionException ex) {
            Logger.getLogger(GamePlay.class.getName()).log(Level.SEVERE, "Could not update board info of " + client, ex);
        }
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
}
