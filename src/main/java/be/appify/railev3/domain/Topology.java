package be.appify.railev3.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Topology {
    private Map<String, Destination> destinations;

    public Topology(List<String> destinationNames) {
        destinations = analyze(destinationNames);
    }

    private Map<String, Destination> analyze(List<String> destinationNames) {
        Map<String, Destination> destinations = new HashMap<>();
        Destination previous = null;
        for (String destinationName : destinationNames) {
            Destination destination = destinations.get(destinationName);
            if (destination == null) {
                destination = new Destination(destinationName);
                destinations.put(destinationName, destination);
            }
            if(previous != null) {
                previous.link(destination);
            }
            previous = destination;
        }
        return destinations;
    }

    public static Builder createBuilder() {
        return new Builder();
    }

    public Destination at(String destinationName) {
        return destinations.get(destinationName);
    }

    public static class Builder {
        List<String> destinationNames = new ArrayList<>();

        public Builder add(String destinationName) {
            destinationNames.add(destinationName);
            return this;
        }

        public Topology build() {
            return new Topology(destinationNames);
        }
    }
}
