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

public class Map extends Sprite{
	// attributes
	private final static double MAP_SIZE = 2400;
	private final static Image MAP_IMAGE = new Image("images/GameAreaBG.png", Map.MAP_SIZE, Map.MAP_SIZE, true,true);

	// initial position of map so that window will show the center of the map
	private final static double INIT_X = -800;
	private final static double INIT_Y = -800;

	private boolean atEdge; // true if window shows edge of map

	Map(){
		super(INIT_X, INIT_Y);
		this.speed = 3;
		this.loadImage(Map.MAP_IMAGE);
		this.atEdge = false;
	}

	// pan method to change position of map shown in the window
	public void pan(){
		if(this.x+this.dx >= -1600 && this.x+this.dx <= 0 && this.y+this.dy >= -1600 && this.y+this.dy <= 0){
			this.atEdge = false;
			this.x += this.dx;
			this.y += this.dy;
			//System.out.println("x Map is: " + this.x);
			//System.out.println("y Map is: " + this.y);
		}else{
			this.atEdge = true; // stop panning of map when already at edge
		}
	}

	// getter
	boolean getAtEdge(){
		return this.atEdge;
	}

}
