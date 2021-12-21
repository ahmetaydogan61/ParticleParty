package Engine;

import java.util.ArrayList;
import java.util.List;

import Materials.DefaultMaterial;

public class Entity 
{
    public Transform transform;
    public Material material;
    private List<Component> m_Components = new ArrayList<Component>();
    
    public Entity()
    {
        transform = new Transform();
        material = new DefaultMaterial();
        material.entity = this;
        material.Init();
        Register.awakes.add(this);
    }

    public void Awake()
	{
		for (Component c : m_Components)
			c.Awake();
	}

    public void Update()
    {
        for (Component c : m_Components)
			c.Update();
    }

    public void Destroy()
    {
        for (Component c : m_Components)
			c.Destroy();
        Register.destorys.add(this);
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
}
