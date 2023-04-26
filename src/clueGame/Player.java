package clueGame;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Set;

@SuppressWarnings("FieldMayBeFinal")
public abstract class Player extends JPanel {
    private static Board board = Board.getInstance();
    private final String name;
    private final String colorStr;
    private final ArrayList<Card> hand;
    private Color color;
    private int row;
    private int col;
    private Boolean endTurn = true;
    private CardSet seenCards = new CardSet();
    private Boolean inRoom = false;
    private Boolean movedBySuggestion = false;

    public Player(String name, String colorStr, int row, int col) {
        super();
        this.name = name;
        this.colorStr = colorStr;
        this.row = row;
        this.col = col;
        hand = new ArrayList<Card>();
        setColor(colorStr);
    }

    public Boolean getMovedBySuggestion() {return movedBySuggestion;}

    public void setMovedBySuggestion(Boolean movedBySuggestion) {
        this.movedBySuggestion = movedBySuggestion;
    }

    private void setColor(String color) {
        switch (color.toLowerCase()) {
            case "red" -> this.color = Color.RED;
            case "blue" -> this.color = Color.BLUE;
            case "green" -> this.color = Color.GREEN;
            case "orange" -> this.color = Color.ORANGE;
            case "purple" -> this.color = Color.MAGENTA;
            case "pink" -> this.color = Color.PINK;
        }
    }

    @Override
    public String toString() {
        return name;
    }

    public void draw(Graphics g, int cellSize, int xOffset, int yOffset) {

        int x = (col * cellSize) + xOffset;
        int y = (row * cellSize) + yOffset;
        g.setColor(this.color);
        g.fillOval(x, y, cellSize, cellSize);

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


    public ArrayList<Card> getHand() {
        return hand;
    }

    public Set<Card> getSeenCards() {
        return seenCards;
    }

    public Boolean getInRoom() {return inRoom;}

    public void setInRoom(Boolean inRoom) {this.inRoom = inRoom;}

    //    public void move(int row, int col) {
//        this.row = row;
//        this.col = col;
//    }
    public void moveBySuggestion(BoardCell cell) {
        movedBySuggestion = true;
        this.row = cell.getRow();
        this.col = cell.getCol();
    }

    public void move(BoardCell cell) {
//        if (inRoom) {
//            board.getRoom(board.getCell(row, col)).removePlayer(this);
//        }
//        if (cell.isRoom()) {
//            board.getRoom(cell).addPlayer(this);
//            inRoom = true;
//        } else
//            inRoom = false;
        if (movedBySuggestion) movedBySuggestion = false;
        this.row = cell.getRow();
        this.col = cell.getCol();
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public BoardCell getCell() {
        return board.getCell(row, col);
    }

    public Boolean isEndTurn() {
        return endTurn;
    }

    public void setEndTurn(Boolean endTurn) {
        this.endTurn = endTurn;
    }


}
