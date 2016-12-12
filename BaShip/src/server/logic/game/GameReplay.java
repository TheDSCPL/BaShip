package server.logic.game;

import server.conn.Client;

/**
 *
 * @author Alex
 */
public class GameReplay {

    public final Client client;

    public GameReplay(Client client, Long gameID) {
        this.client = client;
    }

    // TODO: synchronized methods?
    public void showNextMove() {

    }

    public void showPreviousMove() {

    }

    public void clientClosedGame() {

    }

    public void clientDisconnected() {

    }

    /*private void refreshGameScreenForClient() {
        GameUIInfo info = new GameUIInfo(
                UserS.usernameOfClient(client),
                UserS.usernameOfClient(opponent(client)),
                gameHasStarted(),
                opponentReady ? "Opponent is ready" : "Opponent is placing ships",
                placingShips ? "You can place ships" : "Please wait for opponent",
                placingShips && boardForPlayer(client).placedShipsAreValid(),
                gameStarted ? (client == player1 ? p1Turn : !p1Turn) : false,
                gameStarted ? (client == player1 ? !p1Turn : p1Turn) : false,
                UIType.Play, null, null, null
        );

        try {
            client.updateGameScreen(info);
        }
        catch (ConnectionException ex) {
            Logger.getLogger(GamePlay.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void refreshBoardsForClient() {
        BoardUIInfo leftBoard, rightBoard;
        boolean playing = gameHasStarted();

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
            Logger.getLogger(GamePlay.class.getName()).log(Level.SEVERE, null, ex);
        }
    }*/

}
