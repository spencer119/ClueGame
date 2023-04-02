package clueGame;

import java.util.ArrayList;
import java.util.Set;

public abstract class Player {
    private final String name;
    private final String color;
    private final int row;
    private final int col;
    private final ArrayList<Card> hand;
    private Set<Card> seenCards;

    public Player(String name, String color, int row, int col) {
        this.name = name;
        this.color = color;
        this.row = row;
        this.col = col;
        hand = new ArrayList<Card>();
    }


    public void updateHand(Card card) {
        hand.add(card);
    }

    public void updateSeen(Card seenCard) {
    }

    public Card disproveSuggestion() {
        return null;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public ArrayList<Card> getHand() {
        return hand;
    }

}
