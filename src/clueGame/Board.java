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
    private int numColumns;
    private String layoutConfigFile;
    private String setupConfigFile;
    private Map<Character, Room> roomMap = new HashMap<>();
    private static Board theInstance = new Board();
    private Set<BoardCell> targets = new HashSet<BoardCell>();
    private Set<BoardCell> visited = new HashSet<BoardCell>();
    private Set<BoardCell> calcVisited = new HashSet<BoardCell>(); // Temporary visited list for calculating targets

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
        } catch (BadConfigFormatException e) {
            System.out.println(e);
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
                if (line.startsWith("//")) {
                    continue;
                } else {
                    String[] split = line.split(", ");
                    if (split.length != 3 || split[2].length() != 1) throw new BadConfigFormatException();
                    else if (!split[0].equals("Room") && !split[0].equals("Space"))
                        throw new BadConfigFormatException();
                    roomMap.put(split[2].charAt(0), new Room(split[1]));
                }
            }
            file.close();
        } catch (IOException e) {
            System.out.println(e);
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
                for (String s : line.split(","))
                    if (s.length() == 0 || s.length() > 2) throw new BadConfigFormatException();
                rows.add(new ArrayList<String>(Arrays.asList(line.split(","))));
            }

            numColumns = rows.get(0).size();
            numRows = rows.size();
            setupBoard(rows);

            // check that all rooms have a label and center cell
            for (Room room : roomMap.values()) {
                if (room.getLabelCell() == null || room.getCenterCell() == null)
                    throw new BadConfigFormatException();
            }
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
            grid = new BoardCell[numRows][numColumns];
            for (int i = 0; i < numRows; i++) {
                for (int j = 0; j < numColumns; j++) {
                    String cellStr = rows.get(i).get(j);
                    BoardCell cell = new BoardCell(cellStr, i, j);
                    grid[i][j] = cell;
                    Room room = roomMap.get(cell.getChar());
                    if (cell.isLabel()) {
                        room.setLabelCell(cell);
                    }
                    if (cell.isRoomCenter()) {
                        room.setCenterCell(cell);
                    }
                }
            }
        } catch (IndexOutOfBoundsException e) {
            throw new BadConfigFormatException();
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

    public Room getRoom(char c) {

        return roomMap.get(c);
    }

    public int getNumRows() {

        return numRows;
    }

    public int getNumColumns() {

        return numColumns;
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

    public void calcTargets(BoardCell startCell, int pathLength) {
    }

    /**
     * @return Set of cells calculated from calcTargets()
     */
    public Set<BoardCell> getTargets() {
        return targets;
    }
}

