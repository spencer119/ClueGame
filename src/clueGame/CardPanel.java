package clueGame;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;

public class CardPanel extends JPanel {
    private static final Board board = Board.getInstance();
    private final JPanel seenPeople;
    private final JPanel seenRooms;
    private final JPanel seenWeapons;
    private final JPanel handPeople;
    private final JPanel handRooms;
    private final JPanel handWeapons;

    public CardPanel() {
        JPanel cardPanel = new JPanel();
        setLayout(new GridLayout(3, 0));
        JPanel peoplePanel = new JPanel();
        JPanel roomsPanel = new JPanel();
        JPanel weaponsPanel = new JPanel();
        seenPeople = new JPanel();
        seenRooms = new JPanel();
        seenWeapons = new JPanel();
        handPeople = new JPanel();
        handRooms = new JPanel();
        handWeapons = new JPanel();
        peoplePanel.setLayout(new GridLayout(2, 0));
        roomsPanel.setLayout(new GridLayout(2, 0));
        weaponsPanel.setLayout(new GridLayout(2, 0));
        peoplePanel.setBorder(new TitledBorder(new EtchedBorder(), "People"));
        roomsPanel.setBorder(new TitledBorder(new EtchedBorder(), "Rooms"));
        weaponsPanel.setBorder(new TitledBorder(new EtchedBorder(), "Weapons"));
        seenPeople.setBorder(new TitledBorder(new EtchedBorder(), "Seen"));
        seenRooms.setBorder(new TitledBorder(new EtchedBorder(), "Seen"));
        seenWeapons.setBorder(new TitledBorder(new EtchedBorder(), "Seen"));
        handPeople.setBorder(new TitledBorder(new EtchedBorder(), "In Hand"));
        handRooms.setBorder(new TitledBorder(new EtchedBorder(), "In Hand"));
        handWeapons.setBorder(new TitledBorder(new EtchedBorder(), "In Hand"));
        seenPeople.setLayout(new GridLayout(5, 0, 0, 10));
        handPeople.setLayout(new GridLayout(5, 0, 0, 10));
        seenRooms.setLayout(new GridLayout(5, 0, 0, 10));
        handRooms.setLayout(new GridLayout(5, 0, 0, 10));
        seenWeapons.setLayout(new GridLayout(5, 0));
        handWeapons.setLayout(new GridLayout(5, 0));
        peoplePanel.add(handPeople);
        peoplePanel.add(seenPeople);
        roomsPanel.add(handRooms);
        roomsPanel.add(seenRooms);
        weaponsPanel.add(handWeapons);
        weaponsPanel.add(seenWeapons);
        updateCardPanel(CardType.PERSON);
        updateCardPanel(CardType.ROOM);
        updateCardPanel(CardType.WEAPON);
//        JLabel handLabel = new JLabel("In hand");
//        JLabel seenLabel = new JLabel("Seen");
//        peoplePanel.add(handLabel);
//        updateWeaponPanel();
//        handPanel.add(new JLabel("test"));
//        seenPanel.add(new JLabel("test"));
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

    public void updateCardPanel(CardType type) {
        ArrayList<Card> cards = board.getPlayers().get(0).getHand();
        Set<Card> seenCards = board.getPlayers().get(0).getSeenCards();
        if (Objects.requireNonNull(type) == CardType.PERSON) {
            handPeople.removeAll();
            seenPeople.removeAll();
            for (Card c : cards) {
                if (c.getType() == CardType.PERSON) {
                    handPeople.add(createCardLabel(c.getCardName(), true));
                }
            }
            for (Card c : seenCards) {
                if (c.getType() == CardType.PERSON) {
                    seenPeople.add(createCardLabel(c.getCardName(), false));
                }
            }
        } else if (type == CardType.ROOM) {
            handRooms.removeAll();
            seenRooms.removeAll();
            for (Card c : cards) {
                if (c.getType() == CardType.ROOM) {
                    handRooms.add(createCardLabel(c.getCardName(), true));
                }
            }
            for (Card c : seenCards) {
                if (c.getType() == CardType.ROOM) {
                    seenRooms.add(createCardLabel(c.getCardName(), true));
                }

            }
        } else if (type == CardType.WEAPON) {
            handWeapons.removeAll();
            seenWeapons.removeAll();
            for (Card c : cards) {
                if (c.getType() == CardType.WEAPON) {
                    handWeapons.add(createCardLabel(c.getCardName(), true));
                }
            }
            for (Card c : seenCards) {
                if (c.getType() == CardType.WEAPON) {
                    seenWeapons.add(createCardLabel(c.getCardName(), true));
                }
            }
        }
        repaint();
    }

    private JLabel createCardLabel(String name, Boolean inHand) {
        JLabel label = new JLabel(name);
        label.setBorder(BorderFactory.createCompoundBorder(label.getBorder(), BorderFactory.createEmptyBorder(0, 5, 0, 5)));
        label.setFont(label.getFont().deriveFont(12.0f));
        label.setForeground(inHand ? Color.GREEN : Color.RED);
        return label;
    }


}
