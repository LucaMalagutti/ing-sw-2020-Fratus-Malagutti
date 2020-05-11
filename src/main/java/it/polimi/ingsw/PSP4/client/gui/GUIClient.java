package it.polimi.ingsw.PSP4.client.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class GUIClient extends Application{
    static Stage window;

    public void run(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage stage) {
        window = stage;

        window.getIcons().add(new Image(GUIClient.class.getResourceAsStream("/images/icon.png")));
        window.setTitle("Launcher - Santorini");
        window.setResizable(false);
        window.setOnCloseRequest(e -> {
            e.consume();
            closeProgram();
        });

        GUIClient.updateScene(FXMLFile.LAUNCHER_PLAY, false);
    }

    public void closeProgram(){
        boolean answer = ConfirmBox.displayConfirm("We'll miss you", "Are you sure you want to leave Santorini?");
        if(answer)
            window.close();
    }

    public static void updateScene(FXMLFile file, boolean fullScreen) {
        Parent root;
        try {
            root = FXMLLoader.load(GUIClient.class.getResource("/scenes/" + file.getFileName()));
        } catch (Exception e) {
            AlertBox.displayError("Unexpected error", "We are sorry, something is not working");
            window.close();
            return;
        }
        Scene scene = new Scene(root);
        window.setScene(scene);
        window.setFullScreen(fullScreen);
        window.show();
    }

}
