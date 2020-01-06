package sk.pa3kc.ui;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.image.VolatileImage;

import sun.java2d.SunGraphics2D;

public abstract class DoubleBufferedCanvas extends Canvas {
    private static final long serialVersionUID = 1L;
    private VolatileImage backendImage;
    private SunGraphics2D backendGraphics;
    private int fontSize;

    public DoubleBufferedCanvas() {
        super();
    }

    public int getFontSize() {
        return this.fontSize;
    }

    private void resetBuffer() {
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
        this.backendGraphics = (SunGraphics2D)this.backendImage.getGraphics();
        this.fontSize = this.backendGraphics.getFont().getSize();
    }

    @Override
    public boolean isDoubleBuffered() {
        return true;
    }
    @Override
    public void update(Graphics g) {
        // super.update(g);
        this.paint(g);
    }
    @Override
    public void paint(Graphics g) {
        // super.paint(g);
        if (this.backendImage == null || this.backendImage.validate(super.getGraphicsConfiguration()) != VolatileImage.IMAGE_OK || this.backendImage.contentsLost() || this.backendGraphics == null) {
            this.resetBuffer();
        }

        this.backendGraphics.clearRect(0, 0, this.getWidth(), this.getHeight());

        this.paintBuffer(this.backendGraphics);

        if (!this.backendImage.contentsLost()) {
            g.drawImage(this.backendImage, 0, 0, this);
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

    public abstract void paintBuffer(SunGraphics2D g);
}
