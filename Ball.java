package Pong;

public class Ball {

	private double x;
	private double y;
	
	private double bVelX = 10;
	private double bVelY = 0;
	
	private int bHEIGHT = 10;
	private int bWIDTH = 10;
	
	public Ball(double x, double y, Main game){
		this.x = x;
		this.y = y;
	}
	
	public void tick(){
		x-=bVelX;
		y-=bVelY;
	}
}
