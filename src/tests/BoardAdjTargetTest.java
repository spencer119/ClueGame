package tests;

import clueGame.Board;
import clueGame.BoardCell;
import org.junit.jupiter.api.*;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Tests for adjacency and targets on the Board
 */
public class BoardAdjTargetTest {
    private static Board board;

    @BeforeAll
    public static void setUp() {
        // Board is singleton, get the only instance
        board = Board.getInstance();
        // set the file names to use my config files
        board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
        // Initialize will load config files
        board.initialize();
    }

    @Test // Test adjacency on walkway cells
    public void testAdjacencyWalkway() {
        Set<BoardCell> test = board.getAdjList(2, 7);

        assertTrue(test.contains(board.getCell(2, 6))); // Check all adjacent cells
        assertTrue(test.contains(board.getCell(2, 8)));
        assertTrue(test.contains(board.getCell(3, 7)));
        assertTrue(test.contains(board.getCell(1, 7)));

        assertEquals(4, test.size()); // Make sure there are only 4 adjacent cells
    }

    @Test // Test that non-center room cells have no adjacent cells
    public void testNonCenter() {
        Set<BoardCell> test = board.getAdjList(2, 2);
        assertEquals(0, test.size());
    }

    @Test // Test adjacency on edge of board
    public void testBoardEdge() {
        Set<BoardCell> test = board.getAdjList(12, 0);

        assertTrue(test.contains(board.getCell(13, 0)));
        assertTrue(test.contains(board.getCell(11, 0)));
        assertTrue(test.contains(board.getCell(12, 1)));

        assertEquals(3, test.size());
    }

    @Test // Test that you cant enter a room without a doorway
    public void testBesideRoomNoDoor() {
        Set<BoardCell> test = board.getAdjList(0, 1);

        assertTrue(test.contains(board.getCell(0, 0)));
        assertTrue(test.contains(board.getCell(0, 2)));

        assertEquals(2, test.size());
    }

    @Test // Test adjList next to door
    public void testAdjDoorway() {
        // Get the adjacency list for a cell (2, 0) which is beside a doorway
        Set<BoardCell> testList = board.getAdjList(2, 0);

        // Assert that the adjacency list contains the expected cells
        assertTrue(testList.contains(board.getCell(1, 0))); // Walkway to the left
        assertTrue(testList.contains(board.getCell(3, 3))); // Room with a doorway

        // Assert that the size of the adjacency list is as expected
        assertEquals(2, testList.size());

        // Repeat the above for two other cells
        testList = board.getAdjList(17, 3);
        assertEquals(2, testList.size());
        assertTrue(testList.contains(board.getCell(19, 1)));
        assertTrue(testList.contains(board.getCell(17, 2)));

        testList = board.getAdjList(9, 7);
        assertEquals(4, testList.size());
        assertTrue(testList.contains(board.getCell(9, 10)));
        assertTrue(testList.contains(board.getCell(9, 6)));
        assertTrue(testList.contains(board.getCell(8, 7)));
        assertTrue(testList.contains(board.getCell(10, 7)));
    }

    @Test // Test that you can use a secret passage to move between room centers
    public void testTargetsRoomSecret() {
        BoardCell testing = board.getCell(3, 3);
        Set<BoardCell> testList = board.getAdjList(3, 3);
        assertTrue(testList.contains(board.getCell(19, 1)));
        assertTrue(testList.contains(board.getCell(2, 0)));
    }

    @Test // Test target list on walkway cells
    public void testTargetWalkway() {
        // test a roll of 1
        board.calcTargets(board.getCell(12, 1), 1);
        Set<BoardCell> targets = board.getTargets();

        assertEquals(4, targets.size());
        assertTrue(targets.contains(board.getCell(11, 1)));
        assertTrue(targets.contains(board.getCell(13, 1)));
        assertTrue(targets.contains(board.getCell(12, 2)));
        assertTrue(targets.contains(board.getCell(12, 0)));

        // test a roll of 2
        board.calcTargets(board.getCell(0, 0), 2);
        targets = board.getTargets();
        assertEquals(2, targets.size());
        assertTrue(targets.contains(board.getCell(0, 2)));
        assertTrue(targets.contains(board.getCell(2, 0)));
    }

    @Test
    public void testTargetsAtDoor() {
        // test a roll of 1, at door
        board.calcTargets(board.getCell(2, 0), 1);
        Set<BoardCell> targets = board.getTargets();
        assertTrue(targets.contains(board.getCell(1, 0)));
        assertTrue(targets.contains(board.getCell(3, 3)));
    }

    @Test // Test targets of a room with no secret passage
    public void testTargetsRoomNoSecret() {
        // test a roll of 1, at door
        board.calcTargets(board.getCell(9, 2), 1);
        Set<BoardCell> targets = board.getTargets();
        assertEquals(1, targets.size()); // Make sure doorway is only target
        assertTrue(targets.contains(board.getCell(11, 1)));
    }

    @Test // Test targets one cell away from a blocked walkway
    public void testTargetsWalkwayBlocked() {
        board.getCell(13, 1).setOccupied(true);
        board.calcTargets(board.getCell(13, 0), 1);

        Set<BoardCell> targets = board.getTargets();

        assertEquals(2, targets.size());
        assertTrue(targets.contains(board.getCell(14, 0)));
        assertTrue(targets.contains(board.getCell(12, 0)));
        assertFalse(targets.contains(board.getCell(13, 1)));
    }
}
