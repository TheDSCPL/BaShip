package server.logic.game;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import sharedlib.structs.BoardUIInfo;
import sharedlib.structs.BoardUIInfo.SquareFill;
import sharedlib.structs.Ship;
import sharedlib.utils.Coord;

/**
 *
 * @author Alex
 */
public class BoardTest {

    public BoardTest() {
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

    private void toggleSquaresToPlaceAllShips(Board b) {
        b.togglePlaceShipOnSquare(new Coord(1, 1));

        b.togglePlaceShipOnSquare(new Coord(1, 3));

        b.togglePlaceShipOnSquare(new Coord(1, 5));

        b.togglePlaceShipOnSquare(new Coord(1, 7));

        b.togglePlaceShipOnSquare(new Coord(3, 1));
        b.togglePlaceShipOnSquare(new Coord(3, 2));

        b.togglePlaceShipOnSquare(new Coord(3, 4));
        b.togglePlaceShipOnSquare(new Coord(3, 5));

        b.togglePlaceShipOnSquare(new Coord(3, 7));
        b.togglePlaceShipOnSquare(new Coord(3, 8));

        b.togglePlaceShipOnSquare(new Coord(5, 1));
        b.togglePlaceShipOnSquare(new Coord(5, 2));
        b.togglePlaceShipOnSquare(new Coord(5, 3));

        b.togglePlaceShipOnSquare(new Coord(5, 5));
        b.togglePlaceShipOnSquare(new Coord(5, 6));
        b.togglePlaceShipOnSquare(new Coord(5, 7));

        b.togglePlaceShipOnSquare(new Coord(7, 1));
        b.togglePlaceShipOnSquare(new Coord(7, 2));
        b.togglePlaceShipOnSquare(new Coord(7, 3));
        b.togglePlaceShipOnSquare(new Coord(7, 4));
    }

    private void shootOnAllShips(Board b) {
        b.shootOnSquare(new Coord(1, 1));

        b.shootOnSquare(new Coord(1, 3));

        b.shootOnSquare(new Coord(1, 5));

        b.shootOnSquare(new Coord(1, 7));

        b.shootOnSquare(new Coord(3, 1));
        b.shootOnSquare(new Coord(3, 2));

        b.shootOnSquare(new Coord(3, 4));
        b.shootOnSquare(new Coord(3, 5));

        b.shootOnSquare(new Coord(3, 7));
        b.shootOnSquare(new Coord(3, 8));

        b.shootOnSquare(new Coord(5, 1));
        b.shootOnSquare(new Coord(5, 2));
        b.shootOnSquare(new Coord(5, 3));

        b.shootOnSquare(new Coord(5, 5));
        b.shootOnSquare(new Coord(5, 6));
        b.shootOnSquare(new Coord(5, 7));

        b.shootOnSquare(new Coord(7, 1));
        b.shootOnSquare(new Coord(7, 2));
        b.shootOnSquare(new Coord(7, 3));
        b.shootOnSquare(new Coord(7, 4));
    }

    /**
     * Test of togglePlaceShipOnSquare method, of class Board.
     */
    @Test
    public void testTogglePlaceShipOnSquare() {
        System.out.println("togglePlaceShipOnSquare");
        Board board = new Board();

        board.togglePlaceShipOnSquare(new Coord(1, 3));
        board.togglePlaceShipOnSquare(new Coord(1, 4));
        assertArrayEquals(board.getShips().toArray(), new Ship[]{new Ship(1, 3, 2, true)});

        board.togglePlaceShipOnSquare(new Coord(1, 5));
        assertArrayEquals(board.getShips().toArray(), new Ship[]{new Ship(1, 3, 3, true)});

        board.togglePlaceShipOnSquare(new Coord(1, 3));
        assertArrayEquals(board.getShips().toArray(), new Ship[]{new Ship(1, 4, 2, true)});
    }

    /**
     * Test of placedShipsAreValid method, of class Board.
     */
    @Test
    public void testPlacedShipsAreValid() {
        System.out.println("placedShipsAreValid");
        Board board = new Board();

        board.togglePlaceShipOnSquare(new Coord(3, 2));
        assertFalse(board.placedShipsAreValid());

        board.togglePlaceShipOnSquare(new Coord(3, 2));
        toggleSquaresToPlaceAllShips(board);
        assertTrue(board.placedShipsAreValid());

        board.togglePlaceShipOnSquare(new Coord(1, 6));
        assertFalse(board.placedShipsAreValid());
    }

    /**
     * Test of setShips method, of class Board.
     */
    @Test
    public void testSetShips() {
        System.out.println("setShips");
        Board board = new Board();
        List<Ship> list = Arrays.asList(new Ship[]{new Ship(0, 0, 0, true)});

        board.setShips(list);
        assertEquals(list, board.getShips());

    }

    /**
     * Test of getShips method, of class Board.
     */
    @Test
    public void testGetShips() {
        System.out.println("getShips");
        Board board = new Board();

        Ship[] expected = {
            new Ship(1, 1, 1, true),
            new Ship(1, 3, 1, true),
            new Ship(1, 5, 1, true),
            new Ship(1, 7, 1, true),
            new Ship(3, 1, 2, true),
            new Ship(3, 4, 2, true),
            new Ship(3, 7, 2, true),
            new Ship(5, 1, 3, true),
            new Ship(5, 5, 3, true),
            new Ship(7, 1, 4, true)
        };

        toggleSquaresToPlaceAllShips(board);
        assertEquals(new HashSet(Arrays.asList(expected)), new HashSet(board.getShips()));
    }

    /**
     * Test of canShootOnSquare & shootOnSquare methods, of class Board.
     */
    @Test
    public void testCanShootOnSquare_shootOnSquare() {
        System.out.println("canShootOnSquare_shootOnSquare");
        Board board = new Board();
        Coord c = new Coord(1, 2);

        assertTrue(board.canShootOnSquare(c));
        board.shootOnSquare(c);
        assertFalse(board.canShootOnSquare(c));
    }

    /**
     * Test of removeShotFromSquare method, of class Board.
     */
    @Test
    public void testRemoveShotFromSquare() {
        System.out.println("removeShotFromSquare");
        Board board = new Board();
        Coord c = new Coord(7, 8);

        assertTrue(board.canShootOnSquare(c));
        board.shootOnSquare(c);
        assertFalse(board.canShootOnSquare(c));
        board.removeShotFromSquare(c);
        assertTrue(board.canShootOnSquare(c));
    }

    /**
     * Test of allShipsAreShot method, of class Board.
     */
    @Test
    public void testAllShipsAreShot() {
        System.out.println("allShipsAreShot");
        Board board = new Board();

        board.togglePlaceShipOnSquare(new Coord(1, 1));
        assertFalse(board.allShipsAreShot());

        board.shootOnSquare(new Coord(2, 2));
        assertFalse(board.allShipsAreShot());

        board.shootOnSquare(new Coord(1, 1));
        assertTrue(board.allShipsAreShot());

        board.togglePlaceShipOnSquare(new Coord(1, 1));
        toggleSquaresToPlaceAllShips(board);
        assertFalse(board.allShipsAreShot());

        shootOnAllShips(board);
        assertTrue(board.allShipsAreShot());
    }

    /**
     * Test of getBoardInfoPlacingShips method, of class Board.
     */
    @Test
    public void testGetBoardInfoPlacingShips_ShowEverythingFalse() {
        System.out.println("testGetBoardInfoPlacingShips_ShowEverythingFalse");
        Board board = new Board();

        // Place ships
        toggleSquaresToPlaceAllShips(board);

        // Get info
        BoardUIInfo result = board.getBoardInfoPlacingShips(false);

        // Assertions
        result.board.forEach((c, v) -> assertEquals(v, SquareFill.Empty));
        result.bottomInfo.stream().forEach((v) -> assertEquals(v, SquareFill.Empty));
    }

    /**
     * Test of getBoardInfoPlacingShips method, of class Board.
     */
    @Test
    public void testGetBoardInfoPlacingShips_ShowEverythingTrue() {
        System.out.println("testGetBoardInfoPlacingShips_ShowEverythingTrue");
        Board board = new Board();

        // Place ships
        toggleSquaresToPlaceAllShips(board);

        // Get info
        BoardUIInfo result = board.getBoardInfoPlacingShips(true);

        // Assertions
        assertEquals(result.board.get(new Coord(1, 1)), SquareFill.GraySquare);
        assertEquals(result.board.get(new Coord(3, 1)), SquareFill.GraySquare);
        assertEquals(result.board.get(new Coord(5, 2)), SquareFill.GraySquare);
        assertEquals(result.board.get(new Coord(7, 3)), SquareFill.GraySquare);

        result.bottomInfo.stream().forEach((v) -> assertEquals(v, SquareFill.GraySquare));
    }

    /**
     * Test of getBoardInfoPlaying method, of class Board.
     */
    @Test
    public void testGetBoardInfoPlaying_ShowEverythingFalse() {
        System.out.println("testGetBoardInfoPlaying_ShowEverythingFalse");
        Board board = new Board();
        BoardUIInfo result;

        // Place all ships
        toggleSquaresToPlaceAllShips(board);

        // Test case 1
        board.shootOnSquare(new Coord(3, 1));
        result = board.getBoardInfoPlaying(false);
        assertEquals(result.board.get(new Coord(3, 1)), SquareFill.GraySquareRedCross);
        result.bottomInfo.stream().forEach((v) -> assertEquals(v, SquareFill.Empty));

        // Test case 2
        board.shootOnSquare(new Coord(3, 2));
        result = board.getBoardInfoPlaying(false);
        assertEquals(result.board.get(new Coord(3, 2)), SquareFill.GraySquareRedCross);
        assertEquals(result.bottomInfo.get(10), SquareFill.RedCross);
        assertEquals(result.bottomInfo.get(11), SquareFill.RedCross);

        // Test case 3
        board.shootOnSquare(new Coord(3, 3));
        result = board.getBoardInfoPlaying(false);
        assertEquals(result.board.get(new Coord(3, 3)), SquareFill.BlueDiamond);

        // Test case 4
        shootOnAllShips(board);
        result = board.getBoardInfoPlaying(false);
        result.bottomInfo.stream().forEach((v) -> assertEquals(v, SquareFill.RedCross));
    }

    /**
     * Test of getBoardInfoPlaying method, of class Board.
     */
    @Test
    public void testGetBoardInfoPlaying_ShowEverythingTrue() {
        System.out.println("testGetBoardInfoPlaying_ShowEverythingTrue");
        Board board = new Board();
        BoardUIInfo result;

        // Place all ships
        toggleSquaresToPlaceAllShips(board);

        // Test case 1
        result = board.getBoardInfoPlaying(true);
        result.bottomInfo.stream().forEach((v) -> assertEquals(v, SquareFill.Empty));

        // Test case 2
        board.shootOnSquare(new Coord(7, 1));
        result = board.getBoardInfoPlaying(true);
        assertEquals(result.board.get(new Coord(7, 1)), SquareFill.GraySquareRedCross);
        assertEquals(result.bottomInfo.get(0), SquareFill.RedCross);

        // Test case 3
        board.shootOnSquare(new Coord(3, 3));
        result = board.getBoardInfoPlaying(true);
        assertEquals(result.board.get(new Coord(3, 3)), SquareFill.BlueDiamond);
    }

}
