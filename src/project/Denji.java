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

import javafx.scene.image.Image;

public class Denji extends Sprite {

	// Denji attributes
	private String name;
	private boolean alive;
	private boolean speedBoost; // for ChainsawPU
    private boolean immortal; // for PochitaPU
	private double currentSize;
	private Image denjiImage;
	private int devilsDefeated;
	private int bloodCollected;

	private final static double DENJI_INIT_SIZE = 40;
    private final static Image DENJI_IMAGE = new Image("images/Denji.png", Denji.DENJI_INIT_SIZE, Denji.DENJI_INIT_SIZE, true, true);
    private final static double INIT_X = 400;
	private final static double INIT_Y = 400;
	private final static double INCREASE_SIZE = 120;

	// Denji constructor
    Denji(String name){
        //Super constructor
        super(INIT_X, INIT_Y);
		this.name = name; //initializes values
		this.alive = true;
		this.immortal = false;
		this.speedBoost = false;
		this.speed = 3; // initializes speed to 3
        this.currentSize = Denji.DENJI_INIT_SIZE; // initializes current size to 40
    	this.devilsDefeated = 0;
    	this.bloodCollected = 0;
        this.loadImage(Denji.DENJI_IMAGE);
    }

    public boolean isAlive(){
    	if(this.alive) return true;
    	return false;
    }

    public String getName(){
    	return this.name;
    }

    public boolean immortal(){
        return this.immortal;
    }

    public boolean getSpeedBoost(){
    	return this.speedBoost;
    }

    void setImmortal(boolean b){
        this.immortal = b;
    }

    void setSpeedBoost(boolean b){
    	this.speedBoost = b;
    }

    void die(){
    	this.alive = false;
    }

    // changes the x and y position of Denji as long as it is within the window
    void move(){
    	if(this.x+this.dx <= MainGame.WINDOW_SIZE-(this.width) && this.x+this.dx >= 0 && this.y+this.dy <= MainGame.WINDOW_SIZE-(this.width) && this.y+this.dy >= 0){
    		this.x += this.dx;
    		this.y += this.dy;
    	}
    }

    void incrementDevilsDefeated(){
    	this.devilsDefeated++;
    }

    void incrementBloodCollected(){
    	this.bloodCollected++;
    }

    // getters and setters:
    int getBloodCollected(){
    	return this.bloodCollected;
    }


    int getDevilsDefeated(){
    	return this.devilsDefeated;
    }

    double getCurrentSize(){
    	return this.currentSize;
    }

    void setDenjiImage(Image image){
    	this.denjiImage = image;
    }


    void setDenjiImage(){
    	this.denjiImage = new Image ("images/Denji.png", this.currentSize, this.currentSize, true, true);
        this.loadImage(this.denjiImage);
    }

    void setPochitaImage(){
        Image denjiImmortal = new Image("images/Pochita.png", this.currentSize, this.currentSize, true, true);
        this.loadImage(denjiImmortal);
    }

    void setChainsawImage(){
		Image denjiSpeed = new Image("images/Chainsaw.png", this.currentSize, this.currentSize, true, true);
		this.loadImage(denjiSpeed);
    }

    // updates the size and speed of player
    void updateSizeSpeed(double size){

    	// add size to current size
    	this.currentSize += size;

    	if(this.speedBoost==true){ // if player collected Chainsaw power up
            // changes speed of Denji
    		//System.out.println("Speed Decreased during Speed Boost");
            this.speed = 2*(Denji.INCREASE_SIZE/this.currentSize); // times 2 speed

    	}else{ // player speed is normal
            this.speed = Denji.INCREASE_SIZE/this.currentSize; // decreases the speed once size is increased
    	}

        //System.out.println("DENJI SPEED is now:" + this.speed);

    	if(this.immortal==true){ // if player collected Pochita power up
    		// increases Pochita Image size of player
    		this.setPochitaImage();
    	}
    	else if(this.speedBoost== true){
    		// increases Chainsaw Image size of player
    		this.setChainsawImage();

    	} else{
            this.setDenjiImage(); // increases Denji Image size of player
    	}

        //System.out.println("DENJI SIZE INCREASE");

    	double rightmost = this.x+this.width; // position of player's rightmost edge
    	double bottom = this.y+this.width; // position of player's bottom edge

    	// changes position when size is increased at the edge of map
    	// so that player blob won't move past the map
    	if(rightmost > MainGame.WINDOW_SIZE){ // if player moves past the right edge of map
    		//System.out.println("change denji x");
    		this.x -= (rightmost-MainGame.WINDOW_SIZE); // move position of player to the left

    	}

    	if(bottom > MainGame.WINDOW_SIZE){ // if player moves past bottom of map
    		//System.out.println("change denji y");
    		this.y -= (bottom-MainGame.WINDOW_SIZE); // move position of player upwards
    	}


    }




}

//Reference:
// - EverwingFX
// - https://github.com/omerbselvi/agario/blob/master/namnamnam/Players.java
