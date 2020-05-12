package it.polimi.ingsw.PSP4.controller;

import it.polimi.ingsw.PSP4.client.gui.AlertBox;
import it.polimi.ingsw.PSP4.client.gui.FXMLFile;
import it.polimi.ingsw.PSP4.client.gui.GUIClient;
import it.polimi.ingsw.PSP4.message.requests.Request;
import it.polimi.ingsw.PSP4.message.responses.Response;
import it.polimi.ingsw.PSP4.observer.Observer;
import javafx.scene.control.TextField;

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
