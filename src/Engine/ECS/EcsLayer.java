package Engine.ECS;

import Engine.Core.Layer;
import Engine.Events.Event;
import Engine.Events.EventDispatcher;
import Engine.Events.Event.Type;
import Engine.Renderer.InstanceRenderer;

public class EcsLayer extends Layer implements Runnable
{
    public void OnEvent(Event e) 
    {
        EventDispatcher dispatcher = new EventDispatcher(e);
        dispatcher.dispatch(Type.APPLICATION_SHUTDOWN,(event)->
        {
            Stop();
            return false;
        });
    }

    public void OnUpdate()
    {
        for(Entity e : Register.awakes)
        {
            Register.entities.add(e);
            e.Awake();
        }
        Register.awakes.clear();
        
        for(Entity e : Register.entities)
        {
            e.Update();
            InstanceRenderer.CreateQuad(e.transform.position, e.material.color, e.transform.scale);
        }

        for(Entity e : Register.destroys)
            Register.entities.remove(e);			
        
        Register.destroys.clear();
    }

    private Thread m_Thread;
    private boolean m_Running = false;

    public void Start()
    {
        m_Thread = new Thread(this, "ECS Thread");
        m_Running = true;
        m_Thread.start();
    }

    public void Stop()
    {
        m_Running = false;
        m_Thread.interrupt();
    }

    @Override
    public void run() 
    {
        if(m_Running)
        {
            Stop();
        }
    }
}
