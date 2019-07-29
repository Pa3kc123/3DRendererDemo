package sk.pa3kc.util;

import sk.pa3kc.Program;
import sk.pa3kc.enums.ArithmeticOperation;
import sk.pa3kc.mylibrary.Universal;
import sk.pa3kc.mylibrary.util.ArrayUtils;
import sk.pa3kc.mylibrary.util.NumberUtils;
import sk.pa3kc.pojo.Matrix;
import sk.pa3kc.pojo.ValidationResult;

public class MatrixEditor {
    private final Matrix ref;

    public MatrixEditor(Matrix ref) { this.ref = ref; }

    public Matrix getReference() { return this.ref; }

    //region Public static functions
    public static void printMatrixes(Matrix... matrixes) {
        ValidationResult validation = validate(matrixes);
        if (validation.valid == false)
            throw new RuntimeException("Matrix " + (validation.index + 1) + " is not valid");

        int maxRowCount = 0;
        for (Matrix matrix : matrixes)
            maxRowCount = matrix.getRowCount() > maxRowCount ? matrix.getRowCount() : maxRowCount;

        StringBuilder builder = new StringBuilder();
        for (int row = 0; row < maxRowCount; row++) {
            builder.append("| ");
            for (Matrix matrix : matrixes) {
                if (row < matrix.getRowCount()) {
                    for (int col = 0; col < matrix.getColCount(); col++)  {
                        double val = NumberUtils.round(matrix.getValue(row, col), 2);
                        builder.append((val >= 0 ? "+" : "").concat(String.valueOf(val)));
                    }
                } else {
                    for (int col = 0; col < matrix.getColCount(); col++)
                        builder.append("-1.00");
                }
                builder.append("| ");
            }
            builder.replace(builder.length() - 2, builder.length(), "|" + Program.NEWLINE);
        }
        builder.append(Program.NEWLINE);

        System.out.print(builder.toString());
    }
    //endregion

    //region Public methods
    public void add(Matrix mat, double number) { this.calculate(mat, number, ArithmeticOperation.ADD); }
    public void add(Matrix mat1, Matrix mat2) { this.calculate(mat1, mat2, ArithmeticOperation.ADD); }
    public void subtract(Matrix mat, double number) { this.calculate(mat, number, ArithmeticOperation.SUBTRACT); }
    public void subtract(Matrix mat1, Matrix mat2) { this.calculate(mat1, mat2, ArithmeticOperation.SUBTRACT); }
    public void multiply(Matrix mat, double number) { this.calculate(mat, number, ArithmeticOperation.MULTIPLY); }
    public void multiply(Matrix mat1, Matrix mat2) { this.calculate(mat1, mat2, ArithmeticOperation.MULTIPLY); }
    public void divide(Matrix mat, double number) { this.calculate(mat, number, ArithmeticOperation.DIVIDE); }
    public void divide(Matrix mat1, Matrix mat2) { this.calculate(mat1, mat2, ArithmeticOperation.DIVIDE); }
    //endregion

    //region Package private static methods
    private void calculate(Matrix mat, double number, ArithmeticOperation operation) {
        switch (operation) {
            case ADD:
            case SUBTRACT:
                if (number == 0d) return mat.clone();
            break;
            case MULTIPLY:
                if (number == 0d) return new Matrix(mat.getRowCount(), mat.getColCount());
                if (number == 1d) return mat.clone();
            break;
            case DIVIDE:
                if (number == 0d) throw new ArithmeticException("Cannot divide by zero");
                if (number == 1d) return mat.clone();
            break;
        }

        if (mat.isNotValid()) throw new RuntimeException("Matrix is not valid");

        Matrix result = mat.clone();
        double[][] resultValues = result.getAllValues();

        for (int row = 0; row < mat.getRowCount(); row++)
        for (int col = 0; col < mat.getColCount(); col++)
        switch (operation) {
            case ADD: resultValues[row][col] += number; break;
            case SUBTRACT: resultValues[row][col] -= number; break;
            case MULTIPLY: resultValues[row][col] *= number; break;
            case DIVIDE: resultValues[row][col] /= number; break;
        }
    }
    private void calculate(Matrix mat1, Matrix mat2, ArithmeticOperation operation) {
        ValidationResult validation = validate(mat1, mat2);
        if (validation.valid == false)
            throw new RuntimeException("Matrix" + (validation.index + 1) + " is not valid");

        double[][] m1 = null;
        double[][] m2 = null;

        if (mat1.getColCount() == mat2.getRowCount()) {
            m1 = mat1.getAllValues();
            m2 = mat2.getAllValues();
        } else if (mat1.getRowCount() == mat2.getColCount()) {
            m1 = mat2.getAllValues();
            m2 = mat1.getAllValues();
        } else throw new IllegalArgumentException("Invalid matrix sizes (" + mat1.getRowCount() + "x" + mat1.getColCount() + " <-> " + mat2.getRowCount() + "x" + mat2.getColCount() + ")");

        boolean zeroOnly = ArrayUtils.compareAll(m2, 0);
        boolean oneOnly = ArrayUtils.compareAll(m2, 1);
        switch (operation) {
            case ADD:
            case SUBTRACT:
                if (zeroOnly == true) return new Matrix(ArrayUtils.deepArrCopy(m1));
            break;
            case MULTIPLY:
                if (zeroOnly == true) return new Matrix(m1.length, m1[0].length);
                if (oneOnly == true) return new Matrix(ArrayUtils.deepArrCopy(m1));
            break;
            case DIVIDE:
                if (zeroOnly == true) throw new ArithmeticException("Cannot divide by zero");
                if (oneOnly == true) return new Matrix(ArrayUtils.deepArrCopy(m1));
            break;
        }

        int m1RowCount = m1.length;
        int m1ColCount = m1[0].length;
        int m2RowCount = m2.length;
        int m2ColCount = m2[0].length;

        double[][] result = new double[Universal.min(m1RowCount, m2RowCount)][Universal.min(m1ColCount, m2ColCount)];

        for (int row = 0; row < m1RowCount; row++)
        for (int col = 0; col < m2ColCount; col++)
        for (int i = 0; i < m1ColCount; i++)
        switch (operation) {
            case ADD:
                result[row][col] += m1[row][i] + m2[i][col];
            break;
            case SUBTRACT:
                result[row][col] += m1[row][i] - m2[i][col];
            break;
            case MULTIPLY:
                result[row][col] += m1[row][i] * m2[i][col];
            break;
            case DIVIDE:
                if (m2[i][col] != 0d)
                    result[row][col] += m1[row][i] / m2[i][col];
            break;
        }
    }
    public static ValidationResult validate(Matrix... matrixes) {
        for (int i = 0; i < matrixes.length; i++)
            if (!matrixes[i].isValid()) return new ValidationResult(false, i);
        return new ValidationResult(true, -1);
    }
    //endregion
}
