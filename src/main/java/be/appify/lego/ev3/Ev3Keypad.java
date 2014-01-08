package be.appify.lego.ev3;

import be.appify.component.Key;
import be.appify.component.Keypad;
import lejos.hardware.Button;

import java.util.HashMap;
import java.util.Map;

public class Ev3Keypad implements Keypad {

    private static final Map<Key, Button> BUTTON_MAP = new HashMap<>();
    static {
        BUTTON_MAP.put(Key.DOWN, Button.DOWN);
        BUTTON_MAP.put(Key.UP, Button.UP);
        BUTTON_MAP.put(Key.LEFT, Button.LEFT);
        BUTTON_MAP.put(Key.RIGHT, Button.RIGHT);
        BUTTON_MAP.put(Key.ESCAPE, Button.ESCAPE);
        BUTTON_MAP.put(Key.ENTER, Button.ENTER);
    }

    private Map<Key, Runnable> actions = new HashMap<>();

    public Ev3Keypad onKeyDo(Key key, Runnable action) {
        actions.put(key, action);
        return this;
    }


    public Key awaitKey() {
        while (true) {
            int buttonId = Button.waitForAnyPress();
            for (Key key : actions.keySet()) {
                if (BUTTON_MAP.get(key).getId() == buttonId) {
                    actions.get(key).run();
                    return key;
                }
            }
        }
    }

    public void awaitKey(Key key) {
        onKeyDo(key, new Runnable() {
            @Override
            public void run() {
            }
        });
        awaitKey();
    }
}
