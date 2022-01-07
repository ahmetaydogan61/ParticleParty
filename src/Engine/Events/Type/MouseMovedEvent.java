package Engine.Events.Type;

import Engine.Events.Event;

public class MouseMovedEvent extends Event 
{
	private float x, y;
	
	public MouseMovedEvent(float x, float y) 
    {
		super(Event.Type.MOUSE_MOVED);
		this.x = x;
		this.y = y;
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
