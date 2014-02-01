package be.appify.railev3.domain;

import java.util.HashMap;
import java.util.Map;

public class SwitchPoint extends Location {
    private final Rotation rotation;
    private final Map<Direction, Location> switched;
    private final Location opposite;
    private Direction direction;

    public SwitchPoint(Rotation rotation, Location left, Location right, Location opposite) {
        super(rotation == Rotation.CLOCKWISE ? "|\\" : "|/");
        switched = new HashMap<>();
        this.rotation = rotation;
        switched.put(Direction.LEFT, left);
        switched.put(Direction.RIGHT, right);
        this.opposite = opposite;
        left.link(this, rotation.reverse());
        right.link(this, rotation.reverse());
        opposite.link(this, rotation);
        direction = Direction.LEFT;
    }

    public boolean isSwitchPoint() {
        return true;
    }

    @Override
    Location link(Location other, Rotation rotation) {
        // No more links possible
        return other;
    }

    @Override
    public Location next(Rotation rotation) {
        return next(rotation, direction);
    }

    public Location next(Rotation rotation, Direction direction) {
        return rotation == this.rotation ?
                switched.get(direction) :
                opposite;
    }

    public void throwLeft() {
        direction = Direction.LEFT;
    }

    public void throwRight() {
        direction = Direction.RIGHT;
    }

    public Rotation getRotation() {
        return rotation;
    }
}
