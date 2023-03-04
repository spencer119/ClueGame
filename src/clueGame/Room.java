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
