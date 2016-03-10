package Pong;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Player {

	private double x;
	private double y;
	
	private double velX = 0;
	private double velY = 0;
	
	private int pHEIGHT = 100;
	private int pWIDTH = 20;
	
	private BufferedImage Player;
		
	public Player(double x, double y, Main game){
		this.x = x;
		this.y = y;
		
		
	}
	
	public void tick(){
		x+=velX;
		y+=velY;
		
		if(y <= 0){
			y = 0;
		}
		if(y >= 480 - pHEIGHT - 13){
			y = 480 - pHEIGHT - 13;
		}
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}
	
	public void setVelX(double velX){
		this.velX = velX;
	}
	
	public void setVelY(double velY){
		this.velY = velY;
	}
	
}
