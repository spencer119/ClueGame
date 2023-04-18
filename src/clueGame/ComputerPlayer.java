package clueGame;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

public class ComputerPlayer extends Player {
    private static final Board board = Board.getInstance();
    private final Random rand = new Random();

    public ComputerPlayer(String name, String color, int row, int col) {
        super(name, color, row, col);
    }

    public void move() {
        int row = super.getRow();
        int col = super.getCol();
        BoardCell target = selectTarget(board.getTargets());
        super.move(target.getRow(), target.getCol());
        board.getCell(row, col).setOccupied(false);
        board.getCell(target.getRow(), target.getCol()).setOccupied(true);
        super.setEndTurn(true);
        board.repaint();
    }

    public Solution createSuggestion(Room room) {
        ArrayList<Card> boardDeck = board.getDeck();
        CardSet seen = (CardSet) super.getSeenCards();
        Set<Card> seenWeapons = seen.getCardsByType(CardType.WEAPON);
        Set<Card> seenPeople = seen.getCardsByType(CardType.PERSON);
        ArrayList<Card> potentialWeapons = new ArrayList<Card>();
        ArrayList<Card> potentialPeople = new ArrayList<Card>();
        for (Card c : boardDeck) {
            if (c.getType() == CardType.WEAPON && !seenWeapons.contains(c)) {
                potentialWeapons.add(c);
            }
            if (c.getType() == CardType.PERSON && !seenPeople.contains(c)) {
                potentialPeople.add(c);
            }
        }
        if (potentialPeople.isEmpty() || potentialWeapons.isEmpty()) {
            return null;
        } else {
            return new Solution(new Card(room.getName(), CardType.ROOM), potentialPeople.get(rand.nextInt(potentialPeople.size())), potentialWeapons.get(rand.nextInt(potentialWeapons.size())));
        }
    }

    public BoardCell selectTarget(Set<BoardCell> targets) {
        CardSet seen = (CardSet) super.getSeenCards();
        ArrayList<BoardCell> potential = new ArrayList<BoardCell>();
        for (BoardCell cell : targets) {
            if (cell.isRoomCenter() && !seen.containsName(board.getRoom(cell.getChar()).getName())) {
                potential.add(cell);
            }
        }
        if (potential.isEmpty()) {
            for (BoardCell c : targets)
                if (!c.isRoomCenter()) potential.add(c);

        }
        return potential.get(rand.nextInt(potential.size()));

    }
}
