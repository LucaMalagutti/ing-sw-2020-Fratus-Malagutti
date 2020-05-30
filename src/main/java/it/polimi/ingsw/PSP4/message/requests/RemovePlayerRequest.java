package it.polimi.ingsw.PSP4.message.requests;

import it.polimi.ingsw.PSP4.message.ErrorMessage;
import it.polimi.ingsw.PSP4.message.Message;
import it.polimi.ingsw.PSP4.message.MessageType;
import it.polimi.ingsw.PSP4.model.GameState;

import java.text.MessageFormat;

/**
 * Message to inform that (at least) one player is out of the game
 * Used both to state the winner and to inform that someone has been eliminated
 */
public class RemovePlayerRequest extends Request {
    private static final long serialVersionUID = -517220436172077856L;
    private static final MessageType staticType = MessageType.REMOVE_PLAYER;

    private final String targetPlayer;              //username of the targetPlayer
    private final boolean victory;                  //states if the game has been won
    private String customMessagePlayer = "";        //username of the player receiving the message

    public String getTargetPlayer() { return targetPlayer; }
    public void setCustomMessagePlayer(String customMessagePlayer) {this.customMessagePlayer = customMessagePlayer;}
    public boolean isVictory() { return victory; }

    /**
     * Constructor of the class RemovePlayerRequest
     * @param targetPlayer username of the targetPlayer
     * @param message message from the sender
     * @param victory if true targetPlayer is the winner of the game, otherwise is out of the game
     */
    public RemovePlayerRequest(String targetPlayer, String message, boolean victory) {
        super("@", GameState.getSerializedInstance(), message, staticType);
        this.targetPlayer = targetPlayer;
        this.victory = victory;
    }

    @Override
    public Message validateResponse(String stringMessage) {
        return new ErrorMessage(getPlayer(), Message.NOT_YOUR_TURN);
    }

    public String getCustomMessage() {
        if(isVictory()) {
            if(customMessagePlayer.equals(targetPlayer))
                return MessageFormat.format(getMessage(), "A player") + MessageFormat.format(Message.VICTORY_WINNER, targetPlayer);
            return MessageFormat.format(getMessage(), customMessagePlayer) + MessageFormat.format(Message.VICTORY_LOSER, targetPlayer);
        } else {
            if (targetPlayer.equals("@")) {
                return Message.CLIENT_EXIT_DURING_GAME;
            } else if(customMessagePlayer.equals(targetPlayer)) {
                return MessageFormat.format(getMessage(), customMessagePlayer) + "\n" + MessageFormat.format(Message.DEFEAT_LOSER, targetPlayer);
            } else {
                return MessageFormat.format(getMessage(), targetPlayer) + "\n" + MessageFormat.format(Message.DEFEAT_ENEMY, targetPlayer);
            }
        }
    }

    @Override
    public String toString() { return getCustomMessage(); }
}
