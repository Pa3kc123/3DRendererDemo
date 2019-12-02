package sk.pa3kc.util;

import sk.pa3kc.mylibrary.pojo.ObjectPointer;
import sk.pa3kc.mylibrary.util.ArrayUtils;
import sk.pa3kc.mylibrary.util.StringUtils;
import sk.pa3kc.singletons.Configuration;
import sk.pa3kc.Program;

public class UIThread extends Thread {
    private Runnable[] updateRunnables = new Runnable[0];
    private Runnable[] renderRunnables = new Runnable[0];

    private boolean shutdownRequested = false;
    private boolean running = false;
    private int updateCount = 0;
    private int frameCount = 0;

    // region Getters
    public boolean isRunning() {
        return this.running;
    }

    public boolean isShutdownRequested() {
        return this.shutdownRequested;
    }

    public int getFPS() {
        return this.frameCount;
    }

    public int getUPS() {
        return this.updateCount;
    }
    // endregion

    // region Adders
    public boolean[] addUpdateRunnables(Runnable... runnables) {
        final ObjectPointer<Runnable[]> pointer = new ObjectPointer<Runnable[]>(this.updateRunnables);
        final boolean[] results = ArrayUtils.addAll(pointer, runnables);

        this.updateRunnables = pointer.value;

        return results;
    }

    public boolean[] addRenderRunnables(Runnable... runnables) {
        final ObjectPointer<Runnable[]> pointer = new ObjectPointer<Runnable[]>(this.renderRunnables);
        final boolean[] results = ArrayUtils.addAll(pointer, runnables);

        this.renderRunnables = pointer.value;

        return results;
    }
    // endregion

    // region Removers
    public boolean[] removeUpdateRunnables(Runnable... runnables) {
        final ObjectPointer<Runnable[]> pointer = new ObjectPointer<Runnable[]>(this.updateRunnables);
        final boolean[] results = new boolean[runnables.length];

        ArrayUtils.removeDuplicates(new ObjectPointer<Runnable[]>(runnables));

        for (int i = 0; i < runnables.length; i++)
            results[i] = ArrayUtils.removeAll(pointer, runnables[i]);

        return results;
    }

    public boolean[] removeRenderRunnables(Runnable... runnables) {
        final ObjectPointer<Runnable[]> pointer = new ObjectPointer<Runnable[]>(this.renderRunnables);
        final boolean[] results = new boolean[runnables.length];

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
        Logger.DEBUG("uiThread started");

        final boolean updateLimited = Program.config.getMaxUps() != -1;
        final boolean renderLimited = Program.config.getMaxFps() != -1;
        final Timer timer = new Timer(false);

        int frames = 0;
        int updates = 0;

        long lastIteration = System.currentTimeMillis();
        long lastRender = lastIteration;
        long lastUpdate = lastIteration;
        long lastLog = lastIteration;

        long msecPerUpdate = 1000 / Program.config.getMaxUps();
        long msecPerRender = 1000 / Program.config.getMaxFps();
        long msecPerLog = 1000 / 1;

        long delta;
        // main loop
        while (!this.shutdownRequested) {
            long now = System.currentTimeMillis();

            // should we draw next frame?
            if (renderLimited) {
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
            if (updateLimited) {
                delta = now - lastUpdate;
                if (delta >= msecPerUpdate) {
                    lastUpdate = now - (delta - msecPerUpdate);

                    for (Runnable task : this.updateRunnables)
                        task.run();
                    updates++;
                }
            } else {
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
                Logger.WARN(StringUtils.build("UI cycle took ", cycle, "ms"));

            timer.reset();
        }

        this.running = false;
        Logger.DEBUG("uiThread stopped");
    }
    // endregion
}
