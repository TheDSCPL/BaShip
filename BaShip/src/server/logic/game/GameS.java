package server.logic.game;

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

    public static class GameInfo {

        private static final Set<GamePlay> gamePlaySet = Collections.newSetFromMap(new ConcurrentHashMap<>());
        private static final Set<GameReplay> gameReplaySet = Collections.newSetFromMap(new ConcurrentHashMap<>());

        private static void addGamePlay(GamePlay game) {
            gamePlaySet.add(game);
        }

        private static void removeGamePlay(GamePlay game) {
            gamePlaySet.remove(game);
        }

        private static void addGameReplay(GameReplay game) {
            gameReplaySet.add(game);
        }

        private static void removeGameReplay(GameReplay game) {
            gameReplaySet.remove(game);
        }

        /**
         *
         * @param gameID The game id. This must be an id of a game currently
         * running
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

        private static GameReplay gameReplayFromClient(Client client) {
            return gameReplaySet.stream().filter(g -> g.client == client).findAny().orElse(null);
        }
    }

    public static class PlayerInfo {

        /**
         * @param client
         * @return True if the client is in a game currently. It returns false
         * otherwise, specially when the user is on the game screen, but
         * waiting, not playing.
         * @see GameS#isClientWaiting(Client)
         */
        public static boolean isPlaying(Client client) {
            return GameInfo.gamePlayFromPlayer(client) != null;
        }

        public static boolean isSpectating(Client client) {
            return GameInfo.gamePlayFromSpectator(client) != null;
        }

        public static boolean isReplaying(Client client) {
            return GameInfo.gameReplayFromClient(client) != null;
        }

        /**
         * @param client
         * @return True if the client is waiting for a game.
         * @see GameS#isClientPlaying(Client)
         */
        public static boolean isWaitingForGame(Client client) {
            return Info.isWaitingForRandomGame(client);
        }

        public static boolean isWaitingForPlayer(Client client) {
            return Info.isWaitingForPlayer(client);
        }
    }

    public static class Actions {

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
         * Inform this class that the client who has received a game invitation
         * has answered said invitation (either by accepting or denying it).
         *
         * @param invitedPlayer The client who received the invitation
         * @param accepted True if the client accepted the invitation and wants
         * to start a game
         * @throws sharedlib.exceptions.UserMessageException
         */
        public static void answerGameInvitation(Client invitedPlayer, boolean accepted) throws UserMessageException {
            Client playerWhoSentInvitation = Info.playerWaitingForHim(invitedPlayer);

            if (playerWhoSentInvitation != null) {
                if (accepted) {
                    startGame(invitedPlayer, playerWhoSentInvitation);
                }
                else {
                    try {
                        playerWhoSentInvitation.showMessageAndCloseGame("Player " + UserS.usernameOfClient(invitedPlayer) + " declined your invitation.");
                    }
                    catch (ConnectionException ex) {
                        Logger.getLogger(GameS.class.getName()).log(Level.SEVERE, "Could not send 'invitation declined' message to inviting player " + playerWhoSentInvitation, ex);
                    }
                }

                Info.removePlayerWaitingForPlayer(playerWhoSentInvitation);
            }
        }

        /**
         * When placing ships, inform this class that this player has finished
         * placing all the ships and is ready to start a game. The player must
         * be playing a game, and still be placing ships. Also, all the ships
         * must be in valid positions.
         *
         * @param player The player who is ready to play the game
         */
        public static void clickReadyButton(Client player) {
            if (PlayerInfo.isPlaying(player)) {
                GameInfo.gamePlayFromPlayer(player).clickReadyButton(player);
            }
        }

        /**
         * Informs the state machine of the game that the player clicked on the
         * left board, which then performs the appropriate operations.
         *
         * @param client
         * @param pos
         */
        public static void clientClickedLeftBoard(Client client, Coord pos) {
            if (PlayerInfo.isPlaying(client)) {
                GameInfo.gamePlayFromPlayer(client).playerClickedLeftBoard(client, pos);
            }
            else if (PlayerInfo.isWaitingForGame(client) || PlayerInfo.isWaitingForPlayer(client)) {
                Board board = Info.waitingBoardForPlayer(client);
                board.togglePlaceShipOnSquare(pos);

                try {
                    BoardUIInfo bi = board.getBoardInfoPlacingShips(true);
                    bi.leftBoard = true;
                    client.updateGameBoard(bi);
                }
                catch (ConnectionException ex) {
                    Logger.getLogger(GameS.class.getName()).log(Level.SEVERE, "Could not update board of " + client, ex);
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
            if (PlayerInfo.isPlaying(player)) {
                GameInfo.gamePlayFromPlayer(player).playerClickedRightBoard(player, pos);
            }
        }

        /**
         * Inform this class that a player closed the game.
         *
         * @param client The player who closed the game.
         */
        public synchronized static void clientClosedGame(Client client) {
            if (PlayerInfo.isPlaying(client)) {
                GameInfo.gamePlayFromPlayer(client).clientClosedGame(client);
            }
            else if (PlayerInfo.isSpectating(client)) {
                GameInfo.gamePlayFromSpectator(client).clientClosedGame(client);
            }
            else if (PlayerInfo.isReplaying(client)) {
                GameInfo.gameReplayFromClient(client).clientClosedGame();
            }
            else if (PlayerInfo.isWaitingForGame(client)) {
                Info.removePlayerWaitingForGame(client);
            }
            else if (PlayerInfo.isWaitingForPlayer(client)) { // Player waiting for player
                try {
                    Info.playerInvitedBy(client).closeGameInvitation();
                }
                catch (ConnectionException ex) {
                    Logger.getLogger(GameS.class.getName()).log(Level.SEVERE, "Could not close game invitation message of " + Info.playerInvitedBy(client), ex);
                }

                Info.removePlayerWaitingForPlayer(client);
            }
        }

        /**
         * Inform this class that a client disconnected. Should be called
         * regardless of the client being in a game currently or not. If the
         * client is in a game, this method cancels the game.
         *
         * @param client The client who disconnected.
         */
        public static void clientDisconnected(Client client) {
            if (PlayerInfo.isPlaying(client)) {
                GameInfo.gamePlayFromPlayer(client).clientDisconnected(client);
            }
            else if (PlayerInfo.isSpectating(client)) {
                GameInfo.gamePlayFromSpectator(client).clientDisconnected(client);
            }
            else if (PlayerInfo.isReplaying(client)) {
                GameInfo.gameReplayFromClient(client).clientDisconnected();
            }
            else if (PlayerInfo.isWaitingForGame(client)) {
                Info.removePlayerWaitingForGame(client);
                Info.removeWaitingBoardForPlayer(client);
            }
            else if (PlayerInfo.isWaitingForPlayer(client)) {
                // Disconnected player waiting for player

                try {
                    Info.playerInvitedBy(client).closeGameInvitation();
                }
                catch (ConnectionException ex) {
                    Logger.getLogger(GameS.class.getName()).log(Level.SEVERE, "Could not close game invitation message of " + Info.playerInvitedBy(client), ex);
                }

                Info.removePlayerWaitingForPlayer(client);
            }

            // TODO: Alex: Refactor: to function? (repeated code (from where?))
            Client playerWhoSentInvitation = Info.playerWaitingForHim(client);
            if (playerWhoSentInvitation != null) { // Disconnected player was invited by someone
                try {
                    playerWhoSentInvitation.showMessageAndCloseGame("Player " + UserS.usernameOfClient(client) + " disconnected.");
                }
                catch (ConnectionException ex) {
                    Logger.getLogger(GameS.class.getName()).log(Level.SEVERE, "Could not close game of player " + playerWhoSentInvitation + " who sent invitation", ex);
                }

                Info.removePlayerWaitingForPlayer(playerWhoSentInvitation);
            }
        }

        public static void showNextMove(Client client) throws UserMessageException {
            GameReplay gr = GameInfo.gameReplayFromClient(client);
            System.out.println(gr);
            if (gr != null) {
                gr.showNextMove();
            }
        }

        public static void showPreviousMove(Client client) {
            GameReplay gr = GameInfo.gameReplayFromClient(client);
            if (gr != null) {
                gr.showPreviousMove();
            }
        }

        public synchronized static void sendGameMessage(Client player, String message) throws UserMessageException {
            if (PlayerInfo.isPlaying(player)) {
                GameInfo.gamePlayFromPlayer(player).playerSentMessage(player, message);
            }
        }

        public static void clientDoubleClickedUser(Client client, Long playerID) throws UserMessageException {
            Client clickedClient = UserS.clientWithUserID(playerID);

            if (clickedClient != null && client != clickedClient) { // Online and another player
                if (PlayerInfo.isPlaying(clickedClient)) {
                    spectateGame(client, GameInfo.gamePlayFromPlayer(clickedClient).gameID);
                }
                else if (PlayerInfo.isSpectating(clickedClient)) {
                    spectateGame(client, GameInfo.gamePlayFromSpectator(clickedClient).gameID);
                }
                else if (PlayerInfo.isReplaying(clickedClient)) {
                    startReplay(client, GameInfo.gameReplayFromClient(clickedClient).gameID);
                }
                else if (PlayerInfo.isWaitingForGame(clickedClient)) {
                    startGame(client, clickedClient);
                }
                else if (PlayerInfo.isWaitingForPlayer(clickedClient)) {
                    // Can't do anything
                }
                else { // Just online and available -> invite
                    int x; // Ignore, just here because of auto-indentation issues in NetBeans

                    if (Info.playerWaitingForHim(clickedClient) == null) {
                        try {
                            clickedClient.sendGameInvitation(UserS.usernameOfClient(client));
                            startWait(client, clickedClient);
                        }
                        catch (ConnectionException ex) {
                            Logger.getLogger(GameS.class.getName()).log(Level.SEVERE, "Could not send invitation to " + clickedClient, ex);
                            throw new UserMessageException("Could not invite user");
                        }
                    }
                    else {
                        throw new UserMessageException("Could not invite user because user has already been invited by another player");
                    }
                }
            }
        }

        public static void clientDoubleClickedGame(Client client, Long gameID) throws UserMessageException {
            // Game is currently being played -> spectate
            if (GameInfo.gamePlayFromGameID(gameID) != null) {
                spectateGame(client, gameID);
            }
            // Game has finished (with or without winner)
            else {
                startReplay(client, gameID);
            }
        }

        private static void startGame(Client player1, Client player2) throws UserMessageException {
            GamePlay game;
            game = new GamePlay(player1, Info.waitingBoardForPlayer(player1), player2, Info.waitingBoardForPlayer(player2));
            GameInfo.addGamePlay(game);

            // TODO: Alex: Refactor: organize this?
            Info.removePlayerWaitingForGame(player1);
            Info.removePlayerWaitingForGame(player2);
            Info.removeWaitingBoardForPlayer(player1);
            Info.removeWaitingBoardForPlayer(player2);
        }

        private static void startReplay(Client client, Long gameID) throws UserMessageException {
            GameReplay game = new GameReplay(client, gameID);
            GameInfo.addGameReplay(game);
        }

        private static void startWait(Client clientWaiting, Client targetClient) {
            // Waiting for random game
            if (targetClient == null) {
                Info.addPlayerWaiting(clientWaiting);
            }
            // Waiting for a specific player
            else {
                Info.addPlayerWaitingForPlayer(clientWaiting, targetClient);
            }

            Helpers.updateGameScreenForClient(clientWaiting);

            try {
                BoardUIInfo bi = Info.waitingBoardForPlayer(clientWaiting).getBoardInfoPlacingShips(true);
                bi.leftBoard = true;
                clientWaiting.updateGameBoard(bi);
            }
            catch (ConnectionException ex) {
                Logger.getLogger(GameS.class.getName()).log(Level.SEVERE, "Could not update board of " + clientWaiting, ex);
            }
        }

        private static void spectateGame(Client client, Long gameID) {
            GamePlay gp = GameInfo.gamePlayFromGameID(gameID);

            if (gp != null) {
                gp.addSpectator(client);
            }
        }

    }

    private static class Info { // TODO: Alex: Refactor: organize this?

        private static final Map<Client, Client> playersWaitingForPlayer1 = new ConcurrentHashMap<>();
        private static final Map<Client, Client> playersWaitingForPlayer2 = new ConcurrentHashMap<>();

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

        public static void removePlayerWaitingForGame(Client client) {
            playersWaitingForGame.remove(client);
        }

        private static void removeWaitingBoardForPlayer(Client player) {
            playersWaitingBoards.remove(player);
        }

        /////////////////
        public static boolean isWaitingForPlayer(Client c) {
            return playersWaitingForPlayer1.containsKey(c);
        }

        public static void addPlayerWaitingForPlayer(Client player, Client waitingForPlayer) {
            playersWaitingForPlayer1.put(player, waitingForPlayer);
            playersWaitingForPlayer2.put(waitingForPlayer, player);
        }

        public static Client playerWaitingForHim(Client player) {
            return playersWaitingForPlayer2.get(player);
        }

        public static Client playerInvitedBy(Client player) {
            return playersWaitingForPlayer1.get(player);
        }

        public static void removePlayerWaitingForPlayer(Client playerWhoSentInvite) {
            playersWaitingForPlayer2.remove(playersWaitingForPlayer1.remove(playerWhoSentInvite));
        }
        /////////////////
    }

    static class Callbacks {

        static void gamePlayFinished(GamePlay game) {
            GameInfo.removeGamePlay(game);
        }

        static void gameReplayFinished(GameReplay game) {
            GameInfo.removeGameReplay(game);
        }
    }

    private static class Helpers {

        private static void updateGameScreenForClient(Client client) {
            GameUIInfo gsi = null;

            if (Info.isWaitingForRandomGame(client)) {
                gsi = new GameUIInfo(
                        UserS.usernameOfClient(client), "<waiting>", false,
                        "Waiting for opponent", "You can place ships", false,
                        false, false,
                        UIType.Play, null, null, null
                );
            }
            else if (Info.playerInvitedBy(client) != null) {
                gsi = new GameUIInfo(
                        UserS.usernameOfClient(client), "<waiting for " + UserS.usernameOfClient(Info.playerInvitedBy(client)) + ">", false,
                        "Waiting for opponent", "You can place ships", false,
                        false, false,
                        UIType.Play, null, null, null
                );
            }

            if (gsi != null) {
                try {
                    client.updateGameScreen(gsi);
                }
                catch (ConnectionException ex) {
                    Logger.getLogger(GameS.class.getName()).log(Level.SEVERE, "Could not update game screen of " + client, ex);
                }
            }
        }

    }
}
