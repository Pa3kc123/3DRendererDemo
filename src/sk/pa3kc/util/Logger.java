package sk.pa3kc.util;

import sk.pa3kc.mylibrary.cmd.CmdUtils;
import sk.pa3kc.mylibrary.util.StringUtils;

public class Logger {
    private Logger() {}

    private enum LogTag {
        ERROR,
        WARN,
        DEBUG,
        INFO
    }

    public static void ERROR(Object obj, String message) { log(obj, message, LogTag.ERROR); }
    public static void WARN(Object obj, String message) { log(obj, message, LogTag.WARN); }
    public static void DEBUG(Object obj, String message) { log(obj, message, LogTag.DEBUG); }
    public static void INFO(Object obj, String message) { log(obj, message, LogTag.INFO); }

    private static void log(Object obj, String message, LogTag tag) {
        switch (tag) {
            case ERROR:
                CmdUtils.setForegroundRGB(0xFF, 0, 0);
                System.out.println(StringUtils.build("[ERROR] ", obj.getClass().getSimpleName(), ": ", message));
                CmdUtils.resetColor();
            break;
            case WARN:
                CmdUtils.setForegroundRGB(0xFF, 0xFF, 0);
                System.out.println(StringUtils.build("[WARN] ", obj.getClass().getSimpleName(), ": ", message));
                CmdUtils.resetColor();
            break;
            case DEBUG:
                System.out.println(StringUtils.build("[DEBUG] ", obj.getClass().getSimpleName(), ": ", message));
            break;
            case INFO:
                System.out.println(StringUtils.build("[INFO] ",  obj.getClass().getSimpleName(), ": ", message));
            break;
        }
    }
}
