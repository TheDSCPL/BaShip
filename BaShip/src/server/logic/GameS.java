package server.logic;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.*;
import server.conn.*;
import server.logic.game.Board;
import server.logic.game.GamePlay;
import server.logic.game.GameReplay;
import sharedlib.exceptions.*;
import sharedlib.tuples.GameScreenInfo;
import sharedlib.utils.*;

public class GameS {

    private static final Map<Client, GamePlay> currentGamesPlay = new ConcurrentHashMap<>();
    private static final Map<Client, GameReplay> currentGamesReplay = new ConcurrentHashMap<>();
    private static final Map<Client, Client> playersWaitingForPlayer = new ConcurrentHashMap<>();
    private static final Queue<Client> playersWaitingForGame = new ConcurrentLinkedQueue<>();
    private static final Map<Client, Board> playersWaitingBoards = new ConcurrentHashMap<>();

    public static void startRandomGame(Client client) throws UserMessageException {
        if (!playersWaitingForGame.isEmpty()) {
            startGame(client, playersWaitingForGame.poll()); // Start a game with the player who has been waiting for the most time
        }
        else {
            startWait(client, null);
        }
    }

    public static void startGameWithPlayer(Client client, Long otherPlayerID) throws UserMessageException {

        // Other player must be online
        if (UserS.isUserLoggedIn(otherPlayerID)) {
            Client otherPlayer = UserS.clientFromID(otherPlayerID);

            // Other player is waiting for a game too -> start game immediately
            if (playersWaitingForGame.contains(otherPlayer)) {
                startGame(client, otherPlayer);
            }
            // Other player is playing
            else if (currentGamesPlay.containsKey(client)) {
                throw new UserMessageException("Invited user is currently playing");
            }
            // Other player is online but available
            // \-> player starts waiting for otherPlayer
            // \-> send invitation to otherPlayer
            else {
                startWait(client, otherPlayer);
                sendInvitation(client, otherPlayer);
            }
        }
        else {
            throw new UserMessageException("Invited user is not online at the moment");
        }
    }

    public static void answerGameInvitation(Client client, boolean acepted) {
        // TODO: finish
    }

    private static void sendInvitation(Client from, Client to) {
        try {
            to.sendGameInvitation(); // TODO: finish
        }
        catch (ConnectionException ex) {
            Logger.getLogger(GameS.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void startGame(Client player1, Client player2) throws UserMessageException {
        GamePlay game;
        try {
            game = new GamePlay(player1, playersWaitingBoards.get(player1), player2, playersWaitingBoards.get(player2));
            currentGamesPlay.put(player1, game);
            currentGamesPlay.put(player2, game);
        }
        catch (SQLException ex) {
            Logger.getLogger(GameS.class.getName()).log(Level.SEVERE, null, ex);
            throw new UserMessageException("Could not access DB and create game", ex);
        }
    }

    private static void startWait(Client clientWaiting, Client targetClient) {
        // Waiting for random game
        if (targetClient == null) {
            playersWaitingForGame.add(clientWaiting);
        }
        // Waiting for a specific player
        else {
            playersWaitingForPlayer.put(targetClient, clientWaiting);
        }

        updateGameScreenForClient(targetClient);
    }

    public static void togglePlaceShipOnSquare(Client player, Coord pos) {
        if (isClientPlaying(player)) {
            currentGamesPlay.get(player).togglePlaceShipOnSquare(player, pos);
        }
        else {
            if (!playersWaitingBoards.containsKey(player)) {
                playersWaitingBoards.put(player, new Board());
            }

            Board board = playersWaitingBoards.get(player);
            board.togglePlaceShipOnSquare(pos);

            try {
                player.updateGameBoard(board.getBoardInfoNotPlaying());
            }
            catch (ConnectionException ex) {
                Logger.getLogger(GameS.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void clickReadyButton(Client player) {
        if (isClientPlaying(player)) {
            currentGamesPlay.get(player).clickReadyButton(player);
        }
        else {
            Logger.getLogger(GameS.class.getName()).log(Level.SEVERE, "Player {0} cannot click ready because he's not playing any game", player);
        }
    }

    public static void fireShot(Client player, Coord pos) {
        if (isClientPlaying(player)) {
            currentGamesPlay.get(player).fireShot(player, pos);
        }
        else {
            Logger.getLogger(GameS.class.getName()).log(Level.SEVERE, "Player {0} cannot fire shot because he's not playing any game", player);
        }
    }

    public static void clientDisconnected(Client client) {
        // TODO: finish
    }

    public static boolean isClientPlaying(Client client) {
        return currentGamesPlay.containsKey(client);
    }

    public static boolean isClientWaiting(Client client) {
        return playersWaitingForGame.contains(client) || playersWaitingForPlayer.containsValue(client);
    }
    
    public static boolean isGameRunning(Long gameID) {
        
    }
    
    public static Integer getGameCurrentMoveNumber(Long gameID) {
        
    }

    private static void updateGameScreenForClient(Client client) {
        GameScreenInfo gsi = null;

        if (playersWaitingForGame.contains(client)) {
            gsi = new GameScreenInfo(
                    UserS.usernameFromClient(client), "<waiting>", false,
                    "Waiting for opponent", "You can place ships", false,
                    false, false
            );
        }
        else if (playersWaitingForPlayer.containsValue(client)) {
            gsi = new GameScreenInfo(
                    UserS.usernameFromClient(client), "<waiting for " + "OTHER PLAYER TODO" + ">", false, // TODO: UserS.usernameFromClient(otherPlayer) 
                    "Waiting for opponent", "You can place ships", false,
                    false, false
            );
        }

        if (gsi != null) {
            try {
                client.updateGameScreen(gsi);
            }
            catch (ConnectionException ex) {
                Logger.getLogger(GameS.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
