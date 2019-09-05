package sk.pa3kc.geom;

import java.awt.Graphics;

import sk.pa3kc.inter.Drawable;
import sk.pa3kc.util.Vertex;

public class Box implements Drawable {
    public final Vertex[] vertexes = new Vertex[8];

    public Box(Vertex ver, int length) {
        this(ver, length, length, length);
    }
    public Box(Vertex ver, int length, int height, int width) {
        this.vertexes[0] = new Vertex(ver.getX(), ver.getY(), ver.getZ());
        this.vertexes[1] = new Vertex(ver.getX(), ver.getY() + height, ver.getZ());
        this.vertexes[2] = new Vertex(ver.getX() + length, ver.getY() + height, ver.getZ());
        this.vertexes[3] = new Vertex(ver.getX() + length, ver.getY(), ver.getZ());

        this.vertexes[4] = new Vertex(ver.getX(), ver.getY(), ver.getZ() + width);
        this.vertexes[5] = new Vertex(ver.getX(), ver.getY() + height, ver.getZ() + width);
        this.vertexes[6] = new Vertex(ver.getX() + length, ver.getY() + height, ver.getZ() + width);
        this.vertexes[7] = new Vertex(ver.getX() + length, ver.getY(), ver.getZ() + width);
    }

    @Override
    public Vertex[] getAll() { return this.vertexes; }
    @Override
    public Vertex[] cloneAll() {
        final Vertex[] vertexCopy = new Vertex[this.vertexes.length];
        for (int i = 0; i < this.vertexes.length; i++)
            vertexCopy[i] = (Vertex)this.vertexes[i].clone();

        return vertexCopy;
    }
    @Override
    public void draw(Graphics g) {
        if (g == null) return;

        final Vertex[] vertexCopy = this.cloneAll();
        Shape3D.transform(vertexCopy);

        for (int i = 0; i < vertexCopy.length; i++)
            g.drawString(String.valueOf(i), (int)vertexCopy[i].getX(), (int)vertexCopy[i].getY());

        for (int i = 0; i < 4; i++) {
            int x1 = (int)vertexCopy[i].getX();
            int y1 = (int)vertexCopy[i].getY();
            int x2 = (int)vertexCopy[i+1 == 4 ? 0 : i+1].getX();
            int y2 = (int)vertexCopy[i+1 == 4 ? 0 : i+1].getY();

            g.drawLine(x1, y1, x2, y2);
        }

        for (int i = 4; i < 8; i++) {
            int x1 = (int)vertexCopy[i].getX();
            int y1 = (int)vertexCopy[i].getY();
            int x2 = (int)vertexCopy[i+1 == 8 ? 4 : i+1].getX();
            int y2 = (int)vertexCopy[i+1 == 8 ? 4 : i+1].getY();

            g.drawLine(x1, y1, x2, y2);
        }

        for (int i = 0; i < 4; i++) {
            int x1 = (int)vertexCopy[i].getX();
            int y1 = (int)vertexCopy[i].getY();
            int x2 = (int)vertexCopy[i+4].getX();
            int y2 = (int)vertexCopy[i+4].getY();

            g.drawLine(x1, y1, x2, y2);
        }
    }
}
