package it.polimi.ingsw.PSP4.controller;

import it.polimi.ingsw.PSP4.model.Player;
import it.polimi.ingsw.PSP4.model.Position;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Defines the mechanics of the God card Prometheus
 */
public class PrometheusGameMechanics extends GodGameMechanics {
    private Boolean canMoveUp;          //true if the worker can move up

    //getters and setters
    public Boolean getCanMoveUp() { return canMoveUp; }
    public void setCanMoveUp(Boolean canMoveUp) {
        this.canMoveUp = canMoveUp;
    }

    /**
     * Constructor of the class PrometheusGameMechanics
     * @param component reference to the game mechanics to decorate
     */
    public PrometheusGameMechanics(GameMechanics component) {
        super(component, "Prometheus", PathType.EARLY_BUILD);
        canMoveUp = true;
    }

    /**
     * Doesn't allow the player to move up if he has also built before moving
     */
    @Override
    public ArrayList<Position> getMovePositions(Player player, int callNum) {
        if (callNum > 1) {
            return new ArrayList<>();
        }
        if (!getCanMoveUp()) {
            ArrayList<Position> componentValid = super.getComponent().getMovePositions(player, callNum);
            return componentValid.stream().filter(position -> position.getHeight() <= player.getCurrWorker().getCurrPosition().getHeight()).collect(Collectors.toCollection(ArrayList::new));
        }
        else {
            return super.getComponent().getMovePositions(player, callNum);
        }
    }
}
