package clueGame;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class GameControlPanel extends JPanel {
    private static Integer roll = 1;
    private static Player currentPlayer;
    private static Board board;
    private final JTextField playerField;
    private final JTextField rollField;
    private final JTextField guessField;
    private final JTextField guessResultField;

    public GameControlPanel() {
        // Test player
        currentPlayer = new HumanPlayer("test", "red", 0, 0);
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(2, 0));

        JPanel topRow = new JPanel();
        topRow.setLayout(new GridLayout(1, 4));
        JPanel bottomRow = new JPanel();
        bottomRow.setLayout(new GridLayout(1, 2));

        JPanel turnPanel = new JPanel();
        turnPanel.setLayout(new GridLayout(2, 1));
        JPanel rollPanel = new JPanel();
        rollPanel.setLayout(new GridLayout(2, 1));
        JPanel guessPanel = new JPanel();
        JPanel guessResultPanel = new JPanel();
//        JPanel nextBtn = new JPanel();
//        JPanel accusationBtn = new JPanel();
        // Whose turn panel
        JLabel turnLabel = new JLabel("Whose turn?");
        playerField = new JTextField(currentPlayer.getName());
        playerField.setEditable(false);
        turnPanel.add(turnLabel);
        turnPanel.add(playerField);
        topRow.add(turnPanel);
        // Roll panel
        JLabel rollLabel = new JLabel("Roll: ");
        rollField = new JTextField(roll.toString());
        rollField.setEditable(false);
        rollPanel.add(rollLabel);
        rollPanel.add(rollField);
        topRow.add(rollPanel);
        // Buttons
        JButton nextBtn = new JButton("Next");
        JButton accusationBtn = new JButton("Make accusation");
        topRow.add(nextBtn);
        topRow.add(accusationBtn);
        // Guess panel
        guessField = new JTextField();
        guessField.setEditable(false);
        guessPanel.add(guessField);
        guessPanel.setBorder(new TitledBorder(new EtchedBorder(), "Guess"));
        guessResultField = new JTextField();
        guessResultField.setEditable(false);
        guessResultPanel.add(guessResultField);
        guessResultPanel.setBorder(new TitledBorder(new EtchedBorder(), "Guess Result"));
        bottomRow.add(guessPanel);
        bottomRow.add(guessResultPanel);

        controlPanel.add(topRow);
        controlPanel.add(bottomRow);
        add(controlPanel, BorderLayout.SOUTH);
        add(controlPanel, BorderLayout.SOUTH);

    }

    public static void main(String[] args) {

        GameControlPanel panel = new GameControlPanel();

        JFrame frame = new JFrame();
        frame.setContentPane(panel);
        frame.setSize(750, 180);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel.setTurn(new ComputerPlayer("Test Player", "orange", 0, 0), 5);
        panel.setGuess("I have no guess!");
        panel.setGuessResult("So you have nothing?");

        frame.setVisible(true);

    }

    public void setTurn(Player player, int roll) {
        currentPlayer = player;
        playerField.setText(currentPlayer.getName());
        GameControlPanel.roll = roll;
        rollField.setText(Integer.toString(roll));
    }

    public void setGuess(String guess) {
        guessField.setText(guess);
    }

    public void setGuessResult(String result) {
        guessResultField.setText(result);
    }

    private class NextListener implements MouseListener {
        public void mousePressed(MouseEvent e) {}

        public void mouseReleased(MouseEvent e) {}

        public void mouseEntered(MouseEvent e) {}

        public void mouseExited(MouseEvent e) {}

        public void mouseClicked(MouseEvent e) {}
    }
}
