package clueGame;

import experiment.TestBoardCell;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Board class
 *
 * @author Spencer Hamilton
 */
public class Board {
    private BoardCell grid[][];
    private int numRows;
    private int numCols;
    private String layoutConfigFile;
    private String setupConfigFile;
    private final Map<Character, Room> roomMap = new HashMap<>();
    private static Board theInstance = new Board();
    private Set<BoardCell> targets = new HashSet<BoardCell>();
    private Set<BoardCell> visited = new HashSet<BoardCell>();


    private Board() {
        super();

    }

    /**
     * Initializes the board
     */
    public void initialize() {
        try {
            loadSetupConfig();
            loadLayoutConfig();
            setupAdj();
        } catch (BadConfigFormatException e) {
            System.out.println(e);
        }
    }

    /**
     * Create the adjacency list for each cell
     */
    private void setupAdj() {
        targets = new HashSet<BoardCell>();
        visited = new HashSet<BoardCell>();

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                BoardCell cell = grid[i][j];
                if (cell.isRoom() && !cell.isRoomCenter()) continue; // Ignore all room spots that aren't the center
                // Doorway adjacencies
                if (cell.isDoorway()) {
                    switch (cell.getDoorDirection()) { // Map each doorway to the center of the room
                        case UP -> { // Use the direction to find the room cell adjacent to the door
                            cell.addAdj(roomMap.get(grid[i - 1][j].getChar()).getCenterCell());
                            roomMap.get(grid[i - 1][j].getChar()).getCenterCell().addAdj(cell);
                        }
                        case DOWN -> {
                            cell.addAdj(roomMap.get(grid[i + 1][j].getChar()).getCenterCell());
                            roomMap.get(grid[i + 1][j].getChar()).getCenterCell().addAdj(cell);
                        }
                        case LEFT -> {
                            cell.addAdj(roomMap.get(grid[i][j - 1].getChar()).getCenterCell());
                            roomMap.get(grid[i][j - 1].getChar()).getCenterCell().addAdj(cell);
                        }
                        case RIGHT -> {
                            cell.addAdj(roomMap.get(grid[i][j + 1].getChar()).getCenterCell());
                            roomMap.get(grid[i][j + 1].getChar()).getCenterCell().addAdj(cell);
                        }
                    }
                }
                if (cell.getSecretPassage() != ' ') // Map secret passages to the center of the room
                    roomMap.get(cell.getChar()).setSecretPassage(cell.getSecretPassage());
                if (i > 0 && grid[i - 1][j].isWalkway()) {
                    cell.addAdj(grid[i - 1][j]);
                }
                if (i < numRows - 1 && grid[i + 1][j].isWalkway()) {
                    cell.addAdj(grid[i + 1][j]);
                }
                if (j > 0 && grid[i][j - 1].isWalkway()) {
                    cell.addAdj(grid[i][j - 1]);
                }
                if (j < numCols - 1 && grid[i][j + 1].isWalkway()) {
                    cell.addAdj(grid[i][j + 1]);
                }
            }
        }
        for (Room r : roomMap.values()) { // Map secret passages if it exists
            if (r.getName() == "Walkway") continue;
            if (r.getSecretPassage() != ' ')
                r.getCenterCell().addAdj(roomMap.get(r.getSecretPassage()).getCenterCell()); // Add the ends of SP to adjList
        }
    }


    /**
     * Load ClueSetup.txt
     */
    public void loadSetupConfig() throws BadConfigFormatException {
        try {
            FileReader file = new FileReader(setupConfigFile);
            Scanner scan = new Scanner(file);
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                if (line.startsWith("//")) { // Ignore comments
                    continue;
                } else {
                    String[] split = line.split(", ");
                    // Check for bad config format
                    if (split.length != 3 || split[2].length() != 1) throw new BadConfigFormatException();
                    else if (!split[0].equals("Room") && !split[0].equals("Space"))
                        throw new BadConfigFormatException();
                    roomMap.put(split[2].charAt(0), new Room(split[1]));
                }
            }
            file.close();
        } catch (IOException e) {
            throw new BadConfigFormatException();
        }

    }


    /**
     * Load ClueLayout.csv
     */
    public void loadLayoutConfig() throws BadConfigFormatException {
        try {
            FileReader file = new FileReader(layoutConfigFile);
            Scanner scan = new Scanner(file);
            ArrayList<ArrayList<String>> rows = new ArrayList<>();
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                for (String s : line.split(",")) // Check for bad config format
                    if (s.length() == 0 || s.length() > 2) throw new BadConfigFormatException();
                rows.add(new ArrayList<String>(Arrays.asList(line.split(",")))); // Add each line, split by commas, as an array
            }
            numCols = rows.get(0).size();
            numRows = rows.size();
            setupBoard(rows); // continue board setup
            file.close();
        } catch (FileNotFoundException | ArrayIndexOutOfBoundsException e) {
            throw new BadConfigFormatException();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Helper function for constructor to create the board
     *
     * @param rows the rows of the board
     * @throws BadConfigFormatException bad config format
     */
    private void setupBoard(ArrayList<ArrayList<String>> rows) throws BadConfigFormatException {
        try {
            grid = new BoardCell[numRows][numCols];
            for (int i = 0; i < numRows; i++) {
                for (int j = 0; j < numCols; j++) {
                    String cellStr = rows.get(i).get(j);
                    BoardCell cell = new BoardCell(cellStr, i, j);
                    grid[i][j] = cell;
                    // Assign room labels, centers, and secret passages
                    Room room = roomMap.get(cell.getChar());
                    if (cell.isLabel()) {
                        room.setLabelCell(cell);
                    }
                    if (cell.isRoomCenter()) {
                        room.setCenterCell(cell);
                    }
                    if (cell.getSecretPassage() != ' ') {
                        roomMap.get(cell.getSecretPassage()).setSecretPassage(cell.getChar());
                    }
                }
            }
        } catch (IndexOutOfBoundsException e) {
            throw new BadConfigFormatException();
        }
    }

    /**
     * Calculate a set of possible targets
     *
     * @param startCell  starting cell
     * @param pathLength number of steps
     */
    public void calcTargets(BoardCell startCell, int pathLength) {
        visited.clear(); // Clear previous visited or target lists
        targets.clear();
        calcTargetsHelper(startCell, pathLength); // Call recursive function
    }

    /**
     * Recursive function to calculate targets
     *
     * @param cell       current cell of the recursion
     * @param pathLength number of steps left
     */
    public void calcTargetsHelper(BoardCell cell, int pathLength) {
        if ((pathLength == 0 || cell.isRoom()) && !visited.isEmpty()) {
            if (!cell.getOccupied()) {
                targets.add(cell);
                visited.remove(cell);
            }
            return;
        }
        visited.add(cell);
        for (BoardCell adj : cell.getAdjList()) {
            if (!visited.contains(adj) && !adj.getOccupied()) {
                calcTargetsHelper(adj, pathLength - 1);
                visited.remove(adj);
            }
        }
    }

    /**
     * Set config file paths
     *
     * @param csvPath   path to the csv file
     * @param setupPath path to the setup file
     */
    public void setConfigFiles(String csvPath, String setupPath) {
        this.layoutConfigFile = "data/" + csvPath;
        this.setupConfigFile = "data/" + setupPath;
    }

    public static Board getInstance() {
        return theInstance;
    }

    // Getters

    public Room getRoom(char c) {

        return roomMap.get(c);
    }

    public int getNumRows() {

        return numRows;
    }

    public int getNumCols() {

        return numCols;
    }

    public BoardCell getCell(int i, int j) {

        return grid[i][j];
    }

    public Room getRoom(BoardCell cell) {

        return roomMap.get(cell.getChar());
    }

    public Set<BoardCell> getAdjList(int i, int j) {
        return grid[i][j].getAdjList();
    }

    public Set<BoardCell> getTargets() {
        return targets;
    }
}

