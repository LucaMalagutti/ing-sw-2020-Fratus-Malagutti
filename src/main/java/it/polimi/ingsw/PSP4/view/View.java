package it.polimi.ingsw.PSP4.view;

import it.polimi.ingsw.PSP4.message.Message;
import it.polimi.ingsw.PSP4.model.Player;
import it.polimi.ingsw.PSP4.observer.Observable;
import it.polimi.ingsw.PSP4.observer.Observer;

public abstract class View implements Observable<Message>, Observer<Message> {
    private Player player;

    protected View (Player player) {this.player = player;}

    protected Player getPlayer() {return this.player;}

    protected abstract void showMessage(Object message);

    void handleMove() {};

    public void reportError(String message) {showMessage(message);}
}
