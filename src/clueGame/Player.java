package clueGame;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Set;

@SuppressWarnings("FieldMayBeFinal")
public abstract class Player extends JPanel {
    private final String name;
    private final String colorStr;
    private final ArrayList<Card> hand;
    private Color color;
    private int row;
    private int col;
    private Boolean endTurn = true;
    private CardSet seenCards = new CardSet();

    public Player(String name, String colorStr, int row, int col) {
        super();
        this.name = name;
        this.colorStr = colorStr;
        this.row = row;
        this.col = col;
        hand = new ArrayList<Card>();
        setColor(colorStr);
    }

    private void setColor(String color) {
        switch (color.toLowerCase()) {
            case "red" -> this.color = Color.RED;
            case "blue" -> this.color = Color.BLUE;
            case "green" -> this.color = Color.GREEN;
            case "orange" -> this.color = Color.ORANGE;
            case "purple" -> this.color = Color.MAGENTA;
            case "pink" -> this.color = Color.PINK;
        }
    }

    public void draw(Graphics g, int cellSize, int xOffset, int yOffset) {
        int x = (col * cellSize) + xOffset;
        int y = (row * cellSize) + yOffset;
        g.setColor(this.color);
        g.fillOval(x, y, cellSize, cellSize);
    }

    public void updateHand(Card card) {
        hand.add(card);
    }

    public void updateSeen(Card seenCard) {
        seenCards.add(seenCard);
    }

    public Card disproveSuggestion(Card person, Card room, Card weapon) {
        ArrayList<Card> matchingCards = new ArrayList<Card>();
        if (hand.contains(person))
            matchingCards.add(person);
        if (hand.contains(room))
            matchingCards.add(room);
        if (hand.contains(weapon))
            matchingCards.add(weapon);
        if (matchingCards.size() == 0)
            return null;
        if (matchingCards.size() == 1)
            return matchingCards.get(0);
        return matchingCards.get((int) (Math.random() * matchingCards.size()));

    }

    public String getName() {
        return name;
    }


    public ArrayList<Card> getHand() {
        return hand;
    }

    public Set<Card> getSeenCards() {
        return seenCards;
    }

    public void move(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public Boolean isEndTurn() {
        return endTurn;
    }

    public void setEndTurn(Boolean endTurn) {
        this.endTurn = endTurn;
    }
}
