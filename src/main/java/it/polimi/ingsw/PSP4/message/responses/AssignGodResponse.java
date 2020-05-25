package it.polimi.ingsw.PSP4.message.responses;

import it.polimi.ingsw.PSP4.controller.cardsMechanics.GameMechanics;
import it.polimi.ingsw.PSP4.controller.cardsMechanics.GodType;
import it.polimi.ingsw.PSP4.message.MessageType;
import it.polimi.ingsw.PSP4.model.GameState;

import java.util.List;
import java.util.stream.Collectors;

public class AssignGodResponse extends Response {
    private static final long serialVersionUID = -8207972117872562372L;
    private final static MessageType staticType = MessageType.ASSIGN_GOD;

    private final List<String> allowedGods;         //List of the allowed god names to choose from
    private final String selectedGod;               //Name of the god picked by the player


    /**
     * Constructor of the class AssignGodRequest
     * @param player username of the sender
     * @param allowedGods list of the allowed god names to choose from
     * @param selectedGod name of the god picked by the player
     */
    public AssignGodResponse(String player, List<String> allowedGods, String selectedGod) {
        super(player, "", staticType);
        allowedGods.replaceAll(String::toUpperCase);
        this.allowedGods = allowedGods;
        this.selectedGod = selectedGod.toUpperCase();
    }

    @Override
    public void handle() {
        List<GodType> gods = allowedGods.stream().map(GodType::valueOf).collect(Collectors.toList());
        GameMechanics selectedGodMechanics = GodType.valueOf(selectedGod).getGameMechanics();
        GameState.getInstance().getCurrPlayer().setMechanics(selectedGodMechanics);
        GameState.getInstance().setAllowedGods(gods);
        if (GameState.getInstance().getAllowedGods().size() > 0) {
            GameState.getInstance().assignGod();
        } else {
            GameState.getInstance().chooseStartingPlayer();
        }
    }
}
