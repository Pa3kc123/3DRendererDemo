package sk.pa3kc.singletons;

public interface Locks {
    public static final Object MY_FRAME_LOCK = new Object();
    public static final Object UI_THREAD_LOCK = new Object();
    public static final Object KEYBOARD_LOCK = new Object();
}
