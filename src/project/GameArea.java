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

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.Iterator;

public class GameArea extends AnimationTimer {

	// attributes
	private GraphicsContext gc;
	private Denji denji;
	private Stage stage;
	private Scene scene;
	private Map map;
	private long startSpawn;
	private ArrayList<Devil> devils;
	private ArrayList<Blood> bloodDrops;
	private int min;
	private int sec;
	private MainGame mainGame;
	private PowerUp powerUp;
	private final static Image statusImg = new Image("images/PlayerStats.png", 411, 141, true, true);
	private final static Image pochitaTimerImg = new Image("images/PochitaTimer.png", 304, 80, true, true);
	private final static Image chainsawTimerImg = new Image("images/ChainsawTimer.png", 304, 80, true, true);
	@SuppressWarnings("unused")
	private int denjiGotDevil;
	private int newBloodCollected = 0;
	private int firstSpawn;

    public final static int NUM_DEVILS = 10;
    public final static int DEVIL_TYPES = 5;
    public final static int NUM_BLOOD = 25; // divided by 2 to increase difficulty level

    GameArea(Stage stage, Scene scene, GraphicsContext gc, MainGame mainGame) {
        //Initializes stage and scene
    	this.stage = stage;
    	this.gc = gc;
    	this.scene = scene;
    	this.startSpawn = System.nanoTime(); // starting time the sprite spawned
		this.mainGame = mainGame;

		//Initializes game elements
		this.min = 0; // minutes
    	this.sec = 0; // seconds
    	this.denjiGotDevil = 0; // initialized to 0
    	this.map = new Map(); // creates Map object
    	this.denji = new Denji("Denji"); // creates player
    	this.devils = new ArrayList<Devil>(); // creates arrayList of devils (enemy)
    	this.bloodDrops = new ArrayList<Blood>(); // creates arrayList of blood (food)
    	this.spawnDevils(); // spawns devils, blood drops, and power-up
    	this.spawnBlood();
    	this.spawnPU();
    	this.firstSpawn = 0; // initialized to 0 so that power up will only show after 10 seconds in the game
    	this.handleKeyPressEvent(); // calls method to pan images when WASD is pressed
    }

    @Override
	public void handle(long currentNanoTime){

    	// clears canvas
    	this.gc.clearRect(0, 0, MainGame.MAP_SIZE, MainGame.MAP_SIZE);

    	// computes number of seconds elapsed
 		long currentSec = TimeUnit.NANOSECONDS.toSeconds(currentNanoTime);
 		long startSec = TimeUnit.NANOSECONDS.toSeconds(startSpawn);
 		long timeAlive = currentSec - startSec;

 		this.min = (int)timeAlive/60; // calculates minutes elapsed
 		this.sec = (int)timeAlive-(this.min*60); // calculates the seconds elapsed

    	// checks if player or devil collides with blood drop
    	Iterator<Blood> bloodIter = this.bloodDrops.iterator();
    	while(bloodIter.hasNext()){ // iterates over blood drops
    		Blood bloodDrops = bloodIter.next();

    		if(this.denji.collidesWith(bloodDrops)){ // blood drop collides with player
    			bloodIter.remove(); // removes one blood drop in the arrayList
    			this.denji.incrementBloodCollected();
    			this.newBloodCollected = 1;
    			continue;
    		}

	    	for(int i =0; i < this.devils.size(); i++){ // iterates over all devils
	    		Devil devil = this.devils.get(i);
	    		if(devil.collidesWith(bloodDrops)){
	    			bloodIter.remove(); // changes size and speed of devil
	    			devil.updateSizeSpeed(Blood.BLOOD_SIZE/2); // increments enemy blob size with blood size
	    			break; // stops for loop
	    		}
	    	}

    	}

    	// checks if player collides with devil
    	this.denjiDevilCollision();

    	// checks if devil collides with another devil
    	this.devilDevilCollision();

    	// spawns another blood drop when player collects a blood drop
    	if(this.bloodDrops.size()!=GameArea.NUM_BLOOD){
    		while(this.bloodDrops.size()!=GameArea.NUM_BLOOD){
        		spawnOneBlood(); // spawn one blood drop
    		}

			// increases size and decreases speed of player
    		if(this.newBloodCollected==1){ // player collected blood
    			this.updateDenjiBloodMap(Blood.BLOOD_SIZE/2);
    		}

    	}

    	// shows game over scene when player's size is more than window size
    	// OR when player gets eaten by a larger blob
    	if(!denji.isAlive()||denji.getCurrentSize()>MainGame.WINDOW_SIZE){
    		this.stop();
    		this.displayGameOverScene();
    	}


    	// pans the map according to WASD user input
    	this.map.pan();

    	for(int i =0; i < this.devils.size(); i++){
    		Devil d = this.devils.get(i);
    		d.pan(); // changes position of devils
    	}

    	for(int i =0; i < this.bloodDrops.size(); i++){
    		Blood b = this.bloodDrops.get(i);
    		b.pan(); // changes position of blood drops
    	}

    	for(int i =0; i < this.devils.size(); i++){
    		Devil d = this.devils.get(i);
    		d.autoMove(currentNanoTime); // moves devils randomly for a random number of seconds
    	}

    	this.powerUp.autoSpawn(currentNanoTime); // spawns power-up for 5 seconds

    	if(this.powerUp.getNewSpawn()==true){
    		this.spawnPU(); // spawns new random power up in window every 10 seconds
    	}

    	this.powerUp.pan(); // changes position of power up
    	this.denji.move(); // moves

    	// renders image elements in canvas
    	this.map.render(this.gc); // renders map
    	this.renderBlood(); // renders food
    	this.renderDevils(); // renders enemy blobs
    	this.denji.render(this.gc); // renders player blob

    	this.renderPU(); // renders random power up
    	this.showStats(); // displays the score and time elapsed
    	this.showPowerUpTime(); // displays power up time if applicable

    }

