package it.polimi.ingsw.PSP4.controller.cardsMechanics;

import it.polimi.ingsw.PSP4.model.GameState;
import it.polimi.ingsw.PSP4.model.Player;
import it.polimi.ingsw.PSP4.model.Position;
import it.polimi.ingsw.PSP4.model.Worker;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Defines the mechanics of the God card Limus
 */
public class LimusGameMechanics extends GodGameMechanics {
    private static final GodType type = GodType.LIMUS;             //type which represents the God

    /**
     * Constructor of the class LimusGameMechanics
     * @param component reference to the game mechanics to decorate
     */
    public LimusGameMechanics(GameMechanics component) { super(type, component); }

    /**
     * Wraps each enemy at the beginning of the game
     */
    @Override
    public void setupMechanics(Player player) {
        if(!isEvil())
            GameState.getInstance().wrapPlayers(player);
        getComponent().setupMechanics(player);
    }

    /**
     * If it is evil the player cannot build on spaces neighboring a Limus worker, except to build a dome
     */
    @Override
    public ArrayList<Position> getBuildPositions(Player player, int callNum) {
        ArrayList<Position> componentValid = getComponent().getBuildPositions(player, callNum);
        if (isEvil()) {
            ArrayList<Position> toRemove = new ArrayList<>();
            GameState gs = GameState.getInstance();
            Player limus = null;
            for (Player pl: gs.getPlayers()) {
                if (pl.getMechanics().getType() == GodType.LIMUS) {
                    limus = pl;
                }
            }
            if (limus != null) {
                for (Worker w: limus.getWorkers()) {
                    toRemove.addAll(w.getCurrPosition().getNeighbour().stream().filter(p->p.getHeight()!=3).collect(Collectors.toCollection(ArrayList::new)));
                }
            }
            componentValid.removeAll(toRemove);
        }

        return componentValid;
    }
}