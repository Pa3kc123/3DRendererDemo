package sk.pa3kc;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import sk.pa3kc.mylibrary.DefaultSystemPropertyStrings;
import sk.pa3kc.ui.MyFrame;

public class Program
{
    public static final String NEWLINE = DefaultSystemPropertyStrings.LINE_SEPARATOR;
    public static final String OS_NAME = DefaultSystemPropertyStrings.OS_NAME;

    public static Object globalLock = new Object();

    private static Program instance;

    public final int CHOOSEN_GRAPHICS_DEVICE;
    public final GraphicsConfiguration GRAPHICS_DEVICE_CONFIG;
    public final Rectangle GRAPHICS_DEVICE_BOUNDS;

    private static MyFrame mainFrame = null;

    public static boolean toggled = false;

    public boolean uiThreadRunning = false;
    public final Thread uiThread = new Thread(new Runnable()
    {
        @Override
        public void run()
        {
            uiThreadRunning = true;
            try
            {
                while (uiThreadRunning == true)
                {
                    Runnable runnable = new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            mainFrame.myPanel.repaint();
                        }
                    };
                    SwingUtilities.invokeAndWait(runnable);
                }
            }
            catch (InvocationTargetException ex) { ex.printStackTrace(); }
            catch (InterruptedException ex) { ex.printStackTrace(); }
        }
    });

    private Program(int choosenGraphicsDevice)
    {
        GraphicsDevice[] devices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
        this.CHOOSEN_GRAPHICS_DEVICE = choosenGraphicsDevice < 0 ? 0 : choosenGraphicsDevice > devices.length - 1 ? devices.length - 1 : choosenGraphicsDevice;
        this.GRAPHICS_DEVICE_CONFIG = devices[CHOOSEN_GRAPHICS_DEVICE].getDefaultConfiguration();
        this.GRAPHICS_DEVICE_BOUNDS = this.GRAPHICS_DEVICE_CONFIG.getBounds();
    }

    public static Program getInst() { return instance; }

    public static <T> T safeCast(Object value, Class<T> type)
    {
        try { return type.cast(value); }
        catch (ClassCastException ignored) { return null; }
    }

    public static void main(String... args)
    {
        System.setProperty("sun.java2d.opengl", "false");
        instance = new Program(args.length != 0 ? Integer.parseInt(args[0]) - 1 : 0);

        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                mainFrame = new MyFrame("Test2");
            }
        });
        synchronized (globalLock) {
            try
            {
                globalLock.wait();
            }
            catch (InterruptedException ex)
            {
                ex.printStackTrace();
            }

            instance.uiThread.start();
        }
    }
    public static void ERROR(Class<?> tag, String message) { System.out.println("ERROR: ".concat(tag.getSimpleName()).concat(": ").concat(message)); }
    public static void DEBUG(Class<?> tag, String message) { System.out.println("DEBUG: ".concat(tag.getSimpleName()).concat(": ").concat(message)); }
    public static void INFO(Class<?> tag, String message) { System.out.println("INFO: ".concat(tag.getSimpleName()).concat(": ").concat(message)); }
}
