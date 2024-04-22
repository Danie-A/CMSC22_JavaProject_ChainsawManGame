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

import java.util.concurrent.TimeUnit;

import javafx.scene.image.Image;

// Pochita Power Up
abstract class PowerUp extends Sprite {
	// attributes
    private Map map;
    private long startSpawn;
    private boolean newSpawn;
    private GameArea gameArea;

    public final static int VISIBLE_TIME = 5;
    public final static int PU_SPAWN_DELAY = 10; // power up spawn delay is 10 seconds
    public final static int PU_SIZE = 60;

    // constructor
	PowerUp(double x, double y, Map map, Image image, GameArea gameArea){
		super(x, y);
		this.loadImage(image);
		this.map = map;
		this.gameArea = gameArea;
		this.startSpawn = System.nanoTime(); // start time when power up is spawn
		this.visible = true;
		this.newSpawn = false; // will spawn a new random power up after 10 seconds
	}

	// pan method to pan the power up when player moves
	void pan(){
		if(!map.getAtEdge()){
			this.x += this.dx;
			this.y += this.dy;
		}
	}

	// autoSpawn method to spawn the power up every 10 seconds
	void autoSpawn(long currentNanoTime){
     	long currentSec = TimeUnit.NANOSECONDS.toSeconds(currentNanoTime);
     	long startSec = TimeUnit.NANOSECONDS.toSeconds(this.startSpawn);

     	long elapsedTime = currentSec - startSec;
    	//System.out.println("PU elapsedTime is"+ elapsedTime);

    	if(elapsedTime == PowerUp.PU_SPAWN_DELAY){
    		// if 10 seconds has passed, a new power up will spawn again
    		this.newSpawn = true; // sets newSpawn to true so that another random power up will be shown
    		gameArea.setFirstSpawn(1); // setFirstSpawn to 1 so that power up would not spawn in the first second of the game
    		this.startSpawn = System.nanoTime(); // initialize startSpawn time again
    	}else if(elapsedTime == PowerUp.VISIBLE_TIME){
    		this.visible = false; // sets power up visibility to false when 5 seconds has passed
    		this.newSpawn = false; // sets newSpawn to false so that current power up would still not change
    	}else{
    		this.newSpawn = false; // sets newSpawn to false so that current power up would still not change
    	}
	}

	// getter
	boolean getNewSpawn(){
		return this.newSpawn;
	}

	// abstract method checkCollision
	abstract void checkCollision(Denji denji);

}

//Reference: EverwingFX