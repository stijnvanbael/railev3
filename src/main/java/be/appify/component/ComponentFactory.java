package be.appify.component;

public interface ComponentFactory {
    Display createDisplay();
    Light createLight();
    Keypad createKeypad();
}
