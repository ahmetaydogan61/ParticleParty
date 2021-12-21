package Events;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class Input 
{
    private static Input Instance;
    private boolean KeyPressed[] = new boolean[350];

    private Input()
    {
    }

    public static Input Get()
    {
        if(Instance == null)
            Instance = new Input();
        
        return Instance;
    }

    public static void KeyCallback(long window, int key, int scancode, int action, int mods)
    {
        if(action == GLFW_PRESS)
            Get().KeyPressed[key] = true;
        else if(action == GLFW_RELEASE)
            Get().KeyPressed[key] = false;
    }

    public static boolean GetKey(int keyCode)
    {
        if(keyCode < Get().KeyPressed.length)
            return Get().KeyPressed[keyCode];
        else
            return false;
    }
}
