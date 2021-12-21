package Renderer;

import static org.lwjgl.opengl.GL30.*;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

public class VertexBuffer 
{
    private int m_RendererId;

    public VertexBuffer(float[] data, int size)
    {
        m_RendererId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, m_RendererId);
        FloatBuffer vertexData = BufferUtils.createFloatBuffer(data.length * Float.BYTES);
		vertexData.put(data);
		vertexData.flip();
        glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STATIC_DRAW);
    }    

    public void Destroy()
    {
        glDeleteBuffers(m_RendererId);
    }

    public void Bind()
    {
        glBindBuffer(GL_ARRAY_BUFFER, m_RendererId);
    }

    public void Unbind()
    {
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }
}
