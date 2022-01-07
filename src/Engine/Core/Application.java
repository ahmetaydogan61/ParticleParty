package Engine.Core;

import Engine.ECS.EcsLayer;
import Engine.Editor.EditorLayer;
import Engine.Events.Event;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;

public class Application 
{
    private static Application Instance;
    private Window m_Window;
    private List<Layer> m_LayerStack = new ArrayList<>();
    private ImGuiLayer m_ImGui;
    private EcsLayer m_EcsLayer;

    public Application()
    {
        Instance = this;
        m_Window = new Window(new WindowProps("Particle Party", 1920, 1080));
        m_ImGui = new ImGuiLayer();
        Renderer.Init();
        PushLayer(new EditorLayer());
        PushLayer(m_ImGui);
        m_EcsLayer = new EcsLayer();
        PushLayer(m_EcsLayer);
    }

    public void PushLayer(Layer layer)
    {
        m_LayerStack.add(layer);
        layer.OnAwake();
    }

    public void PopLayer(Layer layer)
    {
        m_LayerStack.remove(layer);
        layer.OnDetach();
    }

    public static void CallEvent(Event e)
    {
        Instance.OnEvent(e);
    }

    public void OnEvent(Event e)
    {
        for(int i = m_LayerStack.size()-1; i >= 0; i--)
            m_LayerStack.get(i).OnEvent(e);
    }

    public void Run()
    {
        float begin = (float) GLFW.glfwGetTime();
        float end = 0.0f;
        float dt = 0.0f;
        
        m_EcsLayer.Start();

        while(m_Window.WindowRunning())
        {
            end = (float) GLFW.glfwGetTime();
            dt = end - begin;
            begin = end;
            Time.SetDelta(dt);

            Renderer.Clear();        

            for(Layer layer : m_LayerStack)
                layer.OnUpdate();

            Renderer.OnRender();

            m_ImGui.Begin(dt);
            for(int i = 0; i < m_LayerStack.size(); i++)
                m_LayerStack.get(i).OnDrawGui();
            m_ImGui.End();
            
            m_Window.OnUpdate();
            Input.UpdateInput();
        }
        
        for(Layer layer : m_LayerStack)
                layer.OnDetach();
        m_Window.OnDetach();
    }

    public static Application Get()
    {
        return Instance;
    }
}
