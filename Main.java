package Pong;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import Pong.Player;

public class Main extends Canvas implements Runnable {
	
	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 320;
	public static final int HEIGHT = WIDTH / 12 * 9;
	public static final int SCALE = 2;
	public final String TITLE = "Pong";
	
	private boolean running = false;
	private Thread thread;
	
	private int pHEIGHT = 100;
	private int pWIDTH = 20;
	private int bHEIGHT = 10;
	private int bWIDTH = 10;
	private int eHEIGHT = 100;
	private int eWIDTH = 20;
	private double ballHit;
	private boolean score = false;
	
	
	private BufferedImage image = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
	
	private Player p;
	private Ball b;
	private Enemy e;
	
	public void init(){
		requestFocus();
		addKeyListener(new KeyInput(this));
		
		p = new Player(20, 150, this);
		b = new Ball(200 ,200, this);
		e = new Enemy(591, 100, this);
	}
	private synchronized void start(){
		if (running)
			return;
		
		running = true;
		thread = new Thread(this);
		thread.start();
	}
	
	private synchronized void stop(){
		if(!running){
			return;
		}
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.exit(1);
		
	}
	
	public void run(){
		init();
		long lastTime = System.nanoTime();
		final double Ticks = 60.0;
		double ns = 1000000000 / Ticks;
		double delta = 0;
		int updates = 0;
		int frames = 0;
		long timer = System.currentTimeMillis();
		while(running){
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if(delta >= 1){
				tick();
				updates++;
				delta--;
			}
			render();
			frames++;
			
			if(System.currentTimeMillis() - timer > 1000){
				timer += 1000;
				System.out.println(updates + " Ticks, Fps " + frames);
				
				updates = 0;
				frames = 0;
			}
		}
		stop();
	}
	private void tick() {
		
		System.out.println(b.getX());
		p.tick();
		b.tick();
		e.tick();
		
		//BallHit
		if(b.getY() >= p.getY() && (b.getY()+bHEIGHT) <= (p.getY()+pHEIGHT)){
			ballHit =(b.getY()+bHEIGHT/2)-(p.getY()+pHEIGHT/2);
		}		
		
		//Collision Ball-Player
		if( (p.getX()+pWIDTH) >= b.getX()){
			if( (b.getY()+bHEIGHT) <= (p.getY()+pHEIGHT+bHEIGHT-1)&&(b.getY()>=p.getY()-bHEIGHT))
			b.setVelX(-(b.getVelX()+1));
			b.setVelY(-(ballHit*6)/45);	
			}

		//Collision Ball-Enemy
		if((e.getX() <= b.getX() + bWIDTH)){
			if(b.getY() <= (e.getY()+eHEIGHT)&&(b.getY()>=e.getY()-eHEIGHT))
			b.setVelX(-b.getVelX());
			b.setVelY(-(ballHit*6)/45);	
		}
		
		//Enemy AI
		if(b.getY() + bHEIGHT / 2 != e.getY() + eHEIGHT / 2){
			e.setVelY(-b.getVelY());
		}
		
		//Scoring
		if(b.getX() <= 0 || b.getX() >= 600){
			score = true;
		}
		if(score = true){
			System.out.println("point");
		}
	}	
	
	private void render(){
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null){
			createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		
		g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
		g.setColor(Color.WHITE);
	
				
		//player rendering
		g.drawRect((int)p.getX(), (int)p.getY(), pWIDTH, pHEIGHT);
		g.fillRect((int)p.getX(), (int)p.getY(), pWIDTH, pHEIGHT);
		//ball rendering
		g.drawRect((int)b.getX(), (int)b.getY(), bWIDTH, bHEIGHT);
		g.fillRect((int)b.getX(), (int)b.getY(), bWIDTH, bHEIGHT);
		//enemy rendering
		g.drawRect((int)e.getX(), (int)e.getY(), eWIDTH, eHEIGHT);
		g.fillRect((int)e.getX(), (int)e.getY(), eWIDTH, eHEIGHT);
		
		g.dispose();
		bs.show();
		
	}
	
	public void keyPressed(KeyEvent e){
		int key =e.getKeyCode();
		
		if(key == KeyEvent.VK_DOWN){
			p.setVelY(5);
		}else if(key == KeyEvent.VK_UP){
			p.setVelY(-5);
		}
		
	}
	
	public void keyReleased(KeyEvent e){
		int key =e.getKeyCode();
	
		if(key == KeyEvent.VK_DOWN){
			p.setVelY(0);
		}else if(key == KeyEvent.VK_UP){
			p.setVelY(0);
		}

	}
	
	public static void main(String args[]){
		Main game = new Main();
		
		game.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		game.setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		game.setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));

		JFrame frame = new JFrame(game.TITLE);
		frame.add(game);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		game.start();
	}

	
	
}