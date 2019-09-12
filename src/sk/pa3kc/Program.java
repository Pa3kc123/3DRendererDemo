package sk.pa3kc;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import sk.pa3kc.enums.UpdateMode;
import sk.pa3kc.mylibrary.DefaultSystemPropertyStrings;
import sk.pa3kc.mylibrary.util.NumberUtils;
import sk.pa3kc.singletons.Locks;
import sk.pa3kc.ui.MyFrame;
import sk.pa3kc.util.Logger;
import sk.pa3kc.util.UIThread;

public class Program {
    private Program() {}

    public static long TIMER_CYCLE_LIMIT;

    public static final String NEWLINE = DefaultSystemPropertyStrings.LINE_SEPARATOR;
    public static final String OS_NAME = DefaultSystemPropertyStrings.OS_NAME;

    public static int CHOOSEN_GRAPHICS_DEVICE;
    public static GraphicsConfiguration GRAPHICS_DEVICE_CONFIG;
    public static Rectangle GRAPHICS_DEVICE_BOUNDS;

    public static MyFrame mainFrame = null;

    public static boolean toggled = true;

    public static UIThread uiThread = new UIThread(66, -1);

    public static void main(String[] args) {
        final GraphicsDevice[] devices;
        try {
            devices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
        } catch (HeadlessException ex) {
            Logger.ERROR("No supported display found");
            System.exit(0xFF);
            return;
        }

        final int graphicsDeviceIndex = args.length > 0 ? Integer.parseInt(args[0]) : 1;
        CHOOSEN_GRAPHICS_DEVICE = NumberUtils.map(graphicsDeviceIndex, 1, devices.length);
        GRAPHICS_DEVICE_CONFIG = devices[CHOOSEN_GRAPHICS_DEVICE - 1].getDefaultConfiguration();
        GRAPHICS_DEVICE_BOUNDS = GRAPHICS_DEVICE_CONFIG.getBounds();

        uiThread.addUpdateRunnables(new Runnable() {
            @Override
            public void run() {
                mainFrame.update(UpdateMode.ALL);
            }
        });
        uiThread.addRenderRunnables(new Runnable() {
            @Override
            public void run() {
                if (toggled) {
                    try {
                        SwingUtilities.invokeAndWait(new Runnable() {
                            @Override
                            public void run() {
                                mainFrame.myPanel.repaint();
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
        TIMER_CYCLE_LIMIT = args.length > 1 ? Long.parseLong(args[1]) : 50L;

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                mainFrame = new MyFrame("Test2");
            }
        });

        Locks.MY_FRAME_LOCK.lock();

        Program.uiThread.start();
    }
}
