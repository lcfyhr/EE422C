package assignment5;
/*
 * CRITTERS2 Main.java
 * EE422C Project 5 submission by
 * Replace <...> with your actual data.
 * J. Michael Routh
 * JMR7567
 * 16190
 * Lars Fyhr
 * lcf597
 * 16195
 * Slip days used: <0>
 * Fall 2020
 */

import java.io.File;

import javafx.application.Application;
import javafx.scene.media.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.lang.reflect.Method;
import java.util.List;

public class Main extends Application{
	
	/* Gets the package name.  The usage assumes that Critter and its
    subclasses are all in the same package. */
	private static String myPackage; // package of Critter file.

	/* Critter cannot be in default pkg. */
	static {
		myPackage = Critter.class.getPackage().toString().split(" ")[1];
	}
	
	@Override
	public void start(Stage primaryStage) {
		try {
			int rows = Params.WORLD_HEIGHT;
			int columns = Params.WORLD_WIDTH;
			
			BorderPane border = new BorderPane(); // have a borderpane around the gridpane
			GridPane tools = new GridPane(); // have a gridpane to the left of critter sim
			GridPane grid = new GridPane();
			border.setCenter(grid);
			border.setLeft(tools);
			grid.setStyle("-fx-background-color: mintcream ;\n" + 	"    -fx-padding: 10 ;");
			
			//int constraint = 30 - (rows/10); //variable grid size based on number of rows
			//if(constraint < 5) {
			//	constraint = 5;
			//}
			
	        tools.setPadding(new Insets(10, 10, 10, 10));
	        tools.setVgap(10);
	        tools.setHgap(10);
	        
	        Text seedText = new Text("Enter Seed (integer)");
	        Text critterText = new Text("Select Critter");
	        Text critterNumberText = new Text("Number of Critters");
	        Text stepNumberText = new Text("Number of Steps (default 1)"); 
	        String runStatsStr = "";
	        Text runStatsText = new Text("Stats\n");

	        TextField seedField = new TextField();
	        TextField crittNumberField = new TextField();
	        TextField stepNumberField = new TextField();
	        
	        
	        //create observable lists for 2 drop down boxes
	        String path = myPackage.replaceAll("\\.", File.separator);
	        ObservableList<String> Names = FXCollections.observableArrayList();

	        String classPathEntry = System.getProperty("user.dir"); //get where java was run from
	     	String fileName; //make an abstract filename

	     	File f = new File(classPathEntry + File.separatorChar + "src" + File.separatorChar + path); //go to where java is running
	        //into the source folder 

	        for (File file : f.listFiles()) { //check each file
	            fileName = file.getName();
	            fileName = fileName.substring(0, fileName.lastIndexOf('.')); //cut the file extension off

	            Class<?> critClass = null;  //reset these two each time
	            Object critInstance = null;

	            try {
	            critClass = Class.forName(myPackage + "." + fileName); //call forName with myPackage.fileName
	            critInstance = critClass.getConstructor().newInstance(); //get instance of class
	            }
	            catch (Exception e) {
	            }
	            if(Critter.class.isInstance(critInstance)) {  //if its an instance of Critter
	                Names.add(fileName); //Add to dropdown list
	            }
	        }
	         
	        ComboBox dropDownCritter = new ComboBox(Names); //make drop down lists from the list of names
	        ComboBox dropDownCritterRunStats = new ComboBox(Names);

            Slider speed = new Slider();
            speed.setMin(0);
            speed.setMax(10);
            speed.setValue(0);
            speed.setShowTickMarks(true);
            speed.setShowTickLabels(true);
            speed.setBlockIncrement(1);
            speed.setMinorTickCount(1);
	        
			Button seedButton = new Button("Set Seed");
			Button critterButton = new Button("Add Critters");
			Button stepButton = new Button("Time Step");
			Button dispButton = new Button("Display World");
			Button runButton = new Button("Run Critters");
			Button quitButton = new Button("Quit");
			Button runStatsButton = new Button("Run Stats");

            seedButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (!seedField.getText().equals("")) {
                        boolean num = true;
                        for (int c = 0; c < seedField.getText().length(); c++) {
                            if (!Character.isDigit(seedField.getText().charAt(c))) {
                               num = false;
                            }
                        }
                        if (num) {
                            Critter.setSeed(Long.parseLong(seedField.getText()));
                        }
                    }
                }
            });

			critterButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (!crittNumberField.getText().equals("")) {
                        boolean num = true;
                        for (int c = 0; c < crittNumberField.getText().length(); c++) {
                            if (!Character.isDigit(crittNumberField.getText().charAt(c))) {
                                num = false;
                            }
                        }
                        if (num) {
                            if (dropDownCritter.getValue() != null) {
                                for (int c = 0; c < Integer.parseInt(crittNumberField.getText()); c++) {
                                    try {
                                        Critter.createCritter(dropDownCritter.getValue().toString());
                                    } catch (InvalidCritterException e) {
                                        System.out.println("error creating: " + dropDownCritter.getValue().toString());
                                    }
                                }
                            }
                        }
                    } else {
                        if (dropDownCritter.getValue() != null) {
                            try {
                                Critter.createCritter(dropDownCritter.getValue().toString());
                            } catch (InvalidCritterException e) {
                                System.out.println("error creating: " + dropDownCritter.getValue().toString());
                            }
                        }
                    }
                }
            });

            stepButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (!stepNumberField.getText().equals("")) {
                        boolean num = true;
                        for (int c = 0; c < stepNumberField.getText().length(); c++) {
                            if (!Character.isDigit(stepNumberField.getText().charAt(c))) {
                                num = false;
                            }
                        }
                        if (num) {
                            for (int s = 0; s < Integer.parseInt(stepNumberField.getText()); s++) {
                                Critter.worldTimeStep();
                            }
                        }
                    } else {
                        Critter.worldTimeStep();
                    }
                }
            });

			dispButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    Critter.displayWorld(grid);
                }
            });

			runButton.setOnAction(new EventHandler<ActionEvent>() {
			    boolean running = false;
                Timeline timeline =
                        new Timeline(new KeyFrame(Duration.millis(500), e -> Critter.displayWorld(grid)),
                                new KeyFrame(Duration.millis(500), e -> loop()),
                                new KeyFrame(Duration.millis(500), e -> statupdate())
                        );

                        void loop() {
                            for (int s = 0; s < (int) speed.getValue(); s++) {
                                Critter.worldTimeStep();
                            }
                        }

                        void statupdate () {
                            if (dropDownCritterRunStats.getValue() != null) {
                                try {
                                    List<Critter> my_list = Critter.getInstances(dropDownCritterRunStats.getValue().toString());
                                    Class<?> c = Class.forName(myPackage + "." + dropDownCritterRunStats.getValue().toString());
                                    Method stats = c.getMethod("runStats", List.class);
                                    runStatsText.setText((String) stats.invoke(null, my_list));
                                } catch (Exception e) {
                                    System.out.println("error creating: " + dropDownCritterRunStats.getValue().toString());
                                }
                            }
                        }

                @Override
                public void handle(ActionEvent event) {
                    running = !running;
                    timeline.setCycleCount(Animation.INDEFINITE); // loop forever
                    if (running) {
                        timeline.play();
                        runButton.setText("Stop Critters");
                        seedButton.setDisable(true);
                        critterButton.setDisable(true);
                        stepButton.setDisable(true);
                        dispButton.setDisable(true);
                        quitButton.setDisable(true);
                        runStatsButton.setDisable(true);
                        dropDownCritter.setDisable(true);
                        dropDownCritterRunStats.setDisable(true);
                        seedField.setDisable(true);
                        crittNumberField.setDisable(true);
                        stepNumberField.setDisable(true);
                        speed.setDisable(true);

                    } else {
                        timeline.pause();
                        runButton.setText("Run Critters");
                        seedButton.setDisable(false);
                        critterButton.setDisable(false);
                        stepButton.setDisable(false);
                        dispButton.setDisable(false);
                        quitButton.setDisable(false);
                        runStatsButton.setDisable(false);
                        dropDownCritter.setDisable(false);
                        dropDownCritterRunStats.setDisable(false);
                        seedField.setDisable(false);
                        crittNumberField.setDisable(false);
                        stepNumberField.setDisable(false);
                        speed.setDisable(false);
                    }
                }
            });

			quitButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    System.exit(0);
                }
            });

			runStatsButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (dropDownCritterRunStats.getValue() != null) {
                        try {
                            List<Critter> my_list = Critter.getInstances(dropDownCritterRunStats.getValue().toString());
                            Class<?> c = Class.forName(myPackage + "." + dropDownCritterRunStats.getValue().toString());
                            Method stats = c.getMethod("runStats", List.class);
                            runStatsText.setText((String) stats.invoke(null, my_list));
                        } catch (Exception e) {
                            System.out.println("error creating: " + dropDownCritterRunStats.getValue().toString());
                        }
                    }
                }
            });

			tools.add(seedText, 1, 1);
			tools.add(seedButton, 5, 3);
			tools.add(seedField, 5, 1);

			tools.add(critterText, 1, 5);
			tools.add(critterNumberText, 1, 7);
			tools.add(crittNumberField, 5, 7);
			tools.add(dropDownCritter, 5, 5);
			tools.add(critterButton, 5, 9);

			tools.add(stepNumberText, 1, 11);
			tools.add(stepNumberField, 5, 11);
			tools.add(stepButton, 5, 13);

			tools.add(dispButton, 1, 15);

			tools.add(runButton, 1, 19);

			tools.add(quitButton, 5, 19);

			tools.add(speed, 5, 15);

			tools.add(runStatsButton, 1, 17);
			tools.add(dropDownCritterRunStats, 5, 17);

			tools.add(runStatsText, 1, 21);

			grid.getChildren().clear();
			for(int i = 0; i < columns+1; i++) {								//create the grid for sim
	            ColumnConstraints column = new ColumnConstraints();
	            if (i>0) {
                    column.setPercentWidth(100 / columns);
                } else {
	                column.setPercentWidth(5 - columns/50);
                }
	            grid.getColumnConstraints().add(column);
	        }

	        for(int i = 0; i < rows+1; i++) {
	            RowConstraints row = new RowConstraints();
	            if (i>0) {
                    row.setPercentHeight(100 / rows);
                } else {
	                row.setPercentHeight(5 - rows/50);
                }
	            grid.getRowConstraints().add(row);
	        }

	        for (int i = 0; i < columns+1; i++) {
	            for (int j = 0; j < rows+1; j++) {
	            	StackPane pane = new StackPane();
	                pane.setStyle("-fx-background-color: steelblue, mintcream ;\n" + //set the background and grid color
	                		      "    -fx-background-insets: 0, 1 1 1 1 ;"); //set the borders of the grid to be 1 pixel wide, overlapping
	                if (i == 0) {;
	                    pane.setStyle("-fx-background-insets: 0, 1 0 0 1;"); //add a pane to the top
	                }
	                if (j == 0) {
	                    pane.setStyle("-fx-background-insets: 0, 1 0 0 1;"); //add a pane to the left
	                }
	                grid.add(pane, i, j);
	            }
	        }
	        Scene scene = new Scene(border, 5*(columns + 75) + 500, 5*(rows+25) + 500, Color.WHITE);
	        primaryStage.setTitle("Project 5, Critters 2");
            String mediapath = "BeepBox-Song.wav";

            //Instantiating Media class
            Media media = new Media(new File(mediapath).toURI().toString());

            //Instantiating MediaPlayer class
            MediaPlayer mediaPlayer = new MediaPlayer(media);

            mediaPlayer.setOnEndOfMedia(new Runnable() {
                public void run() {
                    mediaPlayer.seek(Duration.ZERO);
                }
            });
            mediaPlayer.play();

            mediaPlayer.setOnReady(new Runnable() {
                @Override
                public void run() {
                    mediaPlayer.play();
                }
            });

            primaryStage.setScene(scene);
	        primaryStage.show();
	        
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
