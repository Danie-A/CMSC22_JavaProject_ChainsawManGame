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

import java.util.Random;
import java.util.concurrent.TimeUnit;
import javafx.scene.image.Image;

public class Devil extends Sprite {

	 // attributes
     private int type;
     private boolean alive;
	 private double currentSize;
     private Map map;

	 // five different types of devils
     private final static int MAX_TYPE = 5;
     private final static Image BAT_DEVIL = new Image("images/Bat Devil.png", Devil.INIT_SIZE, Devil.INIT_SIZE, true, true);
     private final static Image ETERNITY_DEVIL = new Image("images/Eternity Devil.png", Devil.INIT_SIZE, Devil.INIT_SIZE, true, true);
     private final static Image LEECH_DEVIL = new Image("images/Leech Devil.png", Devil.INIT_SIZE, Devil.INIT_SIZE, true, true);
     private final static Image TOMATO_DEVIL = new Image("images/Tomato Devil.png", Devil.INIT_SIZE, Devil.INIT_SIZE, true, true);
     private final static Image ZOMBIE_DEVIL = new Image("images/Zombie Devil.png", Devil.INIT_SIZE, Devil.INIT_SIZE, true, true);
     private Image devilImage;

     // for random movement of devil
     private int moveType;
     private int moveTime;
     private long startMove;

     public final static int INIT_SIZE = 40;
     public final static int MAX_MOVE_TIME = 4;
     public final static int MAX_MOVE_TYPE = 4;

     // constructor
     Devil(double x, double y, Map map){
    	 super(x,y);
    	 this.alive = true;
    	 this.speed = 3; // initialize speed to 3
    	 this.currentSize = Devil.INIT_SIZE;
    	 this.startMove = System.nanoTime();

    	 // random devil type
    	 Random r = new Random();
    	 this.type = r.nextInt(Devil.MAX_TYPE)+1;
    	 if(this.type==1){
    		 this.devilImage = Devil.BAT_DEVIL;
    	 }else if(this.type==2){
    		 this.devilImage = Devil.ETERNITY_DEVIL;
    	 }else if(this.type==3){
    		 this.devilImage = Devil.LEECH_DEVIL;
    	 }else if(this.type==4){
    		 this.devilImage = Devil.TOMATO_DEVIL;
    	 }else{
    		 this.devilImage = Devil.ZOMBIE_DEVIL;
    	 }

    	// initializes random move type and move time
   		this.moveTime = r.nextInt(Devil.MAX_MOVE_TIME)+1;
   		//System.out.println("START moveTime is" + this.moveTime);
   		this.moveType = r.nextInt(Devil.MAX_MOVE_TYPE)+1;
   		//System.out.println("START moveType is" + this.moveType);

    	 this.loadImage(this.devilImage);
    	 this.map = map;

     }

     // pan method when player moves
     void pan(){
  		if(!map.getAtEdge()){
 			this.x += this.dx;
 			this.y += this.dy;
 			//System.out.println("Devil Pan Works");
  		}
     }

     // autoMove method so that devil moves for a specific number of seconds
     void autoMove(long currentNanoTime){


    	// current time
     	long currentSec = TimeUnit.NANOSECONDS.toSeconds(currentNanoTime);

     	// start time
     	long startSec = TimeUnit.NANOSECONDS.toSeconds(this.startMove);

     	long moveElapsedTime = currentSec - startSec;
    	//System.out.println("moveElapsedTime is"+moveElapsedTime);

     	// if elapsed time is equal to move time
    	if(moveElapsedTime == this.moveTime){
    		// generates new random move time and move type
 	       	 Random r = new Random();
 	       	 this.moveTime = r.nextInt(Devil.MAX_MOVE_TIME)+1;
 	       	 this.moveType = r.nextInt(Devil.MAX_MOVE_TYPE)+1;
 	       	 this.startMove = System.nanoTime(); // initializes start time
 	   	}else{
 	   		this.move(this.moveType); // calls move method to change position of devil in the map
 	   	}

     }

