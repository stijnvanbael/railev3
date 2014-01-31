package be.appify.railev3.domain;

import be.appify.util.Lazy;

public enum Rotation {
    CLOCKWISE(new Lazy<Rotation>() {
        @Override
        protected Rotation initialize() {
            return COUNTERCLOCKWISE;
        }
    }),
    COUNTERCLOCKWISE(new Lazy<Rotation>() {
        @Override
        protected Rotation initialize() {
            return CLOCKWISE;
        }
    });

    private final Lazy<Rotation> reversed;

    Rotation(Lazy<Rotation> reversed) {
        this.reversed = reversed;
    }

    public Rotation reverse() {
        return reversed.get();
    }
}
