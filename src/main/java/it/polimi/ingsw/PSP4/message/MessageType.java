package it.polimi.ingsw.PSP4.message;

/**
 * Enum of all the possible message types.
 * Used mainly to cast to a specific message type after (de)serialization
 */
public enum MessageType {
    ERROR,
    ASSIGN_GOD,
    CHANGE_WORKER,
    CHOOSE_ALLOWED_GODS,
    CHOOSE_POSITION,
    CHOOSE_STARTING_PLAYER,
    CHOOSE_WORKER,
    REMOVE_PLAYER,
    SKIP_STATE,
    FIRST_WORKER_PLACEMENT,
    WAIT
}
