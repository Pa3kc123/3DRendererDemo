package sk.pa3kc.geom;

import sk.pa3kc.util.Vertex;

public class Camera {
    private final Vertex position;

    public Camera(Vertex position) {
        this.position = position;
    }

    public Vertex getPos() { return this.position; }
}
