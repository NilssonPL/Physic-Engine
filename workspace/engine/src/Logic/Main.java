package Logic;
import java.awt.Canvas;
import javax.swing.JFrame;


public class Main extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GameScreen m_screen;
	
	public Main()
	{
		m_screen = new GameScreen();
		
		Canvas canvas = new Canvas();
		canvas.setSize(m_screen.GetDimension());
		canvas.setIgnoreRepaint(true);
		canvas.requestFocus();

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.add(canvas);
		this.pack();
		this.setLocationRelativeTo(null);
		
		canvas.createBufferStrategy(2);
		m_screen.SetBufferStrategy(canvas.getBufferStrategy());
		
		m_screen.start();
		
		this.setIgnoreRepaint(true);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Main();
	}

}
