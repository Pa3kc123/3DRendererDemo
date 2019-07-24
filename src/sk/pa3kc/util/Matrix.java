package sk.pa3kc.util;

import sk.pa3kc.Program;
import sk.pa3kc.mylibrary.Universal;
import sk.pa3kc.mylibrary.util.ArrayUtils;

public class Matrix implements Cloneable {
    boolean valid = false;
    double[][] values = null;
    int rowCount = -1;
    int colCount = -1;

    //region Constructors
    public Matrix(int rowCount, int colCount) {
        this(new double[rowCount][colCount]);
    }
    public Matrix(double[][] values) {
        if (values == null) return;

        this.rowCount = values.length;
        this.colCount = values.length != 0 ? values[0].length : -1;
        this.valid = this.rowCount != 0 && this.colCount != 0;

        if (this.valid == true) {
            this.values = new double[this.rowCount][this.colCount];

            for (int row = 0; row < this.rowCount; row++)
            for (int col = 0; col < this.colCount; col++)
                this.values[row][col] = values[row][col];
        }
    }
    //endregion

    //region Getters
    public boolean isValid() { return this.valid; }
    public double[][] getValues() { return this.values; }
    public int getRowCount() { return this.rowCount; }
    public int getColCount() { return this.colCount; }
    //endregion

    //region Public methods
    public boolean compareAll(double value) {
        for (int row = 0; row < this.rowCount; row++)
        for (int col = 0; col < this.colCount; col++)
        if (this.values[row][col] != value)
            return false;
        return true;
    }
    //endregion

    //region Public static functions
    public static void printMatrixes(Matrix... matrixes) {
        ValidationResult validation = validate(matrixes);
        if (validation.valid == false)
            throw new RuntimeException("Matrix " + (validation.index + 1) + " is not valid");

        int maxRowCount = 0;
        for (Matrix matrix : matrixes)
            maxRowCount = matrix.rowCount > maxRowCount ? matrix.rowCount : maxRowCount;

        StringBuilder builder = new StringBuilder();
        for (int row = 0; row < maxRowCount; row++) {
            builder.append("| ");
            for (Matrix matrix : matrixes) {
                if (row < matrix.rowCount) {
                    for (int col = 0; col < matrix.colCount; col++)
                        builder.append(String.format("%+.2f ", matrix.values[row][col]));
                } else {
                    for (int col = 0; col < matrix.colCount; col++)
                        builder.append(String.format("% .2f ", -1f));
                }
                builder.append("| ");
            }
            builder.replace(builder.length() - 2, builder.length(), "|" + Program.NEWLINE);
        }
        builder.append(Program.NEWLINE);

        System.out.print(builder.toString());
    }
    //endregion

    //region Public static methods
    public static Matrix add(Matrix mat, double number) { return calculate(mat, number, ArithmeticOperation.ADD); }
    public static Matrix add(Matrix mat1, Matrix mat2) { return calculate(mat1, mat2, ArithmeticOperation.ADD); }
    public static Matrix subtract(Matrix mat, double number) { return calculate(mat, number, ArithmeticOperation.SUBTRACT); }
    public static Matrix subtract(Matrix mat1, Matrix mat2) { return calculate(mat1, mat2, ArithmeticOperation.SUBTRACT); }
    public static Matrix multiply(Matrix mat, double number) { return calculate(mat, number, ArithmeticOperation.MULTIPLY); }
    public static Matrix multiply(Matrix mat1, Matrix mat2) { return calculate(mat1, mat2, ArithmeticOperation.MULTIPLY); }
    public static Matrix divide(Matrix mat, double number) { return calculate(mat, number, ArithmeticOperation.DIVIDE); }
    public static Matrix divide(Matrix mat1, Matrix mat2) { return calculate(mat1, mat2, ArithmeticOperation.DIVIDE); }
    public static Matrix newInstance(Matrix mat, double value) { return Editor.edit(mat.clone()).setToAll(value).reference; }
    //endregion

