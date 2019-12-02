package sk.pa3kc.singletons;

public class Configuration {
    private static Configuration _Inst = new Configuration();
    private Configuration() {};
    public static Configuration getInst() {
        return _Inst;
    }

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
        public static Indexer fromString(String value) {
            if (value == null) return null;
            for (Entry entry : _Entries) {
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
    private static Entry[] _Entries = new Entry[] {
        new Configuration.Entry("max_fps", "fps", -1, Integer.class, int.class, Indexer.MAX_FPS),
        new Configuration.Entry("max_ups", "ups", 66, Integer.class, int.class, Indexer.MAX_UPS),
        new Configuration.Entry("monitor_index", "m", 1, Integer.class, int.class, Indexer.MONITOR_INDEX),
        new Configuration.Entry("ui_cycle_warn_time", "ui", 50L, Long.class, long.class, Indexer.UI_CYCLE_WARN_TIME),
        new Configuration.Entry("linux_sync_warn_time", null, 50l, Long.class, long.class, Indexer.LINUX_SYNC_WARN_TIME),
    };

    public int getMaxFps() {
        return (int)_Entries[Indexer.MAX_FPS.index].getValue(true);
    }
    public int getMaxUps() {
        return (int)_Entries[Indexer.MAX_UPS.index].getValue(true);
    }
    public int getMonitorIndex() {
        return (int)_Entries[Indexer.MONITOR_INDEX.index].getValue(true);
    }
    public long getUiCycleWarnTime() {
        return (long)_Entries[Indexer.UI_CYCLE_WARN_TIME.index].getValue(true);
    }
    public long getLinuxSyncWarnTime() {
        return (long)_Entries[Indexer.LINUX_SYNC_WARN_TIME.index].getValue(true);
    }
    public Entry getProperty(Indexer index) {
        return _Entries[index.index];
    }

    public void setProperty(Indexer index, Object value) {
        _Entries[index.index].setValue(value);
    }

    public static class Entry {
        private final Indexer index;
        private final String fullName;
        private final String shortName;
        private final Class<?> objectType;
        private final Class<?> primitiveType;

        private Object value;

        public Entry(String fullName, String shortName, Object value, Class<?> objectType, Class<?> primitiveType, Indexer index) {
            this.fullName = fullName;
            this.shortName = shortName;
            this.value = value;
            this.objectType = objectType;
            this.primitiveType = primitiveType;
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
        public Object getValue(boolean toPrimitiveType) {
            try {
                if (toPrimitiveType) {
                    return this.primitiveType.cast(this.value);
                } else {
                    return this.objectType.cast(this.value);
                }
            } catch (Throwable ex) {
                ex.printStackTrace();
                return null;
            }
        }

        public boolean setValue(Object value) {
            final Class<?> valueClass = value.getClass();

            if (valueClass.isPrimitive()) {
                if (valueClass == this.primitiveType) {
                    return this._setValue(valueClass, value);
                }
            } else {
                if (valueClass == this.objectType) {
                    return this._setValue(valueClass, value);
                }
            }

            return false;
        }

        private boolean _setValue(Class<?> valueClass, Object value) {
            try {
                this.value = valueClass.cast(value);
                return true;
            } catch (Throwable ex) {
                ex.printStackTrace();
                return false;
            }
        }
    }
}
