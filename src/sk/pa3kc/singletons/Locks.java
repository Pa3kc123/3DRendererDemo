package sk.pa3kc.singletons;

public class Locks {
    public static final Object MY_FRAME_LOCK = new Object();
    public static final Object UI_THREAD_LOCK = new Object();
    public static final Object KEYBOARD_LOCK = new Object();

    private Locks() {}

    public static void notifyAllLocks() {
        synchronized (MY_FRAME_LOCK) { MY_FRAME_LOCK.notifyAll(); }
        synchronized (UI_THREAD_LOCK) { UI_THREAD_LOCK.notifyAll(); }
        synchronized (KEYBOARD_LOCK) { KEYBOARD_LOCK.notifyAll(); }
    }
}
