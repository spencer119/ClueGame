package clueGame;

import java.util.Map;

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

    public void initialize() {
    }

    public static Board getInstance() {
        return theInstance;
    }

    public void loadSetupConfig() {

    }

    public void loadLayoutConfig() {

    }


    public void setConfigFiles(String csvPath, String setupPath) {
        this.layoutConfigFile = csvPath;
        this.setupConfigFile = setupPath;
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
