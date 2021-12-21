package Engine;

import static org.lwjgl.glfw.GLFW.*;

import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.callback.ImStrConsumer;
import imgui.callback.ImStrSupplier;
import imgui.flag.ImGuiBackendFlags;
import imgui.flag.ImGuiConfigFlags;
import imgui.flag.ImGuiKey;
import imgui.flag.ImGuiMouseCursor;
import imgui.gl3.ImGuiImplGl3;

public class ImGuiLayer extends Layer
{
	private long glfwWindow;
	private final long[] mouseCursors = new long[ImGuiMouseCursor.COUNT];
	private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();

	@Override
	public void OnAwake()
	{
		glfwWindow = Application.Get().GetNativeWindow();
		ImGui.createContext();
		final ImGuiIO io = ImGui.getIO();
		io.setIniFilename(null); // We don't want to save .ini file
		io.setConfigFlags(ImGuiConfigFlags.NavEnableKeyboard); // Navigation with keyboard
		io.addConfigFlags(ImGuiConfigFlags.DockingEnable);
		io.addConfigFlags(ImGuiConfigFlags.ViewportsEnable);
		io.setBackendFlags(ImGuiBackendFlags.HasMouseCursors); // Mouse cursors to display while resizing windows etc.
		io.setBackendPlatformName("imgui_java_impl_glfw");

		final int[] keyMap = new int[ImGuiKey.COUNT];
		keyMap[ImGuiKey.Tab] = GLFW_KEY_TAB;
		keyMap[ImGuiKey.LeftArrow] = GLFW_KEY_LEFT;
		keyMap[ImGuiKey.RightArrow] = GLFW_KEY_RIGHT;
		keyMap[ImGuiKey.UpArrow] = GLFW_KEY_UP;
		keyMap[ImGuiKey.DownArrow] = GLFW_KEY_DOWN;
		keyMap[ImGuiKey.PageUp] = GLFW_KEY_PAGE_UP;
		keyMap[ImGuiKey.PageDown] = GLFW_KEY_PAGE_DOWN;
		keyMap[ImGuiKey.Home] = GLFW_KEY_HOME;
		keyMap[ImGuiKey.End] = GLFW_KEY_END;
		keyMap[ImGuiKey.Insert] = GLFW_KEY_INSERT;
		keyMap[ImGuiKey.Delete] = GLFW_KEY_DELETE;
		keyMap[ImGuiKey.Backspace] = GLFW_KEY_BACKSPACE;
		keyMap[ImGuiKey.Space] = GLFW_KEY_SPACE;
		keyMap[ImGuiKey.Enter] = GLFW_KEY_ENTER;
		keyMap[ImGuiKey.Escape] = GLFW_KEY_ESCAPE;
		keyMap[ImGuiKey.KeyPadEnter] = GLFW_KEY_KP_ENTER;
		keyMap[ImGuiKey.A] = GLFW_KEY_A;
		keyMap[ImGuiKey.C] = GLFW_KEY_C;
		keyMap[ImGuiKey.V] = GLFW_KEY_V;
		keyMap[ImGuiKey.X] = GLFW_KEY_X;
		keyMap[ImGuiKey.Y] = GLFW_KEY_Y;
		keyMap[ImGuiKey.Z] = GLFW_KEY_Z;
		io.setKeyMap(keyMap);
		mouseCursors[ImGuiMouseCursor.Arrow] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
		mouseCursors[ImGuiMouseCursor.TextInput] = glfwCreateStandardCursor(GLFW_IBEAM_CURSOR);
		mouseCursors[ImGuiMouseCursor.ResizeAll] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
		mouseCursors[ImGuiMouseCursor.ResizeNS] = glfwCreateStandardCursor(GLFW_VRESIZE_CURSOR);
		mouseCursors[ImGuiMouseCursor.ResizeEW] = glfwCreateStandardCursor(GLFW_HRESIZE_CURSOR);
		mouseCursors[ImGuiMouseCursor.ResizeNESW] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
		mouseCursors[ImGuiMouseCursor.ResizeNWSE] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
		mouseCursors[ImGuiMouseCursor.Hand] = glfwCreateStandardCursor(GLFW_HAND_CURSOR);
		mouseCursors[ImGuiMouseCursor.NotAllowed] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);

