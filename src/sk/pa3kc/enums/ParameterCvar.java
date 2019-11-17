package sk.pa3kc.enums;

import static sk.pa3kc.enums.ParameterIndex.*;

public enum ParameterCvar {
    CVAR_MAX_FPS("-fps"),
    CVAR_MAX_UPS("-ups"),
    CVAR_MONITOR_INDEX("-monitor_index"),
    CVAR_UI_CYCLE("-ui_cycle_delay_warn"),
    CVAR_LINUX_SYNC("-sync_delay_warn");

    public final String cvar;
    ParameterCvar(String cvar) {
        this.cvar = cvar;
    }
    public static ParameterIndex findByString(String value) {
        if (value.equals(CVAR_MAX_FPS.cvar)) return INDEX_MAX_FPS;
        if (value.equals(CVAR_MAX_UPS.cvar)) return INDEX_MAX_UPS;
        if (value.equals(CVAR_MONITOR_INDEX.cvar)) return INDEX_MONITOR_INDEX;
        if (value.equals(CVAR_UI_CYCLE.cvar)) return INDEX_UI_CYCLE;
        if (value.equals(CVAR_LINUX_SYNC.cvar)) return INDEX_LINUX_SYNC;
        else return null;
    }
    @Override
    public String toString() {
        return this.cvar;
    }
}
