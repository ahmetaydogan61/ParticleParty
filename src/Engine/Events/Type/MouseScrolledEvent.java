package Engine.Events.Type;

import Engine.Events.Event;

public class MouseScrolledEvent extends Event
{
    private float m_xOffset = 0.0f;
    private float m_yOffset = 0.0f;

    public MouseScrolledEvent(float xOffset, float yOffset) 
    {
		super(Event.Type.MOUSE_SCROLLED);
        m_xOffset = xOffset;
        m_yOffset = yOffset;
	}

    public float ScrollX()
    {
        return m_xOffset;
    }

    public float ScrollY()
    {
        return m_yOffset;
    }
    
}
