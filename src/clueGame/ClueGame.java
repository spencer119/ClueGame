package clueGame;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Set;

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
        board.startGame();
        // Testing code
        controlPanel.setTurn(board.getCurrentPlayer(), board.getRoll());
        controlPanel.setGuess("No guess");
        controlPanel.setGuessResult("No result");
        frame.setVisible(true);
    }
}
