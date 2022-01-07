package Engine.Core;

import static org.lwjgl.glfw.GLFW.*;

import java.nio.DoubleBuffer;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import Engine.Editor.EditorLayer;
import Engine.Events.Type.MouseScrolledEvent;
import Engine.Utils.Camera;

public class Input 
{
    private static boolean m_PressedKey[] = new boolean[350];
    private static boolean m_PressedMouse[] = new boolean[3];
    private static float m_ScrollX = 0.0f;
    private static float m_ScrollY = 0.0f;
    private static Vector4f orthographicPosition = new Vector4f();
    private static Vector2f m_ViewportSize = new Vector2f();
    private static Vector2f m_ViewportPos = new Vector2f();

    protected static void ScrollCallback(long window ,double xOffset, double yOffset)
    {
        MouseScrolledEvent event = new MouseScrolledEvent((float) xOffset,(float) yOffset);
        Application.CallEvent(event);
    }

    public static float GetX()
    {
        DoubleBuffer x = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer y = BufferUtils.createDoubleBuffer(1);
        glfwGetCursorPos(Window.GetNativeWindow(), x, y);
        return (float) x.get(0);
    }

    public static float GetY()
    {
        DoubleBuffer x = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer y = BufferUtils.createDoubleBuffer(1);
        glfwGetCursorPos(Window.GetNativeWindow(), x, y);
        return (float) y.get(0);
    }

    public static float OrthoX()
    {
        return orthographicPosition.x;
    }

    public static float OrthoY()
    {
        return orthographicPosition.y;
    }

    public static boolean KeyDown(int key)
    {
        int state = glfwGetKey(Window.GetNativeWindow(), key);
        if (state == GLFW_PRESS)
            return true;
        else
            return false;
    }

    public static boolean KeyPressed(int key)
    {
        int state = glfwGetKey(Window.GetNativeWindow(), key);
        if(state == GLFW_PRESS && !m_PressedKey[key])
        {
            m_PressedKey[key] = true;
            return true;
        }
        else if(state == GLFW_RELEASE)
        {
            m_PressedKey[key] = false;
            return false;
        }
        else
            return false;
    }

    public static boolean KeyUp(int key)
    {
        int state = glfwGetKey(Window.GetNativeWindow(), key);
        if (state == GLFW_RELEASE)
            return true;
        else
            return false;
    }

    public static boolean MouseDown(int button)
    {
        switch(button)
        {
            case 0: 
            {
                int state = glfwGetMouseButton(Window.GetNativeWindow(), GLFW_MOUSE_BUTTON_LEFT);
                if(state == GLFW_PRESS)
                    return true;
                else
                    return false;
            }
            case 1:
            {
                int state = glfwGetMouseButton(Window.GetNativeWindow(), GLFW_MOUSE_BUTTON_RIGHT);
                if(state == GLFW_PRESS)
                    return true;
                 else
                    return false;
            }
            case 2:
            {
                int state = glfwGetMouseButton(Window.GetNativeWindow(), GLFW_MOUSE_BUTTON_MIDDLE);
                if(state == GLFW_PRESS)
                    return true;
                 else
                    return false;
            }
            default: 
                return false;
        }
    }

    public static boolean MouseUp(int button)
    {
        switch(button)
        {
            case 0: 
            {
                int state = glfwGetMouseButton(Window.GetNativeWindow(), GLFW_MOUSE_BUTTON_LEFT);
                if(state == GLFW_RELEASE)
                {
                    return true;
                }
                 else
                    return false;
            }
            case 1:
            {
                int state = glfwGetMouseButton(Window.GetNativeWindow(), GLFW_MOUSE_BUTTON_RIGHT);
                if(state == GLFW_RELEASE)
                    return true;
                 else
                    return false;
            }
            case 2:
            {
                int state = glfwGetMouseButton(Window.GetNativeWindow(), GLFW_MOUSE_BUTTON_MIDDLE);
                if(state == GLFW_RELEASE)
                    return true;
                 else
                    return false;
            }
            default: 
                return false;
        }
    }

    public static boolean MousePressed(int button)
    {
        switch(button)
        {
            case 0: 
            {
                int state = glfwGetMouseButton(Window.GetNativeWindow(), GLFW_MOUSE_BUTTON_LEFT);
                if(state == GLFW_PRESS && !m_PressedMouse[button])
                {
                    m_PressedMouse[button] = true;
                    return true;
                }
                else if(state == GLFW_RELEASE)
                {
                    m_PressedMouse[button] = false;
                    return false;
                }
                else
                    return false;
            }
            case 1:
            {
                int state = glfwGetMouseButton(Window.GetNativeWindow(), GLFW_MOUSE_BUTTON_RIGHT);
                if(state == GLFW_PRESS && !m_PressedMouse[button])
                {
                    m_PressedMouse[button] = true;
                    return true;
                }
                else if(state == GLFW_RELEASE)
                {
                    m_PressedMouse[button] = false;
                    return false;
                }
                else
                    return false;
            }
            case 2:
            {
                int state = glfwGetMouseButton(Window.GetNativeWindow(), GLFW_MOUSE_BUTTON_MIDDLE);
                if(state == GLFW_PRESS && !m_PressedMouse[button])
                {
                    m_PressedMouse[button] = true;
                    return true;
                }
                else if(state == GLFW_RELEASE)
                {
                    m_PressedMouse[button] = false;
                    return false;
                }
                else
                    return false;
            }
            default: 
                return false;
        }
    }

    public static float ScrollX()
    {
        return m_ScrollX;
    }

    public static float ScrollY()
    {
        return m_ScrollY;
    }

    public static boolean Dragging(int button)
    {
        return MouseDown(button);
    }

    protected static void UpdateInput()
    {
        m_ViewportSize = EditorLayer.GetViewportSize();
        m_ViewportPos = EditorLayer.GetViewportPos();
        Matrix4f matInverse = Camera.GetVP().invert();
        float x = (2.0f * ((float)(GetX() - m_ViewportPos.x) / (m_ViewportSize.x - 0))) - 1.0f;
        float y = (2.0f * ( 1f - ((float)(GetY() - m_ViewportPos.y) / (m_ViewportSize.y - 0)))) - 1.0f;
        orthographicPosition = new Vector4f(x, y, 0.0f, 1.0f);
        orthographicPosition.mul(matInverse, orthographicPosition);
    }
}
