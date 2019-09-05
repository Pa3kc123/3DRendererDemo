package sk.pa3kc.geom;

import java.awt.Color;
import java.awt.Graphics;

import sk.pa3kc.inter.Drawable;
import sk.pa3kc.util.Vertex;

public class Path3D implements Drawable {
    public final Vertex[] vertexes = new Vertex[2];
    public final Color color;
    public final String text;

    public Path3D(Vertex ver1, Vertex ver2, Color color, String text) {
        this.vertexes[0] = ver1;
        this.vertexes[1] = ver2;
        this.color = color;
        this.text = text;
    }

    @Override
    public Vertex[] getAll() { return this.vertexes; }
    @Override
    public Vertex[] cloneAll() { return this.vertexes.clone(); }
    @Override
    public void draw(Graphics g) {
        if (g == null) return;

        final Vertex[] vertexCopy = new Vertex[this.vertexes.length];
        for (int i = 0; i < this.vertexes.length; i++)
            vertexCopy[i] = (Vertex)this.vertexes[i].clone();

        Shape3D.transform(vertexCopy);

        int x1 = (int) vertexCopy[0].getX();
        int y1 = (int) vertexCopy[0].getY();
        int x2 = (int) vertexCopy[1].getX();
        int y2 = (int) vertexCopy[1].getY();

        g.setColor(this.color);
        g.drawLine(x1, y1, x2, y2);
        g.setColor(Color.WHITE);
        g.drawString(this.text, x2, y2);
    }
}
