package Renderer;

import static org.lwjgl.opengl.GL30.*;

import java.util.Vector;

public class VertexArray 
{
    private int m_RendererId;
    
    public VertexArray()
    {
        m_RendererId = glGenVertexArrays();
    }

    public void Destroy()
    {
        glDeleteVertexArrays(m_RendererId);
    }

    public void AddBuffer(VertexBuffer vb, VertexBufferLayout layout)
    {
        Bind();
        vb.Bind();
        Vector<VertexBufferElement> elements = layout.GetElements();
        int offset = 0;
	    for (int i = 0; i < elements.size(); i++)
	    {
	    	VertexBufferElement element = elements.get(i);
	    	glEnableVertexAttribArray(i);
	    	glVertexAttribPointer(i, element.count, element.type, element.normalized, layout.GetStride(), offset);
            offset += element.count * VertexBufferElement.GetSizeOfType(element.type);
	    }
    }

    public void Bind()
    {
        glBindVertexArray(m_RendererId);
    }

    public void Unbind()
    {
        glBindVertexArray(0);
    }
}
