package sk.pa3kc.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.WindowConstants;

import sk.pa3kc.Program;
import sk.pa3kc.util.Matrix;
import sk.pa3kc.util.Schemas;
import sk.pa3kc.util.Matrix.Editor;
import sk.pa3kc.enums.UpdateMode;
import sk.pa3kc.inter.Updatable;

public class MyFrame extends JFrame implements Updatable, ActionListener
{
    private static final long serialVersionUID = 1L;

    public final double distanceMax = 10d;
    public final double distanceMin = 1d;
    public double distance = 1d;

    private final Timer timer = new Timer(20, this);

    @Override
    public void actionPerformed(ActionEvent e)
    {
        int value = 1;
        int countedValue = this.ySlider.getValue() + value;
        this.xSlider.setValue(countedValue > this.sliderMax ? this.sliderMin : countedValue);
        this.ySlider.setValue(countedValue > this.sliderMax ? this.sliderMin : countedValue);
        this.zSlider.setValue(countedValue > this.sliderMax ? this.sliderMin : countedValue);
        this.update(UpdateMode.ALL);
    }

    //region Components
    public final MyPanel myPanel = new MyPanel();
    private final MySlider xSlider = new MySlider(this.sliderMin, this.sliderMax, this.sliderCur, this, UpdateMode.X);
    private final MySlider ySlider = new MySlider(this.sliderMin, this.sliderMax, this.sliderCur, this, UpdateMode.Y);
    private final MySlider zSlider = new MySlider(this.sliderMin, this.sliderMax, this.sliderCur, this, UpdateMode.Z);
    //endregion

    //region Slider values
    private final int sliderMin = -90;
    private final int sliderCur = -90;
    private final int sliderMax = 269;
    //endregion

    //region Matrixes
    private final Matrix xMatrix = new Matrix(4, 4);
    private final Matrix yMatrix = new Matrix(4, 4);
    private final Matrix zMatrix = new Matrix(4, 4);
    public Matrix rotationMatrix = new Matrix(4, 4);
    private final Matrix projectionMatrix = new Matrix(4, 4);
    //endregion

    private final MyDebugPanel xRotationDebugPanel;
    private final MyDebugPanel yRotationDebugPanel;
    private final MyDebugPanel zRotationDebugPanel;
    private final MyDebugPanel rotationDebugPanel;

    public MyFrame(String name)
    {
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

        // this.xRotationDebugPanel = new MyDebugPanel(this.xMatrix.getRowCount(), this.xMatrix.getColCount(), "X");
        // this.yRotationDebugPanel = new MyDebugPanel(this.yMatrix.getRowCount(), this.yMatrix.getColCount(), "Y");
        // this.zRotationDebugPanel = new MyDebugPanel(this.zMatrix.getRowCount(), this.zMatrix.getColCount(), "Z");
        // this.rotationDebugPanel = new MyDebugPanel(this.rotationMatrix.getRowCount(), this.rotationMatrix.getColCount(), "Rotation");

        this.xRotationDebugPanel = null;
        this.yRotationDebugPanel = null;
        this.zRotationDebugPanel = null;
        this.rotationDebugPanel = null;

        /*MyDebugPanel.show(new MyDebugPanel[][]
        {
            { this.xRotationDebugPanel, this.yRotationDebugPanel },
            { this.zRotationDebugPanel, this.rotationDebugPanel }
        });*/

        this.update(UpdateMode.ALL);

        synchronized (Program.globalLock) {
            Program.globalLock.notify();
        }
    }

    boolean toggleOn = false;

    @Override
    protected void processKeyEvent(KeyEvent e)
    {
        super.processKeyEvent(e);
        if (e.getKeyCode() == KeyEvent.VK_G)
        switch (e.getID())
        {
            case KeyEvent.KEY_PRESSED:
                this.toggleOn = !this.toggleOn;
                keyUpdate();
            break;
        }
    }

    private void keyUpdate()
    {
        if (this.toggleOn == true)
            this.timer.start();
        else
            this.timer.stop();
    }

    @Override
    public void update(UpdateMode plane)
    {
        if (plane.x == true) Schemas.applyRotationX(this.xMatrix, StrictMath.toRadians(this.xSlider.getValue()));
        if (plane.y == true) Schemas.applyRotationY(this.yMatrix, StrictMath.toRadians(this.ySlider.getValue()));
        if (plane.z == true) Schemas.applyRotationZ(this.zMatrix, StrictMath.toRadians(this.zSlider.getValue()));
        if (plane.w == true) this.distance = this.distance > this.distanceMax ? this.distanceMax : this.distance < this.distanceMin ? this.distanceMin : this.distance;

        Editor editor = Editor.edit(this.rotationMatrix).indentify();
        editor.multiply(this.xMatrix);
        editor.multiply(this.yMatrix);
        editor.multiply(this.zMatrix);

        if (this.xRotationDebugPanel != null)
            this.xRotationDebugPanel.updateValues(this.xMatrix);
        if (this.yRotationDebugPanel != null)
            this.yRotationDebugPanel.updateValues(this.yMatrix);
        if (this.zRotationDebugPanel != null)
            this.zRotationDebugPanel.updateValues(this.zMatrix);
        if (this.rotationDebugPanel != null)
            this.rotationDebugPanel.updateValues(this.rotationMatrix);
    }

    @Override
    public void dispose() {
        super.dispose();
        Program.getInst().uiThreadRunning = false;
        this.myPanel.fpsThreadRunning = false;
    }
}
