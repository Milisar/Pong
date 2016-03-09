package Pong;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Player {

	private double x;
	private double y;
	
	private BufferedImage player;
		
	public Player(double x, double y, Main game){
		this.setX(x);
		this.setY(y);
		
	}
	
	public void tick(){
		
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
	
	
	
}
