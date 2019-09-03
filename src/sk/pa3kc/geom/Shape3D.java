package sk.pa3kc.geom;

import sk.pa3kc.matrix.MatrixEditor;
import sk.pa3kc.util.Vertex;

import static sk.pa3kc.singletons.Matrixes.*;

public class Shape3D {

    private static final MatrixEditor editor = MatrixEditor.empty();

    private Shape3D() {}

    public static void transform(Vertex[] vertexes) {
        if (vertexes == null) return;

        if (ROTATION_MATRIX != null)
        for (Vertex vertex : vertexes) {
            if (vertex.isBeingEdited())
                vertex.waitForUnlock();

            editor.changeReference(vertex).multiply(ROTATION_MATRIX);
            editor.release();
            vertex.updateXYZ();
        }

        /*for (int i = 0; i < editors.length; i++) {
            double myZ = 1d / (distance - vertexes[i].getZ());
            editors[i].multiply(new Matrix(new double[][] {
                { myZ, 0d, 0d, 0d },
                { 0d, myZ, 0d, 0d }
            }));
        }

        for (MatrixEditor editor : editors)
            editor.multiply(200);*/
    }
}
