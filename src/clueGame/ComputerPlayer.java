package clueGame;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class ComputerPlayer extends Player {
    private static final Board board = Board.getInstance();
    private final Random rand = new Random();
    private Boolean unableToDisprove = false;

    public ComputerPlayer(String name, String color, int row, int col) {
        super(name, color, row, col);
    }

    /**
     * Perform the ComputerPlayer's turn
     */
    public void move() {
        if (unableToDisprove) {
            createAccusation();
        }
        int row = super.getRow();
        int col = super.getCol();
        BoardCell target = selectTarget(board.getTargets());
        if (target != null) {
            super.move(target);
            board.getCell(row, col).setOccupied(false);
            board.getCell(target.getRow(), target.getCol()).setOccupied(true);
            if (board.getCell(target.getRow(), target.getCol()).isRoom()) {
                Solution suggestion = this.createSuggestion(board.getRoom(target.getChar()));
                Card res = board.handleSuggestion(this, suggestion.getPerson(), suggestion.getRoom(), suggestion.getWeapon());
                if (res == null)
                    unableToDisprove = true;
            }
        }
        super.setEndTurn(true);
        board.repaint();
    }

    private void createAccusation() {
        ArrayList<Card> boardDeck = board.getDeck();
        Set<Card> seen = super.getSeenCards();
        ArrayList<Card> hand = super.getHand();
        Card w = null, p = null, r = null;
        for (Card c : boardDeck) {
            if (!seen.contains(c) && !hand.contains(c)) {
                switch (c.getType()) {
                    case PERSON -> p = c;
                    case WEAPON -> w = c;
                    case ROOM -> r = c;
                }
            }
        }
        if (p != null && w != null && r != null)
            board.checkAccusation(this, new Solution(r, p, w));
    }

    /**
     * Have the AI create a suggestion
     *
     * @param room The room the player is in
     * @return suggested solution
     */
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

    /**
     * @param targets The set of targets to choose from
     * @return The target the AI has chosen
     */
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
        if (potential.isEmpty() && !targets.isEmpty())
            return targets.toArray(new BoardCell[0])[rand.nextInt(targets.size())];
        else if (potential.isEmpty())
            return null;
        else
            return potential.get(rand.nextInt(potential.size()));

    }
}
