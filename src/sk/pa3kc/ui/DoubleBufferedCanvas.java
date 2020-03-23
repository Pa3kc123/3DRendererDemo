package sk.pa3kc.ui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.VolatileImage;

import sk.pa3kc.Program;

public abstract class DoubleBufferedCanvas extends Canvas {
    private static final long serialVersionUID = 1L;
    private VolatileImage backendImage;
    private Graphics2D backendGraphics;
    private int fontSize;

    public DoubleBufferedCanvas() {
        super(Program.GRAPHICS_DEVICE_CONFIG);
    }

    public int getFontSize() {
        return this.fontSize;
    }

    private void resetBuffer() {
        System.out.println("Buffer reset");
        if (this.backendGraphics != null) {
            this.backendGraphics.dispose();
            this.backendGraphics = null;
        }
        if (this.backendImage != null) {
            this.backendImage.flush();
            this.backendImage = null;
        }
        System.gc();

        this.backendImage = super.createVolatileImage(super.getWidth(), super.getHeight());
        this.backendGraphics = this.backendImage.createGraphics();
        this.fontSize = this.backendGraphics.getFont().getSize();
    }

    @Override
    public boolean isDoubleBuffered() {
        return true;
    }
    @Override
    public void repaint() {
        super.repaint();
        synchronized (this) {
            try {
                this.wait();
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
        }
    }
    @Override
    public void update(Graphics g) {
        // super.update(g);
        this.paint(g);
    }
    @Override
    public void paint(Graphics g) {
        // super.paint(g);
        if (this.backendImage == null || this.backendGraphics == null) {
            this.resetBuffer();
        }
        final int validation = this.backendImage.validate(Program.GRAPHICS_DEVICE_CONFIG);
        if (validation != VolatileImage.IMAGE_OK && validation != VolatileImage.IMAGE_RESTORED) {
            this.resetBuffer();
        }
        if (this.backendImage.contentsLost()) {
            this.resetBuffer();
        }

        this.backendGraphics.clearRect(0, 0, this.getWidth(), this.getHeight());

        this.paintBuffer(this.backendGraphics);

        if (!this.backendImage.contentsLost()) {
            g.drawImage(this.backendImage, 0, 0, this);
            synchronized (this) {
                this.notify();
            }
        } else {
            System.out.println("Backend image content lost");
        }
    }

    public void dispose() {
        if (this.backendGraphics != null) {
            this.backendGraphics.dispose();
        }
        this.backendGraphics = null;

        if (this.backendImage != null) {
            this.backendImage.flush();
        }
        this.backendImage = null;
    }

    public abstract void paintBuffer(Graphics2D g);
}
