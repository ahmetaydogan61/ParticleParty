package Engine;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera 
{
    private static Camera Main;
    public Vector3f position;
    private Matrix4f m_Projection;
    private Matrix4f m_View;
    private Matrix4f m_VP;

    public Camera(float width, float height)
    {
        if(Main == null)
            Main = this;
        else 
            return;

        position = new Vector3f();
        m_Projection = new Matrix4f().identity();
        m_Projection.ortho(0.0f, width, 0.0f, height, -1.0f, 1.0f);
        m_View = new Matrix4f().identity();
        m_View.translate(position);
        m_VP = new Matrix4f();
        m_VP = GetViewProjection();
    }

    public void SetOrtho(float width, float height)
    {
        m_Projection = new Matrix4f().identity();
        m_Projection.ortho(0.0f, width, 0.0f, height, -1.0f, 1.0f);
        m_View = new Matrix4f().identity();
        m_View.translate(position);
        m_VP = new Matrix4f();
        m_VP = GetViewProjection();
    }

    public Matrix4f GetViewProjection()
    {
        return m_VP = m_Projection.mul(m_View, m_VP);
    }
   
    public void OnUpdate()
    {
        m_View.translate(position);
    }

    public static Camera Main()
    {
        return Main;
    }
}
