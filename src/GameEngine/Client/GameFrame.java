// By Iacon1
// Created who knows when
// Client game frame - The meat of the game's IO

package GameEngine.Client;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import GameEngine.GameInfo;
import GameEngine.IntPoint2D;
import GameEngine.ScreenCanvas;
import GameEngine.Configurables.ConfigManager;
import GameEngine.EntityTypes.Alignable;
import GameEngine.EntityTypes.Alignable.AlignmentPoint;
import GameEngine.EntityTypes.GameEntity;
import GameEngine.EntityTypes.InputGetter;
import Utils.MiscUtils;

@SuppressWarnings("serial")
public class GameFrame extends JFrame
{
	private ScreenCanvas canvas;
	private boolean queueUpdateRes;
	
	/** Updates the scale of the screen.
	 * 
	 */
	public void checkScale()
	{
		float scaleX = getWidth() / ConfigManager.getScreenWidth();
		if (scaleX < 1) scaleX = 1;
		float scaleY = getHeight() / ConfigManager.getScreenHeight();
		if (scaleY < 1) scaleY = 1;
		
		if (getWidth() >= getHeight()) // wider than tall
			scaleY = scaleX; // Heighten to match 
		else // Taller than wide
			scaleX = scaleY; // Lengthen to match

		canvas.setScale(scaleX, scaleY);
		int newResX = (int) (ConfigManager.getScreenWidth() * scaleX);
		int newResY = (int) (ConfigManager.getScreenHeight() * scaleY);
		// Resize neccessary
		if (getContentPane().getWidth() != newResX || getContentPane().getHeight() != newResY)
		{
			getContentPane().setBounds(0, 0, newResX, newResY);
			queueUpdateRes = true;
		}
		else if (queueUpdateRes)
		{
			getContentPane().setBounds(0, 0, newResX, newResY);
			canvas.setBounds(getContentPane().getBounds());
			
			queueUpdateRes = false;
		}
	}
	
	/** Updates the UI
	 * 
	 */
	public void updateUIStuff() // Updates UI stuff
	{	
		checkScale();
		canvas.repaint();
	}
	
	private class EntityInputListener implements KeyListener, MouseListener
	{
		private void addCommand(GameEntity getter, String command)
		{
			if (command == null) return;
			GameInfo.addCommand("@" + getter.getId() + " " + command);
		}
		
		List<InputGetter> inputGetters()
		{
			List<InputGetter> inputGetters = new ArrayList<InputGetter>();
			
			for (int i = 0; i < GameInfo.getWorld().getEntities().size(); ++i)
			{
				GameEntity entity = GameInfo.getWorld().getEntities().get(i);
				if (entity != null && InputGetter.class.isAssignableFrom(entity.getClass()))
					inputGetters.add((InputGetter) entity);
			}
				
			return inputGetters;
		}
		
		@Override
		public void mouseClicked(MouseEvent e)
		{
			List<InputGetter> inputGetters = inputGetters();
			
			IntPoint2D point = new IntPoint2D(e.getX(), e.getY());
			int mX = canvas.descale(point).x;
			int mY = canvas.descale(point).y;
			int button = 0;
			
			switch (e.getButton())
			{
			case MouseEvent.NOBUTTON: return;
			case MouseEvent.BUTTON1: button = 0; break;
			case MouseEvent.BUTTON2: button = 1; break;
			case MouseEvent.BUTTON3: button = 1; break;
			}
			
			for (int i = 0; i < inputGetters.size(); ++i)
				addCommand((GameEntity) inputGetters.get(i), inputGetters.get(i).onMouseClick(mX, mY, button));
		}
		@Override
		public void mousePressed(MouseEvent e)
		{
			List<InputGetter> inputGetters = inputGetters();
			
			IntPoint2D point = new IntPoint2D(e.getX(), e.getY());
			int mX = canvas.descale(point).x;
			int mY = canvas.descale(point).y;
			int button = 0;
			
			switch (e.getButton())
			{
			case MouseEvent.NOBUTTON: return;
			case MouseEvent.BUTTON1: button = 0; break;
			case MouseEvent.BUTTON2: button = 1; break;
			case MouseEvent.BUTTON3: button = 2; break;
			}
			
			for (int i = 0; i < inputGetters.size(); ++i)
				addCommand((GameEntity) inputGetters.get(i), inputGetters.get(i).onMousePress(mX, mY, button));
		}
		@Override
		public void mouseReleased(MouseEvent e)
		{
			List<InputGetter> inputGetters = inputGetters();
			
			IntPoint2D point = new IntPoint2D(e.getX(), e.getY());
			int mX = canvas.descale(point).x;
			int mY = canvas.descale(point).y;
			int button = 0;
			
			switch (e.getButton())
			{
			case MouseEvent.NOBUTTON: return;
			case MouseEvent.BUTTON1: button = 0; break;
			case MouseEvent.BUTTON2: button = 1; break;
			case MouseEvent.BUTTON3: button = 2; break;
			}
			
			for (int i = 0; i < inputGetters.size(); ++i)
				addCommand((GameEntity) inputGetters.get(i), inputGetters.get(i).onMouseRelease(mX, mY, button));
		}
			
