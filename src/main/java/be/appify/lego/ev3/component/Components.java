package be.appify.lego.ev3.component;

import lejos.hardware.port.Port;

import java.util.HashMap;
import java.util.Map;

public class Components {
    private static Display display = new Display();
    private static Light light = new Light();
    private static Map<Port, Object> sensors = new HashMap<>();

    public static Display display() {
        return display;
    }

    public static Keypad keypad() {
        return new Keypad();
    }

    public static Light light() {
        return light;
    }

    public static ColorSensor colorSensor(Port port) {
        ColorSensor sensor = (ColorSensor) sensors.get(port);
        if(sensor == null) {
            sensor = new ColorSensor(port);
            sensors.put(port, sensor);
        }
        return sensor;
    }
}
