package clueGame;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;

public class CardPanel extends JPanel {
    private static final Board board = Board.getInstance();
    private final JPanel seenPeople = new JPanel();
    private final JPanel seenRooms = new JPanel();
    private final JPanel seenWeapons = new JPanel();
    private final JPanel handPeople = new JPanel();
    private final JPanel handRooms = new JPanel();
    private final JPanel handWeapons = new JPanel();

    public CardPanel() {
        setLayout(new GridLayout(3, 0));

        updateCardPanel(CardType.PERSON);
        updateCardPanel(CardType.ROOM);
        updateCardPanel(CardType.WEAPON);
        add(createPeoplePanel());
        add(createRoomPanel());
        add(createWeaponPanel());

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

    public JPanel createWeaponPanel() {
        JPanel weaponsPanel = new JPanel();
        weaponsPanel.setLayout(new GridLayout(2, 0));
        weaponsPanel.setBorder(new TitledBorder(new EtchedBorder(), "Weapons"));
        seenWeapons.setBorder(new TitledBorder(new EtchedBorder(), "Seen"));
        handWeapons.setBorder(new TitledBorder(new EtchedBorder(), "In Hand"));
        seenWeapons.setLayout(new GridLayout(5, 0));
        handWeapons.setLayout(new GridLayout(5, 0));
        weaponsPanel.add(handWeapons);
        weaponsPanel.add(seenWeapons);
        return weaponsPanel;
    }

    public JPanel createRoomPanel() {
        JPanel roomsPanel = new JPanel();
        roomsPanel.setLayout(new GridLayout(2, 0));
        roomsPanel.setBorder(new TitledBorder(new EtchedBorder(), "Rooms"));
        seenRooms.setBorder(new TitledBorder(new EtchedBorder(), "Seen"));
        handRooms.setBorder(new TitledBorder(new EtchedBorder(), "In Hand"));
        seenRooms.setLayout(new GridLayout(5, 0, 0, 10));
        handRooms.setLayout(new GridLayout(5, 0, 0, 10));
        roomsPanel.add(handRooms);
        roomsPanel.add(seenRooms);
        return roomsPanel;
    }

    public JPanel createPeoplePanel() {
        JPanel peoplePanel = new JPanel();
        peoplePanel.setLayout(new GridLayout(2, 0));
        peoplePanel.setBorder(new TitledBorder(new EtchedBorder(), "People"));
        seenPeople.setBorder(new TitledBorder(new EtchedBorder(), "Seen"));
        handPeople.setBorder(new TitledBorder(new EtchedBorder(), "In Hand"));
        seenPeople.setLayout(new GridLayout(5, 0, 0, 10));
        peoplePanel.add(handPeople);
        peoplePanel.add(seenPeople);
        return peoplePanel;
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
        revalidate();
    }

    public void updateSeen(Card card) {
        switch (card.getType()) {
            case ROOM -> seenRooms.add(createCardLabel(card.getCardName(), false));
            case WEAPON -> seenWeapons.add(createCardLabel(card.getCardName(), false));
            case PERSON -> seenPeople.add(createCardLabel(card.getCardName(), false));
        }
        repaint();
        revalidate();
    }

    private JLabel createCardLabel(String name, Boolean inHand) {
        JLabel label = new JLabel(name);
        label.setBorder(BorderFactory.createCompoundBorder(label.getBorder(), BorderFactory.createEmptyBorder(0, 5, 0, 5)));
        label.setFont(label.getFont().deriveFont(12.0f));
        label.setForeground(inHand ? Color.GREEN : Color.RED);
        return label;
    }


}
