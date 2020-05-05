package it.polimi.ingsw.PSP4.client.gui;

import it.polimi.ingsw.PSP4.message.Message;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class GUIClient extends Application{

    Stage window;
    Scene scene1, scene2;

    //This runs the GUI
    public void run(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        window = primaryStage;

        //button 1
        Button button1 = new Button("START GAME");
        button1.setOnAction(e -> window.setScene(scene2));

        //label 1
        Label label1 = new Label("WELCOME TO SANTORINI");
        label1.setFont(new Font( 32));

        //scene 1
        VBox layout1 = new VBox(50);
        layout1.getChildren().addAll(label1, button1);
        layout1.setAlignment(Pos.CENTER);
        scene1 = new Scene(layout1, 500,350);

        //layout2
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(60,50,100,50));
        grid.setVgap(8);
        grid.setHgap(10);

        //IP ADDRESS Label
        Label ipAddressLabel = new Label("IP Address: ");
        GridPane.setConstraints(ipAddressLabel,0 ,0 );
        //IP ADDRESS Textfield
        TextField ipAddressInput = new TextField();
        ipAddressInput.setPromptText("address");
        GridPane.setConstraints(ipAddressInput,1,0);

        //USERNAME Label
        Label usernameLabel = new Label("Username: ");
        GridPane.setConstraints(usernameLabel,0 ,1 );
        //USERNAME Textfield
        TextField usernameInput = new TextField();
        usernameInput.setPromptText("username");
        GridPane.setConstraints(usernameInput,1,1);

        //ENTER button
        Button enterButton = new Button ("Confirm");
        GridPane.setConstraints(enterButton,1,2);

        grid.getChildren().addAll(ipAddressLabel,ipAddressInput,usernameLabel,usernameInput,enterButton);
        scene2 = new Scene(grid,500,350);

        window.setScene(scene1);
        window.setTitle("SANTORINI");
        window.show();

    }


    public void closeProgram(){
        //if you want to do something before closing
        Boolean answer = ConfirmBox.displayConfirm("Title of the box", "SURE YOU WANT TO EXIT?");
        if(answer)
            window.close();
    }

}
