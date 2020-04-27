package it.polimi.ingsw.PSP4.view;

import it.polimi.ingsw.PSP4.message.Message;
import it.polimi.ingsw.PSP4.message.MessageType;
import it.polimi.ingsw.PSP4.message.requests.RemovePlayerRequest;
import it.polimi.ingsw.PSP4.message.requests.Request;
import it.polimi.ingsw.PSP4.message.requests.WaitRequest;
import it.polimi.ingsw.PSP4.model.Player;
import it.polimi.ingsw.PSP4.observer.Observer;
import it.polimi.ingsw.PSP4.server.SocketClientConnection;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class RemoteView extends View {
    private final ArrayList<Observer<Message>> observers = new ArrayList<>();
    private final SocketClientConnection clientConnection;

    private final BlockingQueue<Request> requests;

    private class MessageReceiver implements Observer<String> {
        @Override
        //stringMessage : client response
        public void update(String stringMessage) {
            if (requests.peek() == null) {
                reportError(Message.NOT_YOUR_TURN);
                return;
            }
            Message response = requests.peek().validateResponse(stringMessage);
            if(response.getType() == MessageType.ERROR)
                reportError(response.getMessage());
            else {
                requests.poll();
                handleMove(response);
            }
        }
    }

    public RemoteView(Player player, SocketClientConnection c) {
        super(player);
        this.clientConnection = c;
        this.requests = new LinkedBlockingQueue<>();
        c.addObserver(new MessageReceiver());
    }

    public void reportError(String message) { clientConnection.asyncSend(message); }

    @Override
    //message : server request
    public void update(Message message) {
        if(!(message instanceof Request)) {
            //TODO: handle exception
            return;
        }
        if (message.getPlayer().equals("@") || message.getPlayer().equals(getPlayer().getUsername())) {
            if(message.getType() == MessageType.REMOVE_PLAYER) {
                //Special case, could close the connection
                RemovePlayerRequest rpr = (RemovePlayerRequest) message;
                String player = getPlayer().getUsername();
                if (!rpr.isVictory() && rpr.getTargetPlayer().equals(player))
                    clientConnection.closeConnection(rpr.getCustomMessage(player), false);
                else if (rpr.isVictory() || rpr.getTargetPlayer().equals("@")) {
                    clientConnection.closeConnection(rpr.getCustomMessage(player), true);
                }
            }
            clientConnection.asyncSend(message);
            if(((Request) message).needsResponse()) {
                try {
                    requests.put((Request) message);
                } catch (Exception e) {
                    //TODO: handle exceptions
                }
            }
        } else {
            clientConnection.asyncSend(new WaitRequest(getPlayer().getUsername(), ((Request) message).getBoard(), message.getPlayer()));
        }
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