    private void showStats(){
    	// shows current game stats
    	this.gc.drawImage(GameArea.statusImg, 10, 10); // image of the game stat names
    	this.gc.setFill(Color.WHITE); // white font color
		this.gc.setFont(Font.font("Verdana", FontWeight.BOLD, 20)); // sets font style to Verdana and bold font weight
		this.gc.fillText(this.min+"m "+this.sec+"s", 265, 120); // adds text for time elapsed in minutes and seconds
 		this.gc.fillText(""+this.denji.getDevilsDefeated(), 265, 43); // adds text for number of devils defeated
 		this.gc.fillText(""+this.denji.getBloodCollected(), 265, 67); // adds text for blood drops collected
 		this.gc.fillText(""+this.denji.getCurrentSize(), 265, 93); // adds text for current size of player
    }

    private void showPowerUpTime(){ // shows timer depending on power up

    	if(this.denji.getSpeedBoost()==true){ // Denji has Chainsaw Power Up
        	// shows remaining power up time
        	this.gc.drawImage(GameArea.chainsawTimerImg, 475, 10); // draws image of chainsaw timer
        	this.gc.setFill(Color.WHITE); // white font color
    		this.gc.setFont(Font.font("Verdana", FontWeight.BOLD, 20)); // sets font style to Verdana and bold font weight
    		ChainsawPU chainsaw = (ChainsawPU) this.powerUp;
    		this.gc.fillText(chainsaw.getTimer().getTime()+"s", 712, 68); // adds text for time remaining

    	}else if(this.denji.immortal()==true){ // Denji has Pochita Power Up
        	// shows remaining power up time
        	this.gc.drawImage(GameArea.pochitaTimerImg, 465, 10); // draws image of chainsaw timer
        	this.gc.setFill(Color.WHITE); // white font color
    		this.gc.setFont(Font.font("Verdana", FontWeight.BOLD, 20)); // sets font style to Verdana and bold font weight
    		PochitaPU pochita = (PochitaPU) this.powerUp;
    		this.gc.fillText(pochita.getTimer().getTime()+"s", 713, 68); // adds text for time remaining

    	}

    }

