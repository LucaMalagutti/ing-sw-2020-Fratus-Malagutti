package it.polimi.ingsw.PSP4.model.serializable;

import it.polimi.ingsw.PSP4.client.cli.CLIChar;
import it.polimi.ingsw.PSP4.client.cli.Color;
import it.polimi.ingsw.PSP4.model.GameState;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Serializable "light" copy of GameState, contains information to be sent to all the players
 */
public final class SerializableGameState implements Serializable {
    private static final long serialVersionUID = 8734470972752644234L;

    private final List<SerializablePosition> board = new ArrayList<>();     //copy of GameState (straight) board
    private final List<SerializablePlayer> players = new ArrayList<>();     //copy of GameState players
    private final int currPlayerIndex;                                      //index of the current player
    private final int numPlayer;                                            //number of players (2 or 3)
    private final List<SerializablePosition> options;                       //available options to highlight
    private final List<SerializablePosition> match = new ArrayList<>();

    /**
     * Constructor of the class SerializableGameState
     */
    public SerializableGameState() {
        GameState gameState = GameState.getInstance();
        gameState.getFlatBoard().forEach(position -> this.board.add(new SerializablePosition(position)));
        gameState.getPlayers().forEach(player -> this.players.add(new SerializablePlayer(player)));
        this.currPlayerIndex = gameState.getPlayers().indexOf(gameState.getCurrPlayer());
        this.numPlayer = gameState.getNumPlayer();
        List<SerializablePosition> options = gameState.getCurrPlayer().getState().getOptions();
        this.options = Objects.requireNonNullElseGet(options, ArrayList::new);
    }

    //getter and setter
    public List<SerializablePosition> getBoard() { return new ArrayList<>(board); }
    public List<SerializablePlayer> getPlayers() { return new ArrayList<>(players); }
    public int getCurrPlayerIndex() { return currPlayerIndex; }
    public SerializablePlayer getCurrPlayer() { return players.get(currPlayerIndex); }
    public int getNumPlayer() { return numPlayer; }
    public List<SerializablePosition> getOptions() { return new ArrayList<>(options); }

    /**
     * Gets list of free positions in all the board
     * @return List of free SerializablePositions
     */
    public List<SerializablePosition> getFreePositions() {
        return getBoard().stream().filter(position -> position.getWorker() == null).collect(Collectors.toList());
    }

    /**
     * Builds a string from this object.
     * The representation is composed of 11 rows and 3 main columns created in helper functions
     * @return a CLI-showable representation of the Board
     */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (int i=0; i<12;i++) {
            s.append(preBoardString(i));
            s.append(boardString(i));
            s.append(postBoardString(i));
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }

    /**
     * Composes the part of the CLI row that represents the board
     * @param rowIndex index of the line to represent, different rows have different (predetermined) content
     * @return string representing a row of the board
     */
    private String boardString(int rowIndex) {
        if (rowIndex == 0) {
            return CLIChar.NUMBER_HORIZONTAL.getString();
        }
        else if (rowIndex%2==1)
            return CLIChar.BOARD_HORIZONTAL.getString();
        else {
            StringBuilder sb = new StringBuilder();
            for (int j=0; j<5;j++) {
                int positionIndex = ((int) Math.floor((double)(rowIndex-1)/2))*5+j;
                sb.append(buildCell(getBoard().get(positionIndex)));
            }
            sb.append(CLIChar.END_BOARD.getString());
            return sb.toString();
        }
    }

    /**
     * Composes the part of the CLI row that shows the player names, their color, their god and the cell index.
     * @param rowIndex index of the line to represent, different rows have different (predetermined) content
     * @return string representing a row of the CLI information placed before the board cell representation
     */
    private String preBoardString(int rowIndex) {
        SerializablePlayer currPlayer = getCurrPlayer();
        SerializablePlayer secondPlayer = getPlayers().get((getCurrPlayerIndex()+1)%getNumPlayer());
        SerializablePlayer thirdPlayer = null;
        if (getNumPlayer() == 3)
            thirdPlayer = getPlayers().get((getCurrPlayerIndex()+2)%getNumPlayer());
        if (rowIndex==2)
            return String.format("%-6s%-31s%2s","Turn: ",getCurrPlayer().getTurnNum() > 0 ? getCurrPlayer().getTurnNum() : "Initial Worker Placement","A ");
        if (rowIndex==4)
            return String.format("%-16s   %-18s%2s",colorPlayer(currPlayer),currPlayer.getCard(),"B ");
        if (rowIndex==6)
            return String.format("%-16s   %-18s%2s",colorPlayer(secondPlayer),secondPlayer.getCard(),"C ");
        if (rowIndex==8)
            if (thirdPlayer == null)
                return String.format("%37s%2s"," ","D ");
            else
                return String.format("%-16s   %-18s%2s",colorPlayer(thirdPlayer),thirdPlayer.getCard(),"D ");
        if (rowIndex==10)
            return String.format("%-7s%-30s%2s","State: ", currPlayer.getState(),"E ");
        return String.format("%39s"," ");
    }

