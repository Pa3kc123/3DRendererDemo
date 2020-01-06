package sk.pa3kc.matrix;

public class VertexMath {
    private VertexMath() {}

    public static void mult(float[][] mat1, float[][] mat2, float[][] out) {
        MatrixMath.multiply(mat1, mat2, out);

        if (out[3][0] != 0) {
            final float w = out[3][0];

            for (int i = 0; i < out.length - 1; i++) {
                out[i][0] /= w;
            }
        }
    }

}
