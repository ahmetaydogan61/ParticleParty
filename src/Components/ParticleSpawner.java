package Components;

import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;
import Engine.Component;
import Engine.ApplicationLayer;
import Engine.Entity;
import Engine.Window;
import Events.MouseListener;
import Utils.Maths;
import imgui.ImGui;

public class ParticleSpawner extends Component
{
    private int[] m_MaxParticles = { 4 };
    private float[] m_LifeTime = { 1.75f };
    private float[] m_Speed = { 8f };
    private Vector4f m_BeginColor = new Vector4f(1.0f, 0.0f, 0.0f, 0.3f);
    private Vector4f m_EndColor = new Vector4f(0.0f, 0.4f, 1.0f, 1.0f);
    private Vector3f m_StartScale = new Vector3f(.35f, .35f, 1.0f);
    private Vector3f m_EndScale = new Vector3f(.0f, .0f, 1.0f);
    private float[] m_ColorChangeSpeed = { 4f };
    private float[] m_ScaleSpeed = { 2.5f };
    private Vector3f m_Direction = new Vector3f(.0f, .0f, 1.0f);
    private boolean m_Stop = false;
    private boolean m_FollowMouse = false;
    private boolean m_RandomDir = true;
    private int windowFlags;
    private boolean m_ShowBar = true;

    public void Awake()
    {
        windowFlags = ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoTitleBar;
        ApplicationLayer.OnGuiDraw.AddAction(e->
        {
            OnDrawImGui();
        });
    }

    public void Update()
    {
        if(ImGui.isMouseDown(0) && !m_Stop && !m_FollowMouse)
            for(int i = 0; i < m_MaxParticles[0]; i++)
                SpawnParticle();
        
        if(m_FollowMouse && !m_Stop)
            for(int i = 0; i < m_MaxParticles[0]; i++)
                SpawnParticle();
        
        if(ImGui.isKeyPressed(GLFW.GLFW_KEY_SPACE))
            m_Stop = !m_Stop;
        
        if(ImGui.isKeyPressed(GLFW.GLFW_KEY_M))
            m_FollowMouse = !m_FollowMouse;

        if(ImGui.isKeyPressed(GLFW.GLFW_KEY_R))
            m_RandomDir = !m_RandomDir;

        if(ImGui.isKeyPressed(GLFW.GLFW_KEY_H))
            m_ShowBar = !m_ShowBar;
            
        if(ImGui.isKeyPressed(GLFW.GLFW_KEY_0))
            Window.HideCursor();
        if(ImGui.isKeyPressed(GLFW.GLFW_KEY_1))
            Window.ShowCursor();
    }

    private void SpawnParticle()
    {
        Entity e = new Entity();
        e.transform.position = new Vector3f(MouseListener.GetX(), MouseListener.GetY(), 0);
        e.transform.scale = m_StartScale;
        Particle particle = new Particle();
        particle.lifeTime = m_LifeTime[0];
        particle.speed = m_Speed[0];
        particle.beginColor = m_BeginColor;
        particle.endColor = m_EndColor;
        particle.startScale = m_StartScale;
        particle.endScale = m_EndScale;
        particle.colorChangeSpeed = m_ColorChangeSpeed[0];
        particle.minScaleSpeed = m_ScaleSpeed[0];
        
        if(m_RandomDir)
            particle.direction = new Vector3f(Maths.RandomFloat(-m_Speed[0], m_Speed[0]), Maths.RandomFloat(-m_Speed[0], m_Speed[0]), 0f);
        else
            particle.direction = m_Direction.mul(Maths.RandomFloat(0, m_Speed[0]), Maths.RandomFloat(0, m_Speed[0]), 0.0f, particle.direction);
        
        e.AddComponent(particle);
    }

    @Override
    public void OnDrawImGui()
    {
        if(m_ShowBar)
        {
            float[] begins = {m_BeginColor.x, m_BeginColor.y, m_BeginColor.z, m_BeginColor.w};
            float[] ends = {m_EndColor.x, m_EndColor.y, m_EndColor.z, m_EndColor.w};
            float[] starScl = {m_StartScale.x, m_StartScale.y, m_StartScale.z};
            float[] endScl = {m_EndScale.x, m_EndScale.y, m_EndScale.z};
            float[] direction = {m_Direction.x, m_Direction.y, m_Direction.z};

            ImGui.setNextWindowSize(325, 325);
            ImGui.setNextWindowPos(0, 0);
            
            ImGui.begin("Particle Spawner", new ImBoolean(true), windowFlags);
            ImGui.sliderInt("Max Particles", m_MaxParticles, 0, 15);
            ImGui.sliderFloat("Lifetime", m_LifeTime, 0.0f, 8.0f);
            ImGui.sliderFloat("Speed", m_Speed, 0.0f, 75.0f);
            ImGui.colorEdit4("Begin Color", begins);
            ImGui.colorEdit4("End Color", ends);
            
            ImGui.sliderFloat3("Starting Scale", starScl, 0.0f, 10f);
            ImGui.sliderFloat3("Ending Scale", endScl, 0.0f, 10f);
            
            ImGui.sliderFloat("Color Change Speed", m_ColorChangeSpeed, 0.0f, 10f);
            ImGui.sliderFloat("Scale Speed", m_ScaleSpeed, 0.0f, 20.0f);
            
            ImGui.checkbox("Random Direction (R)", m_RandomDir);
            
            if(!m_RandomDir)
            ImGui.sliderFloat3("Direction", direction, -50.0f, 50.0f);
            
            ImGui.checkbox("Stop (Space)", m_Stop);
            ImGui.checkbox("Follow Mouse (M)", m_FollowMouse);
            ImGui.text("Press H to hide/show the UI");
            ImGui.text("Press 0/1 to hide/show the cursor");
            ImGui.end();

            m_BeginColor.set(begins);
            m_EndColor.set(ends);
            m_StartScale.set(starScl);
            m_EndScale.set(endScl);
            m_Direction.set(direction);
        }
    }
}
