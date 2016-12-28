package server.logic.game;

import java.math.BigInteger;
import java.security.SecureRandom;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import server.database.Database;
import sharedlib.exceptions.UserMessageException;
import sharedlib.structs.GameUIInfo;
import sharedlib.structs.Message;
import sharedlib.utils.Coord;

public class GamePlayTest {

    public GamePlayTest() {

    }

    @BeforeClass
    public static void setUpClass() {
        Database.testing = true;
    }

    @AfterClass
    public static void tearDownClass() {
        Database.testing = false;
    }

    MockClient client1, client2, client3, client4;
    Board board1, board2;
    GamePlay game;

    @Before
    public void setUp() throws UserMessageException {
        client1 = new MockClient();
        client2 = new MockClient();
        client3 = new MockClient();
        client4 = new MockClient();
        board1 = new Board();
        board2 = new Board();
        game = new GamePlay(client1, board1, client2, board2);
    }

    @After
    public void tearDown() {
        client1 = null;
        client2 = null;
        client3 = null;
        client4 = null;
        board1 = null;
        board2 = null;
        game = null;
    }

    private void placeShipsP1() {
        BoardTest.toggleSquaresToPlaceAllShips(board1);
    }

    private void placeShipsP2() {
        BoardTest.toggleSquaresToPlaceAllShips(board2);
    }

    /**
     * Test of addSpectator method, of class GamePlay.
     */
    @Test
    public void testAddSpectator() throws UserMessageException {
        System.out.println("addSpectator");

        game.addSpectator(client1);
        assertFalse(game.isSpectator(client1));

        assertFalse(game.isSpectator(client3));

        game.addSpectator(client3);
        assertTrue(game.isSpectator(client3));
        assertTrue(client3.wasCalled("updateGameScreen"));
        assertTrue(client3.wasCalled("updateGameBoard"));
        assertTrue(client3.wasCalled("clearGameMessages"));
        client3.clearCalled();

        game.playerClickedLeftBoard(client1, new Coord(0, 0));
        assertTrue(client3.wasCalled("updateGameScreen"));
        assertTrue(client3.wasCalled("updateGameBoard"));
        client3.clearCalled();

        game.playerSentMessage(client1, "MESSAGE");
        assertTrue(client3.wasCalled("informAboutGameMessage"));
        client3.clearCalled();
    }

    /**
     * Test of removeSpectator method, of class GamePlay.
     */
    @Test
    public void testRemoveSpectator() throws UserMessageException {
        System.out.println("removeSpectator");

        game.addSpectator(client3);
        assertTrue(game.isSpectator(client3));
        client3.clearCalled();

        game.removeSpectator(client3);
        assertFalse(game.isSpectator(client3));

        game.playerClickedLeftBoard(client1, new Coord(0, 0));
        assertFalse(client3.wasCalled("updateGameScreen"));
        assertFalse(client3.wasCalled("updateGameBoard"));
        client3.clearCalled();

        game.playerSentMessage(client1, "MESSAGE");
        assertFalse(client3.wasCalled("informAboutGameMessage"));
        client3.clearCalled();
    }

    /**
     * Test of clientClosedGame method, of class GamePlay.
     */
    @Test
    public void testClientClosedGame() {
        System.out.println("clientClosedGame");

        game.addSpectator(client3);

        game.addSpectator(client4);
        game.clientClosedGame(client4);
        assertFalse(game.isSpectator(client4));

        game.clientClosedGame(client1);
        assertTrue(game.hasFinished());
        assertFalse(client1.wasCalled("showMessageAndCloseGame"));
        assertTrue(client2.wasCalled("showMessageAndCloseGame"));
        assertTrue(client3.wasCalled("showMessageAndCloseGame"));
    }

    /**
     * Test of clientDisconnected method, of class GamePlay.
     */
    @Test
    public void testClientDisconnected() {
        System.out.println("clientDisconnected");

        game.addSpectator(client3);

        game.addSpectator(client4);
        game.clientDisconnected(client4);
        assertFalse(game.isSpectator(client4));

        game.clientDisconnected(client1);
        assertTrue(game.hasFinished());
        assertTrue(client2.wasCalled("showMessageAndCloseGame"));
        assertTrue(client3.wasCalled("showMessageAndCloseGame"));
    }

