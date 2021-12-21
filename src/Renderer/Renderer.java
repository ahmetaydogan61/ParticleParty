package Renderer;

import static org.lwjgl.opengl.GL30.*;

import Engine.Layer;

public class Renderer extends Layer
{
    public void Clear()
    {
        glClear(GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void OnAwake()
    {

    }

    public void Draw(VertexArray va, IndexBuffer ib)
    {
        va.Bind();
        ib.Bind();
        glDrawElements(GL_TRIANGLES, ib.GetCount(), GL_UNSIGNED_INT, 0);
    }
}
