package sk.pa3kc.pojo;

public class Vector extends Vertex {
    /**
     * Length of vector
     */
    private float magnitude;
    /**
     * Direction of vector
     */
    private float[][] direction;
    /**
     * Represented as ARGB (#AARRGGBB)
     */
    private int color;

    public Vector() {
        super(new float[4][1]);
    }
    public Vector(float x, float y, float z) {
        this(x, y, z, 1f);
    }
    public Vector(float x, float y, float z, float w) {
        super(new float[][] {
            { x },
            { y },
            { z },
            { w }
        });
    }
    public Vector(float[][] mat) {
        super(mat);
    }

    public float getMagnitude() {
        return this.magnitude;
    }
}
