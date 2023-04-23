package clueGame;

import java.util.Set;

public class Card {
    private final String cardName;
    private final CardType type;

    public Card(String cardName, CardType type) {
        this.cardName = cardName;
        this.type = type;
    }

    @Override
    public boolean equals(Object target) {
        if (target instanceof Card card) {
            return cardName.equals(card.cardName) && type == card.type;
        } else if (target instanceof String s) {
            return cardName.equals(s);
        }
        return false;
    }

    @Override
    public String toString() {
        return cardName;
    }

    public String getCardName() {
        return cardName;
    }

    public CardType getType() {
        return type;
    }
}


