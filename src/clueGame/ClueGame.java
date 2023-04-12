package clueGame;

import javax.swing.*;
import java.awt.*;

public class ClueGame extends JFrame {
    private static Board board;

    public static void main(String[] args) {
        board = Board.getInstance();
        board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
        board.initialize();
        GameControlPanel controlPanel = new GameControlPanel();
        CardPanel cardPanel = new CardPanel();
        JFrame frame = new JFrame("Clue Game");
        frame.add(controlPanel, BorderLayout.SOUTH);
        frame.add(cardPanel, BorderLayout.EAST);
        frame.add(board, BorderLayout.CENTER);
        frame.setSize(1000, 1000);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Testing code
        controlPanel.setTurn(new ComputerPlayer("Test Player", "red", 9, 9), 1);
        controlPanel.setGuess("Guess placeholder");
        controlPanel.setGuessResult("Result placeholder");
        frame.setVisible(true);
    }
}
