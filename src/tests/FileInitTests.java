package tests;

import clueGame.Board;
import clueGame.BoardCell;
import clueGame.DoorDirection;
import clueGame.Room;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class FileInitTests {
    public static final int NUM_ROWS = 23;
    public static final int NUM_COLUMNS = 23;
    public static final int LEGEND_SIZE = 11;
    private static Board board;

    @BeforeAll
    public static void setUp() {
        board = Board.getInstance();
        // set the file names to use my config files
        board.setConfigFiles("ClueLayout306.csv", "ClueSetup306.txt");
        // Initialize will load BOTH config files
        board.initialize();
    }

    @Test
    public void testRooms() {
        BoardCell cell = board.getCell(0, 0);
        Room room = board.getRoom(cell);
        // Test walkway
        assertTrue(room != null);
        assertEquals(room.getName(), "Walkway");
        assertFalse(cell.isRoomCenter());
        assertFalse(cell.isLabel());

        // Test normal room space
        cell = board.getCell(1, 1);
        room = board.getRoom(cell);
        assertTrue(room != null);
        assertEquals(room.getName(), "Attic");
        assertFalse(cell.isLabel());
        assertFalse(cell.isRoomCenter());
        assertFalse(cell.isDoorway());

        // Test room center
        cell = board.getCell(3, 3);
        room = board.getRoom(cell);
        assertTrue(room != null);
        assertEquals(room.getName(), "Attic");
        assertTrue(cell.isRoomCenter());
        assertTrue(room.getCenterCell() == cell);

        // Test room label
        cell = board.getCell(8, 3);
        room = board.getRoom(cell);
        assertTrue(room != null);
        assertEquals(room.getName(), "Theater");
        assertTrue(cell.isLabel());
        assertTrue(room.getLabelCell() == cell);

        // Test secret passage
        cell = board.getCell(1, 5);
        room = board.getRoom(cell);
        assertTrue(room != null);
        assertEquals(room.getName(), "Attic");
        assertTrue(cell.getSecretPassage() == 'P');

        // test a closet
        cell = board.getCell(0, 11);
        room = board.getRoom(cell);
        assertTrue(room != null);
        assertEquals(room.getName(), "Unused");
        assertFalse(cell.isRoomCenter());
        assertFalse(cell.isLabel());
    }

    @Test
    public void testBoardDimensions() {
        assertEquals(NUM_ROWS, board.getNumRows());
        assertEquals(NUM_COLUMNS, board.getNumColumns());
    }

    @Test
    public void testRoomLabels() {
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
        assertEquals("Attic", board.getRoom('W').getName());
        assertEquals("Walkway", board.getRoom('W').getName());

    }

    @Test
    public void testNumberOfDoorways() {
        int numDoors = 0;
        for (int row = 0; row < board.getNumRows(); row++)
            for (int col = 0; col < board.getNumColumns(); col++) {
                BoardCell cell = board.getCell(row, col);
                if (cell.isDoorway())
                    numDoors++;
            }
        assertEquals(9, numDoors);
    }

    @Test
    public void FourDoorDirections() {
        BoardCell cell = board.getCell(2, 0);
        assertTrue(cell.isDoorway());
        assertEquals(DoorDirection.RIGHT, cell.getDoorDirection());

        cell = board.getCell(11, 1);
        assertTrue(cell.isDoorway());
        assertEquals(DoorDirection.UP, cell.getDoorDirection());

        cell = board.getCell(17, 11);
        assertTrue(cell.isDoorway());
        assertEquals(DoorDirection.LEFT, cell.getDoorDirection());

        cell = board.getCell(6, 21);
        assertTrue(cell.isDoorway());
        assertEquals(DoorDirection.DOWN, cell.getDoorDirection());

        // Test that walkways are not doors
        cell = board.getCell(0, 0);
        assertFalse(cell.isDoorway());
    }

}
