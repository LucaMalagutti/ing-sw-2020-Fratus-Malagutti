package it.polimi.ingsw.PSP4.controller.cardsMechanics;

import it.polimi.ingsw.PSP4.model.Player;
import it.polimi.ingsw.PSP4.model.Position;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Defines the mechanics of the God card Athena, when it affects an enemy
 */
public class AthenaEnemyGameMechanics extends GodGameMechanics {
    //getter and setter
    @Override
    public GodType getType() { return getComponent().getType(); }

    @Override
    public String getName() { return "Athena_Enemy"; }

    @Override
    public PathType getPath() { return getComponent().getPath(); }

    /**
     * Constructor of the class AthenaEnemyGameMechanics
     * @param component reference to the game mechanics to decorate
     */
    protected AthenaEnemyGameMechanics(GameMechanics component) { super(component); }

    /**
     * Removes the possibility of moving up
     */
    @Override
    public ArrayList<Position> getMovePositions(Player player, int callNum) {
        ArrayList<Position> componentValid = super.getComponent().getMovePositions(player, callNum);
        return componentValid.stream().filter(position -> position.getHeight() <= player.getCurrWorker().getCurrPosition().getHeight()).collect(Collectors.toCollection(ArrayList::new));
    }
}
