package be.appify.lego.ev3.ui;

import lejos.hardware.lcd.LCD;

public class Text {
    private final String text;
    private boolean invert = false;

    public Text(String text) {
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
