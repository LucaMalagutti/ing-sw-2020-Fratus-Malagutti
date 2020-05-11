package it.polimi.ingsw.PSP4.client.gui;

import it.polimi.ingsw.PSP4.server.SocketClientConnection;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConfirmBox {

    static private Stage window;
    static boolean answer;

    //shows confirmation box
    public static boolean displayConfirm(String title, String message){

        window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.getIcons().addAll(GUIClient.window.getIcons());
        window.setTitle(title);
        window.setMinWidth(200);

        Label label = new Label();
        label.setText(message);

        Button yesButton = new Button ("Yes");
        yesButton.setMinWidth(75);
        yesButton.setOnAction(e -> yesButtonPushed());

        Button noButton = new Button ("No");
        noButton.setMinWidth(75);
        noButton.setOnAction(e -> noButtonPushed());

        window.setOnCloseRequest(e -> noButtonPushed());

        HBox buttons = new HBox(10);

        buttons.getChildren().addAll(noButton, yesButton);
        buttons.setAlignment(Pos.CENTER);

        VBox layout = new VBox(10);

        layout.getChildren().addAll(label, buttons);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20, 20, 20 ,20));

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();

        return answer;
    }

    private static void yesButtonPushed() {
        answer = true;
        window.close();
    }

    private static void noButtonPushed() {
        answer = false;
        window.close();
    }
}