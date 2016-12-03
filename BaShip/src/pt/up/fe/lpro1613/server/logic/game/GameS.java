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
import pt.up.fe.lpro1613.sharedlib.structs.GameUIInfo;
import pt.up.fe.lpro1613.sharedlib.utils.Coord;

/**
 * Class responsible for managing games being currently played and replayed by
 * users.
 *
 * @author Alex
 */
public class GameS {

    private static class Info {

        private static final Map<Long, GamePlay> currentGamesPlay = new ConcurrentHashMap<>();
        private static final Map<Client, GamePlay> currentGamesPlayFromUser = new ConcurrentHashMap<>();
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

        public static boolean isPlaying(Client c) {
            return currentGamesPlayFromUser.containsKey(c);
        }

        public static boolean gameIDisBeingPlayed(Long gameID) {
            return currentGamesPlay.containsKey(gameID);
        }

        public static GamePlay gameFromGameID(Long gameID) {
            return currentGamesPlay.get(gameID);
        }

        public static GamePlay gameFromPlayer(Client client) {
            return currentGamesPlayFromUser.get(client);
        }

        public static Board waitingBoardForPlayer(Client client) {
            if (!playersWaitingBoards.containsKey(client)) {
                playersWaitingBoards.put(client, new Board());
            }
            
            return playersWaitingBoards.get(client);
        }

        public static void setUsersPlayingGame(Client p1, Client p2, GamePlay gp) {
            currentGamesPlayFromUser.put(p1, gp);
            currentGamesPlayFromUser.put(p2, gp);
        }

        public static void registerGame(GamePlay gp) {
            currentGamesPlay.put(gp.gameID, gp);
        }

        public static void addPlayerWaiting(Client client) {
            playersWaitingForGame.add(client);
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
            Info.setUsersPlayingGame(player1, player2, game);
            Info.registerGame(game);
        }
        catch (SQLException ex) {
            Logger.getLogger(GameS.class.getName()).log(Level.SEVERE, null, ex);
            throw new UserMessageException("Could not access DB and create game: " + ex.getMessage());
        }
    }

    private static void startWait(Client clientWaiting, Client targetClient) {
        // Waiting for random game
        if (targetClient == null) {
            Info.addPlayerWaiting(targetClient);
        }
        // Waiting for a specific player
        /*else {
            playersWaitingForPlayer.put(targetClient, clientWaiting);
        }*/

        updateGameScreenForClient(clientWaiting);
    }

    /**
     * When placing ships, inform this class that this client has clicked on a
     * square on the grid. The player must be playing a game, and still be
     * placing ships.
     *
     * @param player The player who clicked on the board
     * @param pos The position of the board he cliked on
     */
    public static void togglePlaceShipOnSquare(Client player, Coord pos) {
        if (isClientPlaying(player)) {
            Info.gameFromPlayer(player).togglePlaceShipOnSquare(player, pos);
        }
        else {
            Board board = Info.waitingBoardForPlayer(player);
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
     * When placing ships, inform this class that this player has finished
     * placing all the ships and is ready to start a game. The player must be
     * playing a game, and still be placing ships. Also, all the ships must be
     * in valid positions.
     *
     * @param player The player who is ready to play the game
     */
    public static void clickReadyButton(Client player) {
        if (isClientPlaying(player)) {
            Info.gameFromPlayer(player).clickReadyButton(player);
        }
        else {
            Logger.getLogger(GameS.class.getName()).log(Level.SEVERE, "Player {0} cannot click ready because he's not playing any game", player);
        }
    }

    /**
     * When playing (after placing ships), inform this class that the player
     * intends to fire a missile on the given coordinates. Requires that it's
     * this client's turn to play.
     *
     * @param player The player who fired the shot.
     * @param pos The coordinate of where the shot was fired, on the board of
     * the opponent.
     */
    public static void fireShot(Client player, Coord pos) {
        if (isClientPlaying(player)) {
            Info.gameFromPlayer(player).fireShot(player, pos);
        }
        else {
            Logger.getLogger(GameS.class.getName()).log(Level.SEVERE, "Player {0} cannot fire shot because he's not playing any game", player);
        }
    }

    /**
     * Inform this class that a player closed the game.
     *
     * @param client The player who closed the game.
     */
    public static void closeGame(Client client) {
        if (Info.isPlaying(client)) {
            Info.gameFromPlayer(client).gameClosedByClient(client);
        }
    }

    static void gameFinished(GamePlay game) {
        // TODO: finish
    }

    /**
     * Inform this class that a client disconnected. Should be called regardless
     * of the client being in a game currently or not. If the client is in a
     * game, this method cancels the game.
     *
     * @param client The client who disconnected.
     */
    public static void clientDisconnected(Client client) {
        // TODO: finish
        //currentGamesPlayFromUser.get(client).clientDisconnected(client); // currentGamesPlayFromUser.get(client) may be null
    }

    /**
     * @param client
     * @return True if the client is in a game currently. It returns false
     * otherwise, specially when the user is on the game screen, but waiting,
     * not playing.
     * @see GameS#isClientWaiting(Client)
     */
    public static boolean isClientPlaying(Client client) {
        return Info.isPlaying(client);
    }

    /**
     * @param client
     * @return True if the client is waiting for a game.
     * @see GameS#isClientPlaying(Client)
     */
    public static boolean isClientWaiting(Client client) {
        return Info.isWaitingForRandomGame(client);// || playersWaitingForPlayer.containsValue(client);
    }

    /*public static boolean isGameRunning(Long gameID) {
        return currentGamesPlay.get(gameID).gameHasStarted();
    }*/
    /**
     *
     * @param gameID The game id. This must be an id of a game currently running
     * @return The number of moves that have been played already in the game
     * (includes both players' moves).
     */
    public static Integer getGameCurrentMoveNumber(Long gameID) {
        if (Info.gameIDisBeingPlayed(gameID)) {
            return Info.gameFromGameID(gameID).getCurrentMoveNumber();
        }

        return null;
    }

    private static void updateGameScreenForClient(Client client) {
        GameUIInfo gsi = null;

        if (Info.isWaitingForRandomGame(client)) {
            gsi = new GameUIInfo(
                    UserS.usernameFromClient(client), "<waiting>", false,
                    "Waiting for opponent", "You can place ships", false,
                    false, false
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
