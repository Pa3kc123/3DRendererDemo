package sk.pa3kc.matrix;

import sk.pa3kc.Program;
import sk.pa3kc.enums.ArithmeticOperation;
import sk.pa3kc.mylibrary.util.ArrayUtils;
import sk.pa3kc.mylibrary.util.NumberUtils;
import sk.pa3kc.mylibrary.util.StringUtils;
import sk.pa3kc.matrix.Matrix;

public class MatrixEditor {
    private final Matrix ref;

    public MatrixEditor(Matrix ref) {
        if (ref.isBeingEdited()) throw new IllegalStateException("Referenced matrix is already been edited by another editor");
        this.ref = ref;
        this.ref.setBeingEdited(true);
    }

    //region Getters
    public Matrix getReference() { return this.ref; }
    //endregion

    //region Setters
    public void set(int row, int col, double value) { this.ref.setValueAt(row, col, value); }
    //endregion

    //region Public static functions
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
        builder.append(Program.NEWLINE);

        System.out.print(builder.toString());
    }
    //endregion

    //region Public methods
    public void add(double number) { this.calculate(number, ArithmeticOperation.ADD); }
    public void add(Matrix mat) { this.calculate(mat, ArithmeticOperation.ADD); }
    public void subtract(double number) { this.calculate(number, ArithmeticOperation.SUBTRACT); }
    public void subtract(Matrix mat) { this.calculate(mat, ArithmeticOperation.SUBTRACT); }
    public void multiply(double number) { this.calculate(number, ArithmeticOperation.MULTIPLY); }
    public void multiply(Matrix mat) { this.calculate(mat, ArithmeticOperation.MULTIPLY); }
    public void divide(double number) { this.calculate(number, ArithmeticOperation.DIVIDE); }
    public void divide(Matrix mat) { this.calculate(mat, ArithmeticOperation.DIVIDE); }
    public void identify() {
        for (int row = 0; row < this.ref.rowCount; row++)
        for (int col = 0; col < this.ref.colCount; col++)
            this.ref.values[row][col] = row == col ? 1d : 0d;
    }
    public void setValueAt(int row, int col, double value) {
        if (row >= this.ref.rowCount) throw new ArrayIndexOutOfBoundsException(StringUtils.build("row (", row, ") must be less than ", this.ref.rowCount));
        if (col >= this.ref.colCount) throw new ArrayIndexOutOfBoundsException(StringUtils.build("col (", col, ") must be less than ", this.ref.colCount));

        this.ref.values[row][col] = value;
    }
    public void replaceWith(double[][] ref) {
        if (ref.length == 0) throw new RuntimeException("referenced value has 0 rows");
        if (ref[0].length == 0) throw new RuntimeException("referenced value has 0 columns");

        this.ref.values = ref;
    }
    //endregion

    //region Package private methods
    private void calculate(double number, ArithmeticOperation operation) {
        if (this.ref.isNotValid()) throw new RuntimeException("Referenced matrix is not valid");

        double[][] values = this.ref.getAllValues();

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

        double[][] m1 = null;
        double[][] m2 = null;

        if (this.ref.getColCount() == mat.getRowCount()) {
            m1 = this.ref.getAllValues();
            m2 = mat.getAllValues();
        } else if (this.ref.getRowCount() == mat.getColCount()) {
            m1 = mat.getAllValues();
            m2 = this.ref.getAllValues();
        } else throw new IllegalArgumentException("Invalid matrix sizes (" + this.ref.getRowCount() + "x" + this.ref.getColCount() + " <-> " + mat.getRowCount() + "x" + mat.getColCount() + ")");

        boolean zeroOnly = ArrayUtils.compareAll(m2, 0);
        boolean oneOnly = ArrayUtils.compareAll(m2, 1);
        switch (operation) {
            case ADD:
            case SUBTRACT:
                if (zeroOnly == true) return;
            break;
            case MULTIPLY:
                if (oneOnly == true) return;
                if (zeroOnly == true) {
                    for (int row = 0; row < m1.length; row++)
                    for (int col = 0; col < m1.length; col++)
                        m1[row][col] = 0;
                    return;
                }
            break;
            case DIVIDE:
                if (zeroOnly == true) throw new ArithmeticException("Cannot divide by zero");
                if (oneOnly == true) return;
            break;
        }

        int m1RowCount = m1.length;
        int m1ColCount = m1[0].length;
        int m2RowCount = m2.length;
        int m2ColCount = m2[0].length;

        double[][] result = new double[NumberUtils.min(m1RowCount, m2RowCount)][NumberUtils.min(m1ColCount, m2ColCount)];

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
        synchronized (this.ref.matrixLock) {
            this.ref.matrixLock.notify();
        }
    }
    //endregion
}
