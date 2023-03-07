package clueGame;

/**
 * Room class
 *
 * @author spencer
 */
public class Room {

    private String name;
    private BoardCell centerCell;
    private BoardCell labelCell;

    public Room(String name) {
        this.name = name;
    }

    public void setCenterCell(BoardCell centerCell) {
        this.centerCell = centerCell;
    }

    public void setLabelCell(BoardCell labelCell) {
        this.labelCell = labelCell;
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
