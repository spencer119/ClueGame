package clueGame;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
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
                    System.out.println(split.length);
                    System.out.println(split[0] + " " + split[1] + " " + split[2]);
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
                rows.add(new ArrayList<String>(Arrays.asList(line.split(","))));
            }
            numColumns = rows.get(0).size();
            numRows = rows.size();
            setupBoard(rows);
        } catch (FileNotFoundException e) {
            throw new BadConfigFormatException();
        }

    }

    private void setupBoard(ArrayList<ArrayList<String>> rows) {
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
}
