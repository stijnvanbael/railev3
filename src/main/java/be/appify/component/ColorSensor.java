package be.appify.component;

import be.appify.util.Function;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.Color;

import java.util.concurrent.CountDownLatch;

public class ColorSensor {
    private static final Mode COLOR = new ColorMode();
    private EV3ColorSensor sensor;
    private Mode mode;
    private Port port;
    private Runnable onInitializeAction;
    private CountDownLatch initialized;

    ColorSensor(Port port) {
        this.port = port;
    }

    public ColorSensor colorMode() {
        activateMode(COLOR);
        return this;
    }

    public ColorSensor onInitialize(Runnable onInitializeAction) {
        this.onInitializeAction = onInitializeAction;
        return this;
    }

    public boolean isInitialized() {
        return sensor != null;
    }

    public void awaitInitialization() {
        if (initialized != null) {
            try {
                initialized.await();
            } catch (InterruptedException e) {
            }
        }
    }

    private void activateMode(Mode mode) {
        this.mode = mode;
        new Thread(new Runnable() {
            @Override
            public void run() {
                initializeSensorIfNecessary();
                ColorSensor.this.mode.activate(sensor);
            }
        }, "Color sensor: initialize").start();
    }

    private void initializeSensorIfNecessary() {
        if (sensor == null) {
            initialized = new CountDownLatch(1);
            sensor = new EV3ColorSensor(port);
            if (onInitializeAction != null) {
                onInitializeAction.run();
            }
            initialized.countDown();
        }
    }

    public ColorSensor off() {
        if (mode != null) {
            mode.desactivate();
        }
        if (sensor != null) {
            sensor.close();
        }
        sensor = null;
        return this;
    }

    public ColorSensor onColorChanged(Function<SensorColor, Void> onColorChangedAction) {
        if (!(this.mode instanceof ColorMode)) {
            throw new IllegalStateException("Can only listen to color changes in color mode, call colorMode() first.");
        }
        this.mode.onChangeAction(onColorChangedAction);
        return this;
    }

    private static interface Mode<R> {
        void activate(EV3ColorSensor sensor);

        void onChangeAction(Function<R, Void> onChangeAction);

        void desactivate();
    }

    private static class ColorMode implements Mode<SensorColor> {
        private boolean active;
        private Function<SensorColor, Void> onChangeAction;

        @Override
        public void activate(final EV3ColorSensor sensor) {
            active = true;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int previousColor = Color.NONE;
                    int color;
                    while (active) {
                        try {
                            color = sensor.getColorID();
                            if (color != previousColor && onChangeAction != null) {
                                onChangeAction.apply(SensorColor.fromInt(color));
                                previousColor = color;
                            }
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            active = false;
                        }
                    }
                }
            }, "Color sensor: color mode").start();
        }

        @Override
        public void onChangeAction(Function<SensorColor, Void> onChangeAction) {
            this.onChangeAction = onChangeAction;
        }

        @Override
        public void desactivate() {
            active = false;
        }
    }
}
