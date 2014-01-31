package be.appify.railev3.domain;

import java.util.HashMap;
import java.util.Map;

public class Location {
    private String name;
    private Map<Rotation, Location> neighbors = new HashMap<>();

    public Location(String name) {
        this.name = name;
    }

    public Location next(Rotation rotation) {
        return neighbors.get(rotation);
    }

    public String getName() {
        return name;
    }

    Location link(Location other, Rotation rotation) {
        if (neighbors.containsKey(rotation)) {
            if(other.isSwitchPoint()) {
                neighbors.put(rotation, other);
            } else if (neighbors.get(rotation) != other) {
                insertSwitchPoint(other, rotation);
            }
        } else {
            neighbors.put(rotation, other);
            other.link(this, rotation.reverse());
        }
        return other;
    }

    private Location insertSwitchPoint(Location other, Rotation rotation) {
        Location left = (rotation == Rotation.CLOCKWISE ? other : neighbors.get(rotation));
        Location right = (rotation == Rotation.CLOCKWISE ? neighbors.get(rotation) : other);
        return new SwitchPoint(rotation, left, right, this);
    }

    @Override
    public String toString() {
        return name;
    }

    public boolean isSwitchPoint() {
        return false;
    }
}
