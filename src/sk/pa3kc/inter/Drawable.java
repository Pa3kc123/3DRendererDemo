package sk.pa3kc.inter;

import java.awt.Graphics;

import sk.pa3kc.util.Matrix;

public interface Drawable
{
    public void draw(Graphics g, Matrix rotationMatrix, double distance);
}
