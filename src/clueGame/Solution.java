package clueGame;

public class Solution {
    private final Card room;
    private final Card person;
    private final Card weapon;

    public Solution(Card room, Card person, Card weapon) {
        this.room = room;
        this.person = person;
        this.weapon = weapon;
    }

    public Card getRoom() {
        return room;
    }

    public Card getPerson() {
        return person;
    }

    public Card getWeapon() {
        return weapon;
    }
}
