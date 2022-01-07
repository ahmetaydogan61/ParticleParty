package Engine.Editor;

import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;
import Engine.Events.*;
import Engine.Events.Type.MouseScrolledEvent;
import Engine.Core.Input;
import Engine.Core.Time;
import Engine.Utils.Camera;
import Engine.Utils.Maths;

public class EditorCameraController 
{
    private Camera m_Camera;
    private Vector3f m_Position = new Vector3f();
    private float m_MoveSpeed = 15.0f;
    private float m_ZoomLevel = 1f;
    private float m_ZoomSpeed = 0.25f;

    public EditorCameraController(Camera camera)
    {
        m_Camera = camera;
        m_Camera.SetPosition(m_Position);
        m_Camera.SetZoom(m_ZoomLevel);
    }
    
    private float x = 0.0f;
    private float y = 0.0f;
    private boolean m_Down = false;

    public void OnUpdate()
    {
        if(Input.MouseDown(2) && !m_Down)
        {
            m_Down = true;
            x = Input.OrthoX();
            y = Input.OrthoY();
        }
        else if(Input.MouseUp(2))
            m_Down = false;

        if(Input.Dragging(2))
        {
            float offsetX = x - Input.OrthoX();
            float offsetY = y - Input.OrthoY();
            m_Position.x += offsetX * Time.Delta() * m_MoveSpeed;
            m_Position.y += offsetY * Time.Delta() * m_MoveSpeed;
        }

        if(Input.KeyDown(GLFW_KEY_LEFT_CONTROL))
        {
            if(Input.KeyDown(GLFW_KEY_W))
                m_Position.y += m_MoveSpeed * Time.Delta();
            else if(Input.KeyDown(GLFW_KEY_S))
                m_Position.y -= m_MoveSpeed * Time.Delta();
            
            if(Input.KeyDown(GLFW_KEY_A))
                m_Position.x -= m_MoveSpeed * Time.Delta();
            else if(Input.KeyDown(GLFW_KEY_D))
                m_Position.x += m_MoveSpeed * Time.Delta();
        }
        m_Camera.SetPosition(m_Position);
    }

    public void OnEvent(Event e)
    {
        EventDispatcher dispatcher = new EventDispatcher(e);
        dispatcher.dispatch(Event.Type.MOUSE_SCROLLED, (Event event) -> (OnMouseScrolled((MouseScrolledEvent) event)));
    }

    private boolean OnMouseScrolled(MouseScrolledEvent e)
    {
        m_ZoomLevel -= e.ScrollY() * m_ZoomSpeed * Time.Delta();
        m_ZoomLevel = Maths.Clamp(m_ZoomLevel, 1, 100);
        m_ZoomSpeed -= e.ScrollY();
        m_ZoomSpeed = Maths.Clamp(m_ZoomSpeed, 1, 100);
        m_Camera.SetZoom(m_ZoomLevel);
        m_MoveSpeed -= e.ScrollY() * 0.5f;
        m_MoveSpeed = Maths.Clamp(m_MoveSpeed, 15.0f , 50f);
        return true;
    }
}
