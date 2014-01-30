package be.appify.railev3.domain;

import java.util.HashMap;
import java.util.Map;

public class Destination {
    private String name;
    private Map<Direction, Destination> neighbors = new HashMap<>();

    public Destination(String name) {
        this.name = name;
    }

    public Destination next(Direction direction) {
        return neighbors.get(direction);
    }

    public String getName() {
        return name;
    }

    void link(Destination previous) {
        neighbors.put(Direction.CLOCKWISE, previous);
        previous.neighbors.put(Direction.COUNTERCLOCKWISE, this);
    }
}
