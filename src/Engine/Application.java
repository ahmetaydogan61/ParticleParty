package Engine;

import org.lwjgl.glfw.GLFW;

import Renderer.Renderer;
import Utils.Time;

public class Application 
{
    private static Application Instance = null;
    private Window m_Window;
    private LayerStack m_LayerStack;
    private ImGuiLayer m_ImGuiLayer;
    private Renderer m_Renderer;
    private ApplicationLayer m_Core;

    public Application()
    {
        if(Instance == null)
            Instance = this;
        else
            return;

        m_LayerStack = new LayerStack();
        m_Window = Window.Create(new WindowProps("Particle Feast", 1280, 720, true));

        m_Renderer = new Renderer();
        m_ImGuiLayer = new ImGuiLayer();
        m_Core = new ApplicationLayer(m_Renderer);
        m_LayerStack.PushLayer(m_Core);        
        SceneManager.LoadScene();
    }

    public void Run()
    {
        Time timer = Time.Init();
		float begin = Time.GetTime();
		float end = 0.0f;
		float dt = 0.0f;

        m_ImGuiLayer.OnAwake();

        while(m_Window.WindowRunning())
        {
            end = (float) GLFW.glfwGetTime();
            dt = end - begin;
            begin = end;
            timer.SetDelta(dt);

            m_Renderer.Clear();

            for(Layer layer : m_LayerStack.GetLayers())
                layer.OnUpdate();

            m_ImGuiLayer.Begin(dt);
            for(Layer layer : m_LayerStack.GetLayers())
                layer.OnImGuiRender();
            m_ImGuiLayer.End();

            m_Window.OnUpdate();
        }
    }

    public static Application Get()
    {
        return Instance;
    }

    public Window GetWindow()
    {
        return m_Window;
    }

    public long GetNativeWindow()
    {
        return m_Window.GetWindow();
    }
}
