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

    public Solution createSuggestion(Room room) {
        CardSet seen = (CardSet) super.getSeenCards();
        Set<Card> seenWeapons = seen.getCardsByType(CardType.WEAPON);
        Set<Card> seenPeople = seen.getCardsByType(CardType.PERSON);
        return null;
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
