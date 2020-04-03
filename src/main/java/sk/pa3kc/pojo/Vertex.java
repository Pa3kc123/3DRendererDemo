package sk.pa3kc.pojo;

import sk.pa3kc.Program;
import sk.pa3kc.mylibrary.util.FloatMath;
import sk.pa3kc.mylibrary.util.NumberUtils;
import sk.pa3kc.mylibrary.util.StringUtils;

public class Vertex extends Matrix {
    public Vertex() {
        super(new float[4][1]);
    }
    public Vertex(float x, float y, float z) {
        this(x, y, z, 1f);
    }
    public Vertex(float x, float y, float z, float w) {
        super(new float[][] {
            { x },
            { y },
            { z },
            { w }
        });
    }
    public Vertex(float[][] mat) {
        super(mat);
    }

    //region Getters
    public float getX() { return super.values[0][0]; }
    public float getY() { return super.values[1][0]; }
    public float getZ() { return super.values[2][0]; }
    public float getW() { return super.values[3][0]; }
    //endregion

    //region Setters
    public void setX(float x) { super.values[0][0] = x; }
    public void setY(float y) { super.values[1][0] = y; }
    public void setZ(float z) { super.values[2][0] = z; }
    public void setW(float w) { super.values[3][0] = w; }
    //endregion

    //region Public functions
    public static void printVertexes(Vertex... vertexes) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < vertexes.length; i++) {
            final String x = StringUtils.build("X = ", NumberUtils.round(vertexes[i].getX(), 2));
            final String y = StringUtils.build("Y = ", NumberUtils.round(vertexes[i].getY(), 2));
            final String z = StringUtils.build("Z = ", NumberUtils.round(vertexes[i].getZ(), 2));
            final String w = StringUtils.build("W = ", NumberUtils.round(vertexes[i].getW(), 2));

            builder.append(StringUtils.build(i, ". Vertex = [ ", x, " | ", y, " | ", z, " | ", w, " ]", Program.NEWLINE));
        }

        builder.append(Program.NEWLINE);

        System.out.print(builder.toString());
    }
    public static float getLength(Vertex ver1, Vertex ver2) {
        final float x = ver1.getX() - ver2.getX();
        final float y = ver1.getY() - ver2.getY();
        final float z = ver1.getZ() - ver2.getZ();

        final Vertex tmp = new Vertex(x, y, z);
        return (float)StrictMath.sqrt((StrictMath.pow(tmp.getX(), 2) + StrictMath.pow(tmp.getY(), 2)));
    }
    public static Vertex createCrossProduct(Vertex ver1, Vertex ver2) {
        final float x = ver1.getY() * ver2.getZ() - ver1.getZ() * ver2.getY();
		final float y = ver1.getZ() * ver2.getX() - ver1.getX() * ver2.getZ();
		final float z = ver1.getX() * ver2.getY() - ver1.getY() * ver2.getX();

        return new Vertex(x, y, z);
    }
    //endregion

    //region Overrides
    @Override
    public Vertex clone() {
        return new Vertex(super.cloneAllValues());
    }
    //endregion
}
