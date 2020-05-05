package it.polimi.ingsw.PSP4.model.serializable;

public enum CLIChar {
    NUMBER_HORIZONTAL("  0   1   2   3   4  "),
    BOARD_HORIZONTAL("┼───┼───┼───┼───┼───┼"),
    FIRST_LEVEL("■"),
    SECOND_LEVEL("▣"),
    THIRD_LEVEL("□"),
    DOME("●"),
    BLANK(" "),
    ERROR("#"),
    WORKER_ON_GROUND("♟"),
    AVIABLE_POSITIONS_LEFT("▸"),
    AVIABLE_POSITIONS_RIGHT("◂"),
    END_BOARD("|"),
    KEY_TITLE("SYMBOL KEY");


    private final String string;
    public String getString() {return this.string;}

    CLIChar(String string) {
        this.string = string;
    }
}
