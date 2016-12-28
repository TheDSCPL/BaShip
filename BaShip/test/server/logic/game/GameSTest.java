package server.logic.game;

import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import server.database.Database;
import sharedlib.structs.BoardUIInfo;
import sharedlib.structs.GameUIInfo;

/**
 *
 * @author Alex
 */
public class GameSTest {
    
    public GameSTest() {
        
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

    @Before
    public void setUp() {
        client1 = new MockClient();
        client2 = new MockClient();
        client3 = new MockClient();
        client4 = new MockClient();
    }

    @After
    public void tearDown() {
        client1 = null;
        client2 = null;
        client3 = null;
        client4 = null;
    }

    /**
     * Test of startRandomGame method, of class GameS.
     */
    @Test
    public void testStartRandomGame() throws Exception {
        System.out.println("startRandomGame");
        
        // Update screen for waiting
        client1.clearCalled();
        GameS.Actions.startRandomGame(client1);
        assertTrue(client1.wasCalled("updateGameScreen"));
        assertFalse(((GameUIInfo)client1.callArgument("updateGameScreen")).showRightBoard);
        assertTrue(client1.wasCalled("updateGameBoard"));
        assertTrue(((BoardUIInfo)client1.callArgument("updateGameBoard")).leftBoard);
        
        // Update screen for game with player (for both players)
        client1.clearCalled();
        client2.clearCalled();
        GameS.Actions.startRandomGame(client2);
        assertTrue(client1.wasCalled("updateGameScreen"));
        assertFalse(((GameUIInfo)client1.callArgument("updateGameScreen")).showRightBoard);
        assertTrue(client2.wasCalled("updateGameScreen"));
        assertFalse(((GameUIInfo)client2.callArgument("updateGameScreen")).showRightBoard);
        assertTrue(client2.wasCalled("updateGameBoard"));
        assertTrue(((BoardUIInfo)client2.callArgument("updateGameBoard")).leftBoard);
    }
    
}
