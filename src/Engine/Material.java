package Engine;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import Renderer.IndexBuffer;
import Renderer.Renderer;
import Renderer.Shader;
import Renderer.VertexArray;
import Renderer.VertexBuffer;
import Renderer.VertexBufferLayout;

public abstract class Material 
{
  public Entity entity;
  public Vector4f color;
  protected Shader m_Shader;
  protected VertexArray m_VertexArray;
  protected VertexBuffer m_VertexBuffer; 
  protected VertexBufferLayout m_BufferLayout;
  protected IndexBuffer m_IndexBuffer;
  
  public Material()
  {
    color = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
  }
  
  public Material(Entity entity)
  {
    this.entity = entity;
    color = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
  }
  
  public abstract void Init();
  
  public void OnRender(Renderer renderer)
  {
    Matrix4f model = new Matrix4f().identity().translate(entity.transform.position).scale(entity.transform.scale);
    Matrix4f mvp = Camera.Main().GetViewProjection();
    mvp = mvp.mul(model);
    m_Shader.Bind();
    m_Shader.UploadUniform4f("u_Color", color.x, color.y, color.z, color.w);
    m_Shader.UploadUniformMat4f("u_MVP", mvp);
  }
  
  protected void Draw(Renderer renderer)
  {
    renderer.Draw(m_VertexArray, m_IndexBuffer);
  }
}
