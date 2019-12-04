package sk.pa3kc.singletons;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Configuration {
    private static Configuration _inst = new Configuration();

    private final Entry<? extends Number>[] entries;

    @SuppressWarnings("checked")
    private Configuration() {
        final Collection<Entry<? extends Number>> entries = Collections.unmodifiableCollection(new ArrayList<Entry<? extends Number>>() {
            private static final long serialVersionUID = 1L;

            {
                super.add(new Entry<Integer>("max_ups", "ups", Integer.valueOf(66), Indexer.MAX_UPS));
                super.add(new Entry<Integer>("max_fps", "fps", Integer.valueOf(-1), Indexer.MAX_FPS));
                super.add(new Entry<Integer>("monitor_index", "m", Integer.valueOf(1), Indexer.MONITOR_INDEX));
                super.add(new Entry<Long>("ui_cycle_warn_time", "ui", Long.valueOf(50L), Indexer.UI_CYCLE_WARN_TIME));
                super.add(new Entry<Long>("linux_sync_warn_time", null, Long.valueOf(50l), Indexer.LINUX_SYNC_WARN_TIME));
            }
        });

        this.entries = entries.toArray(new Entry[0]);

        final Entry<Number> tmp = new Entry<Integer>("", "", new Integer(10), null);
        tmp.setValue(Integer.valueOf(10));
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
    public Entry<? extends Number> getProperty(Indexer index) {
        return this.entries[index.index];
    }

    public void setMaxFps(int value) {
        this.entries[Indexer.MAX_FPS.index].setValue(Integer.valueOf(value));
    }

    // TODO: Not working for some reason
    // public <T> void setProperty(Indexer index, T value) {
    //     final Entry<? extends Number> entry = this.entries[index.index];
    //     entry.setValue(value);
    // }

    public static enum Indexer {
        MAX_FPS(0),
        MAX_UPS(1),
        MONITOR_INDEX(2),
        UI_CYCLE_WARN_TIME(3),
        LINUX_SYNC_WARN_TIME(4);

        public final int index;

        private Indexer(int index) {
            this.index = index;
        }

        @SuppressWarnings("Unchecked")
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
        public Object getValue() {
            return this.value;
        }

        public void setValue(T value) {
            this.value = value;
        }
    }
}
