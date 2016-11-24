package server.logic;

import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.conn.Client;
import sharedlib.exceptions.ConnectionException;
import sharedlib.exceptions.UserMessageException;
import sharedlib.tuples.GameScreenInfo;

public class GameS {

    private static final Map<Client, GameS> currentGames = new ConcurrentHashMap<>();
    private static final Map<Client, Client> playersWaitingForPlayer = new ConcurrentHashMap<>();
    private static final Queue<Client> playersWaitingForGame = new ConcurrentLinkedQueue<>();

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
            else if (currentGames.containsKey(player)) {
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
        else {
            throw new UserMessageException("Invited user is not online at the moment");
        }
    }

    public static void answerGameInvitation(Client client, boolean acepted) {
        // TODO: finish
    }

    private static void startGame(Client p1, Client p2) {
        GameS game = new GameS(p1, p2);
        
        // TODO: create game in DB

        currentGames.put(p1, game);
        currentGames.put(p2, game);

        try {
            p1.showGameScreen(
                    new GameScreenInfo(
                            UserS.usernameFromClient(p1), UserS.usernameFromClient(p2), false,
                            "TEXT1", "TEXT2", false
                    )
            );

            p2.showGameScreen(
                    new GameScreenInfo(
                            UserS.usernameFromClient(p2), UserS.usernameFromClient(p1), false,
                            "TEXT1", "TEXT2", false
                    )
            );
        }
        catch (ConnectionException ex) {
            Logger.getLogger(GameS.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void startWait(Client player, Client otherPlayer) {
        // Waiting for random game
        if (otherPlayer == null) {
            playersWaitingForGame.add(player);

            try {
                player.showGameScreen(
                        new GameScreenInfo(
                                UserS.usernameFromClient(player), "?????", false,
                                "Waiting for opponent", "You can place ships", false
                        )
                );
            }
            catch (ConnectionException ex) {
                Logger.getLogger(GameS.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        // Waiting for a specific player
        else {
            playersWaitingForPlayer.put(otherPlayer, player);

            try {
                player.showGameScreen(
                        new GameScreenInfo(
                                UserS.usernameFromClient(player), UserS.usernameFromClient(otherPlayer), false,
                                "Waiting for opponent to accept invitation", "You can place ships", false
                        )
                );
            }
            catch (ConnectionException ex) {
                Logger.getLogger(GameS.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private static void sendInvitation(Client from, Client to) {
        try {
            to.sendGameInvitation(); // TODO: finish
        }
        catch (ConnectionException ex) {
            Logger.getLogger(GameS.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public final Client player1;
    public final Client player2;
    private final Set<Client> spectators = new HashSet<>(); // TODO: import java.util.concurrent.ConcurrentHashMap;

    private GameS(Client p1, Client p2) {
        player1 = p1;
        player2 = p2;
    }

}
