package sk.pa3kc.ui;

import sk.pa3kc.util.UIThread;

import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.glfw.GLFWCursorPosCallbackI;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallbackI;
import org.lwjgl.glfw.GLFWMouseButtonCallbackI;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

public class GLWindow {
    public static final long GL_NULL = MemoryUtil.NULL;

    private final int width;
    private final int height;
    private final UIThread uiThread;

    private boolean initialized = false;
    public long window = GL_NULL;

    private final GLFWKeyCallbackI keyboardCallback;
    private final GLFWMouseButtonCallbackI mouseButtonCallback;
    private final GLFWCursorPosCallbackI mouseMoveCallback;

    public GLWindow(int width, int height) {
        this.width = width;
        this.height = height;

        this.uiThread = new UIThread() {
            @Override
            public void init() {
                glfwMakeContextCurrent(window);
                GL.createCapabilities();
                GL11.glClearColor(0f, 0f, 0f, 1f);
            }

            @Override
            public void dispose() {
                if (window != GL_NULL) {
                    glfwSetWindowShouldClose(window, true);
                    glfwDestroyWindow(window);
                    glfwMakeContextCurrent(GL_NULL);
                }
            }
        };

        this.uiThread.getUpdatables().add(() -> {
            glfwPollEvents();

            if (this.window != GL_NULL && glfwWindowShouldClose(this.window)) {
                this.uiThread.stop();
            }
        });

        this.keyboardCallback = (long window, int key, int scancode, int action, int mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS && this.uiThread != null) {
                this.uiThread.stop();
            }
        };
        this.mouseButtonCallback = (long window, int button, int action, int mods) -> {};
        this.mouseMoveCallback = (long window, double xpos, double ypos) -> {};
    }

    public int getWidth() {
        return this.width;
    }
    public int getHeight() {
        return this.height;
    }
    public boolean isInitialized() {
        return this.initialized;
    }
    public UIThread getUIThread() {
        return this.uiThread;
    }

    public void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            throw new IllegalStateException("GLFW cannot be initialized on this machine");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

        this.window = glfwCreateWindow(this.width, this.height, "GLWindow", GL_NULL, GL_NULL);

        if (this.window == GL_NULL) {
            throw new IllegalStateException("Window failed to create");
        }

        // Setting window to middle of the screen
        final GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(this.window, (vidMode.width() - this.width) / 2, (vidMode.height() - this.height) / 2);

        // Callbacks
        glfwSetKeyCallback(this.window, this.keyboardCallback);
        glfwSetMouseButtonCallback(this.window, this.mouseButtonCallback);
        glfwSetCursorPosCallback(this.window, this.mouseMoveCallback);

        // Showing window
        glfwMakeContextCurrent(this.window);
        glfwShowWindow(this.window);

        // Clearing context of window so other thread can fight for it :D
        glfwMakeContextCurrent(GL_NULL);

        this.initialized = true;
        this.uiThread.setWindow(this.window);
        this.uiThread.start();
    }

    public void swapBuffers() {
        glfwSwapBuffers(this.window);
    }

    public void dispose() {
        if (this.window != GL_NULL) {
            glfwDestroyWindow(this.window);
        }
    }
}
