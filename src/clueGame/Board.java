package clueGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Board class
 *
 * @author Spencer Hamilton
 */
public class Board extends JPanel {
    private static final Board theInstance = new Board();
    private final Map<Character, Room> roomMap = new HashMap<>();

    private final ArrayList<Card> deck = new ArrayList<>();
    private final ArrayList<Player> players = new ArrayList<>();
    private BoardCell[][] grid;
    private int numRows;
    private int numCols;
    private int curRoll = 0;
    private String layoutConfigFile;
    private String setupConfigFile;
    private Set<BoardCell> targets = new HashSet<>();
    private Set<BoardCell> visited = new HashSet<>();
    private Solution theAnswer;
    private Player currentPlayer;
    private GameControlPanel controlPanel;
    private CardPanel cardPanel;
    private Boolean gameOver = false;

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
//            JOptionPane.showMessageDialog(null, "You are " + players.get(0).getName() + ".\nCan you find the solution before the AI?", "Welcome to Clue", JOptionPane.INFORMATION_MESSAGE);
        } catch (BadConfigFormatException e) { // Catch any bad config file format exceptions
            System.out.println("Bad config file format.");
        }
        addMouseListener(new BoardClick());
    }

    /**
     * Start the first player's turn and roll the dice
     */
    public void startGame() {
        currentPlayer = players.get(0);
        currentPlayer.setEndTurn(false);
        rollDie();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int height = getHeight();
        int width = getWidth();
        int cellLength = Math.min(width / numCols, height / numRows);
        int xOffset = (width - (cellLength * numCols)) / 2;
        int yOffset = (height - (cellLength * numRows)) / 2;
        g.setColor(Color.white);
        g.fillRect(0, 0, width, height);

        // List of doorways or room cells to be painted second
        ArrayList<BoardCell> secondLayer = new ArrayList<BoardCell>();

        for (BoardCell[] boardCells : grid) { // Draw non door or label cells
            for (BoardCell boardCell : boardCells) {
                if (boardCell.isLabel() || boardCell.isDoorway())
                    secondLayer.add(boardCell); // Add to be drawn later
                boardCell.draw(g, cellLength, xOffset, yOffset);

            }
        }

        for (BoardCell c : secondLayer) {
            if (c.isLabel())
                c.drawLabel(g, cellLength, xOffset, yOffset, roomMap.get(c.getChar()).getName()); // Draw labels
            else if (c.isDoorway()) c.drawDoor(g, cellLength, xOffset, yOffset); // Draw doorways
        }
        // Color possible targets green if it's the human player's turn
        if (currentPlayer instanceof HumanPlayer && !currentPlayer.isEndTurn()) {
            for (BoardCell c : targets) {
                if (c.isRoomCenter()) { // If the target is a room center, color every room cell green
                    for (BoardCell[] boardCells : grid) {
                        for (BoardCell boardCell : boardCells) {
                            if (boardCell.getChar() == c.getChar()) {
                                boardCell.drawTargetRoom(g, cellLength, xOffset, yOffset);
                            }
                        }
                    }
                    // Redraw the room label over the green cell
                    roomMap.get(c.getChar()).getLabelCell().drawLabel(g, cellLength, xOffset, yOffset, roomMap.get(c.getChar()).getName());
                } else {
                    c.drawTarget(g, cellLength, xOffset, yOffset); // Other non-room targets
                }
            }
        }
        int i = 1;
        for (Player p : players) { // Draw players on board
            if (p.getMovedBySuggestion()) {
                p.draw(g, cellLength, xOffset + (i * 5), yOffset);
                i++;
            } else
                p.draw(g, cellLength, xOffset, yOffset);
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
                        case "Room", "Space" -> {
                            roomMap.put(split[2].charAt(0), new Room(split[1])); // Add to roomMap
                            if (!split[0].equals("Space")) // Add to deck
                                deck.add(new Card(split[1], CardType.ROOM));
                        }
                        case "Player" -> {
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
                            deck.add(new Card(split[2], CardType.PERSON));
                        }
                        case "Weapon" -> deck.add(new Card(split[1], CardType.WEAPON));
                        default -> {
                        }
                    }

                }
            }
            file.close();
            scan.close();
        } catch (IOException e) {
            throw new BadConfigFormatException();
        }

    }

    public void createSolution() {
        Random rand = new Random();
        int person = -1, weapon = -1, room = -1;
        while (person == -1 || weapon == -1 || room == -1) {
            int index = rand.nextInt(deck.size());
            Card c = deck.get(index);
            switch (c.getType()) {
                case PERSON -> {
                    if (person == -1) {
                        person = index;
                    }
                }
                case WEAPON -> {
                    if (weapon == -1) {
                        weapon = index;
                    }
                }
                case ROOM -> {
                    if (room == -1) {
                        room = index;
                    }
                }
            }
        }
        Card r = deck.get(room);
        Card p = deck.get(person);
        Card w = deck.get(weapon);
        theAnswer = new Solution(r, p, w);
        deck.remove(r);
        deck.remove(p);
        deck.remove(w);
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
     * Roll a random number and calculate targets for that roll with the current player
     */
    private void rollDie() {
        Random rand = new Random(System.currentTimeMillis());
        int roll;
        int attempts = 0;
        do {
            roll = rand.nextInt(6) + 1;
            this.calcTargets(getCell(currentPlayer.getRow(), currentPlayer.getCol()), roll);
            attempts++;
            if (attempts > 100) break;
        } while (this.getTargets().size() == 0);
        curRoll = roll;
    }

    public int getRoll() {
        return curRoll;
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

//    public void moveToRoom(Player player, Room room) {
//
//    }

    public void moveToRoom(Card playerCard, Room room) {
        for (Player p : this.getPlayers()) {
            if (playerCard.getCardName().equals(p.getName())) {
                p.getCell().setOccupied(false);
                p.moveBySuggestion(room.getCenterCell());
            }
        }
        repaint();
    }

    public void updateControlPanel(Player suggestingPlayer, String guess, String guessResult) {
        if (guess != null)
            controlPanel.setGuess(guess);
        if (guessResult != null)
            controlPanel.setGuessResult(guessResult);
        if (suggestingPlayer != null)
            controlPanel.setGuessPlayer(suggestingPlayer);
    }


    public Boolean checkAccusation(Player accuser, Solution accusation) {
        if (accusation.equals(theAnswer)) {
            this.createDialog("Game over", accuser.getName() + " wins!\nSolution: " + theAnswer.toString());
        } else {
            this.createDialog("Game over", accuser.getName() + " loses!\n The correct solution was: " + theAnswer.toString());
        }
        endGame();
        return accusation.equals(theAnswer);
    }

    public Card handleSuggestion(Player suggestingPlayer, Card person, Card room, Card weapon) {
        moveToRoom(person, roomMap.get(suggestingPlayer.getCell().getChar()));
        String guessStr = person.toString() + ", " + room.toString() + ", " + weapon.toString();
        for (Player p : players) {
            if (p != suggestingPlayer) {
                Card c = p.disproveSuggestion(person, room, weapon);
                if (c != null) {
                    suggestingPlayer.updateSeen(c);
                    if (suggestingPlayer instanceof HumanPlayer)
                        updateControlPanel(suggestingPlayer, guessStr, "Disproven by " + p + ": " + c);
                    else
                        updateControlPanel(suggestingPlayer, guessStr, "Disproven by " + p);
                    return c;
                }
            }
        }
        updateControlPanel(suggestingPlayer, guessStr, "No new clue");
        return null;
    }

    public void addSeen(Card card) {
        cardPanel.updateSeen(card);
//        cardPanel.updateCardPanel(card.getType());
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
        calcTargetsHelper(startCell, pathLength, startCell.isRoom()); // Call recursive function
    }

    /**
     * Recursive function to calculate targets
     *
     * @param cell       current cell of the recursion
     * @param pathLength number of steps left
     */
    public void calcTargetsHelper(BoardCell cell, int pathLength, Boolean startInRoom) {

        if (!startInRoom) {
            if ((pathLength == 0 || cell.isRoom() || cell.isRoomCenter())) {
                if (!cell.getOccupied()) {
                    targets.add(cell);
                    visited.remove(cell); // Remove cell from visited so it can be visited again
                }
                return;
            }
        }
        visited.add(cell); // Add to visited so you cannot backtrack in this move
        for (BoardCell adj : cell.getAdjList()) {
            if (!visited.contains(adj) && !adj.getOccupied()) {
                calcTargetsHelper(adj, pathLength - 1, false);
                visited.remove(adj); // Remove after recursion is finished
            }
        }
    }

    public void deal() {
        createSolution(); // This removes the solution from the deck
        Collections.shuffle(deck);
        int i = 0;
        for (Card c : deck) {
            players.get(i).updateHand(c);
            if (i == players.size() - 1) {
                i = 0;
            } else {
                i++;
            }
        }
        // Re-add solution to deck
        deck.add(theAnswer.getRoom());
        deck.add(theAnswer.getPerson());
        deck.add(theAnswer.getWeapon());
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

    public ArrayList<Card> getDeck() {
        return deck;
    }

    public int getNumRows() {

        return numRows;
    }

    public int getNumCols() {

        return numCols;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public BoardCell getCell(int i, int j) {

        return grid[i][j];
    }

    public Room getRoom(BoardCell cell) {

        return roomMap.get(cell.getChar());
    }

    public Boolean isGameOver() {return gameOver;}

    public void endGame() {
        gameOver = true;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
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

    public void setControlPanel(GameControlPanel controlPanel) {
        this.controlPanel = controlPanel;
    }

    public void setCardPanel(CardPanel cardPanel) {
        this.cardPanel = cardPanel;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public Solution getTheAnswer() {
        return theAnswer;
    }

    public void nextTurn() {
        currentPlayer.setEndTurn(false);
        int index = players.indexOf(currentPlayer);
        if (index == players.size() - 1)
            currentPlayer = players.get(0);
        else
            currentPlayer = players.get(index + 1);
        rollDie();
        if (currentPlayer instanceof HumanPlayer) {
            controlPanel.setNextBtn(false);
            if (currentPlayer.getMovedBySuggestion() && currentPlayer.getCell().isRoom()) {
                int res = JOptionPane.showConfirmDialog(null, "You were moved to this room by a suggestion.\nDo you want to make a suggestion in this room?", "Moved by Suggestion", JOptionPane.YES_NO_OPTION);
                if (res == JOptionPane.YES_OPTION) {
                    SuggestionDialog suggest = new SuggestionDialog(null);
                    currentPlayer.setMovedBySuggestion(false);
                    currentPlayer.setEndTurn(true);
                    controlPanel.setNextBtn(true);
                }
            }
        }
    }

    public void createDialog(String title, String message) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    public Player getHumanPlayer() {
        for (Player p : players) {
            if (p instanceof HumanPlayer) {
                return p;
            }
        }
        return null;
    }

    private class BoardClick implements MouseListener {
        public void mousePressed(MouseEvent e) {
        }

        public void mouseClicked(MouseEvent e) {
            if (currentPlayer instanceof ComputerPlayer) return;
            if (currentPlayer.isEndTurn())
                createDialog("Error", "You have already moved for this turn.\nClick next to end your turn");
            if (currentPlayer.getMovedBySuggestion()) currentPlayer.setMovedBySuggestion(false);
            int row = currentPlayer.getRow();
            int col = currentPlayer.getCol();
            int x = getWidth();
            int y = getHeight();
            int cellSize = Math.min((x / numCols), (y / numRows));
            int xOffset = (x - (numCols * cellSize)) / 2;
            int yOffset = (y - (numRows * cellSize)) / 2;
            int newRow = (e.getY() - yOffset) / cellSize;
            int newCol = (e.getX() - xOffset) / cellSize;
            BoardCell target = getCell(newRow, newCol);
            // If they clicked on a room but not the center cell, change target to center
            if (target.isRoom() && !target.isRoomCenter()) {
                target = roomMap.get(target.getChar()).getCenterCell();
                newRow = target.getRow();
                newCol = target.getCol();
                getCell(row, col).setOccupied(false); // Update occupied
            }
            if (targets.contains(target)) {
                currentPlayer.move(target); // Update player position
                getCell(row, col).setOccupied(false); // Update occupied
                getCell(newRow, newCol).setOccupied(true);
                controlPanel.setNextBtn(true);
                currentPlayer.setEndTurn(true);
                if (getCell(newRow, newCol).isRoom()) {
                    repaint();
                    SuggestionDialog suggestionDialog = new SuggestionDialog(null);
                } else {
                    repaint();
                }
            } else {
                createDialog("Invalid move", "Not a valid target");
            }
        }

        public void mouseReleased(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }
    }
}

