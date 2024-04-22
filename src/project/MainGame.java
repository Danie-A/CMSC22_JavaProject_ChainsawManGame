/*************************************************************************************************************************
 *
 * CMSC 22 Object-Oriented Programming
 * Final Project
 *
 * Problem Domain:
 * This game is inspired by an ongoing anime entitled, Chainsaw Man.
 *
 * The player is Denji (yellow player blob), a half-human and half-devil, who tries to achieve his life goals by defeating devil monsters (dark brown enemy blobs).
 *
 * In order to defeat them, the player is tasked to collect as many blood drops as possible (small red circles) in order to increase Denji's size so that he can defeat other monsters who are smaller than him.
 *
 * 2 power-ups can be collected by Denji:
 * [1] Pochita Power-Up 		 - player will be immune to monsters
 * [2] Chainsaw Power-Up 	     - player's speed will increase two times faster
 *
 * If a devil monster eats Denji because it is larger than him, then, the game will end.
 *
 * (c) Institute of Computer Science, CAS, UPLB
 * @authors Danielle Araez and Zaina Guzman
 * @date 2022-12-08
 *************************************************************************************************************************/

package project;

import java.util.stream.Stream;

import javafx.scene.paint.Color;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainGame {
	// attributes
	Stage stage;
	private Scene scene;
	private Scene splashScene;
	private Scene creditScene;
	private Scene instructScene;
	private Group root;
	private Canvas canvas;
	private GraphicsContext gc;
	private GameArea gameArea;
	public static final int WINDOW_SIZE = 800; // window size is fixed to 800 by 800
	public static final int MAP_SIZE = 2400;

	//the class constructor
	public MainGame() {
		// initialize stage elements for game area
		this.root = new Group();
		this.scene = new Scene(root, MainGame.WINDOW_SIZE,MainGame.WINDOW_SIZE,Color.CADETBLUE);
		this.canvas = new Canvas(MainGame.MAP_SIZE,MainGame.MAP_SIZE);
		this.root.getChildren().add(canvas);
		this.gc = canvas.getGraphicsContext2D();

	}

	//method to add the stage elements for splash, instruction, and credit scenes
	public void setStage(Stage stage) {
		this.stage = stage; // initialize stage
		this.stage.setTitle("Chainsaw Man Blob Game"); // set title of the game
		this.initSplash(stage);			// initializes the Splash Screen with the needed buttons
		this.initInstruct(stage); // initializes Instructions Screen
		this.initCredit(stage); // initializes Credits Screen

		this.stage.setScene(this.splashScene); // set splash scene as the first thing user sees once game is run
        this.stage.setResizable(false); // set window as unresizable - fixed 800 by 800 window
		this.stage.show(); // show the set up stage elements
	}

	// function for setting game area
	void setGame(Stage stage) {
		this.gameArea = new GameArea(this.stage, this.scene, this.gc, this);
        stage.setScene(this.scene); // change scene to the main game scene
        gameArea.start(); // calls the handle() method of game area
	}


	// initializes splash stage
	private void initSplash(Stage stage) {

		// creates stackpane where splash elements will be added to
		StackPane root = new StackPane();
        root.getChildren().addAll(this.createSplashCanvas(),this.createSplashVBox()); // creates canvas and vbox for the splash screen
        this.splashScene = new Scene(root); // creates new Scene for the splash scene

		// adds css style for buttons
		String css = this.getClass().getResource("project.css").toExternalForm();
		this.splashScene.getStylesheets().add(css);
	}

	// draws splash background image in splash canvas
	private Canvas createSplashCanvas() {
    	Canvas canvas = new Canvas(MainGame.WINDOW_SIZE,MainGame.WINDOW_SIZE);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        Image splashBG = new Image("images/splashBG.jpg");
        gc.drawImage(splashBG, 0, 0, MainGame.WINDOW_SIZE, MainGame.WINDOW_SIZE);
        return canvas;
    }

	// creates vbox layout pane for splash scene - where buttons will be added
    private VBox createSplashVBox() {
    	VBox vbox = new VBox();
        vbox.setAlignment(Pos.BOTTOM_LEFT); // set alignment to bottom left
        vbox.setPadding(new Insets(10,10,90,220)); // specify padding of vbox for better positioning of buttons
        vbox.setSpacing(12); // set spacing of buttons

		// creates buttons for the different game features
        Button startButton = new Button("Start Game");
		Button instructButton = new Button("Instructions");
        Button creditButton = new Button("Credits");
		Button exitButton = new Button("Exit");

		// apply same css style for all the buttons created
		Stream.of(startButton, instructButton, creditButton,exitButton).forEach(button -> button.getStyleClass().add("splashButton"));

		// add all the buttons in the vbox layout pane
        vbox.getChildren().addAll(startButton, instructButton, creditButton, exitButton);

		// calls methods to change the scenes when button is clicked through event handlers
		this.whenExitIsClicked(exitButton);
		this.whenCreditIsClicked(creditButton);
		this.whenInstructIsClicked(instructButton);

		// sets the game area scene when start button is clicked
        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                setGame(stage);		// Changes the scene into the game scene
            }
        });

        return vbox;
    }

	// initializes instruction scene
	private void initInstruct(Stage stage) {
		StackPane root = new StackPane();
        root.getChildren().addAll(this.createInstructCanvas(),this.createBackVBox());
        this.instructScene = new Scene(root);
		String css = this.getClass().getResource("project.css").toExternalForm();
		this.instructScene.getStylesheets().add(css);
	}

	// creates canvas for instruction scene where the background is placed
	private Canvas createInstructCanvas() {
    	Canvas canvas = new Canvas(MainGame.WINDOW_SIZE,MainGame.WINDOW_SIZE);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        Image instructBG = new Image("images/instructBG.jpg");
        gc.drawImage(instructBG, 0, 0, MainGame.WINDOW_SIZE, MainGame.WINDOW_SIZE);
        return canvas;
    }

	// initializes credit scene
	private void initCredit(Stage stage) {
		StackPane root = new StackPane();
        root.getChildren().addAll(this.createCreditCanvas(),this.createBackVBox());
        this.creditScene = new Scene(root);
		String css = this.getClass().getResource("project.css").toExternalForm();
		this.creditScene.getStylesheets().add(css);
	}

	// creates canvas where credit background is placed
	private Canvas createCreditCanvas() {
    	Canvas canvas = new Canvas(MainGame.WINDOW_SIZE,MainGame.WINDOW_SIZE);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Image creditBG = new Image("images/creditBG.jpg");
        gc.drawImage(creditBG, 0, 0, MainGame.WINDOW_SIZE, MainGame.WINDOW_SIZE);
        return canvas;
    }

	// creates a Vbox for the back button in the instruction and credit scene
    private VBox createBackVBox() {
    	VBox vbox = new VBox();
        vbox.setAlignment(Pos.TOP_LEFT); // set alignment to top left
        vbox.setPadding(new Insets(30,10,10,30)); // specify positioning of the button
        vbox.setSpacing(12);

		// creates new back button
        Button backButton = new Button("Back");
		backButton.getStyleClass().add("backButton");

		// creates image view for the back icon image
		Image img = new Image("images/backIcon.png", 30, 30, true,true);
		ImageView view = new ImageView(img);

		// changes the size of the image
		view.setFitHeight(30);
		view.setPreserveRatio(true); // preserves ratio so image would not stretch
		backButton.setGraphic(view); // places and displays the back icon image inside the button

		// adds back button to vbox
        vbox.getChildren().addAll(backButton);

		// calls method so it will go back to the splash scene when the back button is clicked through an event handler
		this.whenBackIsClicked(backButton);
        return vbox;
    }


	// program will close when exit button is clicked
	private void whenExitIsClicked(Button button){
		button.setOnMouseClicked(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent arg0){
				System.exit(0); // application will exit when exit button is clicked
			}
		});
	}

	// goes back to the splash scene when the back button is clicked
	private void whenBackIsClicked(Button button){
		button.setOnMouseClicked(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent arg0){
				// goes back to splash scene
				stage.setScene(splashScene);
			}
		});
	}

	// goes to the instruction scene when instruction button is clicked in the splash scene
	private void whenInstructIsClicked(Button button){
		button.setOnMouseClicked(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent arg0){
				// goes to instruction scene
				stage.setScene(instructScene);
			}
		});
	}

	// goes to the credit scene when credit button is clicked in the splash scene
	private void whenCreditIsClicked(Button button){
		button.setOnMouseClicked(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent arg0){
				// goes to credits scene
				stage.setScene(creditScene);
			}
		});
	}

}