package it.polimi.ingsw.PSP4.client.gui;

public enum FXMLFile {
    LAUNCHER_PLAY("launcher-play.fxml"),
    LAUNCHER_FORM("launcher-form.fxml"),
    LAUNCHER_NUMBER_PLAYERS("launcher-number-players.fxml"),
    LOBBY_GODS_SELECTION("lobby-gods-selection.fxml"),
    LOBBY_STARTING_PLAYER_SELECTION_TWO("lobby-starting-player-selection-two.fxml"),
    LOBBY_STARTING_PLAYER_SELECTION_THREE("lobby-starting-player-selection-three.fxml"),
    LOBBY_WAIT("lobby-wait.fxml"),
    BOARD("board.fxml");

    private final String fileName;

    FXMLFile(String fileName) { this.fileName = fileName; }

    public String getFileName() { return fileName; }

    public String getWindowTitle() {
        switch(this) {
            case LAUNCHER_PLAY:
            case LAUNCHER_FORM:
            case LAUNCHER_NUMBER_PLAYERS:
                return GUIClient.WINDOW_TITLE_LAUNCHER;
            case LOBBY_GODS_SELECTION:
            case LOBBY_STARTING_PLAYER_SELECTION_TWO:
            case LOBBY_STARTING_PLAYER_SELECTION_THREE:
                return GUIClient.WINDOW_TITLE_LOBBY;
            default:
                return GUIClient.WINDOW_TITLE_GAME;
        }
    }
}
