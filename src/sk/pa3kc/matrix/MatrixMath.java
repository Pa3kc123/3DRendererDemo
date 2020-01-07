package sk.pa3kc.matrix;

import sk.pa3kc.mylibrary.util.ArrayUtils;
import sk.pa3kc.mylibrary.util.StringUtils;

import static java.lang.StrictMath.cos;
import static java.lang.StrictMath.sin;

public class MatrixMath {
    private MatrixMath() {}

    public enum ArithmeticOperation {
        ADD,
        SUB,
        MULT,
        DIV
    }

    public static void add(float[][] mat, float number, float[][] out) {
        MatrixMath.calculate(mat, number, out, ArithmeticOperation.ADD);
    }
    public static void add(float[][] mat1, float[][] mat2, float[][] out) {
        MatrixMath.calculate(mat1, mat2, out, ArithmeticOperation.ADD);
    }
    public static void add(float[][] mat, float number) {
        MatrixMath.calculate(mat, number, ArithmeticOperation.ADD);
    }
    public static void add(float[][] mat1, float[][] mat2) {
        MatrixMath.calculate(mat1, mat2, ArithmeticOperation.ADD);
    }

    public static void subtract(float[][] mat, float number, float[][] out) {
        MatrixMath.calculate(mat, number, out, ArithmeticOperation.SUB);
    }
    public static void subtract(float[][] mat1, float[][] mat2, float[][] out) {
        MatrixMath.calculate(mat1, mat2, out, ArithmeticOperation.SUB);
    }
    public static void subtract(float[][] mat, float number) {
        MatrixMath.calculate(mat, number, ArithmeticOperation.SUB);
    }
    public static void subtract(float[][] mat1, float[][] mat2) {
        MatrixMath.calculate(mat1, mat2, ArithmeticOperation.SUB);
    }

    public static void multiply(float[][] mat, float number, float[][] out) {
        MatrixMath.calculate(mat, number, out, ArithmeticOperation.MULT);
    }
    public static void multiply(float[][] mat1, float[][] mat2, float[][] out) {
        MatrixMath.calculate(mat1, mat2, out, ArithmeticOperation.MULT);
    }
    public static void multiply(float[][] mat, float number) {
        MatrixMath.calculate(mat, number, ArithmeticOperation.MULT);
    }
    public static void multiply(float[][] mat1, float[][] mat2) {
        MatrixMath.calculate(mat1, mat2, ArithmeticOperation.MULT);
    }

    public static void divide(float[][] mat, float number, float[][] out) {
        MatrixMath.calculate(mat, number, out, ArithmeticOperation.DIV);
    }
    public static void divide(float[][] mat1, float[][] mat2, float[][] out) {
        MatrixMath.calculate(mat1, mat2, out, ArithmeticOperation.DIV);
    }
    public static void divide(float[][] mat, float number) {
        MatrixMath.calculate(mat, number, ArithmeticOperation.DIV);
    }
    public static void divide(float[][] mat1, float[][] mat2) {
        MatrixMath.calculate(mat1, mat2, ArithmeticOperation.DIV);
    }

    //region Private methods
    private static void calculate(float[][] mat, float number, float[][] out, ArithmeticOperation operation) {

        if (mat == null) {
            throw new NullPointerException("input cannot be null");
        }
        if (out == null) {
            throw new NullPointerException("output cannot be null");
        }
        if (out.length < mat.length || out[0].length < mat[0].length) {
            throw new IllegalArgumentException("out matrix is smaller than input matrix");
        }

        switch (operation) {
            case ADD:
            case SUB:
                if (number == 0d) return;
            break;
            case MULT:
                if (number == 1d) return;
                if (number == 0d) {
                    for (int row = 0; row < mat.length; row++)
                    for (int col = 0; col < mat[row].length; col++)
                        out[row][col] = 0;

                    return;
                }
            break;
            case DIV:
                if (number == 0d) throw new ArithmeticException("Cannot divide by zero");
                if (number == 1d) return;
            break;
        }

        for (int row = 0; row < mat.length; row++)
        for (int col = 0; col < mat[row].length; col++)
        switch (operation) {
            case ADD: out[row][col] = mat[row][col] + number; break;
            case SUB: out[row][col] = mat[row][col] - number; break;
            case MULT: out[row][col] = mat[row][col] * number; break;
            case DIV: out[row][col] = mat[row][col] / number; break;
        }
    }
    private static void calculate(float[][] mat, float number, ArithmeticOperation operation) {
        MatrixMath.calculate(ArrayUtils.deepArrCopy(mat), number, mat, operation);
    }
    private static void calculate(float[][] mat1, float[][] mat2, float[][] out, ArithmeticOperation operation) {
        if (mat1 == null) {
            throw new NullPointerException("first input cannot be null");
        }
        if (mat2 == null) {
            throw new NullPointerException("second input cannot be null");
        }
        if (out == null) {
            throw new NullPointerException("output cannot be null");
        }

        final float[][] m1;
        final float[][] m2;

        if (mat1[0].length == mat2.length) {
            m1 = mat1;
            m2 = mat2;
        } else if (mat1.length == mat2[0].length) {
            m1 = mat2;
            m2 = mat1;
        } else {
            final int m1RowCount = mat1.length;
            final int m1ColCount = mat1[0].length;
            final int m2RowCount = mat2.length;
            final int m2ColCount = mat2[0].length;

            String msg = StringUtils.build("Invalid matrix sizes (", m1RowCount, "x", m1ColCount, " <-> ", m2RowCount, "x", m2ColCount, ")");
            throw new IllegalArgumentException(msg);
        }

        float sum;
        for (int row = 0; row < m1.length; row++) {
            for (int col = 0; col < m2[0].length; col++) {
                sum = 0f;
                for (int i = 0; i < m1[0].length; i++) {
                    switch (operation) {
                        case ADD: sum += m1[row][i] + m2[i][col]; break;
                        case SUB: sum += m1[row][i] - m2[i][col]; break;
                        case MULT: sum += m1[row][i] * m2[i][col]; break;
                        case DIV:
                            if (m2[i][col] != 0d) {
                                sum += m1[row][i] / m2[i][col];
                            }
                        break;
                    }
                }
                out[row][col] = sum;
            }
        }
    }
    private static void calculate(float[][] mat1, float[][] mat2, ArithmeticOperation operation) {
        MatrixMath.calculate(ArrayUtils.deepArrCopy(mat1), mat2, mat1, operation);
    }

