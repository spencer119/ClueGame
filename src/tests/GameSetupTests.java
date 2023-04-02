package tests;

import clueGame.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * Tests for game setup
 */
public class GameSetupTests {
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
    public void testInitialize() {
    }

    @Test
    public void testPlayers() {
        ArrayList<Player> players = board.getPlayers();
        // Check if the list contains 6 players in total
        assertEquals(6, players.size());
        // Check if the list contains 1 HumanPlayer and 5 ComputerPlayers.
        int humanPlayerCount = 0;
        int computerPlayerCount = 0;

        for (Player player : players) {
            if (player instanceof HumanPlayer) {
                humanPlayerCount++;
            } else if (player instanceof ComputerPlayer) {
                computerPlayerCount++;
            }
        }

        // Assert the counts are 1 and 5
        assertEquals(1, humanPlayerCount);
        assertEquals(5, computerPlayerCount);
    }

    @Test
    public void testSolutions() {
        assertEquals(board.getSolution().getPerson().getType(), CardType.PERSON);
        assertEquals(board.getSolution().getWeapon().getType(), CardType.WEAPON);
        assertEquals(board.getSolution().getRoom().getType(), CardType.ROOM);
    }

    @Test
    public void testCards() {
    }
}
