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

public class PowerUpTimer extends Thread {
	// attributes
	private Denji denji;
	private int time;
	private int type;
	private final static int POWER_UP_TIME = 5;

	// constructor
	PowerUpTimer(Denji denji, int type){
		this.denji = denji;
		this.time = PowerUpTimer.POWER_UP_TIME;
		this.type = type;
	}

	// counts down so that player will eventually lose power up
	private void countDown(){
		while(this.time!=0){
			try{
				Thread.sleep(1000); // 1 second
				this.time--; // subtract 1 to the time
				System.out.println("this.time is " + this.time);
			}catch(InterruptedException e){
				System.out.println(e.getMessage());
			}
		}

		if(this.type==0){
	        System.out.println("Pochita End");

	        // makes player mortal
	        denji.setImmortal(false);
	        denji.setDenjiImage();
		}else{
	        System.out.println("Chainsaw End");

	        // turns player speed back to normal
	        this.denji.setSpeed(denji.getSpeed()/2);
	        this.denji.setSpeedBoost(false);
	        this.denji.setDenjiImage();
	        System.out.println("Denji Speed Back To:" + denji.getSpeed());

		}

	}

	// getter
	public double getTime() {
    	return this.time;
	}


	@Override
    public void run(){
		this.countDown();
    }
}
