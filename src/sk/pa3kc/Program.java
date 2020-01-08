package sk.pa3kc;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.io.File;
import java.util.Arrays;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import sk.pa3kc.mylibrary.DefaultSystemPropertyStrings;
import sk.pa3kc.mylibrary.util.NumberUtils;
import sk.pa3kc.pojo.Matrix;
import sk.pa3kc.pojo.Vertex;
import sk.pa3kc.singletons.Configuration;
import sk.pa3kc.ui.MainFrame;
import sk.pa3kc.util.Logger;
import sk.pa3kc.util.ObjFile;
import sk.pa3kc.util.Parameters;
import sk.pa3kc.util.UIThread;

public class Program {
    private Program() {}

    public static final String NEWLINE = DefaultSystemPropertyStrings.LINE_SEPARATOR;
    public static final String OS_NAME = DefaultSystemPropertyStrings.OS_NAME;

    public static GraphicsConfiguration GRAPHICS_DEVICE_CONFIG;
    public static Rectangle GRAPHICS_DEVICE_BOUNDS;

    public static float FOV = 90f;

    public static UIThread UI_THREAD;
    public static MainFrame mainFrame;
    public static World world;
    private static Thread loopThread = null;

    public static void main(String[] args) {
        if (args.length == 0) {
            System.exit(1);
        }

        ObjFile obj = null;

        try {
            obj = new ObjFile(new File(args[0]));
        } catch (Throwable ex) {
            ex.printStackTrace();
            System.exit(2);
            return;
        }

        final Parameters params = new Parameters(args);

        for (String optionName : params.getOptionNames()) {
            final String optionValue = params.getOption(optionName);

            if (optionValue != null) {
                final Configuration.Indexer index = Configuration.Indexer.fromString(optionName);

                switch (index) {
                    case MAX_FPS:
                        int maxFps = NumberUtils.tryParseInt(optionValue);
                        if (maxFps != -1) {
                            Configuration.getInst().setMaxFps(maxFps);
                        }
                    break;
                    case MAX_UPS:
                        int maxUps = NumberUtils.tryParseInt(optionValue);
                        if (maxUps != -1) {
                            Configuration.getInst().setMaxUps(maxUps);
                        }
                    break;
                    case MONITOR_INDEX:
                        int monitorIndex = NumberUtils.tryParseInt(optionValue);
                        if (monitorIndex != -1) {
                            Configuration.getInst().setMonitorIndex(monitorIndex);
                        }
                    break;
                    case LINUX_SYNC_WARN_TIME:
                        long linuxSyncWarnTime = NumberUtils.tryParseLong(optionValue);
                        if (linuxSyncWarnTime != -1) {
                            Configuration.getInst().setLinuxSyncWarnTime(linuxSyncWarnTime);
                        }
                    break;
                    case UI_CYCLE_WARN_TIME:
                        long uiCycleWarnTime = NumberUtils.tryParseLong(optionValue);
                        if (uiCycleWarnTime != -1) {
                            Configuration.getInst().setUiCycleWarnTime(uiCycleWarnTime);
                        }
                    break;
                    case DEBUG_ENABLED:
                    break;
                }
            }
        }

        final GraphicsDevice[] devices;

        try {
            devices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
        } catch (HeadlessException ex) {
            Logger.ERROR("No supported display found");
            System.exit(0xFF);
            return;
        }

        final int graphicsDeviceIndex = Configuration.getInst().getMonitorIndex();
        GRAPHICS_DEVICE_CONFIG = devices[NumberUtils.map(graphicsDeviceIndex, 1, devices.length) - 1].getDefaultConfiguration();
        GRAPHICS_DEVICE_BOUNDS = GRAPHICS_DEVICE_CONFIG.getBounds();

        final float aspectRatio = (float)GRAPHICS_DEVICE_BOUNDS.getWidth() / (float)GRAPHICS_DEVICE_BOUNDS.getHeight();
        final float fovRad = (float)StrictMath.toRadians(Program.FOV);
        final float far = 1000f;
        final float near = 0.1f;

        final float[][] matrix = Matrix.PROJECTION_MATRIX.getAllValues();
        matrix[0][0] = aspectRatio * fovRad;
        matrix[1][1] = fovRad;
        matrix[2][2] = far / (far - near);
        matrix[3][2] = (-far * near) / (far - near);
        matrix[2][3] = 1f;
        matrix[3][3] = 0f;

        Program.world = new World(2);
        Program.world.getPlayers()[0] = new Player(new Vertex(0f, 0f, 300f, 1f));
        Program.world.getMesh().addAll(Arrays.asList(obj.getFaces()));
        // Program.UI_THREAD = new UIThread();
        try {
            Display.setDisplayMode(new DisplayMode(GRAPHICS_DEVICE_BOUNDS.width, GRAPHICS_DEVICE_BOUNDS.height));
            Display.setTitle("Test Window");
            Display.create();

            GL11.glMatrixMode(GL11.GL_PROJECTION);
            GL11.glLoadIdentity();
            GL11.glOrtho(0, 0, Display.getWidth(), Display.getHeight(), near, far);

            GL11.glMatrixMode(GL11.GL_MODELVIEW);
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

            GL11.glBegin(GL11.GL_TRIANGLES);
            GL11.glVertex2f(0, 0);
            GL11.glVertex2f(GRAPHICS_DEVICE_BOUNDS.width, GRAPHICS_DEVICE_BOUNDS.height);
            GL11.glVertex2f(GRAPHICS_DEVICE_BOUNDS.width, 0);
            GL11.glEnd();
        } catch (Throwable ex) {
            ex.printStackTrace();
            Display.destroy();
            System.exit(1);
            return;
        }

        loopThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!Display.isCloseRequested()) {
                    Display.update();
                    Display.sync(60);
                }
                Display.destroy();
                System.exit(0);
            }
        });
        loopThread.start();
        // Program.mainFrame = new MainFrame("Test2");
        // Program.UI_THREAD.start();
    }
}
