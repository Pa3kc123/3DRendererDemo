package sk.pa3kc.pojo;

import sk.pa3kc.Program;
import sk.pa3kc.inter.Validatable;
import sk.pa3kc.mylibrary.util.ArrayUtils;

public class Matrix implements Cloneable, Validatable {
    private boolean valid = false;
    private double[][] values = null;
    private int rowCount = -1;
    private int colCount = -1;

    //region Constructors
    public Matrix(int rowCount, int colCount) {
        this(new double[rowCount][colCount]);
    }
    public Matrix(double[][] values) {
        if (values == null) return;

        this.rowCount = values.length;
        this.colCount = values.length != 0 ? values[0].length : -1;
        this.valid = this.rowCount != 0 && this.colCount != 0;

        if (this.valid) {
            this.values = new double[this.rowCount][this.colCount];

            for (int row = 0; row < this.rowCount; row++)
            for (int col = 0; col < this.colCount; col++)
                this.values[row][col] = values[row][col];
        }
    }
    //endregion

    //region Getters
    public double[][] getAllValues() { return this.values; }
    public double getValue(int row, int col) { return this.values[row][col]; }
    public int getRowCount() { return this.rowCount; }
    public int getColCount() { return this.colCount; }
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
