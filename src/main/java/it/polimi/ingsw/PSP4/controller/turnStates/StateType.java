package it.polimi.ingsw.PSP4.controller.turnStates;

import it.polimi.ingsw.PSP4.message.Message;

/**
 * Enum which identifies the type of a state
 */
public enum StateType {
    MOVE("Move"), BUILD("Build"), WAIT("Wait");

    private final String string;

    StateType(String string) { this.string = string; }

    public String getString() { return string; }

    public String getMessage() {
        switch (this) {
            case MOVE:
                return Message.CHOOSE_POSITION_MOVE;
            case BUILD:
                return Message.CHOOSE_POSITION_BUILD;
            default:
                System.out.println("Not a valid state");
                return "";
        }
    }
}
