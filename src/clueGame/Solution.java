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

    @Override
    public String toString() {
        return room.toString() + ", " + person.toString() + ", " + weapon.toString();
    }

    @Override
    public boolean equals(Object target) {
        if (target instanceof Solution) {
            return room.equals(((Solution) target).getRoom()) && person.equals(((Solution) target).getPerson()) && weapon.equals(((Solution) target).getWeapon());
        }
        return false;
    }

}
