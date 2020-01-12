package sk.pa3kc.util;

import java.util.ArrayList;

import org.lwjgl.glfw.GLFW;

import sk.pa3kc.mylibrary.util.StringUtils;
import sk.pa3kc.singletons.Configuration;
import sk.pa3kc.ui.GLWindow;

public abstract class UIThread implements Runnable {
    private enum ThreadState {
        RUNNING,
        // PAUSING,
        // PAUSED,
        STOPPING,
        STOPPED
    }

    private ArrayList<Runnable> updatables = new ArrayList<Runnable>();
    private ArrayList<Runnable> renderables = new ArrayList<Runnable>();

    private final Thread thread;
    private ThreadState state = ThreadState.STOPPED;
    private long window = GLWindow.GL_NULL;

    private int updateCount = 0;
    private int frameCount = 0;

    public UIThread() {
        this.thread = new Thread(this);
    }

    //region Getters
    public ArrayList<Runnable> getUpdatables() {
        return this.updatables;
    }
    public ArrayList<Runnable> getRenderables() {
        return this.renderables;
    }
    public int getFPS() {
        return this.frameCount;
    }
    public int getUPS() {
        return this.updateCount;
    }
    //endregion

    //region Setters
    public void setWindow(long window) {
        this.window = window;
    }
    //endregion

    //region Public methods
    public void start() {
        this.threadStateChanged(ThreadState.RUNNING);
        this.thread.start();
    }
    public void stop() {
        this.threadStateChanged(ThreadState.STOPPING);
    }
    public void threadStateChanged(ThreadState newState) {
        this.state = newState;
        Logger.DEBUG(StringUtils.build("UIThread#threadStateChanged -> ", this.state.toString()));
    }
    //endregion

    //region Overrides
    @Override
    public void run() {
        this.init();

        final Timer timer = new Timer(false);
        long msecPerUpdate = Configuration.getInst().getMaxUps();
        long msecPerRender = Configuration.getInst().getMaxFps();
        long msecPerLog = 1000;

        if (Configuration.getInst().getMaxUps() != 0) {
            msecPerUpdate = 1000 / Configuration.getInst().getMaxUps();
        }

        if (Configuration.getInst().getMaxFps() != 0) {
            msecPerRender = 1000 / Configuration.getInst().getMaxFps();
        }

        long lastRender;
        long lastUpdate;
        long lastLog;
        {
            long lastIteration = System.currentTimeMillis();
            lastRender = lastIteration;
            lastUpdate = lastIteration;
            lastLog = lastIteration;
        }

        int frames = 0;
        int updates = 0;
        long delta;

        while (this.state != ThreadState.STOPPING) {
            long now = System.currentTimeMillis();

            // should we draw next frame?
            if (msecPerRender != 0L) {
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
            if (msecPerUpdate != 0) {
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
                if (this.window != GLWindow.GL_NULL) {
                    GLFW.glfwSetWindowTitle(this.window, "FPS: " + this.frameCount + " | UPS: " + this.updateCount);
                }

                frames = 0;
                updates = 0;
            }

            // long cycle = timer.cycle();
            // if (cycle > Configuration.getInst().getUiCycleWarnTime()) {
            //     Logger.WARN(StringUtils.build("UI cycle took ", cycle, "ms"));
            // }

            timer.reset();
        }

        this.dispose();
        this.threadStateChanged(ThreadState.STOPPED);
    }
    //endregion

    public abstract void init();
    public abstract void dispose();
}
