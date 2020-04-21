package it.polimi.ingsw.PSP4.controller;

import it.polimi.ingsw.PSP4.controller.cardsMechanics.GameMechanics;
import it.polimi.ingsw.PSP4.controller.cardsMechanics.GodType;
import it.polimi.ingsw.PSP4.message.*;
import it.polimi.ingsw.PSP4.model.GameState;
import it.polimi.ingsw.PSP4.observer.Observer;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller base class. Observes its (virtual) view
 */
public class Controller implements Observer<Message> {
    /**
     * At the view's notifyObservers(), updates the model depending on the message content
     * @param message message sent by the view
     */
    @Override
    public void update(Message message) {

    }
}
