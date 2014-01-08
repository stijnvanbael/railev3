package be.appify.component;

import be.appify.lego.ev3.Ev3ComponentFactory;
import lejos.hardware.port.Port;

import java.util.HashMap;
import java.util.Map;

public class Components {
    private static ComponentFactory componentFactory = new Ev3ComponentFactory();
    private static Display display = componentFactory.createDisplay();
    private static Light light = componentFactory.createLight();
    private static Map<Port, Object> hardware = new HashMap<>();

    public static Display display() {
        return display;
    }

    public static Keypad keypad() {
        return componentFactory.createKeypad();
    }

    public static Light light() {
        return light;
    }

    public static ColorSensor colorSensor(Port port) {
        ColorSensor sensor = (ColorSensor) hardware.get(port);
        if(sensor == null) {
            sensor = new ColorSensor(port);
            hardware.put(port, sensor);
        }
        return sensor;
    }

    public static Motor motor(Port port) {
        Motor motor = (Motor) hardware.get(port);
        if(motor == null) {
            motor = new Motor(port);
            hardware.put(port, motor);
        }
        return motor;
    }

    public static void factory(ComponentFactory componentFactory) {
        Components.componentFactory = componentFactory;
    }
}
