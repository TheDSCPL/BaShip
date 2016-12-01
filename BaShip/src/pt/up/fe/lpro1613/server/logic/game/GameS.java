package pt.up.fe.lpro1613.server.logic.game;

import java.sql.SQLException;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import pt.up.fe.lpro1613.server.conn.Client;
import pt.up.fe.lpro1613.server.logic.UserS;
import pt.up.fe.lpro1613.sharedlib.exceptions.ConnectionException;
import pt.up.fe.lpro1613.sharedlib.exceptions.UserMessageException;
import pt.up.fe.lpro1613.sharedlib.tuples.GameUIInfo;
import pt.up.fe.lpro1613.sharedlib.utils.Coord;

/**
 * XXX
 * @author Alex
 */
public class GameS {

    private static final Map<Long, GamePlay> currentGamesPlay = new ConcurrentHashMap<>();
    private static final Map<Client, GamePlay> currentGamesPlayFromUser = new ConcurrentHashMap<>();
    private static final Map<Client, Client> playersWaitingForPlayer = new ConcurrentHashMap<>();
    private static final Queue<Client> playersWaitingForGame = new ConcurrentLinkedQueue<>();
    private static final Map<Client, Board> playersWaitingBoards = new ConcurrentHashMap<>();

    /**
     * XXX
     * @param client
     * @throws UserMessageException 
     */
    public static void startRandomGame(Client client) throws UserMessageException {
        if (!playersWaitingForGame.isEmpty()) {
            startGame(client, playersWaitingForGame.poll()); // Start a game with the player who has been waiting for the most time
        }
        else {
            startWait(client, null);
        }
    }

    /**
     * XXX
     * @param client
     * @param otherPlayerID
     * @throws UserMessageException 
     */
    public static void startGameWithPlayer(Client client, Long otherPlayerID) throws UserMessageException {

        // Other player must be online
        if (UserS.isUserLoggedIn(otherPlayerID)) {
            Client otherPlayer = UserS.clientFromID(otherPlayerID);

            // Other player is waiting for a game too -> start game immediately
            if (playersWaitingForGame.contains(otherPlayer)) {
                startGame(client, otherPlayer);
            }
            // Other player is playing
            else if (currentGamesPlayFromUser.containsKey(client)) {
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

    /**
     * XXX
     * @param client
     * @param acepted 
     */
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
            currentGamesPlayFromUser.put(player1, game);
            currentGamesPlayFromUser.put(player2, game);
            currentGamesPlay.put(game.gameID, game);
        }
        catch (SQLException ex) {
            Logger.getLogger(GameS.class.getName()).log(Level.SEVERE, null, ex);
            throw new UserMessageException("Could not access DB and create game: " + ex.getMessage());
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

    /**
     * XXX
     * @param player
     * @param pos 
     */
    public static void togglePlaceShipOnSquare(Client player, Coord pos) {
        if (isClientPlaying(player)) {
            currentGamesPlayFromUser.get(player).togglePlaceShipOnSquare(player, pos);
        }
        else {
            if (!playersWaitingBoards.containsKey(player)) {
                playersWaitingBoards.put(player, new Board());
            }

            Board board = playersWaitingBoards.get(player);
            board.togglePlaceShipOnSquare(pos);

            try {
                player.updateGameBoard(board.getBoardInfoNotPlaying(true));
            }
            catch (ConnectionException ex) {
                Logger.getLogger(GameS.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * XXX
     * @param player 
     */
    public static void clickReadyButton(Client player) {
        if (isClientPlaying(player)) {
            currentGamesPlayFromUser.get(player).clickReadyButton(player);
        }
        else {
            Logger.getLogger(GameS.class.getName()).log(Level.SEVERE, "Player {0} cannot click ready because he's not playing any game", player);
        }
    }

    /**
     * XXX
     * @param player
     * @param pos 
     */
    public static void fireShot(Client player, Coord pos) {
        if (isClientPlaying(player)) {
            currentGamesPlayFromUser.get(player).fireShot(player, pos);
        }
        else {
            Logger.getLogger(GameS.class.getName()).log(Level.SEVERE, "Player {0} cannot fire shot because he's not playing any game", player);
        }
    }
    
    /**
     * XXX
     * @param client 
     */
    public static void closeGame(Client client) {
        currentGamesPlayFromUser.get(client).gameClosedByClient(client);
    }
    
    /**
     * XXX
     * @param game 
     */
    public static void gameFinished(GamePlay game) {
        // TODO: finish
    }

    /**
     * XXX
     * @param client 
     */
    public static void clientDisconnected(Client client) {
        // TODO: finish
        //currentGamesPlayFromUser.get(client).clientDisconnected(client); // currentGamesPlayFromUser.get(client) may be null
    }

    /**
     * XXX
     * @param client
     * @return 
     */
    public static boolean isClientPlaying(Client client) {
        return currentGamesPlayFromUser.containsKey(client);
    }

    /**
     * XXX
     * @param client
     * @return 
     */
    public static boolean isClientWaiting(Client client) {
        return playersWaitingForGame.contains(client) || playersWaitingForPlayer.containsValue(client);
    }

    /*public static boolean isGameRunning(Long gameID) {
        return currentGamesPlay.get(gameID).gameHasStarted();
    }*/

    /**
     * XXX
     * @param gameID
     * @return 
     */
    public static Integer getGameCurrentMoveNumber(Long gameID) {
        return currentGamesPlay.get(gameID).getCurrentMoveNumber();
    }

    private static void updateGameScreenForClient(Client client) {
        GameUIInfo gsi = null;

        if (playersWaitingForGame.contains(client)) {
            gsi = new GameUIInfo(
                    UserS.usernameFromClient(client), "<waiting>", false,
                    "Waiting for opponent", "You can place ships", false,
                    false, false
            );
        }
        else if (playersWaitingForPlayer.containsValue(client)) {
            gsi = new GameUIInfo(
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
