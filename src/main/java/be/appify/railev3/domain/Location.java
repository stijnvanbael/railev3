package be.appify.railev3.domain;

import java.util.HashMap;
import java.util.Map;

public class Location {
    private String name;
    private Map<Rotation, Location> neighbors = new HashMap<>();
    private Map<Rotation, Integer> distances = new HashMap<>();

    public Location(String name) {
        this.name = name;
    }

    public Location next(Rotation rotation) {
        return neighbors.get(rotation);
    }

    public String getName() {
        return name;
    }

    Location link(Location other, Rotation rotation, int distance) {
        if (neighbors.containsKey(rotation)) {
            if(other.isSwitchPoint()) {
                neighbors.put(rotation, other);
                distances.put(rotation, distance);
            } else if (neighbors.get(rotation) != other) {
                insertSwitchPoint(other, rotation, distance);
            }
        } else {
            neighbors.put(rotation, other);
            distances.put(rotation, distance);
            other.link(this, rotation.reverse(), distance);
        }
        return other;
    }

    private Location insertSwitchPoint(Location other, Rotation rotation, int distance) {
        Location left = (rotation == Rotation.CLOCKWISE ? other : neighbors.get(rotation));
        Location right = (rotation == Rotation.CLOCKWISE ? neighbors.get(rotation) : other);
        return new SwitchPoint(rotation, left, right, this, distance);
    }

    @Override
    public String toString() {
        return name;
    }

    public boolean isSwitchPoint() {
        return false;
    }

    public int distanceToNext(Rotation rotation) {
        return distances.containsKey(rotation) ? distances.get(rotation) : 0;
    }
}
