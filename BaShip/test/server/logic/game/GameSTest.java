package server.logic.game;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import server.database.Database;

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
        
        //GameS.startRandomGame(client);
    }

    /**
     * Test of answerGameInvitation method, of class GameS.
     */
    @Test
    public void testAnswerGameInvitation() throws Exception {
        System.out.println("answerGameInvitation");
        
        //GameS.answerGameInvitation(invitedPlayer, accepted);
    }

    /**
     * Test of clickReadyButton method, of class GameS.
     */
    @Test
    public void testClickReadyButton() {
        System.out.println("clickReadyButton");
        
        //GameS.clickReadyButton(player);
    }

    /**
     * Test of clientClickedLeftBoard method, of class GameS.
     */
    @Test
    public void testClientClickedLeftBoard() {
        System.out.println("clientClickedLeftBoard");
        
        //GameS.clientClickedLeftBoard(client, pos);
    }

    /**
     * Test of clientClickedRightBoard method, of class GameS.
     */
    @Test
    public void testClientClickedRightBoard() {
        System.out.println("clientClickedRightBoard");
        
        //GameS.clientClickedRightBoard(player, pos);
    }

    /**
     * Test of clientClosedGame method, of class GameS.
     */
    @Test
    public void testClientClosedGame() {
        System.out.println("clientClosedGame");
        
        //GameS.clientClosedGame(client);
    }

    /**
     * Test of clientDisconnected method, of class GameS.
     */
    @Test
    public void testClientDisconnected() {
        System.out.println("clientDisconnected");
        
        //GameS.clientDisconnected(client);
    }

    /**
     * Test of showNextMove method, of class GameS.
     */
    @Test
    public void testShowNextMove() throws Exception {
        System.out.println("showNextMove");
        
        //GameS.showNextMove(client);
    }

    /**
     * Test of showPreviousMove method, of class GameS.
     */
    @Test
    public void testShowPreviousMove() {
        System.out.println("showPreviousMove");
        
        //GameS.showPreviousMove(client);
    }

    /**
     * Test of sendGameMessage method, of class GameS.
     */
    @Test
    public void testSendGameMessage() throws Exception {
        System.out.println("sendGameMessage");
        
        //GameS.sendGameMessage(player, message);
    }

    /**
     * Test of clientDoubleClickedUser method, of class GameS.
     */
    @Test
    public void testClientDoubleClickedUser() throws Exception {
        System.out.println("clientDoubleClickedUser");
        
        //GameS.clientDoubleClickedUser(client, playerID);
    }

    /**
     * Test of clientDoubleClickedGame method, of class GameS.
     */
    @Test
    public void testClientDoubleClickedGame() throws Exception {
        System.out.println("clientDoubleClickedGame");
        
        //GameS.clientDoubleClickedGame(client, gameID);
    }
    
}
