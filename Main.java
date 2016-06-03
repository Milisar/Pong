package Pong;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class Main extends Canvas implements Runnable {
	
	private double playerScore = 0;
	private double enemyScore = 0;
	
	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 320;
	public static final int HEIGHT = WIDTH / 12 * 9;
	public static final int SCALE = 2;
	public final String TITLE = "Pong";
	
	private boolean running = false;
	private boolean inMenu = true;
	private boolean inHelp = false;
	private Thread thread;
	
	private int pHEIGHT = 100;
	private int pWIDTH = 20;
	private int bHEIGHT = 10;
	private int bWIDTH = 10;
	private int eHEIGHT = 100;
	private int eWIDTH = 20;
	private double menuCursor = 0;
	private double singleplayer = 0;
	private double multiplayer = 0;
	private double ballHit;	
	
	private BufferedImage image = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
	
	private Player p;
	private Ball b;
	private Enemy e;
	
	public void init(){
		requestFocus();
		addKeyListener(new KeyInput(this));
		
		p = new Player(20, getHeight()/2 - pHEIGHT/2, this);
		b = new Ball(getWidth()/2 - bWIDTH/2 ,getHeight()/2 - bHEIGHT/2, this);
		e = new Enemy(getWidth()- eWIDTH -20, getHeight()/2 - eHEIGHT/2, this);
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
				render();
				frames++;
				updates++;
				delta--;
			}
			
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
		if(!inMenu){
			p.tick();
			b.tick();
			e.tick();
		}
		
		//BallHit
		if(b.getY() + bHEIGHT >= p.getY() && (b.getY()) <= (p.getY()+pHEIGHT)){
			ballHit =(b.getY()+bHEIGHT/2)-(p.getY()+pHEIGHT/2);
		}else if(b.getY() + bHEIGHT >= e.getY() && (b.getY()) <= (e.getY() +eHEIGHT)){
			ballHit =(b.getY()+bHEIGHT/2)-(e.getY()+eHEIGHT/2);
		}
		
		//Collision Ball-Player
		if( (p.getX()+pWIDTH) >= b.getX() && (b.getY() +bHEIGHT >= p.getY() && b.getY() <= p.getY() + pHEIGHT)){
				b.setVelX(-(b.getVelX()+1));
				b.setVelY((ballHit*b.getVelX())/45);	
		}

		//Collision Ball-Enemy
		if((e.getX() <= b.getX() + bWIDTH) && (b.getY() <= (e.getY()+eHEIGHT)&&(b.getY() + bHEIGHT - 1 >=e.getY()))){
				b.setVelX(-(b.getVelX()));
				b.setVelY(-(ballHit*b.getVelX())/45);	
		}
		
		//Enemy AI
		if(singleplayer == 1){
			if(b.getY() + bHEIGHT / 2 != e.getY() + eHEIGHT / 2 && b.getY() <= e.getY() + eHEIGHT && b.getY() + bHEIGHT >= e.getY()){
				e.setVelY(-b.getVelY());
			}else if(b.getY() > e.getY() + eHEIGHT){
				e.setVelY(-5);
			}else if(b.getY() < e.getY()){
				e.setVelY(5);
			}
		
			if(e.getVelY() >= 5){
			e.setVelY(5);
			}
			if(e.getVelY() <= -5){
				e.setVelY(-5);
			}
		}
		//Score Player
		if(b.getX() >= e.getX() + 10){
			playerScore ++;
			b.setVelX(2);
			b.setVelY(0);
			b.setX(getWidth()/2 - bWIDTH/2);
			b.setY(getHeight()/2 - bHEIGHT/2);
			p.setY(getHeight()/2 - pHEIGHT/2);
			e.setY(getHeight()/2 - eHEIGHT/2);
		}
		
		//Score Enemy
		if(b.getX() <= 10){
			enemyScore ++;
			b.setVelX(2);
			b.setVelY(0);
			b.setX(getWidth()/2 - bWIDTH/2);
			b.setY(getHeight()/2 - bHEIGHT/2);
			p.setY(getHeight()/2 - pHEIGHT/2);
			e.setY(getHeight()/2 - eHEIGHT/2);
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
	
		if(!inMenu && !inHelp)	{	
			//player rendering
			g.drawRect((int)p.getX(), (int)p.getY(), pWIDTH, pHEIGHT);
			g.fillRect((int)p.getX(), (int)p.getY(), pWIDTH, pHEIGHT);
			//ball rendering
			g.drawRect((int)b.getX(), (int)b.getY(), bWIDTH, bHEIGHT);
			g.fillRect((int)b.getX(), (int)b.getY(), bWIDTH, bHEIGHT);
			//enemy rendering
			g.drawRect((int)e.getX(), (int)e.getY(), eWIDTH, eHEIGHT);
			g.fillRect((int)e.getX(), (int)e.getY(), eWIDTH, eHEIGHT);
			//score
			g.setFont(new Font("default", Font.BOLD, 16));
			String s = (int)playerScore + "    -    " + (int)enemyScore;
			g.drawString(s, (getWidth() / 2) - (s.length() * g.getFont().getSize()) / 4, 20);
		}else if(inMenu){
			//menu
			g.setFont(new Font("default", Font.BOLD, 16));
			String d ="Singleplayer";
			String a ="MultiPlayer";
			String h ="Controls";
			String c ="->";
			g.drawString(d, (getWidth()/2), getHeight()/2 - 40);
			g.drawString(a, (getWidth()/2), getHeight()/2);
			g.drawString(h, (getWidth()/2), getHeight()/2 + 40);
			if(menuCursor == 0){
				g.drawString(c, ((getWidth()/2) - 50), (getHeight()/2) - 40);
			}else if(menuCursor == 1){
				g.drawString(c, ((getWidth()/2) - 50), (getHeight()/2));
			}else if(menuCursor == 2){
				g.drawString(c, ((getWidth()/2) - 50), (getHeight()/2) + 40);
			}
		}else if(inHelp){
			g.setFont(new Font("default", Font.BOLD, 16));
			String a ="Singleplayer          Up Arrow         Down Arrow          Esc";
			String b ="Multiplayer p1           W key                 S key                Esc";
			String f ="Multiplayer p2        Up Arrow         Down Arrow         Esc";
			String c ="Up";
			String d ="Down";
			String e ="Reset";
			String h ="Backspace to go";
			String i ="back to menu";
			g.drawString(b, 75, 275);
			g.drawString(a, 75, 175);
			g.drawString(f, 75, 375);
			g.drawString(c, 245, 75);
			g.drawString(d, 375, 75);
			g.drawString(e, 490, 75);
			g.drawString(h, 65, 75);
			g.drawString(i, 65, 100);

			
		}
		
		g.dispose();
		bs.show();
		
	}
	
	public void keyPressed(KeyEvent q){
		int key =q.getKeyCode();
		
		if(key == KeyEvent.VK_DOWN){
			if(!inMenu){
				if(singleplayer == 1){
					p.setVelY(5);
				}else if(multiplayer == 1){
					e.setVelY(5);
				}
			}else{
				if(menuCursor == 2){
					menuCursor = 0;
				}else{
					menuCursor ++;
				}
			}
		}else if(key == KeyEvent.VK_UP){
			if(!inMenu){
				if(singleplayer == 1){
					p.setVelY(-5);
				}else if(multiplayer == 1){
					e.setVelY(-5);
				}		
			}else{
				if(menuCursor == 0){
					menuCursor = 2;
				}else{
					menuCursor --;
				}
			}
		}
		if(key == KeyEvent.VK_W){
			if(multiplayer == 1){
				p.setVelY(-5);
			}
		}
		if(key == KeyEvent.VK_S){
			if(multiplayer == 1){
				p.setVelY(5);
			}
		}
		if(key == KeyEvent.VK_ESCAPE){
			if(!inMenu && !inHelp){
			b.setVelX(2);
			b.setVelY(0);
			b.setX(getWidth()/2 - bWIDTH/2);
			b.setY(getHeight()/2 - bHEIGHT/2);
			p.setY(getHeight()/2 - pHEIGHT/2);
			e.setY(getHeight()/2 - eHEIGHT/2);
			}
		}
		if(key == KeyEvent.VK_ENTER){
			if(inMenu){
				if(menuCursor == 0){
					singleplayer = 1;
					multiplayer = 0;
					inMenu = false;
				}else if(menuCursor == 1){
					multiplayer = 1;
					singleplayer = 0;
					inMenu = false;
				}else if(menuCursor == 2){
					multiplayer = 0;
					singleplayer = 0;
					inMenu = false;
					inHelp = true;
				}
			}
		}
	}
	
	public void keyReleased(KeyEvent q){
		int key =q.getKeyCode();
	
		if(key == KeyEvent.VK_DOWN){
			if(multiplayer == 1){
				e.setVelY(0);
			}else if(singleplayer == 1){
				p.setVelY(0);
			}
		}else if(key == KeyEvent.VK_UP){
			if(multiplayer == 1){
				e.setVelY(0);
			}else if(singleplayer == 1){
				p.setVelY(0);
			}
		}
		if(key == KeyEvent.VK_W){
			if(multiplayer == 1){
				p.setVelY(0);
			}
		}
		if(key == KeyEvent.VK_S){
			if(multiplayer == 1){
				p.setVelY(0);
			}
		}
		if(key == KeyEvent.VK_BACK_SPACE){
			if(!inMenu){
			inMenu = true;
			singleplayer = 0;
			multiplayer = 0;
			inHelp = false;
			b.setVelX(2);
			b.setVelY(0);
			b.setX(getWidth()/2 - bWIDTH/2);
			b.setY(getHeight()/2 - bHEIGHT/2);
			p.setY(getHeight()/2 - pHEIGHT/2);
			e.setY(getHeight()/2 - eHEIGHT/2);
			}
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