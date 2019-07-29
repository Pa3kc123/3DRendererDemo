package sk.pa3kc.geom;

import sk.pa3kc.singletons.Matrixes;
import sk.pa3kc.util.Matrix;
import sk.pa3kc.util.Vertex;

public class Shape3D {
    private static final boolean APPLY_DISTANCE = false;
    private static final boolean APPLY_MULIPLICATION = false;

    private Shape3D() {}

    public static void transform(Vertex[] vertexes) {
        if (vertexes == null) return;

        if (Matrixes.rotationMatrix != null)
        for (int i = 0; i < vertexes.length; i++)
            vertexes[i] = Vertex.multiply(vertexes[i], Matrixes.rotationMatrix);

        if (APPLY_DISTANCE == true)
        for (int i = 0; i < vertexes.length; i++) {
            double myZ = 1d / (distance - vertexes[i].getZ());
            vertexes[i] = Vertex.multiply(vertexes[i], new Matrix(new double[][] {
                { myZ, 0d, 0d, 0d },
                { 0d, myZ, 0d, 0d }
            }));
        }

        if (APPLY_MULIPLICATION == true)
        for (int i = 0; i < vertexes.length; i++) {
            vertexes[i] = Vertex.multiply(vertexes[i], 200);
            vertexes[i].updateXYZ();
        }
    }
}
