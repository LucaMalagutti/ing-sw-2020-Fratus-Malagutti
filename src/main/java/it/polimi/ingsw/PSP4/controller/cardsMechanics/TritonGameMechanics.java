package it.polimi.ingsw.PSP4.controller.cardsMechanics;

import it.polimi.ingsw.PSP4.model.Player;
import it.polimi.ingsw.PSP4.model.Position;

/**
 * Defines the mechanics of the God card Triton
 */
public class TritonGameMechanics extends GodGameMechanics {
    private final static GodType type = GodType.TRITON;         //type which represents the God
    private Position lastPositionMove;

    @Override
    public PathType getPath() {
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

    @Override
    public void move(Player player, Position futurePosition) {
        getComponent().move(player, futurePosition);
        lastPositionMove = futurePosition;
    }
}
