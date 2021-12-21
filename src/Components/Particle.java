package Components;

import org.joml.Vector3f;
import org.joml.Vector4f;

import Engine.Component;
import Utils.Maths;
import Utils.Time;

public class Particle extends Component
{
    public float lifeTime = 2f;
    public float speed = 4f;
    private float scaleSpeed = 0.2f;
    public float minScaleSpeed = 0.25f;
    public float colorChangeSpeed = 2f;
    
    public Vector3f direction = new Vector3f();
    public Vector3f startScale = new Vector3f(1.0f, 1.0f, 1.0f);
    public Vector3f endScale = new Vector3f();

    public Vector4f beginColor = new Vector4f(1.0f, 0.0f, 0.0f, 0.3f);
    public Vector4f endColor = new Vector4f(0.0f, 0.4f, 1.0f, 1.0f);
    
    public void Awake()
    {
        entity.material.color = beginColor;
        scaleSpeed = Maths.RandomFloat(minScaleSpeed, lifeTime);
    }

    public void Update()
    {
        entity.material.color = Maths.Lerp(entity.material.color, endColor, colorChangeSpeed * Time.Delta());
        entity.transform.scale = Maths.Lerp(entity.transform.scale, endScale, scaleSpeed * Time.Delta());
        entity.transform.position.x += direction.x * speed * Time.Delta(); 
        entity.transform.position.y += direction.y * speed * Time.Delta();
        lifeTime -= Time.Delta();
        if(lifeTime <= 0.0f)
            entity.Destroy();
    }
}
