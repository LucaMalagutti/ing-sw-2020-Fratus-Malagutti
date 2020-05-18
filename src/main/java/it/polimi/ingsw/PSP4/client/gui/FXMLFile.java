package it.polimi.ingsw.PSP4.client.gui;

public enum FXMLFile {
    LAUNCHER_PLAY("launcher-play.fxml"),
    LAUNCHER_FORM("launcher-form.fxml"),
    LAUNCHER_NUMBER_PLAYERS("launcher-number-players.fxml"),
    LOBBY_ALLOWED_GODS("lobby-allowed-gods.fxml"),
    LOBBY_YOUR_GOD_SELECTION_TWO("lobby-your-god-selection-two.fxml"),
    LOBBY_YOUR_GOD_SELECTION_THREE("lobby-your-god-selection-three.fxml"),
    LOBBY_STARTING_PLAYER_SELECTION_TWO("lobby-starting-player-selection-two.fxml"),
    LOBBY_STARTING_PLAYER_SELECTION_THREE("lobby-starting-player-selection-three.fxml");

    private final String fileName;

    FXMLFile(String fileName) { this.fileName = fileName; }

    public String getFileName() { return fileName; }
}
