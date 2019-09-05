package sk.pa3kc.inter;

import java.awt.Graphics;

import sk.pa3kc.util.Vertex;

public interface Drawable {
    Vertex[] getAll();
    Vertex[] cloneAll();
    void draw(Graphics g);
}
