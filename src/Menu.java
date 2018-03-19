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
import java.util.ArrayList;
import java.util.List;

public class Menu extends Application {
    private Play othello;

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
        BorderPane Bpane1 = new BorderPane();
        GridPane Gpane1 = new GridPane();


        //Set properties for GridPane
        Gpane.setAlignment(Pos.CENTER);
        Gpane.setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
        Gpane.setHgap(10);
        Gpane.setVgap(40);

        Gpane1.setAlignment(Pos.CENTER_LEFT);
        Gpane1.setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
        Gpane1.setHgap(10);
        Gpane1.setVgap(20);


        //Creates the label and its properties
        Label L1 = new Label();
        L1.setStyle("-fx-border-color: black; -fx-font-size: 14 px; -fx-font-family: New Times Roman;");

        Label L2 = new Label("Enter the name of a character");
        L2.setStyle("-fx-border-color: white; -fx-font-size: 14 px; -fx-font-family: New Times Roman;");



        //Creating Textfield for user input and setting its properties.
        TextField T1 = new TextField();
        T1.setPromptText("Enter the name of a character from the play.");


        //Creating button and stylizing it.
        Button confirm = new Button("Enter");
        Button bsc1 = new Button("Speaker line-counter");
        Button bsc2 = new Button("Fragement Searching");



        //Buttons, labels, and text-fields for scene2
        Button confirm1 = new Button("Enter");
        Button confirm2 = new Button("Enter");
        Button save = new Button("Save");
        Label L3 = new Label("Type in the fragment you want to search for");
        Label L4 = new Label("Type in the fragment you want to replace");
        Label LabelFragCount = new Label();
        Label ReplaceFrag = new Label();
        TextField T2 = new TextField();
        TextField T3 = new TextField();
        TextField T4 = new TextField();
        TextField FragLine = new TextField();

        //Stylize labels
        L3.setStyle("-fx-border-color: white; -fx-font-size: 14 px; -fx-font-family: New Times Roman;");
        L4.setStyle("-fx-border-color: white; -fx-font-size: 14 px; -fx-font-family: New Times Roman;");
        LabelFragCount.setStyle("-fx-border-color: black; -fx-font-size: 14 px; -fx-font-family: New Times Roman;");
        ReplaceFrag.setStyle("-fx-border-color: black; -fx-font-size: 14 px; -fx-font-family: New Times Roman;");

        T2.setPromptText("Type in fragment");
        T3.setPromptText("Fragment to be changed");
        T4.setPromptText("Edited Fragment");
        FragLine.setPromptText("Number for appearance");
        //Nests GridPane within center of BorderPane and adds nodes to GridPane

        Bpane.setCenter(Gpane);
        Bpane.setTop(bsc2);
        BorderPane.setMargin(bsc2, new Insets(12,12,12,12));
        Bpane.setAlignment(bsc2, Pos.CENTER);
        Gpane.add(L2, 0, 0);
        Gpane.add(T1, 0,  1);
        Gpane.add(confirm, 1, 1);
        Gpane.add(L1, 0,2);


        Bpane1.setCenter(Gpane1);
        Bpane1.setTop(bsc1);
        Bpane1.setBottom(save);
        BorderPane.setMargin(bsc1, new Insets(12,12,12,12));
        Bpane1.setAlignment(bsc1, Pos.CENTER);
        Gpane1.add(L3,0,0);
        Gpane1.add(T2,0,1);
        Gpane1.add(confirm1,1,1);
        Gpane1.add(LabelFragCount,0,2);
        Gpane1.add(L4,0,3);
        Gpane1.add(T3,0,4);
        Gpane1.add(T4,1,4);
        Gpane1.add(FragLine,2,4);
        Gpane1.add(confirm2,3,4);
        Gpane1.add(ReplaceFrag,0,5);



        //Creating scenes
        Scene scene2 = new Scene(Bpane1,540,405);
        Scene scene1 = new Scene(Bpane, 400,300);

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
        confirm1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String frag = T2.getText();
                int num = othello.fragmentCount(frag);
                ArrayList<String> lines = othello.findLinesFromFragment(frag);
                if (num > 0){
                    LabelFragCount.setText("Fragment is found in the play " + num + " times.");
                }
                else{
                    LabelFragCount.setText("Fragment not found in play.");
                }
                }
            });
        confirm2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String frag = T3.getText();
                String editfrag = T4.getText();
                int line = Integer.parseInt(FragLine.getText());
                ArrayList<String> foundFragments = othello.findLinesFromFragment(frag);
                boolean success;
                if (line > 0 && line <= foundFragments.size()){
                    success = othello.replaceFragment(frag, editfrag, othello.findLinesFromFragment(frag).get(line-1));
                }
                else {
                    success = false;
                }
                if (success){
                    ReplaceFrag.setText("Fragment replaced successfully.");
                }
                else{
                    ReplaceFrag.setText("Search failed.");
                }

            }
        });
        save.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                boolean success = othello.saveFile();
            }
        });
        bsc2.setOnAction(e -> primaryStage.setScene(scene2));
        bsc1.setOnAction(e -> primaryStage.setScene(scene1));


            primaryStage.setScene(scene1);
        Image icon = new Image("https://i.imgur.com/GX5zmdS.png");
        primaryStage.getIcons().add(icon);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}

// public class AltDriver {
//    public static  void main(String[] args) {
//        launch(args);
//    }
// }