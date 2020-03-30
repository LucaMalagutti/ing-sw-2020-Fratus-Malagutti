package it.polimi.ingsw.PSP4.controller;

import it.polimi.ingsw.PSP4.model.Position;
import it.polimi.ingsw.PSP4.model.Worker;

import java.util.ArrayList;

/** Base class for the God card Decorator
 */
abstract public class GameMechanics {
    /** Modifies available movement positions based on the card effect
     * @param worker selected worker for the movement
     * @param callNum number of movements during this turn
     * @return ArrayList containing positions where this card allows the player to move the selected worker
     */
    abstract public ArrayList<Position> getMovePositions(Worker worker, int callNum);

    /** Modifies available building position based on the card effect
     * @param worker selected worker for the build
     * @param callNum number of builds during this turn
     * @return ArrayList containing positions where this card allows the player to move the selected worker
     */
    abstract public ArrayList<Position> getBuildPositions(Worker worker, int callNum);

    /** Adds a card's special winning condition to the basic one
     * @param worker worker that moved in this turn
     * @return boolean indicating whether the game has been won
     */
    abstract public boolean checkWinCondition(Worker worker);
}
