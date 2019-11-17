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
import sk.pa3kc.singletons.Parameters;
import sk.pa3kc.ui.MyFrame;
import sk.pa3kc.util.Logger;
import sk.pa3kc.util.UIThread;

public class Program {
    private Program() {}

    public static long TIMER_CYCLE_LIMIT;

    public static final String NEWLINE = DefaultSystemPropertyStrings.LINE_SEPARATOR;
    public static final String OS_NAME = DefaultSystemPropertyStrings.OS_NAME;

    public static GraphicsConfiguration GRAPHICS_DEVICE_CONFIG;
    public static Rectangle GRAPHICS_DEVICE_BOUNDS;

    public static MyFrame mainFrame;

    public static UIThread UI_THREAD = null;

    public static void main(String[] args) {
        Parameters.init(args);

        new UIThread(Parameters.getInst().MAX_UPS.getValue(), Parameters.getInst().MAX_FPS.getValue());

        for (int i = 0; i < args.length; i++) {
            TIMER_CYCLE_LIMIT = args.length > 1 ? Long.parseLong(args[1]) : 50L;
        }

        final GraphicsDevice[] devices;

        try {
            devices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
        } catch (HeadlessException ex) {
            Logger.ERROR("No supported display found");
            System.exit(0xFF);
            return;
        }

        final int graphicsDeviceIndex = args.length > 0 ? Integer.parseInt(args[0]) : 1;
        GRAPHICS_DEVICE_CONFIG = devices[NumberUtils.map(graphicsDeviceIndex, 1, devices.length) - 1].getDefaultConfiguration();
        GRAPHICS_DEVICE_BOUNDS = GRAPHICS_DEVICE_CONFIG.getBounds();

        UI_THREAD.addUpdateRunnables(new Runnable() {
            @Override
            public void run() {
                mainFrame.update(UpdateMode.ALL);
            }
        });
        UI_THREAD.addRenderRunnables(new Runnable() {
            @Override
            public void run() {
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
        });

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                mainFrame = new MyFrame("Test2");
            }
        });

        Locks.MY_FRAME_LOCK.lock();

        Program.UI_THREAD.start();
    }
}
