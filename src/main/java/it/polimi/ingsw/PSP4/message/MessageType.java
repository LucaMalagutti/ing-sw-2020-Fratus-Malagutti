package it.polimi.ingsw.PSP4.message;

import it.polimi.ingsw.PSP4.controller.cardsMechanics.GameMechanics;
import it.polimi.ingsw.PSP4.controller.cardsMechanics.GodType;
import it.polimi.ingsw.PSP4.model.GameState;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Enum of all the possible message types.
 * Used mainly to cast to a specific message type after (de)serialization
 */
public enum MessageType {
    DEFAULT,
    WAIT_MESSAGE,
    CHOOSE_ALLOWED_GODS,
    ASSIGN_GOD,
    CHOOSE_STARTING_PLAYER_MESSAGE;

    public void handleResponse(Message message) {
        switch (this) {
            case CHOOSE_ALLOWED_GODS:
                ChooseAllowedGodsMessage m = (ChooseAllowedGodsMessage) message;
                List<GodType> gods = m.getSelectableGods().stream().map(GodType::valueOf).collect(Collectors.toList());
                GameState.getInstance().setAllowedGods(gods);
                GameState.getInstance().assignGod();
                break;
            case ASSIGN_GOD:
                AssignGodMessage m2 = (AssignGodMessage) message;
                List<GodType> gods2 = m2.getAllowedGods().stream().map(GodType::valueOf).collect(Collectors.toList());
                GameMechanics selectedGodMechanics = GodType.valueOf(m2.getChosenGod()).getGameMechanics(GameState.getInstance().getPlayers());
                GameState.getInstance().getCurrPlayer().setMechanics(selectedGodMechanics);
                GameState.getInstance().setAllowedGods(gods2);
                if (GameState.getInstance().getAllowedGods().size() > 0) {
                    GameState.getInstance().assignGod();
                }
                else {
                    GameState.getInstance().chooseStartingPlayer();
                }
                break;
            case CHOOSE_STARTING_PLAYER_MESSAGE:
                ChooseStartingPlayerMessage m3 = (ChooseStartingPlayerMessage) message;
                String chosenPlayer = m3.getPlayerNames().get(0);
                //TODO handle null exception in player search
                GameState.getInstance().setCurrPlayer(GameState.getInstance().getPlayerFromUsername(chosenPlayer));
                GameState.getInstance().placeWorkers();
                break;
        }
    }
}
