package be.appify.lego.ev3;

import be.appify.ui.Text;
import lejos.hardware.lcd.LCD;

public class Ev3Text implements Text {
    private final String text;
    private boolean invert = false;

    public Ev3Text(String text) {
        this.text = text;
    }

    public Text invert() {
        this.invert = true;
        return this;
    }

    public void showAt(int x, int y) {
        LCD.drawString(text, x, y, invert);
    }
}
