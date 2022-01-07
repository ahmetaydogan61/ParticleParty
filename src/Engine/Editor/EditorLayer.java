package Engine.Editor;

import org.joml.Vector2f;

import Components.ParticleController;
import Components.Particle;
import Engine.Core.ImGuiLayer;
import Engine.Core.Layer;
import Engine.Core.Renderer;
import Engine.Core.Time;
import Engine.ECS.Component;
import Engine.ECS.Entity;
import Engine.ECS.Register;
import Engine.Events.Event;
import Engine.Utils.Camera;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.ImGuiViewport;
import imgui.ImVec2;
import imgui.flag.ImGuiDockNodeFlags;
import imgui.flag.ImGuiStyleVar;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;

public class EditorLayer extends Layer
{
    private static Camera m_Camera;
    private static EditorCameraController m_EditorCameraController;
    private static Vector2f m_ViewportSize = new Vector2f();
    private static Vector2f m_ViewportPos = new Vector2f();
    private static float m_AspectRatio;
    private static boolean m_ViewPortFocused;
    private static boolean m_ViewPortHovered;
    private static Entity m_SelectedEntity = null;
    private static boolean m_ShowDebug = true;
    private static boolean m_ShowInspector = true;

    public void OnAwake()
    {
        m_Camera = new Camera();
        m_EditorCameraController = new EditorCameraController(m_Camera);
        m_ViewportSize = new Vector2f();
        m_SelectedEntity = new Entity();
        m_SelectedEntity.transform.scale = new Vector2f();
        m_SelectedEntity.AddComponent(new Particle());
        m_SelectedEntity.AddComponent(new ParticleController());
    }

    public void OnEvent(Event e)
    {
        m_EditorCameraController.OnEvent(e);
    }

    public void OnUpdate()
    {
        m_EditorCameraController.OnUpdate();
        if(m_ViewportSize.x != Renderer.GetFramebuffer().GetWidth() || m_ViewportSize.y != Renderer.GetFramebuffer().GetHeight())
        {
            Renderer.GetFramebuffer().Resize((int) m_ViewportSize.x,(int) m_ViewportSize.y);
            m_AspectRatio = m_ViewportSize.x / m_ViewportSize.y;
            Camera.Main().SetProjection();
        }
    }

    protected static Entity GetSelectedEntity()
    {
        return m_SelectedEntity;
    }

    public void OnDrawGui()
    {
        Dockspace();
        Menubar();
        Viewport();
        if(m_ShowInspector)
            Inspector();
        if(m_ShowDebug)
            Debug();
    }

    public static float GetAspectRatio() 
    {
        return m_AspectRatio;
    }

    public static boolean GetWindowState()
    {
        return !m_ViewPortFocused && !m_ViewPortHovered;
    }

    public static boolean ViewportFocused()
    {
        return m_ViewPortFocused;
    }

    public static boolean ViewportHovered()
    {
        return m_ViewPortHovered;
    }

    public static Vector2f GetViewportSize()
    {
        return m_ViewportSize;
    }

    public static Vector2f GetViewportPos()
    {
        return m_ViewportPos;
    }

    private void Dockspace()
    {
        boolean opt_fullscreen = true;
        int dockpace_flags = ImGuiDockNodeFlags.None;
        int windowFlags = ImGuiWindowFlags.MenuBar | ImGuiWindowFlags.NoDocking;

        if(opt_fullscreen)
        {
            ImGuiViewport viewport = ImGui.getMainViewport();
            ImGui.setNextWindowPos(viewport.getPosX(), viewport.getPosY());
            ImGui.setNextWindowSize(viewport.getSizeX(), viewport.getSizeY());
            ImGui.setNextWindowViewport(viewport.getID());
            ImGui.pushStyleVar(ImGuiStyleVar.WindowRounding, 0.0f);            
            ImGui.pushStyleVar(ImGuiStyleVar.WindowBorderSize, 0.0f);
            windowFlags |= ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoCollapse | ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoMove;            
            windowFlags |= ImGuiWindowFlags.NoBringToFrontOnFocus | ImGuiWindowFlags.NoNavFocus;
        }

        ImGui.pushStyleVar(ImGuiStyleVar.WindowPadding, 0, 0);
        ImGui.begin("Dockspace", new ImBoolean(true), windowFlags);
        ImGui.popStyleVar();
        
        if(opt_fullscreen)
            ImGui.popStyleVar(2);

        int dockspaceId = ImGui.getID("MyDockSpace");
        ImGui.dockSpace(dockspaceId, 0, 0, dockpace_flags);
    }
    
