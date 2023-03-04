package experiment;

import java.util.HashSet;
import java.util.Set;

/**
 * TestBoard class for testing
 */
public class TestBoard {
    final static int ROWS = 4;
    final static int COLS = 4;
    private TestBoardCell[][] grid;
    private Set<TestBoardCell> targets = new HashSet<TestBoardCell>();
    private Set<TestBoardCell> visited = new HashSet<TestBoardCell>();
    private Set<TestBoardCell> calcVisited = new HashSet<TestBoardCell>(); // Temporary visited list for calculating targets

    public TestBoard() {
        grid = new TestBoardCell[ROWS][COLS];
        targets = new HashSet<TestBoardCell>();
        visited = new HashSet<TestBoardCell>();
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                grid[i][j] = new TestBoardCell(i, j);
            }
        }
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                if (i > 0) {
                    grid[i][j].addAdjacency(grid[i - 1][j]);
                }
                if (i < ROWS - 1) {
                    grid[i][j].addAdjacency(grid[i + 1][j]);
                }
                if (j > 0) {
                    grid[i][j].addAdjacency(grid[i][j - 1]);
                }
                if (j < COLS - 1) {
                    grid[i][j].addAdjacency(grid[i][j + 1]);
                }
            }
        }
    }



    /**
     * Calculate potential cells to move to
     * @param startCell Cell to start movement from
     * @param pathLength number of cells to move
     */
    public void calcTargets(TestBoardCell startCell, int pathLength) {
        if (pathLength == 0 || startCell.isRoom()) {
            if (!startCell.getOccupied())
            {
                targets.add(startCell);
            }
            return;
        }
        calcVisited.add(startCell);
        for (TestBoardCell adj : startCell.getAdjList()) {
            if (!visited.contains(adj) && !calcVisited.contains(adj)&& !adj.getOccupied()) {
                calcTargets(adj, pathLength - 1);
                calcVisited.remove(adj);
            }
        }
    }

    /**
     * @param row row position of cell
     * @param col column position of cell
     * @return Specific cell at specified column and row
     */
    public TestBoardCell getCell(int row, int col) {
        return grid[row][col];
    }

    /**
     * @return Set of cells calculated from calcTargets()
     */
    public Set<TestBoardCell> getTargets() {
        return targets;
    }
}