		@Override
		public void mouseEntered(MouseEvent e) // I don't think this can actually trigger bc of how the canvas is set up
		{
			return;
		}
		@Override
		public void mouseExited(MouseEvent e) // See mouseEntered
		{
			return;
		}

		@Override
		public void keyTyped(KeyEvent e) // Not really useful to us I think; TODO Maybe?
		{
//			findInputGetters();
//				
//			for (int i = 0; i < inputGetters.size(); ++i) inputGetters.get(i).onKeyPress(e.getKeyCode());
		}
			
		@Override
		public void keyPressed(KeyEvent e)
		{
			List<InputGetter> inputGetters = inputGetters();
				
			for (int i = 0; i < inputGetters.size(); ++i)
				addCommand((GameEntity) inputGetters.get(i), inputGetters.get(i).onKeyPress(e.getKeyCode()));
		}
		@Override
		public void keyReleased(KeyEvent e)
		{
			List<InputGetter> inputGetters = inputGetters();
			
			for (int i = 0; i < inputGetters.size(); ++i)
				addCommand((GameEntity) inputGetters.get(i), inputGetters.get(i).onKeyRelease(e.getKeyCode()));
		}
	}

	private void registerInputListener()
	{
		EntityInputListener listener = new EntityInputListener();
		
		addMouseListener(listener);
		addKeyListener(listener);
	}
	
	public GameFrame()
	{		
		GameInfo.setFrame(this);
		queueUpdateRes = false;
		setTitle(MiscUtils.getProgramName() + " Client");
		
		setIconImages(MiscUtils.getIcons(MiscUtils.ExecType.client));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(true);
		setVisible(true);
		
		getContentPane().setLayout(null);
		
		getContentPane().setPreferredSize(new Dimension(ConfigManager.getScreenWidth(), ConfigManager.getScreenHeight()));
		
		canvas = new ScreenCanvas();
		canvas.setBounds(0, 0, ConfigManager.getScreenWidth(), ConfigManager.getScreenHeight());
		getContentPane().add(canvas);
		
		pack();
		
		setFocusable(true);
		requestFocus();
		registerInputListener();
	}
	
	public static IntPoint2D getAlignmentPoint(AlignmentPoint point)
	{
		IntPoint2D pos = new IntPoint2D(0, 0);
		switch (point)
		{
		case northWest: break;
		case north: pos.x = ConfigManager.getScreenWidth() / 2; break;
		case northEast: pos.x = ConfigManager.getScreenWidth(); break;
		
		case west: pos.y = ConfigManager.getScreenHeight() / 2; break;
		case center: pos.x = ConfigManager.getScreenWidth() / 2;
			pos.y = ConfigManager.getScreenHeight() / 2; break;
		case east: pos.x = ConfigManager.getScreenWidth() - 1; 
			pos.y = ConfigManager.getScreenHeight() / 2; break;
			
		case southWest: pos.y = ConfigManager.getScreenHeight() - 1; break;
		case south: pos.x = ConfigManager.getScreenWidth() / 2; 
			pos.y = ConfigManager.getScreenHeight() - 1; break;
		case southEast: pos.x = ConfigManager.getScreenWidth() - 1; 
			pos.y = ConfigManager.getScreenHeight() - 1; break;
			
		default: return null;
		}
		return pos;
	}
	public static void align(AlignmentPoint point, Alignable target, AlignmentPoint targetPoint)
	{
		target.align(targetPoint, null, point);
	}
}
