package sk.pa3kc;

import sk.pa3kc.enums.MovementDirection;
import sk.pa3kc.util.Vertex;

public class Player {
    private static final Player INSTANCE = new Player();

    private Player() { }

    public static Player getInstance() { return INSTANCE; }

    private MovementDirection moveDirection = MovementDirection.NONE;
    private boolean isMoving = false;
    private Vertex location = new Vertex(0d, 0d, 0d, 1d);
    private Vertex lookDirection = new Vertex(0d, 0d, 0d, 1d);

    public MovementDirection getMoveDirection() { return this.moveDirection; }
    public boolean getIsMoving() { return this.isMoving; }
    public final Vertex getLocation() { return this.location; }

    public void setDirection(MovementDirection direction) {
        this.moveDirection = direction;
        this.isMoving = this.moveDirection != MovementDirection.NONE;
    }
    public void setLocation(Vertex location) {
        if (location.isValid() == false || location.getRowCount() < this.location.getRowCount() || location.getColCount() < this.location.getColCount())
            throw new IllegalArgumentException("location argument is not valid");

        for (int row = 0; row < this.location.getRowCount(); row++)
        for (int col = 0; col < this.location.getColCount(); col++)
            this.location.getValues()[row][col] = location.getValues()[row][col];
    }
}
