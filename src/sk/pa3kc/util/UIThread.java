package sk.pa3kc.util;

import java.util.ArrayList;

import sk.pa3kc.mylibrary.util.StringUtils;
import sk.pa3kc.singletons.Configuration;

public class UIThread extends Thread {
    private ArrayList<Runnable> updatables = new ArrayList<Runnable>();
    private ArrayList<Runnable> renderables = new ArrayList<Runnable>();

    private boolean shutdownRequested = false;
    private boolean running = false;
    private int updateCount = 0;
    private int frameCount = 0;

    // region Getters
    public ArrayList<Runnable> getUpdatables() {
        return this.updatables;
    }
    public ArrayList<Runnable> getRenderables() {
        return this.renderables;
    }
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

        final boolean updateLimited = Configuration.getInst().getMaxUps() != -1;
        final boolean renderLimited = Configuration.getInst().getMaxFps() != -1;
        final Timer timer = new Timer(false);

        int frames = 0;
        int updates = 0;

        long lastIteration = System.currentTimeMillis();
        long lastRender = lastIteration;
        long lastUpdate = lastIteration;
        long lastLog = lastIteration;

        long msecPerUpdate = 1000 / Configuration.getInst().getMaxUps();
        long msecPerRender = 1000 / Configuration.getInst().getMaxFps();
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

                    for (Runnable task : this.renderables)
                        task.run();
                    frames++;
                }
            } else {
                for (Runnable task : this.renderables)
                    task.run();
                frames++;
            }

            // is it time for updating next game iteration?
            if (updateLimited) {
                delta = now - lastUpdate;
                if (delta >= msecPerUpdate) {
                    lastUpdate = now - (delta - msecPerUpdate);

                    for (Runnable task : this.updatables)
                        task.run();
                    updates++;
                }
            } else {
                for (Runnable task : this.updatables)
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
            if (cycle > Configuration.getInst().getUiCycleWarnTime())
                Logger.WARN(StringUtils.build("UI cycle took ", cycle, "ms"));

            timer.reset();
        }

        this.running = false;
        Logger.DEBUG("uiThread stopped");
    }
    // endregion
}
