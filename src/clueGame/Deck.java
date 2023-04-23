package clueGame;

import java.util.ArrayList;

public class Deck extends ArrayList<Card> {
    public Card getByName(String name) {
        for (Card c : this) {
            if (c.getCardName().equals(name)) {
                return c;
            }
        }
        return null;
    }
}
