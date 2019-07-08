package sk.pa3kc.util;

import sk.pa3kc.Program;
import sk.pa3kc.util.Matrix;

public class Vertex extends Matrix
{
    private double X;
    private double Y;
    private double Z;
    private double W;

    public Vertex()
    {
        super(4, 1);
        updateXYZ();
    }
    public Vertex(double x, double y, double z)
    {
        this(x, y, z, 1d);
    }
    public Vertex(double x, double y, double z, double w)
    {
        super(new double[][]
        {
            { x },
            { y },
            { z },
            { w }
        });
        updateXYZ();
    }
    public Vertex(Matrix mat)
    {
        super(mat.values);
        updateXYZ();
    }

    //region Getters
    public double getX() { return this.X; }
    public double getY() { return this.Y; }
    public double getZ() { return this.Z; }
    public double getW() { return this.W; }
    //endregion

    //region Public methods
    public double getLength()
    { return this.valid == true ? StrictMath.sqrt(createDotProduct(this, this)) : -1d; }
    //endregion

    //region Private functions
    public void updateXYZ()
    {
        this.X = super.rowCount > 0 ? super.values[0][0] : -1;
        this.Y = super.rowCount > 1 ? super.values[1][0] : -1;
        this.Z = super.rowCount > 2 ? super.values[2][0] : -1;
        this.W = super.rowCount > 3 ? super.values[3][0] : -1;
    }
    //endregion

    //region Public static functions
    public static void printVertexes(Vertex... vertexes)
    {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < vertexes.length; i++)
            builder.append(String.format("%d. Vertex = [ X = %+.2f | Y = %+.2f | Z = %+.2f | W = %.2f ]%n", i, vertexes[i].X, vertexes[i].Y, vertexes[i].Z, vertexes[i].W));
        builder.append(Program.NEWLINE);

        System.out.print(builder.toString());
    }
    //endregion

    //region Public static methods
    public static double createDotProduct(Vertex ver1, Vertex ver2)
    {
        ValidationResult validation = Matrix.validate(ver1, ver2);
        if (validation.valid == false)
            throw new RuntimeException("Matrix" + (validation.index + 1) + " is not valid");

        return (ver1.X * ver2.X) + (ver1.Y * ver2.Y) + (ver1.Z * ver2.Z);
    }
    public static Vertex createCrossProduct(Vertex ver1, Vertex ver2)
    {
        ValidationResult validation = Matrix.validate(ver1, ver2);
        if (validation.valid == false)
            throw new RuntimeException("Matrix" + (validation.index + 1) + " is not valid");

        double x = ver1.Y * ver2.Z - ver1.Z * ver2.Y;
		double y = ver1.Z * ver2.X - ver1.X * ver2.Z;
		double z = ver1.X * ver2.Y - ver1.Y * ver2.X;

        return new Vertex(x, y, z);
    }
    public static Vertex add(Matrix mat, double number) { return new Vertex(calculate(mat, number, ArithmeticOperation.ADD)); }
    public static Vertex add(Matrix mat1, Matrix mat2) { return new Vertex(calculate(mat1, mat2, ArithmeticOperation.ADD)); }
    public static Vertex subtract(Matrix mat, double number) { return new Vertex(calculate(mat, number, ArithmeticOperation.SUBTRACT)); }
    public static Vertex subtract(Matrix mat1, Matrix mat2) { return new Vertex(calculate(mat1, mat2, ArithmeticOperation.SUBTRACT)); }
    public static Vertex multiply(Matrix mat, double number) { return new Vertex(calculate(mat, number, ArithmeticOperation.MULTIPLY)); }
    public static Vertex multiply(Matrix mat1, Matrix mat2) { return new Vertex(calculate(mat1, mat2, ArithmeticOperation.MULTIPLY)); }
    public static Vertex divide(Matrix mat, double number) { return new Vertex(calculate(mat, number, ArithmeticOperation.DIVIDE)); }
    public static Vertex divide(Matrix mat1, Matrix mat2) { return new Vertex(calculate(mat1, mat2, ArithmeticOperation.DIVIDE)); }
    //endregion
}
