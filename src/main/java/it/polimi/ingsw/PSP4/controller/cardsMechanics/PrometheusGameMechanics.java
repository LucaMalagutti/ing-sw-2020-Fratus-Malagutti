package it.polimi.ingsw.PSP4.controller.cardsMechanics;

import it.polimi.ingsw.PSP4.model.Player;
import it.polimi.ingsw.PSP4.model.Position;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Defines the mechanics of the God card Prometheus
 */
public class PrometheusGameMechanics extends GodGameMechanics {
    private static final GodType type = GodType.PROMETHEUS;         //type which represents the God

    /**
     * Constructor of the class PrometheusGameMechanics
     * @param component reference to the game mechanics to decorate
     */
    public PrometheusGameMechanics(GameMechanics component) {
        super(type, component);
    }

    /**
     * Doesn't allow the player to move up if he has also built before moving
     */
    @Override
    public ArrayList<Position> getMovePositions(Player player, int callNum) {
        ArrayList<Position> componentValid = getComponent().getMovePositions(player, callNum);

        //It should never be evil, in such case at least it won't change the behaviour
        //Prometheus can move up IFF he hasn't built before, i.e. if his worker is not locked
        if (!isEvil() && player.isWorkerLocked())
            return componentValid.stream().filter(position -> position.getHeight() <= player.getCurrWorker().getCurrPosition().getHeight()).collect(Collectors.toCollection(ArrayList::new));
        return componentValid;
    }
}
