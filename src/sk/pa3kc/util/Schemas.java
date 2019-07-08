package sk.pa3kc.util;

import static java.lang.StrictMath.cos;
import static java.lang.StrictMath.sin;

public class Schemas
{
    private Schemas() {}

    public static boolean applyRotationX(Matrix mat, double angrad)
    {
        if (mat.valid == false || mat.rowCount < 4 || mat.colCount < 4) return false;
        mat.values[0][0] =  1d;
        mat.values[1][1] =  cos(angrad);
        mat.values[1][2] = -sin(angrad);
        mat.values[2][1] =  sin(angrad);
        mat.values[2][2] =  cos(angrad);
        mat.values[3][3] =  1d;
        return true;
    }

    public static boolean applyRotationY(Matrix mat, double angrad)
    {
        if (mat.valid == false || mat.rowCount < 4 || mat.colCount < 4) return false;
        mat.values[0][0] =  cos(angrad);
        mat.values[0][2] =  sin(angrad);
        mat.values[1][1] =  1d;
        mat.values[2][0] = -sin(angrad);
        mat.values[2][2] =  cos(angrad);
        mat.values[3][3] =  1d;
        return true;
    }

    public static boolean applyRotationZ(Matrix mat, double angrad)
    {
        if (mat.valid == false || mat.rowCount < 4 || mat.colCount < 4) return false;
        mat.values[0][0] =  cos(angrad);
        mat.values[0][1] = -sin(angrad);
        mat.values[1][0] =  sin(angrad);
        mat.values[1][1] =  cos(angrad);
        mat.values[2][2] =  1d;
        mat.values[3][3] =  1d;
        return true;
    }

    public static Matrix createProjectionMatrix(double fovDeg, double aspectRatio, double near, double far)
    {
        double fovRad = 1.0d / StrictMath.tan(StrictMath.toRadians(fovDeg * 0.5d));
		Matrix matrix = new Matrix(4, 4);
        matrix.values[0][0] = aspectRatio * fovRad;
		matrix.values[1][1] = fovRad;
		matrix.values[2][2] = far / (far - near);
		matrix.values[3][2] = (-far * near) / (far - near);
		matrix.values[2][3] = 1.0d;
		matrix.values[3][3] = 0.0d;
        return matrix;
    }
}
