package it.polimi.ingsw.PSP4.controller.turnStates;

/**
 * Enum which identifies the operation that is being performed by a state
 */
public enum StateStep {
    INITIALIZE,
    WAIT_RESPONSE,
    PERFORM_ACTION,
    CHANGE_WORKER,
    SKIP_STATE
}
