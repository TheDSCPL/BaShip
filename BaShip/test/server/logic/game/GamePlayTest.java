package server.logic.game;

import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import server.conn.Client;
import sharedlib.utils.Coord;
/**
 *
 * @author Alex
 */
public class GamePlayTest {
    
    public GamePlayTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of addSpectator method, of class GamePlay.
     */
    @Test
    public void testAddSpectator() {
        System.out.println("addSpectator");
        Client client = null;
        GamePlay instance = null;
        instance.addSpectator(client);
        // TODO review the generated test code and remove the default call to fail.
        assertFalse(true);
    }

    /**
     * Test of removeSpectator method, of class GamePlay.
     */
    @Test
    public void testRemoveSpectator() {
        System.out.println("removeSpectator");
        Client client = null;
        GamePlay instance = null;
        instance.removeSpectator(client);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of hasSpectator method, of class GamePlay.
     */
    @Test
    public void testHasSpectator() {
        System.out.println("hasSpectator");
        Client client = null;
        GamePlay instance = null;
        boolean expResult = false;
        boolean result = instance.hasSpectator(client);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of hasPlayer method, of class GamePlay.
     */
    @Test
    public void testHasPlayer() {
        System.out.println("hasPlayer");
        Client client = null;
        GamePlay instance = null;
        boolean expResult = false;
        boolean result = instance.hasPlayer(client);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of clientClosedGame method, of class GamePlay.
     */
    @Test
    public void testClientClosedGame() {
        System.out.println("clientClosedGame");
        Client client = null;
        GamePlay instance = null;
        instance.clientClosedGame(client);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of clientDisconnected method, of class GamePlay.
     */
    @Test
    public void testClientDisconnected() {
        System.out.println("clientDisconnected");
        Client client = null;
        GamePlay instance = null;
        instance.clientDisconnected(client);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of clickReadyButton method, of class GamePlay.
     */
    @Test
    public void testClickReadyButton() {
        System.out.println("clickReadyButton");
        Client player = null;
        GamePlay instance = null;
        instance.clickReadyButton(player);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of playerClickedRightBoard method, of class GamePlay.
     */
    @Test
    public void testPlayerClickedRightBoard() {
        System.out.println("playerClickedRightBoard");
        Client player = null;
        Coord pos = null;
        GamePlay instance = null;
        instance.playerClickedRightBoard(player, pos);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of playerClickedLeftBoard method, of class GamePlay.
     */
    @Test
    public void testPlayerClickedLeftBoard() {
        System.out.println("playerClickedLeftBoard");
        Client player = null;
        Coord pos = null;
        GamePlay instance = null;
        instance.playerClickedLeftBoard(player, pos);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of playerSentMessage method, of class GamePlay.
     */
    @Test
    public void testPlayerSentMessage() throws Exception {
        System.out.println("playerSentMessage");
        Client player = null;
        String text = "";
        GamePlay instance = null;
        instance.playerSentMessage(player, text);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isPlayer method, of class GamePlay.
     */
    @Test
    public void testIsPlayer() {
        System.out.println("isPlayer");
        Client client = null;
        GamePlay instance = null;
        boolean expResult = false;
        boolean result = instance.isPlayer(client);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isSpectator method, of class GamePlay.
     */
    @Test
    public void testIsSpectator() {
        System.out.println("isSpectator");
        Client client = null;
        GamePlay instance = null;
        boolean expResult = false;
        boolean result = instance.isSpectator(client);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of gameHasStarted method, of class GamePlay.
     */
    @Test
    public void testGameHasStarted() {
        System.out.println("gameHasStarted");
        GamePlay instance = null;
        boolean expResult = false;
        boolean result = instance.gameHasStarted();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCurrentMoveNumber method, of class GamePlay.
     */
    @Test
    public void testGetCurrentMoveNumber() {
        System.out.println("getCurrentMoveNumber");
        GamePlay instance = null;
        int expResult = 0;
        int result = instance.getCurrentMoveNumber();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