    /**
     * Test of clickReadyButton method, of class GamePlay.
     */
    @Test
    public void testClickReadyButton() {
        System.out.println("clickReadyButton");

        game.addSpectator(client3);

        // Not a player
        game.clickReadyButton(client3);
        assertFalse(client1.wasCalled("updateGameInfo"));
        client1.clearCalled();

        // No ships
        game.clickReadyButton(client1);
        assertFalse(client1.wasCalled("updateGameInfo"));
        client1.clearCalled();

        // Invalid ships
        game.playerClickedLeftBoard(client1, new Coord(0, 0));
        client1.clearCalled();
        game.clickReadyButton(client1);
        assertFalse(client1.wasCalled("updateGameScreen"));
        game.playerClickedLeftBoard(client1, new Coord(0, 0)); // Undo
        client1.clearCalled();

        // Ship placement accepted but game hasn't started
        placeShipsP1();
        game.clickReadyButton(client1);
        System.out.println(client1.calledMethods);
        assertTrue(client1.wasCalled("updateGameScreen"));
        assertFalse(((GameUIInfo) client1.callArgument("updateGameScreen")).showRightBoard);
        client1.clearCalled();

        // Already clicked ready
        game.clickReadyButton(client1);
        assertFalse(client1.wasCalled("updateGameScreen"));
        client1.clearCalled();

        // Both ready, game started
        placeShipsP2();
        game.clickReadyButton(client2);
        assertTrue(((GameUIInfo) client1.callArgument("updateGameScreen")).showRightBoard);
        assertTrue(((GameUIInfo) client2.callArgument("updateGameScreen")).showRightBoard);
        assertTrue(game.hasStarted());
    }

    /**
     * Test of isPlayer method, of class GamePlay.
     */
    @Test
    public void testIsPlayer() {
        System.out.println("isPlayer");

        game.addSpectator(client3);

        assertTrue(game.isPlayer(client1));
        assertTrue(game.isPlayer(client2));
        assertFalse(game.isPlayer(client3));
        assertFalse(game.isPlayer(client4));
    }

    /**
     * Test of isSpectator method, of class GamePlay.
     */
    @Test
    public void testIsSpectator() {
        System.out.println("isSpectator");

        game.addSpectator(client3);

        assertFalse(game.isSpectator(client1));
        assertFalse(game.isSpectator(client2));
        assertTrue(game.isSpectator(client3));
        assertFalse(game.isSpectator(client4));
    }

    /**
     * Test of getCurrentMoveNumber method, of class GamePlay.
     */
    @Test
    public void testGetCurrentMoveNumber() {
        System.out.println("getCurrentMoveNumber");

        placeShipsP1();
        placeShipsP2();
        game.clickReadyButton(client1);
        game.clickReadyButton(client2);

        assertEquals(game.getCurrentMoveNumber(), 0);

        game.playerClickedRightBoard(game.getCurrentPlayer(), new Coord(0, 0));
        assertEquals(game.getCurrentMoveNumber(), 1);

        game.playerClickedRightBoard(game.getCurrentPlayer(), new Coord(0, 0));
        assertEquals(game.getCurrentMoveNumber(), 2);
    }

    /**
     * Test of playerClickedRightBoard method, of class GamePlay.
     */
    @Test
    public void testPlayerClickedRightBoard() {
        System.out.println("playerClickedRightBoard");
    }

    /**
     * Test of playerClickedLeftBoard method, of class GamePlay.
     */
    @Test
    public void testPlayerClickedLeftBoard() {
        System.out.println("playerClickedLeftBoard");

        // Spectator
        client3.clearCalled();
        game.addSpectator(client3);
        game.playerClickedLeftBoard(client3, new Coord(0, 0));
        assertFalse(client3.wasCalled("updateGameScreen"));
        
        // Player placing ships
        game.addSpectator(client3);
        game.playerClickedLeftBoard(client3, new Coord(0, 0));
        assertFalse(client3.wasCalled("updateGameScreen"));
    }

    /**
     * Test of playerSentMessage method, of class GamePlay.
     */
    @Test
    public void testPlayerSentMessage() throws Exception {
        System.out.println("playerSentMessage");
        SecureRandom random = new SecureRandom();
        
        game.addSpectator(client3);

        for (int i = 0; i < 200; i++) {
            client1.clearCalled();
            client2.clearCalled();

            String str = new BigInteger(130, random).toString(32);
            game.playerSentMessage(random.nextBoolean() ? client1 : client2, str);
            
            assertEquals(((Message) client1.callArgument("informAboutGameMessage")).text, str);
            assertEquals(((Message) client2.callArgument("informAboutGameMessage")).text, str);
            assertEquals(((Message) client3.callArgument("informAboutGameMessage")).text, str);
        }
    }
}
