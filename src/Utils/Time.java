package Utils;

public class Time
{
	private static Time m_Timer = null;
	private static float m_BeginTime = System.nanoTime();
	private static float m_DeltaTime;

	private Time()
	{

	}

	public static Time Init()
	{
		if(m_Timer == null)
			m_Timer = new Time();

		return m_Timer;
	}

	public static float GetTime()
	{
		return (float)((System.nanoTime() - m_BeginTime) * 1E-9);
	}

	public static float Delta()
	{
		return m_DeltaTime;
	}

	public void SetDelta(float dt)
	{
		m_DeltaTime = dt;
	}
}
