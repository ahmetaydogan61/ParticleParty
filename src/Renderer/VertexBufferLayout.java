package Renderer;

import static org.lwjgl.opengl.GL15.*;

import java.util.Vector;

class VertexBufferElement
{
    public int type;
	public int count;
	public boolean normalized;

    public VertexBufferElement(int type, int count, Boolean normalized)
    {
        this.type = type;
        this.count = count;
        this.normalized = normalized;
    }

	static int GetSizeOfType(int type)
	{
		switch (type)
		{
			case GL_FLOAT:			return 4;
			case GL_UNSIGNED_INT:   return 4;
			case GL_UNSIGNED_BYTE:  return 1;
		}
		return 0;
	}
}

public class VertexBufferLayout 
{
    private Vector<VertexBufferElement> m_Elements = new Vector<VertexBufferElement>();
	private int m_Stride;
    
    public VertexBufferLayout()
    {
        m_Stride = 0;
    }

    public void Pushf(int count)
    {
        m_Elements.add(new VertexBufferElement(GL_FLOAT, count, false)); 
        m_Stride += VertexBufferElement.GetSizeOfType(GL_FLOAT) * count;
    }

    public void Pushi(int count)
    {
        m_Elements.add(new VertexBufferElement(GL_UNSIGNED_INT, count, false)); 
        m_Stride += VertexBufferElement.GetSizeOfType(GL_UNSIGNED_INT) * count;
    }

    public void Pushc(int count, Class<Character> type)
    {
        m_Elements.add(new VertexBufferElement(GL_UNSIGNED_BYTE, count, true)); 
        m_Stride += VertexBufferElement.GetSizeOfType(GL_UNSIGNED_BYTE) * count;
    }

    public final Vector<VertexBufferElement> GetElements()
    {
        return m_Elements;
    }

    public int GetStride()
    {
        return m_Stride;
    }
}
