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
import sk.pa3kc.util.UIThread;

import static sk.pa3kc.util.Logger.ERROR;

public class Program {
    public static long TIMER_CYCLE_LIMIT;

    public static final String NEWLINE = DefaultSystemPropertyStrings.LINE_SEPARATOR;
    public static final String OS_NAME = DefaultSystemPropertyStrings.OS_NAME;

    private static Program instance;

    public static int CHOOSEN_GRAPHICS_DEVICE;
    public static GraphicsConfiguration GRAPHICS_DEVICE_CONFIG;
    public static Rectangle GRAPHICS_DEVICE_BOUNDS;

    public static MyFrame mainFrame = null;

    public static boolean toggled = true;

    public static UIThread uiThread = new UIThread(66, 60);

    private Program(int graphicsDeviceIndex) {
        final GraphicsDevice[] devices;
        try {
            devices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
        } catch (HeadlessException ex) {
            ERROR(this, "No supported display found");
            System.exit(0xFF);
            return;
        }
        Program.CHOOSEN_GRAPHICS_DEVICE = NumberUtils.map(graphicsDeviceIndex, 1, devices.length);
        Program.GRAPHICS_DEVICE_CONFIG = devices[Program.CHOOSEN_GRAPHICS_DEVICE - 1].getDefaultConfiguration();
        Program.GRAPHICS_DEVICE_BOUNDS = Program.GRAPHICS_DEVICE_CONFIG.getBounds();

        // Keyboard.getInst().getKeyInfo('g').addOnPressedAction(new Runnable() {
        //     @Override
        //     public void run() {
        //         Program.toggled = !Program.toggled;
        //     }
        // });

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

    public static void main(String[] args) {
        System.out.println(Program.OS_NAME);
        instance = new Program(args.length > 0 ? Integer.parseInt(args[0]) : 1);
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
