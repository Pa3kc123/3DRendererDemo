package sk.pa3kc.util;

public class Timer {
    private final boolean resetOnCycle;
    private long lastTime;

    public Timer(boolean resetOnCycle) {
        this.resetOnCycle = resetOnCycle;
        this.lastTime = System.currentTimeMillis();
    }

    public long cycle() {
        if (this.resetOnCycle) {
            final long result = System.currentTimeMillis() - this.lastTime;
            this.reset();
            return result;
        } else return System.currentTimeMillis() - this.lastTime;
    }

    public void reset() {
        this.lastTime = System.currentTimeMillis();
    }

    public static long time(Runnable func) {
        final long tmp = System.currentTimeMillis();

        func.run();

        return System.currentTimeMillis() - tmp;
    }
}
