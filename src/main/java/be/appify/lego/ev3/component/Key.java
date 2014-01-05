package be.appify.lego.ev3.component;

import lejos.hardware.Button;

public enum Key {
    UP(Button.UP),
    DOWN(Button.DOWN),
    LEFT(Button.LEFT),
    RIGHT(Button.RIGHT),
    ENTER(Button.ENTER),
    ESCAPE(Button.ESCAPE);

    Button button;

    private Key(Button button) {
        this.button = button;
    }
}
