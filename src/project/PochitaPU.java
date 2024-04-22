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

// Pochita Power Up: Grants temporary immortality
public class PochitaPU extends PowerUp {
	// attributes
	private PowerUpTimer timer;
    private final static Image IMAGE_SPRITE = new Image("images/Pochita.png", PowerUp.PU_SIZE, PowerUp.PU_SIZE, true, true);

    PochitaPU(double x, double y, Map map, GameArea gameArea){
        //Super constructor
        super(x, y, map, PochitaPU.IMAGE_SPRITE, gameArea);

    }


    void checkCollision(Denji denji){ // checks collision of power up with player
        if(this.collidesWith(denji)){

            System.out.println("Denji Collides with Pochita");

        	this.visible = false; // set visibility to false

        	// makes player immortal - immune to monsters
			denji.setImmortal(true);

            // Changes image to indicate power-up
			denji.setPochitaImage();

            this.timer = new PowerUpTimer(denji, 0); // 0 for PochitaPU, 1 for ChainsawPU
            this.timer.start(); // starts five-second timer

		}
    }

    public PowerUpTimer getTimer(){
    	return this.timer;
    }



}