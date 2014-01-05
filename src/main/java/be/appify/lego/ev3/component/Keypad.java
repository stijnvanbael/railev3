package be.appify.lego.ev3.component;

import lejos.hardware.Button;

import java.util.HashMap;
import java.util.Map;

public class Keypad {

    private Map<Key, Runnable> actions = new HashMap<>();

    public Keypad onKeyDo(Key key, Runnable action) {
        actions.put(key, action);
        return this;
    }


    public Key awaitKey() {
        while (true) {
            int buttonId = Button.waitForAnyPress();
            for (Key key : actions.keySet()) {
                if (key.button.getId() == buttonId) {
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
