package SimpleEngine;

import java.awt.Dimension;


public abstract class MainRoom extends SimpleRoom
{
	
	public MainRoom(String gameName, int w, int h)
	{
		super(w, h);
		
		initialize(w, h);
		
		Game_output out = new Game_output(gameName, new Dimension(w, h));
		new Game_looper(this, 60, out);
		
		// Start this room's source timers.
		new Game_input (this, out);
						
	}

	public abstract void initialize(int w, int h);
	
}
