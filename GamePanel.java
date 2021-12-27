import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;


public class GamePanel extends JPanel implements ActionListener
{
	//variables
	static final int SCREEN_WIDTH = 600;
	static final int SCREEN_HEIGHT = 600;
	static final int UNIT_SIZE = 15;
	static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT)/UNIT_SIZE;
	static final int DELAY = 75;
	int bodyParts = 4;
	int fruitEaten = 0;
	int fruitX;
	int fruitY;
	char direction = 'R';		// Right, Left, Up, Down = R, L, U, D
	boolean running = false;
	Timer timer;
	Random random;
	
	// array declaration for snake body
	final int x[] = new int[GAME_UNITS];
	final int y[]= new int[GAME_UNITS];
	
	// random color generator
	Random rand = new Random();
	float r = rand.nextFloat();
	float b = rand.nextFloat();
	float g = rand.nextFloat();
	Color randomC = new Color(r,g,b);
	
		
	// default constructor that will initialize the screen and start the game
	GamePanel()
	{
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
		this.setBackground(Color.BLACK);		// set background color to black
		this.setFocusable(true);				
		this.addKeyListener(new MyKeyAdapter()); // initialize key listener (will evaluate what keys are pressed)
		startGame();							// calls start game method 
	}
	
	// method to run the game
	public void startGame()
	{
		newFruit();								// call fruit method to place a fruit in a random place on the screen
		running = true;							// running is set to true until a rule is violated
		timer = new Timer(DELAY,this);			// timer is initialized and set to the delay value of 75 milliseconds 
		timer.start();							// start the timer 
			
	}
	
	// colors in specified object passed in
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);	
		draw(g);
		
	}
	
	public void draw(Graphics g)
	{
		if(running)
		{
			// optional grid lines
			// draw a grid to visualize the unit size
			/*
			for(int i = 0; i < SCREEN_HEIGHT/UNIT_SIZE; i++)		// draw lines along the y-axis 
			{
				g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
			}
		
		
			for(int i = 0; i < SCREEN_HEIGHT/UNIT_SIZE; i++)
			{
				g.drawLine(0, i*UNIT_SIZE, SCREEN_HEIGHT, i*UNIT_SIZE);	// draw lines along the x-axis
			}
		*/
		
			g.setColor(randomC);									// set fruit to a random color
			g.fillOval(fruitX, fruitY, UNIT_SIZE, UNIT_SIZE);		// fill in specified shape with that color
		
			//draw snake
			for(int i  = 0; i< bodyParts; i++)						// if i is less than snake size specified, draw again
			{
				if(i == 0)											// if i is zero then it is the snake head - set to CYAN
				{
					g.setColor(Color.CYAN);
					g.fillRect(x[i],y[i], UNIT_SIZE,UNIT_SIZE);
				}
				else												// if it is not zero then it is snake body -set to orange			
				{
					g.setColor(Color.orange);
					g.fillRect(x[i],y[i], UNIT_SIZE,UNIT_SIZE);
				}
			}
			// Current score
			g.setColor(Color.CYAN);
			g.setFont(new Font("Monospaced", Font.BOLD, 28));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("SCORE: " + fruitEaten, (SCREEN_WIDTH-metrics.stringWidth("SCORE: " + fruitEaten))/2, g.getFont().getSize());
		}
		else
		{
			GameOver(g);
		}
	}
	
	// generate a new fruit when the game is started and when the fruit is consumed
	public void newFruit()
	{	
		fruitX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
		fruitY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
		
	}
	
	public void move ()
	{
		for (int j = bodyParts; j > 0; j--)
		{
			// shift our snake by one unit
			x[j] = x[j-1];
			y[j] = y[j-1];
		}
		
		switch(direction)
		{
		case 'U':
			y[0]= y[0]-UNIT_SIZE;	// move up by one unit
			break;
		case'D':
			y[0]=y[0]+UNIT_SIZE;	// move down by one unit
			break;
		case 'R':
			x[0]=x[0]+UNIT_SIZE;	// move right one unit
			break;
		case 'L':
			x[0]=x[0]-UNIT_SIZE;	// move left one unit
			break;
		}
	}
	
	public void checkFruit ()
	{
		if((x[0]== fruitX && y[0]== fruitY))
		{
			bodyParts++;
			fruitEaten++;
			newFruit();
		}
		
	}
	
	public void checkCollision()
	{
		// will check if snake head hits the snakes body
		for(int i = bodyParts; i > 0; i--)
		{
			if((x[0]==x[i]) && (y[0]==y[i]))
			{
				running = false;
			}
		}
		
		// check if head touches left border
		if(x[0] < 0)
		{
			running = false;
		}
		
		// check if head touches right border
		if(x[0] > SCREEN_WIDTH)
		{
		    running = false;
		}
		
		// check if head touches top border
		if(y[0] < 0)
		{
			running = false;
		}
				
		// check if head touches bottom border
		if(y[0] > SCREEN_HEIGHT)
		{
		    running = false;
		}
	}
	
	public void GameOver(Graphics g)
	{
		// Game over text 
		g.setColor(Color.green);
		g.setFont(new Font("Monospaced", Font.BOLD, 75));
		FontMetrics metrics = getFontMetrics(g.getFont());
		g.drawString("GAME OVER", (SCREEN_WIDTH-metrics.stringWidth("GAME OVER"))/2, SCREEN_HEIGHT/2);
		
		// Current score
		g.setColor(Color.CYAN);
		g.setFont(new Font("Monospaced", Font.BOLD, 28));
		FontMetrics metric = getFontMetrics(g.getFont());
		g.drawString("SCORE: " + fruitEaten, (SCREEN_WIDTH-metric.stringWidth("SCORE: " + fruitEaten))/2, g.getFont().getSize());
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if(running)
		{
			move();
			checkFruit();
			checkCollision();
			
		}
		repaint();
		
		
	}
	
	public class MyKeyAdapter extends KeyAdapter
	{
		@Override
		public void keyPressed(KeyEvent e)
		{
			switch(e.getKeyCode())
			{
			case KeyEvent.VK_LEFT:
				if(direction != 'R')
				{
					direction ='L';
				}
				break;
			case KeyEvent.VK_RIGHT:
				if(direction != 'L')
				{
					direction ='R';
				}
				break;
			case KeyEvent.VK_UP:
				if(direction != 'D')
				{
					direction ='U';
				}
				break;
			case KeyEvent.VK_DOWN:
				if(direction != 'U')
				{
					direction ='D';
				}
				break;
			}
			
		}
		
	}

}
