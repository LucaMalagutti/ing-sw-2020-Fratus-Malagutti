package it.polimi.ingsw.PSP4.controller.turnStates;

/**
 * Enum which identifies the type of a state
 */
public enum StateType {
    MOVE("Move"), BUILD("Build"), WAIT("Wait");

    private final String string;

    StateType(String string) { this.string = string; }

    public String getString() { return string; }
}
