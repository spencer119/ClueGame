package clueGame;

import java.util.HashSet;
import java.util.Set;

public class CardSet extends HashSet<Card> {
    public boolean containsName(String name) {
        for (Card c : this) {
            if (c.getCardName().equals(name)) {
                return true;
            }
        }
        return false;
    }
}
