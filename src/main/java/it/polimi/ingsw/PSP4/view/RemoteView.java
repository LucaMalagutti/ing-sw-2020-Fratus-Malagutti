package it.polimi.ingsw.PSP4.view;

import it.polimi.ingsw.PSP4.controller.cardsMechanics.GodType;
import it.polimi.ingsw.PSP4.message.*;
import it.polimi.ingsw.PSP4.message.requests.RemovePlayerRequest;
import it.polimi.ingsw.PSP4.message.requests.WaitRequest;
import it.polimi.ingsw.PSP4.message.responses.*;
import it.polimi.ingsw.PSP4.model.GameState;
import it.polimi.ingsw.PSP4.model.Player;
import it.polimi.ingsw.PSP4.model.serializable.SerializablePlayer;
import it.polimi.ingsw.PSP4.model.serializable.SerializablePosition;
import it.polimi.ingsw.PSP4.observer.Observer;
import it.polimi.ingsw.PSP4.server.SocketClientConnection;

import java.lang.management.PlatformLoggingMXBean;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RemoteView extends View {
    private final ArrayList<Observer<Message>> observers = new ArrayList<>();
    private final SocketClientConnection clientConnection;

    private MessageType expectedMessage;

    private class MessageReceiver implements Observer<String> {
        @Override
        //stringMessage : client response
        public void update(String stringMessage) {
            if (expectedMessage == MessageType.WAIT) {
                reportError(Message.NOT_YOUR_TURN);
            }
            else if (expectedMessage == MessageType.ASSIGN_GOD) {
                handleAssignGods(stringMessage);
            }
            else if (expectedMessage == MessageType.CHOOSE_ALLOWED_GODS) {
                handleChooseAllowedGods(stringMessage);
            }
            else if (expectedMessage == MessageType.CHOOSE_POSITION) {
                handleChoosePosition(stringMessage);
            }
            else if (expectedMessage == MessageType.CHOOSE_STARTING_PLAYER) {
                handleChooseStartingPlayer(stringMessage);
            }
        }

        private void handleAssignGods(String stringMessage) {
            String chosenGod = stringMessage.toUpperCase().replaceAll("\\s","");
            List<String> allowedGods = GameState.getInstance().getAllowedGods().stream().map(Enum::toString).collect(Collectors.toList());
            if (allowedGods.contains(chosenGod)) {
                allowedGods.remove(chosenGod);
                handleMove(new AssignGodResponse(GameState.getInstance().getCurrPlayer().getUsername(), allowedGods, chosenGod));
            }
            else if (allowedGods.size() == 1) {
                String lastGod = allowedGods.get(0);
                allowedGods.remove(lastGod);
                handleMove(new AssignGodResponse(GameState.getInstance().getCurrPlayer().getUsername(), allowedGods, lastGod));
            }
            else {
                reportError(Message.NOT_VALID_GOD_NAME);
            }
        }

        private void handleChooseAllowedGods(String stringMessage) {
            List<String> implementedGodList = GodType.getImplementedGodsList();
            String[] godNames = stringMessage.split(" ");
            if (godNames.length == GameState.getInstance().getNumPlayer()) {
                List<String> selectableGods = new ArrayList<>();
                for (String godName: godNames) {
                    if (!implementedGodList.contains(godName.toUpperCase())) {
                        reportError(Message.NOT_VALID_GOD_LIST);
                        return;
                    }
                    selectableGods.add(godName.toUpperCase());
                }
                handleMove(new ChooseAllowedGodsResponse(GameState.getInstance().getCurrPlayer().getUsername(), selectableGods));
            }
            else {
                reportError(Message.NOT_VALID_GOD_LIST);
            }
        }

        private void handleChoosePosition(String stringMessage) {
            stringMessage = stringMessage.toUpperCase().trim();
            Player currentPlayer = GameState.getInstance().getCurrPlayer();
            if(stringMessage.equals("CHANGE") && currentPlayer.getState().canChangeWorker()) {
                handleMove(new ChangeWorkerResponse(currentPlayer.getUsername()));
                return;
            }
            if(stringMessage.equals("SKIP") && currentPlayer.getState().canBeSkipped()) {
                handleMove(new SkipStateResponse(currentPlayer.getUsername()));
                return;
            }
            String[] coordinates = stringMessage.split(",");
            int row, col;
            try {
                row = Integer.parseInt(coordinates[0]);
                col = Integer.parseInt(coordinates[1]);
            } catch (NumberFormatException e) {
                reportError(MessageFormat.format(Message.NOT_VALID_POSITION, stringMessage));
                return;
            }
            List<SerializablePosition> options = currentPlayer.getState().getOptions();
            List<SerializablePosition> selected = options.stream().filter(p -> p.getRow() == row && p.getCol() == col).collect(Collectors.toList());
            if(selected.size() == 1) {
                handleMove(new ChoosePositionResponse(currentPlayer.getUsername(), selected.get(0)));
            } else {
                reportError(MessageFormat.format(Message.NOT_VALID_POSITION, stringMessage));
            }
        }

        private void handleChooseStartingPlayer(String stringMessage) {
            if (GameState.getInstance().getPlayers().stream().map(Player::getUsername).collect(Collectors.toList()).contains(stringMessage)) {
                handleMove(new ChooseStartingPlayerResponse(GameState.getInstance().getCurrPlayer().getUsername(), stringMessage));
            }
            else {
                reportError(MessageFormat.format(Message.NOT_VALID_USERNAME, stringMessage));
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
    //message : server request
    public void update(Message message) {
        if (message.getPlayer().equals("all") || message.getPlayer().equals(getPlayer().getUsername())) {
            if(message.getType() == MessageType.REMOVE_PLAYER) {
                //Special case, could close the connection
                RemovePlayerRequest rpr = (RemovePlayerRequest) message;
                String player = getPlayer().getUsername();
                if(rpr.isVictory() || rpr.getTargetPlayer().equals(player))
                    clientConnection.closeConnection(rpr.getCustomMessage(player));
                else
                    clientConnection.asyncSend(message);
            } else {
                clientConnection.asyncSend(message);
                expectedMessage = message.getType();
            }
        }
        else {
            expectedMessage = MessageType.WAIT;
            clientConnection.asyncSend(new WaitRequest(getPlayer().getUsername(),message.getPlayer()));
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
