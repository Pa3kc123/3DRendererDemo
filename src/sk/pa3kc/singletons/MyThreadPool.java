package sk.pa3kc.singletons;

public class MyThreadPool {
    private static final MyThreadPool instance = new MyThreadPool();

    private Thread uiThread = null;

    private MyThreadPool() {}

    public MyThreadPool getInst() { return instance; }

    public Thread getUIThread() { return this.uiThread; }

    public void setUIThread(Thread value) { this.uiThread = value; }
}
