package sk.pa3kc.ui;

import java.awt.event.KeyEvent;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import sk.pa3kc.Program;
import sk.pa3kc.util.MatrixEditor;
import sk.pa3kc.util.Schemas;
import sk.pa3kc.enums.UpdateMode;
import sk.pa3kc.inter.Updatable;
import sk.pa3kc.singletons.Keyboard;
import sk.pa3kc.singletons.Locks;
import sk.pa3kc.singletons.Matrixes;
import sk.pa3kc.singletons.MyThreadPool;

public class MyFrame extends JFrame implements Updatable {
    private static final long serialVersionUID = 1L;

    public final double distanceMax = 10d;
    public final double distanceMin = 1d;
    public double distance = 1d;

    //region Components
    public final MyPanel myPanel = new MyPanel();
    public final MySlider xSlider = new MySlider(this.sliderMin, this.sliderMax, this.sliderCur, this, UpdateMode.X);
    public final MySlider ySlider = new MySlider(this.sliderMin, this.sliderMax, this.sliderCur, this, UpdateMode.Y);
    public final MySlider zSlider = new MySlider(this.sliderMin, this.sliderMax, this.sliderCur, this, UpdateMode.Z);
    //endregion

    //region Slider values
    public final int sliderMin = -90;
    public final int sliderCur = 0;
    public final int sliderMax = 269;
    //endregion

    public MyFrame(String name) {
        super(name);
        JPanel pane = (JPanel)super.getContentPane();
        super.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));

        pane.add(this.myPanel);

        super.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        super.setFocusable(true);
        super.setLocation(Program.getInst().GRAPHICS_DEVICE_BOUNDS.x, Program.getInst().GRAPHICS_DEVICE_BOUNDS.y);
        super.setResizable(false);
        super.setSize(Program.getInst().GRAPHICS_DEVICE_BOUNDS.width, Program.getInst().GRAPHICS_DEVICE_BOUNDS.height);
        super.setUndecorated(true);
        super.setVisible(true);

        this.update(UpdateMode.ALL);

        synchronized (Locks.UI_THREAD_LOCK) {
            Locks.UI_THREAD_LOCK.notify();
        }
    }

    @Override
    protected void processKeyEvent(KeyEvent e) {
        super.processKeyEvent(e);
        Keyboard.getInst().processKeyEvent(e);
    }

    @Override
    public void update(UpdateMode plane) {
        if (plane.x) Schemas.applyRotationX(Matrixes.xMatrix, StrictMath.toRadians(this.xSlider.getValue()));
        if (plane.y) Schemas.applyRotationY(Matrixes.yMatrix, StrictMath.toRadians(this.ySlider.getValue()));
        if (plane.z) Schemas.applyRotationZ(Matrixes.zMatrix, StrictMath.toRadians(this.zSlider.getValue()));
        if (plane.w) this.distance = this.distance > this.distanceMax ? this.distanceMax : this.distance < this.distanceMin ? this.distanceMin : this.distance;

        MatrixEditor editor = new MatrixEditor(Matrixes.rotationMatrix);
        editor.identify();
        editor.multiply(Matrixes.xMatrix);
        editor.multiply(Matrixes.yMatrix);
        editor.multiply(Matrixes.zMatrix);
    }

    @Override
    public void dispose() {
        super.dispose();
        MyThreadPool.requestShutdownOnAllThreads();
        Locks.notifyAllLocks();
    }
}
