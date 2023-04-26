package clueGame;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class ClueGame extends JFrame {
    private static Board board;

    public static void main(String[] args) {
//        Music musicPlayer = new Music("data/music.wav");
//        musicPlayer.play();
        startGame();
    }

    private static void startGame() {
        board = Board.getInstance();
        board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
        board.initialize();
        GameControlPanel controlPanel = new GameControlPanel();
        CardPanel cardPanel = new CardPanel();
        board.setCardPanel(cardPanel);
        board.setControlPanel(controlPanel);
        JFrame frame = new JFrame("Clue Game");
        frame.add(controlPanel, BorderLayout.SOUTH);
        frame.add(cardPanel, BorderLayout.EAST);
        frame.add(board, BorderLayout.CENTER);
        frame.setSize(1000, 1000);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        board.startGame(); // Start first turn
        controlPanel.setTurn(board.getCurrentPlayer(), board.getRoll());
        controlPanel.setGuess("No guess");
        controlPanel.setGuessResult("No result");
        frame.setVisible(true);
        ActionListener gameEndListener = e -> {
            if (board.isGameOver()) {
                frame.dispose();
                ((Timer) e.getSource()).stop();

            }
        };
        new Timer(100, gameEndListener).start();
    }
}
