package be.appify.component;

public interface Keypad {

    Keypad onKeyDo(Key key, Runnable action);

    Key awaitKey();

    void awaitKey(Key key);
}
