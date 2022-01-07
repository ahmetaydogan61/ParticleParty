package Engine.Core;

public class Time 
{
    private static float m_DeltaTime = 0.0f;
    private static float m_FixedDeltaTime = 0.0f;
    protected static void SetDelta(float dt) { m_DeltaTime = dt; }
    public static void SetFixedDelta(float dt) { m_FixedDeltaTime = dt; }
    public static float Delta() { return m_DeltaTime; }
    public static float FixedDelta() { return m_FixedDeltaTime; }
}
