package it.polimi.ingsw.PSP4.controller;

import it.polimi.ingsw.PSP4.model.Player;
import it.polimi.ingsw.PSP4.model.Position;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Defines the mechanics of the God card Athena, when it affects an enemy
 */
public class AthenaEnemyGameMechanics extends GodGameMechanics {
    /**
     * Constructor of the class AthenaEnemyGameMechanics
     * @param component reference to the game mechanics to decorate
     */
    public AthenaEnemyGameMechanics(GameMechanics component) {
        super(component, "Athena_Enemy", component.getPath());
    }

    /**
     * Removes the possibility of moving up
     */
    @Override
    public ArrayList<Position> getMovePositions(Player player, int callNum) {
        ArrayList<Position> componentValid = super.getComponent().getMovePositions(player, callNum);
        return componentValid.stream().filter(position -> position.getHeight() <= player.getCurrWorker().getCurrPosition().getHeight()).collect(Collectors.toCollection(ArrayList::new));
    }
}
