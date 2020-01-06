package sk.pa3kc;

import java.util.ArrayList;

import sk.pa3kc.inter.Drawable;
import sun.java2d.SunGraphics2D;

public class World {
    private final ArrayList<Drawable> mesh = new ArrayList<Drawable>();

    private final Player[] players;

    public World(int maxPlayerCount) {
        this.players = new Player[maxPlayerCount];
    }

    public Player[] getPlayers() {
        return this.players;
    }
    public ArrayList<Drawable> getMesh() {
        return this.mesh;
    }

    public void draw(SunGraphics2D g) {
        for (Drawable obj : this.mesh) {
            obj.draw(g);
        }
    }

    @Override
    public String toString() {
        return "Not the best idea... :D";
    }
}