    private void devilDevilCollision(){ // method for collision between devils
    	ArrayList<Devil> toRemove = new ArrayList<Devil>(); // initializes array of devils to remove since it is smaller than devil collided with

    	Iterator<Devil> devilIter = this.devils.iterator(); // creates iterator to iterate over all spawned devils

    	while(devilIter.hasNext()){ // iterates over all devils
    		Devil devil = devilIter.next();
    		Iterator<Devil> devilIter2 = this.devils.iterator(); // creates another iterator to iterate over all spawned devils

    		while(devilIter2.hasNext()){ // iterates over all devils -- iterator inside an iterator
        		Devil devil2 = devilIter2.next();

        		if(devil==devil2)continue; // skips iteration if they are the same devil (devil can't collide with itself)

        		else if(devil.collidesWith(devil2)){ // if devil collides with another devil

        			if(devil.getCurrentSize()>devil2.getCurrentSize()){ // if first devil is larger -- second devil will be removed
        				if(!toRemove.contains(devil2)){ // if second devil is not yet in toRemove array
            				toRemove.add(devil2); // adds second devil to array
            				devil.updateSizeSpeed(devil2.getCurrentSize()); // updates the size and speed of first devil

            				System.out.println("Devil Current Size is now: " + devil.getCurrentSize());
        				}
        			}

        			else if(devil2.getCurrentSize()>devil.getCurrentSize()){ // if second devil is larger -- first devil will be removed
        				if(!toRemove.contains(devil)){ // if first devil is not yet in the toRemove array
            				toRemove.add(devil); // adds first devil to array
            				devil2.updateSizeSpeed(devil.getCurrentSize()); // updates the size and speed of second devil
            				System.out.println("Devil2 Current Size is now: " + devil2.getCurrentSize());
        				}
        			}

        		}



        	}

    	}

    	this.devils.removeAll(toRemove);

    	if(this.devils.size() < GameArea.NUM_DEVILS){
    		spawnOneDevil();
    		//System.out.println("DEVIL DEVIL - numDevils is " + this.devils.size());
    	}

    }

    private void updateDenjiBloodMap(double addSize){
    	this.denji.updateSizeSpeed(addSize);
		// update panning speed of map and blood drops
		map.setSpeed(denji.getSpeed());

    	for(int i =0; i < this.bloodDrops.size(); i++){
    		Blood b = this.bloodDrops.get(i);
    		b.setSpeed(denji.getSpeed());
    	}

		this.newBloodCollected=0; // set back to 0
    }

    private void denjiDevilCollision(){
    	Iterator<Devil> devilIter = this.devils.iterator();
    	while(devilIter.hasNext()){
    		Devil devil = devilIter.next();
    		if(this.denji.collidesWith(devil)){
    			// player defeats devil when devil is smaller
    			if(this.denji.getCurrentSize()>devil.getCurrentSize()){
    				this.updateDenjiBloodMap(devil.getCurrentSize());  // divided by 2 for moderate difficulty
    				this.denji.incrementDevilsDefeated();
    				devilIter.remove();
    				this.denjiGotDevil = 1; // checker to spawn a devil
    			}else if(devil.getCurrentSize()>this.denji.getCurrentSize()){
    				// devil is larger than denji
					if (this.denji.immortal()==false){ //checks if denji has pochita powerup
						this.denji.die(); // denji dies - game is over
					}
    			}
    		}
    	}

    	if(this.devils.size() < GameArea.NUM_DEVILS){ // code to spawn another devil
    		this.spawnOneDevil();
    		this.denjiGotDevil = 0; // set back to 0
    		System.out.println("DENJI GOT DEVIL - NUM DEVILS is " + this.devils.size());
    	}

    }

    private void displayGameOverScene(){ // displays game over scene
		GameOverScene gameOver = new GameOverScene(denji,min,sec, mainGame);
		this.stage.setScene(gameOver.getScene());
    }

    private void spawnOneDevil(){ // spawns one devil
    	Random r = new Random();
    	double x = r.nextInt(((int)map.getX()+MainGame.MAP_SIZE-Devil.INIT_SIZE)-(int)map.getX())+(int)map.getX();
    	double y = r.nextInt(((int)map.getY()+MainGame.MAP_SIZE-Devil.INIT_SIZE)-(int)map.getY())+(int)map.getY();
    	this.devils.add(new Devil(x,y, this.map));
    	//System.out.println("Devil "+ i + " has spawned.");
    }


