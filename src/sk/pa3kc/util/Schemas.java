package sk.pa3kc.util;

import sk.pa3kc.matrix.Matrix;
import sk.pa3kc.matrix.MatrixEditor;

import static java.lang.StrictMath.cos;
import static java.lang.StrictMath.sin;

public class Schemas {
    private Schemas() {}

    private static final MatrixEditor editor = MatrixEditor.empty();

    public static boolean applyRotationX(Matrix mat, double angrad) {
        if (mat.isNotValid() || mat.getRowCount() < 4 || mat.getColCount() < 4) return false;
        if (mat.isBeingEdited())
            mat.waitForUnlock();

        editor.changeReference(mat);
        editor.setValueAt(1, 1,  cos(angrad));
        editor.setValueAt(1, 2, -sin(angrad));
        editor.setValueAt(2, 1,  sin(angrad));
        editor.setValueAt(2, 2,  cos(angrad));
        editor.release();

        return true;
    }

    public static boolean applyRotationY(Matrix mat, double angrad) {
        if (mat.isNotValid() || mat.getRowCount() < 4 || mat.getColCount() < 4) return false;
        if (mat.isBeingEdited())
            mat.waitForUnlock();

        editor.changeReference(mat);
        editor.setValueAt(0, 0,  cos(angrad));
        editor.setValueAt(0, 2,  sin(angrad));
        editor.setValueAt(2, 0, -sin(angrad));
        editor.setValueAt(2, 2,  cos(angrad));
        editor.release();

        return true;
    }

    public static boolean applyRotationZ(Matrix mat, double angrad) {
        if (mat.isNotValid() || mat.getRowCount() < 4 || mat.getColCount() < 4) return false;
        if (mat.isBeingEdited())
            mat.waitForUnlock();

        editor.changeReference(mat);
        editor.setValueAt(0, 0,  cos(angrad));
        editor.setValueAt(0, 1, -sin(angrad));
        editor.setValueAt(1, 0,  sin(angrad));
        editor.setValueAt(1, 1,  cos(angrad));
        editor.release();

        return true;
    }

    public static Matrix createProjectionMatrix(double fovDeg, double aspectRatio, double distanceNear, double distanceFar) {
        double fovRad = 1.0d / StrictMath.tan(StrictMath.toRadians(fovDeg * 0.5d));
        Matrix matrix = new Matrix(4, 4);

        editor.changeReference(matrix);
        editor.setValueAt(0, 0, aspectRatio * fovRad);
		editor.setValueAt(1, 1, fovRad);
		editor.setValueAt(2, 2, distanceFar / (distanceFar - distanceNear));
		editor.setValueAt(3, 2, (-distanceFar * distanceNear) / (distanceFar - distanceNear));
		editor.setValueAt(2, 3, 1.0d);
        editor.setValueAt(3, 3, 0.0d);
        editor.release();

        return matrix;
    }
}
