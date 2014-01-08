package be.appify.lego.ev3;

import be.appify.component.Display;
import be.appify.ui.Text;
import lejos.hardware.lcd.LCD;

public class Ev3Display implements Display {
    public Text text(String text) {
        return new Ev3Text(text);
    }

    public void clear() {
        LCD.clear();
        LCD.refresh();
    }

    @Override
    public void clearLine(int y) {
        LCD.clear(y);
    }
}
