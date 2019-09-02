package sk.pa3kc.matrix;

import sk.pa3kc.Program;
import sk.pa3kc.inter.Validatable;
import sk.pa3kc.mylibrary.util.ArrayUtils;
import sk.pa3kc.mylibrary.util.StringUtils;

public class Matrix implements Cloneable, Validatable {
    public final Object matrixLock = new Object();

    boolean valid;
    double[][] values;
    int rowCount;
    int colCount;
    boolean beingEdited;

    //region Constructors
    public Matrix(int rowCount, int colCount) {
        this(new double[rowCount][colCount]);
    }
    public Matrix(double[][] ref) {
        if (ref == null) return;

        this.values = ref;
        this.rowCount = values.length;
        this.colCount = values.length != 0 ? values[0].length : -1;
        this.valid = this.rowCount != 0 && this.colCount != 0;
    }
    //endregion

    //region Getters
    public double[][] getAllValues() { return this.values; }
    public double getValue(int row, int col) { return this.values[row][col]; }
    public int getRowCount() { return this.rowCount; }
    public int getColCount() { return this.colCount; }
    public boolean isBeingEdited() { return this.beingEdited; }
    public boolean isNotBeingEdited() { return !this.beingEdited; }
    //endregion

    //region Setters
    void setAllValues(double[][] ref) {
        if (ref == null) throw new NullPointerException("ref cannot be null");
        this.values = ref;
        this.rowCount = this.values.length;
        this.colCount = this.values.length != 0 ? this.values[0].length : -1;
        this.valid = this.rowCount != 0 && this.colCount != 0;
    }
    void setValueAt(int row, int col, double value) {
        if (row >= this.rowCount) throw new ArrayIndexOutOfBoundsException(StringUtils.build("row (", row, ") must be less than ", this.rowCount));
        if (col >= this.colCount) throw new ArrayIndexOutOfBoundsException(StringUtils.build("col (", col, ") must be less than ", this.colCount));

        this.values[row][col] = value;
    }
    void setBeingEdited(boolean state) { this.beingEdited = state; }
    //endregion

    //region Public methods
    public void waitForUnlock() {
        synchronized (this.matrixLock) {
            try {
                this.matrixLock.wait();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
    //endregion

    //region Overrides
    @Override
    public boolean isValid() { return this.valid; }
    @Override
    public boolean isNotValid() { return !this.valid; }
    @Override
    public Matrix clone() {
        if (!this.valid) throw new RuntimeException("Matrix is not valid");

        double[][] tmpValues = ArrayUtils.deepArrCopy(this.values);

        return new Matrix(tmpValues);
    }
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.rowCount);
        builder.append("x");
        builder.append(this.colCount);
        builder.append(" ");
        builder.append(this.valid ? "VALID" : "NOT VALID");
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
    public boolean equals(Object obj) {
        if (!(obj instanceof Matrix)) return false;

        Matrix ref = (Matrix)obj;

        return ref.valid == this.valid && ref.values.equals(this.values) && ref.colCount == this.colCount && ref.rowCount == this.rowCount;
    }
    //endregion
}
