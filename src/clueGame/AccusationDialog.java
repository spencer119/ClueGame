package clueGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class AccusationDialog extends JDialog {
    private static Board board;
    private final JComboBox<Card> personBox;
    private final JComboBox<Card> weaponBox;
    private final JComboBox<Card> roomBox;
    private final JButton accuseBtn;
    private final JButton cancelBtn;
    private Card personCard, weaponCard, roomCard;

    public AccusationDialog(JFrame parent) {
        super(parent, "Make an accusation", true);
        setSize(250, 200);
        setLayout(new GridLayout(4, 2, 5, 5));
        JLabel roomLabel = new JLabel("Room");
        JLabel personLabel = new JLabel("Person");
        JLabel weaponLabel = new JLabel("Weapon");
        personBox = new JComboBox<Card>();
        weaponBox = new JComboBox<Card>();
        roomBox = new JComboBox<Card>();
        accuseBtn = new JButton("Accuse");
        cancelBtn = new JButton("Cancel");

        board = Board.getInstance();
        ArrayList<Card> deck = board.getDeck();
        for (Card c : deck) {
            switch (c.getType()) {
                case PERSON -> personBox.addItem(c);
                case WEAPON -> weaponBox.addItem(c);
                case ROOM -> roomBox.addItem(c);
            }
        }
        personBox.setSelectedIndex(-1);
        weaponBox.setSelectedIndex(-1);
        roomBox.setSelectedIndex(-1);
        AccusationListener listener = new AccusationListener();
        personBox.addActionListener(listener);
        weaponBox.addActionListener(listener);
        roomBox.addActionListener(listener);
        accuseBtn.addActionListener(listener);
        cancelBtn.addActionListener(listener);
        accuseBtn.setEnabled(false);
        add(roomLabel);
        add(roomBox);
        add(personLabel);
        add(personBox);
        add(weaponLabel);
        add(weaponBox);
        add(cancelBtn);
        add(accuseBtn);
        setVisible(true);
    }

    public static void main(String[] args) {
        testDialog();
    }

    public static void testDialog() {
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
        board.startGame(); // Start first turn
        controlPanel.setTurn(board.getCurrentPlayer(), board.getRoll());
        controlPanel.setGuess("No guess");
        controlPanel.setGuessResult("No result");
        frame.setVisible(true);
        AccusationDialog dialog = new AccusationDialog(frame);
    }

    private class AccusationListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // Enable submit button if both boxes are selected
            accuseBtn.setEnabled(roomBox.getSelectedIndex() != -1 && personBox.getSelectedIndex() != -1 && weaponBox.getSelectedIndex() != -1);
            if (e.getSource() instanceof JComboBox) {
                if (e.getSource().equals(personBox)) {
                    personCard = (Card) personBox.getSelectedItem();
                } else if (e.getSource().equals(weaponBox)) {
                    weaponCard = (Card) weaponBox.getSelectedItem();
                } else if (e.getSource().equals(roomBox)) {
                    roomCard = (Card) roomBox.getSelectedItem();
                }
            } else if (e.getSource() instanceof JButton) {
                if (e.getSource().equals(cancelBtn)) dispose(); // Cancel button
                else if (e.getSource().equals(accuseBtn)) { // Submit button
                    JOptionPane.showMessageDialog(null, "Submit: " + personCard + " " + weaponCard + " " + roomCard);
                }
            }

        }
    }
}
