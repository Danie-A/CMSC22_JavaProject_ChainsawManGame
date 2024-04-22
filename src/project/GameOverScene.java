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

 import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
 import javafx.scene.canvas.Canvas;
 import javafx.scene.canvas.GraphicsContext;
 import javafx.scene.control.Button;
 import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
 import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
 import javafx.scene.text.Font;
 import javafx.scene.text.FontWeight;

 public class GameOverScene{
	 // attributes
	 private Scene scene;
	 private StackPane pane;
	 private VBox vbox;
	 private GraphicsContext gc;
	 private Canvas canvas;
	 private Image gameOverBG;
	 private MainGame mainGame;


	 GameOverScene(Denji denji, int min, int sec, MainGame mainGame){

		 // set up canvas and graphics context
		 this.canvas = new Canvas(MainGame.WINDOW_SIZE, MainGame.WINDOW_SIZE);
		 this.gc = canvas.getGraphicsContext2D();
		 this.mainGame = mainGame;

		 // set up game over background in canvas
		 if(denji.getCurrentSize()>=MainGame.WINDOW_SIZE){
			 this.gameOverBG = new Image("images/GameOverWon.png", MainGame.WINDOW_SIZE, MainGame.WINDOW_SIZE, true, true);
		 }else{
			 this.gameOverBG = new Image("images/GameOverLost.png", MainGame.WINDOW_SIZE, MainGame.WINDOW_SIZE, true,true);
		 }

        // creates stack pane for placing canvas and vbox
        this.pane = new StackPane();
		 // displays game summary
    	this.gc.drawImage(this.gameOverBG, 0, 0);
    	this.gc.setFill(Color.WHITE);
		this.gc.setFont(Font.font("Verdana", FontWeight.BOLD, 22));
 		this.gc.fillText(""+denji.getDevilsDefeated(), 630, 657);
 		this.gc.fillText(""+denji.getBloodCollected(), 630, 687);
 		this.gc.fillText(""+denji.getCurrentSize(), 630, 717);
		this.gc.fillText(min+"m "+sec+"s", 630, 747);

		// creates vbox for placing exit button
		this.vbox = new VBox();
        this.vbox.setAlignment(Pos.TOP_LEFT); // set alignment to top left
        this.vbox.setPadding(new Insets(30,10,10,30)); // specify positioning of the button
        this.vbox.setSpacing(12);

		 // creates home button
		Button homeButton = new Button("Home"); // creates button
		this.whenHomeIsClicked(homeButton); // event handler when home button is clicked
		homeButton.getStyleClass().add("backButton");
 		homeButton.setStyle("-fx-pref-width: 140px;");

		// creates image view for the back icon image
		Image img = new Image("images/HomeIcon.png", 20, 20, true,true);
		ImageView view = new ImageView(img);

		// changes the size of the image
		view.setFitHeight(20);
		view.setPreserveRatio(true); // preserves ratio so image would not stretch
		homeButton.setGraphic(view); // places and displays the home icon image inside the button


		// creates exit button
 		Button exitButton = new Button("Exit"); // creates button
 		this.whenExitIsClicked(exitButton); // event handler when exit button is clicked
 		exitButton.getStyleClass().add("backButton");
 		exitButton.setStyle("-fx-pref-width: 140px;");

		// creates image view for the back icon image
		Image img2 = new Image("images/ExitIcon.png", 20, 20, true,true);
		ImageView view2 = new ImageView(img2);

		// changes the size of the image
		view2.setFitHeight(20);
		view2.setPreserveRatio(true); // preserves ratio so image would not stretch
		exitButton.setGraphic(view2); // places and displays the exit icon image inside the button


 		// places exit and home button in vbox
 		this.vbox.getChildren().addAll(homeButton, exitButton);

 		// places canvas and vbox to the stack pane
        this.pane.getChildren().addAll(this.canvas,this.vbox);

        // places stack pane in the scene
        this.scene = new Scene(this.pane);

        // access the CSS file for styling the button
        String css = this.getClass().getResource("project.css").toExternalForm();
		this.scene.getStylesheets().add(css);




	 }

	 // getter
	 Scene getScene(){
		 return this.scene;
	 }


	// program will close when exit button is clicked
	private void whenExitIsClicked(Button button){
		button.setOnMouseClicked(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent arg0){
				System.exit(0); // application will exit when exit button is clicked
			}
		});
	}

	// program will return to main menu when home button is clicked
	private void whenHomeIsClicked(Button button){
		button.setOnMouseClicked(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent arg0){
				mainGame.setStage(mainGame.stage); // application will set MainGame stage when home button is clicked
			}
		});
	}


 }

