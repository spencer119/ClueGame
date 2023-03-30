package tests;

import clueGame.Board;
import clueGame.BoardCell;
import clueGame.DoorDirection;
import clueGame.Room;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author spencer
 * Tests to check config files
 */
public class FileInitTests {
    public static final int NUM_ROWS = 23;
    public static final int NUM_COLUMNS = 23;
    public static final int LEGEND_SIZE = 11;
    private static Board board;

    @BeforeAll
    public static void setUp() {
        board = Board.getInstance();
        // set the file names to use my config files
        board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
        // Initialize will load BOTH config files
        board.initialize();
    }

    @Test
    public void testRooms() {
        BoardCell cell = board.getCell(0, 0);
        Room room = board.getRoom(cell);
        // Test walkway
        assertNotNull(room);
        assertEquals(room.getName(), "Walkway");
        assertFalse(cell.isRoomCenter());
        assertFalse(cell.isLabel());

        // Test normal room space
        cell = board.getCell(1, 1);
        room = board.getRoom(cell);
        assertNotNull(room);
        assertEquals(room.getName(), "Attic");
        assertFalse(cell.isLabel());
        assertFalse(cell.isRoomCenter());
        assertFalse(cell.isDoorway());

        // Test room center
        cell = board.getCell(3, 3);
        room = board.getRoom(cell);
        assertNotNull(room);
        assertEquals(room.getName(), "Attic");
        assertTrue(cell.isRoomCenter());
        assertSame(room.getCenterCell(), cell);

        // Test room label
        cell = board.getCell(8, 3);
        room = board.getRoom(cell);
        assertNotNull(room);
        assertEquals(room.getName(), "Theater");
        assertTrue(cell.isLabel());
        assertSame(room.getLabelCell(), cell);

        // Test secret passage
        cell = board.getCell(1, 5);
        room = board.getRoom(cell);
        assertNotNull(room);
        assertEquals(room.getName(), "Attic");
        assertEquals('P', cell.getSecretPassage());

        // test a closet
        cell = board.getCell(0, 11);
        room = board.getRoom(cell);
        assertNotNull(room);
        assertEquals(room.getName(), "Unused");
        assertFalse(cell.isRoomCenter());
        assertFalse(cell.isLabel());
    }

    @Test // Test that the board has the correct number of rows and columns
    public void testBoardDimensions() {
        assertEquals(NUM_ROWS, board.getNumRows());
        assertEquals(NUM_COLUMNS, board.getNumCols());
    }

    @Test // Test that each room is labeled correctly
    public void testRoomLabels() {
        // Check the names of each room based on their one-letter label
        assertEquals("Storage", board.getRoom('S').getName());
        assertEquals("Underground Passage", board.getRoom('P').getName());
        assertEquals("Basement", board.getRoom('B').getName());
        assertEquals("Observatory", board.getRoom('O').getName());
        assertEquals("Attic", board.getRoom('A').getName());
        assertEquals("Theater", board.getRoom('T').getName());
        assertEquals("Cellar", board.getRoom('C').getName());
        assertEquals("Game Room", board.getRoom('G').getName());
        assertEquals("Library", board.getRoom('L').getName());
        assertEquals("Unused", board.getRoom('X').getName());
        assertEquals("Walkway", board.getRoom('W').getName());

    }

    @Test // Test that the correct number of doorways are on the board
    public void testNumberOfDoorways() {
        int numDoors = 0;
        // Iterate through board and count doors
        for (int row = 0; row < board.getNumRows(); row++)
            for (int col = 0; col < board.getNumCols(); col++) {
                BoardCell cell = board.getCell(row, col);
                if (cell.isDoorway())
                    numDoors++;
            }
        assertEquals(9, numDoors); // Check there are 9
    }

    @Test // Test that each door has the correct direction
    public void FourDoorDirections() {
        // Right doorway
        BoardCell cell = board.getCell(2, 0);
        assertTrue(cell.isDoorway());
        assertEquals(DoorDirection.RIGHT, cell.getDoorDirection());

        // Up doorway
        cell = board.getCell(11, 1);
        assertTrue(cell.isDoorway());
        assertEquals(DoorDirection.UP, cell.getDoorDirection());

        // Left doorway
        cell = board.getCell(17, 11);
        assertTrue(cell.isDoorway());
        assertEquals(DoorDirection.LEFT, cell.getDoorDirection());

        // Down doorway
        cell = board.getCell(6, 21);
        assertTrue(cell.isDoorway());
        assertEquals(DoorDirection.DOWN, cell.getDoorDirection());

        // Test that walkways are not doors
        cell = board.getCell(0, 0);
        assertFalse(cell.isDoorway());
    }

}
