package Renderer;

import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.stb.STBImage.*;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

public class Texture 
{
    private int m_RendererID;
    private ByteBuffer m_LocalBuffer;
    private String m_FilePath;
    private IntBuffer m_Width, m_Height, m_BPP;

    public Texture(String filePath)
    {
        m_FilePath = filePath;
        stbi_set_flip_vertically_on_load(true);

        m_Width = BufferUtils.createIntBuffer(1);
        m_Height = BufferUtils.createIntBuffer(1);
        m_BPP = BufferUtils.createIntBuffer(1);
	    m_LocalBuffer = stbi_load(m_FilePath, m_Width, m_Height, m_BPP, 4);
	
        m_RendererID = glGenTextures();
	    glBindTexture(GL_TEXTURE_2D, m_RendererID);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
	    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
	    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
	    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

	    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, m_Width.get(0), m_Height.get(0), 0, GL_RGBA, GL_UNSIGNED_BYTE, m_LocalBuffer);
	    glBindTexture(GL_TEXTURE_2D, 0);

	    if (m_LocalBuffer != null)
	    	stbi_image_free(m_LocalBuffer);
        else
            System.out.println("Local buffer = null");
        
    }
    
    public void Destroy()
    {
        glDeleteTextures(m_RendererID);
    }

    public void Bind(int slot)
    {
        glActiveTexture(GL_TEXTURE0 + slot);
        glBindTexture(GL_TEXTURE_2D, m_RendererID);
    }

    public void Unbind()
    {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public int GetWidth()
    {
        return m_Width.get(0);
    }

    public int GetHeight()
    {
        return m_Height.get(0);
    }
}
