package Game_Engine.Engine.engine;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import Game_Engine.Engine.Objs.Obj_Container;
import Game_Engine.Engine.Objs.actionLogging.Command;

/*
 * Input Class, Conceived by Bryce Summers, 12 - 31 - 2012.
 * Purpose, Provides a root source of Key Events.
 */

public class Game_input
{

	// -- Local Variables.
	public static int mouse_x, mouse_y;
	
	public static int mouse_button = 0;
	
	private Obj_Container data;
	
	private key_input   l_key;
	private mouse_input l_mouse;
	private Game_output output;
	
	public Game_input(Obj_Container data_in, Game_output output_in)
	{
		// Set the Object data.
		data = data_in;
		
		// Initialize listeners.
		l_key   = new key_input();
		l_mouse = new mouse_input();
	
		// Initialize the OS JFrame.
		this.output = output_in;
		
		start();
	}

	// Enable's this input class's object stack to receive keyboard inputs.
	public void start()
	{
		output.setFocusable(true);
		output.requestFocus();
		output.addKeyListener(l_key);
		output.addMouseListener(l_mouse);
		output.addMouseMotionListener(l_mouse);
		output.addMouseWheelListener(l_mouse);
		
		//http://stackoverflow.com/questions/8275204/how-can-i-listen-to-a-tab-key-pressed-typed-in-java
		output.setFocusTraversalKeysEnabled(false);
	}
	
	// Stops this stack of objects from receiving input.
	public void stop()
	{
		output.removeKeyListener(l_key);
		output.removeMouseListener(l_mouse);
		output.removeMouseMotionListener(l_mouse);
		output.removeMouseWheelListener(l_mouse);
	}
		
	// Listen for Keyboard inputs.
	private class key_input extends KeyAdapter
	{
		@Override
		public void keyPressed(KeyEvent e)
	    {
			if(Obj_Container.globalLog)
			{
				Command.logMove(mouse_x, mouse_y);
				Command.logKeyP(e.getKeyCode());
			}
			
			// Pressing any key will communicate the the root container's proxy cursor, if it exists.
			if(data.proxy_cursor != null)
			{
				data.proxy_cursor.keyPSpecial(e.getKeyCode());
				return;
			}
			data.keyP(e.getKeyCode());
		}

		@Override
		public void keyReleased(KeyEvent e)
		{			
			if(Obj_Container.globalLog)
			{
				Command.logMove(mouse_x, mouse_y);
				Command.logKeyR(e.getKeyCode());
			}
			
			data.keyR(e.getKeyCode());
		}
		
		// FIXME: Determine if we should implement more Key input functionality.
		
	}
	
	// Listen for Mouse inputs.
	private class mouse_input extends MouseAdapter implements MouseMotionListener
	{
		@Override
		public void mousePressed(MouseEvent e)
		{
			mouse_button = e.getButton();
			
			computeMouseCoords(e);
			
			if(Obj_Container.globalLog)
			{
				Command.logMove(mouse_x, mouse_y);
				Command.logMouseP();
			}
			
			// Disable mouse input when the root container contains a proxy_cursor.
			if(data.proxy_cursor != null)
			{
				return;
			}

			data.mouseP(mouse_x, mouse_y);
			data.global_mouseP();
		}
		
		@Override
		public void mouseReleased(MouseEvent e)
		{
			
			mouse_button = e.getButton();
			
			computeMouseCoords(e);
			
			if(Obj_Container.globalLog)
			{
				Command.logMove(mouse_x, mouse_y);
				Command.logMouseR();
			}			
			
			// Disable mouse input when the root container contains a proxy_cursor.
			if(data.proxy_cursor != null)
			{
				return;
			}

			data.mouseR(mouse_x, mouse_y);
			data.global_mouseR();
		}
		
		@Override
		public void mouseMoved(MouseEvent e)
		{
			computeMouseCoords(e);
			
			// Disable mouse input when the root container contains a proxy_cursor.
			if(data.proxy_cursor != null)
			{
				return;
			}			

			data.mouseM(mouse_x, mouse_y);
			data.global_mouseM(mouse_x, mouse_y);
		}
		
		@Override
		public void mouseDragged(MouseEvent e)
		{
			
			computeMouseCoords(e);
		
			// Disable mouse input when the root container contains a proxy_cursor.
			if(data.proxy_cursor != null)
			{
				return;
			}

			data.mouseD(mouse_x, mouse_y);
			data.global_mouseD(mouse_x, mouse_y);
		}
		
		public void mouseWheelMoved(MouseWheelEvent e)
		{
			// Disable mouse input when the root container contains a proxy_cursor.
			if(data.proxy_cursor != null)
			{
				return;
			}
			
			int rotate = e.getWheelRotation();
			data.global_mouseScroll(rotate);
		}
	}
	
	public KeyAdapter getKeyListener()
	{
		return new key_input();
	}
	
	public MouseAdapter getMouseListener()
	{
		return new mouse_input();
	}
	
	public MouseMotionListener getMouseMotionListener()
	{
		return new mouse_input();
	}
	
	public MouseWheelListener getMouseWheelListener()
	{
		return new mouse_input();
	}
	
	// Takes a MouseEvent and computes the coordinates of the mouse in the game.
	private void computeMouseCoords(MouseEvent e)
	{
				
		// Calculate the appropriate x and y coordinates,
		// in relation to the distorted image on the screen.
		mouse_x = e.getX() - output.x1;
		mouse_y = e.getY() - output.y1;
		
		mouse_x = mouse_x * data.getW()/(output.x2 - output.x1);
		mouse_y = mouse_y * data.getH()/(output.y2 - output.y1);
	}	
	
}