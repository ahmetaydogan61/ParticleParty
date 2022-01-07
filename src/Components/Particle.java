package Components;

import org.joml.Vector2f;
import org.joml.Vector4f;

import Engine.Core.Time;
import Engine.ECS.Component;
import Engine.ECS.Entity;
import Engine.ECS.Material;
import Engine.Utils.Maths;
import imgui.ImGui;

public class Particle extends Component
{
    public boolean play = false;
    public int[] particleCount = { 15 };
    public float[] lifeTime = { 2f };
    public float[] speed = { 1f };
    
    public float[] startColor = { 1.0f, 0.0f, 0.0f, 0.3f };
    public float[] endColor = { 0.0f, 0.4f, 1.0f, 1.0f };
    public float[] colorChangeSpeed = { 2f };
    
    public float[] startScale = { .35f, .35f };
    public float[] endScale = { 0.0f, 0.0f };
    public float[] scaleSpeed = { 2f , 10f };
    public boolean scaleRandom = false;
    
    public int[] direction = { 0, 0 };
    public float[] positionGap = { 0f, 0f };
    
    private float m_Sensivity = 2f;

    public void Update()
    {
        if(play)
            for(int i = 0; i < particleCount[0]; i++)
            {
                ParticleObject p = new ParticleObject();
                p.transform.position.x = entity.transform.position.x + Maths.RandomFloat(-positionGap[0] / 2, positionGap[0] / 2);
                p.transform.position.y = entity.transform.position.y + Maths.RandomFloat(-positionGap[1] / 2, positionGap[1] / 2);
            
                p.lifeTime = lifeTime[0];

                p.startColor = new Vector4f(startColor[0], startColor[1], startColor[2], startColor[3]);
                p.endColor = new Vector4f(endColor[0], endColor[1], endColor[2], endColor[3]);

                p.startScale = new Vector2f(startScale[0], startScale[1]);
                p.endScale = new Vector2f(endScale[0], endScale[1]);

                Vector2f directionVector = new Vector2f();
                if(direction[0] > 0)
                    directionVector.x = Maths.RandomFloat(0, speed[0]); 
                else if(direction[0] < 0)
                    directionVector.x = Maths.RandomFloat(-speed[0], 0); 
                else
                    directionVector.x = Maths.RandomFloat(-speed[0], speed[0]); 

                if(direction[1] > 0)
                    directionVector.y = Maths.RandomFloat(0, speed[0]); 
                else if(direction[1] < 0)
                    directionVector.y = Maths.RandomFloat(-speed[0], 0); 
                else
                    directionVector.y = Maths.RandomFloat(-speed[0], speed[0]); 
                p.direction = directionVector;

                p.speed = speed[0];
                p.colorChangeSpeed = colorChangeSpeed[0];

                if(scaleRandom)
                    p.scaleSpeed = Maths.RandomFloat(scaleSpeed[0], scaleSpeed[1]);
                else
                    p.scaleSpeed = scaleSpeed[0];
            }
    }

    public void OnDrawGui()
    {
        ImGui.beginChild("Particle");
        
        if(ImGui.checkbox("Play", play))
        {
            play = !play;
        }

        ImGui.sliderInt("Particle Count", particleCount, 0, 100);
        ImGui.dragFloat("Lifetime", lifeTime, Time.Delta() * m_Sensivity, 0, 10);
        ImGui.dragFloat("Speed", speed, Time.Delta() * m_Sensivity, 0, Float.MAX_VALUE);
        
        ImGui.colorEdit4("Start Color", startColor);
        ImGui.colorEdit4("End Color", endColor);
        ImGui.dragFloat("Color Change Speed", colorChangeSpeed, Time.Delta() * m_Sensivity, 0, 150);

        ImGui.dragFloat2("Start Scale", startScale, Time.Delta() * m_Sensivity, 0, Float.MAX_VALUE);
        ImGui.dragFloat2("End Scale", endScale, Time.Delta() * m_Sensivity , 0, Float.MAX_VALUE);

        if(scaleRandom)
            ImGui.dragFloat2("Scale Speed", scaleSpeed, Time.Delta() * m_Sensivity, 1, 150);
        else
            ImGui.dragFloat("Scale Speed", scaleSpeed, Time.Delta() * m_Sensivity, 1, 150);

        if(ImGui.checkbox("Scale Randomly", scaleRandom))
            scaleRandom = !scaleRandom;

        ImGui.sliderInt2("Direction", direction, -1, 1);
        ImGui.dragFloat2("Position Gap", positionGap, Time.Delta() * m_Sensivity , -Float.MAX_VALUE, Float.MAX_VALUE);
        ImGui.endChild();
    }
}

class ParticleObject extends Entity
{
    public Vector4f startColor = new Vector4f(1.0f, 0.0f, 0.0f, 0.3f);
    public Vector4f endColor = new Vector4f(0.0f, 0.4f, 1.0f, 1.0f);
    
    public Vector2f startScale = new Vector2f(.15f, .15f);
    public Vector2f endScale = new Vector2f();

    public float lifeTime = 2.25f;
    public float speed = 2f;
    public float scaleSpeed = 0f;
    public float colorChangeSpeed = 4f;
    public Vector2f direction = new Vector2f();

    public void Awake()
    {
        super.Awake();
        material = new Material();
        material.color = startColor;
        transform.scale = startScale;
    }

    public void Update()
    {
        material.color = Maths.Lerp(material.color, endColor, colorChangeSpeed * Time.Delta());
        transform.scale = Maths.Lerp(transform.scale, endScale, scaleSpeed * Time.Delta());
        transform.position.x += direction.x * speed * Time.Delta(); 
        transform.position.y += direction.y * speed * Time.Delta();
        lifeTime -= Time.Delta();
        if(lifeTime <= 0.0f)
            Destroy();
    }
}