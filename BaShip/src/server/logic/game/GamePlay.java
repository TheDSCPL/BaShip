package server.logic.game;

import java.util.*;
import java.util.logging.*;
import server.conn.*;
import server.logic.*;
import sharedlib.exceptions.*;
import sharedlib.tuples.*;
import sharedlib.utils.*;

public class GamePlay {

    public final Client player1, player2;
    private PlayerState p1State, p2State;
    private final Board p1Board, p2Board;
    private final Long gameID;
    
    private boolean p1Turn;
    private final HashSet<Client> spectators = new HashSet<>(); // TODO: make this set thread-safe!

    private enum PlayerState {
        PlacingShips, Waiting, Playing
    }

    public GamePlay(Client p1, Board p1Board, Client p2, Board p2Board) {

        // Set players
        player1 = p1;
        player2 = p2;

        // Set boards
        this.p1Board = p1Board != null ? p1Board : new Board();
        this.p2Board = p2Board != null ? p2Board : new Board();

        // Assign turns
        p1Turn = new Random().nextBoolean();

        // TODO: create game in DB
        gameID = 0L;
        
        // Refresh interfaces
        refreshClientInfo();
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
            
            // TODO: finish
            // Start game & save ship positions
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

        // TODO: verify: other tests?
        // Everything ok, continue...
        // Send that to board
        boardForPlayer(opponent(player)).shootOnSquare(pos);

        // Save shot on DB
        // TODO: finish
        
        if (boardForPlayer(opponent(player)).allShipsAreShot()) { // Check if player won
            // TODO: finish
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

        // TODO: verify: other tests?
        // Everything ok, continue...
        // Send info to corresponding board
        boardForPlayer(player).togglePlaceShipOnSquare(pos);
        
        // Refresh interfaces
        refreshClientInfo();
    }

    private void refreshClientInfo() {
        refreshClientInfoForClient(player1);
        refreshClientInfoForClient(player2);
        spectators.stream().forEach((c) -> refreshClientInfoForClient(c));
    }

    private void refreshClientInfoForClient(Client client) {
        refreshGameScreenForClient(client);
        refreshBoardsForClient(client);
    }

    private void refreshGameScreenForClient(Client client) {
        GameScreenInfo info;

        if (isPlayer(client)) {
            info = new GameScreenInfo(
                    UserS.usernameFromClient(client),
                    UserS.usernameFromClient(opponent(client)),
                    gameHasStarted(),
                    !gameHasStarted() ? "TODO" : null, // TODO: finish
                    !gameHasStarted() ? "TODO" : null, // TODO: finish
                    false, // TODO: finish
                    client == player1 ? p1Turn : !p1Turn,
                    client == player1 ? !p1Turn : p1Turn
            );
        }
        else {
            info = new GameScreenInfo(
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
        BoardInfo leftBoard, rightBoard;

        if (isPlayer(client)) {
            // TODO: finish
        }
        else {
            // TODO: finish
        }

        /*try {
            client.updateGameBoard(leftBoard);
            client.updateGameBoard(rightBoard);
        }
        catch (ConnectionException ex) {
            Logger.getLogger(GamePlay.class.getName()).log(Level.SEVERE, null, ex);
        }*/
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

    private boolean gameHasStarted() {
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
}
