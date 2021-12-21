package Renderer;

import static org.lwjgl.opengl.GL15.*;

public class IndexBuffer 
{
    private int m_RendererId;
    private int m_Count;

    public IndexBuffer(int[] data, int count)
    {
        m_Count = count;
        m_RendererId = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, m_RendererId);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, data, GL_STATIC_DRAW);
    }
    
    public void Destroy()
    {
        glDeleteBuffers(m_RendererId);
    }

    public void Bind()
    {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, m_RendererId);
    }

    public void Unbind()
    {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    public int GetCount()
    {
        return m_Count;
    }
}
