package it.polimi.ingsw.PSP4.client.gui;

import it.polimi.ingsw.PSP4.server.SocketClientConnection;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConfirmBox {

    static boolean answer;

    //shows confirmation box
    public static boolean displayConfirm(String title, String message){

        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(200);

        Label label = new Label();
        label.setText(message);

        Button yesButton = new Button ("Yes");
        yesButton.setOnAction(e -> {
            answer = true;
            window.close();
        }
        );

        Button noButton = new Button ("No");
        noButton.setOnAction(e -> {
            answer = false;
            window.close();
        }
        );

        VBox layout = new VBox(10);

        layout.getChildren().addAll(label,yesButton,noButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();

        return answer;

    }
}