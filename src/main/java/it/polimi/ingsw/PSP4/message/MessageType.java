package it.polimi.ingsw.PSP4.message;

/**
 * Enum of all the possible message types.
 * Used mainly to cast to a specific message type after (de)serialization
 */
public enum MessageType {
    ASSIGN_GOD,
    CHANGE_WORKER,
    CHOOSE_ALLOWED_GODS,
    CHOOSE_NUM_PLAYERS,
    CHOOSE_POSITION,
    CHOOSE_STARTING_PLAYER,
    CHOOSE_USERNAME,
    CHOOSE_WORKER,
    CONFIRMATION,
    ERROR,
    INFO,
    REMOVE_PLAYER,
    PING,
    SKIP_STATE,
    START_TURN,
    FIRST_WORKER_PLACEMENT,
    WAIT
}
