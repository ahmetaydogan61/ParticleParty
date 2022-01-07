package Engine.Core;

import static org.lwjgl.opengl.GL33.*;

import java.util.ArrayList;
import java.util.List;
import Engine.Renderer.Framebuffer;
import Engine.Renderer.InstanceRenderer;

public class Renderer 
{
    private static Framebuffer m_Framebuffer;
    private static List<RenderLayer> m_LayerStack = new ArrayList<>();

    private Renderer()
    {

    }
    
    public static void AddRenderLayer(RenderLayer layer)
    {
        m_LayerStack.add(layer);
        layer.OnAwake();
    }

    public static void RemoveRenderLayer(RenderLayer layer)
    {
        m_LayerStack.remove(layer);
        layer.OnDetach();
    }

    protected static void Init()
    {
        m_Framebuffer = new Framebuffer(Window.GetWidth(), Window.GetHeight());
        AddRenderLayer(new InstanceRenderer());
    }
    
    protected static void Clear()
    {
        m_Framebuffer.Bind();
        glViewport(0, 0, m_Framebuffer.GetWidth(), m_Framebuffer.GetHeight());
        glClear(GL_COLOR_BUFFER_BIT);

        for(RenderLayer layer : m_LayerStack)
            layer.Clear();
    }


    protected static void OnRender()
    {
        for(RenderLayer layer : m_LayerStack)
            layer.OnRender();

        m_Framebuffer.Unbind();
    }

    public static Framebuffer GetFramebuffer()
    {
        return m_Framebuffer;
    }
}