    //region Package private static methods
    static Matrix calculate(Matrix mat, double number, ArithmeticOperation operation) {
        switch (operation) {
            case ADD:
            case SUBTRACT:
                if (number == 0d) return mat.clone();
            break;
            case MULTIPLY:
                if (number == 0d) return new Matrix(mat.rowCount, mat.colCount);
                if (number == 1d) return mat.clone();
            break;
            case DIVIDE:
                if (number == 0d) throw new ArithmeticException("Cannot divide by zero");
                if (number == 1d) return mat.clone();
            break;
        }

        if (mat.valid == false) throw new RuntimeException("Matrix is not valid");

        Matrix result = mat.clone();

        for (int row = 0; row < mat.rowCount; row++)
        for (int col = 0; col < mat.colCount; col++)
        switch (operation) {
            case ADD: result.values[row][col] += number; break;
            case SUBTRACT: result.values[row][col] -= number; break;
            case MULTIPLY: result.values[row][col] *= number; break;
            case DIVIDE: result.values[row][col] /= number; break;
        }

        return result;
    }
    static Matrix calculate(Matrix mat1, Matrix mat2, ArithmeticOperation operation) {
        ValidationResult validation = validate(mat1, mat2);
        if (validation.valid == false)
            throw new RuntimeException("Matrix" + (validation.index + 1) + " is not valid");

        double[][] m1 = null;
        double[][] m2 = null;

        if (mat1.colCount == mat2.rowCount) {
            m1 = mat1.values;
            m2 = mat2.values;
        } else if (mat1.rowCount == mat2.colCount) {
            m1 = mat2.values;
            m2 = mat1.values;
        } else throw new IllegalArgumentException("Invalid matrix sizes (" + mat1.rowCount + "x" + mat1.colCount + " <-> " + mat2.rowCount + "x" + mat2.colCount + ")");

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

        return new Matrix(result);
    }
    static ValidationResult validate(Matrix... matrixes) {
        for (int i = 0; i < matrixes.length; i++)
            if (matrixes[i].valid == false) return new ValidationResult(false, i);
        return new ValidationResult(true, -1);
    }
    //endregion

