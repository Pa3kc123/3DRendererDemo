package sk.pa3kc;

import sk.pa3kc.enums.MovementDirection;
import sk.pa3kc.util.Vertex;

public class Player {
    private MovementDirection moveDirection = MovementDirection.NONE;
    private boolean isMoving = false;

    private final Vertex location;

    public Player(Vertex location) {
        this.location = location;
    }

    public MovementDirection getMoveDirection() { return this.moveDirection; }
    public boolean getIsMoving() { return this.isMoving; }
    public Vertex getLocation() { return this.location; }

    public void setDirection(MovementDirection direction) {
        this.moveDirection = direction;
        this.isMoving = this.moveDirection != MovementDirection.NONE;
    }
    public void setLocation(Vertex ver) {
        this.location.setX(ver.getX());
        this.location.setY(ver.getY());
        this.location.setZ(ver.getZ());
        this.location.setW(ver.getW());
    }
}
