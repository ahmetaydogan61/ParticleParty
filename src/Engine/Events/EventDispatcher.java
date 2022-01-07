package Engine.Events;

public class EventDispatcher 
{
    private Event m_Event;
	
	public EventDispatcher(Event event) 
    {
		this.m_Event = event;
	}
	
	public void dispatch(Event.Type type, EventHandler handler) 
    {
		if (m_Event.handled)
			return;
		
		if (m_Event.GetType() == type)
			m_Event.handled = handler.onEvent(m_Event);
	}    
}
