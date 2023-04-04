package clueGame;

import java.util.ArrayList;
import java.util.Set;

@SuppressWarnings("FieldMayBeFinal")
public abstract class Player {
    private final String name;
    private final String color;
    private final ArrayList<Card> hand;
    private int row;
    private int col;
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

    public String getColor() {
        return color;
    }

    public ArrayList<Card> getHand() {
        return hand;
    }

}
