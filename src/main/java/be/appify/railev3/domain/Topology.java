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

        public Builder add(String locationName) {
            events.add(new LocationEvent(locationName));
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

        public LocationEvent(String locationName) {
            this.locationName = locationName;
        }

        @Override
        public void applyTo(TopologyResolution resolution) {
            Location location = resolution.getLocation(locationName);
            if (location == null) {
                location = new Location(locationName);
            }
            resolution.addLocation(location);
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

        public void addLocation(Location location) {
            if (location != previous) {
                locations.put(location.getName(), location);
                if (previous != null) {
                    previous = previous.link(location, rotation);
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

    @Override
    public String toString() {
        if(locations.isEmpty()) {
            return "EMPTY";
        }
        Location first = locations.values().iterator().next();
        Location current = first;
        StringBuilder builder = new StringBuilder();
        boolean done;
        do {
            builder.append(current.toString()).append("\n");
            current = current.next(Rotation.CLOCKWISE);
            done = current == null || current == first;
            if(!done) {
                builder.append("|\n");
            }
        } while (!done);
        String topologyAsString = builder.toString();
        if(current == first) {
            topologyAsString = addLoop(topologyAsString);
        }
        return topologyAsString;
    }

    private String addLoop(String topologyAsString) {
        List<String> lines = Arrays.asList(topologyAsString.split("\\\n"));
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < lines.size(); i++) {
            if(i == 0) {
                builder.append(" /");
            } else if(i == lines.size() - 1) {
                builder.append(" \\");
            } else {
                builder.append("| ");
            }
            builder.append(lines.get(i));
            builder.append("\n");
        }
        return builder.toString();
    }
}
