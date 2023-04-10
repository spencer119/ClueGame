package clueGame;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;

public class CardPanel extends JPanel {
    private static final Board board = Board.getInstance();
    private final JPanel peoplePanel;
    private final JPanel weaponsPanel;
    private final JPanel roomsPanel;

    public CardPanel() {
        JPanel cardPanel = new JPanel();
        setLayout(new GridLayout(3, 0));
        peoplePanel = new JPanel();
        roomsPanel = new JPanel();
        weaponsPanel = new JPanel();
        peoplePanel.setLayout(new GridLayout(0, 1));
        roomsPanel.setLayout(new GridLayout(0, 1));
        weaponsPanel.setLayout(new GridLayout(0, 1));
        peoplePanel.setBorder(new TitledBorder(new EtchedBorder(), "People"));
        roomsPanel.setBorder(new TitledBorder(new EtchedBorder(), "Rooms"));
        weaponsPanel.setBorder(new TitledBorder(new EtchedBorder(), "Weapons"));
        JLabel handLabel = new JLabel("In hand");
        JLabel seenLabel = new JLabel("Seen");
        peoplePanel.add(handLabel);
        JPanel handPanel = new JPanel();

        JPanel seenPanel = new JPanel();
        handPanel.setLayout(new GridLayout(0, 1));
        seenPanel.setLayout(new GridLayout(0, 1));

        peoplePanel.add(handPanel);
        peoplePanel.add(seenLabel);
        peoplePanel.add(seenPanel);
        updateWeaponPanel();
        handPanel.add(new JLabel("test"));
        seenPanel.add(new JLabel("test"));
        add(peoplePanel);
        add(roomsPanel);
        add(weaponsPanel);


    }

    public static void main(String[] args) {
        board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
        board.initialize();
        CardPanel cardPanel = new CardPanel();
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 900);
        frame.setContentPane(cardPanel);
        frame.setVisible(true);
        cardPanel.setVisible(true);
    }

    public void updateWeaponPanel() {
        ArrayList<Card> cards = board.getPlayers().get(0).getHand();
        for (Card c : cards) {
            System.out.println(c.getCardName());
            weaponsPanel.removeAll();
            if (c.getType() == CardType.WEAPON) {
                weaponsPanel.add(new JLabel(c.getCardName()));
            }
        }
    }
}
