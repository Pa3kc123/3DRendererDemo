package sk.pa3kc.ui;

import java.awt.event.KeyEvent;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import sk.pa3kc.Program;
import sk.pa3kc.util.Schemas;
import sk.pa3kc.util.Matrix.Editor;
import sk.pa3kc.enums.UpdateMode;
import sk.pa3kc.inter.Updatable;
import sk.pa3kc.singletons.Keyboard;
import sk.pa3kc.singletons.Locks;
import sk.pa3kc.singletons.Matrixes;

public class MyFrame extends JFrame implements Updatable {
    private static final long serialVersionUID = 1L;

    public final double distanceMax = 10d;
    public final double distanceMin = 1d;
    public double distance = 1d;

    public int updateCount = 0;
    public Thread updateThread;
    public boolean updateThreadRunning = false;

    //region Components
    public final MyPanel myPanel = new MyPanel();
    private final MySlider xSlider = new MySlider(this.sliderMin, this.sliderMax, this.sliderCur, this, UpdateMode.X);
    private final MySlider ySlider = new MySlider(this.sliderMin, this.sliderMax, this.sliderCur, this, UpdateMode.Y);
    private final MySlider zSlider = new MySlider(this.sliderMin, this.sliderMax, this.sliderCur, this, UpdateMode.Z);
    //endregion

    //region Slider values
    private final int sliderMin = -90;
    private final int sliderCur = 0;
    private final int sliderMax = 269;
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

        this.updateThread = new Thread(new Runnable() {
            @Override
            public void run(){
                updateThreadRunning = true;

                while (updateThreadRunning) {
                    if (Program.toggled) {
                        int value = 1;
                        int countedValue = ySlider.getValue() + value;
                        xSlider.setValue(countedValue > sliderMax ? sliderMin : countedValue);
                        ySlider.setValue(countedValue > sliderMax ? sliderMin : countedValue);
                        zSlider.setValue(countedValue > sliderMax ? sliderMin : countedValue);
                        update(UpdateMode.ALL);
                    } else {
                        synchronized (Locks.KEYBOARD_LOCK) {
                            try {
                                Locks.KEYBOARD_LOCK.wait();
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                }
            }
        });

        synchronized (Locks.MY_FRAME_LOCK) {
            Locks.MY_FRAME_LOCK.notify();
        }
        this.updateThread.start();
    }

    @Override
    protected void processKeyEvent(KeyEvent e) {
        super.processKeyEvent(e);
        Keyboard.getInst().processKeyEvent(e);
    }

    @Override
    public void update(UpdateMode plane) {
        if (plane.x == true) Schemas.applyRotationX(Matrixes.xMatrix, StrictMath.toRadians(this.xSlider.getValue()));
        if (plane.y == true) Schemas.applyRotationY(Matrixes.yMatrix, StrictMath.toRadians(this.ySlider.getValue()));
        if (plane.z == true) Schemas.applyRotationZ(Matrixes.zMatrix, StrictMath.toRadians(this.zSlider.getValue()));
        if (plane.w == true) this.distance = this.distance > this.distanceMax ? this.distanceMax : this.distance < this.distanceMin ? this.distanceMin : this.distance;

        Editor editor = Editor.edit(Matrixes.rotationMatrix).indentify();
        editor.multiply(Matrixes.xMatrix);
        editor.multiply(Matrixes.yMatrix);
        editor.multiply(Matrixes.zMatrix);

        this.updateCount++;
    }

    @Override
    public void dispose() {
        super.dispose();
        Program.getInst().uiThreadRunning = false;
        this.myPanel.fpsThreadRunning = false;
        this.updateThreadRunning = false;
        Locks.notifyAllLocks();
    }
}
