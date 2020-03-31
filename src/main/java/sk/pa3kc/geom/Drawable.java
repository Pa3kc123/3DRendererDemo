package sk.pa3kc.geom;

import java.awt.Graphics2D;

import sk.pa3kc.Program;
import sk.pa3kc.matrix.MatrixMath;
import sk.pa3kc.matrix.VertexMath;
import sk.pa3kc.pojo.Matrix;
import sk.pa3kc.pojo.Vertex;

public abstract class Drawable {
    protected final Vertex normal = new Vertex();

    public abstract void draw(Graphics2D g);

    protected Vertex[] translate(Vertex[] vertecies) {
        if (vertecies == null) return null;

        final Vertex[] clone = new Vertex[vertecies.length];
        for (int i = 0; i < clone.length; i++) {
            clone[i] = vertecies[i].clone();

            // Apply Rotation
            VertexMath.multiply(clone[i].getAllValues(), Matrix.ROTATION_MATRIX.getAllValues());

            clone[i].setZ(clone[i].getZ() + Program.world.getPlayers()[0].getLocation().getZ());
        }

        if (clone.length >= 3) {
            final float[][] line1 = new float[4][1];
            final float[][] line2 = new float[4][1];

            line1[0][0] = clone[1].getX() - clone[0].getX();
            line1[1][0] = clone[1].getY() - clone[0].getY();
            line1[2][0] = clone[1].getZ() - clone[0].getZ();
            line2[0][0] = clone[clone.length - 1].getX() - clone[0].getX();
            line2[1][0] = clone[clone.length - 1].getY() - clone[0].getY();
            line2[2][0] = clone[clone.length - 1].getZ() - clone[0].getZ();

            MatrixMath.identify(this.normal.getAllValues());
            MatrixMath.crossProduct(line1, line2, this.normal.getAllValues());
            MatrixMath.normalize(this.normal.getAllValues());
        }

        return clone;
    }
}
