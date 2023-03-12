package clueGame;

import java.util.HashSet;
import java.util.Set;

/**
 * Room class
 *
 * @author spencer
 */
public class Room {

    private String name;
    private BoardCell centerCell;
    private BoardCell labelCell;
    private Set<BoardCell> exitCells = new HashSet<>();

    public Room(String name) {
        this.name = name;
    }

    public void setCenterCell(BoardCell centerCell) {
        this.centerCell = centerCell;
    }

    public void setLabelCell(BoardCell labelCell) {
        this.labelCell = labelCell;
    }

    public void addExitCell(BoardCell exitCell) {
        exitCells.add(exitCell);
    }

    public String getName() {
        return name;
    }

    public BoardCell getCenterCell() {
        return centerCell;
    }

    public BoardCell getLabelCell() {
        return labelCell;
    }
}
