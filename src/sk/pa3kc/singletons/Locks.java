package sk.pa3kc.singletons;

import sk.pa3kc.util.Lock;

public class Locks {
    public static final Lock MY_FRAME_LOCK = new Lock("MY_FRAME_LOCK");;
    public static final Lock UI_THREAD_LOCK = new Lock("UI_THREAD_LOCK");;
    public static final Lock KEYBOARD_LOCK = new Lock("KEYBOARD_LOCK");;

    private Locks() {}

    public static void unlockAllLocks() {
        Locks.MY_FRAME_LOCK.unlockAll();
        Locks.UI_THREAD_LOCK.unlockAll();
        Locks.KEYBOARD_LOCK.unlockAll();
    }
}
