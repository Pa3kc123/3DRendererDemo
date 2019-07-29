package sk.pa3kc.util;

import static java.lang.StrictMath.cos;
import static java.lang.StrictMath.sin;

import sk.pa3kc.pojo.Matrix;

public class Schemas {
    private Schemas() {}

    public static boolean applyRotationX(Matrix mat, double angrad) {
        if (mat.isNotValid() || mat.getRowCount() < 4 || mat.getColCount() < 4) return false;
        double[][] values = mat.getAllValues();

        values[0][0] =  1d;
        values[1][1] =  cos(angrad);
        values[1][2] = -sin(angrad);
        values[2][1] =  sin(angrad);
        values[2][2] =  cos(angrad);
        values[3][3] =  1d;

        return true;
    }

    public static boolean applyRotationY(Matrix mat, double angrad) {
        if (mat.isNotValid() || mat.getRowCount() < 4 || mat.getColCount() < 4) return false;
        double[][] values = mat.getAllValues();

        values[0][0] =  cos(angrad);
        values[0][2] =  sin(angrad);
        values[1][1] =  1d;
        values[2][0] = -sin(angrad);
        values[2][2] =  cos(angrad);
        values[3][3] =  1d;

        return true;
    }

    public static boolean applyRotationZ(Matrix mat, double angrad) {
        if (mat.isNotValid() || mat.getRowCount() < 4 || mat.getColCount() < 4) return false;
        double[][] values = mat.getAllValues();

        values[0][0] =  cos(angrad);
        values[0][1] = -sin(angrad);
        values[1][0] =  sin(angrad);
        values[1][1] =  cos(angrad);
        values[2][2] =  1d;
        values[3][3] =  1d;

        return true;
    }

    public static Matrix createProjectionMatrix(double fovDeg, double aspectRatio, double distanceNear, double distanceFar) {
        double fovRad = 1.0d / StrictMath.tan(StrictMath.toRadians(fovDeg * 0.5d));
        Matrix matrix = new Matrix(4, 4);
        double[][] values = matrix.getAllValues();

        values[0][0] = aspectRatio * fovRad;
		values[1][1] = fovRad;
		values[2][2] = distanceFar / (distanceFar - distanceNear);
		values[3][2] = (-distanceFar * distanceNear) / (distanceFar - distanceNear);
		values[2][3] = 1.0d;
		values[3][3] = 0.0d;

        return matrix;
    }
}
