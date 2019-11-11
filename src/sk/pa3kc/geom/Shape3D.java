package sk.pa3kc.geom;

import sk.pa3kc.matrix.MatrixEditor;
import sk.pa3kc.util.Vertex;

import static sk.pa3kc.singletons.Matrixes.*;

import sk.pa3kc.Program;

public class Shape3D {
    private Shape3D() {}

    private static final MatrixEditor editor = MatrixEditor.empty();

    public static void transform(Vertex[] vertexes) {
        if (vertexes == null) return;

        double myZ;
        for (Vertex vertex : vertexes) {
            if (vertex.isBeingEdited())
                vertex.waitForUnlock();

            myZ = 1d / (Program.mainFrame.distance - vertex.getZ());
            editor.changeReference(PROJECTION_MATRIX);
            editor.setValueAt(0, 0, myZ);
            editor.setValueAt(1, 1, myZ);
            editor.release();

            editor.changeReference(vertex);
            editor.multiply(ROTATION_MATRIX);
            editor.multiply(PROJECTION_MATRIX);
            editor.multiply(200d);
            editor.release();
            vertex.updateXYZ();
        }
    }
}