    //region Overrides
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.rowCount);
        builder.append("x");
        builder.append(this.colCount);
        builder.append(" ");
        builder.append(this.valid == true ? "VALID" : "NOT VALID");
        builder.append(Program.NEWLINE);

        for (int row = 0; row < this.rowCount; row++) {
            builder.append("{");
            for (int col = 0; col < this.colCount; col++) {
                builder.append(" ");
                builder.append(this.values[row][col]);
                builder.append(",");
            }
            builder.replace(builder.length() - 1, builder.length(), " }");
            builder.append(Program.NEWLINE);
        }
        builder.append(Program.NEWLINE);

        return builder.toString();
    }

    @Override
    public Matrix clone() {
        return new Matrix(this.values);
    }
    //endregion

    //region Extras
    static class ValidationResult {
        public final int index;
        public final boolean valid;

        ValidationResult(boolean valid, int index) {
            this.valid = valid;
            this.index = index;
        }
    }
    enum ArithmeticOperation {
        ADD,
        SUBTRACT,
        MULTIPLY,
        DIVIDE
    }
    public static class Editor {
        private Matrix reference;

        private Editor(Matrix reference) { this.reference = reference; }

        public static Editor edit(Matrix ref) { return new Editor(ref); }

        public Editor clear() { return this.setToAll(0d); }
        public Editor indentify() {
            if (this.reference.valid == false) throw new RuntimeException("Matrix is not valid");

            for (int row = 0; row < this.reference.rowCount; row++)
            for (int col = 0; col < this.reference.colCount; col++)
                this.reference.values[row][col] = row == col ? 1d : 0d;

            return this;
        }
        public Editor setToAll(double number) {
            if (this.reference.valid == false) throw new RuntimeException("Matrix is not valid");

            for (int row = 0; row < this.reference.rowCount; row++)
            for (int col = 0; col < this.reference.colCount; col++)
                this.reference.values[row][col] = number;

            return this;
        }

        public Editor add(double number) { return calculate(number, ArithmeticOperation.ADD); }
        public Editor add(Matrix mat2) { return calculate(mat2, ArithmeticOperation.ADD); }
        public Editor subtract(double number) { return calculate(number, ArithmeticOperation.SUBTRACT); }
        public Editor subtract(Matrix mat2) { return calculate(mat2, ArithmeticOperation.SUBTRACT); }
        public Editor multiply(double number) { return calculate(number, ArithmeticOperation.MULTIPLY); }
        public Editor multiply(Matrix mat2) { return calculate(mat2, ArithmeticOperation.MULTIPLY); }
        public Editor divide(double number) { return calculate(number, ArithmeticOperation.DIVIDE); }
        public Editor divide(Matrix mat2) { return calculate(mat2, ArithmeticOperation.DIVIDE); }

        private Editor calculate(double number, ArithmeticOperation operation) {
            switch (operation) {
                case ADD:
                case SUBTRACT:
                    if (number == 0d) return this;
                break;
                case MULTIPLY:
                    if (number == 0d) return setToAll(0d);
                    if (number == 1d) return this;
                break;
                case DIVIDE:
                    if (number == 0d) throw new ArithmeticException("Cannot divide by zero");
                    if (number == 1d) return this;
                break;
            }

            if (this.reference.valid == false) throw new RuntimeException("Matrix is not valid");

            for (int row = 0; row < this.reference.rowCount; row++)
            for (int col = 0; col < this.reference.colCount; col++)
            switch (operation) {
                case ADD: this.reference.values[row][col] += number; break;
                case SUBTRACT: this.reference.values[row][col] -= number; break;
                case MULTIPLY: this.reference.values[row][col] *= number; break;
                case DIVIDE: this.reference.values[row][col] /= number; break;
            }

            return this;
        }
        private Editor calculate(Matrix mat, ArithmeticOperation operation) {
            if (mat.valid == false) throw new RuntimeException("Matrix 2 is not valid");

            double[][] m1 = null;
            double[][] m2 = null;

            if (this.reference.colCount == mat.rowCount) {
                m1 = ArrayUtils.deepArrCopy(this.reference.values);
                m2 = ArrayUtils.deepArrCopy(mat.values);
            } else if (this.reference.rowCount == mat.colCount) {
                m2 = ArrayUtils.deepArrCopy(this.reference.values);
                m1 = ArrayUtils.deepArrCopy(mat.values);
            } else throw new IllegalArgumentException("Invalid matrix sizes (" + this.reference.rowCount + "x" + this.reference.colCount + " <-> " + mat.rowCount + "x" + mat.colCount + ")");

            boolean zeroOnly = ArrayUtils.compareAll(m2, 0);
            boolean oneOnly = ArrayUtils.compareAll(m2, 1);
            switch (operation) {
                case ADD:
                case SUBTRACT:
                    if (zeroOnly == true) return this;
                break;
                case MULTIPLY:
                    if (zeroOnly == true) return setToAll(0d);
                    if (oneOnly == true) return this;
                break;
                case DIVIDE:
                    if (zeroOnly == true) throw new ArithmeticException("Cannot divide by zero");
                    if (oneOnly == true) return this;
                break;
            }

            Editor.edit(this.reference).clear();
            for (int row = 0; row < this.reference.rowCount; row++)
            for (int col = 0; col < this.reference.colCount; col++)
            for (int i = 0; i < this.reference.colCount; i++)
            switch (operation) {
                case ADD:
                    this.reference.values[row][col] += m1[row][i] + m2[i][col];
                break;
                case SUBTRACT:
                    this.reference.values[row][col] += m1[row][i] - m2[i][col];
                break;
                case MULTIPLY:
                    this.reference.values[row][col] += m1[row][i] * m2[i][col];
                break;
                case DIVIDE:
                    if (m2[i][col] != 0d)
                        this.reference.values[row][col] += m1[row][i] / m2[i][col];
                break;
            }

            return this;
        }
    }
    //endregion
}
