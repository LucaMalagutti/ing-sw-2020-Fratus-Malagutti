package it.polimi.ingsw.PSP4.controller;

import it.polimi.ingsw.PSP4.model.Position;
import it.polimi.ingsw.PSP4.model.Worker;

import java.util.ArrayList;
import java.util.stream.Collectors;

/** Defines the mechanics of the God card Prometheus
 */
public class PrometheusGameMechanics extends GodGameMechanics {
    private Boolean canMoveUp;

    public Boolean getCanMoveUp() { return canMoveUp; }

    public void setCanMoveUp(Boolean canMoveUp) {
        this.canMoveUp = canMoveUp;
    }

    public PrometheusGameMechanics(GameMechanics component) {
        super(component);
        canMoveUp = true;
    }

    /** Doesn't allow the player to move up if he has also built before moving
     */
    @Override
    public ArrayList<Position> getMovePositions(Worker worker, int callNum) {
        if (callNum > 1) {
            return new ArrayList<>();
        }
        if (!canMoveUp) {
            ArrayList<Position> componentValid = super.getComponent().getMovePositions(worker, callNum);
            return componentValid.stream().filter(position -> position.getHeight() <= worker.getCurrPosition().getHeight()).collect(Collectors.toCollection(ArrayList::new));
        }
        else {
            return super.getComponent().getMovePositions(worker, callNum);
        }
    }
}
