package sk.pa3kc.util;

import sk.pa3kc.matrix.Matrix;
import sk.pa3kc.matrix.MatrixEditor;

import static java.lang.StrictMath.cos;
import static java.lang.StrictMath.sin;

public class Schemas {
    private Schemas() {}

    public static boolean applyRotationX(Matrix mat, double angrad) {
        if (mat.isNotValid() || mat.getRowCount() < 4 || mat.getColCount() < 4) return false;
        if (mat.isBeingEdited())
            mat.waitForUnlock();

        MatrixEditor editor = new MatrixEditor(mat);
        editor.set(0, 0,  1d);
        editor.set(1, 1,  cos(angrad));
        editor.set(1, 2, -sin(angrad));
        editor.set(2, 1,  sin(angrad));
        editor.set(2, 2,  cos(angrad));
        editor.set(3, 3,  1d);
        editor.release();

        return true;
    }

    public static boolean applyRotationY(Matrix mat, double angrad) {
        if (mat.isNotValid() || mat.getRowCount() < 4 || mat.getColCount() < 4) return false;
        if (mat.isBeingEdited())
            mat.waitForUnlock();

        MatrixEditor editor = new MatrixEditor(mat);
        editor.set(0, 0,  cos(angrad));
        editor.set(0, 2,  sin(angrad));
        editor.set(1, 1,  1d);
        editor.set(2, 0, -sin(angrad));
        editor.set(2, 2,  cos(angrad));
        editor.set(3, 3,  1d);
        editor.release();

        return true;
    }

    public static boolean applyRotationZ(Matrix mat, double angrad) {
        if (mat.isNotValid() || mat.getRowCount() < 4 || mat.getColCount() < 4) return false;
        if (mat.isBeingEdited())
            mat.waitForUnlock();

        MatrixEditor editor = new MatrixEditor(mat);
        editor.set(0, 0,  cos(angrad));
        editor.set(0, 1, -sin(angrad));
        editor.set(1, 0,  sin(angrad));
        editor.set(1, 1,  cos(angrad));
        editor.set(2, 2,  1d);
        editor.set(3, 3,  1d);
        editor.release();

        return true;
    }

    public static Matrix createProjectionMatrix(double fovDeg, double aspectRatio, double distanceNear, double distanceFar) {
        double fovRad = 1.0d / StrictMath.tan(StrictMath.toRadians(fovDeg * 0.5d));
        Matrix matrix = new Matrix(4, 4);

        MatrixEditor editor = new MatrixEditor(matrix);
        editor.set(0, 0, aspectRatio * fovRad);
		editor.set(1, 1, fovRad);
		editor.set(2, 2, distanceFar / (distanceFar - distanceNear));
		editor.set(3, 2, (-distanceFar * distanceNear) / (distanceFar - distanceNear));
		editor.set(2, 3, 1.0d);
        editor.set(3, 3, 0.0d);
        editor.release();

        return matrix;
    }
}
