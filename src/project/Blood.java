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

public class Blood extends Sprite{
	public final static int BLOOD_SIZE = 20; // blood size is 20
	private final static Image BLOOD_IMAGE = new Image("images/BloodDrop.png",Blood.BLOOD_SIZE, Blood.BLOOD_SIZE, true,true);
	private Map map;


	Blood(double x, double y, Map map, Denji denji){
		super(x,y);
		this.speed = denji.getSpeed(); // speed for panning of blood is set to player's speed
		this.loadImage(Blood.BLOOD_IMAGE);
		this.map = map;
	}

	public void pan(){
		if(!map.getAtEdge()){ // pans the blood drop while the map is not yet showing the edge
			this.x += this.dx;
			this.y += this.dy;
		}
	}




}
