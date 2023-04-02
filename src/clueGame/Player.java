package clueGame;

public abstract class Player {
    private final String name;
    private final String color;
    private final int row;
    private final int col;

    public Player(String name, String color, int row, int col) {
        this.name = name;
        this.color = color;
        this.row = row;
        this.col = col;
    }


    public void updateHand(Card card) {
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

}