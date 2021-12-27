import javax.swing.JFrame;

public class GameFrame  extends JFrame
{
	GameFrame()
	{
		GamePanel panel = new GamePanel();	// create an instance of GamePanel for GameFrane default constructor 
		this.add(panel);					// add panel to gameframe
		this.setTitle("Snake");				// set title to Snake
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);			// fixed size
		this.pack();						// fit components in jframe neatly
		this.setVisible(true);				// set frame to visible
		this.setLocationRelativeTo(null);	// location of frame set to center
	}
}

