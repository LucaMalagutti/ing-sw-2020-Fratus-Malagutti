package it.polimi.ingsw.PSP4.message.responses;

import it.polimi.ingsw.PSP4.controller.cardsMechanics.GodType;
import it.polimi.ingsw.PSP4.message.MessageType;
import it.polimi.ingsw.PSP4.model.GameState;

import java.util.List;
import java.util.stream.Collectors;


/**
 * Message to tell the controller the gods allowed for this game
 */
public class ChooseAllowedGodsResponse extends Response {
    private static final long serialVersionUID = 932844113254266439L;
    private static final MessageType staticType = MessageType.CHOOSE_ALLOWED_GODS;

    private final List<String> selectedGods;            //List of gods selected by the player

    /**
     * Constructor of the class ChooseAllowedGodsResponse
     * @param player username of the sender
     * @param selectedGods list of gods selected by the player
     */
    public ChooseAllowedGodsResponse(String player, List<String> selectedGods) {
        super(player, "", staticType);
        selectedGods.replaceAll(String::toUpperCase);
        this.selectedGods = selectedGods;
    }

    @Override
    public void handle() {
        List<GodType> gods = this.selectedGods.stream().map(GodType::valueOf).collect(Collectors.toList());
        GameState.getInstance().setAllowedGods(gods);
        GameState.getInstance().assignGod();
    }
}
