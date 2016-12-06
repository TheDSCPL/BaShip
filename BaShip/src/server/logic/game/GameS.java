package server.logic.game;

import java.sql.SQLException;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.conn.Client;
import server.logic.UserS;
import sharedlib.exceptions.ConnectionException;
import sharedlib.exceptions.UserMessageException;
import sharedlib.structs.BoardUIInfo;
import sharedlib.structs.GameUIInfo;
import sharedlib.structs.GameUIInfo.UIType;
import sharedlib.utils.Coord;

/**
 * Class responsible for managing games being currently played and replayed by
 * users.
 *
 * @author Alex
 */
public class GameS {

    private static final Set<GamePlay> gamePlaySet = Collections.newSetFromMap(new ConcurrentHashMap<>());

    private static class Info {

        //private static final Map<Client, Client> playersWaitingForPlayer = new ConcurrentHashMap<>();
        private static final Queue<Client> playersWaitingForGame = new ConcurrentLinkedQueue<>();
        private static final Map<Client, Board> playersWaitingBoards = new ConcurrentHashMap<>();

        public static Client nextPlayerWaitingForGame() {
            return playersWaitingForGame.poll();
        }

        public static boolean existsPlayersWaitingForRandomGame() {
            return !playersWaitingForGame.isEmpty();
        }

        public static boolean isWaitingForRandomGame(Client c) {
            return playersWaitingForGame.contains(c);
        }
        
        public static Board waitingBoardForPlayer(Client client) {
            if (!playersWaitingBoards.containsKey(client)) {
                playersWaitingBoards.put(client, new Board());
            }

            return playersWaitingBoards.get(client);
        }

        public static void addPlayerWaiting(Client client) {
            playersWaitingForGame.add(client);
        }

        public static void removePlayerWaiting(Client client) {
            playersWaitingForGame.remove(client);
        }

        private static void removeWaitingBoardForPlayer(Client player) {
            playersWaitingBoards.remove(player);
        }
    }

    /**
     * Start a game with another random player. The player may be put on a
     * waiting list if there are no other players available.
     *
     * @param client The client that wants to start a game. Cannot be null.
     * @throws UserMessageException
     */
    public static void startRandomGame(Client client) throws UserMessageException {
        if (Info.existsPlayersWaitingForRandomGame()) {
            startGame(client, Info.nextPlayerWaitingForGame()); // Start a game with the player who has been waiting for the most time
        }
        else {
            startWait(client, null);
        }
    }

    /**
     * Start a game with another player. The other player must be online and
     * available (not in a game), or else a {@code UserMessageException} is
     * thrown. If the player is available, an invitation is sent to him.
     *
     * @param client The client that wants to start a game
     * @param otherPlayerID The player who is to be invited
     * @throws UserMessageException
     */
    public static void startGameWithPlayer(Client client, Long otherPlayerID) throws UserMessageException {

        // Other player must be online
        /*if (UserS.isUserLoggedIn(otherPlayerID)) {
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
                //sendInvitation(client, otherPlayer);
            }
        }
        else {
            throw new UserMessageException("Invited user is not online at the moment");
        }*/
    }

    /**
     * Inform this class that the client who has received a game invitation has
     * answered said invitation (either by accepting or denying it).
     *
     * @param client The client who received the invitation
     * @param acepted True if the client accepted the invitation and wants to
     * start a game
     */
    /*public static void answerGameInvitation(Client client, boolean acepted) {
        // TODO: finish
    }*/

 /*private static void sendInvitation(Client from, Client to) {
        try {
            to.sendGameInvitation(); // TODO: finish
        }
        catch (ConnectionException ex) {
            Logger.getLogger(GameS.class.getName()).log(Level.SEVERE, null, ex);
        }
    }*/
    private static void startGame(Client player1, Client player2) throws UserMessageException {
        GamePlay game;
        try {
            game = new GamePlay(player1, Info.waitingBoardForPlayer(player1), player2, Info.waitingBoardForPlayer(player2));
            gamePlaySet.add(game);
            Info.removeWaitingBoardForPlayer(player1);
            Info.removeWaitingBoardForPlayer(player2);
        }
        catch (SQLException ex) {
            Logger.getLogger(GameS.class.getName()).log(Level.SEVERE, null, ex);
            throw new UserMessageException("Could not access DB and create game: " + ex.getMessage());
        }
    }

