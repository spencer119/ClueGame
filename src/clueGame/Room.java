package clueGame;

import java.util.HashSet;
import java.util.Set;

/**
 * Room class
 *
 * @author spencer
 */
public class Room {
    private final String name;
    private BoardCell centerCell;
    private BoardCell labelCell;
    private char secretPassage = ' '; // Char of the room that the secret passage leads to

    public Room(String name) {
        this.name = name;
    }

    // Setters
    public char getSecretPassage() {
        return secretPassage;
    }

    public void setSecretPassage(char secretPassage) {
        this.secretPassage = secretPassage;
    }

    public void setCenterCell(BoardCell centerCell) {
        this.centerCell = centerCell;
    }

    public void setLabelCell(BoardCell labelCell) {
        this.labelCell = labelCell;
    }

    // Getters
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
