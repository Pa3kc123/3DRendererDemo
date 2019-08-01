package sk.pa3kc.util;

import sk.pa3kc.Program;
import sk.pa3kc.matrix.Matrix;
import sk.pa3kc.mylibrary.util.NumberUtils;
import sk.pa3kc.mylibrary.util.StringUtils;

public class Vertex extends Matrix {
    private double X;
    private double Y;
    private double Z;
    private double W;

    public Vertex() {
        super(4, 1);
        updateXYZ();
    }
    public Vertex(double x, double y, double z) {
        this(x, y, z, 1d);
    }
    public Vertex(double x, double y, double z, double w) {
        super(new double[][] {
            { x },
            { y },
            { z },
            { w }
        });
        updateXYZ();
    }
    public Vertex(Matrix mat) {
        super(mat.getAllValues());
        updateXYZ();
    }

    //region Getters
    public double getX() { return this.X; }
    public double getY() { return this.Y; }
    public double getZ() { return this.Z; }
    public double getW() { return this.W; }
    //endregion

    //region Public methods
    public double getLength() { return this.isValid() ? StrictMath.sqrt(createDotProduct(this, this)) : -1d; }
    //endregion

    //region Private functions
    public void updateXYZ() {
        this.X = super.getRowCount() > 0 ? super.getValue(0, 0) : -1;
        this.Y = super.getRowCount() > 1 ? super.getValue(1, 0) : -1;
        this.Z = super.getRowCount() > 2 ? super.getValue(2, 0) : -1;
        this.W = super.getRowCount() > 3 ? super.getValue(3, 0) : -1;
    }
    //endregion

    //region Public static functions
    public static void printVertexes(Vertex... vertexes) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < vertexes.length; i++) {
            String x = StringUtils.build("X = ", NumberUtils.round(vertexes[i].X, 2));
            String y = StringUtils.build("Y = ", NumberUtils.round(vertexes[i].Y, 2));
            String z = StringUtils.build("Z = ", NumberUtils.round(vertexes[i].Z, 2));
            String w = StringUtils.build("W = ", NumberUtils.round(vertexes[i].W, 2));
            builder.append(StringUtils.build(i, ". Vertex = [ ", x, " | ", y, " | ", z, " | ", w, " ]", Program.NEWLINE));
        }
        builder.append(Program.NEWLINE);

        System.out.print(builder.toString());
    }
    //endregion

    //region Public static methods
    public static double getLength(Vertex ver1, Vertex ver2) {
        if (ver1.isNotValid()) throw new RuntimeException("Vertex1 is not is not valid");
        if (ver2.isNotValid()) throw new RuntimeException("Vertex2 is not is not valid");

        Vertex tmp = new Vertex(ver1.getX() - ver2.getX(), ver1.getY() - ver2.getY(), ver1.getZ() - ver2.getZ());
        return Math.sqrt(StrictMath.pow(tmp.getX(), 2) + StrictMath.pow(tmp.getY(), 2));
    }
    public static double createDotProduct(Vertex ver1, Vertex ver2) {
        if (ver1.isNotValid()) throw new RuntimeException("Vertex1 is not is not valid");
        if (ver2.isNotValid()) throw new RuntimeException("Vertex2 is not is not valid");

        return (ver1.X * ver2.X) + (ver1.Y * ver2.Y) + (ver1.Z * ver2.Z);
    }
    public static Vertex createCrossProduct(Vertex ver1, Vertex ver2) {
        if (ver1.isNotValid()) throw new RuntimeException("Vertex1 is not is not valid");
        if (ver2.isNotValid()) throw new RuntimeException("Vertex2 is not is not valid");

        double x = ver1.Y * ver2.Z - ver1.Z * ver2.Y;
		double y = ver1.Z * ver2.X - ver1.X * ver2.Z;
		double z = ver1.X * ver2.Y - ver1.Y * ver2.X;

        return new Vertex(x, y, z);
    }
    //endregion
}
