package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable{

	public final int TS = 40;
	public final int TC = 20;
	public final int w_width = 800, w_height = 800;
	
	public int score = 0;
	
	int FPS = 9;
	Vector<Vector2f> parts = new Vector<Vector2f>();
	
	int headX = 10;
	int headY = 10;
	
	int appleX = 5;
	int appleY = 5;
	
	int xVel = 0, yVel = 0;
	
	KeyHandler keyH = new KeyHandler();
	Thread gameThread;
	
	public GamePanel() {
		this.setPreferredSize(new Dimension(w_width, w_height));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		this.addKeyListener(keyH);
		this.setFocusable(true);
	}

	public void startGameThread() {
		gameThread = new Thread(this);
		gameThread.start();
	}

	@Override
	public void run() {		
		double drawInterval = 1000000000/FPS;
		double delta = 0;
		long lastTime = System.nanoTime();
		long currentTime;

		while(gameThread != null) {
			currentTime = System.nanoTime();
			
			delta += (currentTime - lastTime) / drawInterval;
			lastTime = currentTime;
			
			if(delta >= 1) {
				update();
				repaint();
				delta = 0;
			}
		}
	}

	// draw rect by position
	public void drbp(int x, int y, Color color, Graphics g) {
		g.setColor(color);
		g.fillRect(x*TS+1, y*TS+1, TS-2, TS-2);
	}
	
	// new apple pos
	public void nap() {
		boolean valid = false;
		while(!valid) {
			valid = true;
			appleX = (int) ((Math.random() * 20));
			appleY = (int) ((Math.random() * 20));

			for(int i=0;i<parts.size();i++) {
				Vector2f sp = parts.get(i);

				if(appleX == sp.x && appleY == sp.y) {
					valid = false;
				}
			}
		}
	}
	
	public boolean checkAlive() {
		for(int i=0; i<parts.size();i++) {
			Vector2f sp = parts.get(i);
			if(headX == sp.x && headY == sp.y) {
				return true;
			}
		}
		return false;
	}
	
	// change direction
	public void chd(String direction) {
		switch(direction) {
			case "up":
				if(yVel == 0) {
					xVel = 0;
					yVel = -1;
				}
				break;
			case "down":
				if(yVel == 0) {
					xVel = 0;
					yVel = 1;
				}
				break;
			case "left":
				if(xVel == 0) {
					xVel = -1;
					yVel = 0;
				}
				break;
			case "right":
				if(xVel == 0) {
					xVel = 1;
					yVel = 0;
				}
				break;
		}
	}
	
	public void keyHandle() {
		if(keyH.upp) {
			chd("up");
		} else if(keyH.downp) { 
			chd("down");
		} else if(keyH.leftp) {
			chd("left");
		} else if(keyH.rightp) {
			chd("right");
		}
	}
	
	public void update() {
		keyHandle();
		headX += xVel;
		headY += yVel;
		
		//cmo
		if(headX < 0) headX = TC-1;
		if(headX > TC-1) headX = 0;
		if(headY < 0) headY = TC-1;
		if(headY > TC-1) headY = 0;
		
		if(checkAlive()) {
			parts.clear();
			score = 0;
			xVel = 0;
			yVel = 0;
		}
		
		// manage parts
		parts.add(new Vector2f(headX, headY));
		if(parts.size() > score+2) {
			parts.remove(0);
		}
		
		// check apple
		if(appleX == headX && appleY == headY) {
			score++;
			nap();
		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D)g;
		
		// draw parts
		for (int i=0; i<parts.size();i++) {
			Vector2f sp = parts.get(i);
			
			drbp(sp.x, sp.y, Color.blue, g2);
		}
		
		// draw head and the apple
		drbp(headX, headY, Color.white, g2);
		drbp(appleX, appleY, Color.red, g2);

		// load font
		Font customFont = new Font("Verdana", Font.BOLD, 20);
		try {
		    customFont = Font.createFont(Font.TRUETYPE_FONT, new File("FFFFORWA.TTF")).deriveFont(20f);
		    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		    ge.registerFont(customFont);
		} catch (IOException e) {
		    e.printStackTrace();
		} catch(FontFormatException e) {
		    e.printStackTrace();
		}

		// set font, color & draw score
		g2.setFont(customFont);
		g2.setColor(Color.white);
		g2.drawString("Score: " + Integer.toString(score), 20, 45);

		g2.dispose();
	}
}
