package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable, KeyListener {
	
	public static final int width=400;
	public static final int height=400;
	
	private Thread thread;
	private boolean running;
	private long targetTime;
	
	private int score;
	private int level;
	private boolean gameover;
	
	private final int size=10;
	Entity head,apple;
	ArrayList<Entity> snake;
	private Graphics2D g2D;
	private BufferedImage image;
	
	private int dx,dy;
	private boolean up,down,left ,right,start;
	public GamePanel() {
		setPreferredSize(new Dimension(width,height));
		setFocusable(true);
		requestFocus();
		addKeyListener(this);
	}
	private void setFPS(int fps) {
		targetTime = 1000/fps;
	}
	public void addNotify() {
		super.addNotify();
		thread=new Thread(this);
		thread.start();
	}
	@Override
	public void keyPressed(KeyEvent e) {
		int k=e.getKeyCode();
		
		if(k==KeyEvent.VK_UP)up=true;
		if(k==KeyEvent.VK_DOWN)down=true;
		if(k==KeyEvent.VK_LEFT)left=true;
		if(k==KeyEvent.VK_RIGHT)right=true;
		if(k==KeyEvent.VK_ENTER)start=true;
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
	int k=e.getKeyCode();
		
		if(k==KeyEvent.VK_UP)up=false;
		if(k==KeyEvent.VK_DOWN)down=false;
		if(k==KeyEvent.VK_LEFT)left=false;
		if(k==KeyEvent.VK_RIGHT)right=false;
		if(k==KeyEvent.VK_ENTER)start=false;

	}

	@Override
	public void keyTyped(KeyEvent e) {
		

	}

	@Override
	public void run() {
		if(running)return;
		init();
		long startTime;
		long elapsed;
		long wait;
		while(running) {
			startTime=System.nanoTime();
			
			update();
			Render();
			elapsed=System.nanoTime()-startTime;
			wait=targetTime-elapsed/1000000;
			
			if(wait>0) {
				try {
					Thread.sleep(wait);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	public void init() {
		image=new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
		g2D=image.createGraphics();
		running =true;
		setUpLevel();
		gameover=false;
		level=1;
		setFPS(level*10);
	}
	public void setUpLevel() {
		snake=new ArrayList<Entity>();
		head=new Entity(size);
		head.setPosition(width/2, height/2);
		snake.add(head);
	
		for(int i=1;i<3;i++) {
			Entity e =new Entity(size);
			e.setPosition(head.getX()+(i*size),head.getY());
			snake.add(e);
		}
		apple=new Entity(size);
		setApple();
		score=0;
	}
	
	public void setApple() {
		int x=(int)(Math.random()*(width-size));
		int y=(int)(Math.random()*(height-size));
		apple.setPosition(x, y);
	}
	private void Render() {
		render(g2D);
		Graphics g= getGraphics();
		g.drawImage(image,0,0,null);
		g.dispose();
	}
	private void update() {
		if(gameover) {
			if(start) {
				setUpLevel();
			}
			return;
		}
		if(up && dy==0) {
			dy=-size;
			dx=0;
		}
		if(down && dy==0) {
			dy=size;
			dx=0;
		}
		if(left && dx==0) {
			dx=-size;
			dy=0;
		}
		if(right && dx==0 && dy!=0) {
			dx=size;
			dy=0;
		}
		if(dx!=0 || dy!=0) { 
				for(int i=snake.size()-1;i>0;i--) {
				snake.get(i).setPosition(snake.get(i-1).getX(),
				snake.get(i-1).getY());
			}
			head.move(dx, dy);
		}
		for(Entity e : snake) {
			if(e.isCollision(head)) {
				gameover=true;
				break;
			}
		}
		if(apple.isCollision(head)) {
			score++;
			setApple();
			Entity e =new Entity(size);
			
			snake.add(e);
		if(score%10==0) {
			level++;
			
			setFPS(level*10);
		}
		}
		
		if(head.getX()<0)head.setX(width);
		if(head.getY()<0)head.setY(height);
		if(head.getX()>width)head.setX(0);
		if(head.getX()>height)head.setY(0);
	}
	private void render(Graphics2D g2d2) {
		g2D.clearRect(0, 0, width, height);
		g2D.setColor(Color.BLUE);
		for(Entity e:snake) {
			e.render(g2D);
		}
		g2D.setColor(Color.red);
		apple.render(g2D);
		if(gameover) {
			g2D.drawString("game over", 150, 200);
		}
		g2D.setColor(Color.WHITE);
		g2D.drawString("Score :"+score + " Level :"+level, 10,10);
		if(dx==0 && dy==0) {
			g2D.drawString("READY", 150, 200);
		}
	}

}
