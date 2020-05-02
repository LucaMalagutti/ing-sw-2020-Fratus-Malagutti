package it.polimi.ingsw.PSP4.view;

import it.polimi.ingsw.PSP4.message.MessageType;
import it.polimi.ingsw.PSP4.message.requests.RemovePlayerRequest;
import it.polimi.ingsw.PSP4.message.requests.Request;
import it.polimi.ingsw.PSP4.message.requests.WaitRequest;
import it.polimi.ingsw.PSP4.message.responses.Response;
import it.polimi.ingsw.PSP4.model.Player;
import it.polimi.ingsw.PSP4.observer.Observer;
import it.polimi.ingsw.PSP4.server.SocketClientConnection;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class RemoteView extends View {
    private final ArrayList<Observer<Response>> observers = new ArrayList<>();
    private final SocketClientConnection clientConnection;

    private final BlockingQueue<Request> requests;

    private class MessageReceiver implements Observer<Response> {
        @Override
        //response : client response
        public void update(Response response) {
            if (requests.peek() == null) {
                return;
            }
            requests.poll();
            handleMove(response);
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
    public void update(Request request) {
        if (request.getPlayer().equals("@") || request.getPlayer().equals(getPlayer().getUsername())) {
            if(request.getType() == MessageType.REMOVE_PLAYER) {
                //Special case, could close the connection
                RemovePlayerRequest rpr = (RemovePlayerRequest) request;
                String player = getPlayer().getUsername();
                if (!rpr.isVictory() && rpr.getTargetPlayer().equals(player))
                    clientConnection.closeConnection(rpr.getCustomMessage(player), false);
                else if (rpr.isVictory() || rpr.getTargetPlayer().equals("@")) {
                    clientConnection.closeConnection(rpr.getCustomMessage(player), true);
                }
            }
            clientConnection.asyncSend(request);
            if(request.needsResponse()) {
                try {
                    requests.put(request);
                } catch (Exception e) {
                    //TODO: handle exceptions
                }
            }
        } else {
            clientConnection.asyncSend(new WaitRequest(getPlayer().getUsername(), request.getBoard(), request.getPlayer()));
        }
    }

    @Override
    public void notifyObservers(Response response) {
        synchronized (observers) {
            for (Observer<Response> obs: observers) {
                obs.update(response);
            }
        }
    }

    @Override
    public void addObserver(Observer<Response> o) {
        synchronized (observers) {
            observers.add(o);
        }
    }

    @Override
    public void removeObserver(Observer<Response> o) {
        synchronized (observers) {
            observers.remove(o);
        }
    }
}
