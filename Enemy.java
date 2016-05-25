package Pong;

public class Enemy {
	
	private double x;
	private double y;
	
	private double velX;
	private double velY;
	
	private int eHEIGHT = 100;
	private int eWIDTH = 20;
	
	public Enemy(double x, double y, Main game){
		this.x = x;
		this.y = y;
	}
	
	public void tick(){
		x+=velX;
		y+=velY;
		
		if(y <= 0){
			y = 0;
		}
		if(y >= 480 - eHEIGHT -13){
			y = 480 - eHEIGHT -13;
		}	
		if(velY >= 5){
			velY = 5;
		}
		if(velY <= -5){
			velY = -5;
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
	
	public double getVelX(double velX){
		return velX;
	}
	
	public double getVelY(double velY){
		return velY;
	}
	
	public void setVelX(double velX){
		this.velX = velX;
	}
	
	public void setVelY(double velY){
		this.velY = velY;
	}
	
}
