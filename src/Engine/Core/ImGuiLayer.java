package Engine.Core;

import static org.lwjgl.glfw.GLFW.*;

import Engine.Events.Event;
import Engine.Events.EventDispatcher;
import Engine.Events.Type.MouseScrolledEvent;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiConfigFlags;
import imgui.flag.ImGuiStyleVar;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;

public class ImGuiLayer extends Layer
{
	private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();
	private final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
	private static boolean m_AllowEvent;

    public void OnAwake()
    {
        ImGui.createContext();
        final ImGuiIO io = ImGui.getIO();
		io.setIniFilename("src/Resources/DefaultLayout.ini");
		io.setWantSaveIniSettings(true);
		io.addConfigFlags(ImGuiConfigFlags.DockingEnable);
		io.setBackendPlatformName("imgui_java");

		glfwSetKeyCallback(Window.GetNativeWindow(), (window, key, scan, action, mods)->
		{
			if(!m_AllowEvent)
				return;
				
			if (action == GLFW_PRESS)
				io.setKeysDown(key, true);
			else if (action == GLFW_RELEASE)
				io.setKeysDown(key, false);

			io.setKeyCtrl(io.getKeysDown(GLFW_KEY_LEFT_CONTROL) || io.getKeysDown(GLFW_KEY_RIGHT_CONTROL));
			io.setKeyShift(io.getKeysDown(GLFW_KEY_LEFT_SHIFT) || io.getKeysDown(GLFW_KEY_RIGHT_SHIFT));
			io.setKeyAlt(io.getKeysDown(GLFW_KEY_LEFT_ALT) || io.getKeysDown(GLFW_KEY_RIGHT_ALT));
			io.setKeySuper(io.getKeysDown(GLFW_KEY_LEFT_SUPER) || io.getKeysDown(GLFW_KEY_RIGHT_SUPER));
		});

		glfwSetCharCallback(Window.GetNativeWindow(), (w, c) -> 
		{
			if(!m_AllowEvent)
				return;

			if (c != GLFW_KEY_DELETE)
				io.addInputCharacter(c);
		});

		ImGui.pushStyleVar(ImGuiStyleVar.FrameRounding, 6);
		ImGui.pushStyleVar(ImGuiStyleVar.ItemSpacing, 8f, 6f);
		ImGui.pushStyleVar(ImGuiStyleVar.FramePadding, 4f, 4f);
		ImGui.pushStyleVar(ImGuiStyleVar.PopupRounding, 6f);
		ImGui.pushStyleVar(ImGuiStyleVar.TabRounding, 6f);
		ImGui.pushStyleVar(ImGuiStyleVar.WindowRounding, 6f);
		ImGui.pushStyleColor(ImGuiStyleVar.PopupBorderSize, 0);
		ImGui.pushStyleColor(ImGuiStyleVar.WindowBorderSize, 0);

        imGuiGlfw.init(Window.GetNativeWindow(), false);
		imGuiGl3.init("#version 330 core");
    }

    public void Begin(final float deltaTime)
	{
		imGuiGlfw.newFrame();
		glfwSetInputMode(Window.GetNativeWindow(), GLFW_CURSOR, Window.GetProps().cursorHidden ? GLFW_CURSOR_HIDDEN : GLFW_CURSOR_NORMAL);
		ImGui.newFrame();
	}

    public void End()
	{
		ImGui.render();
		imGuiGl3.renderDrawData(ImGui.getDrawData());
		
		if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable))
		{
			final long win = glfwGetCurrentContext();
			ImGui.updatePlatformWindows();
			ImGui.renderPlatformWindowsDefault();
			glfwMakeContextCurrent(win);
		}
	}

	public void OnEvent(Event e)
    {
		if(m_AllowEvent)
		{
			EventDispatcher dispatcher = new EventDispatcher(e);
			dispatcher.dispatch(Event.Type.MOUSE_SCROLLED, (Event event) -> (OnMouseScrolled((MouseScrolledEvent) event)));
		}
    }

    private boolean OnMouseScrolled(MouseScrolledEvent e)
    {
		final ImGuiIO io = ImGui.getIO();
		io.setMouseWheel(e.ScrollY());
		io.setMouseWheelH(e.ScrollX());
        return true;
    }

	public static void BlockEvent(boolean allow)
	{
		m_AllowEvent = allow;
	}
}
