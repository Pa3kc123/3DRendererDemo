package sk.pa3kc.geom;

import java.awt.Color;
import java.awt.Graphics2D;

import org.lwjgl.opengl.GL11;

import sk.pa3kc.Program;
import sk.pa3kc.geom.Drawable;
import sk.pa3kc.matrix.MatrixMath;
import sk.pa3kc.matrix.VertexMath;
import sk.pa3kc.pojo.Matrix;
import sk.pa3kc.pojo.Vertex;

public class Triangle3D extends Drawable {
    public final Vertex[] vertecies = new Vertex[3];
    public final Color color;

    public float dotProduct = 0f;

    public Triangle3D(Vertex ver1, Vertex ver2, Vertex ver3) {
        this(ver1, ver2, ver3, Color.WHITE);
    }
    public Triangle3D(Vertex ver1, Vertex ver2, Vertex ver3, Color color) {
        this.vertecies[0] = ver1;
        this.vertecies[1] = ver2;
        this.vertecies[2] = ver3;
        this.color = color;
    }

    @Override
    protected Vertex[] translate(Vertex[] vertecies) {
        final Vertex[] clones = super.translate(this.vertecies);

        this.dotProduct = MatrixMath.dotProduct(super.normal.getAllValues(), vertecies[0].getAllValues());

        return clones;
    }

    @Override
    public void draw(Graphics2D g) {
        final Vertex[] vertecies = this.translate(this.vertecies);

        final int[] xPoints = new int[vertecies.length];
        final int[] yPoints = new int[vertecies.length];

        if (this.dotProduct < 0f) {
            for (int i = 0; i < vertecies.length; i++) {
                // final float[][] normalizedLight = Program.world.getLight().cloneAllValues();
                // MatrixMath.normalize(normalizedLight);

                // final float dp = MatrixMath.dotProduct(vertecies[0].getAllValues(), normalizedLight);

                // Projection
                VertexMath.multiply(vertecies[i].getAllValues(), Matrix.PROJECTION_MATRIX.getAllValues());

                vertecies[i].setX((vertecies[i].getX() + 1f) * (0.5f * (float)Program.GRAPHICS_DEVICE_BOUNDS.getWidth()));
                vertecies[i].setY((vertecies[i].getY() + 1f) * (0.5f * (float)Program.GRAPHICS_DEVICE_BOUNDS.getHeight()));

                xPoints[i] = (int)vertecies[i].getX();
                yPoints[i] = (int)vertecies[i].getY();
            }

            final int tmp = g.getColor().getRGB();

            g.setColor(Color.BLACK);
            g.drawPolygon(xPoints, yPoints, vertecies.length);

            g.setColor(Color.WHITE);
            g.fillPolygon(xPoints, yPoints, vertecies.length);

            g.setColor(new Color(tmp));
        }
    }

    @Override
    public void drawGL() {
        final Vertex[] vertecies = this.translate(this.vertecies);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

        GL11.glBegin(GL11.GL_TRIANGLES);
        if (this.dotProduct < 0f) {
            for (Vertex vertex : vertecies) {
                VertexMath.multiply(vertex.getAllValues(), Matrix.PROJECTION_MATRIX.getAllValues());

                vertex.setX((vertex.getX() + 1f) * (0.5f * (float)Program.GRAPHICS_DEVICE_BOUNDS.getWidth()));
                vertex.setY((vertex.getY() + 1f) * (0.5f * (float)Program.GRAPHICS_DEVICE_BOUNDS.getHeight()));

                GL11.glVertex2f(vertex.getX(), vertex.getY());
            }
        }
        GL11.glEnd();
    }
}
