package Engine;

import Renderer.Renderer;
import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;
import static org.lwjgl.opengl.GL15.glClearColor;

import Events.Event;

public class ApplicationLayer extends Layer
{
    private Camera m_Camera;
    private Renderer m_Renderer;
    public static Event OnGuiDraw;

    public ApplicationLayer(Renderer renderer)
    {
        m_Renderer = renderer;
        OnGuiDraw = new Event();
    }

    @Override
    public void OnAwake()
    {
        m_Camera = new Camera(Window.GetWidth(), Window.GetHeight());
        OnGuiDraw.AddAction(e->
        {
            DrawDebug();
        });
    }    

    @Override
    public void OnUpdate()
    {
        m_Camera.OnUpdate();

        for(Entity e : Register.awakes)
        {
            Register.entities.add(e);
            e.Awake();
        }
        Register.awakes.clear();

        for(Entity e : Register.entities)
        {
            e.Update();				
            e.material.OnRender(m_Renderer);
        }

        for(Entity e : Register.destorys)
            Register.entities.remove(e);			
        
        Register.destorys.clear();

    }

    int windowFlags = ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoTitleBar;
    float[] clearColor = {0.1f, 0.1f, 0.1f, 1.0f};

    @Override
    public void OnImGuiRender()
    {
        OnGuiDraw.DoAction();
    }

    private void DrawDebug()
    {
        ImGui.setNextWindowSize(355, 55);
		ImGui.setNextWindowPos(Window.GetWidth() - 355, Window.GetHeight() - 50);

		ImGui.begin("Debug", new ImBoolean(true), windowFlags);
		ImGui.colorEdit4("Background Color", clearColor);
		ImGui.text(String.format("Application average %.1f ms/frame (%.1f FPS)", 1000.0f / ImGui.getIO().getFramerate(), ImGui.getIO().getFramerate()));
		ImGui.end();

        glClearColor(clearColor[0], clearColor[1], clearColor[2], clearColor[3]);
    }
}
