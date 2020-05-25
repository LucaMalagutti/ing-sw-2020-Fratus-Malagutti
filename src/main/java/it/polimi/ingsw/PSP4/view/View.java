package it.polimi.ingsw.PSP4.view;

import it.polimi.ingsw.PSP4.message.requests.Request;
import it.polimi.ingsw.PSP4.message.responses.Response;
import it.polimi.ingsw.PSP4.model.Player;
import it.polimi.ingsw.PSP4.observer.Observable;
import it.polimi.ingsw.PSP4.observer.Observer;

public abstract class View implements Observable<Response>, Observer<Request> {
    private final Player player;

    protected View (Player player) {this.player = player;}

    protected Player getPlayer() {return this.player;}

    void handleMove(Response response) {
        notifyObservers(response);
    }
}
