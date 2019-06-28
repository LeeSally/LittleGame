package GameAirCaft;

import java.awt.Color;
import java.awt.Font;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent; 
import java.awt.event.MouseMotionListener;
import java.util.ArrayList; 

import javax.swing.JFrame;
import javax.swing.JLabel; 
import javax.swing.SwingConstants;

public class SkyGround{
	private JFrame FrmSky;
	private MyPlane Me; 
	private boolean isOver = false;
	private JLabel lbl_Stop;
	private ArrayList<FlyObj> FlyObjList = new ArrayList<FlyObj>();
	
	public static void main(String[] args) {
		new SkyGround();
	}

	
	SkyGround(){
		FrmSky = new JFrame("Air war");
		FrmSky.setSize(500, 600);
		FrmSky.setBackground(Color.BLUE); 
		FrmSky.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		FrmSky.setLayout(null); 

		Me = new MyPlane(50,500);
		FrmSky.add(Me.getObj());
		
		new RemoveObj();
		
		FrmSky.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				if(KeyEvent.getKeyText(e.getKeyCode())=="Space" && !isOver) {
					new Thread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							int count = 0;
							
							while(count++ < 3) { 
								try {
									Thread.sleep(50);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
								Bullet curB = new Bullet(Me.getX(),Me.getY(),1,5);
								FlyObjList.add(curB);
								
								FrmSky.add(curB.getObj());
							}
						}
						
					}).start();
				}
			}
			
			
			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub
			}
			
		});
		
		FrmSky.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseDragged(MouseEvent arg0) {
				// TODO Auto-generated method stub
				moveMe();
			}

			@Override
			public void mouseMoved(MouseEvent arg0) {
				// TODO Auto-generated method stub
				moveMe();
			}
			
		});

		FrmSky.repaint();
		FrmSky.setVisible(true); 
		
		new BornEnemy1();
		new BornEnemy3();
	}
	
	
	JFrame getSky() {
		return FrmSky;
	}
	
	
	class Bullet extends FlyObj implements Runnable{
		
		public Bullet(int x, int y, int size, int speed) {
			// TODO Auto-generated constructor stub 
			super(x - size*5/2,y - size*5/2,size*5,size*5,1,speed);
			theObj.setBackground(Color.GRAY);
			new Thread(this).start();
		}
		
		
		public void run() {
			// TODO Auto-generated method stub
 
			int curY = Me.getY(); 
			while(curY > 0) {
				try {
					if(speed == 5) {
						Thread.sleep(3);
					}else if(speed == 3) {
						Thread.sleep(5);
					}else if(speed == 1) {
						Thread.sleep(10);
					}
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(this.isDie) {
					break;
				}
				
				curY -= 1;
				theObj.setLocation(theObj.getX(),curY);
				 
				double disX;
				double disY;
				
				for(int i = 0; i < FlyObjList.size();i++) {
					try {
						if(FlyObjList.get(i).getClass().equals(EnemyPlane.class)) {
							
							disX =  this.getX() - FlyObjList.get(i).getX();
							disY =  this.getY() - FlyObjList.get(i).getY();
							
							if(Math.pow(disX, 2) + Math.pow(disY, 2) < Math.pow(FlyObjList.get(i).getWidth(),2) + 25) {
								synchronized(this){
									this.die();
									FlyObjList.get(i).cutLife();
									break;
								} 
							}
							
						}
					}catch(NullPointerException e) {
						System.out.println("null element: " + e.getMessage());
						continue;						
					}catch(IndexOutOfBoundsException e) {
						System.out.println("Index over: " + e.getMessage());
						continue;						
					}
				}
			}

			synchronized(this){
				this.die();
				
				if(isOver) {
					gameOver();
				}
			}
		}

	}

	
	class RemoveObj implements Runnable{  
		
		RemoveObj() {
			new Thread(this).start();
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub 
			while(!isOver) {
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				try {
					for(int i = 0; i < FlyObjList.size();i++) {
						synchronized(this) {
							if(FlyObjList.get(i)!=null) {
								if(FlyObjList.get(i).isDie) {
//									System.out.println("Remove fly object..." + FlyObjList.size());
									FlyObjList.remove(i);
								}
							}
						}
					} 
				}catch(NullPointerException e) {
					System.out.println("null value: " + e.getMessage());
					continue;						
				}catch(IndexOutOfBoundsException e) {
					System.out.println("Index over: " + e.getMessage());
					continue;						
				}
				
			}			
		}
	}
	
	
	public void gameOver() {
		isOver = true;
		
		
		Me.die();
		Me.theObj.setVisible(true);
		
		lbl_Stop = new JLabel("GAME OVER");
		lbl_Stop.setFont(new Font("Calibri",Font.BOLD,30));
		lbl_Stop.setBackground(Color.GRAY);
		lbl_Stop.setForeground(Color.WHITE);
		lbl_Stop.setOpaque(true);
		lbl_Stop.setHorizontalAlignment(SwingConstants.CENTER);
		
		lbl_Stop.setBounds(FrmSky.getWidth()/2 -150 , FrmSky.getHeight()/2 -50,300,50);
		lbl_Stop.setVisible(true);
		FrmSky.add(lbl_Stop);
		FrmSky.repaint(); 
	}


	class BornEnemy1 implements Runnable {
		
		public BornEnemy1() {
			new Thread(this).start();
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub 
			while(true) { 
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				int curX; 
				curX = (int) (Math.random() * FrmSky.getWidth());
				
				EnemyPlane curE = new EnemyPlane(curX,2,1,5);
				FrmSky.add(curE.getObj());
				FlyObjList.add(curE);
			}
		}
		
	}
	
	
	
	class BornEnemy3 implements Runnable{
		
		public BornEnemy3() {
			new Thread(this).start();
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			while(true) { 
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				int curX; 
				curX = (int) (Math.random() * FrmSky.getWidth());
				
				EnemyPlane curE = new EnemyPlane(curX,4,3,1);
				FrmSky.add(curE.getObj());
				FlyObjList.add(curE);
			}
		}
		
	} 
	
	
	public void moveMe() {
		
		if(!isOver) {
			Point MousePos= MouseInfo.getPointerInfo().getLocation(); 
			JFrame TargetSky= this.getSky(); 
			double targetX;
			double targetY;

			
			if(MousePos.getX() < TargetSky.getX()) {
				targetX =  Me.getWidth();
			}else if(MousePos.getX() > TargetSky.getX() + TargetSky.getWidth() -  Me.getWidth() ) {
				targetX =  TargetSky.getWidth() - Me.getWidth()/2 - 8;
			}else {
				targetX = MousePos.getX() - TargetSky.getX() - Me.getWidth()/2 - 8;
			} 
			
			
			if(MousePos.getY() < TargetSky.getY()) {
				targetY = Me.getHeight();
			}else if(MousePos.getY()  > TargetSky.getY() + TargetSky.getHeight() -  Me.getHeight()) {
				targetY = TargetSky.getHeight() - Me.getHeight()/2 - 30 ;
			}else {
				targetY = MousePos.getY() -TargetSky.getY() - Me.getHeight()/2 -30;
			} 
			
			Me.getObj().setLocation((int)targetX,(int)targetY);
		}
	}
	
	
	class MyPlane extends FlyObj {
		
		public MyPlane(int x, int y) {
			super(x, y, 30, 30, 1, 0);
			// TODO Auto-generated constructor stub
			theObj.setBackground(Color.red);
		}
	}
	
	
	class EnemyPlane extends FlyObj implements Runnable{
		
		public EnemyPlane(int x, int size, int life, int speed) {
			super(x-size*5/2, 0, size*5, size*5, life, speed);
			// TODO Auto-generated constructor stub
			theObj.setBackground(Color.black);
			new Thread(this).start();
		} 
		
		
		@Override
		public synchronized void die() {
			// TODO Auto-generated method stub
			super.die();
			this.theObj.setLocation(0,0);
		}


		@Override
		public void run() {
			// TODO Auto-generated method stub
 
			int curY = 0; 
			while(curY < FrmSky.getHeight()- 50) {
				try {
					if(speed == 5) {
						Thread.sleep(3);
					}else if(speed == 3) {
						Thread.sleep(5);
					}else if(speed == 1) {
						Thread.sleep(10);
					}
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(this.isDie) {
					break;
				}
				
				curY += 1;

				theObj.setLocation(theObj.getX(),curY);
				
				if(!isOver){
					double disX;
					double disY;
					
					disX =  theObj.getX() - Me.getX();
					disY =  theObj.getY() - Me.getY();
					
					synchronized(this){
						if(Math.pow(disX, 2) + Math.pow(disY, 2) < Math.pow(Me.getWidth(),2) + 25 ) {
							isOver = true;
							break;
						}
					}
				}
			}
			
			synchronized(this){
				this.die();
				
				if(isOver) {
					gameOver();
				}
			} 
		}
	}

}


