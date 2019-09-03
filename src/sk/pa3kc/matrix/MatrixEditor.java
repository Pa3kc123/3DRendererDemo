package sk.pa3kc.matrix;

import sk.pa3kc.Program;
import sk.pa3kc.enums.ArithmeticOperation;
import sk.pa3kc.mylibrary.util.NumberUtils;
import sk.pa3kc.mylibrary.util.StringUtils;
import sk.pa3kc.matrix.Matrix;

public class MatrixEditor {
    private Matrix ref;

    //region Constructors
    public MatrixEditor(Matrix ref) {
        if (ref.isBeingEdited()) throw new IllegalStateException("Referenced matrix is already been edited by another editor");
        this.ref = ref;
        this.ref.setBeingEdited(true);
    }
    public static MatrixEditor empty() { return new MatrixEditor(new Matrix(0, 0)); }
    //endregion

    //region Getters
    public Matrix getReference() { return this.ref; }
    //endregion

    //region Changers
    public MatrixEditor changeReference(Matrix ref) {
        if (ref == null) throw new NullPointerException("Reference cannot be null");
        if (ref.isBeingEdited()) throw new IllegalStateException("Referenced matrix is already been edited by another editor");
        this.ref = ref;
        this.ref.setBeingEdited(true);
        return this;
    }
    //endregion

    //region Public functions
    public static void printMatrixes(Matrix... matrixes) {
        for (int i = 0; i < matrixes.length; i++)
        if (matrixes[i].isNotValid())
            throw new RuntimeException(StringUtils.build("Matrix", i+1, "is not valid"));

        int maxRowCount = 0;
        for (Matrix matrix : matrixes)
            maxRowCount = NumberUtils.max(matrix.getRowCount(), maxRowCount);

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

        System.out.println(builder.toString());
    }
    //endregion

    //region Public methods
    public MatrixEditor add(double number) {
        this.calculate(number, ArithmeticOperation.ADD);
        return this;
    }
    public MatrixEditor add(Matrix mat) {
        this.calculate(mat, ArithmeticOperation.ADD);
        return this;
    }
    public MatrixEditor subtract(double number) {
        this.calculate(number, ArithmeticOperation.SUBTRACT);
        return this;
    }
    public MatrixEditor subtract(Matrix mat) {
        this.calculate(mat, ArithmeticOperation.SUBTRACT);
        return this;
    }
    public MatrixEditor multiply(double number) {
        this.calculate(number, ArithmeticOperation.MULTIPLY);
        return this;
    }
    public MatrixEditor multiply(Matrix mat) {
        this.calculate(mat, ArithmeticOperation.MULTIPLY);
        return this;
    }
    public MatrixEditor divide(double number) {
        this.calculate(number, ArithmeticOperation.DIVIDE);
        return this;
    }
    public MatrixEditor divide(Matrix mat) {
        this.calculate(mat, ArithmeticOperation.DIVIDE);
        return this;
    }
    public MatrixEditor identify() {
        for (int row = 0; row < this.ref.rowCount; row++)
        for (int col = 0; col < this.ref.colCount; col++)
            this.ref.values[row][col] = row == col ? 1d : 0d;

        return this;
    }
    public MatrixEditor setValueAt(int row, int col, double value) {
        if (row >= this.ref.rowCount) throw new ArrayIndexOutOfBoundsException(StringUtils.build("row (", row, ") must be less than ", this.ref.rowCount));
        if (col >= this.ref.colCount) throw new ArrayIndexOutOfBoundsException(StringUtils.build("col (", col, ") must be less than ", this.ref.colCount));

        this.ref.values[row][col] = value;

        return this;
    }
    public MatrixEditor replaceWith(double[][] ref) {
        if (ref.length == 0) throw new RuntimeException("reference has no rows");
        if (ref[0].length == 0) throw new RuntimeException("reference has no columns");

        this.ref.values = ref;

        return this;
    }
    //endregion

    //region Private methods
    private void calculate(double number, ArithmeticOperation operation) {
        if (this.ref.isNotValid()) throw new RuntimeException("Referenced matrix is not valid");

        final double[][] values = this.ref.getAllValues();

        switch (operation) {
            case ADD:
            case SUBTRACT:
                if (number == 0d) return;
            break;
            case MULTIPLY:
                if (number == 1d) return;
                if (number == 0d) {
                    for (int row = 0; row < values.length; row++)
                    for (int col = 0; col < values[row].length; col++)
                        values[row][col] = 0;

                    return;
                }
            break;
            case DIVIDE:
                if (number == 0d) throw new ArithmeticException("Cannot divide by zero");
                if (number == 1d) return;
            break;
        }

        for (int row = 0; row < values.length; row++)
        for (int col = 0; col < values[row].length; col++)
        switch (operation) {
            case ADD: values[row][col] += number; break;
            case SUBTRACT: values[row][col] -= number; break;
            case MULTIPLY: values[row][col] *= number; break;
            case DIVIDE: values[row][col] /= number; break;
        }
    }
    private void calculate(Matrix mat, ArithmeticOperation operation) {
        if (this.ref.isNotValid()) throw new RuntimeException("Referenced matrix is not valid");
        if (mat.isNotValid()) throw new RuntimeException("Matrix is not valid");

        final double[][] m1;
        final double[][] m2;

        if (this.ref.getColCount() == mat.getRowCount()) {
            m1 = this.ref.getAllValues();
            m2 = mat.getAllValues();
        } else if (this.ref.getRowCount() == mat.getColCount()) {
            m1 = mat.getAllValues();
            m2 = this.ref.getAllValues();
        } else {
            String msg = StringUtils.build("Invalid matrix sizes (", this.ref.getRowCount(), "x", this.ref.getColCount(), " <-> ", mat.getRowCount(), "x", mat.getColCount(), ")");
            throw new IllegalArgumentException(msg);
        }

        /*boolean zeroOnly = ArrayUtils.compareAll(m2, 0);
        boolean oneOnly = ArrayUtils.compareAll(m2, 1);
        switch (operation) {
            case ADD:
            case SUBTRACT:
                if (zeroOnly) return;
            break;
            case MULTIPLY:
                if (oneOnly) return;
                if (zeroOnly) {
                    for (int row = 0; row < m1.length; row++)
                    for (int col = 0; col < m1.length; col++)
                        this.ref.setAllValues(new double[m1.length][m1.length]);
                    return;
                }
            break;
            case DIVIDE:
                if (zeroOnly) throw new ArithmeticException("Cannot divide by zero");
                if (oneOnly) return;
            break;
        }*/

        final int m1RowCount = m1.length;
        final int m1ColCount = m1[0].length;
        final int m2RowCount = m2.length;
        final int m2ColCount = m2[0].length;

        final double[][] result = new double[NumberUtils.min(m1RowCount, m2RowCount)][NumberUtils.min(m1ColCount, m2ColCount)];

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

        this.ref.setAllValues(result);
    }
    public void release() {
        this.ref.setBeingEdited(false);
        this.ref.matrixLock.unlockAll();
        this.ref = null;
    }
    //endregion
}
