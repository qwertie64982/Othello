import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class Menu extends Application {
    Play othello;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Othello");
        try {
            othello = new Play();
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
        BorderPane Bpane = new BorderPane();
        GridPane Gpane = new GridPane();


        //Set properties for GridPane
        Gpane.setAlignment(Pos.CENTER);
        Gpane.setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
        Gpane.setHgap(10);
        Gpane.setVgap(40);


        //Creates the label and its properties
        Label L1 = new Label();
        L1.setStyle("-fx-border-color: black; -fx-font-size: 15 px; -fx-font-family: New Times Roman;");


        //Creating Textfield for user input and setting its properties.
        TextField T1 = new TextField();
        T1.setPromptText("Enter the name of a character from the play.");


        //Creating button and stylizing it.
        Button confirm = new Button("Enter");
        Button bsc1 = new Button("Speaker line-counter");
        Button bsc2 = new Button("Fragement Searching");
        // person = othello.countSpeakerLines("RODERIGO");

        // Label label1 = new Label("Roderigo speaks " + person + " Times");

        // pane.setStyle(
        //       "-fx-border-color: black; -fx-background-color: lightgray;");


        //Nests GridPane within center of BorderPane and adds nodes to GridPane

        Bpane.setCenter(Gpane);
        Bpane.setTop(bsc2);
        BorderPane.setMargin(bsc2, new Insets(12,12,12,12));
        Bpane.setAlignment(bsc2, Pos.CENTER);
        Gpane.add(T1, 0,  0);
        Gpane.add(confirm, 1, 0);
        Gpane.add(L1, 0,1);


        //Creating scenes
        Scene scene2 = new Scene(bsc1,200,200);
        Scene scene1 = new Scene(Bpane, 200,200);

        //Sets actions for all buttons
//        confirm.setOnAction(e -> per = T1.getText());
        confirm.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String per = T1.getText();

                if (othello.countSpeakerLines(per) == -1) {
                    L1.setText("This Character is not in the play.");
                } else if (othello.countSpeakerLines(per) == 0) {
                    L1.setText("This character does not speak in the play.");
                } else {
                    L1.setText("This Character has spoken " + othello.countSpeakerLines(per) + " times.");
                }
            }
        });
        bsc2.setOnAction(e -> primaryStage.setScene(scene2));
        bsc1.setOnAction(e -> primaryStage.setScene(scene1));
        

            primaryStage.setScene(scene1);
        Image icon = new Image("https://i.imgur.com/GX5zmdS.png");
        primaryStage.getIcons().add(icon);
        primaryStage.show();
    }
}

// public class AltDriver {
//    public static  void main(String[] args) {
//        launch(args);
//    }
// }