    private void spawnOneBlood(){ // spawns one blood drop
    	Random r = new Random();
    	double x = r.nextInt(((int)map.getX()+MainGame.MAP_SIZE-Blood.BLOOD_SIZE)-(int)map.getX())+(int)map.getX();
    	double y = r.nextInt(((int)map.getY()+MainGame.MAP_SIZE-Blood.BLOOD_SIZE)-(int)map.getY())+(int)map.getY();
		this.bloodDrops.add(new Blood(x,y, this.map, this.denji));

    }


    private void spawnBlood(){ // generates food or blood drops
    	Random r = new Random();
    	for(int i =0; i < GameArea.NUM_BLOOD; i++){
    		int x = r.nextInt((1600-Blood.BLOOD_SIZE)+MainGame.WINDOW_SIZE)-MainGame.WINDOW_SIZE;
    		int y = r.nextInt((1600-Blood.BLOOD_SIZE)+MainGame.WINDOW_SIZE)-MainGame.WINDOW_SIZE;
    		this.bloodDrops.add(new Blood(x,y, this.map, this.denji));
    		//System.out.println("Blood Drop "+ i + " has spawned.");
    	}
    }


    private void renderBlood(){ // renders blood drops
    	for(int i =0; i < this.bloodDrops.size(); i++){
    		Blood b = this.bloodDrops.get(i);
    		b.render(this.gc);
    	}
    }


    private void panBlood(KeyCode ke){ // pans each blood drop according to movement of Denji
    	for(int i = 0; i < this.bloodDrops.size(); i++){
    		Blood b = this.bloodDrops.get(i);
        	if(ke==KeyCode.W){
        		b.setDY(b.getSpeed());
        		//System.out.println("B " + i + "changed speed");
        	}

        	if(ke==KeyCode.A){
        		b.setDX(b.getSpeed());
        	}

        	if(ke==KeyCode.S){
        		b.setDY(-b.getSpeed());
        	}

        	if(ke==KeyCode.D){
        		b.setDX(-b.getSpeed());
        	}
        	//System.out.println(ke+" key pressed. [BLOOD]");

    	}

    }


    private void stopBlood(KeyCode ke){ // stops blood drops when player stops moving
    	for(int i = 0; i < this.bloodDrops.size(); i++){
    		Blood b = this.bloodDrops.get(i);
        	b.setDX(0);
        	b.setDY(0);
    	}

    }


    private void spawnDevils(){ // spawns devils at random areas
    	Random r = new Random();
    	for(int i=0; i < GameArea.NUM_DEVILS; i++){
    		int x = r.nextInt(1600-Devil.INIT_SIZE+MainGame.WINDOW_SIZE)-MainGame.WINDOW_SIZE;
    		int y = r.nextInt(1600-Devil.INIT_SIZE+MainGame.WINDOW_SIZE)-MainGame.WINDOW_SIZE;
    		this.devils.add(new Devil(x,y, this.map));
    		//System.out.println("Devil "+ i + " has spawned.");
    	}
    }

    private void renderDevils(){ // renders spawned devils
    	for(Devil d: this.devils){
    		d.render(this.gc);
    	}
    }


    private void panDevils(KeyCode ke){ // pans the devils when player moves across the map
    	for(int i = 0; i < this.devils.size(); i++){
    		Devil d = this.devils.get(i);
        	if(ke==KeyCode.W){
        		d.setDY(this.denji.getSpeed());
        	}

        	if(ke==KeyCode.A){
        		d.setDX(this.denji.getSpeed());
        	}

        	if(ke==KeyCode.S){
        		d.setDY(-this.denji.getSpeed());
        	}

        	if(ke==KeyCode.D){
        		d.setDX(-this.denji.getSpeed());
        	}
    	}

    }


    private void stopDevils(KeyCode ke){ // stops panning of devils when WASD isn't pressed
    	for(int i = 0; i < this.devils.size(); i++){
    		Devil d = this.devils.get(i);
        	d.setDX(0);
        	d.setDY(0);
    	}

    }

