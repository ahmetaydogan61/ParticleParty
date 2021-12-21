package Engine;

import org.joml.Vector3f;

import Components.ParticleSpawner;

public class SceneManager 
{
    public static void LoadScene()
    {
        ResourcePool.GetShader("src/Resources/Shaders/Default.shader");

        Entity e = new Entity();
        e.AddComponent(new ParticleSpawner());
        e.transform.scale = new Vector3f();
    }    
}
