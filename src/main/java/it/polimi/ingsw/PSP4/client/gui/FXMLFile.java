package it.polimi.ingsw.PSP4.client.gui;

public enum FXMLFile {
    LAUNCHER_PLAY("launcher-play.fxml"),
    LAUNCHER_FORM("launcher-form.fxml"),
    LAUNCHER_NUMBER_PLAYERS("launcher-number-players.fxml"),
    LOBBY_ALLOWED_GODS("lobby-allowed-gods.fxml");

    private final String fileName;

    FXMLFile(String fileName) { this.fileName = fileName; }

    public String getFileName() { return fileName; }
}
