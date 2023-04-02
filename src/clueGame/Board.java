package clueGame;

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
    private static final Board theInstance = new Board();
    private final Map<Character, Room> roomMap = new HashMap<>();
    private final ArrayList<Card> deck = new ArrayList<>();
    private final ArrayList<Player> players = new ArrayList<>();
    private BoardCell[][] grid;
    private int numRows;
    private int numCols;
    private String layoutConfigFile;
    private String setupConfigFile;
    private Set<BoardCell> targets = new HashSet<>();
    private Set<BoardCell> visited = new HashSet<>();

    // Default constructor
    private Board() {
        super();
    }

    public static Board getInstance() {
        return theInstance;
    }

    /**
     * Initializes the board
     */
    public void initialize() {
        try {
            loadSetupConfig(); // Load ClueSetup.txt
            loadLayoutConfig(); // Load ClueLayout.csv
            setupAdj(); // Setup adjacency lists
            deal(); // Deal cards
        } catch (BadConfigFormatException e) { // Catch any bad config file format exceptions
            System.out.println("Bad config file format.");
        }
    }

    /**
     * Create the adjacency list for each cell
     */
    private void setupAdj() {
        targets = new HashSet<>();
        visited = new HashSet<>();

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                BoardCell cell = grid[i][j];
                if (cell.isRoom() && !cell.isRoomCenter()) continue; // Ignore all room spots that aren't the center
                // Doorway adjacency
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
                // If the current cell has a secret passage, map it to the center of the room
                if (cell.getSecretPassage() != ' ') {
                    roomMap.get(cell.getChar()).setSecretPassage(cell.getSecretPassage());
                }
                // Add adjacent walkway cells to the current cell's adjacency list, if they exist
                if (i > 0 && grid[i - 1][j].isWalkway()) { // check cell above
                    cell.addAdj(grid[i - 1][j]);
                }
                if (i < numRows - 1 && grid[i + 1][j].isWalkway()) { // check cell below
                    cell.addAdj(grid[i + 1][j]);
                }
                if (j > 0 && grid[i][j - 1].isWalkway()) { // check cell to the left
                    cell.addAdj(grid[i][j - 1]);
                }
                if (j < numCols - 1 && grid[i][j + 1].isWalkway()) { // check cell to the right
                    cell.addAdj(grid[i][j + 1]);
                }
            }
        }
        for (Room r : roomMap.values()) { // Map secret passages if it exists
            if (r.getName().equals("Walkway")) continue;
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
                if (!line.startsWith("//")) { // Ignore comments
                    String[] split = line.split(", ");
                    // Check for bad config format
                    switch (split[0]) {
                        case "Room":
                        case "Space":
                            roomMap.put(split[2].charAt(0), new Room(split[1])); // Add to roomMap
                            break;
                        case "Player":
                            System.out.println(split[1] + " " + split[2] + " " + split[3] + " " + split[4] + " " + split[5]);
                            try {
                                int row = Integer.parseInt(split[4]);
                                int col = Integer.parseInt(split[5]);
                                if (split[1].equals("Human")) {
                                    players.add(new HumanPlayer(split[2], split[3], row, col));
                                } else if (split[1].equals("Computer")) {
                                    players.add(new ComputerPlayer(split[2], split[3], row, col));
                                }
                            } catch (NumberFormatException e) {
                                throw new BadConfigFormatException();
                            }
                            break;
                        case "Weapon":
                            break;
                        default:
                            break;

                    }

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
            ArrayList<ArrayList<String>> boardRows = new ArrayList<>(); // For rooms and spaces
            // For Players, Cards, Weapons, etc...
            ArrayList<ArrayList<String>> otherRows = new ArrayList<>();
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                for (String s : line.split(",")) // Check for bad config format
                    if (s.length() == 0 || s.length() > 2) throw new BadConfigFormatException();
                boardRows.add(new ArrayList<>(Arrays.asList(line.split(",")))); // Add each line, split by commas, as an array
            }
            // Set board dimensions
            numCols = boardRows.get(0).size();
            numRows = boardRows.size();
            file.close();
            setupBoard(boardRows); // continue board setup
        } catch (FileNotFoundException | ArrayIndexOutOfBoundsException e) { // Bad config
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
                    if (cell.getSecretPassage() != ' ') { // Check for secret passage
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
        if ((pathLength == 0 || cell.isRoom())) {
            if (!cell.getOccupied()) {
                targets.add(cell);
                visited.remove(cell); // Remove cell from visited so it can be visited again
            }
            return;
        }
        visited.add(cell); // Add to visited so you cannot backtrack in this move
        for (BoardCell adj : cell.getAdjList()) {
            if (!visited.contains(adj) && !adj.getOccupied()) {
                calcTargetsHelper(adj, pathLength - 1);
                visited.remove(adj); // Remove after recursion is finished
            }
        }
    }

    public void deal() {

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

    // Getters
    // Get a room by its char representation
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

    /**
     * @param i row position
     * @param j column position
     * @return adjacency of the cell (i,j)
     */
    public Set<BoardCell> getAdjList(int i, int j) {
        return grid[i][j].getAdjList();
    }

    // Get targets calculated by calcTargets()
    public Set<BoardCell> getTargets() {
        return targets;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }
}

