package it.polimi.ingsw.PSP4.controller;

import it.polimi.ingsw.PSP4.message.Message;
import it.polimi.ingsw.PSP4.message.responses.Response;
import it.polimi.ingsw.PSP4.observer.Observer;

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
        //TODO: handle message instanceof Request
        if(message instanceof Response)
            ((Response) message).handle();
        else
            System.out.println("Received Request");
    }
}
