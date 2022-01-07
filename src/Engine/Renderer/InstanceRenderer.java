package Engine.Renderer;

import static org.lwjgl.opengl.GL33.*;

import java.nio.FloatBuffer;

import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import Engine.Core.RenderLayer;
import Engine.Utils.Camera;

public class InstanceRenderer extends RenderLayer
{
    private static int m_Index = 0;
    private static int m_MaxInstances = 100000;

    private static float[] m_QuadVertices = 
    {
        -0.5f, -0.5f,   1.0f, 0.0f, 0.0f,   1.0f, 1.0f,  
         0.5f, -0.5f,   0.0f, 1.0f, 0.0f,   1.0f, 1.0f,  
         0.5f,  0.5f,   0.0f, 0.0f, 1.0f,   1.0f, 1.0f,  
        -0.5f,  0.5f,   1.0f, 0.0f, 0.0f,   1.0f, 1.0f 
    };

    private static int[] m_QuadIndices =
    {
        0, 1, 2,
        2, 3, 0
    };

    private int va;
    private int vb;
    private int ivb;
    private int ib;
    private Shader shader;
    private static FloatBuffer m_TranslationBuffer = BufferUtils.createFloatBuffer(m_MaxInstances * (2 + 2 + 3));

    public void OnAwake()
    {
        ivb = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, ivb);
        glBufferData(GL_ARRAY_BUFFER, m_TranslationBuffer, GL_DYNAMIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        va = glGenVertexArrays();
        vb = glGenBuffers();
        
        glBindVertexArray(va);
        glBindBuffer(GL_ARRAY_BUFFER, vb);
        FloatBuffer vertexData = BufferUtils.createFloatBuffer(m_QuadVertices.length);
		vertexData.put(m_QuadVertices);
		vertexData.flip();
        glBufferData(GL_ARRAY_BUFFER, vertexData, GL_DYNAMIC_DRAW);

        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0, 2, GL_FLOAT, false, 7 * Float.BYTES, 0);

        glEnableVertexAttribArray(1);
        glBindBuffer(GL_ARRAY_BUFFER, ivb);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 7 * Float.BYTES, 0);
        
        glEnableVertexAttribArray(2);
        glVertexAttribPointer(2, 3, GL_FLOAT, false, 7 * Float.BYTES, 2 * Float.BYTES);
        
        glEnableVertexAttribArray(3);
        glVertexAttribPointer(3, 2, GL_FLOAT, false, 7 * Float.BYTES, 5 * Float.BYTES);

        glVertexAttribDivisor(1, 1);
        glVertexAttribDivisor(2, 1);
        glVertexAttribDivisor(3, 1);
        
        ib = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ib);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, m_QuadIndices, GL_STATIC_DRAW);
        
        shader = new Shader("src/Resources/Shaders/Default.shader");
        shader.Bind();
        
        glBindBuffer(GL_ARRAY_BUFFER, ivb);
        glBindVertexArray(va);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ib);
    }

    public void OnRender()
    {
        shader.Bind();
        glBindBuffer(GL_ARRAY_BUFFER, ivb);
        glBufferData(GL_ARRAY_BUFFER, m_TranslationBuffer, GL_STATIC_DRAW);
        
        shader.UploadUniformMat4f("u_VP", Camera.GetVP());
        
        glBindVertexArray(va);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ib);
        glDrawElementsInstanced(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0, m_Index / (2 + 2 + 3));
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    private void ClearBuffer()
    {
        m_Index = 0;
        m_TranslationBuffer.clear();
    }

    public static void CreateQuad(Vector2f position, Vector4f color, Vector2f size)
    {
        if(m_Index / (2 + 2 + 3) >= m_MaxInstances) return;
        m_TranslationBuffer.put(m_Index++, position.x);
        m_TranslationBuffer.put(m_Index++, position.y);
        m_TranslationBuffer.put(m_Index++, color.x);
        m_TranslationBuffer.put(m_Index++, color.y);
        m_TranslationBuffer.put(m_Index++, color.z);
        m_TranslationBuffer.put(m_Index++, size.x);
        m_TranslationBuffer.put(m_Index++, size.y);
    }

    @Override
    public void OnDetach() 
    {
        glDeleteBuffers(va);
        glDeleteBuffers(vb);
        glDeleteBuffers(ib);
        glDeleteBuffers(ivb);
        shader.Destroy();
        m_TranslationBuffer.clear();
    }

    @Override
    public void Clear() 
    {
        ClearBuffer();    
    }
}
