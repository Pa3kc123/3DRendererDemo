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
import sk.pa3kc.singletons.Configuration;
import sk.pa3kc.singletons.Locks;
import sk.pa3kc.ui.MyFrame;
import sk.pa3kc.util.Logger;
import sk.pa3kc.util.Parameters;
import sk.pa3kc.util.UIThread;

public class Program {
    private Program() {}

    public static final String NEWLINE = DefaultSystemPropertyStrings.LINE_SEPARATOR;
    public static final String OS_NAME = DefaultSystemPropertyStrings.OS_NAME;

    public static GraphicsConfiguration GRAPHICS_DEVICE_CONFIG;
    public static Rectangle GRAPHICS_DEVICE_BOUNDS;

    public static MyFrame mainFrame;

    public static UIThread UI_THREAD = new UIThread();

    public static void main(String[] args) {
        final Parameters params = new Parameters(args);

        for (String optionName : params.getOptionNames()) {
            final String optionValue = params.getOption(optionName);

            if (optionValue != null) {
                final Configuration.Indexer index = Configuration.Indexer.fromString(optionName);

                switch (index) {
                    case MAX_FPS:
                        int maxFps = tryParseInt(optionValue);
                        if (maxFps != -1) {
                            Configuration.getInst().setMaxFps(maxFps);
                        }
                    break;
                    case MAX_UPS:
                        int maxUps = tryParseInt(optionValue);
                        if (maxUps != -1) {
                            Configuration.getInst().setMaxUps(maxUps);
                        }
                    break;
                    case MONITOR_INDEX:
                        int monitorIndex = tryParseInt(optionValue);
                        if (monitorIndex != -1) {
                            Configuration.getInst().setMonitorIndex(monitorIndex);
                        }
                    break;
                    case LINUX_SYNC_WARN_TIME:
                        long linuxSyncWarnTime = tryParseLong(optionValue);
                        if (linuxSyncWarnTime != -1) {
                            Configuration.getInst().setLinuxSyncWarnTime(linuxSyncWarnTime);
                        }
                    break;
                    case UI_CYCLE_WARN_TIME:
                        long uiCycleWarnTime = tryParseLong(optionValue);
                        if (uiCycleWarnTime != -1) {
                            Configuration.getInst().setUiCycleWarnTime(uiCycleWarnTime);
                        }
                    break;
                }
            }
        }

        UI_THREAD.start();

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
