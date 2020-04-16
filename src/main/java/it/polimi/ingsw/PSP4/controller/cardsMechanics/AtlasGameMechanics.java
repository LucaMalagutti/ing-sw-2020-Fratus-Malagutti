package it.polimi.ingsw.PSP4.controller.cardsMechanics;

import it.polimi.ingsw.PSP4.controller.turnStates.PathType;
import it.polimi.ingsw.PSP4.model.Player;
import it.polimi.ingsw.PSP4.model.Position;

/**
 * Defines the mechanics of the God card Atlas
 */
public class AtlasGameMechanics extends GodGameMechanics {
    /**
     * Constructor of the class AtlasGameMechanics
     * @param component reference to the game mechanics to decorate
     */
    public AtlasGameMechanics(GameMechanics component) {
        super(component, "Atlas", PathType.DEFAULT);
    }

    /**
     * Ask the player if want to force the construction of a dome (only if height < 3)
     * @param position position in which has to build
     * @return false if height is 3 (no choices) or the player wants to build a block
     */
    public boolean forceDome(Position position) {
        if(position.getHeight() == 3)
            return false;
        //To be implemented
        return true;
    }

    @Override
    public void build(Player player, Position futurePosition) {
        if(futurePosition == null){
            //exception
        }
        else if(futurePosition.getWorker() != null){
            //exception
        }
        else if(futurePosition.hasDome()){
            //exception
        }
        else {
            player.lockWorker();

            if (forceDome(futurePosition))
                futurePosition.setDome(true);
            else
                futurePosition.increaseHeight();
        }
    }
}
