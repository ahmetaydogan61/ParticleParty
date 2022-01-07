package Engine.ECS;

import java.util.ArrayList;
import java.util.List;

public class Register 
{
    public static List<Entity> awakes = new ArrayList<>();
    public static List<Entity> entities = new ArrayList<>();
    public static List<Entity> destroys = new ArrayList<>();

    public static void ClearAll()
    {
        awakes.clear();
        destroys.clear();
        for(Entity e : entities)
            e.Destroy();
    }
}
