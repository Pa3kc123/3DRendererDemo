package sk.pa3kc.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;

import sk.pa3kc.Program;
import sk.pa3kc.World;

import sun.java2d.SunGraphics2D;

public class MainCanvas extends DoubleBufferedCanvas {
    private static final long serialVersionUID = 1L;

    private final int FONT_SIZE = 15;
    private final Font FONT = new Font("Dialog", Font.PLAIN, FONT_SIZE);
    private final Toolkit TOOLKIT = Toolkit.getDefaultToolkit();

    private final World world;

    public MainCanvas(World world) {
        super();

        this.world = world;

        super.setBackground(Color.BLACK);
    }

    @Override
    public void paintBuffer(SunGraphics2D g) {
        world.draw(g);

        g.setFont(FONT);
        int stringX = (int) -(super.getWidth() / 2);
        int stringY = (int) -(super.getHeight() / 2) + FONT_SIZE;

        final String[] infoArr = new String[] {
            ("FPS: " + Program.UI_THREAD.getFPS()),
            ("UPS: " + Program.UI_THREAD.getUPS())
        };

        for (int i = 0; i < infoArr.length; i++) {
            g.drawString(infoArr[i], stringX, stringY + (FONT_SIZE * (i+1)));
        }
    }
}
