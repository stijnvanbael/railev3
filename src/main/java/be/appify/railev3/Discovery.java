package be.appify.railev3;

import be.appify.component.*;
import be.appify.railev3.domain.Topology;
import be.appify.ui.Menu;
import be.appify.util.Function;
import lejos.hardware.Sound;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;

import java.util.ArrayList;
import java.util.List;

public class Discovery {
    private List<String> allDestinations = new ArrayList<>();
    private Topology.Builder topologyBuilder;

    public Discovery(List<String> allDestinations) {
        this.allDestinations = allDestinations;
    }

    void initializeDiscovery() {
        allDestinations.clear();
        topologyBuilder = Topology.createBuilder();
        Components.display().clear();
        ColorSensor colorSensor = Components.colorSensor(SensorPort.S3);
        Components.display().text("Initializing").showAt(0, 3);
        Components.display().text("Please wait...").showAt(2, 4);
        colorSensor.colorMode();
        colorSensor.awaitActivation();
        colorSensor.onColorChanged(new Function<SensorColor, Void>() {
            @Override
            public Void apply(SensorColor color) {
                if (color != SensorColor.BLACK && color != SensorColor.UNKNOWN && color != SensorColor.WHITE) {
                    discovered(color);
                }
                return null;
            }
        });
        discoveryMenu();
        colorSensor.off();
    }

    private void discovered(SensorColor color) {
        String colorName = color.name();
        visualizeDiscovery(colorName);
        registerDiscovery(colorName);
    }

    private void registerDiscovery(String colorName) {
        if (!allDestinations.contains(colorName)) {
            allDestinations.add(colorName);
        }
        topologyBuilder.add(colorName);
    }

    private void visualizeDiscovery(String colorName) {
        Components.display().clearLine(7);
        Components.display().text("Detected: " + colorName).showAt(0, 7);
        Sound.twoBeeps();
        Components.light().color(LightColor.ORANGE).flash();
    }

    void discoveryMenu() {
        // /\
        // |O  BLUE
        // ||\
        // |OO GREEN YELLOW
        // ||/
        // |O  RED
        // \/
        Components.display().clear();
        final Motor motor = Components.motor(MotorPort.A);
        motor.speed(360);

        Menu.<String>create()
                .header("Discover topology:")
                .option("Start", new Runnable() {
                    @Override
                    public void run() {
                        startEngine(motor);
                    }
                })
                .option("Pause", new Runnable() {
                    @Override
                    public void run() {
                        stopEngine(motor);
                    }
                })
                .option("Reverse", new Runnable() {
                    @Override
                    public void run() {
                        reverseEngine(motor);
                    }
                })
                .exitOption("End", new Runnable() {
                    @Override
                    public void run() {
                        endDiscovery(motor);
                    }
                })
                .showAt(0, 0);
    }

    void endDiscovery(Motor motor) {
        stopEngine(motor);
    }

    void reverseEngine(Motor motor) {
        motor.reverse();
    }

    void stopEngine(Motor motor) {
        motor.stop();
    }

    void startEngine(Motor motor) {
        motor.start();

    }
}