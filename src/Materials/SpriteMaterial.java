package Materials;

import Engine.Material;
import Renderer.IndexBuffer;
import Renderer.Renderer;
import Renderer.Shader;
import Renderer.Texture;
import Renderer.VertexArray;
import Renderer.VertexBuffer;
import Renderer.VertexBufferLayout;

public class SpriteMaterial extends Material
{
    protected Texture m_Texture;

    float vertices[] =
	{ 
		//positions  	//texCoords
	   -50.0f,   -50.0f, 	0.0f, 0.0f,
		50.0f,   -50.0f, 	1.0f, 0.0f,
		50.0f,    50.0f, 	1.0f, 1.0f,
	   -50.0f,    50.0f, 	0.0f, 1.0f
	};

	int indices[] =
	{ 
		0, 1, 2,
	  	2, 3, 0 
	};

    @Override
    public void Init() 
    {
        m_VertexArray = new VertexArray();
        m_VertexBuffer = new VertexBuffer(vertices, vertices.length * Float.BYTES);
        m_BufferLayout = new VertexBufferLayout();
        m_BufferLayout.Pushf(2);
        m_BufferLayout.Pushf(2);
        m_VertexArray.AddBuffer(m_VertexBuffer, m_BufferLayout);

        m_IndexBuffer = new IndexBuffer(indices, indices.length);
        m_Shader = new Shader("src/Resources/Shaders/DefaultSprite.shader");
        m_Shader.Bind();
        m_Shader.UploadUniform4f("u_Color", color.x, color.y, color.z, color.w);

        m_Texture = new Texture("src/Resources/Textures/image.png");
		m_Texture.Bind(0);
		m_Shader.UploadUniform1i("u_Texture", 0);

        m_VertexArray.Unbind();
		m_VertexBuffer.Unbind();
		m_IndexBuffer.Unbind();
		m_Shader.Unbind();
    }

    @Override
    public void OnRender(Renderer renderer)
    {
        super.OnRender(renderer);;
        Draw(renderer);
    }
}
