package sk.pa3kc.singletons;

public class MyThreadPool {
    private MyThreadPool() {}

    public static boolean allRunning = true;

    public static boolean uiThreadRunning = false;
    public static Thread uiThread = null;

    public static void requestShutdownOnAllThreads() { allRunning = false; }
}
