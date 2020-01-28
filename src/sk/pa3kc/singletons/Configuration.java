package sk.pa3kc.singletons;

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
    }
}