		glfwSetKeyCallback(glfwWindow, (w, key, scancode, action, mods) -> {
		if (action == GLFW_PRESS)
		{
			io.setKeysDown(key, true);
		}
		else if (action == GLFW_RELEASE)
		{
			io.setKeysDown(key, false);
		}

			io.setKeyCtrl(io.getKeysDown(GLFW_KEY_LEFT_CONTROL) || io.getKeysDown(GLFW_KEY_RIGHT_CONTROL));
			io.setKeyShift(io.getKeysDown(GLFW_KEY_LEFT_SHIFT) || io.getKeysDown(GLFW_KEY_RIGHT_SHIFT));
			io.setKeyAlt(io.getKeysDown(GLFW_KEY_LEFT_ALT) || io.getKeysDown(GLFW_KEY_RIGHT_ALT));
			io.setKeySuper(io.getKeysDown(GLFW_KEY_LEFT_SUPER) || io.getKeysDown(GLFW_KEY_RIGHT_SUPER));
		});

		glfwSetCharCallback(glfwWindow, (w, c) -> {
			if (c != GLFW_KEY_DELETE)
			{
				io.addInputCharacter(c);
			}
		});

		glfwSetMouseButtonCallback(glfwWindow, (w, button, action, mods) -> {
			final boolean[] mouseDown = new boolean[5];

			mouseDown[0] = button == GLFW_MOUSE_BUTTON_1 && action != GLFW_RELEASE;
			mouseDown[1] = button == GLFW_MOUSE_BUTTON_2 && action != GLFW_RELEASE;
			mouseDown[2] = button == GLFW_MOUSE_BUTTON_3 && action != GLFW_RELEASE;
			mouseDown[3] = button == GLFW_MOUSE_BUTTON_4 && action != GLFW_RELEASE;
			mouseDown[4] = button == GLFW_MOUSE_BUTTON_5 && action != GLFW_RELEASE;

			io.setMouseDown(mouseDown);

			if (!io.getWantCaptureMouse() && mouseDown[1])
			{
				ImGui.setWindowFocus(null);
			}
		});

		glfwSetScrollCallback(glfwWindow, (w, xOffset, yOffset) -> {
			io.setMouseWheelH(io.getMouseWheelH() + (float) xOffset);
			io.setMouseWheel(io.getMouseWheel() + (float) yOffset);
		});

		io.setSetClipboardTextFn(new ImStrConsumer() {
			@Override
			public void accept(final String s)
			{
				glfwSetClipboardString(glfwWindow, s);
			}
		});

		io.setGetClipboardTextFn(new ImStrSupplier() {
			@Override
			public String get()
			{
				final String clipboardString = glfwGetClipboardString(glfwWindow);
				if (clipboardString != null)
				{
					return clipboardString;
				}
				else
				{
					return "";
				}
			}
		});

		imGuiGl3.init("#version 330 core");
	}

	public void Begin(final float deltaTime)
	{
		float[] winWidth =
		{ Window.GetWidth() };
		float[] winHeight =
		{ Window.GetHeight() };
		double[] mousePosX =
		{ 0 };
		double[] mousePosY =
		{ 0 };
		glfwGetCursorPos(glfwWindow, mousePosX, mousePosY);

		final ImGuiIO io = ImGui.getIO();
		io.setDisplaySize(winWidth[0], winHeight[0]);
		io.setDisplayFramebufferScale(1f, 1f);
		io.setMousePos((float) mousePosX[0], (float) mousePosY[0]);
		io.setDeltaTime(deltaTime > 0.0f ? deltaTime : 1.0f / 60.0f);

		final int imguiCursor = ImGui.getMouseCursor();
		glfwSetCursor(glfwWindow, mouseCursors[imguiCursor]);
		glfwSetInputMode(glfwWindow, GLFW_CURSOR, Window.m_WindowProps.cursorHidden ? GLFW_CURSOR_HIDDEN : GLFW_CURSOR_NORMAL);
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

	public void DestroyImGui()
	{
		imGuiGl3.dispose();
		ImGui.destroyContext();
	}
}