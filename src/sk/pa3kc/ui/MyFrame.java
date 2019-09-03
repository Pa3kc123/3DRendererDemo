package sk.pa3kc.ui;

import java.awt.event.KeyEvent;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import sk.pa3kc.Program;
import sk.pa3kc.util.Schemas;
import sk.pa3kc.enums.UpdateMode;
import sk.pa3kc.inter.Updatable;
import sk.pa3kc.matrix.MatrixEditor;
import sk.pa3kc.mylibrary.util.NumberUtils;
import sk.pa3kc.singletons.Keyboard;
import sk.pa3kc.singletons.Locks;

import static sk.pa3kc.singletons.Matrixes.*;

public class MyFrame extends JFrame implements Updatable {
    private static final long serialVersionUID = 1L;

    private final MatrixEditor editor = MatrixEditor.empty();

    public final double distanceMax = 10d;
    public final double distanceMin = 1d;
    public double distance = 1d;

    //region Components
    public final MyPanel myPanel = new MyPanel();
    //endregion

    //region Slider values
    public final int sliderMin = -90;
    public final int sliderDef = 45;
    public final int sliderMax = 269;
    public int sliderCur = sliderDef;
    //endregion

    public MyFrame(String name) {
        super(name);

        final Runnable action = new Runnable() {
            @Override
            public void run() {
                sliderCur++;
            }
        };

        Keyboard.getKeyInfo('g').addOnTypedAction(action);
        Keyboard.getKeyInfo('h').addOnTypedAction(action);

        final JPanel pane = (JPanel)super.getContentPane();
        super.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));

        pane.add(this.myPanel);

        super.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        super.setFocusable(true);
        super.setLocation(Program.GRAPHICS_DEVICE_BOUNDS.x, Program.GRAPHICS_DEVICE_BOUNDS.y);
        super.setResizable(false);
        super.setSize(Program.GRAPHICS_DEVICE_BOUNDS.width, Program.GRAPHICS_DEVICE_BOUNDS.height);
        super.setUndecorated(true);
        super.setVisible(true);

        this.update(UpdateMode.ALL);

        Locks.MY_FRAME_LOCK.unlockOne();
    }

    @Override
    protected void processKeyEvent(KeyEvent e) {
        super.processKeyEvent(e);
        Keyboard.processKeyEvent(e);
    }

    @Override
    public void update(UpdateMode plane) {
        if (plane.x) Schemas.applyRotationX(X_MATRIX, StrictMath.toRadians(this.sliderCur));
        if (plane.y) Schemas.applyRotationY(Y_MATRIX, StrictMath.toRadians(this.sliderCur));
        if (plane.z) Schemas.applyRotationZ(Z_MATRIX, StrictMath.toRadians(this.sliderCur));
        if (plane.w) NumberUtils.map(this.distance, this.distanceMin, this.distanceMax);

        if (ROTATION_MATRIX.isBeingEdited())
            ROTATION_MATRIX.waitForUnlock();

        editor.changeReference(ROTATION_MATRIX).identify();

        if (X_MATRIX.isBeingEdited())
            X_MATRIX.waitForUnlock();
        editor.multiply(X_MATRIX);

        if (Y_MATRIX.isBeingEdited())
            Y_MATRIX.waitForUnlock();
        editor.multiply(Y_MATRIX);

        if (Z_MATRIX.isBeingEdited())
            Z_MATRIX.waitForUnlock();
        editor.multiply(Z_MATRIX);

        editor.release();
    }

    @Override
    public void dispose() {
        if (!Program.uiThread.isShutdownRequested())
            Program.uiThread.requestShutdown();
        Locks.unlockAllLocks();
        super.dispose();
    }
}