	private void spawnPU(){ // spawns a random power-up either Pochita or Chainsaw
    	Random r = new Random();

    	// spawn in a random position inside the window
    	int x = r.nextInt(MainGame.WINDOW_SIZE-PowerUp.PU_SIZE);
    	int y = r.nextInt(MainGame.WINDOW_SIZE-PowerUp.PU_SIZE);

    	int random = r.nextInt(2);

		if (random==0){ // randomizes power-up to be spawned
			this.powerUp = new PochitaPU(x,y, this.map, this); // 0 - Pochita Immunity Power-Up
		}
		else {
			this.powerUp = new ChainsawPU(x,y,this.map, this); // 1 - Chainsaw Speed Power-Up
		}

    }

     private void renderPU(){ //renders the random power-up to the canvas
    	 if(this.powerUp.getVisible()&&this.firstSpawn!=0){
    		 this.powerUp.render(this.gc);
    		 this.powerUp.checkCollision(this.denji);
    	 }
     }


     private void panPU(KeyCode ke){ // pans the power-up whenever player moves
         	if(ke==KeyCode.W){
         		this.powerUp.setDY(this.denji.getSpeed());
         	}

         	if(ke==KeyCode.A){
         		this.powerUp.setDX(this.denji.getSpeed());
         	}

         	if(ke==KeyCode.S){
         		this.powerUp.setDY(-this.denji.getSpeed());
         	}

         	if(ke==KeyCode.D){
         		this.powerUp.setDX(-this.denji.getSpeed());
         	}
    }


     private void stopPU(KeyCode ke){ // stops panning of power-up when player stops moving
         	this.powerUp.setDX(0);
         	this.powerUp.setDY(0);
     }



    private void handleKeyPressEvent(){ // handle method for panning and stopping movement of images whenever WASD is pressed
    	scene.setOnKeyPressed(new EventHandler<KeyEvent>(){
    		public void handle(KeyEvent e){
    			KeyCode code = e.getCode();
    			moveDenji(code);
    			panMap(code);
    			panBlood(code);
    			panDevils(code);
    			panPU(code);
    		}
    	});

    	scene.setOnKeyReleased(new EventHandler<KeyEvent>(){
    		public void handle(KeyEvent e){
    			KeyCode code = e.getCode();
    			stopDenji(code);
    			stopMap(code);
    			stopBlood(code);
    			stopDevils(code);
    			stopPU(code);
    		}
    	});
    }

    private void moveDenji(KeyCode ke){ // moves player whenever
    	if(ke==KeyCode.W){
    		//System.out.println("denji speed is " + denji.getSpeed());
    		this.denji.setDY(-denji.getSpeed());
    	}

    	if(ke==KeyCode.A){
    		this.denji.setDX(-denji.getSpeed());
    	}

    	if(ke==KeyCode.S){
    		this.denji.setDY(denji.getSpeed());
    	}

    	if(ke==KeyCode.D){
    		this.denji.setDX(denji.getSpeed());
    	}

    	//System.out.println(ke+" key pressed. [DENJI]");
    }

    private void stopDenji(KeyCode ke){ // stops movement of player when WASD isn't pressed
    	this.denji.setDX(0);
    	this.denji.setDY(0);
    }

    private void panMap(KeyCode ke){ // pans map
    	if(ke==KeyCode.W){
    		this.map.setDY(map.getSpeed());
    	}

    	if(ke==KeyCode.A){
    		this.map.setDX(map.getSpeed());
    	}

    	if(ke==KeyCode.S){
    		this.map.setDY(-map.getSpeed());
    	}

    	if(ke==KeyCode.D){
    		this.map.setDX(-map.getSpeed());
    	}

    	//System.out.println(ke+" key pressed. [MAP]");
    }

    private void stopMap(KeyCode ke){ // stops movement of map
    	this.map.setDX(0);
    	this.map.setDY(0);
    }

    // getters and setters:

    long getStartSpawn(){
    	return this.startSpawn;
    }

    Map getMap(){
    	return this.map;
    }

    void setFirstSpawn(int x){
    	this.firstSpawn = x;
    }


}

