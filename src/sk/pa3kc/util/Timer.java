package sk.pa3kc.util;

public class Timer {
    private long lastTime;
    private boolean resetOnCycle;

    public Timer(boolean resetOnCycle) {
        this.resetOnCycle = resetOnCycle;
        this.lastTime = System.currentTimeMillis();
    }

    public long cycle() {
        if (this.resetOnCycle) {
            long result = System.currentTimeMillis() - this.lastTime;
            this.reset();
            return result;
        } else return System.currentTimeMillis() - this.lastTime;
    }

    public void reset() {
        this.lastTime = System.currentTimeMillis();
    }

    public static long time(Runnable func) {
        long tmp = System.currentTimeMillis();

        func.run();

        return System.currentTimeMillis() - tmp;
    }
}
