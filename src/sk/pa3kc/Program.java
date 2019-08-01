package sk.pa3kc;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import sk.pa3kc.enums.UpdateMode;
import sk.pa3kc.matrix.Matrix;
import sk.pa3kc.matrix.MatrixEditor;
import sk.pa3kc.mylibrary.DefaultSystemPropertyStrings;
import sk.pa3kc.mylibrary.cmd.CmdUtils;
import sk.pa3kc.mylibrary.util.StringUtils;
import sk.pa3kc.singletons.Keyboard;
import sk.pa3kc.singletons.Locks;
import sk.pa3kc.ui.MyFrame;
import sk.pa3kc.util.UIThread;

public class Program {
    public static final String NEWLINE = DefaultSystemPropertyStrings.LINE_SEPARATOR;
    public static final String OS_NAME = DefaultSystemPropertyStrings.OS_NAME;
    public static final double FRAME_LIMIT = 60d;

    private static Program instance;

    public static int CHOOSEN_GRAPHICS_DEVICE;
    public static GraphicsConfiguration GRAPHICS_DEVICE_CONFIG;
    public static Rectangle GRAPHICS_DEVICE_BOUNDS;

    public static MyFrame mainFrame = null;

    public static boolean toggled = false;

    public static UIThread uiThread = new UIThread(60, 60);

    private Program(int graphicsDeviceIndex) {
        GraphicsDevice[] devices = null;
        try {
            devices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
        } catch (HeadlessException ex) {
            ERROR(this, "No supported display found");
            System.exit(0xFF);
        }
        Program.CHOOSEN_GRAPHICS_DEVICE = graphicsDeviceIndex < 0 ? 0 : graphicsDeviceIndex > devices.length - 1 ? devices.length - 1 : graphicsDeviceIndex;
        Program.GRAPHICS_DEVICE_CONFIG = devices[Program.CHOOSEN_GRAPHICS_DEVICE].getDefaultConfiguration();
        Program.GRAPHICS_DEVICE_BOUNDS = Program.GRAPHICS_DEVICE_CONFIG.getBounds();

        Keyboard.getInst().getKeyInfo('g').addOnPressedAction(new Runnable() {
            @Override
            public void run() {
                Program.toggled = !Program.toggled;

                if (Program.toggled)
                synchronized (Locks.KEYBOARD_LOCK) {
                    Locks.KEYBOARD_LOCK.notify();
                }
            }
        });

        Program.uiThread.addUpdateRunnables(new Runnable() {
            @Override
            public void run() {
                Program.mainFrame.update(UpdateMode.ALL);
            }
        });
        Program.uiThread.addRenderRunnables(new Runnable() {
            @Override
            public void run() {
                if (Program.toggled) {
                    try {
                        SwingUtilities.invokeAndWait(new Runnable() {
                            @Override
                            public void run() {
                                Program.mainFrame.myPanel.repaint();
                            }
                        });
                    } catch (Throwable ex) {
                        if (ex instanceof InvocationTargetException) ex.printStackTrace();
                        else if (ex instanceof InterruptedException) ex.printStackTrace();
                        else ex.printStackTrace();
                        System.err.println("Multiple errors occured during drawing cycle... Exiting");
                        System.exit(0xFF);
                    }
                }
            }
        });
    }

    public static Program getInst() { return instance; }

    public static <T> T castOrNull(Object value, Class<T> type) {
        try {
            return type.cast(value);
        } catch (ClassCastException ignored) {
            return null;
        }
    }

    public static void main(String[] args) {
        System.setProperty("sun.java2d.opengl", "false");
        instance = new Program(args.length != 0 ? Integer.parseInt(args[0]) - 1 : 0);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                mainFrame = new MyFrame("Test2");
            }
        });

        synchronized (Locks.MY_FRAME_LOCK) {
            try {
                Locks.MY_FRAME_LOCK.wait();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        Program.uiThread.start();
    }

    private enum LogTag {
        ERROR,
        DEBUG,
        INFO
    }
    public static void ERROR(Object obj, String message) { log(obj, message, LogTag.ERROR); }
    public static void DEBUG(Object obj, String message) { log(obj, message, LogTag.DEBUG); }
    public static void INFO(Object obj, String message) { log(obj, message, LogTag.INFO); }

    private static void log(Object obj, String message, LogTag tag) {
        switch (tag) {
            case ERROR:
                CmdUtils.setForegroundRGB(0xFF, 0, 0);
                System.out.println(StringUtils.build("[ERROR] ", obj.getClass().getSimpleName(), ": ", message));
                CmdUtils.resetColor();
            break;
            case DEBUG:
                System.out.println(StringUtils.build("[DEBUG] ", obj.getClass().getSimpleName(), ": ", message));
            break;
            case INFO:
                System.out.println(StringUtils.build("[INFO] ",  obj.getClass().getSimpleName(), ": ", message));
            break;
        }
    }
}
