package sk.pa3kc.util;

import sk.pa3kc.mylibrary.pojo.ObjectPointer;
import sk.pa3kc.mylibrary.util.ArrayUtils;

public class UIThread extends Thread {
    private final double FRAME_LIMIT;
    private final double UPDATE_LIMIT;

    private Runnable[] updateRunnables = new Runnable[0];
    private Runnable[] renderRunnables = new Runnable[0];

    private boolean shutdownRequested = false;
    private boolean running = false;
    private int updateCount = 0;
    private int frameCount = 0;

    public UIThread(double updateLimit, double frameLimit) {
        this.UPDATE_LIMIT = updateLimit;
        this.FRAME_LIMIT = frameLimit;
    }

    //region Getters
    public boolean isRunning() { return this.running; }
    public boolean isShutdownRequested() { return this.shutdownRequested; }
    public double getFrameLimit() { return this.FRAME_LIMIT; }
    public double getUpdateLimit() { return this.UPDATE_LIMIT; }
    public int getFrameCount() { return this.frameCount; }
    public int getUpdateCount() { return this.updateCount; }
    //endregion

    //region Adders
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
    //endregion

    //region Removers
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
    //endregion

    //region Public methods
    public void requestShutdown() {
        this.shutdownRequested = true;
    }
    //endregion

    //region Overrides
    @Override
    public void run() {
        this.running = true;
        System.out.println("uiThread started");

        final double nsPerUpdate = 1000000000.0d / this.UPDATE_LIMIT;
        // final double nsPerFrame = 1000000000.0d / this.FRAME_LIMIT;

        long lastTime = System.nanoTime();
        double unprocessedTime = 0d;

        int frames = 0;
        int updates = 0;

        long frameCounter = System.currentTimeMillis();
        int updateErrorCounter = 0;
        int renderErrorCounter = 0;

        while (!this.shutdownRequested) {
            long currentTime = System.nanoTime();
            long passedTime = currentTime - lastTime;
            lastTime = currentTime;
            unprocessedTime += passedTime;

            if (unprocessedTime >= nsPerUpdate) {
                unprocessedTime = 0;
                try {
                    for (Runnable runnable : this.updateRunnables)
                        runnable.run();
                    updates++;
                    updateErrorCounter = 0;
                } catch (Exception ex) {
                    updateErrorCounter++;
                    if (updateErrorCounter == 5) {
                        ex.printStackTrace();
                        System.exit(0xFF);
                    }
                }
            }

            try {
                for (Runnable runnable : this.renderRunnables)
                    runnable.run();
                frames++;
                renderErrorCounter = 0;
            } catch (Exception ex) {
                renderErrorCounter++;
                if (renderErrorCounter == 5) {
                    ex.printStackTrace();
                    System.exit(0xFF);
                }
            }

            if (System.currentTimeMillis() - frameCounter >= 1000) {
                this.updateCount = updates;
                this.frameCount = frames;

                updates = 0;
                frames = 0;
                frameCounter += 1000;
            }
        }

        this.running = false;
        System.out.println("uiThread stopped");
    }
    //endregion
}
