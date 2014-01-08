package be.appify.lego.ev3;

import be.appify.component.ComponentFactory;
import be.appify.component.Display;
import be.appify.component.Keypad;
import be.appify.component.Light;

public class Ev3ComponentFactory implements ComponentFactory {
    @Override
    public Display createDisplay() {
        return new Ev3Display();
    }

    @Override
    public Light createLight() {
        return new Ev3Light();
    }

    @Override
    public Keypad createKeypad() {
        return new Ev3Keypad();
    }
}
