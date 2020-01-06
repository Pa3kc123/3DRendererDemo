package sk.pa3kc.matrix;

import static java.lang.StrictMath.sin;

import sk.pa3kc.mylibrary.util.ArrayUtils;
import sk.pa3kc.mylibrary.util.StringUtils;

import static java.lang.StrictMath.cos;

public class MatrixMath {
    private MatrixMath() {}

    public enum ArithmeticOperation {
        ADD,
        SUBTRACT,
        MULTIPLY,
        DIVIDE
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
        MatrixMath.calculate(mat, number, out, ArithmeticOperation.SUBTRACT);
    }
    public static void subtract(float[][] mat1, float[][] mat2, float[][] out) {
        MatrixMath.calculate(mat1, mat2, out, ArithmeticOperation.SUBTRACT);
    }
    public static void subtract(float[][] mat, float number) {
        MatrixMath.calculate(mat, number, ArithmeticOperation.SUBTRACT);
    }
    public static void subtract(float[][] mat1, float[][] mat2) {
        MatrixMath.calculate(mat1, mat2, ArithmeticOperation.SUBTRACT);
    }

    public static void multiply(float[][] mat, float number, float[][] out) {
        MatrixMath.calculate(mat, number, out, ArithmeticOperation.MULTIPLY);
    }
    public static void multiply(float[][] mat1, float[][] mat2, float[][] out) {
        MatrixMath.calculate(mat1, mat2, out, ArithmeticOperation.MULTIPLY);
    }
    public static void multiply(float[][] mat, float number) {
        MatrixMath.calculate(mat, number, ArithmeticOperation.MULTIPLY);
    }
    public static void multiply(float[][] mat1, float[][] mat2) {
        MatrixMath.calculate(mat1, mat2, ArithmeticOperation.MULTIPLY);
    }

    public static void divide(float[][] mat, float number, float[][] out) {
        MatrixMath.calculate(mat, number, out, ArithmeticOperation.DIVIDE);
    }
    public static void divide(float[][] mat1, float[][] mat2, float[][] out) {
        MatrixMath.calculate(mat1, mat2, out, ArithmeticOperation.DIVIDE);
    }
    public static void divide(float[][] mat, float number) {
        MatrixMath.calculate(mat, number, ArithmeticOperation.DIVIDE);
    }
    public static void divide(float[][] mat1, float[][] mat2) {
        MatrixMath.calculate(mat1, mat2, ArithmeticOperation.DIVIDE);
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
            case SUBTRACT:
                if (number == 0d) return;
            break;
            case MULTIPLY:
                if (number == 1d) return;
                if (number == 0d) {
                    for (int row = 0; row < mat.length; row++)
                    for (int col = 0; col < mat[row].length; col++)
                        out[row][col] = 0;

                    return;
                }
            break;
            case DIVIDE:
                if (number == 0d) throw new ArithmeticException("Cannot divide by zero");
                if (number == 1d) return;
            break;
        }

        for (int row = 0; row < mat.length; row++)
        for (int col = 0; col < mat[row].length; col++)
        switch (operation) {
            case ADD: out[row][col] = mat[row][col] + number; break;
            case SUBTRACT: out[row][col] = mat[row][col] - number; break;
            case MULTIPLY: out[row][col] = mat[row][col] * number; break;
            case DIVIDE: out[row][col] = mat[row][col] / number; break;
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
        if (out.length < mat1.length || out[0].length < mat1[0].length) {
            throw new IllegalArgumentException("out matrix is smaller than first input matrix");
        }
        if (out.length < mat2.length || out[0].length < mat2[0].length) {
            throw new IllegalArgumentException("out matrix is smaller than second input matrix");
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

        // final float[][] result = new float[NumberUtils.min(m1.length, m2.length)][NumberUtils.min(m1[0].length, m2[0].length)];

        for (int row = 0; row < m1.length; row++)
        for (int col = 0; col < m2[0].length; col++)
        for (int i = 0; i < m1[0].length; i++)
        switch (operation) {
            case ADD: out[row][col] += m1[row][i] + m2[i][col]; break;
            case SUBTRACT: out[row][col] += m1[row][i] - m2[i][col]; break;
            case MULTIPLY: out[row][col] += m1[row][i] * m2[i][col]; break;
            case DIVIDE:
                if (m2[i][col] != 0d)
                    out[row][col] += m1[row][i] / m2[i][col];
            break;
        }
    }
    private static void calculate(float[][] mat1, float[][] mat2, ArithmeticOperation operation) {
        MatrixMath.calculate(ArrayUtils.deepArrCopy(mat1), mat2, mat1, operation);
    }

    public static void applyRotationX(float[][] mat, float[][] out, float angrad) {
        if (mat == null || mat.length < 4 || mat[0] == null || mat[0].length < 4) {
            throw new IllegalArgumentException("Input matrix must be at least of size 4x4");
        }
        if (out == null || out.length < 4 || out[0] == null || out[0].length < 4) {
            throw new IllegalArgumentException("Input matrix must be at least of size 4x4");
        }

        out[1][1] = (float) cos(angrad);
        out[1][2] = (float)-sin(angrad);
        out[2][1] = (float) sin(angrad);
        out[2][2] = (float) cos(angrad);
    }

    public static void applyRotationY(float[][] mat, float[][] out, float angrad) {
        if (mat == null || mat.length < 4 || mat[0] == null || mat[0].length < 4) {
            throw new IllegalArgumentException("Input matrix must be at least of size 4x4");
        }
        if (out == null || out.length < 4 || out[0] == null || out[0].length < 4) {
            throw new IllegalArgumentException("Input matrix must be at least of size 4x4");
        }

        out[0][0] = (float) cos(angrad);
        out[0][2] = (float) sin(angrad);
        out[2][0] = (float)-sin(angrad);
        out[2][2] = (float) cos(angrad);
    }

    public static void applyRotationZ(float[][] mat, float[][] out, float angrad) {
        if (mat == null || mat.length < 4 || mat[0] == null || mat[0].length < 4) {
            throw new IllegalArgumentException("Input matrix must be at least of size 4x4");
        }
        if (out == null || out.length < 4 || out[0] == null || out[0].length < 4) {
            throw new IllegalArgumentException("Input matrix must be at least of size 4x4");
        }

        out[0][0] = (float) cos(angrad);
        out[0][1] = (float)-sin(angrad);
        out[1][0] = (float) sin(angrad);
        out[1][1] = (float) cos(angrad);
    }
}
