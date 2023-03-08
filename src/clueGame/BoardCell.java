package clueGame;

import java.util.Set;

/**
 * BoardCell class
 *
 * @author spencer
 */
public class BoardCell {
    private int row;
    private int col;
    private boolean isOccupied;
    private boolean isRoom;
    private char initial;

    private DoorDirection doorDirection;

    private Boolean roomLabel;
    private Boolean roomCenter;

    private char secretPassage;
    private Set<BoardCell> adjList;

    /**
     * @param charLabel the label of the cell
     * @param row       the row of the cell
     * @param col       the column of the cell
     */
    public BoardCell(String charLabel, int row, int col) {
        this.row = row;
        this.col = col;

        roomLabel = false;
        roomCenter = false;
        doorDirection = DoorDirection.NONE;
        secretPassage = ' ';

        if (charLabel.length() == 1) {
            initial = charLabel.charAt(0);
        } else {
            initial = charLabel.charAt(0);
            switch (charLabel.charAt(1)) {
                case '>':
                    doorDirection = DoorDirection.RIGHT;
                    break;
                case '<':
                    doorDirection = DoorDirection.LEFT;
                    break;
                case '^':
                    doorDirection = DoorDirection.UP;
                    break;
                case 'v':
                    doorDirection = DoorDirection.DOWN;
                    break;
                case '#':
                    roomLabel = true;
                    break;
                case '*':
                    roomCenter = true;
                    break;
                default:
                    secretPassage = charLabel.charAt(1);
                    break;
            }
        }
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
