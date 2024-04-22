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

// REFERENCE from: Everwing Game Example
package project;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Sprite {
	protected Image img;
	protected double x, y, dx, dy;
	protected boolean visible;
	protected double speed;
	protected double width;
	protected double height;

	public Sprite(double xPos, double yPos){
		this.x = xPos;
		this.y = yPos;
		this.visible = true;
	}

	//method to set the object's image
	protected void loadImage(Image img){
		try{
			this.img = img;
	        this.setSize();
		} catch(Exception e){}
	}

	//method to set the image to the image view node
	void render(GraphicsContext gc){
		gc.drawImage(this.img, this.x, this.y);
    }

	//method to set the object's width and height properties
	private void setSize(){
		this.width = this.img.getWidth();
	    this.height = this.img.getHeight();
	}
	//method that will check for collision of two sprites
	public boolean collidesWith(Sprite rect2)	{
		Rectangle2D rectangle1 = this.getBounds();
		Rectangle2D rectangle2 = rect2.getBounds();
		return rectangle1.intersects(rectangle2);
	}
	//method that will return the bounds of an image
	private Rectangle2D getBounds(){
		return new Rectangle2D(this.x, this.y, this.width, this.height);
	}

	//method to return the image
	Image getImage(){
		return this.img;
	}
	//getters
	double getDX(){
		return this.dx;
	}

	double getDY(){
		return this.dy;
	}

	public double getX() {
    	return this.x;
	}

	public double getY() {
    	return this.y;
	}

	public boolean getVisible(){
		return visible;
	}

	double getSpeed(){
		return this.speed;
	}

	//setters
	public void setX(double x) {
    	this.x = x;
	}

	public void setY(double y) {
    	this.y = y;
	}

	public void setDX(double dx){
		this.dx = dx;
	}

	public void setDY(double dy){
		this.dy = dy;
	}


	public void setWidth(double val){
		this.width = val;
	}

	public void setHeight(double val){
		this.height = val;
	}

	public void setVisible(boolean value){
		this.visible = value;
	}

	public void vanish(){
		this.visible = false;
	}

	void setSpeed(double speed){
		this.speed = speed;
	}

}


