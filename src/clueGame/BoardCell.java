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

    private char initial;

    private DoorDirection doorDirection;

    private Boolean roomLabel;
    private Boolean roomCenter;

    private char secretPassage;
    private Set<BoardCell> adjList;

    /**
     * @param adj the adj to add to adjList
     */
    public void addAdj(BoardCell adj) {

    }

    public boolean isDoorway() {
        return false;
    }

    public Object getDoorDirection() {
        return null;
    }

    public boolean isLabel() {
        return false;
    }

    public boolean isRoomCenter() {
        return false;
    }

    public char getSecretPassage() {
        return 0;
    }
}
