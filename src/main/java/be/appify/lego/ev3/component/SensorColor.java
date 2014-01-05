package be.appify.lego.ev3.component;


import lejos.robotics.Color;

public enum SensorColor {
    UNKNOWN(Color.NONE),
    RED(Color.RED),
    GREEN(Color.GREEN),
    BLUE(Color.BLUE),
    YELLOW(Color.YELLOW),
    WHITE(Color.WHITE),
    BLACK(Color.BLACK),
    BROWN(Color.BROWN);

    private int color;

    SensorColor(int color) {
        this.color = color;
    }

    public static SensorColor fromInt(int color) {
        for(SensorColor sensorColor : values()) {
            if(sensorColor.color == color) {
                return sensorColor;
            }
        }
        return SensorColor.UNKNOWN;
    }
}
