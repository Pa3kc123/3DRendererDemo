package sk.pa3kc.pojo;

import sk.pa3kc.Program;
import sk.pa3kc.mylibrary.util.NumberUtils;

public class Matrix implements Cloneable {
    public static final Matrix X_MATRIX = Matrix.identified(4, 4);
    public static final Matrix Y_MATRIX = Matrix.identified(4, 4);
    public static final Matrix Z_MATRIX = Matrix.identified(4, 4);
    public static final Matrix ROTATION_MATRIX = Matrix.empty(4, 4);
    public static final Matrix PROJECTION_MATRIX = Matrix.empty(4, 4);

    protected float[][] values;
    private int rowCount;
    private int colCount;

    //region Constructors
    protected Matrix(float[][] ref) {
        this.values = ref;

        if (ref == null) {
            this.rowCount = this.colCount = 0;
        } else {
            this.rowCount = values.length;
            this.colCount = values.length != 0 ? values[0].length : -1;
        }
    }
    public static Matrix custom(float[][] mat) {
        return new Matrix(mat);
    }
    public static Matrix empty(int rowCount, int colCount) {
        return new Matrix(new float[rowCount][colCount]);
    }
    public static Matrix identified(int rowCount, int colCount) {
        final float[][] values = new float[rowCount][colCount];

        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < colCount; col++) {
                values[row][col] = row == col ? 1f : 0f;
            }
        }

        return new Matrix(values);
    }
    //endregion

    //region Getters
    public float[][] getAllValues() { return this.values; }
    public float[][] cloneAllValues() {
        final float[][] matrix = new float[this.rowCount][this.colCount];

        for (int row = 0; row < this.rowCount; row++) {
            for (int col = 0; col < this.colCount; col++) {
                matrix[row][col] = this.values[row][col];
            }
        }

        return matrix;
    }
    public float getValueAt(int row, int col) { return this.values[row][col]; }
    public int getRowCount() { return this.rowCount; }
    public int getColCount() { return this.colCount; }
    //endregion

    //region Setters
    public void setAllValues(float[][] ref) {
        this.values = ref;

        if (ref == null) {
            this.rowCount = this.colCount = 0;
        } else {
            this.rowCount = this.values.length;
            this.colCount = this.values.length != 0 ? this.values[0].length : -1;
        }
    }
    public void setValueAt(int row, int col, double value) {
        this.values[row][col] = (float)value;
    }
    public void setValueAt(int row, int col, float value) {
        this.values[row][col] = value;
    }
    //endregion

    //region Overrides
    @Override
    public Matrix clone() {
        return new Matrix(this.cloneAllValues());
    }
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.rowCount);
        builder.append('x');
        builder.append(this.colCount);
        builder.append(Program.NEWLINE);

        for (int row = 0; row < this.rowCount; row++) {
            for (int col = 0; col < this.colCount; col++) {
                builder.append(NumberUtils.round(this.values[row][col], 2));
                builder.append(" | ");
            }
            builder.replace(builder.length() - 3, builder.length(), Program.NEWLINE);
        }

        return builder.toString();
    }
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Matrix)) return false;

        final Matrix ref = (Matrix)obj;

        if (ref.colCount != this.colCount || ref.rowCount != this.rowCount) return false;
        for (int row = 0; row < this.rowCount; row++) {
            for (int col = 0; col < this.colCount; col++) {
                if (ref.values[row][col] != this.values[row][col]) {
                    return false;
                }
            }
        }

        return true;
    }
    //endregion
}
