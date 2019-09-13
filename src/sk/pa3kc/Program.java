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
import sk.pa3kc.pojo.Parameter;
import sk.pa3kc.singletons.Locks;
import sk.pa3kc.ui.MyFrame;
import sk.pa3kc.util.Logger;
import sk.pa3kc.util.UIThread;

public class Program {
    private Program() {}

    private interface Parameters {
        public static final Parameter<Integer> MAX_FPS = new Parameter<Integer>("-fps", new Integer(-1));
        public static final Parameter<Integer> MAX_UPS = new Parameter<Integer>("-ups", new Integer(66));
        public static final Parameter<Integer> MONITOR_INDEX = new Parameter<Integer>("-monitor_index", new Integer(1));
        public static final Parameter<Long> UI_CYCLE = new Parameter<Long>("-ui_cycle_delay_warn", new Long(50L));
        public static final Parameter<Long> LINUX_SYNC = new Parameter<Long>("-sync_delay_warn", new Long(50L));
    }

    public static long TIMER_CYCLE_LIMIT;

    public static final String NEWLINE = DefaultSystemPropertyStrings.LINE_SEPARATOR;
    public static final String OS_NAME = DefaultSystemPropertyStrings.OS_NAME;

    public static GraphicsConfiguration GRAPHICS_DEVICE_CONFIG;
    public static Rectangle GRAPHICS_DEVICE_BOUNDS;

    public static MyFrame mainFrame;

    public static final UIThread UI_THREAD = new UIThread(66, -1);

    public static void main(String[] args) {

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

    private static void parseArgs(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i] == Parameters.MAX_FPS.getFlagName()) {

            }
        }
    }
}
