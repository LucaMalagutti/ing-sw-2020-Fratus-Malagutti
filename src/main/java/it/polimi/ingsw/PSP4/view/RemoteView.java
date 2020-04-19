package it.polimi.ingsw.PSP4.view;

import it.polimi.ingsw.PSP4.controller.cardsMechanics.GodType;
import it.polimi.ingsw.PSP4.message.ChooseAllowedGodsMessage;
import it.polimi.ingsw.PSP4.message.Message;
import it.polimi.ingsw.PSP4.message.MessageType;
import it.polimi.ingsw.PSP4.model.GameState;
import it.polimi.ingsw.PSP4.model.Player;
import it.polimi.ingsw.PSP4.observer.Observer;
import it.polimi.ingsw.PSP4.server.SocketClientConnection;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RemoteView extends View {
    private final ArrayList<Observer<Message>> observers = new ArrayList<>();
    private final SocketClientConnection clientConnection;

    private MessageType expectedMessage;

    private class MessageReceiver implements Observer<String> {
        @Override
        public void update(String stringMessage) {
            if (expectedMessage == MessageType.CHOOSE_ALLOWED_GODS) {
                handleAllowedGodMessage(stringMessage);
            }
        }

        private void handleAllowedGodMessage(String stringMessage) {
            List<String> implementedGodList = Stream.of(GodType.values()).map(Enum::name).collect(Collectors.toList());
            String[] godNames = stringMessage.split(" ");
            if (godNames.length == GameState.getInstance().getNumPlayer()) {
                List<String> selectableGods = new ArrayList<>();
                for (String godName: godNames) {
                    if (!implementedGodList.contains(godName.toUpperCase())) {
                        reportError("Not a valid god list. Try again with valid god names separated by a single space.");
                        return;
                    }
                    selectableGods.add(godName.toUpperCase());
                }
                handleMove(new ChooseAllowedGodsMessage(GameState.getInstance().getCurrPlayer().getUsername(),"",selectableGods));
            }
            else {
                reportError("Not a valid god list. Try again with valid god names separated by a single space.");
            }
        }
    }

    public RemoteView(Player player, SocketClientConnection c) {
        super(player);
        this.clientConnection = c;
        this.expectedMessage = MessageType.DEFAULT;
        c.addObserver(new MessageReceiver());
    }

    public void reportError(String message) {clientConnection.asyncSend(message);}

    @Override
    public void update(Message message) {
        if (message.getPlayer().equals("all") || message.getPlayer().equals(getPlayer().getUsername())) {
            clientConnection.asyncSend(message);
            expectedMessage = message.getType();
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
