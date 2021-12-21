package Engine;

import java.util.HashMap;
import java.util.Map;

import Renderer.Shader;

public class ResourcePool 
{
    private static Map<String, Shader> m_Shaders = new HashMap<>();
    
    private ResourcePool(){}

    public static Shader GetShader(String filePath)
    {
        if(m_Shaders.containsKey(filePath))
            return m_Shaders.get(filePath);
        else
        {
            Shader shader = new Shader(filePath);
            m_Shaders.put(filePath, shader);
            return shader;
        }
    }
}
