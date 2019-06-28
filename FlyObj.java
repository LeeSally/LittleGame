package GameAirCaft;

import java.awt.Color;
import javax.swing.JPanel;

public abstract class FlyObj {
	
	protected JPanel theObj;
	protected boolean isDie = false; 
	protected int LifeVal = 1;
	protected int speed = 1;
	
	FlyObj(int x, int y, int weight, int height, int life, int speed){
		this.LifeVal = life;
		this.speed = speed;
		theObj = new JPanel();
		theObj.setBounds(x - weight/2 ,y - height/2,weight,height);
		theObj.setBackground(Color.red);
		theObj.setVisible(true);		
	}
	
	
	JPanel getObj() {
		return theObj;
	}
	
	public void hide() {
		theObj.setVisible(false);
	}
	
	public int getX() {
		return theObj.getX() + theObj.getWidth()/2;
	}
	
	public int getWidth() {
		return theObj.getWidth();
	}
	
	public int getHeight() {
		return theObj.getHeight();
	}
	
	public int getY() {
		return theObj.getY() + theObj.getHeight()/2;
	}
	
	public synchronized void cutLife() {
		this.LifeVal--;
		if(this.LifeVal==0) {
			die();
		}
	}

	
	public synchronized void die() {
		isDie = true; 
		int count = 0; 
		
		synchronized(this) {
			while(count++ < 3) {
				try {
					Thread.sleep(30);
					theObj.setVisible(false);
					
					Thread.sleep(30);
					theObj.setVisible(true);
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			}
			this.getObj().setVisible(false);
		}
	} 
}
