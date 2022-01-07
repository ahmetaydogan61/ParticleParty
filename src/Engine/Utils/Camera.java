package Engine.Utils;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import Engine.Editor.EditorLayer;

public class Camera 
{
    private static Camera Main;
    private float aspectRatio;
    private Matrix4f m_ProjectionMatrix;
    private Matrix4f m_ViewMatrix;
    private Matrix4f m_ViewProjectionMatrix;
    private Vector3f m_Position;
    private float m_ZoomLevel = 1.0f;

    public Camera()
    {
        Main = this;
        float left = 0, right = 0, bottom = 0, top = 0;
        aspectRatio = EditorLayer.GetAspectRatio();
        left = -aspectRatio * m_ZoomLevel;
        right = aspectRatio * m_ZoomLevel;
        bottom = -m_ZoomLevel;
        top = m_ZoomLevel;
        m_Position = new Vector3f();
        m_ProjectionMatrix = new Matrix4f().identity().ortho(left, right, bottom, top, -1.0f, 1.0f);
        m_ViewMatrix = new Matrix4f().identity();
        
        m_ViewProjectionMatrix = new Matrix4f();
        m_ProjectionMatrix.mul(m_ViewMatrix, m_ViewProjectionMatrix);
    }

    public void SetProjection()
    {
        float left = 0, right = 0, bottom = 0, top = 0;
        aspectRatio = EditorLayer.GetAspectRatio();
        left = -aspectRatio * m_ZoomLevel;
        right = aspectRatio * m_ZoomLevel;
        bottom = -m_ZoomLevel;
        top = m_ZoomLevel;
        m_ProjectionMatrix = new Matrix4f().identity().ortho(left, right, bottom, top, -1.0f, 1.0f);
        m_ViewProjectionMatrix = new Matrix4f().identity();
        GetViewProjectionMatrix();
    }
   
    private Matrix4f GetViewProjectionMatrix()
    {
        return m_ProjectionMatrix.mul(m_ViewMatrix, m_ViewProjectionMatrix);
    }

    private void RecalculateViewMatrix()
	{
        Matrix4f transform = new Matrix4f().identity();
        m_ViewMatrix = transform.translate(m_Position).invert();
        GetViewProjectionMatrix();
	}

    public void SetPosition(Vector3f position)
    {
        m_Position = position;
        RecalculateViewMatrix();
    }

    public void SetZoom(float zoomLevel)
    {
        m_ZoomLevel = zoomLevel;
        SetProjection();
    }

    public float GetZoom()
    {
        return m_ZoomLevel;
    }

    public static Camera Main()
    {
        return Main;
    }

    public static Matrix4f GetVP()
    {
        return Main.GetViewProjectionMatrix();
    }
}
