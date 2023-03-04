package experiment;

import java.util.HashSet;
import java.util.Set;

/**
 * TestBoard class for testing
 */
public class TestBoard {
    public TestBoard() {
    }

    /**
     * Calculate potential cells to move to
     * @param startCell Cell to start movement from
     * @param pathLength number of cells to move
     */
    public void calcTargets(TestBoardCell startCell, int pathLength) {}

    /**
     * @param row row position of cell
     * @param col column position of cell
     * @return Specific cell at specified column and row
     */
    public TestBoardCell getCell(int row, int col) {
        TestBoardCell cell = new TestBoardCell(row, col);
        return cell;
    }

    /**
     * @return Set of cells calculated from calcTargets()
     */
    public Set<TestBoardCell> getTargets() {
        return new HashSet<TestBoardCell>();
    }
}
