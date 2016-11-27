package server.logic;

import java.util.*;
import java.util.concurrent.*;
import java.util.logging.*;
import server.conn.*;
import sharedlib.exceptions.*;
import sharedlib.utils.*;

public class GameS {

    private static final Map<Client, GameS> currentGames = new ConcurrentHashMap<>();
    private static final Map<Client, Client> playersWaitingForPlayer = new ConcurrentHashMap<>();
    private static final Queue<Client> playersWaitingForGame = new ConcurrentLinkedQueue<>();
    private static final Map<Client, Board> playersWaitingBoards = new ConcurrentHashMap<>();

    public static void startRandomGame(Client player) throws UserMessageException {
        if (!playersWaitingForGame.isEmpty()) {
            startGame(player, playersWaitingForGame.poll()); // Start a game with the player who has been waiting for the most time
        }
        else {
            startWait(player, null);
        }
    }

    public static void startGameWithPlayer(Client player, Long otherPlayerID) throws UserMessageException {

        // Other player must be online
        if (UserS.isUserLoggedIn(otherPlayerID)) {
            Client otherPlayer = UserS.clientFromID(otherPlayerID);

            // Other player is waiting for a game too -> start game immediately
            if (playersWaitingForGame.contains(otherPlayer)) {
                startGame(player, otherPlayer);
            }
            // Other player is playing
            else {
                if (currentGames.containsKey(player)) {
                    throw new UserMessageException("Invited user is currently playing");
                }
                // Other player is online but available
                // \-> player starts waiting for otherPlayer
                // \-> send invitation to otherPlayer
                else {
                    startWait(player, otherPlayer);
                    sendInvitation(player, otherPlayer);
                }
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

    private static void startGame(Client p1, Client p2) {
        GameS game = new GameS(p1, playersWaitingBoards.get(p1), p2, playersWaitingBoards.get(p2));
        
        currentGames.put(p1, game);
        currentGames.put(p2, game);
    }

    private static void startWait(Client player, Client otherPlayer) {
        // Waiting for random game
        if (otherPlayer == null) {
            playersWaitingForGame.add(player);

            /*try {
                player.updateGameScreen(
                        new GameScreenInfo(
                                UserS.usernameFromClient(player), "<waiting>", false,
                                "Waiting for opponent", "You can place ships", false
                        )
                );
            }
            catch (ConnectionException ex) {
                Logger.getLogger(GameS.class.getName()).log(Level.SEVERE, null, ex);
            }*/
        }
        // Waiting for a specific player
        else {
            playersWaitingForPlayer.put(otherPlayer, player);

            /*try {
                player.updateGameScreen(
                        new GameScreenInfo(
                                UserS.usernameFromClient(player), "<waiting for " + UserS.usernameFromClient(otherPlayer) + ">", false,
                                "Waiting for opponent to accept invitation", "You can place ships", false
                        )
                );
            }
            catch (ConnectionException ex) {
                Logger.getLogger(GameS.class.getName()).log(Level.SEVERE, null, ex);
            }*/ // TODO: finish
        }
    }
    
    public static void togglePlaceShipOnSquare(Client player, Coord pos) {
        if (isUserPlaying(player)) {
            currentGames.get(player).togglePlaceShipOnSquare_(player, pos);
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

    public static void fireShot(Client player, Coord pos) {
        if (isUserPlaying(player)) {
            currentGames.get(player).fireShot_(player, pos);
        }
        else {
            Logger.getLogger(GameS.class.getName()).log(Level.SEVERE, "Player {0} cannot fire shot because he's not playing any game", player);
        }
    }

    public static boolean isUserPlaying(Client user) {
        return currentGames.containsKey(user);
    }
    
    public static boolean isUserWaiting(Client user) {
        return playersWaitingForGame.contains(user) || playersWaitingForPlayer.containsValue(user);
    }

    /// --------
    public final Client player1;
    public final Client player2;

    private boolean player1Ready;
    private boolean player2Ready;
    private boolean player1Turn;
    //private final HashSet<Client> spectators; TODO: make this set thread-safe?

    private final Board p1Board;
    private final Board p2Board;

    private GameS(Client p1, Board p1Board, Client p2, Board p2Board) {

        // Set players
        player1 = p1;
        player2 = p2;
        
        // Set boards
        this.p1Board = p1Board != null ? p1Board : new Board();
        this.p2Board = p2Board != null ? p2Board : new Board();

        // Assign turns
        player1Turn = new Random().nextBoolean();

        // TODO: create game in DB
        // Refresh game screen info
        refreshClientInfo();
    }

    private void fireShot_(Client player, Coord pos) {
        // Verify if player is in this game
        // Verify current turn
        // Verify player can shoot there
        // other verifications?
        // Everything ok:
        // Save shot on DB
        // Change turn or end game
        refreshClientInfo();
    }

    private void togglePlaceShipOnSquare_(Client player, Coord pos) {
        // Verify if player is in this game
        // Verify player can still place ships
        // other verifications?
        // Everything ok:
        // Send inf to corresponding board
        refreshClientInfo();
    }

    private void refreshClientInfo() {
        // Send GameScreenInfo and BoardInfo to everyone (players and spectators)
    }

}