    private static void startWait(Client clientWaiting, Client targetClient) {
        // Waiting for random game
        if (targetClient == null) {
            Info.addPlayerWaiting(clientWaiting);
        }
        // Waiting for a specific player
        /*else {
            playersWaitingForPlayer.put(targetClient, clientWaiting);
        }*/

        updateGameScreenForClient(clientWaiting);

        try {
            BoardUIInfo bi = Info.waitingBoardForPlayer(clientWaiting).getBoardInfoPlacingShips(true);
            bi.leftBoard = true;
            clientWaiting.updateGameBoard(bi);
        }
        catch (ConnectionException ex) {
            Logger.getLogger(GameS.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * When placing ships, inform this class that this player has finished
     * placing all the ships and is ready to start a game. The player must be
     * playing a game, and still be placing ships. Also, all the ships must be
     * in valid positions.
     *
     * @param player The player who is ready to play the game
     */
    public static void clickReadyButton(Client player) {
        if (isClientPlaying(player)) {
            gamePlayFromPlayer(player).clickReadyButton(player);
        }
    }

    public static void spectateGame(Client client, Long gameID) {
        GamePlay gp = gamePlayFromGameID(gameID);

        if (gp != null) {
            gp.addSpectator(client);
        }
    }

    /**
     * Informs the state machine of the game that the player clicked on the left
     * board, which then performs the appropriate operations.
     *
     * @param client
     * @param pos
     */
    public static void clientClickedLeftBoard(Client client, Coord pos) {
        if (isClientPlaying(client)) {
            gamePlayFromPlayer(client).playerClickedLeftBoard(client, pos);
        }
        else if (isClientWaiting(client)) {
            Board board = Info.waitingBoardForPlayer(client);
            board.togglePlaceShipOnSquare(pos);

            try {
                BoardUIInfo bi = board.getBoardInfoPlacingShips(true);
                bi.leftBoard = true;
                client.updateGameBoard(bi);
            }
            catch (ConnectionException ex) {
                Logger.getLogger(GameS.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Informs the state machine of the game that the player clicked on the
     * right board, which then performs the appropriate operations.
     *
     * @param player
     * @param pos
     */
    public static void clientClickedRightBoard(Client player, Coord pos) {
        if (isClientPlaying(player)) {
            gamePlayFromPlayer(player).playerClickedRightBoard(player, pos);
        }
    }

    /**
     * Inform this class that a player closed the game.
     *
     * @param client The player who closed the game.
     */
    public synchronized static void clientClosedGame(Client client) {
        if (isClientPlaying(client)) {
            gamePlayFromPlayer(client).clientClosedGame(client);
        }
        else if (isClientSpectating(client)) {
            gamePlayFromSpectator(client).clientClosedGame(client);
        }
        else if (Info.isWaitingForRandomGame(client)) {
            Info.removePlayerWaiting(client);
        }
    }

    public synchronized static void sendGameMessage(Client player, String message) throws SQLException, ConnectionException {
        if (isClientPlaying(player)) {
            gamePlayFromPlayer(player).playerSentMessage(player, message);
        }
        else {
            Logger.getLogger(GameS.class.getName()).log(Level.SEVERE, "Player {0} cannot send message because he's not playing any game", player);
        }
    }

    static void gameFinished(GamePlay game) {
        gamePlaySet.remove(game);
    }

    /**
     * Inform this class that a client disconnected. Should be called regardless
     * of the client being in a game currently or not. If the client is in a
     * game, this method cancels the game.
     *
     * @param client The client who disconnected.
     */
    public static void clientDisconnected(Client client) {
        if (isClientPlaying(client)) {
            gamePlayFromPlayer(client).clientDisconnected(client);
        }
        else if (isClientSpectating(client)) {
            gamePlayFromSpectator(client).clientDisconnected(client);
        }
        else if (isClientWaiting(client)) {
            Info.removePlayerWaiting(client);
            Info.removeWaitingBoardForPlayer(client);
        }
    }

    /**
     * @param client
     * @return True if the client is in a game currently. It returns false
     * otherwise, specially when the user is on the game screen, but waiting,
     * not playing.
     * @see GameS#isClientWaiting(Client)
     */
    public static boolean isClientPlaying(Client client) {
        return gamePlayFromPlayer(client) != null;
    }

    public static boolean isClientSpectating(Client client) {
        return gamePlayFromSpectator(client) != null;
    }

    /**
     * @param client
     * @return True if the client is waiting for a game.
     * @see GameS#isClientPlaying(Client)
     */
    public static boolean isClientWaiting(Client client) {
        return Info.isWaitingForRandomGame(client);// || playersWaitingForPlayer.containsValue(client);
    }
    
    /**
     *
     * @param gameID The game id. This must be an id of a game currently running
     * @return The number of moves that have been played already in the game
     * (includes both players' moves).
     */
    public static Integer getGameCurrentMoveNumber(Long gameID) {
        GamePlay gp = gamePlayFromGameID(gameID);

        if (gp != null) {
            return gp.getCurrentMoveNumber();
        }

        return null;
    }

    private static GamePlay gamePlayFromGameID(Long gameID) {
        return gamePlaySet.stream().filter(g -> Objects.equals(g.gameID, gameID)).findAny().orElse(null);
    }

    private static GamePlay gamePlayFromPlayer(Client player) {
        return gamePlaySet.stream().filter(g -> g.hasPlayer(player)).findAny().orElse(null);
    }

    private static GamePlay gamePlayFromSpectator(Client spectator) {
        return gamePlaySet.stream().filter(g -> g.hasSpectator(spectator)).findAny().orElse(null);
    }

    private static void updateGameScreenForClient(Client client) {
        GameUIInfo gsi = null;

        if (Info.isWaitingForRandomGame(client)) {
            gsi = new GameUIInfo(
                    UserS.usernameFromClient(client), "<waiting>", false,
                    "Waiting for opponent", "You can place ships", false,
                    false, false,
                    UIType.Play
            );
        }
        /*else if (playersWaitingForPlayer.containsValue(client)) {
            gsi = new GameUIInfo(
                    UserS.usernameFromClient(client), "<waiting for " + "OTHER PLAYER TODO" + ">", false, // TODO: UserS.usernameFromClient(otherPlayer) 
                    "Waiting for opponent", "You can place ships", false,
                    false, false
            );
        }*/

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
