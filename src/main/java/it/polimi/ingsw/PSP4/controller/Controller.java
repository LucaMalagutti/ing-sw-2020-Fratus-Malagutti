package it.polimi.ingsw.PSP4.controller;

import it.polimi.ingsw.PSP4.message.Message;
import it.polimi.ingsw.PSP4.model.GameState;
import it.polimi.ingsw.PSP4.observer.Observer;

public class Controller implements Observer<Message> {
    private final GameState gameState;

    public Controller(GameState gameState) {
        super();
        this.gameState = gameState;
    }

    @Override
    public void update(Message message) {

    }


}
