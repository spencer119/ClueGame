package clueGame;

import java.util.HashSet;
import java.util.Set;

/**
 * BoardCell class
 *
 * @author spencer
 */
public class BoardCell {
    private final int row;
    private final int col;
    private boolean isOccupied;
    private boolean isRoom;
    private final char initial;

    private DoorDirection doorDirection;
    private char doorRoom;

    private Boolean roomLabel;
    private Boolean roomCenter;

    private char secretPassage;
    private final Set<BoardCell> adjList = new HashSet<>(); // List of adjacent cells

    /**
     * @param charLabel the label of the cell
     * @param row       the row of the cell
     * @param col       the column of the cell
     */
    public BoardCell(String charLabel, int row, int col) {
        this.row = row;
        this.col = col;

        // Initialize instance vars
        roomLabel = false;
        roomCenter = false;
        doorDirection = DoorDirection.NONE;
        secretPassage = ' ';
        // Assign the cell's initial char
        if (charLabel.length() == 1) { // Non doorway cell
            initial = charLabel.charAt(0);
        } else { // Doorway cell
            initial = charLabel.charAt(0); // Assign room char
            switch (charLabel.charAt(1)) { // Assign door direction
                case '>' -> doorDirection = DoorDirection.RIGHT;
                case '<' -> doorDirection = DoorDirection.LEFT;
                case '^' -> doorDirection = DoorDirection.UP;
                case 'v' -> doorDirection = DoorDirection.DOWN;
                case '#' -> roomLabel = true;
                case '*' -> roomCenter = true;
                default -> secretPassage = charLabel.charAt(1); // Secret passage cell
            }
        }
    }

    public boolean isWalkway() {
        return (initial == 'W');
    }

    /**
     * @param adj the adj to add to adjList
     */
    public void addAdj(BoardCell adj) {
        adjList.add(adj);
    }

    public boolean isDoorway() {
        return (doorDirection != DoorDirection.NONE);
    }

    public DoorDirection getDoorDirection() {
        return doorDirection;
    }

    public boolean isLabel() {
        return roomLabel;
    }

    public boolean isRoomCenter() {
        return roomCenter;
    }

    public char getSecretPassage() {
        return secretPassage;
    }

    public char getChar() {
        return initial;
    }


    public Set<BoardCell> getAdjList() {
        return adjList;
    }

    // Check if a cell is a room or is occupied
    public boolean isRoom() {
        return isRoom;
    }

    public boolean getOccupied() {
        return isOccupied;
    }

    public void setOccupied(boolean isOccupied) {
        this.isOccupied = isOccupied;
    }

    public void setRoom(boolean isRoom) {
        this.isRoom = isRoom;
    }
}
