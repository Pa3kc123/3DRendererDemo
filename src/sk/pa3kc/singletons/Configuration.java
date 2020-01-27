package sk.pa3kc.singletons;

import java.util.Arrays;

public class Configuration {
    private static Configuration _inst = new Configuration();

    private final Entry[] entries;

    @SuppressWarnings("checked")
    private Configuration() {
        this.entries = Arrays.asList(
            new Entry<Integer>("max_fps", "fps", Integer.valueOf(0), Indexer.MAX_FPS),
            new Entry<Integer>("max_ups", "ups", Integer.valueOf(66), Indexer.MAX_UPS),
            new Entry<Integer>("monitor_index", "m", Integer.valueOf(1), Indexer.MONITOR_INDEX),
            new Entry<Long>("ui_cycle_warn_time", "ui", Long.valueOf(50L), Indexer.UI_CYCLE_WARN_TIME),
            new Entry<Long>("linux_sync_warn_time", null, Long.valueOf(50L), Indexer.LINUX_SYNC_WARN_TIME),
            new Entry<Boolean>("enable_debug", "debug", Boolean.valueOf(false), Indexer.DEBUG_ENABLED)
        ).toArray(new Entry[0]);
    };
    public static Configuration getInst() {
        return _inst;
    }

    public int getMaxFps() {
        return ((Integer)this.entries[Indexer.MAX_FPS.index].getValue()).intValue();
    }
    public int getMaxUps() {
        return ((Integer)this.entries[Indexer.MAX_UPS.index].getValue()).intValue();
    }
    public int getMonitorIndex() {
        return ((Integer)this.entries[Indexer.MONITOR_INDEX.index].getValue()).intValue();
    }
    public long getUiCycleWarnTime() {
        return ((Long)this.entries[Indexer.UI_CYCLE_WARN_TIME.index].getValue()).longValue();
    }
    public long getLinuxSyncWarnTime() {
        return ((Long)this.entries[Indexer.LINUX_SYNC_WARN_TIME.index].getValue()).longValue();
    }
    public boolean getDebugEnabled() {
        return ((Boolean)this.entries[Indexer.DEBUG_ENABLED.index].getValue()).booleanValue();
    }
    public Entry<?> getProperty(Indexer index) {
        return this.entries[index.index];
    }

    @SuppressWarnings("checked")
    public void setMaxFps(int value) {
        this.entries[Indexer.MAX_FPS.index].setValue(Integer.valueOf(value));
    }
    public void setMaxUps(int value) {
        this.entries[Indexer.MAX_UPS.index].setValue(Integer.valueOf(value));
    }
    public void setMonitorIndex(int value) {
        this.entries[Indexer.MONITOR_INDEX.index].setValue(Integer.valueOf(value));
    }
    public void setUiCycleWarnTime(long value) {
        this.entries[Indexer.UI_CYCLE_WARN_TIME.index].setValue(Long.valueOf(value));
    }
    public void setLinuxSyncWarnTime(long value) {
        this.entries[Indexer.LINUX_SYNC_WARN_TIME.index].setValue(Long.valueOf(value));
    }
    public void setDebugEnabled(boolean value) {
        this.entries[Indexer.DEBUG_ENABLED.index].setValue(Boolean.valueOf(value));
    }

    public static enum Indexer {
        MAX_FPS(0),
        MAX_UPS(1),
        MONITOR_INDEX(2),
        UI_CYCLE_WARN_TIME(3),
        LINUX_SYNC_WARN_TIME(4),
        DEBUG_ENABLED(5);

        public final int index;

        private Indexer(int index) {
            this.index = index;
        }

        public static Indexer fromString(String value) {
            if (value == null) {
                return null;
            }

            for (Entry<?> entry : Configuration.getInst().entries) {
                if (value.equals(entry.getFullName()) || value.equals(entry.getShortName())) {
                    return entry.index;
                }
            }

            return null;
        }

        @Override
        public String toString() {
            return String.valueOf(this.index);
        }
    }

    public static class Entry<T> {
        private final Indexer index;
        private final String fullName;
        private final String shortName;

        private T value;

        public Entry(String fullName, String shortName, T value, Indexer index) {
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
        public Indexer getIndex() {
            return this.index;
        }
        public T getValue() {
            return this.value;
        }

        public void setValue(T value) {
            this.value = value;
        }
    }
}
