package tests;

import clueGame.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

public class ComputerAITest {
    private static Board board;
    private static ComputerPlayer ai1;

    @BeforeAll
    public static void setUp() {
        // Board is singleton, get the only instance
        board = Board.getInstance();
        // set the file names to use my config files
        board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
        // Initialize will load config files
        board.initialize();
        ai1 = new ComputerPlayer("testAI 1", "red", 13, 2);
        board.addPlayer(ai1);
    }

    @Test
    public void testAISuggestion() {
        Room attic = board.getRoom('A');

    }

    @Test
    public void testAITarget() {
        board.calcTargets(board.getCell(2, 0), 1); // Doorawy to attic
        BoardCell target = ai1.selectTarget(board.getTargets());
        assertEquals(board.getCell(3, 3), target); // Attic center
        // Test when room is seen
        ai1.updateSeen(new Card("Attic", CardType.ROOM));
        board.calcTargets(board.getCell(2, 0), 1); // Doorawy to attic
        target = ai1.selectTarget(board.getTargets());
        assertNotEquals(board.getCell(3, 3), target); // Attic center
        // Test no room random target on walkway
        board.calcTargets(board.getCell(13, 2), 1);
        target = ai1.selectTarget(board.getTargets());
        assertEquals('W', target.getChar());
        // Test target is Storage room not Library which is seen
        ai1.updateSeen(new Card("Library", CardType.ROOM));
        board.calcTargets(board.getCell(16, 17), 2);
        target = ai1.selectTarget(board.getTargets());
        assertEquals(target, board.getRoom('S').getCenterCell());

    }
}
