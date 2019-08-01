package sk.pa3kc.geom;

import sk.pa3kc.matrix.Matrix;
import sk.pa3kc.matrix.MatrixEditor;
import sk.pa3kc.singletons.Matrixes;
import sk.pa3kc.util.Vertex;

public class Shape3D {

    private static final boolean APPLY_DISTANCE = false;
    private static final boolean APPLY_MULIPLICATION = false;

    private static final double distance = 1;

    private Shape3D() {}

    public static void transform(Vertex[] vertexes) {
        if (vertexes == null) return;

        MatrixEditor[] editors = new MatrixEditor[vertexes.length];
        for (int i = 0; i < vertexes.length; i++) {
            Vertex vertex = vertexes[i];
            if (vertex.isBeingEdited())
                vertex.waitForUnlock();
            editors[i] = new MatrixEditor(vertex);
        }

        if (Matrixes.rotationMatrix != null)
        for (MatrixEditor editor : editors)
            editor.multiply(Matrixes.rotationMatrix);

        if (APPLY_DISTANCE)
        for (int i = 0; i < editors.length; i++) {
            double myZ = 1d / (distance - vertexes[i].getZ());
            editors[i].multiply(new Matrix(new double[][] {
                { myZ, 0d, 0d, 0d },
                { 0d, myZ, 0d, 0d }
            }));
        }

        if (APPLY_MULIPLICATION)
        for (MatrixEditor editor : editors)
            editor.multiply(200);

        for (int i = 0; i < editors.length; i++) {
            editors[i].release();
            vertexes[i].updateXYZ();
        }
    }
}
