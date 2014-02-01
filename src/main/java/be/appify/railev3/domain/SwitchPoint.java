package be.appify.railev3.domain;

import java.util.HashMap;
import java.util.Map;

public class SwitchPoint extends Location {
    private final Rotation rotation;
    private final Map<Direction, Location> switched;
    private final Location opposite;
    private Direction direction;

    public SwitchPoint(Rotation rotation, Location left, Location right, Location opposite, int newDistance) {
        super(rotation == Rotation.CLOCKWISE ? "|\\" : "|/");
        switched = new HashMap<>();
        this.rotation = rotation;
        switched.put(Direction.LEFT, left);
        switched.put(Direction.RIGHT, right);
        this.opposite = opposite;
        int distanceToLeft = left.distanceToNext(rotation.reverse()) != 0 ? left.distanceToNext(rotation.reverse()) : newDistance;
        int distanceToRight = right.distanceToNext(rotation.reverse()) != 0 ? right.distanceToNext(rotation.reverse()) : newDistance;
        int distanceToOpposite = Math.min(distanceToLeft, distanceToRight) / 2;
        left.link(this, rotation.reverse(), distanceToLeft - distanceToOpposite);
        right.link(this, rotation.reverse(), distanceToRight - distanceToOpposite);
        opposite.link(this, rotation, distanceToOpposite);
        direction = Direction.LEFT;
    }

    public boolean isSwitchPoint() {
        return true;
    }

    @Override
    Location link(Location other, Rotation rotation, int distance) {
        // No more links possible
        return other;
    }

    @Override
    public Location next(Rotation rotation) {
        return next(rotation, direction);
    }

    public int distanceToNext(Rotation rotation) {
        return next(rotation).distanceToNext(rotation.reverse());
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
}
