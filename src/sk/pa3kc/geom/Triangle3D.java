package sk.pa3kc.geom;

import java.awt.Color;

import sk.pa3kc.inter.Drawable;
import sk.pa3kc.util.Vertex;

import sun.java2d.SunGraphics2D;

public class Triangle3D implements Drawable {
    public final Vertex[] vertexes = new Vertex[3];
    public final Color color;

    public Triangle3D(Vertex ver1, Vertex ver2, Vertex ver3, Color color) {
        this.vertexes[0] = ver1;
        this.vertexes[1] = ver2;
        this.vertexes[2] = ver3;
        this.color = color;
    }

    @Override
    public void draw(SunGraphics2D g) {
        if (g == null) return;

        final Vertex[] vertexCopy = new Vertex[this.vertexes.length];
        for (int i = 0; i < this.vertexes.length; i++) {
            vertexCopy[i] = this.vertexes[i].clone();
        }

        Shape3D.transform(vertexCopy);

        for (int i = 0; i < vertexCopy.length; i++) {
            int x1 = (int) vertexCopy[i].getX();
            int y1 = (int) vertexCopy[i].getY();
            int x2 = (int) vertexCopy[i+1 == vertexCopy.length ? 0 : i+1].getX();
            int y2 = (int) vertexCopy[i+1 == vertexCopy.length ? 0 : i+1].getY();
            g.drawLine(x1, y1, x2, y2);
        }
    }
}
