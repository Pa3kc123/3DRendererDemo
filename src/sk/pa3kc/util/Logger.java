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

    public static void ERROR(String message) { log(message, LogTag.ERROR); }
    public static void WARN(String message) { log(message, LogTag.WARN); }
    public static void DEBUG(String message) { log(message, LogTag.DEBUG); }
    public static void INFO(String message) { log(message, LogTag.INFO); }

    private static void log(String message, LogTag tag) {
        String name = Thread.currentThread().getStackTrace()[3].getClassName();
        name = name.substring(name.lastIndexOf('.') + 1, name.length());

        switch (tag) {
            case ERROR:
                CmdUtils.setForegroundRGB(0xFF, 0, 0);
                System.out.println(StringUtils.build("[ERROR] ", name, ": ", message));
                CmdUtils.resetColor();
            break;
            case WARN:
                CmdUtils.setForegroundRGB(0xFF, 0xFF, 0);
                System.out.println(StringUtils.build("[WARN ] ", name, ": ", message));
                CmdUtils.resetColor();
            break;
            case DEBUG:
                System.out.println(StringUtils.build("[DEBUG] ", name, ": ", message));
            break;
            case INFO:
                System.out.println(StringUtils.build("[INFO ] ",  name, ": ", message));
            break;
        }
    }
}
