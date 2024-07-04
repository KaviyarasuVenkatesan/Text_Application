package com.example.messengerclient;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private Button button_send;
    @FXML
    private TextField tf_message;
    @FXML
    private ScrollPane sp_main;
    @FXML
    private VBox vbox_message;

    private Client client;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try
        {
            client = new Client(new Socket("localhost" , 6969));
            System.out.println("Connected to Server");
        }catch (IOException e)
        {
            e.printStackTrace();
            System.out.println("Error in connecting Server");
        }

        vbox_message.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                sp_main.setVvalue((Double)t1);
            }
        });

        client.receiveMessageFromServer(vbox_message);

        button_send.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String messageToSend = tf_message.getText();
                if(!messageToSend.isEmpty())
                {
                    HBox hBox = new HBox();
                    hBox.setAlignment(Pos.CENTER_RIGHT);
                    hBox.setPadding(new Insets(5,5,5,10));

                    Text text = new Text(messageToSend);
                    TextFlow textFlow = new TextFlow(text);

                    textFlow.setStyle("-fx-color :rgb(243,235,250);" +
                            "-fx-background-color :rgb(15,150,250);" +
                            "-fx-background-radius : 20px;");

//                    textFlow.setStyle("-fx-background-color: #0f96fa; " +
//                            "-fx-background-radius: 20px;");

                    textFlow.setPadding(new Insets(5,10,5,10));

                    hBox.getChildren().add(textFlow);
                    vbox_message.getChildren().add(hBox);

                    client.sendMessageToServer(messageToSend);
                    tf_message.clear();
                }
            }
        });
    }

    public static void addLabel(String messageFromTheServer , VBox vBox)
    {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(5,5,5,10));

        Text text = new Text(messageFromTheServer);
        TextFlow textFlow = new TextFlow(text);
        textFlow.setStyle("-fx-background-color : rgb(233,233,233);" +
                "-fx-background-radius : 20px;");
        textFlow.setPadding(new Insets(5,10,5,10));

        hBox.getChildren().add(textFlow);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                vBox.getChildren().add(hBox);
            }
        });
    }
}