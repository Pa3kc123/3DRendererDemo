package sk.pa3kc.geom;

import java.awt.Color;
import java.awt.Graphics2D;

import sk.pa3kc.pojo.Vertex;

public class Path3D extends Drawable {
    public final Vertex[] vertecies = new Vertex[2];
    public final Color color;
    public final String text;

    public Path3D(Vertex ver1, Vertex ver2, Color color, String text) {
        this.vertecies[0] = ver1;
        this.vertecies[1] = ver2;
        this.color = color;
        this.text = text;
    }

    @Override
    public void draw(Graphics2D g) {
        final Vertex[] vertecies = super.translate(this.vertecies);

        int x1 = (int) vertecies[0].getX();
        int y1 = (int) vertecies[0].getY();
        int x2 = (int) vertecies[1].getX();
        int y2 = (int) vertecies[1].getY();

        g.setColor(this.color);
        g.drawLine(x1, y1, x2, y2);
        g.setColor(Color.WHITE);
        g.drawString(this.text, x2, y2);
    }

    @Override
    public void drawGL() {
    }
}
