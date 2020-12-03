/*
 * EE422C Final Project submission by
 * Lars Fyhr
 * lcf597
 * 16195
 * Fall 2020
 * Slip Days Used: 3/3
 */

package client;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import com.sun.javafx.scene.control.skin.ScrollPaneSkin;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.image.*;
import javafx.scene.text.Text;
import javafx.scene.Node;
import javafx.geometry.*;
import javafx.util.*;
import javafx.animation.*;
import java.io.File;
import java.util.ArrayList;


public class Main extends Application{

    VBox itempane;
    Image image = new Image("File:JavaMarketLogo.png");
    Label username;
    ToolBar tb;
    Button quit;
    GridPane root;
    Stage stage;
    boolean connected;
    Client c;
    String name;
    String pswrd;
    MediaPlayer mp;
    ArrayList<ClientItem> items = new ArrayList<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primstage) {
        VBox startpage = new VBox();
        username = new Label();
        stage = primstage;
        stage.setTitle("JavaMarket");
        root = new GridPane();
        itempane = new VBox();

        //Add a quit feature
        quit = new Button("Quit");
        quit.setOnAction(e -> {
            System.exit(0);
        });

        //Generating the page objects and structuring accordingly
        tb = new ToolBar();
        tb.getItems().add(username);
        username.setAlignment(Pos.CENTER_LEFT);
        Pane hspacer = new Pane();
        HBox.setHgrow(hspacer, Priority.ALWAYS);
        Pane vspacer = new Pane();
        VBox.setVgrow(vspacer, Priority.ALWAYS);
        tb.getItems().add(hspacer);
        tb.getItems().add(quit);
        quit.setAlignment(Pos.CENTER_RIGHT);
        root.setAlignment(Pos.BOTTOM_CENTER);

        login();

        //Adding the logo
        ImageView iv = new ImageView(image);
        iv.setFitHeight(200);
        iv.setFitWidth(400);

        //Continued editing of page contents
        startpage.getChildren().add(iv);
        startpage.getChildren().add(root);
        startpage.getChildren().add(vspacer);
        startpage.getChildren().add(tb);
        stage.setScene(new Scene(startpage, 400, 400));
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();

        String mediapath = "chaching.mp3";
        //Instantiating Media class
        Media media = new Media(new File(mediapath).toURI().toString());
        //Instantiating MediaPlayer class
        mp = new MediaPlayer(media);
    }

    //Create the login page for the Client's GUI
    public void login() {
        Text usr = new Text("Username:");
        Text pass = new Text("Password (opt):");
        Text err = new Text("");
        Button connect = new Button();
        TextField usrentry = new TextField();
        TextField passentry = new TextField();
        connect.setText("Sign In");
        //Try and connect the GUI as a client to the server, or return error
        connect.setOnAction(e -> {
            if (!connected && !usrentry.getText().equals("")) {
                err.setText("");
                name = usrentry.getText();
                pswrd = passentry.getText();
                c = new Client("0.0.0.0", 5000, name, pswrd);
                if (c.connected) {
                    connected = true;
                    homepage();
                } else {
                    err.setText("Could Not Connect to Server");
                    err.setFill(Color.RED);
                }
            } else if (!connected) {
                err.setText("Please Enter a Username");
                err.setFill(Color.RED);
            }
        });

        //Formatting the page
        root.getColumnConstraints().add(new ColumnConstraints(50));
        root.getColumnConstraints().add(new ColumnConstraints(100));
        root.getColumnConstraints().add(new ColumnConstraints(200));
        root.getColumnConstraints().add(new ColumnConstraints(50));
        root.add(usr, 1, 1);
        root.add(pass, 1, 2);
        root.add(usrentry, 2, 1);
        root.add(passentry, 2, 2);
        root.add(err, 2,3);
        root.add(connect, 2, 4);
        root.setAlignment(Pos.CENTER);
    }

    //Probe the server to get changes to items and then update the items accordingly
    public void update() {
        c.writeCommand("update");
        String itemlist = c.getItems();
        itemlist = itemlist.replace("[","");
        itemlist = itemlist.replace("]","");
        String [] tempitems = itemlist.split(", ");
        for (int i = 0; i < tempitems.length; i++) {
            ClientItem tempci = new ClientItem(tempitems[i]);
            for (ClientItem ci : items) {
                if (tempci.iname.equals(ci.iname)) {
                    ci.updateinfo(tempci.currprice, tempci.numbids, tempci.endtime, tempci.topbidder);
                    ci.updateGP(mp);
                }
            }
        }
    }

    //Generate the Client's GUI homepage for JavaMarket
    public void homepage() {
        //Set constant animating update loop
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.2), ev -> {
            update();
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        //Make scrollable pane for the items
        ScrollPane sp = new ScrollPane();
        sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        sp.setFitToWidth(true);
        sp.setSkin(new ScrollPaneSkin(sp) {
            @Override
            public void onTraverse(Node n, Bounds b) {
            }
        });

        //Format the page
        itempane.getChildren().add(sp);
        sp.setContent(root);
        root.getChildren().clear();
        root.getColumnConstraints().clear();
        root.setAlignment(Pos.TOP_CENTER);
        root.getColumnConstraints().add(new ColumnConstraints(800));

        //Add the Logo
        ImageView iv = new ImageView(image);
        iv.setFitWidth(300);
        iv.setFitHeight(120);
        StackPane p = new StackPane();
        p.setAlignment(Pos.CENTER);
        p.getChildren().add(iv);
        p.setStyle("-fx-border-color: black; -fx-border-width: 0 0 1 0");
        root.add(p, 0, 0);

        //Add a username view and add it
        username.setText("Current User: " + c.name);
        itempane.getChildren().add(tb);

        //Generate the item pane's in the page
        String itemlist = c.getItems();
        itemlist = itemlist.replace("[","");
        itemlist = itemlist.replace("]","");
        String [] items = itemlist.split(", ");
        for (int i = 0; i < items.length; i++) {
            root.add(genItem(items[i]),0, i + 1);
        }
        update();

        //Update the stage
        stage.setScene(new Scene(itempane));
        stage.setWidth(itempane.getWidth() + 20);
        stage.setHeight(700);
        stage.centerOnScreen();
    }

    //Generate the item list for client-side use
    public GridPane genItem(String item) {
        ClientItem i = new ClientItem(item);
        i.attachGP(c);
        items.add(i);
        return i.ip;
    }

}
