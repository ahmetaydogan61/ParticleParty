package Engine.Events;

public class Event 
{
    public enum Type 
    {
		MOUSE_PRESSED,
		MOUSE_RELEASED,
		MOUSE_MOVED,
		MOUSE_SCROLLED,
		APPLICATION_SHUTDOWN
	}
	
	private Type m_Type;
	boolean handled;
	
	protected Event(Type type) 
    {
		this.m_Type = type;
	}
	
	public Type GetType() 
    {
		return m_Type;
	}
}
