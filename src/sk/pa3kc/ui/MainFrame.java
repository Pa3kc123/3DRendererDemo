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
        Program.UI_THREAD.setRequestShutdown(true);
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
