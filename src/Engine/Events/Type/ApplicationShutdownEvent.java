package Engine.Events.Type;

import Engine.Events.Event;

public class ApplicationShutdownEvent extends Event
{
    public ApplicationShutdownEvent() 
    {
        super(Type.APPLICATION_SHUTDOWN);
    }
}
