package Engine.Events.Type;

import Engine.Events.Event;

public class MouseButtonEvent extends Event 
{ 
	protected int button;
	protected float x, y;
	
	protected MouseButtonEvent(int button, float x, float y, Type type) 
    {
		super(type);
		this.button = button;
		this.x = x;
		this.y = y;
	}

	public int GetButton() 
    {
		return button;
	}

	public float GetX() 
    {
		return x;
	}

	public float GetY() 
    {
		return y;
	}
	
}
