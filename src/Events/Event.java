package Events;

import java.util.ArrayList;

public class Event
{
    private ArrayList<Event> m_Events = new ArrayList<Event>();
    private ArrayList<Action> m_Actions = new ArrayList<Action>();
    public Object object;

    public Event()
    {
        m_Events.add(this);
    }

    public void Subscribe(Event e)
    {
        m_Events.add(e);
    }

    public void Unsubscribe(Event e)
    {
        m_Events.remove(e);
    }

    public void DoAction(Object object)
    {
        this.object = object;
        for(Event event : m_Events)
            for(Action a : event.m_Actions)
                a.Perform(a);
    }

    public void DoAction()
    {
        for(Event event : m_Events)
            for(Action a : event.m_Actions)
                a.Perform(a);
    }

    public void AddAction(Action a)
    {
        m_Actions.add(a);
    }

    public void RemoveAction(Action a)
    {
        m_Actions.remove(a);
    }
}
