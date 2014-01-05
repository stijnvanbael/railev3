package be.appify.railev3;

import be.appify.lego.ev3.component.*;
import be.appify.lego.ev3.ui.Menu;
import be.appify.util.Function;
import be.appify.util.Lists;
import lejos.hardware.Sound;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;

import java.util.ArrayList;
import java.util.List;

public class RailEv3 {
    private List<String> allDestinations = new ArrayList<>();

    private List<String> destinations = new ArrayList<>();

    public static void main(String[] args) {
        new RailEv3();
    }

    public RailEv3() {
        try {
            mainMenu();
        } catch (Throwable e) {
            e.printStackTrace();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e1) {
                System.exit(0);
            }
        }
    }

    private void mainMenu() {
        Menu.<String>create()
                .header("Main menu:")
                .option("Discover topology", new Runnable() {
                    @Override
                    public void run() {
                        initializeDiscovery();
                    }
                })
                .option("Edit route", new Runnable() {
                    @Override
                    public void run() {
                        editRoute();
                    }
                })
                .option("Depart", new Runnable() {
                    @Override
                    public void run() {
                        depart();
                    }
                })
                .option("Exit", new Runnable() {
                    @Override
                    public void run() {
                        System.exit(0);
                    }
                })
                .showAt(0, 0);
    }

    private void initializeDiscovery() {
        Components.display().clear();
        ColorSensor colorSensor = Components.colorSensor(SensorPort.S3);
        colorSensor.colorMode();
        if (!colorSensor.isInitialized()) {
            Components.display().text("Initializing").showAt(0, 3);
            Components.display().text("Please wait...").showAt(2, 4);
            colorSensor.awaitInitialization();
        }
        colorSensor.onColorChanged(new Function<SensorColor, Void>() {
            @Override
            public Void apply(SensorColor color) {
                if (color != SensorColor.BLACK && color != SensorColor.UNKNOWN && color != SensorColor.WHITE) {
                    String colorName = color.name();
                    Sound.twoBeeps();
                    Components.light().color(LightColor.ORANGE).blink();
                    if (!allDestinations.contains(colorName)) {
                        allDestinations.add(colorName);
                    }
                }
                return null;
            }
        });
        discoveryMenu();
        colorSensor.off();
    }

    private void discoveryMenu() {
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

    private void endDiscovery(Motor motor) {
        stopEngine(motor);
    }

    private void reverseEngine(Motor motor) {
        motor.reverse();
    }

    private void stopEngine(Motor motor) {
        motor.stop();
    }

    private void startEngine(Motor motor) {
        motor.start();

    }

    private void editRoute() {
        final Menu<String> menu = Menu.<String>create()
                .header("Route:");
        menu.onRefresh(new Runnable() {
            @Override
            public void run() {
                menu.clear();
                Lists.forEach(destinations)
                        .apply(new Function<String, Void>() {
                            @Override
                            public Void apply(final String destination) {
                                menu.option(destination, new Runnable() {
                                    @Override
                                    public void run() {
                                        editDestination(destination);
                                    }
                                });
                                return null;
                            }
                        });
                if (!destinations.isEmpty())
                    menu.separator();
                if (destinations.size() < 5)
                    menu.option("Add destination", new Runnable() {
                        @Override
                        public void run() {
                            addDestination();
                        }
                    });
                menu.exitOption("Back");
            }
        });
        menu.showAt(0, 0);
    }

    private void editDestination(final String destination) {
        Menu.<String>create()
                .exitOption("Move up", new Runnable() {
                    @Override
                    public void run() {
                        moveDestinationUp(destination);
                    }
                })
                .exitOption("Move down", new Runnable() {
                    @Override
                    public void run() {
                        moveDestinationDown(destination);
                    }
                })
                .exitOption("Delete", new Runnable() {
                    @Override
                    public void run() {
                        deleteDestination(destination);
                    }
                })
                .exitOption("Cancel")
                .showAt(0, 0);
    }

    private void deleteDestination(String destination) {
        destinations.remove(destination);
    }

    private void moveDestinationDown(String destination) {
        int index = destinations.indexOf(destination);
        if (index < destinations.size()) {
            destinations.remove(destination);
            destinations.add(index + 1, destination);
        }
    }

    private void moveDestinationUp(String destination) {
        int index = destinations.indexOf(destination);
        if (index > 0) {
            destinations.remove(destination);
            destinations.add(index - 1, destination);
        }
    }

    private void depart() {
    }

    private void addDestination() {
        Menu<String> menu = Menu.<String>create()
                .header("Add destination:");
        for (final String destination : allDestinations) {
            menu.exitOption(destination, new Runnable() {
                @Override
                public void run() {
                    addDestination(destination);
                }
            });
        }
        menu.showAt(0, 0);
    }

    private void addDestination(String destination) {
        destinations.add(destination);
    }
}
