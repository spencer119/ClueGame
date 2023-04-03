package tests;

import clueGame.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Tests for game setup
 */
public class GameSolutionTest {
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

    @Test
    public void testAccusation() {
        Solution boardSolution = board.getTheAnswer();
        ArrayList<Card> deck = board.getDeck();
        assertTrue(board.checkAccusation(boardSolution));
        Solution wrongWeapon = new Solution(boardSolution.getRoom(), boardSolution.getPerson(), new Card("Lego", CardType.WEAPON));
        assertFalse(board.checkAccusation(wrongWeapon));
        Solution wrongPerson = new Solution(boardSolution.getRoom(), new Card("Ted Bundy", CardType.PERSON), boardSolution.getWeapon());
        assertFalse(board.checkAccusation(wrongPerson));
        Solution wrongRoom = new Solution(new Card("MZ 026", CardType.ROOM), boardSolution.getPerson(), boardSolution.getWeapon());
        assertFalse(board.checkAccusation(wrongRoom));
    }
}
