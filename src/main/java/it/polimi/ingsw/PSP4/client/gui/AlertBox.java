package it.polimi.ingsw.PSP4.client.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertBox {

    /**
     * Shows the error message in a new window
     * @param title title of the AlertBox
     * @param message message displayed in the center of the AlertBox
     */
    public static void displayError(String title, String message){
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.getIcons().addAll(GUIClient.window.getIcons());
        window.setTitle(title);

        Label label = new Label();
        label.setText(message);

        Button closeButton = new Button("OK");
        closeButton.setMinWidth(75);
        closeButton.setOnAction(e -> window.close());

        VBox layout = new VBox(10);

        layout.getChildren().addAll(label, closeButton);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20, 20, 20 ,20));

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();

    }
}
