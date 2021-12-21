package Engine;

import static org.lwjgl.glfw.GLFW.*;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import Events.Input;
import Events.MouseListener;
import Utils.ImageParser;

class WindowProps
{
	public String name;
	public int width;
	public int height;
	public boolean VsyncOn;
	public boolean fullscreen;
	public boolean cursorHidden = false;

	public WindowProps(String name, int width, int height, boolean fullscreen)
	{
		this.name = name;
		this.width = width;
		this.height = height;
		this.fullscreen = fullscreen;
	}
}

public class Window
{
	private static Window m_Window = null;
	private long m_GlfwWindow;
	protected static WindowProps m_WindowProps;
	
	public Window()
	{
		Init();
	}
	public static Window Create(WindowProps props)
	{
		m_WindowProps = props;
		m_Window = new Window();
		return m_Window;
	}

	private void Init()
	{
		GLFWErrorCallback.createPrint(System.err).set();

		if (!glfwInit())
		throw new IllegalStateException("Unable to initialize GLFW");
			
		if(m_WindowProps.fullscreen)
		{
			m_WindowProps.width = 1920;
			m_WindowProps.height = 1080;
			m_GlfwWindow = glfwCreateWindow(m_WindowProps.width, m_WindowProps.height, m_WindowProps.name, glfwGetPrimaryMonitor(), NULL);
		}
		else
			m_GlfwWindow = glfwCreateWindow(m_WindowProps.width, m_WindowProps.height, m_WindowProps.name, NULL, NULL);


		if (m_GlfwWindow == NULL)
			throw new IllegalStateException("Failed to create GLFW Window");

		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_DOUBLEBUFFER, GL_TRUE);
		
		glfwSetCursorPosCallback(m_GlfwWindow, MouseListener::MousePositionCallback);
		glfwSetMouseButtonCallback(m_GlfwWindow, MouseListener::MouseButtonCallback);
		glfwSetScrollCallback(m_GlfwWindow, MouseListener::MouseScrollCallback);
		glfwSetKeyCallback(m_GlfwWindow, Input::KeyCallback);
		glfwSetWindowSizeCallback(m_GlfwWindow, (w, width,height) ->
		{
			m_WindowProps.width = width;
			m_WindowProps.height = height;
		});

		glfwSetFramebufferSizeCallback(m_GlfwWindow, (w, width,height) ->
		{
			IntBuffer wi, he;
			wi = BufferUtils.createIntBuffer(1);
			he = BufferUtils.createIntBuffer(1);
			glfwGetFramebufferSize(m_GlfwWindow, wi, he);
			glViewport(0, 0, wi.get(0), he.get(0));
			Camera.Main().SetOrtho(width, height);
		});

		try (MemoryStack stack = stackPush())
		{
			IntBuffer pWidth = stack.mallocInt(1);
			IntBuffer pHeight = stack.mallocInt(1);

			glfwGetWindowSize(m_GlfwWindow, pWidth, pHeight);

			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

			glfwSetWindowPos(m_GlfwWindow, (vidmode.width() - pWidth.get(0)) / 2,
					(vidmode.height() - pHeight.get(0)) / 2);
		}

		glfwMakeContextCurrent(m_GlfwWindow);
		SetVSync(true);
		GL.createCapabilities();
		SetWindowIcon("src/Resources/Textures/Icon.png");

		glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}

	public void OnDetach()
	{
		glfwFreeCallbacks(m_GlfwWindow);
		glfwDestroyWindow(m_GlfwWindow);

		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}

	public long GetWindow()
	{
		return m_GlfwWindow;
	}

	public static int GetWidth() 
	{
		return m_WindowProps.width;
	}

    public static int GetHeight()
	{
        return m_WindowProps.height;
    }

	protected void OnUpdate()
	{
		glfwSwapBuffers(m_GlfwWindow);
		glfwPollEvents();
	}

	protected boolean WindowRunning()
	{
		return !glfwWindowShouldClose(m_GlfwWindow);
	}

	private void SetVSync(boolean enabled)
	{
		if(enabled)
			glfwSwapInterval(1);
		else
			glfwSwapInterval(0);
		m_WindowProps.VsyncOn = enabled;
	}

	public static void HideCursor()
	{
		m_WindowProps.cursorHidden = true;
	}

	public static void ShowCursor()
	{
		m_WindowProps.cursorHidden = false;
	}

	private void SetWindowIcon(String filePath)
	{
		ImageParser icon  = ImageParser.LoadImage(filePath);
		GLFWImage image = GLFWImage.malloc(); 
		GLFWImage.Buffer imagebf = GLFWImage.malloc(1);
        image.set(icon.GetWidth(), icon.GetHeight(), icon.GetImage());
        imagebf.put(0, image);
        glfwSetWindowIcon(m_GlfwWindow, imagebf);
	}
}
