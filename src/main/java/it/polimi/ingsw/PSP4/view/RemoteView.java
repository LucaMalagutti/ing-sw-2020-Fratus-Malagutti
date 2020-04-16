package it.polimi.ingsw.PSP4.view;

import it.polimi.ingsw.PSP4.message.Message;
import it.polimi.ingsw.PSP4.model.Player;
import it.polimi.ingsw.PSP4.observer.Observer;
import it.polimi.ingsw.PSP4.server.SocketClientConnection;

import java.util.ArrayList;

public class RemoteView extends View {
    private final ArrayList<Observer<Message>> observers = new ArrayList<>();
    private SocketClientConnection clientConnection;

    private class MessageReceiver implements Observer<String> {
        @Override
        public void update(String message) {
            System.out.println("Received: " + message);
            try{
                handleMove();
            }catch(IllegalArgumentException e){
                clientConnection.asyncSend("Error!");
            }
        }
    }

    public RemoteView(Player player, SocketClientConnection c) {
        super(player);
        this.clientConnection = c;
    }

    @Override
    protected void showMessage(Object message) {
        clientConnection.asyncSend(message);
    }

    @Override
    public void update(Message message) {

    }

    @Override
    public void notifyObservers(Message message) {
        synchronized (observers) {
            for (Observer<Message> obs: observers) {
                obs.update(message);
            }
        }
    }

    @Override
    public void addObserver(Observer<Message> o) {
        synchronized (observers) {
            observers.add(o);
        }
    }

    @Override
    public void removeObserver(Observer<Message> o) {
        synchronized (observers) {
            observers.remove(o);
        }
    }
}
