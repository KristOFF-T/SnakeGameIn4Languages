package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener{
	
	public boolean leftp, rightp, upp, downp;

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		
		if(code == KeyEvent.VK_LEFT) leftp = true;
		if(code == KeyEvent.VK_RIGHT) rightp = true;
		if(code == KeyEvent.VK_UP) upp = true;
		if(code == KeyEvent.VK_DOWN) downp = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		
		if(code == KeyEvent.VK_LEFT) leftp = false;
		if(code == KeyEvent.VK_RIGHT) rightp = false;
		if(code == KeyEvent.VK_UP) upp = false;
		if(code == KeyEvent.VK_DOWN) downp = false;
	}
}
