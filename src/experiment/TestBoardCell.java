package experiment;

import java.util.HashSet;
import java.util.Set;

public class TestBoardCell {
    private int row;
    private int col;
    private boolean isOccupied;
    private boolean isRoom;

    private Set<TestBoardCell> adjList; // List of adjacent cells

    /** Constructor for testing board cell
     * @param row cell row position
     * @param col cell column position
     */
    public TestBoardCell(int row, int col) {
        this.row = row;
        this.col = col;
        isOccupied = false;
        isRoom = false;
    }

    /**
     * @param cell Add cell to the adjacency list
     */
    public void addAjacency(TestBoardCell cell) {}


    /**
     * @return Get list of adjacent cells to the current cell
     */
    public Set<TestBoardCell> getAdjList() {
        return new HashSet<TestBoardCell>();
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
