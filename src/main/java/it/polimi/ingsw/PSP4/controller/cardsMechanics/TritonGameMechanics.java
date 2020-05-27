package it.polimi.ingsw.PSP4.controller.cardsMechanics;

import it.polimi.ingsw.PSP4.model.Player;
import it.polimi.ingsw.PSP4.model.Position;

/**
 * Defines the mechanics of the God card Triton
 */
public class TritonGameMechanics extends GodGameMechanics {
    private final static GodType type = GodType.TRITON;         //type which represents the God
    private Position lastPositionMove;

    /**
     * Changes its path relative to lastPositionMove
     * Each time the path is INFINITE_MOVE lastPositionMove is set to null
     */
    @Override
    public PathType getPath() {
        //It should never be evil, in such case at least it won't change the behaviour
        if(isEvil())
            return getComponent().getPath();

        if(lastPositionMove == null || !lastPositionMove.isPerimeter())
            return PathType.DEFAULT;
        lastPositionMove = null;
        return PathType.INFINITE_MOVE;
    }

    /**
     * Constructor of the class TritonGameMechanics
     * @param component reference to the game mechanics to decorate
     */
    public TritonGameMechanics(GameMechanics component) { super(type, component); }

    /**
     * Sets lastPositionMove parameter
     */
    @Override
    public void move(Player player, Position futurePosition) {
        getComponent().move(player, futurePosition);
        //It should never be evil, in such case at least it won't change the behaviour
        if(!isEvil())
            lastPositionMove = futurePosition;
    }
}
