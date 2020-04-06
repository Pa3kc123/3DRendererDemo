package sk.pa3kc;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.io.File;
import java.nio.IntBuffer;
import java.util.Arrays;

import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCharCallback;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import sk.pa3kc.geom.Drawable;
import sk.pa3kc.mylibrary.DefaultSystemPropertyStrings;
import sk.pa3kc.mylibrary.MyLibrary;
import sk.pa3kc.mylibrary.util.ArgsParser;
import sk.pa3kc.mylibrary.util.NumberUtils;
import sk.pa3kc.pojo.Matrix;
import sk.pa3kc.pojo.Vertex;
import sk.pa3kc.singletons.Configuration;
import sk.pa3kc.ui.MainCanvas;
import sk.pa3kc.ui.MainFrame;
import sk.pa3kc.util.Logger;
import sk.pa3kc.util.ObjFile;
import sk.pa3kc.util.UIThread;

public class Program {
    private Program() {}

    public static final String NEWLINE = DefaultSystemPropertyStrings.LINE_SEPARATOR;
    public static final String OS_NAME = DefaultSystemPropertyStrings.OS_NAME;

    public static GraphicsConfiguration GRAPHICS_DEVICE_CONFIG;
    public static Rectangle GRAPHICS_DEVICE_BOUNDS;

    public static float FOV = 90f;

    public static UIThread UI_THREAD;
    public static World world;
    public static MainFrame MAIN_FRAME;

    public static void main(String[] args) {
        final ArgsParser params = new ArgsParser(args);
        Configuration.setupConfig(params);

        ObjFile obj = null;

        try {
            obj = new ObjFile(new File(params.getArgument(0)));
        } catch (Throwable ex) {
            ex.printStackTrace();
            System.exit(2);
            return;
        }

        final GraphicsDevice[] devices;

        try {
            devices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
        } catch (HeadlessException ex) {
            Logger.ERROR("No supported display found");
            System.exit(0xFF);
            return;
        }

        final int graphicsDeviceIndex = Configuration.getMonitorIndex();
        GRAPHICS_DEVICE_CONFIG = devices[NumberUtils.map(graphicsDeviceIndex, 1, devices.length) - 1].getDefaultConfiguration();
        GRAPHICS_DEVICE_BOUNDS = GRAPHICS_DEVICE_CONFIG.getBounds();

        final float aspectRatio = (float)GRAPHICS_DEVICE_BOUNDS.getWidth() / (float)GRAPHICS_DEVICE_BOUNDS.getHeight();
        final float fovRad = FOV / 180f * 3.14159f;
        final float far = 1000f;
        final float near = 0.1f;

        final float[][] matrix = Matrix.PROJECTION_MATRIX.getAllValues();
        matrix[0][0] = aspectRatio * fovRad;
        matrix[1][1] = fovRad;
        matrix[2][2] = far / (far - near);
        matrix[3][2] = (-far * near) / (far - near);
        matrix[2][3] = 1f;
        matrix[3][3] = 0f;

        world = new World(1);
        world.getPlayers()[0] = new Player(new Vertex(0f, 0f, 1000f, 1f));
        world.getMesh().addAll(Arrays.asList(obj.getFaces()));

        // Setup error callbacks
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        // Configure GLFW
        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_FALSE);

        // Create window
        final long window = GLFW.glfwCreateWindow(GRAPHICS_DEVICE_BOUNDS.width, GRAPHICS_DEVICE_BOUNDS.height, "Test window", MemoryUtil.NULL, MemoryUtil.NULL);
        if (window == MemoryUtil.NULL) {
            throw new IllegalStateException("Unable to create window");
        }

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        GLFW.glfwSetKeyCallback(window, (win, key, scancode, action, mode) -> {
            if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE) {
                GLFW.glfwSetWindowShouldClose(win, true);
            }
        });

        try (final MemoryStack stack = MemoryStack.stackPush()) {
            final IntBuffer pWidth = stack.mallocInt(1);
            final IntBuffer pHeight = stack.mallocInt(1);

            GLFW.glfwGetWindowSize(window, pWidth, pHeight);

            final GLFWVidMode vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());

            GLFW.glfwSetWindowPos(
                window,
                (vidMode.width() - pWidth.get(0)) / 2,
				(vidMode.height() - pHeight.get(0)) / 2
            );
        }

        GLFW.glfwMakeContextCurrent(window);
        GLFW.glfwSwapInterval(0);
        GLFW.glfwShowWindow(window);

        GL.createCapabilities();

        GL11.glClearColor(0f, 0f, 0f, 1f);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0d, GRAPHICS_DEVICE_BOUNDS.getWidth(), GRAPHICS_DEVICE_BOUNDS.getHeight(), 0d, 1, -1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);

        while (!GLFW.glfwWindowShouldClose(window)) {
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

            Program.triangleCounter = 0;
            for (final Drawable drawable : Program.world.getMesh()) {
                drawable.drawGL();
            }
            System.out.println("Triangle counter = " + Program.triangleCounter);

            GLFW.glfwSwapBuffers(window);

            GLFW.glfwPollEvents();
        }

        Callbacks.glfwFreeCallbacks(window);
        GLFW.glfwDestroyWindow(window);

        GLFW.glfwTerminate();
        GLFW.glfwSetErrorCallback(null).set();

        UI_THREAD = new UIThread();
        UI_THREAD.start();

        // MAIN_FRAME = new MainFrame("Test");
    }

    public static int triangleCounter = 0;
}
