package sk.pa3kc.geom;

import java.awt.Color;
import java.awt.Graphics;

import sk.pa3kc.inter.Drawable;
import sk.pa3kc.util.Matrix;
import sk.pa3kc.util.Vertex;

public class Triangle implements Drawable {
    public final Vertex[] vertexes = new Vertex[3];
    public final Color color;

    public Triangle(Vertex ver1, Vertex ver2, Vertex ver3, Color color) {
        this.vertexes[0] = ver1;
        this.vertexes[1] = ver2;
        this.vertexes[2] = ver3;
        this.color = color;
    }
    public Triangle(Vertex ver1, Vertex ver2, Color color) {
        double length = Vertex.getLength(ver1, ver2);
        double o = (double)length * 3d;
        double s = o / 2d;
        double S = StrictMath.sqrt(s * StrictMath.pow((s - length), 3d));

        this.vertexes[0] = ver1;
        this.vertexes[1] = new Vertex(ver1.getX() + ((double)length / 2d), ver1.getY() - (2d * (S / length)), ver1.getZ());
        this.vertexes[2] = ver2;
        this.color = color;
    }

    public Vertex[] getAll() { return this.vertexes.clone(); }

    @Override
    public void draw(Graphics g) {
        if (g == null) return;

        Vertex[] vertexCopy = this.vertexes.clone();

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
