package experiment;

import java.util.Set;

public class TestBoardCell {
    private int row;
    private int col;
    private boolean isOccupied;
    private boolean isRoom;
    private Set<TestBoardCell> adjacentList;
    public TestBoardCell(int row, int col) {
        this.row = row;
        this.col = col;
        isOccupied = false;
        isRoom = false;
    }
    public void addAjacency(TestBoardCell cell) {}


    public Set<TestBoardCell> getAdjList() {
        return adjacentList;
    }

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
