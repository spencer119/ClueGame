package tests;

import clueGame.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Set;

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
        assertTrue(board.checkAccusation(new ComputerPlayer("testPlayer", "red", 1, 1), boardSolution));
        Solution wrongWeapon = new Solution(boardSolution.getRoom(), boardSolution.getPerson(), new Card("Lego", CardType.WEAPON));
        assertFalse(board.checkAccusation(new ComputerPlayer("testPlayer", "red", 1, 1), wrongWeapon));
        Solution wrongPerson = new Solution(boardSolution.getRoom(), new Card("Ted Bundy", CardType.PERSON), boardSolution.getWeapon());
        assertFalse(board.checkAccusation(new ComputerPlayer("testPlayer", "red", 1, 1), wrongPerson));
        Solution wrongRoom = new Solution(new Card("MZ 026", CardType.ROOM), boardSolution.getPerson(), boardSolution.getWeapon());
        assertFalse(board.checkAccusation(new ComputerPlayer("testPlayer", "red", 1, 1), wrongRoom));
    }

    @Test
    public void testSuggestions() {
        Player testPlayer = new HumanPlayer("test", "red", 7, 7);
        Player disprovePlayer = new HumanPlayer("disprove", "blue", 10, 10);

        Card testPerson = new Card("testPerson", CardType.PERSON);
        Card testRoom = new Card("testRoom", CardType.ROOM);
        Card testWeapon = new Card("testWeapon", CardType.WEAPON);
        Card wrongWeapon = new Card("wrongWeapon", CardType.WEAPON);
        Card wrongPerson = new Card("wrongPerson", CardType.PERSON);
        Card wrongRoom = new Card("wrongRoom", CardType.ROOM);
        Card disproveRoom = new Card("disproveRoom", CardType.ROOM);

        testPlayer.updateHand(testPerson);
        testPlayer.updateHand(testRoom);
        testPlayer.updateHand(testWeapon);
        disprovePlayer.updateHand(disproveRoom);
        board.addPlayer(testPlayer);
        board.addPlayer(disprovePlayer);
        // disproveSuggestion tests

        // Test when player cannot disprove any cards
        assertNull(testPlayer.disproveSuggestion(wrongPerson, wrongRoom, wrongWeapon));
        // Player can disprove testPerson
        assertNotNull(testPlayer.disproveSuggestion(testPerson, wrongRoom, wrongWeapon));

        // Handle suggestion tests
        // Test where suggesting player has the cards
        assertNull(board.handleSuggestion(testPlayer, wrongPerson, wrongRoom, wrongWeapon));
        // disprovePlayer can disprove room card
        assertNotNull(board.handleSuggestion(testPlayer, wrongPerson, disproveRoom, wrongWeapon));

        Player testPlayer2 = new HumanPlayer("test2", "red", 7, 7);
        testPlayer2.updateHand(testPerson);
        Card c = board.handleSuggestion(testPlayer, testPerson, disproveRoom, wrongWeapon);
        // Test that disproveRoom was returned not testPerson since testPlayer2 is last in list
        assertEquals(c.getCardName(), "disproveRoom");
    }
}
