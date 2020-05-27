package it.polimi.ingsw.PSP4.controller.cardsMechanics;

import it.polimi.ingsw.PSP4.model.GameState;
import it.polimi.ingsw.PSP4.model.Player;
import it.polimi.ingsw.PSP4.model.Position;
import it.polimi.ingsw.PSP4.model.Worker;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Defines the mechanics of the God card Athena
 */
public class AthenaGameMechanics extends GodGameMechanics {
    private static final GodType type = GodType.ATHENA;             //type which represents the God

    /**
     * Constructor of the class AthenaGameMechanics
     * @param component reference to the game mechanics to decorate
     */
    public AthenaGameMechanics(GameMechanics component) { super(type, component); }

    /**
     * Removes the possibility to move up (only if evil)
     */
    @Override
    public ArrayList<Position> getMovePositions(Player player, int callNum) {
        ArrayList<Position> componentValid = getComponent().getMovePositions(player, callNum);
        if(!isEvil())
            return componentValid;
        return componentValid.stream().filter(position -> position.getHeight() <= player.getCurrWorker().getCurrPosition().getHeight()).collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * If not evil wraps the enemies with an evil version when moves up
     * Otherwise unwraps the enemies
     */
    @Override
    public void move(Player player, Position futurePosition) {
        getComponent().move(player, futurePosition);
        if(isEvil())
            return;
        Worker currWorker = player.getCurrWorker();
        if(currWorker.getCurrPosition().getHeight() > currWorker.getPrevPosition().getHeight())
            GameState.getInstance().wrapPlayers(player);
        else
            GameState.getInstance().unwrapPlayers(player);
    }
}
