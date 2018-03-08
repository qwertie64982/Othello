import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.image.Image;

public class Menu extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Othello");
        Play othello = new Play();

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
        // person = othello.countSpeakerLines("RODERIGO");

        // Label label1 = new Label("Roderigo speaks " + person + " Times");

        // pane.setStyle(
        //       "-fx-border-color: black; -fx-background-color: lightgray;");


        //Nests GridPane within center of BorderPane and adds nodes to GridPane
        Bpane.setCenter(Gpane);
        Gpane.add(T1, 0,  0);
        Gpane.add(confirm, 1, 0);
        Gpane.add(L1, 0,1);

        //Sets action for "enter" button
        // TODO: make it so that this isn't case-sensitive.
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


        Scene scene = new Scene(Bpane, 200,200);
        primaryStage.setScene(scene);
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