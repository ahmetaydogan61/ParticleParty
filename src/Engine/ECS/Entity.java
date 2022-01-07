package Engine.ECS;

import java.util.ArrayList;
import java.util.List;

public class Entity 
{
	public static int AwakeCalls = 0; 
	public static int DestroyCalls = 0; 
    public Transform transform = new Transform();
    public Material material = new Material();
    private List<Component> m_Components = new ArrayList<Component>();
    
    public Entity()
    {
        Register.awakes.add(this);
    }

    public void Awake()
	{
		AwakeCalls++;
		for (Component c : m_Components)
			c.Awake();
	}

    public void Update()
    {
        for (int i = 0; i < m_Components.size(); i++)
		{
			Component c = m_Components.get(i);
			c.Update();
		}
    }

    public void Destroy()
    {
		DestroyCalls++;
        for (Component c : m_Components)
			c.Destroy();
       	Register.destroys.add(this); 
    }

    public void AddComponent(Component component)
	{
		m_Components.add(component);
		component.entity = this;
	}

	public <T extends Component> void RemoveComponent(Class<T> componentClass)
	{
		for (int i = 0; i < m_Components.size(); i++)
		{
			Component c = m_Components.get(i);
			if (componentClass.isAssignableFrom(c.getClass()))
			{
				m_Components.get(i).Destroy();
				m_Components.remove(i);
				return;
			}
		}
	}

	public <T extends Component> T GetComponent(Class<T> componentClass)
	{
		for (Component c : m_Components)
			if (componentClass.isAssignableFrom(c.getClass()))
				return componentClass.cast(c);
		return null;
	}

	public List<Component> GetComponents()
	{
		return m_Components;
	}

}
