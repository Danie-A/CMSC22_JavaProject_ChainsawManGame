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

// Chainsaw Power Up: grants temporary two times speed increase boost for player

public class ChainsawPU extends PowerUp {

	 // attributes
     private PowerUpTimer timer;
     private final static Image IMAGE_SPRITE = new Image("images/Chainsaw.png", PowerUp.PU_SIZE, PowerUp.PU_SIZE, true, true);

     ChainsawPU (double x, double y, Map map, GameArea gameArea){
         //Super constructor
         super(x, y, map, ChainsawPU.IMAGE_SPRITE, gameArea);

     }

     void checkCollision(Denji denji){ // code when player collides with a power up
         if(this.collidesWith(denji)){
        	System.out.println("Denji Collides with Chainsaw Speed");

        	this.visible=false; // sets visibility to false so that it wouldn't be rendered on canvas

            // activates speed boost
        	denji.setSpeedBoost(true);
        	System.out.println("Denji Speed Before Is:" + denji.getSpeed());
        	// increases player speed
        	denji.setSpeed(denji.getSpeed()*2); // sets player speed two times faster
 			System.out.println("Denji Speed Is Now:" + denji.getSpeed());

            // changes image as visual cue for player
 			denji.setChainsawImage();

 			// starts timer
            this.timer = new PowerUpTimer(denji, 1); // 1 for ChainsawPU, 0 for PochitaPU
            this.timer.start();


 		}
     }

     public PowerUpTimer getTimer(){
     	return this.timer;
     }

}