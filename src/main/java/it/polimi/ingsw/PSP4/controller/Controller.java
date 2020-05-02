package it.polimi.ingsw.PSP4.controller;

import it.polimi.ingsw.PSP4.message.requests.Request;
import it.polimi.ingsw.PSP4.message.responses.Response;
import it.polimi.ingsw.PSP4.observer.Observer;

/**
 * Controller base class. Observes all the (virtual) views
 */
public class Controller implements Observer<Response> {
    /**
     * At the view's notifyObservers(), updates the model depending on the message content
     * @param response Response sent by the view
     */
    @Override
    public void update(Response response) {
        response.handle();
    }
}
