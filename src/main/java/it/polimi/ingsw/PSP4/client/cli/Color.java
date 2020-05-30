package it.polimi.ingsw.PSP4.client.cli;

public enum Color {
    RED("\u001B[1;31m"),
    GREEN("\u001B[1;32m"),
    YELLOW("\u001B[1;33m"),
    BLUE("\u001B[1;34m"),
    PURPLE("\u001B[1;35m");

    public static final String RESET = "\u001B[0m";

    private final String escape;

    Color(String escape) {
        this.escape = escape;
    }
    public String getEscape() {
        return escape;
    }
    @Override
    public String toString() {
        return escape;
    }

    public String getName() {
        switch (this) {
            case RED:
                return "Red";
            case GREEN:
                return "Green";
            case YELLOW:
                return "Yellow";
            case PURPLE:
                return "Purple";
            default:
                return "Blue";
        }
    }
}