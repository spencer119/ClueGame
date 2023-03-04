package tests;

import experiment.TestBoard;
import experiment.TestBoardCell;
import org.junit.jupiter.api.*;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the Board class movement and adjacency
 */
public class BoardTestsExp {
    TestBoard board;
    /**
     * Setup the board before each test
     */
    @BeforeEach
    public void setup() {
        board = new TestBoard();
    }

//    @Test
//    public void test() {
//        TestBoardCell cell = board.getCell(0, 0);
//        Set<TestBoardCell> testList = cell.getAdjList();
//
//    }

    /**
     * Test that the adjacency list for each cell is correct
     */
    @Test
    public void testAdjacency() {
        TestBoardCell cell = board.getCell(0, 0);
        Set<TestBoardCell> testList = cell.getAdjList();
        // Check for cell [0,0]
        assertTrue(testList.contains(board.getCell(1, 0)));
        assertTrue(testList.contains(board.getCell(0, 1)));
        assertFalse(testList.contains(board.getCell(0, 0))); // Make sure no backtracking occurs
        assertEquals(2, testList.size());

        // Check for cell [0,3]
        cell = board.getCell(0, 3);
        testList = cell.getAdjList();
        assertTrue(testList.contains(board.getCell(1, 3)));
        assertTrue(testList.contains(board.getCell(0, 2)));
        assertFalse(testList.contains(board.getCell(0, 3))); // Make sure no backtracking occurs
        assertEquals(2, testList.size());

        // Check for cell [3,3]
        cell = board.getCell(3, 3);
        testList = cell.getAdjList();
        assertTrue(testList.contains(board.getCell(3, 2)));
        assertTrue(testList.contains(board.getCell(2, 3)));
        assertFalse(testList.contains(board.getCell(3, 3))); // Make sure no backtracking occurs
        assertEquals(2, testList.size());

        // Check for cell [3,0]
        cell = board.getCell(3, 0);
        testList = cell.getAdjList();
        assertTrue(testList.contains(board.getCell(2, 0)));
        assertTrue(testList.contains(board.getCell(3, 1)));
        assertFalse(testList.contains(board.getCell(3, 0))); // Make sure no backtracking occurs
        assertEquals(2, testList.size());

        // Check for cell [2,2]
        cell = board.getCell(2, 2);
        testList = cell.getAdjList();
        assertTrue(testList.contains(board.getCell(1, 2)));
        assertTrue(testList.contains(board.getCell(3, 2)));
        assertTrue(testList.contains(board.getCell(2, 1)));
        assertTrue(testList.contains(board.getCell(2, 3)));
        assertFalse(testList.contains(board.getCell(2, 2))); // Make sure no backtracking occurs

        assertEquals(4, testList.size());
    }

    /**
     * Test for correct movement on an empty board
     */
    @Test
    public void testTargetsNormal() {
        TestBoardCell cell = board.getCell(0, 0);
        board.calcTargets(cell, 2);
        Set<TestBoardCell> targets = board.getTargets();
        assertEquals(3, targets.size());
        assertTrue(targets.contains(board.getCell(0, 2)));
        assertTrue(targets.contains(board.getCell(1, 1)));
        assertTrue(targets.contains(board.getCell(2, 0)));
        board = new TestBoard();
        cell = board.getCell(0, 3);
        board.calcTargets(cell, 3);
        targets = board.getTargets();
        assertEquals(6, targets.size());
        assertTrue(targets.contains(board.getCell(0, 0)));
        assertTrue(targets.contains(board.getCell(1, 1)));
        assertTrue(targets.contains(board.getCell(2, 2)));
        assertTrue(targets.contains(board.getCell(3, 3)));

    }

    /**
     * Test for correct movement on a board with one or more rooms
     */
    @Test
    public void testTargetsRoom() {
        TestBoardCell cell = board.getCell(0, 0);
        board.getCell(0, 1).setRoom(true);
        board.getCell(1, 0).setRoom(true);
        board.calcTargets(cell, 3);
        Set<TestBoardCell> targets = board.getTargets();

        assertEquals(2, targets.size());
        assertTrue(targets.contains(board.getCell(0, 1)));
        assertTrue(targets.contains(board.getCell(1, 0)));

//        board.getCell(0, 1).setRoom(false);
//        board.calcTargets(cell, 2);
//        targets = board.getTargets();
//
//        assertEquals(3, targets.size());
//        assertTrue(targets.contains(board.getCell(0, 2)));
//        assertTrue(targets.contains(board.getCell(1, 1)));
//        assertTrue(targets.contains(board.getCell(1, 0)));

    }

    /**
     * Test for correct movement on a board with one or more occupied cells
     */
    @Test
    public void testTargetsOccupied() {
        TestBoardCell cell = board.getCell(0, 0);
        board.getCell(0, 1).setOccupied(true);
        board.getCell(2, 2).setOccupied(true);
        board.getCell(3, 1).setOccupied(true);
        board.calcTargets(cell, 6); // Testing max roll
        Set<TestBoardCell> targets = board.getTargets();
        // Ensure cant move to occupied
        assertFalse(targets.contains(board.getCell(0, 1)));
        assertFalse(targets.contains(board.getCell(2, 2)));
        assertFalse(targets.contains(board.getCell(3, 1)));

        // Test the only possible spots
        assertTrue(targets.contains(board.getCell(0, 2)));
        assertTrue(targets.contains(board.getCell(1, 3)));
        assertTrue(targets.contains(board.getCell(3, 3)));

    }

    /**
     * Test for correct movement on a board with one or more rooms and one or more occupied cells
     */
    @Test
    public void testTargetsMixed () {
        TestBoardCell cell = board.getCell(3,3);
        board.getCell(1,3).setRoom(true);
        board.getCell(3,0).setOccupied(true);
        board.calcTargets(cell, 3);
        Set<TestBoardCell> targets = board.getTargets();
        assertEquals(5, targets.size());
        assertFalse(targets.contains(board.getCell(3, 0))); // check for occupied
        assertTrue(targets.contains(board.getCell(1, 3))); // check for room
        assertTrue(targets.contains(board.getCell(1, 2)));
        assertTrue(targets.contains(board.getCell(2, 1)));
        assertTrue(targets.contains(board.getCell(2, 3)));
        assertTrue(targets.contains(board.getCell(3, 2)));

//        board = new TestBoard();
//        cell = board.getCell(3,1);
//        board.calcTargets(cell, 1);
//        targets = board.getTargets();
//
//        assertEquals(2, targets.size());
//        assertFalse(targets.contains(board.getCell(3, 0))); // check for occupied
//        assertFalse(targets.contains(board.getCell(3, 1))); // check for backtrack
//        assertTrue(targets.contains(board.getCell(2, 1)));
//        assertTrue(targets.contains(board.getCell(3, 2)));

    }
}
