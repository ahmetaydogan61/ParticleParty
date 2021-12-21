package Engine;

import org.joml.Vector3f;

public class Transform 
{
    public Vector3f position;    
    public Vector3f scale;    

    public Transform()
    {
        position = new Vector3f();
        scale = new Vector3f(1.0f, 1.0f, 1.0f);
    }
}