    public static void applyRotationX(float[][] out, float angrad) {
        if (out == null || out.length < 4 || out[0] == null || out[0].length < 4) {
            throw new IllegalArgumentException("Input matrix must be at least of size 4x4");
        }

        out[1][1] = (float) cos(angrad);
        out[1][2] = (float)-sin(angrad);
        out[2][1] = (float) sin(angrad);
        out[2][2] = (float) cos(angrad);
    }
    public static void applyRotationY(float[][] out, float angrad) {
        if (out == null || out.length < 4 || out[0] == null || out[0].length < 4) {
            throw new IllegalArgumentException("Input matrix must be at least of size 4x4");
        }

        out[0][0] = (float) cos(angrad);
        out[0][2] = (float) sin(angrad);
        out[2][0] = (float)-sin(angrad);
        out[2][2] = (float) cos(angrad);
    }
    public static void applyRotationZ(float[][] out, float angrad) {
        if (out == null || out.length < 4 || out[0] == null || out[0].length < 4) {
            throw new IllegalArgumentException("Input matrix must be at least of size 4x4");
        }

        out[0][0] = (float) cos(angrad);
        out[0][1] = (float)-sin(angrad);
        out[1][0] = (float) sin(angrad);
        out[1][1] = (float) cos(angrad);
    }

    /**
     * Return {@code float} value representing similarity of 2 matrixes
     * @param mat1 first matrix
     * @param mat2 second matrix
     * @return value representing similarity
     */
    public static float dotProduct(float[][] mat1, float[][] mat2) {
        if (mat1.length < 3 || mat1[0].length == 0) {
            throw new IllegalArgumentException("mat1 must be at least of size 3x1");
        }
        if (mat2.length < 3 || mat2[0].length == 0) {
            throw new IllegalArgumentException("mat2 must be at least of size 3x1");
        }

        return (mat1[0][0] * mat2[0][0]) + (mat1[1][0] * mat2[1][0]) + (mat1[2][0] * mat2[2][0]);
    }

    public static void crossProduct(float[][] mat1, float[][] mat2, float[][] out) {
        if (mat1.length < 3 || mat1[0].length < 1) {
            throw new IllegalArgumentException("mat1 must be at least of size 3x4");
        }
        if (mat2.length < 3 || mat2[0].length < 1) {
            throw new IllegalArgumentException("mat2 must be at least of size 3x4");
        }
        if (out.length < 3 || out[0].length < 1) {
            throw new IllegalArgumentException("out must be at least of size 3x4");
        }

        out[0][0] = (mat1[1][0] * mat2[2][0]) - (mat1[2][0] * mat2[1][0]);
        out[1][0] = (mat1[2][0] * mat2[0][0]) - (mat1[0][0] * mat2[2][0]);
        out[2][0] = (mat1[0][0] * mat2[1][0]) - (mat1[1][0] * mat2[0][0]);
    }

    public static void identify(float[][] mat) {
        for (int row = 0; row < mat.length; row++) {
            for (int col = 0; col < mat[0].length; col++) {
                mat[row][col] = row == col ? 1f : 0f;
            }
        }
    }

    public static void normalize(float[][] mat) {
        if (mat.length < 3 || mat[0].length == 0) {
            throw new IllegalArgumentException("mat must be at least of size 3x1");
        }

        final float l = (float)StrictMath.sqrt((mat[0][0] * mat[0][0]) + (mat[1][0] * mat[1][0]) + (mat[2][0] * mat[2][0]));
        mat[0][0] /= l;
        mat[1][0] /= l;
        mat[2][0] /= l;
    }
}
