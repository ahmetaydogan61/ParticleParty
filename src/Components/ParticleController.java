package Components;

import Engine.Core.Input;
import Engine.Core.Time;
import Engine.Core.Window;
import Engine.ECS.Component;
import Engine.Editor.EditorLayer;
import imgui.ImGui;

import static org.lwjgl.glfw.GLFW.*;

import org.joml.Vector2f;

public class ParticleController extends Component
{
    private Particle m_Particle;
    private float m_Speed[] = {5};
    private boolean m_UseMouse = true;
    private boolean m_ShowCursor = true;
    private boolean m_PlayAlways = false;

    public void Awake()
    {
        m_Particle = entity.GetComponent(Particle.class);
    }

    public void Update()
    {
        if(!EditorLayer.GetWindowState())
            if(m_UseMouse)
                MouseControls();
            else
                KeyboardControls();
        
        if(!m_PlayAlways)
        {
            if(Input.MouseDown(0) && EditorLayer.ViewportFocused() && EditorLayer.ViewportHovered() || Input.KeyDown(GLFW_KEY_SPACE))
                m_Particle.play = true;
            else
                m_Particle.play = false;
        }

        if(Input.MousePressed(1))
            m_UseMouse = !m_UseMouse;

        if(!m_ShowCursor && !EditorLayer.ViewportHovered())
            Window.ShowCursor();
        else if(!m_ShowCursor && EditorLayer.ViewportFocused() && EditorLayer.ViewportHovered())
            Window.HideCursor();
    }

    public void OnDrawGui() 
    {
        ImGui.beginChild("Particle Controller");

        if(ImGui.checkbox("Play Always", m_PlayAlways))
        {
            m_PlayAlways = !m_PlayAlways;
            if(m_PlayAlways)
                m_Particle.play = true;
        }

        if(ImGui.checkbox("Use Mouse", m_UseMouse))
            m_UseMouse = !m_UseMouse;
        
        if(ImGui.checkbox("Show Cursor", m_ShowCursor))
        {
            m_ShowCursor = !m_ShowCursor;
            if(m_ShowCursor)
                Window.ShowCursor();
            else
                Window.HideCursor();
        }
        
        if(!m_UseMouse)
            ImGui.dragFloat("Speed", m_Speed, Time.Delta() * 2, 0, 10);
        
        ImGui.endChild();
    }

    private void KeyboardControls()
    {
        if(Input.KeyDown(GLFW_KEY_W))
            entity.transform.position.y += Time.Delta() * m_Speed[0];
        if(Input.KeyDown(GLFW_KEY_S))
            entity.transform.position.y -= Time.Delta() * m_Speed[0];
        if(Input.KeyDown(GLFW_KEY_D))
            entity.transform.position.x += Time.Delta() * m_Speed[0];
        if(Input.KeyDown(GLFW_KEY_A))
            entity.transform.position.x -= Time.Delta() * m_Speed[0];
    }

    private void MouseControls()
    {
        entity.transform.position = new Vector2f(Input.OrthoX(), Input.OrthoY());
    }
}
