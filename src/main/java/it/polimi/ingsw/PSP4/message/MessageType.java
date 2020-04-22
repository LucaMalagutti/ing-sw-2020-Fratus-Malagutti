package it.polimi.ingsw.PSP4.message;

import it.polimi.ingsw.PSP4.message.responses.*;

/**
 * Enum of all the possible message types.
 * Used mainly to cast to a specific message type after (de)serialization
 */
public enum MessageType {
    DEFAULT,
    ASSIGN_GOD,
    CHANGE_WORKER,
    CHOOSE_ALLOWED_GODS,
    CHOOSE_POSITION,
    CHOOSE_STARTING_PLAYER,
    SKIP_STATE,
    WAIT;

    public void handleResponse(Response response) {
        switch (this) {
            case ASSIGN_GOD:
                AssignGodResponse ag = (AssignGodResponse) response;
                ag.handle();
                break;
            case CHANGE_WORKER:
                ChangeWorkerResponse cw = (ChangeWorkerResponse) response;
                cw.handle();
                break;
            case CHOOSE_ALLOWED_GODS:
                ChooseAllowedGodsResponse cag = (ChooseAllowedGodsResponse) response;
                cag.handle();
                break;
            case CHOOSE_POSITION:
                ChoosePositionResponse cp = (ChoosePositionResponse) response;
                cp.handle();
                break;
            case CHOOSE_STARTING_PLAYER:
                ChooseStartingPlayerResponse csp = (ChooseStartingPlayerResponse) response;
                csp.handle();
                break;
            case SKIP_STATE:
                SkipStateResponse ss = (SkipStateResponse) response;
                ss.handle();
                break;
            default:
                //TODO: handle not possible response
                break;
        }
    }
}
