package clueGame;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

/**
 * BoardCell class
 *
 * @author spencer
 */
public class BoardCell extends JPanel {
    private final int row;
    private final int col;
    private final char initial;
    private final Set<BoardCell> adjList = new HashSet<>(); // List of adjacent cells
    private boolean isOccupied;
    private boolean isRoom;
    private DoorDirection doorDirection;
    private char doorRoom;
    private Boolean roomLabel;
    private Boolean roomCenter;
    private char secretPassage;

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

    public void draw(Graphics g, int cellSize, int xOffset, int yOffset) {
        int x = (col * cellSize) + xOffset;
        int y = (row * cellSize) + yOffset;
        switch (initial) {
            case 'W' -> {
                g.setColor(Color.YELLOW);
                g.fillRect(x, y, cellSize, cellSize);
                g.setColor(Color.BLACK);
                g.drawRect(x, y, cellSize, cellSize);
            }
            case 'X' -> {
                g.setColor(Color.BLACK);
                g.fillRect(x, y, cellSize, cellSize);
                g.drawRect(x, y, cellSize, cellSize);
            }

            default -> {
                if (secretPassage == ' ') {
                    g.setColor(Color.GRAY);
                    g.fillRect(x, y, cellSize, cellSize);
                } else {
                    g.setColor(Color.GRAY);
                    g.fillRect(x, y, cellSize, cellSize);
                    g.setColor(Color.CYAN);
//                    g.fillRect(x, y, cellSize, cellSize);
                    g.drawString("SP", x + (cellSize / 4), y + (cellSize / 2));
                }
            }
        }
        repaint();
    }

    public void drawDoor(Graphics g, int cellSize, int xOffset, int yOffset) {
        int x = (col * cellSize) + xOffset;
        int y = (row * cellSize) + yOffset;
        int doorSize = cellSize / 7;
        g.setColor(Color.BLUE);
        switch (doorDirection) {
            case UP -> g.fillRect(x, y - doorSize, cellSize, doorSize);
            case DOWN -> g.fillRect(x, y + cellSize, cellSize, doorSize);
            case LEFT -> g.fillRect(x - doorSize, y, doorSize, cellSize);
            case RIGHT -> g.fillRect(x + cellSize, y, doorSize, cellSize);
        }
    }

//    public void drawDoor(Graphics g, int cellSize, int xOffset, int yOffset) {
//        int x = (col * cellSize) + xOffset;
//        int y = (row * cellSize) + yOffset;
//        int margin = cellSize / 4;
//        int[] xPoints;
//        int[] yPoints;
//        switch (this.doorDirection) {
//            case UP -> {
//                xPoints = new int[]{x + margin, x + cellSize / 2, x + cellSize - margin};
//                yPoints = new int[]{y + cellSize - margin, y + margin, y + cellSize - margin};
//            }
//            case DOWN -> {
//                xPoints = new int[]{x + margin, x + cellSize / 2, x + cellSize - margin};
//                yPoints = new int[]{y + margin, y + cellSize - margin, y + margin};
//            }
//            case LEFT -> {
//                xPoints = new int[]{x + cellSize - margin, x + margin, x + cellSize - margin};
//                yPoints = new int[]{y + margin, y + cellSize / 2, y + cellSize - margin};
//            }
//            case RIGHT -> {
//                xPoints = new int[]{x + margin, x + cellSize - margin, x + margin};
//                yPoints = new int[]{y + margin, y + cellSize / 2, y + cellSize - margin};
//            }
//
//            default -> throw new IllegalStateException("Invalid door direction");
//        }
////        int[] xPoints = {x + margin, x + cellSize - margin, x + margin};
////        int[] yPoints = {y + margin, y + cellSize / 2, y + cellSize - margin};
//        g.setColor(Color.YELLOW);
//        g.fillRect(x, y, cellSize, cellSize);
//        g.setColor(Color.BLACK);
//        g.drawRect(x, y, cellSize, cellSize);
//        g.setColor(Color.GRAY);
//        g.fillPolygon(xPoints, yPoints, 3);
//    }

    public void drawLabel(Graphics g, int cellSize, int xOffset, int yOffset, String name) {
        int x = (cellSize * col) + xOffset;
        int y = (cellSize * row) + yOffset;
        g.setColor(Color.BLUE);
        g.drawString(name, x, y + cellSize / 2);
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

    public void setRoom(boolean isRoom) {
        this.isRoom = isRoom;
    }

    public boolean getOccupied() {
        return isOccupied;
    }

    public void setOccupied(boolean isOccupied) {
        this.isOccupied = isOccupied;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}
