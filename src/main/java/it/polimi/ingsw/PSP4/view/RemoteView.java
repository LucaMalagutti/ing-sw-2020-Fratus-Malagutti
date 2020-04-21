package it.polimi.ingsw.PSP4.view;

import it.polimi.ingsw.PSP4.controller.cardsMechanics.GodType;
import it.polimi.ingsw.PSP4.message.*;
import it.polimi.ingsw.PSP4.model.GameState;
import it.polimi.ingsw.PSP4.model.Player;
import it.polimi.ingsw.PSP4.observer.Observer;
import it.polimi.ingsw.PSP4.server.SocketClientConnection;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RemoteView extends View {
    private final ArrayList<Observer<Message>> observers = new ArrayList<>();
    private final SocketClientConnection clientConnection;

    private MessageType expectedMessage;

    private class MessageReceiver implements Observer<String> {
        @Override
        public void update(String stringMessage) {
            if (expectedMessage == MessageType.WAIT_MESSAGE) {
                reportError("There is a time and place for everything, but not now.");
            }
            else if (expectedMessage == MessageType.CHOOSE_ALLOWED_GODS) {
                handleAllowedGodsMessage(stringMessage);
            }
            else if (expectedMessage == MessageType.ASSIGN_GOD) {
                handleAssignGodsMessage(stringMessage);
            }
            else if (expectedMessage == MessageType.CHOOSE_STARTING_PLAYER_MESSAGE) {
                handleChooseStartingPlayerMessage(stringMessage);
            }
        }

        private void handleAllowedGodsMessage(String stringMessage) {
            List<String> implementedGodList = GodType.getImplementedGodsList();
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

        private void handleAssignGodsMessage(String stringMessage) {
            String chosenGod = stringMessage.toUpperCase().replaceAll("\\s","");
            List<String> allowedGods = GameState.getInstance().getAllowedGods().stream().map(Enum::toString).collect(Collectors.toList());
            if (allowedGods.contains(chosenGod)) {
                allowedGods.remove(chosenGod);
                handleMove(new AssignGodMessage(GameState.getInstance().getCurrPlayer().getUsername(),"", allowedGods, chosenGod));
            }
            else if (allowedGods.size() == 1) {
                String lastGod = allowedGods.get(0);
                allowedGods.remove(lastGod);
                handleMove(new AssignGodMessage(GameState.getInstance().getCurrPlayer().getUsername(),"", allowedGods, lastGod));
            }
            else {
                reportError("Not a valid god name. Try again.");
            }
        }

        private void handleChooseStartingPlayerMessage(String stringMessage) {
            if (GameState.getInstance().getPlayers().stream().map(Player::getUsername).collect(Collectors.toList()).contains(stringMessage)) {
                List<String> chosenPlayer = new ArrayList<>();
                chosenPlayer.add(stringMessage);
                handleMove(new ChooseStartingPlayerMessage(GameState.getInstance().getCurrPlayer().getUsername(),chosenPlayer));
            }
            else {
                reportError(stringMessage+" is not a valid player name. Try again.");
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
        else {
            expectedMessage = MessageType.WAIT_MESSAGE;
            clientConnection.asyncSend(new WaitMessage(getPlayer().getUsername(),message.getPlayer()));
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
