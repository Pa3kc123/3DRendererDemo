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
    public final static int VERTEX_COUNT = 3;

    public final Vertex[] vertecies = new Vertex[VERTEX_COUNT];
    public final Color color;

    public float dotProduct = 0f;
    public Vertex crossProduct = new Vertex();

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
        MatrixMath.crossProduct(this.vertecies[0].getAllValues(), this.vertecies[1].getAllValues(), this.crossProduct.getAllValues());

        return clones;
    }

    @Override
    public void draw(Graphics2D g) {
        final Vertex[] vertecies = this.translate(this.vertecies);

        final int[] xPoints = new int[VERTEX_COUNT];
        final int[] yPoints = new int[VERTEX_COUNT];

        if (this.dotProduct < 0f) {
            for (int i = 0; i < VERTEX_COUNT; i++) {
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

            g.setColor(Color.WHITE);
            g.drawPolygon(xPoints, yPoints, vertecies.length);

            // g.setColor(Color.WHITE);
            // g.fillPolygon(xPoints, yPoints, vertecies.length);

            g.setColor(new Color(tmp));
        }
    }

    @Override
    public void drawGL() {
        final Vertex[] vertecies = this.translate(this.vertecies);

        //if (this.dotProduct < 0f) {
            GL11.glColor4f(1f, 1f, 1f, 1f);
            GL11.glBegin(GL11.GL_TRIANGLES);
//            GL11.glTranslatef(Program.GRAPHICS_DEVICE_BOUNDS.width / 2f, Program.GRAPHICS_DEVICE_BOUNDS.height / 2f, 0f);
            for (int i = 0; i < VERTEX_COUNT; i++) {
                // final float[][] normalizedLight = Program.world.getLight().cloneAllValues();
                // MatrixMath.normalize(normalizedLight);

                // final float dp = MatrixMath.dotProduct(vertecies[0].getAllValues(), normalizedLight);

                // Projection
                VertexMath.multiply(vertecies[i].getAllValues(), Matrix.PROJECTION_MATRIX.getAllValues());

                final float x = this.vertecies[i].getX();
                final float y = this.vertecies[i].getY();
                GL11.glVertex2f(x, y);
            }
            GL11.glEnd();
        //}
    }
}
