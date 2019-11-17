package sk.pa3kc.singletons;

import sk.pa3kc.enums.ParameterCvar;
import sk.pa3kc.enums.ParameterIndex;
import sk.pa3kc.pojo.Pair;
import sk.pa3kc.util.SizedMap;

public class Parameters {
    private static Parameters INST = null;

    public final Pair<ParameterIndex, Integer> MAX_FPS = new Pair<ParameterIndex, Integer>(ParameterIndex.INDEX_MAX_FPS, -1);
    public final Pair<ParameterIndex, Integer> MAX_UPS = new Pair<ParameterIndex, Integer>(ParameterIndex.INDEX_MAX_UPS, 66);
    public final Pair<ParameterIndex, Integer> MONITOR_INDEX = new Pair<ParameterIndex, Integer>(ParameterIndex.INDEX_MONITOR_INDEX, 1);
    public final Pair<ParameterIndex, Long> UI_CYCLE = new Pair<ParameterIndex, Long>(ParameterIndex.INDEX_UI_CYCLE, 50L);
    public final Pair<ParameterIndex, Long> LINUX_SYNC = new Pair<ParameterIndex, Long>(ParameterIndex.INDEX_LINUX_SYNC, 50L);

    private Parameters(String[] args) {
        int optionCount = 0;
        for (String arg : args) {
            if (arg.startsWith("-")) {
                optionCount++;
            }
        }

        final SizedMap<Integer, Pair> map = new SizedMap<>(optionCount);



        for (int i = 0; i < args.length; i++) {
            final ParameterIndex index = ParameterCvar.findByString(args[i]);

            if (index == null) continue;

            switch (index) {
                case INDEX_MAX_FPS: break;
                case INDEX_MAX_UPS: break;
                case INDEX_MONITOR_INDEX: break;
                case INDEX_UI_CYCLE: break;
                case INDEX_LINUX_SYNC: break;
            }
        }
    }

    public static boolean init(String[] args) {
        final boolean result = INST == null;

        if (result) {
            INST = new Parameters(args);
        }

        return result;
    }
    public static Parameters getInst() {
        return INST;
    }
}
