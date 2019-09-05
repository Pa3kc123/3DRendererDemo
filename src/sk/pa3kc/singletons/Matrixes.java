package sk.pa3kc.singletons;

import sk.pa3kc.matrix.Matrix;

public class Matrixes {
    public static final Matrix X_MATRIX = Matrix.newIdentifiedMatrix(4, 4);
    public static final Matrix Y_MATRIX = Matrix.newIdentifiedMatrix(4, 4);
    public static final Matrix Z_MATRIX = Matrix.newIdentifiedMatrix(4, 4);
    public static final Matrix ROTATION_MATRIX = new Matrix(4, 4);
    public static final Matrix PROJECTION_MATRIX = new Matrix(2, 4);
}
