package sk.pa3kc;

import java.awt.Graphics2D;
import java.util.ArrayList;

import sk.pa3kc.geom.Drawable;
import sk.pa3kc.pojo.Vertex;

public class World extends Drawable {
    private final ArrayList<Drawable> mesh = new ArrayList<Drawable>();

    private final Player[] players;
    private final Vertex light;

    public World(int maxPlayerCount) {
        this.players = new Player[maxPlayerCount];
        this.light = new Vertex(0f, 0f, -200f);
    }

    public ArrayList<Drawable> getMesh() {
        return this.mesh;
    }
    public Player[] getPlayers() {
        return this.players;
    }
    public Vertex getLight() {
        return this.light;
    }

    public void draw(Graphics2D g) {
        for (Drawable obj : this.mesh) {
            obj.draw(g);
        }
    }

    @Override
    public String toString() {
        return "Not the best idea... :D";
    }
}
