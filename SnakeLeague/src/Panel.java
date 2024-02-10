import java.awt.event.*;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.awt.FontFormatException;
import javax.swing.JPanel;
import javax.swing.*;
import java.util.Random;
import java.awt.*;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Graphics2D;

public class Panel extends JPanel implements ActionListener {
	static final int SCREEN_WIDTH = 600;
	static final int SCREEN_HEIGHT = 600;
	static final int UNIT_SIZE = 75;
	static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT) / UNIT_SIZE;
	static final int DELAY = 125;
	
	final int x[] = new int[GAME_UNITS];
	final int y[] = new int[GAME_UNITS];
	int bodyUnits = 1;
	int championsFeasted;
	int championX;
	int championY;
	Image teemo;
	Image cho;
	char direction = 'R';
	boolean moving = false;
	Timer timer;
	Random random;
	Image background;
	Font customFont;
	Image tail;
	
	Panel() {
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		background = Toolkit.getDefaultToolkit().getImage("src/images/sum rift.png");
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		
		try {
			File fontFile = new File("src/fonts/BeaufortforLOL-Regular.ttf");
			customFont = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(Font.BOLD, 40);
		} catch (IOException | FontFormatException e) {
			e.printStackTrace();
		}
		startGame();
		
	}
	
	public void startGame( ) {
		spawnChampion();
		moving = true;
		timer = new Timer(DELAY, this);
		timer.start();
		teemo = Toolkit.getDefaultToolkit().getImage("src/images/Teemo_Render (1).png");
		cho = Toolkit.getDefaultToolkit().getImage("src/images/cho face.png");
		tail = Toolkit.getDefaultToolkit().getImage("src/images/deadteemo.png");
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
		draw(g);
		
	}
	
	public void draw(Graphics g) {
		
		if(moving) {
			Graphics g2d = (Graphics2D) g;
			
			for(int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
				g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
				g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
			}
			
			g2d.drawImage(teemo, championX, championY, UNIT_SIZE, UNIT_SIZE, this);
		
			for(int i = 0; i < bodyUnits; i++) {
				if(i == 0) {
					g2d.drawImage(cho, x[i], y[i], UNIT_SIZE, UNIT_SIZE, this);
				}
				else {
					g2d.drawImage(tail, x[i], y[i], UNIT_SIZE, UNIT_SIZE, this);
				}
				
		}
		g.setColor(new Color(201, 20, 20));
		g.setFont(customFont);
		FontMetrics metrics = getFontMetrics(customFont);
				g.drawString("Champions Feasted : " + championsFeasted, (SCREEN_WIDTH - metrics.stringWidth("Champions Feasted : " + championsFeasted)) / 2,  g.getFont().getSize());
	}
		else {
			Defeat(g);
		}
}
	
	public void spawnChampion() {
		boolean canSpawn = false;
		
		while (!canSpawn) {
			championX = random.nextInt((int)(SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
	        championY = random.nextInt((int)(SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
	        boolean collide = false;
	        for (int i = 0; i < bodyUnits; i++) {
	        	if (championX == x[i] && championY == y[i]) {
	        		collide = true;
	        		break;
	        	}
	        }
	        
	        if (!collide) {
	        	canSpawn = true;
	        	
	        }
		}
	}
	
	public void move() {
		for(int i = bodyUnits; i > 0; i--) {
			x[i] = x[i - 1];
			y[i] = y[i - 1];
			
		}
		
		switch(direction) {
			case 'U':
				y[0] = y[0] - UNIT_SIZE;
				break;
			case 'D':
				y[0] = y[0] + UNIT_SIZE;
				break;
			case 'L':
				x[0] = x[0] - UNIT_SIZE;
				break;
			case 'R':
				x[0] = x[0] + UNIT_SIZE;
				break;
		}
		
	}
	
	public void checkChampions() {
		if((x[0] == championX) && (y[0] == championY)) {
			bodyUnits++;
			championsFeasted++;
			spawnChampion();
		}
	}
	
	public void checkDeaths() {
		//checks if runs into itself
		for(int i = bodyUnits; i > 0; i--) {
			if((x[0] == x[i]) && (y[0] == y[i])) {
				moving = false;
			}
		}
		//checks if dies to left wall
		if(x[0] < 0) {
			moving = false;
		}
		//checks if dies to right wall
		if(x[0] > SCREEN_WIDTH) {
			moving = false;
		}
		//top wall
		if (y[0] < 0) {
			moving = false;
		}
		//bottom wall
		if (y[0] > SCREEN_HEIGHT) {
			moving = false;
		}
		if(!moving) {
			timer.stop();
		}
	}
	
	public void Defeat(Graphics g) {
		//score
		g.setColor(new Color(201, 20, 20));
		g.setFont(customFont);
		FontMetrics metrics1 = getFontMetrics(customFont);
		g.drawString("Champions Feasted : " + championsFeasted, (SCREEN_WIDTH - metrics1.stringWidth("Champions Feasted : " + championsFeasted)) / 2,  g.getFont().getSize());
		//game over
		Font defeatFont = customFont.deriveFont(150f);
		g.setColor(Color.red);
		g.setFont(defeatFont);
		FontMetrics metrics2 = getFontMetrics(defeatFont);
				g.drawString("Defeat", (SCREEN_WIDTH - metrics2.stringWidth("Defeat")) / 2, SCREEN_HEIGHT / 2);;
	}
	
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(moving) {
			move();
			checkChampions();
			checkDeaths();
		}
		
		repaint();
		
	}
	
	public class MyKeyAdapter extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if(direction != 'R') {
					direction = 'L';
				}
				break;
			case KeyEvent.VK_RIGHT:
				if(direction != 'L') {
					direction = 'R';
				}
				break;
			case KeyEvent.VK_UP:
				if(direction != 'D') {
					direction = 'U';
				}
				break;
			case KeyEvent.VK_DOWN:
				if(direction != 'U') {
					direction = 'D';
				}
				break;
			}
		}
	}
}
