package it.polimi.ingsw.PSP4.message;

public class WaitMessage extends Message{
    private static final long serialVersionUID = 6863047056948282579L;
    private final static MessageType staticType = MessageType.WAIT_MESSAGE;

    public String playingPlayer;

    public WaitMessage(String player, String playingPlayer) {
        super(player, "\nWait for "+playingPlayer+" to finish his turn\n");
        this.playingPlayer = playingPlayer;
    }

    @Override
    public String toString() {
        return getMessage();
    }
}
