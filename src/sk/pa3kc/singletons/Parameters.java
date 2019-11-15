package sk.pa3kc.singletons;

public class Parameters {
    private Parameters() {}

    private static final String[] flags = new String[6];

    public static String[] getAllFlags() { return flags; }
    public static String getFlag(int index) { return flags[index]; }
}

enum ParameterEnum {
    FPS("-fps"),
    UPS("-ups"),
    MONITOR_INDEX("-monitor_index"),
    UI_CYCLE_DELAY_WARN("-ui_cycle_delay_warn"),
    SYNC_DELAY_WARN("-sync_delay_warn");

    public final String name;
    ParameterEnum(String name) {
        this.name = name;
    }
    @Override
    public String toString() {
        return this.name;
    }
}
