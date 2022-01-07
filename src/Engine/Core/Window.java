package Engine.Core;

import static org.lwjgl.glfw.GLFW.*;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.IntBuffer;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import Engine.Events.Type.ApplicationShutdownEvent;
import Engine.Utils.ImageParser;

class WindowProps
{
    public String name;
	public int width;
	public int height;
	public boolean VsyncOn;
	public boolean cursorHidden = false;
	public float aspectRatio;

	public WindowProps(String name, int width, int height)
	{
		this.name = name;
		this.width = width;
		this.height = height;
		aspectRatio = (float)width / height;
	}
}

public class Window 
{
    private static long m_Window;
    private static WindowProps m_WindowProps;

    public Window(WindowProps props)
    {
        m_WindowProps = props;
        Init();
    }

    public void Init()
    {
        GLFWErrorCallback.createPrint(System.err).set();

		if (!glfwInit())
		throw new IllegalStateException("Unable to initialize GLFW");

        m_Window = glfwCreateWindow(m_WindowProps.width, m_WindowProps.height, m_WindowProps.name, NULL, NULL);
    
        if (m_Window == NULL)
			throw new IllegalStateException("Failed to create GLFW Window");

		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_DOUBLEBUFFER, GL_TRUE);
		glfwWindowHint(GLFW_MAXIMIZED, GL_FALSE);
		glfwSetScrollCallback(m_Window, Input::ScrollCallback);

		try (MemoryStack stack = stackPush())
		{
			IntBuffer pWidth = stack.mallocInt(1);
			IntBuffer pHeight = stack.mallocInt(1);

			glfwGetWindowSize(m_Window, pWidth, pHeight);

			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

			glfwSetWindowPos(m_Window, (vidmode.width() - pWidth.get(0)) / 2,
					(vidmode.height() - pHeight.get(0)) / 2);
		}

        glfwMakeContextCurrent(m_Window);
		SetVSync(true);
		GL.createCapabilities();
		SetWindowIcon("src/Resources/Textures/Icon.png");
    }

    protected void OnUpdate()
    {
        glfwSwapBuffers(m_Window);
		glfwPollEvents();
    }

    public void OnDetach()
    {
		ApplicationShutdownEvent event = new ApplicationShutdownEvent();
		Application.CallEvent(event);
        
		glfwFreeCallbacks(m_Window);
		glfwDestroyWindow(m_Window);

		glfwTerminate();
		glfwSetErrorCallback(null).free();
    }

    protected boolean WindowRunning()
	{
		return !glfwWindowShouldClose(m_Window);
	}

    public static float GetAspectRatio()
	{
		return m_WindowProps.aspectRatio;
	}

    public static void HideCursor()
	{
		m_WindowProps.cursorHidden = true;
	}

	public static void ShowCursor()
	{
		m_WindowProps.cursorHidden = false;
	}

    public static long GetNativeWindow()
    {
        return m_Window;
    }

    public static int GetWidth()
    {
        return m_WindowProps.width;
    }

    public static int GetHeight()
    {
        return m_WindowProps.height;
    }

	public static WindowProps GetProps()
	{
		return m_WindowProps;
	}

    private void SetVSync(boolean set)
    {
        if(set)
            glfwSwapInterval(1);
        else
            glfwSwapInterval(0);
            
		m_WindowProps.VsyncOn = set;
    }

	private void SetWindowIcon(String filePath)
	{
		ImageParser icon  = ImageParser.LoadImage(filePath);
		GLFWImage image = GLFWImage.malloc(); 
		GLFWImage.Buffer imagebf = GLFWImage.malloc(1);
        image.set(icon.GetWidth(), icon.GetHeight(), icon.GetImage());
        imagebf.put(0, image);
        glfwSetWindowIcon(m_Window, imagebf);
	}
}
