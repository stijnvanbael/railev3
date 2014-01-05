package be.appify.lego.ev3.component;

import be.appify.lego.ev3.ui.Text;
import lejos.hardware.lcd.LCD;

public class Display {

    public Text text(String text) {
        return new Text(text);
    }

    public void clear() {
        LCD.clear();
        LCD.refresh();
    }
}