    private void Menubar()
    {
        if(ImGui.beginMainMenuBar())
        {
            if(ImGui.beginMenu("View", true))
            {
                if(ImGui.beginMenu("Viewports"))
                {
                    if(ImGui.menuItem("Inspector Panel", "", m_ShowInspector))
                        m_ShowInspector = !m_ShowInspector;

                    if(ImGui.menuItem("Debug Panel", "", m_ShowDebug))
                        m_ShowDebug = !m_ShowDebug;

                    ImGui.endMenu();
                }
                ImGui.endMenu();
            }
            ImGui.endMainMenuBar();
        }
    }

    private void Viewport()
    {
        ImGui.pushStyleVar(ImGuiStyleVar.WindowPadding, 0, 0);
        ImGuiIO io = ImGui.getIO();
        int flag = ImGuiWindowFlags.NoScrollbar;
       
        ImGui.begin("Viewport", new ImBoolean(true), flag);

        m_ViewPortFocused = ImGui.isWindowFocused();
        m_ViewPortHovered = ImGui.isWindowHovered();
        
        ImGuiLayer.BlockEvent(GetWindowState());

        io.setConfigWindowsMoveFromTitleBarOnly(!GetWindowState());

        ImVec2 viewportPanelSize = ImGui.getContentRegionAvail();
        ImVec2 viewportPanelPos = ImGui.getCursorScreenPos();
        m_ViewportSize = new Vector2f(viewportPanelSize.x, viewportPanelSize.y);
        m_ViewportPos = new Vector2f(viewportPanelPos.x, viewportPanelPos.y);

        int textureID = Renderer.GetFramebuffer().GetColorAttachmentID();
        ImGui.image(textureID, m_ViewportSize.x, m_ViewportSize.y, 0, 1, 1, 0);
        ImGui.end();
        ImGui.popStyleVar();
        ImGui.end();
    }

    private void Inspector()
    {
        ImGui.begin("Inspector");
        if(m_SelectedEntity != null)
        {
            ImGui.beginGroup();
                float[] position = { m_SelectedEntity.transform.position.x,  m_SelectedEntity.transform.position.y };
                float[] scale = { m_SelectedEntity.transform.scale.x,  m_SelectedEntity.transform.scale.y };
                ImGui.text("Transform");
                ImGui.dragFloat2("Position", position, Time.Delta() * 5, Float.MIN_VALUE, Float.MIN_VALUE);
                m_SelectedEntity.transform.position.set(position);
                m_SelectedEntity.transform.scale.set(scale);
            ImGui.endGroup();

            AddPadding(0, 10);

            AddPadding(0, 5);
            
            for(int i = 0; i < m_SelectedEntity.GetComponents().size(); i++)
            {
                Component component = m_SelectedEntity.GetComponents().get(i);
                if(ImGui.beginTabBar("Components"))
                {
                    if(ImGui.beginTabItem(component.getClass().getSimpleName()))
                    {
                        component.OnDrawGui();
                        ImGui.endTabItem();
                    }
                    ImGui.endTabBar();
                }
            }
        }
        ImGui.end();
    }

    private void AddPadding(float x, float y)
    {
        ImVec2 p0 = ImGui.getCursorScreenPos();
        ImGui.setCursorScreenPos(p0.x + x, p0.y + y);
    }

    private void Debug()
    {
        ImGui.begin("Debug");
        ImGui.text(String.format("Average %.1f ms/frame (%.1f FPS)", 1000.0f / ImGui.getIO().getFramerate(), ImGui.getIO().getFramerate()));
        ImGui.text(String.format("Awake Count: %d", Register.awakes.size()));
        ImGui.text(String.format("Entity Count: %d", Register.entities.size()));
        ImGui.text(String.format("Destroy Count: %d", Register.destroys.size()));
        ImGui.text(String.format("Awake Calls: %d", Entity.AwakeCalls));
        ImGui.text(String.format("Destroy Calls: %d", Entity.DestroyCalls));
        ImGui.end();
    }
}   
