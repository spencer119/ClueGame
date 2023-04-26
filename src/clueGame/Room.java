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
    private final Set<Player> playersInRoom = new HashSet<Player>();
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

    // Getters
    public String getName() {
        return name;
    }

    public BoardCell getCenterCell() {
        return centerCell;
    }

    public void setCenterCell(BoardCell centerCell) {
        this.centerCell = centerCell;
    }

    public BoardCell getLabelCell() {
        return labelCell;
    }

    public void setLabelCell(BoardCell labelCell) {
        this.labelCell = labelCell;
    }

    public void addPlayer(Player player) {
        playersInRoom.add(player);
    }

    public void removePlayer(Player player) {
        playersInRoom.remove(player);
    }

    public Set<Player> getPlayersInRoom() {return playersInRoom;}
}
