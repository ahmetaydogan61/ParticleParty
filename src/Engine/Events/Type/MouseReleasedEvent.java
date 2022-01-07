package Engine.Events.Type;

import Engine.Events.Event;

public class MouseReleasedEvent extends MouseButtonEvent 
{
	public MouseReleasedEvent(int button, float x, float y) 
    {
		super(button, x, y, Event.Type.MOUSE_RELEASED);
	}
}
