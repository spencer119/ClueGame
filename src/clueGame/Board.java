package clueGame;

import java.util.Map;

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
    private Map<Character, Room> roomMap;
    private static Board theInstance = new Board();

    private Board() {
        super();
    }

    /**
     * Initializes the board
     */
    public void initialize() {
    }


    /**
     * Load ClueSetup.txt
     */
    public void loadSetupConfig() {

    }

    /**
     * Load ClueLayout.csv
     */
    public void loadLayoutConfig() {

    }


    /**
     * Set config file paths
     *
     * @param csvPath   path to the csv file
     * @param setupPath path to the setup file
     */
    public void setConfigFiles(String csvPath, String setupPath) {
        this.layoutConfigFile = csvPath;
        this.setupConfigFile = setupPath;
    }

    public static Board getInstance() {
        return theInstance;
    }

    public Room getRoom(char c) {

        return new Room();
    }

    public int getNumRows() {

        return numRows;
    }

    public int getNumColumns() {

        return numColumns;
    }

    public BoardCell getCell(int i, int j) {

        return new BoardCell();
    }

    public Room getRoom(BoardCell cell) {

        return new Room();
    }
}
