package Engine.Renderer;

import static org.lwjgl.opengl.GL33.*;

public class Framebuffer 
{
    private int m_Width;
    private int m_Height;
    private int m_RendererID = 0;
    private int m_ColorAttachment;
    private int m_DepthAttachment;

    public Framebuffer(int width, int height)
    {
        this.m_Width = width;
        this.m_Height = height;
        Invalidate();
    }

    public void Invalidate()
    {
        if(m_RendererID != 0)
        {
            Destroy();
        }

        m_RendererID = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, m_RendererID);
        
        m_ColorAttachment = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, m_ColorAttachment);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, m_Width , m_Height, 0, GL_RGBA, GL_UNSIGNED_BYTE, 0);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, m_ColorAttachment, 0);
        
        m_DepthAttachment = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, m_DepthAttachment);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH24_STENCIL8, m_Width, m_Height, 0, GL_DEPTH_STENCIL, GL_UNSIGNED_INT_24_8, 0);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_STENCIL_ATTACHMENT, GL_TEXTURE_2D, m_DepthAttachment, 0);
        
        //if(glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE)
        //    System.out.println("framebuffer err");

        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    public void Resize(int width, int height)
    {
       this.m_Width = width;
       this.m_Height = height;
       Invalidate();
    }

    public void Bind()
    {
        glBindFramebuffer(GL_FRAMEBUFFER, m_RendererID);
    }

    public void Unbind()
    {
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    public void Destroy()
    {
        glDeleteFramebuffers(m_RendererID);
        glDeleteTextures(m_ColorAttachment);
        glDeleteTextures(m_DepthAttachment);
    }

    public int GetColorAttachmentID()
    {
        return m_ColorAttachment;
    }

    public int GetWidth()
    {
        return m_Width;
    }

    public int GetHeight()
    {
        return m_Height;
    }
}
