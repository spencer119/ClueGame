package clueGame;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Set of Cards with custom functions specifically for Cards
 */
public class CardSet extends HashSet<Card> {
    /**
     * @param name Name of the card to search for
     * @return True if the card is in the set, false otherwise
     */
    public boolean containsName(String name) {
        for (Card c : this) {
            if (c.getCardName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param type Type of card to search for
     * @return Set of cards of the given type
     */
    public Set<Card> getCardsByType(CardType type) {
        Set<Card> cards = new HashSet<>();
        for (Card c : this) {
            if (c.getType() == type) {
                cards.add(c);
            }
        }
        return cards;
    }

    /**
     * @param deck The deck to compare against
     * @return Set of cards that are in the deck but not in the set
     */
    public Set<Card> getUnseenCards(ArrayList<Card> deck) {
        Set<Card> unseen = new HashSet<>();
        for (Card c : deck) {

        }
        return unseen;
    }
}