    /**
     * Composes the part of a CLI row that shows after the board i.e. blank space or a line of the symbol key
     * @param rowIndex index of the line to represent, different rows have different (predetermined) content
     * @return string representing a row of the post-board information
     */
    private String postBoardString(int rowIndex) {
        if (rowIndex == 0)
            return String.format(" %-24s",CLIChar.KEY_TITLE.getString());
        if (rowIndex == 2)
            return String.format(" %-24s",CLIChar.FIRST_LEVEL.getString()+": First level block");
        if (rowIndex == 4)
            return String.format(" %-24s",CLIChar.SECOND_LEVEL.getString()+": Second level block");
        if (rowIndex == 6)
            return String.format(" %-24s",CLIChar.THIRD_LEVEL.getString()+": Third level block");
        if (rowIndex == 8)
            return String.format(" %-24s",CLIChar.DOME.getString()+": Dome");
        if (rowIndex == 10)
            return String.format(" %-24s",CLIChar.WORKER_ON_GROUND.getString()+": Worker on ground");
        return String.format("%25s", " ");
    }

    /**
     * Returns the color of the player.
     * @param player player
     * @return the player's color
     */
    private Color getPlayerColor(SerializablePlayer player) {
        int playerIndex = getPlayers().indexOf(player);
        if (playerIndex == 1)
            return Color.BLUE;
        if (playerIndex == 2)
            return Color.RED;
        return Color.GREEN;
    }

    /**
     * Colors a CLIChar with a color
     * @return CLIChar colored as String
     */
    private String colorChar(CLIChar character, Color color) {
        return String.format("%s%s%s",color,character.getString(),Color.RESET);
    }

    /**
     * Creates a board cell representation -> "| ☃ " given its content.
     * Places blocks and colors based on workers
     * @param position position to be represented in the cell
     * @return a string representation of the cell
     */
    private String buildCell(SerializablePosition position) {
        String cellContent = null;
        if (position.getWorker() == null) {
            if (position.hasDome())
                cellContent = CLIChar.DOME.getString();
            else if (position.getHeight() == 0)
                cellContent = CLIChar.BLANK.getString();
            else if (position.getHeight() == 1)
                cellContent = CLIChar.FIRST_LEVEL.getString();
            else if (position.getHeight() == 2)
                cellContent = CLIChar.SECOND_LEVEL.getString();
            else if (position.getHeight() == 3)
                cellContent = CLIChar.THIRD_LEVEL.getString();
        }
        else {
            SerializablePlayer player = getPlayerFromWorker(position);
            Color currWorkerColor;
            if(position.getWorker().equals(player.getCurrWorker()))
                currWorkerColor = Color.PURPLE;
            else
                currWorkerColor = getPlayerColor(getPlayerFromWorker(position));
            if (position.getHeight() == 0)
                cellContent = colorChar(CLIChar.WORKER_ON_GROUND,currWorkerColor);
            else if (position.getHeight() == 1)
                cellContent = colorChar(CLIChar.FIRST_LEVEL,currWorkerColor);
            else if (position.getHeight() == 2)
                cellContent = colorChar(CLIChar.SECOND_LEVEL,currWorkerColor);
            else if (position.getHeight() == 3)
                cellContent = colorChar(CLIChar.THIRD_LEVEL,currWorkerColor);
        }
        if (cellContent == null)
            cellContent = colorChar(CLIChar.ERROR,getPlayerColor(getPlayerFromWorker(position)));
        List<SerializablePosition> match = options.stream().filter(pos -> pos.equals(position)).collect(Collectors.toList());
        if(match.size() == 1){
            String left_arrow = colorChar(CLIChar.AVAILABLE_POSITIONS_LEFT, Color.PURPLE);
            String right_arrow = colorChar(CLIChar.AVAILABLE_POSITIONS_RIGHT, Color.PURPLE);
            return String.format("|%s%s%s",left_arrow,cellContent,right_arrow);
        }
        else
            return String.format("| %s ",cellContent);
    }

    /**
     * Searches the player who owns the worker placed in a position
     * @param position position occupied by a worker
     * @return player who owns the worker
     */
    private SerializablePlayer getPlayerFromWorker(SerializablePosition position) {
        for (SerializablePlayer player: getPlayers()) {
            for (String worker: player.getWorkers()) {
                if (worker.equals(position.getWorker()))
                    return player;
            }
        }
        return getCurrPlayer();
    }

    private String colorPlayer(SerializablePlayer player) {
        return String.format("%s%-16s%s", getPlayerColor(player),player.getUsername(),Color.RESET);
    }
}