     // randomly changes position of devil
     void move(int moveType){
   		 if(moveType==1){ // move to the left
   			 if(map.getX()<=this.x-this.speed){
   				 this.x -= this.speed;
   			 }
   			 //System.out.println("move to left");
   		 }else if(moveType==2){ // right
   			 if(map.getX()+MainGame.MAP_SIZE>=this.x+this.width+this.speed){
   				 this.x += this.speed;

   			 }
   			//System.out.println("move to right");
   		 }else if(moveType==3){ // up
   			 if(map.getY()<=this.y-this.speed){
   				 this.y -= this.speed;
   			 }
   			//System.out.println("move up");
   		 }else{ // down
   			 if(map.getY()+MainGame.MAP_SIZE>=this.y+this.width+this.speed){
   				 this.y += this.speed;
   			 }
   			//System.out.println("move down");
   		 }

     }

    // updates the size and speed of the devil
    void updateSizeSpeed(double size){

    	this.currentSize += size; // increases size of devil

        // changes speed of devil
        this.speed = 120/this.currentSize; // decreases the speed once size is increased

	   	 if(this.type==1){
			 this.devilImage = new Image("images/Bat Devil.png", this.currentSize, this.currentSize, true, true);
		 }else if(this.type==2){
			 this.devilImage = new Image("images/Eternity Devil.png", this.currentSize, this.currentSize, true, true);
		 }else if(this.type==3){
			 this.devilImage = new Image("images/Leech Devil.png", this.currentSize, this.currentSize, true, true);
		 }else if(this.type==4){
			 this.devilImage = new Image("images/Tomato Devil.png", this.currentSize, this.currentSize, true, true);
		 }else{
			 this.devilImage = new Image("images/Zombie Devil.png", this.currentSize, this.currentSize, true, true);
		 }

	   	this.loadImage(this.devilImage);

    	double rightmost = this.x+this.width;
    	double bottom = this.y+this.width;

    	// changes position when size is increased at the edge of map
    	// so that enemy blob won't move past the map
    	if(rightmost > map.getX()+MainGame.MAP_SIZE){
    		//System.out.println("change denji x");
    		this.x -= (rightmost-map.getX()+MainGame.MAP_SIZE); // moves to the left when at the right edge

    	}

    	if(bottom > map.getY()+MainGame.MAP_SIZE){
    		//System.out.println("change denji y");
    		this.y -= (map.getY()+MainGame.MAP_SIZE); // moves up when at the bottom of map
    	}


    }


 	void checkCollision(Denji denji){ // Collision with Denji
         if(this.collidesWith(denji)){
         	if (denji.getCurrentSize()>this.getCurrentSize()){
                 denji.updateSizeSpeed(this.getCurrentSize()); // Increases player size
                 this.die(); // Devil dies
 				System.out.println("devil dies");
             }
             else if (denji.getCurrentSize()<this.getCurrentSize()){
                 if (denji.immortal()==false){ // Checks if Denji is immortal at that time
                     denji.die(); // Player dies if Denji is mortal
                 }

             }
     	}
    }

 	void checkCollision(Blood blood){
         if(this.collidesWith(blood)){
         	this.increaseSize(10); // Increases size by 10 when devil eats blood
 			System.out.println("devil eats blood");
     	}
     }

	double getCurrentSize(){
        return this.currentSize;
    }

	private void die(){
		this.vanish();
	}

 	void increaseSize(double size){
        this.currentSize += size;
        this.speed = 120/this.speed;

		if(this.type==1){
			this.devilImage = new Image("images/Bat Devil.png", this.currentSize, this.currentSize, true, true);
		}
		else if(this.type==2){
			this.devilImage = new Image("images/Eternity Devil.png", this.currentSize, this.currentSize, true, true);
		}
		else if(this.type==3){
			this.devilImage = new Image("images/Leech Devil.png", this.currentSize, this.currentSize, true, true);
		}
		else if(this.type==4){
			this.devilImage = new Image("images/Tomato Devil.png", this.currentSize, this.currentSize, true, true);
		}
		else{
			this.devilImage = new Image("images/Zombie Devil.png", this.currentSize, this.currentSize, true, true);
		}

        this.loadImage(this.devilImage);
        System.out.println("DEVIL size increase");

		double rightmost = this.x+this.width;
    	double bottom = this.y+this.width;

    	// changes position when size is increased at the edge of map
    	if(rightmost > MainGame.WINDOW_SIZE){
    		//System.out.println("change denji x");
    		this.x -= (rightmost-MainGame.WINDOW_SIZE);

    	}

    	if(bottom > MainGame.WINDOW_SIZE){
    		//System.out.println("change denji y");
    		this.y -= (bottom-MainGame.WINDOW_SIZE);
    	}

    }

     public boolean isAlive(){
    	 return this.alive;
     }


}
