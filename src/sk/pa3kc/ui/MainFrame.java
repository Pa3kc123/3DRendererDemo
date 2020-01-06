package sk.pa3kc.ui;

import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import sk.pa3kc.Program;
import sk.pa3kc.matrix.Matrix;
import sk.pa3kc.matrix.MatrixMath;
import sk.pa3kc.singletons.Keyboard;
import sk.pa3kc.util.Logger;

public class MainFrame extends Frame implements WindowListener {
    private static final long serialVersionUID = 1L;

    private float angleX = 0f;
    private float deltaX = 0f;
    private float angleY = 0f;
    private float deltaY = 0f;
    private float angleZ = 0f;
    private float deltaZ = 0f;

    public final MainCanvas canvas;

    public MainFrame(String name) {
        super(name);

        super.add(this.canvas = new MainCanvas(Program.world));

        final class MyMouseAdapter implements MouseListener, MouseMotionListener, MouseWheelListener {
            @Override public void mouseWheelMoved(MouseWheelEvent e) {}
            @Override public void mouseDragged(MouseEvent e) {}
            @Override public void mouseMoved(MouseEvent e) {}
            @Override public void mouseClicked(MouseEvent e) {}
            @Override public void mousePressed(MouseEvent e) {}
            @Override public void mouseReleased(MouseEvent e) {}
            @Override public void mouseEntered(MouseEvent e) {}
            @Override public void mouseExited(MouseEvent e) {}
        }
        final MyMouseAdapter adapter = new MyMouseAdapter();
        super.addMouseListener(adapter);
        super.addMouseMotionListener(adapter);
        super.addMouseWheelListener(adapter);

        final class MyKeyAdapter implements KeyListener {
            @Override public void keyTyped(KeyEvent e) {}
            @Override public void keyPressed(KeyEvent e) {}
            @Override public void keyReleased(KeyEvent e) {}
        }
        super.addKeyListener(new MyKeyAdapter());

        super.addWindowListener(this);

        Program.UI_THREAD.getUpdatables().add(new Runnable() {
            @Override
            public void run() {
                Matrix.ROTATION_MATRIX.identify();
                MatrixMath.applyRotationX(Matrix.ROTATION_MATRIX.getAllValues(), Matrix.X_MATRIX.getAllValues(), (angleX / 180f * 3.14159f));
                MatrixMath.applyRotationY(Matrix.ROTATION_MATRIX.getAllValues(), Matrix.Y_MATRIX.getAllValues(), (angleY / 180f * 3.14159f));
                MatrixMath.applyRotationZ(Matrix.ROTATION_MATRIX.getAllValues(), Matrix.Z_MATRIX.getAllValues(), (angleZ / 180f * 3.14159f));
            }
        });

        Program.UI_THREAD.getRenderables().add(new Runnable() {
            @Override
            public void run() {
                canvas.repaint();
            }
        });

        super.setFocusable(true);
        super.setLocation(Program.GRAPHICS_DEVICE_BOUNDS.x, Program.GRAPHICS_DEVICE_BOUNDS.y);
        super.setResizable(false);
        super.setSize(Program.GRAPHICS_DEVICE_BOUNDS.width, Program.GRAPHICS_DEVICE_BOUNDS.height);
        super.setUndecorated(true);
        super.setVisible(true);
    }

    public void updateXRotation(float delta) {
        this.angleX += delta;
        if (this.angleX >= 360f) {
            this.angleX -= 360f;
        } else if (this.angleX < 0f) {
            this.angleX += 359;
        }
    }
    public void updateYRotation(float delta) {
        this.angleY += delta;
        if (this.angleY >= 360f) {
            this.angleY -= 360f;
        } else if (this.angleY < 0f) {
            this.angleY += 359;
        }
    }
    public void updateZRotation(float delta) {
        this.angleZ += delta;
        if (this.angleZ >= 360f) {
            this.angleZ -= 360f;
        } else if (this.angleZ < 0f) {
            this.angleZ += 359;
        }
    }

    @Override
    protected void processKeyEvent(KeyEvent e) {
        super.processKeyEvent(e);
        Keyboard.processKeyEvent(e);
    }

    @Override
    protected void processMouseEvent(MouseEvent e) {
        super.processMouseEvent(e);
    }

    @Override
    protected void processMouseMotionEvent(MouseEvent e) {
        super.processMouseMotionEvent(e);
    }

    @Override
    protected void processMouseWheelEvent(MouseWheelEvent e) {
        super.processMouseWheelEvent(e);
    }

    @Override
    public void dispose() {
        if (!Program.UI_THREAD.isShutdownRequested())
            Program.UI_THREAD.requestShutdown();
        super.dispose();
    }

    @Override
    public void windowOpened(WindowEvent e) {
        Logger.DEBUG("windowOpened");
    }

    @Override
    public void windowClosing(WindowEvent e) {
        Logger.DEBUG("windowClosing");
        this.dispose();
    }

    @Override
    public void windowClosed(WindowEvent e) {
        Logger.DEBUG("windowClosed");
    }

    @Override
    public void windowIconified(WindowEvent e) {
        Logger.DEBUG("windowIconified");
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        Logger.DEBUG("windowDeiconified");
    }

    @Override
    public void windowActivated(WindowEvent e) {
        Logger.DEBUG("windowActivated");
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        Logger.DEBUG("windowDeactivated");
    }
}
