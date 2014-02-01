package be.appify.component;

import be.appify.util.Function;
import com.sun.xml.internal.bind.v2.model.runtime.RuntimeLeafInfo;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.Color;

import java.util.concurrent.CountDownLatch;

public class ColorSensor {
    private static final Mode<SensorColor> COLOR = new ColorMode();
    private EV3ColorSensor sensor;
    private Mode<SensorColor> mode;
    private Port port;
    private CountDownLatch active = new CountDownLatch(1);

    ColorSensor(Port port) {
        this.port = port;
    }

    public ColorSensor colorMode() {
        activateMode(COLOR);
        return this;
    }

    public boolean isActive() {
        return sensor != null && mode != null && mode.isActive();
    }

    public void awaitActivation() {
        if (active != null) {
            try {
                active.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void activateMode(Mode<SensorColor> mode) {
        this.mode = mode;
        new Thread(new Runnable() {
            @Override
            public void run() {
                initializeSensorIfNecessary();
                ColorSensor.this.mode.activate(sensor);
                active.countDown();
                active = new CountDownLatch(1);
            }
        }, "Color sensor: initialize").start();
    }

    private void initializeSensorIfNecessary() {
        if (sensor == null) {
            sensor = new EV3ColorSensor(port);
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

        boolean isActive();
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

        @Override
        public boolean isActive() {
            return active;
        }
    }
}
