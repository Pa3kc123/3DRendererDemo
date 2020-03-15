package sk.pa3kc.singletons;

<<<<<<< HEAD
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
=======
public class Configuration {
    private static Configuration _inst = new Configuration();

    private final Entry<Integer> MAX_FPS = new Entry<>("max_fps", "fps", Integer.valueOf(0), Indexer.MAX_FPS);
    private final Entry<Integer> MAX_UPS = new Entry<>("max_ups", "ups", Integer.valueOf(66), Indexer.MAX_UPS);
    private final Entry<Integer> MONITOR_INDEX = new Entry<>("monitor_index", "m", Integer.valueOf(1), Indexer.MONITOR_INDEX);
    private final Entry<Long> UI_CYCLE_WARN_TIME = new Entry<>("ui_cycle_warn_time", "ui", Long.valueOf(50L), Indexer.UI_CYCLE_WARN_TIME);
    private final Entry<Long> LINUX_SYNC_WARN_TIME = new Entry<>("linux_sync_warn_time", null, Long.valueOf(50L), Indexer.LINUX_SYNC_WARN_TIME);
    private final Entry<Boolean> ENABLE_DEBUG = new Entry<>("enable_debug", "debug", Boolean.valueOf(false), Indexer.DEBUG_ENABLED);

    public static Configuration getInst() {
        return _inst;
    }

    public int getMaxFps() {
        return MAX_FPS.getValue().intValue();
    }
    public int getMaxUps() {
        return MAX_UPS.getValue().intValue();
    }
    public int getMonitorIndex() {
        return MONITOR_INDEX.getValue().intValue();
    }
    public long getUiCycleWarnTime() {
        return UI_CYCLE_WARN_TIME.getValue().longValue();
    }
    public long getLinuxSyncWarnTime() {
        return LINUX_SYNC_WARN_TIME.getValue().longValue();
    }
    public boolean getDebugEnabled() {
        return ENABLE_DEBUG.getValue().booleanValue();
    }

    public void setMaxFps(int value) {
        MAX_FPS.setValue(Integer.valueOf(value));
    }
    public void setMaxUps(int value) {
        MAX_UPS.setValue(Integer.valueOf(value));
    }
    public void setMonitorIndex(int value) {
        MONITOR_INDEX.setValue(Integer.valueOf(value));
    }
    public void setUiCycleWarnTime(long value) {
        UI_CYCLE_WARN_TIME.setValue(Long.valueOf(value));
    }
    public void setLinuxSyncWarnTime(long value) {
        LINUX_SYNC_WARN_TIME.setValue(Long.valueOf(value));
    }
    public void setDebugEnabled(boolean value) {
        ENABLE_DEBUG.setValue(Boolean.valueOf(value));
    }

    public static enum Indexer {
        MAX_FPS,
        MAX_UPS,
        MONITOR_INDEX,
        UI_CYCLE_WARN_TIME,
        LINUX_SYNC_WARN_TIME,
        DEBUG_ENABLED;
    }
}

class Entry<T> {
    private final Configuration.Indexer index;
    private final String fullName;
    private final String shortName;

    private T value;

    public Entry(String fullName, String shortName, T value, Configuration.Indexer index) {
        this.fullName = fullName;
        this.shortName = shortName;
        this.value = value;
        this.index = index;
    }

    public String getFullName() {
        return this.fullName;
    }
    public String getShortName() {
        return this.shortName;
    }
    public Configuration.Indexer getIndex() {
        return this.index;
    }
    public T getValue() {
        return this.value;
    }

    public void setValue(T value) {
        this.value = value;
>>>>>>> e9eb711e13b99bcce53960f2d7dd4e8ae0fdb57a
    }
}
