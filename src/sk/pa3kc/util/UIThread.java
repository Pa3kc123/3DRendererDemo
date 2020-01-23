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
    private final Thread logThread;
    private ThreadState state = ThreadState.STOPPED;
    private long window = GLWindow.GL_NULL;

    private int lastFrameCount = 0;
    private int lastUpdateCount = 0;
    private int currFrameCount = 0;
    private int currUpdateCount = 0;

    public UIThread() {
        this.thread = new Thread(this);
        this.logThread = new Thread(() -> {
            while (this.state != ThreadState.STOPPING) {
                try {
                    Thread.sleep(1000);

                    this.lastUpdateCount = this.currUpdateCount;
                    this.lastFrameCount = this.currFrameCount;
                    this.currUpdateCount = 0;
                    this.currFrameCount = 0;

                    System.out.println("FPS: " + this.lastFrameCount + " | UPS: " + this.lastUpdateCount);
                    } catch (Throwable ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    //region Getters
    public ArrayList<Runnable> getUpdatables() {
        return this.updatables;
    }
    public ArrayList<Runnable> getRenderables() {
        return this.renderables;
    }
    public int getFPS() {
        return this.lastFrameCount;
    }
    public int getUPS() {
        return this.lastUpdateCount;
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

        if (Configuration.getInst().getMaxUps() != 0) {
            msecPerUpdate = 1000 / Configuration.getInst().getMaxUps();
        }

        if (Configuration.getInst().getMaxFps() != 0) {
            msecPerRender = 1000 / Configuration.getInst().getMaxFps();
        }

        long lastRender;
        long lastUpdate;
        lastRender = lastUpdate = System.currentTimeMillis();

        long delta;
        while (this.state != ThreadState.STOPPING) {
            long now = System.currentTimeMillis();

            // should we draw next frame?
            if (msecPerRender != 0L) {
                delta = now - lastRender;
                if (delta >= msecPerRender) {
                    lastRender = now - (delta - msecPerRender);

                    render();
                }
            } else render();

            // is it time for updating next game iteration?
            if (msecPerUpdate != 0) {
                delta = now - lastUpdate;
                if (delta >= msecPerUpdate) {
                    lastUpdate = now - (delta - msecPerUpdate);
                    update();
                }
            } else update();

            timer.reset();
        }

        this.dispose();
        this.threadStateChanged(ThreadState.STOPPED);
    }
    //endregion

    private void update() {
        GLFW.glfwPollEvents();
        for (Runnable task : this.updatables) {
            task.run();
        }
        this.currUpdateCount++;
    }
    private void render() {
        for (Runnable task : this.renderables) {
            task.run();
        }
        this.currFrameCount++;
        GLFW.glfwSwapBuffers(this.window);
    }

    public abstract void init();
    public abstract void dispose();
}
