package sk.pa3kc.singletons;

import sk.pa3kc.util.Parameters;

public class Configuration {
    private static int MAX_FPS = 0;
    private static int MAX_UPS = 66;
    private static int MONITOR_INDEX = 1;
    private static long UI_CYCLE_WARN_TIME = 50L;
    private static long LINUX_SYNC_WARN_TIME = 50L;
    private static boolean DEBUG_ENABLED = false;

    public static void setupConfig(Parameters params) {
        for (final String key : params.getOptionNames()) {
            final String value = params.getOption(key);

            switch (key) {
                case "max_fps":
                case "fps":
                    try {
                        MAX_FPS = Integer.parseInt(value);
                    } catch (Throwable ex) {
                        ex.printStackTrace();
                    }
                break;

                case "max_ups":
                case "ups":
                    try {
                        MAX_UPS = Integer.parseInt(value);
                    } catch (Throwable ex) {
                        ex.printStackTrace();
                    }
                break;

                case "monitor_index":
                case "m":
                    try {
                        MONITOR_INDEX = Integer.parseInt(value);
                    } catch (Throwable ex) {
                        ex.printStackTrace();
                    }
                break;

                case "ui_cycle_warn_time":
                case "ui":
                    try {
                        UI_CYCLE_WARN_TIME = Long.parseLong(value);
                    } catch (Throwable ex) {
                        ex.printStackTrace();
                    }
                break;

                case "linux_sync_warn_time":
                    try {
                        LINUX_SYNC_WARN_TIME = Long.parseLong(value);
                    } catch (Throwable ex) {
                        ex.printStackTrace();
                    }
                break;

                case "enable_debug":
                case "debug":
                    try {
                        DEBUG_ENABLED = Boolean.parseBoolean(value);
                    } catch (Throwable ex) {
                        ex.printStackTrace();
                    }
                break;
            }
        }
    }

    public static int getMaxFps() {
        return MAX_FPS;
    }
    public static int getMaxUps() {
        return MAX_UPS;
    }
    public static int getMonitorIndex() {
        return MONITOR_INDEX;
    }
    public static long getUiCycleWarnTime() {
        return UI_CYCLE_WARN_TIME;
    }
    public static long getLinuxSyncWarnTime() {
        return LINUX_SYNC_WARN_TIME;
    }
    public static boolean getDebugEnabled() {
        return DEBUG_ENABLED;
    }

    public static void setMaxFps(int value) {
        MAX_FPS = value;
    }
    public static void setMaxUps(int value) {
        MAX_UPS = value;
    }
    public static void setMonitorIndex(int value) {
        MONITOR_INDEX = value;
    }
    public static void setUiCycleWarnTime(long value) {
        UI_CYCLE_WARN_TIME = value;
    }
    public static void setLinuxSyncWarnTime(long value) {
        LINUX_SYNC_WARN_TIME = value;
    }
    public static void setDebugEnabled(boolean value) {
        DEBUG_ENABLED = value;
    }
}
