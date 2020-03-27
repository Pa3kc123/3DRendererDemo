package sk.pa3kc.ui;

import java.awt.Frame;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import sk.pa3kc.Program;
import sk.pa3kc.util.Logger;

public class MainFrame extends Frame implements WindowListener {
    private static final long serialVersionUID = 1L;

    public final MainCanvas canvas;

    public MainFrame(String name) {
        super(name);

        super.add(this.canvas = new MainCanvas(Program.world));

        super.addWindowListener(this);

        super.setFocusable(true);
        super.setLocation(Program.GRAPHICS_DEVICE_BOUNDS.x, Program.GRAPHICS_DEVICE_BOUNDS.y);
        super.setResizable(false);
        super.setSize(Program.GRAPHICS_DEVICE_BOUNDS.width, Program.GRAPHICS_DEVICE_BOUNDS.height);
        super.setUndecorated(true);
        super.setVisible(true);
    }

    @Override
    public void dispose() {
        Program.UI_THREAD.stop();
        canvas.dispose();
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

    private static float angleX = 0f;
    private static float deltaX = 0f;
    private static float angleY = 0f;
    private static float deltaY = 0f;
    private static float angleZ = 0f;

    private static boolean mouse1Pressed = false;
    private static boolean mouse2Pressed = false;
    private static boolean mouse3Pressed = false;
    private static int lastX = 0;
    private static int lastY = 0;

    public static void updateXRotation(float delta) {
        angleX += delta;
        if (angleX >= 360f) {
            angleX -= 360f;
        } else if (angleX < 0f) {
            angleX += 359;
        }
    }
    public static void updateYRotation(float delta) {
        angleY += delta;
        if (angleY >= 360f) {
            angleY -= 360f;
        } else if (angleY < 0f) {
            angleY += 359;
        }
    }
    public static void updateZRotation(float delta) {
        angleZ += delta;
        if (angleZ >= 360f) {
            angleZ -= 360f;
        } else if (angleZ < 0f) {
            angleZ += 359;
        }
    }
}
