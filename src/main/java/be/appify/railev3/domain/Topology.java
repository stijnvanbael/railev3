package be.appify.railev3.domain;

import be.appify.util.Lists;
import be.appify.util.Procedure;

import java.util.*;

public class Topology {
    private Map<String, Location> locations;

    public Topology(List<TopologyEvent> events) {
        locations = analyze(events);
    }

    private Map<String, Location> analyze(List<TopologyEvent> events) {
        final TopologyResolution resolution = new TopologyResolution();
        Lists.forEach(events).apply(new Procedure<TopologyEvent>() {
            @Override
            public void apply(TopologyEvent event) {
                event.applyTo(resolution);
            }
        });
        return resolution.getLocations();
    }

    public static Builder createBuilder() {
        return new Builder();
    }

    public Location at(String locationName) {
        return locations.get(locationName);
    }

    public static class Builder {
        List<TopologyEvent> events = new ArrayList<>();

        public Builder add(String locationName, int distance) {
            events.add(new LocationEvent(locationName, distance));
            return this;
        }

        public Topology build() {
            return new Topology(events);
        }

        public Builder reverse() {
            events.add(new ReverseEvent());
            return this;
        }

    }

    private static interface TopologyEvent {
        void applyTo(TopologyResolution resolution);
    }

    private static class LocationEvent implements TopologyEvent {
        private String locationName;
        private int distance;

        public LocationEvent(String locationName, int distance) {
            this.locationName = locationName;
            this.distance = distance;
        }

        @Override
        public void applyTo(TopologyResolution resolution) {
            Location location = resolution.getLocation(locationName);
            if (location == null) {
                location = new Location(locationName);
            }
            resolution.addLocation(location, distance);
        }
    }

    private static class ReverseEvent implements TopologyEvent {
        @Override
        public void applyTo(TopologyResolution resolution) {
            resolution.reverseDirection();
        }
    }

    private static class TopologyResolution {
        private Map<String, Location> locations = new LinkedHashMap<>();
        private Location previous;
        private Rotation rotation = Rotation.CLOCKWISE;

        public Location getLocation(String locationName) {
            return locations.get(locationName);
        }

        public void addLocation(Location location, int distance) {
            if (location != previous) {
                locations.put(location.getName(), location);
                if (previous != null) {
                    previous = previous.link(location, rotation, distance);
                } else {
                    previous = location;
                }
            }
        }

        public Map<String, Location> getLocations() {
            return locations;
        }

        public void reverseDirection() {
            rotation = rotation.reverse();
        }
    }
}
