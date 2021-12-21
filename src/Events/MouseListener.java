package Events;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

import Engine.Window;

public class MouseListener 
{
    private static MouseListener Instance;
    
    private double scrollX, scrollY;
    private double xPos, yPos, lastY, lastX;
    private boolean mouseButtonPressed[] = new boolean[3];
    private boolean isDragging;

    private MouseListener()
    {
        scrollX = 0.0;
        scrollY = 0.0;
        xPos = 0.0;
        yPos = 0.0;
        lastX = 0.0;
        lastY = 0.0;
    }

    public static MouseListener This()
    {
        if(Instance == null)
            Instance = new MouseListener();
        return Instance;
    }

    public static void MousePositionCallback(long window, double xPos, double yPos)
    {
        This().lastX = xPos;
        This().lastY = yPos;
        This().xPos = xPos;
        This().yPos = yPos;
        This().isDragging = This().mouseButtonPressed[0] || This().mouseButtonPressed[1] || This().mouseButtonPressed[2];
    }

    public static void MouseButtonCallback(long window, int button, int action, int mods)
    {
        if(action == GLFW_PRESS)
        {
            if(button < This().mouseButtonPressed.length)
                This().mouseButtonPressed[button] = true;
        }
        else if(action == GLFW_RELEASE)
            if(button < This().mouseButtonPressed.length)
            {
                This().mouseButtonPressed[button] = false;
                This().isDragging = false;
            }
    }

    public static void MouseScrollCallback(long window, double xOffset, double yOffset)
    {
        This().scrollX = xOffset;
        This().scrollY = yOffset;
    }
    
    public static void EndFrame()
    {
        This().scrollX = 0;
        This().scrollY = 0;
        This().lastX = This().xPos;
        This().lastY = This().yPos;
    }

    public static float GetX()
    {
        return (float) This().xPos;
    }

    public static float GetY()
    {
        return (float) Window.GetHeight() - (float) This().yPos;
    }

    public static float GetdX()
    {
        return (float) (This().lastX - This().xPos);
    }

    public static float GetdY()
    {
        return (float) (This().lastY - This().yPos);
    }

    public static float GetScrollX()
    {
        return (float) This().scrollX;
    }

    public static float GetScrollY()
    {
        return (float) This().scrollY;
    }

    public static boolean IsDragging()
    {
        return This().isDragging;
    }

    public static boolean MouseButtonDown(int button)
    {
        if(button < This().mouseButtonPressed.length)
            return This().mouseButtonPressed[button];
        else
            return false;
    }
}
