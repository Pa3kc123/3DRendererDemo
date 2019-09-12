package sk.pa3kc.util;

import sk.pa3kc.mylibrary.pojo.ObjectPointer;
import sk.pa3kc.mylibrary.util.ArrayUtils;
import sk.pa3kc.mylibrary.util.StringUtils;

import sk.pa3kc.Program;

public class UIThread extends Thread {
    private final long FRAME_LIMIT;
    private final long UPDATE_LIMIT;

    private Runnable[] updateRunnables = new Runnable[0];
    private Runnable[] renderRunnables = new Runnable[0];

    private boolean shutdownRequested = false;
    private boolean running = false;
    private int updateCount = 0;
    private int frameCount = 0;

    public UIThread(long updateLimit, long frameLimit) {
        this.UPDATE_LIMIT = updateLimit;
        this.FRAME_LIMIT = frameLimit;
    }

    // region Getters
    public boolean isRunning() {
        return this.running;
    }

    public boolean isShutdownRequested() {
        return this.shutdownRequested;
    }

    public long getFrameLimit() {
        return this.FRAME_LIMIT;
    }

    public long getUpdateLimit() {
        return this.UPDATE_LIMIT;
    }

    public int getFrameCount() {
        return this.frameCount;
    }

    public int getUpdateCount() {
        return this.updateCount;
    }
    // endregion

    // region Adders
    public boolean[] addUpdateRunnables(Runnable... runnables) {
        ObjectPointer<Runnable[]> pointer = new ObjectPointer<Runnable[]>(this.updateRunnables);
        boolean[] results = ArrayUtils.addAll(pointer, runnables);

        this.updateRunnables = pointer.value;

        return results;
    }

    public boolean[] addRenderRunnables(Runnable... runnables) {
        ObjectPointer<Runnable[]> pointer = new ObjectPointer<Runnable[]>(this.renderRunnables);
        boolean[] results = ArrayUtils.addAll(pointer, runnables);

        this.renderRunnables = pointer.value;

        return results;
    }
    // endregion

    // region Removers
    public boolean[] removeUpdateRunnables(Runnable... runnables) {
        ObjectPointer<Runnable[]> pointer = new ObjectPointer<Runnable[]>(this.updateRunnables);
        boolean[] results = new boolean[runnables.length];

        ArrayUtils.removeDuplicates(new ObjectPointer<Runnable[]>(runnables));

        for (int i = 0; i < runnables.length; i++)
            results[i] = ArrayUtils.removeAll(pointer, runnables[i]);

        return results;
    }

    public boolean[] removeRenderRunnables(Runnable... runnables) {
        ObjectPointer<Runnable[]> pointer = new ObjectPointer<Runnable[]>(this.renderRunnables);
        boolean[] results = new boolean[runnables.length];

        ArrayUtils.removeDuplicates(new ObjectPointer<Runnable[]>(runnables));

        for (int i = 0; i < runnables.length; i++)
            results[i] = ArrayUtils.removeAll(pointer, runnables[i]);

        return results;
    }
    // endregion

    // region Public methods
    public void requestShutdown() {
        this.shutdownRequested = true;
    }
    // endregion

    // region Overrides
    @Override
    public void run() {
        this.running = true;
        Logger.DEBUG(this, "uiThread started");

        int frames = 0;
        int updates = 0;

        long lastIteration = System.currentTimeMillis();
        long lastRender = lastIteration;
        long lastUpdate = lastIteration;
        long lastLog = lastIteration;

        final boolean updateLimited = this.UPDATE_LIMIT != -1;
        final boolean renderLimited = this.FRAME_LIMIT != -1;

        long msecPerUpdate = 1000 / this.UPDATE_LIMIT;
        long msecPerRender = 1000 / this.FRAME_LIMIT;
        long msecPerLog = 1000 / 1;

        Timer timer = new Timer(false);

        long delta;
        // main game loop
        while (!this.shutdownRequested) {
            long now = System.currentTimeMillis();

            // should we draw next frame?
            if (updateLimited) {
                delta = now - lastRender;
                if (delta >= msecPerRender) {
                    lastRender = now - (delta - msecPerRender);

                    for (Runnable task : this.renderRunnables)
                        task.run();
                    frames++;
                }
            } else {
                for (Runnable task : this.renderRunnables)
                    task.run();
                frames++;
            }

            // is it time for updating next game iteration?
            delta = now - lastUpdate;
            if (delta >= msecPerUpdate) {
                lastUpdate = now - (delta - msecPerUpdate);

                for (Runnable task : this.updateRunnables)
                    task.run();

                updates++;
            }

            delta = now - lastLog;
            if (delta >= msecPerLog) {
                lastLog = now - (delta - msecPerLog);

                this.frameCount = frames;
                this.updateCount = updates;

                frames = 0;
                updates = 0;
            }

            long cycle = timer.cycle();
            if (cycle > Program.TIMER_CYCLE_LIMIT)
                Logger.WARN(this, StringUtils.build("UI cycle took ", cycle, "ms"));

            timer.reset();
        }

        this.running = false;
        System.out.println("uiThread stopped");
    }
    // endregion
}
