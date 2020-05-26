package it.polimi.ingsw.PSP4.controller.cardsMechanics;

import it.polimi.ingsw.PSP4.model.Player;
import it.polimi.ingsw.PSP4.model.Position;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Defines the mechanics of the God card Hestia
 */
public class HestiaGameMechanics extends GodGameMechanics {
    private static final GodType type = GodType.HESTIA;            //type which represents the God

    /**
     * Constructor of the class HestiaGameMechanics
     * @param component reference to the game mechanics to decorate
     */
    public HestiaGameMechanics(GameMechanics component) {
        super(type, component);
    }

    /**
     * Adds possibility to build a second time, except on cells on the edges of the board
     */
    @Override
    public ArrayList<Position> getBuildPositions(Player player, int callNum) {
        ArrayList<Position> componentValid = super.getComponent().getBuildPositions(player, callNum);

        if (callNum == 2) {
            ArrayList<Position> sidePositions = componentValid.stream().filter(Position::isPerimeter).collect(Collectors.toCollection(ArrayList::new));
            componentValid.removeAll(sidePositions);
        }
        return componentValid;
    }
}
