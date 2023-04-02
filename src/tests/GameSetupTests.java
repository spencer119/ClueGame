package tests;

import clueGame.Board;
import clueGame.BoardCell;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


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


}
